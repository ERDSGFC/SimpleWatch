package com.qin.common.dao.insert;

import com.qin.common.mapper.BaseMapper;
import com.qin.common.mvc.base.BaseObj;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

public interface InsertMapper<T extends BaseObj> extends BaseMapper<T> {


    /**
     * 插入一条数据
     * @param where 查询条件
     * @return true 存在 false 不存在
     */
    @Insert(value = SCRIPT_START +
            "INSERT INTO " +
            "`"+SYMBOL_TABLE+"` " +
            "<foreach item=\"item\" index=\"index\" collection=\"obj.toMap()\" open=\"(\" separator=\",\" close=\")\">" +
            " `${index}` " +
            "</foreach>" +
            "VALUE" +
            "<foreach item=\"item\" index=\"index\" collection=\"obj.toMap()\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item} " +
            "</foreach>" +
            SCRIPT_END
    )
    @Options(useGeneratedKeys = true, keyProperty = "obj.id")
    boolean insertObj(@Param("obj") T obj);




}
