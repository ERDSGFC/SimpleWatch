package com.qin.common.dao;

import org.apache.ibatis.annotations.Param;


public interface BaseDao<T> {
    /**
     * 数据库表名占位符符
     */
    String SYMBOL_TABLE = "=table=";
    /**
     * 数据库表主键占位符符
     */
    String PRIMARY_KEY = "=id=";
    /**
     * 脚本开始标签
     */
    String SCRIPT_START = "<script>";
    /**
     * 脚本结束标签
     */
    String SCRIPT_END = "</script>";
//    @Select(value =
//        SCRIPT_START+"select if(exists((select * from `"+SYMBOL_TABLE+"` " + WHERE + " limit 1)), 1, 0) as flag"+SCRIPT_END
//    )
//    boolean selectExists(@Param("whereFast") List<MyCondition> where);
}
