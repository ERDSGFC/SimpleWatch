package com.qin.common.dao;

import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * mybatis mapper 操作合集
 * @param <T>
 */
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
    /**
     *  加动态的where AND查询语句
     * 列如 where `status` = 1 AND `id` IN ( 1, 2, 3)
     */
    String WHERE = "<where>" +
                   "<foreach item=\"item\" collection=\"whereCondition\" separator=\"AND\" >" +
                   "<choose>" +
                   "<when test=\"item.whereInFlag == false\">" +
                   " `${item.field}` ${item.condition} #{item.value} " +
                   "</when>" +
                   "<otherwise>" +
                   " `${item.field}` IN " +
                   "<foreach item=\"v\" collection=\"item.value\" open=\"(\" separator=\",\" close=\")\" >" +
                   " #{v} " +
                   "</foreach>" +
                   "</otherwise>" +
                   "</choose>" +
                   "</foreach>" +
                   "</where>";
    /**
     * 查询语句的查询列
     */
    String SELECT_COLUMN = "<choose>" +
                           "<when test=\"columns.length == 0\"> * </when>" +
                           "<otherwise> " +
                           "<foreach item=\"item\" collection=\"columns\" separator=\",\" >" +
                           " `${item}` " +
                           "</foreach>" +
                           "</otherwise>" +
                           "</choose>";

    /**
     * Sql 排序拼接 默认是主键倒序
     */
    String ORDER_BY = "<choose>" +
                      "<when test=\"order == null or order.isEmpty() \"> "+PRIMARY_KEY+" desc</when>" +
                      "<otherwise> " +
                      "<foreach item=\"orderBy\"  collection=\"order\"  separator=\",\" >" +
                      " `${orderBy.field}` ${orderBy.sortType} " +
                      "</foreach>" +
                      "</otherwise>" +
                      "</choose>" ;
    /**
     * Sql 跟新 SET
     */
    String UPDATE_SET = "<set>" +
                        "<foreach item=\"item\" index=\"index\" collection=\"set\"  separator=\",\" >" +
                        " `${index}` = #{item} " +
                        "</foreach>" +
                        "</set>";

    /**
     * 判断数据是否存在
     * select if(exists(select * from geo_position where status = 1), 1, 0) as flag
     * @param where 查询条件
     * @return true 存在 false 不存在
     */
    @Select(value =
        SCRIPT_START+"select if(exists((select * from `"+SYMBOL_TABLE+"` "+WHERE+" limit 1)), 1, 0) as flag"+SCRIPT_END
    )
    boolean selectExists(@Param("whereFast") List<Object> where);


    /**
     * 通过Map 插入一条数据
     * @param record
     * @return
     */
    @Insert(value =
                    SCRIPT_START +
                    "INSERT INTO " +
                    "`"+SYMBOL_TABLE+"` " +
                    "<foreach item=\"item\" index=\"index\" collection=\"record\" open=\"(\" separator=\",\" close=\")\">" +
                    " `${index}` " +
                    "</foreach>" +
                    "VALUE" +
                    "<foreach item=\"item\" index=\"index\" collection=\"record\" open=\"(\" separator=\",\" close=\")\">" +
                    " #{item} " +
                    "</foreach>" +
                    SCRIPT_END
    )
    boolean insertOneRecord(@Param("record") Map<String, Object> record);


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
                    "select "+SELECT_COLUMN+" from `"+SYMBOL_TABLE+"` where "+PRIMARY_KEY+" = #{primaryKey} limit 1" +
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
                    UPDATE_SET +
                    "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +
                    "</script>"
    )
    boolean updateById(@Param("set") Map<String, Object> set, @Param("primaryKey") long primaryKey);
}
