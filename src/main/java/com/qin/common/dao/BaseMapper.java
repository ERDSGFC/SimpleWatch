package com.qin.common.dao;

import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * mybatis mapper 操作合集
 * @param <T>
 */
public interface BaseMapper<T> {
    /**
     * 数据库表名占位符符
     */
    String SYMBOL_TABLE = "${_tableName_}";
    /**
     * 数据库表主键占位符符
     */
    String PRIMARY_KEY = "${_tablePrimaryKey_}";
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
    boolean selectExists(@Param("whereCondition") List<Object> where);
    
    /**
     * select IF(IFNULL((select `id` FROM `user` where code  = "CodeNumber"), 52) = 52, 1,0) as flag
     * select IF(IFNULL((select `id` FROM `user` where user_name  = "小黑"), 1) = 1, 1,0) as flag
     * 只能用在唯一索引的查询 修改时判断是否唯一 ,查询逻辑 查询你要修改的值的主键，如果为空就设为和自己主键一样的值，在比较和直接主键是否相等, 相等就
     * 说明你没有修改这个值，或者是你修改这个值是一个表中没有的值
     * @param where where条件
     * @param primaryKey 主键的值
     * @return boolean true 表示唯一 false 不唯一
     */
    @Select(value = SCRIPT_START +
                    "select IF(IFNULL((select `"+PRIMARY_KEY+"` FROM `"+SYMBOL_TABLE+"` " +
                    WHERE + "), #{primaryKey}) = #{primaryKey}, 1,0) as flag" +
                    SCRIPT_END
    )
    @Options()
    boolean selectUnique(@Param("whereCondition") List<DynCondition> where, @Param("primaryKey") long primaryKey);

    /**
     * 插入一条数据
     * @param obj 查询条件
     * @return true 存在 false 不存在
     */
    @Insert(value = SCRIPT_START + "INSERT INTO `"+SYMBOL_TABLE+"` ${_insertObjSql_}" + SCRIPT_END)
    @Options(useGeneratedKeys = true, keyProperty = "obj.id")
    boolean insertObj(@Param("obj") T obj);
    /**
     * 通过Map 插入一条数据
     * @param record 插入的记录
     * @return 返回是否插入成功 true 成功 false 失败
     */
    @Insert(value = SCRIPT_START + "INSERT INTO `"+SYMBOL_TABLE+"` " +
                    "<foreach item=\"item\" index=\"index\" collection=\"record\" open=\"(\" separator=\",\" close=\")\">" +
                    " `${index}` " +
                    "</foreach>" +
                    "VALUE" +
                    "<foreach item=\"item\" index=\"index\" collection=\"record\" open=\"(\" separator=\",\" close=\")\">" +
                    " #{item} " +
                    "</foreach>" +
                    SCRIPT_END
    )
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    boolean insertOneRecord(@Param("record") Map<String, Object> record);
    // 根据主键查询一条
    @Select("select * from `"+SYMBOL_TABLE+"` where "+PRIMARY_KEY+" = #{primaryKey} limit 1")
    T findOneById(@Param("primaryKey") long primaryKey);

    // 根据主键查询一条
    @Select(value= SCRIPT_START + "select "+SELECT_COLUMN+" from `"+SYMBOL_TABLE+"` where "+PRIMARY_KEY+" = #{primaryKey} limit 1" + SCRIPT_END)
    HashMap<String, Object> findOneMapById(@Param("primaryKey") long primaryKey, @Param("columns") String... columns);
    // 查询全部
    @Select("select * from `"+SYMBOL_TABLE+"`")
    ArrayList<T> findAll();
    // 查询全部 排序
    @Select(value = SCRIPT_START+ "select * from `"+SYMBOL_TABLE+"` order by " + ORDER_BY + SCRIPT_END)
    ArrayList<T> findAllOrder(@Param("order") List<OrderBy> order);
    //  根据条件和排序选择性的查询 第一条数据
    @Select(value = SCRIPT_START + "select * from `"+SYMBOL_TABLE+"`" + WHERE+ " order by " + ORDER_BY+ " limit 1"+ SCRIPT_END)
    T findOneSelectiveOrder(@Param("whereCondition")List<DynCondition> conditions, @Param("order") List<OrderBy> order);
    //  根据条件和排序选择性的查询 全部数据
    @Select(value = SCRIPT_START + "select * from `"+SYMBOL_TABLE+"`" + WHERE+ " order by " + ORDER_BY + SCRIPT_END)
    ArrayList<T> findSelectiveOrder(@Param("whereCondition") List<DynCondition> whereFast, @Param("order") List<OrderBy> order);
    //  根据条件选择性的查询 全部数据
    @Select(value = {SCRIPT_START + "select * from `"+SYMBOL_TABLE+"`" + WHERE+ SCRIPT_END})
    ArrayList<T> findSelective(@Param("whereCondition") List<DynCondition> whereFast);
    //  根据条件选择性的查询 一条数据
    @Select(value = {SCRIPT_START + "select * from `"+SYMBOL_TABLE+"`" + WHERE+ " limit 1"+ SCRIPT_END})
    T findOneSelective(@Param("whereCondition") List<DynCondition> whereFast);
    // 根据primaryKey 修改
    @Update(value = SCRIPT_START + "UPDATE `"+SYMBOL_TABLE+"` ${_updateObj_}" + "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +SCRIPT_END)
    boolean updateChangeFieldsById(@Param("newObj") T newObj,@Param("oldObj") T oldObj, @Param("primaryKey") long primaryKey);
    @Update(value = SCRIPT_START + "UPDATE `"+SYMBOL_TABLE+"` ${_updateObj_}" + "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +SCRIPT_END)
    boolean updateAllFieldsById(@Param("newObj") T newObj, @Param("primaryKey") long primaryKey);
    // 根据primaryKey 修改
    @Update(value = SCRIPT_START + "UPDATE `"+SYMBOL_TABLE+"`" + UPDATE_SET + "WHERE "+PRIMARY_KEY+"=#{primaryKey}" +SCRIPT_END)
    boolean updateById(@Param("set") Map<String, Object> set, @Param("primaryKey") long primaryKey);
    // 根据 条件修改
    @Update(value = SCRIPT_START + "UPDATE `"+SYMBOL_TABLE+"`" + UPDATE_SET + WHERE + SCRIPT_END)
    long updateSelective(@Param("set") Map<String, Object> set, @Param("whereCondition") @NotNull List<DynCondition> whereFast);
    // 根据primaryKey 删除
    @Delete(value = "DELETE FROM `"+SYMBOL_TABLE+"` WHERE "+PRIMARY_KEY+" = #{primaryKey}")
    boolean delById(@Param("primaryKey") long primaryKey);
    // 根据参数来 变化 删除
    @Delete(value = SCRIPT_START + "DELETE FROM `"+SYMBOL_TABLE+"`" + WHERE + SCRIPT_END)
    long delSelective(@Param("whereCondition") @NotNull List<DynCondition> where);
}
