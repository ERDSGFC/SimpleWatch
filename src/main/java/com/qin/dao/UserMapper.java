package com.qin.dao;

import com.qin.pojo.po.UserPO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    UserPO findOneById(@Param("primaryKey") long primaryKey);
}
