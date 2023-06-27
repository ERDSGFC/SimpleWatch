package com.qin.dao;

import com.qin.common.TableInfo;
import com.qin.common.dao.BaseMapper;
import com.qin.pojo.po.UserPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@CacheNamespace()
@TableInfo(value = "user", fields = {"userName", "userPassword"})
//@CacheNamespaceRef
public interface UserMapper extends BaseMapper<UserPO> {

}
