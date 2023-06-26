package com.qin.common.dao;

import com.qin.common.BaseObj;
import com.qin.common.mvc.base.BaseObj;
import org.apache.ibatis.annotations.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T extends BaseObj> {

    String SYMBOL_TABLE = "=table=";
    String PRIMARY_KEY = "=id=";

    String FOR_WHERE =
            "<where>" +
            "<foreach item=\"item\" collection=\"where\" separator=\"AND\" >" +
            " `${item.field}` ${item.condition} #{item.value} " +
            "</foreach>" +
            "</where>";
    String FOR_COLUMN =
            "<choose>" +
            "<when test=\"columns.length == 0\"> * </when>" +
            "<otherwise> " +
            "<foreach item=\"item\" collection=\"columns\" separator=\",\" >" +
            " `${item}` " +
            "</foreach>" +
            "</otherwise>" +
            "</choose>" ;

    String ORDER_BY =
            "<choose>" +
            "<when test=\"order == null or order.isEmpty() \"> "+PRIMARY_KEY+" desc</when>" +
            "<otherwise> " +
            "<foreach item=\"order_v\"  collection=\"order\"  separator=\",\" >" +
            " `${order_v.field}` ${order_v.sortType} " +
            "</foreach>" +
            "</otherwise>" +
            "</choose>" ;
    String WHERE =
            "<where>" +
            "<foreach item=\"item\" collection=\"whereFast\"  separator=\"AND\" >" +
            "<choose>" +
            "<when test=\"item.flag\">" +
            " `${item.field}` In " +
            "<foreach item=\"v\" collection=\"item.value\" open=\"(\" separator=\",\" close=\")\" >" +
            " #{v} " +
            "</foreach>" +
            "</when>" +
            "<otherwise>" +
            " `${item.field}` ${item.condition} #{item.value} " +
            "</otherwise>" +
            "</choose>" +
            "</foreach>" +
            "</where>";
    String FOR_SET =
            "<set>" +
            "<foreach item=\"item\" index=\"index\" collection=\"set\"  separator=\",\" >" +
            " `${index}` = #{item} " +
            "</foreach>" +
            "</set>";
//    String IF_WHERE =
//            "<if test=\"whereFast != null\">" +
//            "<where>" +
//            "<foreach item=\"item\" collection=\"whereFast\"  separator=\"AND\" >" +
//            "<choose>" +
//            "<when test=\"item.flag\">" +
//            " `${item.field}` In " +
//            "<foreach item=\"v\" collection=\"item.value\" open=\"(\" separator=\",\" close=\")\" >" +
//            " #{v} " +
//            "</foreach>" +
//            "</when>" +
//            "<otherwise>" +
//            " `${item.field}` ${item.condition} #{item.value} " +
//            "</otherwise>" +
//            "</choose>" +
//            "</foreach>" +
//            "</where>" +
//            "</if>" ;

    // 插入一条数据
    @Insert(value =
    "<script>" +
    "INSERT INTO " +
    "`"+SYMBOL_TABLE+"` " +
    "<foreach item=\"item\" index=\"index\" collection=\"obj.toMap()\" open=\"(\" separator=\",\" close=\")\">" +
    " `${index}` " +
    "</foreach>" +
    "VALUE" +
    "<foreach item=\"item\" index=\"index\" collection=\"obj.toMap()\" open=\"(\" separator=\",\" close=\")\">" +
    " #{item} " +
    "</foreach>" +
    "</script>"
    )
    @Options(useGeneratedKeys = true, keyProperty = "obj.id")
    boolean insertObj(@Param("obj") T obj);

    @Insert(value =
    "<script>" +
    "INSERT INTO " +
    "`"+SYMBOL_TABLE+"` " +
    "<foreach item=\"item\" index=\"index\" collection=\"record\" open=\"(\" separator=\",\" close=\")\">" +
    " `${index}` " +
    "</foreach>" +
    "VALUE" +
    "<foreach item=\"item\" index=\"index\" collection=\"record\" open=\"(\" separator=\",\" close=\")\">" +
    " #{item} " +
    "</foreach>" +
    "</script>"
    )
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    boolean insertOneRecord(@Param("record") Map<String, Object> record);

    // 1. 查询是否存在 select if(exists(select * from geo_position where status = 1), 1, 0) as flag
    @Select(value =
    "<script>" +
    "select if(exists((select * from `"+SYMBOL_TABLE+"` " +
    WHERE +
    " limit 1)), 1, 0) as flag" +
    "</script>"
    )
    boolean selectExists(@Param("whereFast") List<MyCondition> where);

    /**
     * select IF(IFNULL((select `id` FROM intelligent.hardware_instruction where code  = "PHON"), 52) = 52, 1,0) as flag
     * 只能用在唯一索引的查询 判断
     * @param where where条件
     * @param primaryKey 主键的值
     * @return boolean true 表示唯一 false 不唯一
     */
    @Select(value =
    "<script>" +
    "select IF(IFNULL((select `"+PRIMARY_KEY+"` FROM `"+SYMBOL_TABLE+"` " +
    WHERE + "), #{primaryKey}) = #{primaryKey}, 1,0) as flag" +
    "</script>"
    )
    boolean selectUnique(@Param("whereFast") List<MyCondition> where, @Param("primaryKey") long primaryKey);

    // 根据主键查询一条  =id=
    @Select("select * from `"+SYMBOL_TABLE+"` where "+PRIMARY_KEY+" = #{primaryKey} limit 1")
    T findOneById(@Param("primaryKey") long primaryKey);

    // 根据主键查询一条  =id=
    @Select(
            value= "<script>" +
            "select "+FOR_COLUMN+" from `"+SYMBOL_TABLE+"` where "+PRIMARY_KEY+" = #{primaryKey} limit 1" +
            "</script>"
    )
    HashMap<String, Object> findOneMapById(@Param("primaryKey") long primaryKey, @Param("columns") String... columns);

    // 查询全部
    @Select("select * from `"+SYMBOL_TABLE+"`")
    ArrayList<T> findAll();

    // 查询全部 排序
    @Select(value =
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    " order by " +
    ORDER_BY +
    "</script>"
    )
    ArrayList<T> findAllOrder(@Param("order") List<OrderBy> order);


    @Select(value =
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE+
    " order by " +
    ORDER_BY+
    " limit 1"+
    "</script>"
    )
    T findOneSelectiveOrder(@Param("whereFast")List<MyCondition> conditions, @Param("order") List<OrderBy> order);

    // 根据参数来 变化 查询 排序
    @Select(value =
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE+
    " order by " +
    ORDER_BY+
    "</script>"
    )
    ArrayList<T> findSelectiveOrder(@Param("whereFast") List<MyCondition> whereFast, @Param("order") List<OrderBy> order);

    // 根据参数来 变化 查询
    @Select(value = {
            "<script>" +
                    "select * from `"+SYMBOL_TABLE+"`" +
                    WHERE+
                    "</script>"
    })
    ArrayList<T> findSelective(@Param("whereFast") List<MyCondition> whereFast);

    // 根据参数来 变化 查询
    @Select(value = {
            "<script>" +
                    "select * from `"+SYMBOL_TABLE+"`" +
                    WHERE+
                    " limit 1"+
                    "</script>"
    })
    T findOneSelective(@Param("whereFast") List<MyCondition> whereFast);

    @MapKey("id")
    @Select(value = {
            "<script>" +
                    "select * from `"+SYMBOL_TABLE+"`" +
                    WHERE +
                    "</script>"
    })
    Map<Integer, T> findMapIdSelective(@Param("whereFast") List<MyCondition> where);

    @MapKey("id")
    @Select(value = {
            "<script>" +
                    "select * from `"+SYMBOL_TABLE+"`" +
                    WHERE +
                    " order by " +
                    ORDER_BY+
                    "</script>"
    })
    Map<Integer, T> findMapIdSelectiveOrder(@Param("whereFast") List<MyCondition> where, @Param("order") List<OrderBy> order);

    // 根据primaryKey 删除
    @Delete(value = "DELETE FROM `"+SYMBOL_TABLE+"` WHERE "+PRIMARY_KEY+" = #{primaryKey}")
    boolean delById(@Param("primaryKey") long primaryKey);

    // 根据参数来 变化 删除
    @Delete(value =
    "<script>" +
    "DELETE FROM `"+SYMBOL_TABLE+"`" +
    WHERE +
    "</script>"
    )
    long delSelective(@Param("whereFast") @NotNull List<MyCondition> whereFast);

    // 根据primaryKey 修改
    @Update(value =
    "<script>" +
    "UPDATE `"+SYMBOL_TABLE+"`" +
    FOR_SET +
    "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +
    "</script>"
    )
    boolean updateById(@Param("set") Map<String, Object> set, @Param("primaryKey") long primaryKey);

}
