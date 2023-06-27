package com.qin.common.dao.update;

import com.qin.common.dao.DynCondition;
import com.qin.common.dao.JsonChange;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

import static com.qin.common.dao.BaseMapper.*;

public interface UpdateMapper<T> {

    String FOR_CHANGE =
    "<set>" +
    "<foreach item=\"item\" collection=\"change\"  separator=\",\" >" +
    "<choose>" +
    "<when test=\"item.jsonSql != null\">" +
    " `${item.field}` = ${item.jsonSql} " +
    "</when>" +
    "<otherwise>" +
    " `${item.field}` = #{item.value} " +
    "</otherwise>" +
    "</choose>" +
    "</foreach>" +
    "</set>";


    @Update(value =
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_CHANGE +
    "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +
    "</script>"
    )
    boolean updateJsonValueById(@Param("change")List<JsonChange> change, @Param("primaryKey") long primaryKey);

    @Update(
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_CHANGE +
    WHERE +
    "</script>"
    )
    long updateJsonSelective(@Param("change")List<JsonChange> change, @Param("whereFast") @NotNull List<DynCondition> whereFast);


}