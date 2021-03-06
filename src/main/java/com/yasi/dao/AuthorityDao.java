package com.yasi.dao;

import com.yasi.vo.Authority;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author wangzi
 * @date 17/10/19 下午11:25.
 */
@Repository
public interface AuthorityDao {
    /**
     * find Authority by pojo
     * @param vo Authority
     * @return
     */
    List<Authority> findAuthorityByPojo(Authority vo);
}
