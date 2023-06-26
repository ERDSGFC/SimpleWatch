package com.qin.dao;

import com.qin.common.dao.BaseDao;
import com.qin.pojo.po.UserPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace()
//@CacheNamespaceRef
public interface UserMapper extends BaseDao<UserPO> {

    @Select("select * from `user` where id = #{primaryKey}")
    UserPO findOneById(@Param("primaryKey") long primaryKey);
}
