package com.yasi.web;

import com.common.base.BaseController;
import com.common.util.DateUtil;
import com.common.util.StringUtil;
import com.yasi.dto.NineSkyDataGetDto;
import com.yasi.dto.NineSkyDataGetResDto;
import com.yasi.dto.NineSkyResData;
import com.yasi.model.NineSkyData;
import com.yasi.service.NineSkyDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangzi
 * @date 18/12/18 上午11:14.
 */
@RestController
@Scope("prototype")
@RequestMapping("NineSkyController/")
public class NineSkyController extends BaseController {
    private static Logger logger = Logger.getLogger(NineSkyController.class);

    @Autowired
    private NineSkyDataService service;

    @RequestMapping(value = "insert.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public NineSkyResData insert(@RequestBody NineSkyData nineSkyData) {
        logger.info("CustomerController[]insert[]进入方法");
        NineSkyResData nineSkyResData = new NineSkyResData();
        nineSkyResData.setResult(1);
        nineSkyResData.setMessage("");
        nineSkyResData.setUploadPeriod(30);
        nineSkyResData.setResultTime("" + DateUtil.getCurDateStrMiao_());

        if (StringUtil.isEmpty(nineSkyData.getDateTime()) || nineSkyData.getDateTime().length() != 19) {
            nineSkyResData.setMessage("dateTime is invalid");
            logger.error("CustomerController[]insert[]上传时间不合法:" + nineSkyData.getDateTime());
            return nineSkyResData;
        }

        if (StringUtil.isEmpty(nineSkyData.getInstrumentId())) {
            nineSkyResData.setMessage("instrumentId is invalid");
            logger.error("CustomerController[]insert[]上传设备id不合法:" + nineSkyData.getInstrumentId());
            return nineSkyResData;
        }

        if (StringUtil.isEmpty(nineSkyData.getLocation())) {
            nineSkyResData.setMessage("location is invalid");
            logger.error("CustomerController[]insert[]上传位置不合法:" + nineSkyData.getLocation());
            return nineSkyResData;
        }

        if (nineSkyData.getConductivity() == null) {
            nineSkyResData.setMessage("conductivity is invalid");
            logger.error("CustomerController[]insert[]上传导电性不合法:" + nineSkyData.getConductivity());
            return nineSkyResData;
        }

        if (nineSkyData.getDissolvedOxygen() == null) {
            nineSkyResData.setMessage("dissolveOxygen is invalid");
            logger.error("CustomerController[]insert[]上传溶解氧不合法:" + nineSkyData.getDissolvedOxygen());
            return nineSkyResData;
        }

        if (nineSkyData.getFlow() == null) {
            nineSkyResData.setMessage("flow is invalid");
            logger.error("CustomerController[]insert[]上传巴歇尔槽明渠流量流量不合法:" + nineSkyData.getFlow());
            return nineSkyResData;
        }

        if (nineSkyData.getNtu() == null) {
            nineSkyResData.setMessage("ntu is invalid");
            logger.error("CustomerController[]insert[]上传浊度不合法:" + nineSkyData.getNtu());
            return nineSkyResData;
        }

        if (nineSkyData.getPh() == null) {
            nineSkyResData.setMessage("ph is invalid");
            logger.error("CustomerController[]insert[]上传ph不合法:" + nineSkyData.getPh());
            return nineSkyResData;
        }

        if (nineSkyData.getTemperature() == null) {
            nineSkyResData.setMessage("temperature is invalid");
            logger.error("CustomerController[]insert[]上传ph不合法:" + nineSkyData.getTemperature());
            return nineSkyResData;
        }

        if (nineSkyData.getDoState() == null) {
            nineSkyResData.setMessage("doState is invalid");
            logger.error("CustomerController[]insert[]上传溶解氧传感器电源状态位不合法:" + nineSkyData.getDoState());
            return nineSkyResData;
        }

        if (nineSkyData.getCtState() == null) {
            nineSkyResData.setMessage("ctState is invalid");
            logger.error("CustomerController[]insert[]上传电导率、温度传感器电源状态位不合法:" + nineSkyData.getCtState());
            return nineSkyResData;
        }

        if (nineSkyData.getFlState() == null) {
            nineSkyResData.setMessage("flState is invalid");
            logger.error("CustomerController[]insert[]上传流量传感器电源状态位不合法:" + nineSkyData.getFlState());
            return nineSkyResData;
        }

        if (nineSkyData.getNtuState() == null) {
            nineSkyResData.setMessage("ntuState is invalid");
            logger.error("CustomerController[]insert[]上传浊度传感器电源状态位不合法:" + nineSkyData.getNtuState());
            return nineSkyResData;
        }

        if (nineSkyData.getPhState() == null) {
            nineSkyResData.setMessage("phState is invalid");
            logger.error("CustomerController[]insert[]上传ph传感器电源状态位不合法:" + nineSkyData.getPhState());
            return nineSkyResData;
        }

        NineSkyResData resData = service.insert(nineSkyData);
        logger.info("CustomerController[]insert[]调用service层结束 resdata:" + resData);
        //判断查询结果
        if (null == resData) {
            nineSkyResData.setResult(1);
            nineSkyResData.setMessage("invoke service fail");
            nineSkyResData.setUploadPeriod(30);
            nineSkyResData.setResultTime("" + DateUtil.getCurDateStrMiao_());
            return nineSkyResData;
        }
        logger.info("CustomerController[]insert[]插入数据成功");
        //返回正常插入结果
        return resData;
    }

    /**
     * 根据日期和设备id查询久天数据
     *
     * @return
     */
    @RequestMapping("findByTime.do")
    public Object findByTime() {
        NineSkyDataGetResDto resDto = new NineSkyDataGetResDto();
        try {
            //校验参数合法性
            String instrumentId = request.getParameter("instrumentId");
            String start = request.getParameter("start");
            String end = request.getParameter("end");

            if (StringUtils.isEmpty(instrumentId) || !"L01".equals(instrumentId)) {
                logger.error("findByTime[]instrumentId参数不合法:" + instrumentId);
                resDto.setMsg("设备id参数不合法");
                resDto.setResult(1);
                resDto.setList(null);
                return resDto;
            }

            if (StringUtils.isEmpty(start) || start.length() != 19) {
                logger.error("findByTime[]start参数不合法:" + start);
                resDto.setMsg("start参数不合法");
                resDto.setResult(1);
                resDto.setList(null);
                return resDto;
            }

            if (StringUtils.isEmpty(end) || end.length() != 19) {
                logger.error("findByTime[]end参数不合法:" + end);
                resDto.setMsg("end参数不合法");
                resDto.setResult(1);
                resDto.setList(null);
                return resDto;
            }

            NineSkyDataGetDto dto = new NineSkyDataGetDto();
            dto.setInstrumentId(instrumentId);
            dto.setStart(start);
            dto.setEnd(end);
            //组装dto
            resDto = service.findByTime(dto);
            return resDto;
        } catch (Exception e) {
            logger.error("findByTime[]调用服务层:" + e.getMessage());
            resDto.setMsg("内部错误");
            resDto.setResult(1);
            resDto.setList(null);
            return resDto;
        }
    }
}
