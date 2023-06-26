package com.qin.common.dao.update;

import com.qin.common.mapper.BaseMapper;
import com.qin.common.mapper.MyChange;
import com.qin.common.mapper.MyCondition;
import com.qin.common.mvc.base.BaseObj;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface UpdateMapper<T extends BaseObj> extends BaseMapper<T> {


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

    // 根据 条件修改 AND
    @Update(value =
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_SET +
    FOR_WHERE +
    "</script>"
    )
    long updateByAnd(@Param("set") Map<String, Object> set, @Param("where") @NotNull List<MyCondition> where);

    // 根据 条件修改
    @Update(value =
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_SET +
    WHERE +
    "</script>"
    )
    long updateSelective(@Param("set") Map<String, Object> set, @Param("whereFast") @NotNull List<MyCondition> whereFast);

    @Update(value =
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_CHANGE +
    "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +
    "</script>"
    )
    boolean updateJsonValueById(@Param("change")List<MyChange> change, @Param("primaryKey") long primaryKey);

    @Update(
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_CHANGE +
    WHERE +
    "</script>"
    )
    long updateJsonSelective(@Param("change")List<MyChange> change, @Param("whereFast") @NotNull List<MyCondition> whereFast);


}