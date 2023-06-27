package com.qin.common.dao.select;

import com.qin.common.dao.DynCondition;
import com.qin.common.dao.OrderBy;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.qin.common.dao.BaseMapper.*;

public interface SelectMapper<T> {

    String IF_FOR_WHERE =
        "<if test=\"where != null\">" +
        "<where>" +
        "<foreach item=\"item\" collection=\"where\"  separator=\"AND\" >" +
        " `${item.field}` ${item.condition} #{item.value} " +
        "</foreach>" +
        "</where>" +
        "</if>" ;
    String WHERE_IN_PRIMARY_KEY =
            "<where>" +
            PRIMARY_KEY+" In" +
            "<foreach item=\"in_V\" collection=\"whereIn\" open=\"(\" separator=\",\" close=\")\" >" +
            " #{in_V} " +
            "</foreach>" +
            "</where>" ;
    String WHERE_IN =
            "<where>" +
            "${where_name} In" +
            "<foreach item=\"in_V\" collection=\"whereIn\" open=\"(\" separator=\",\" close=\")\" >" +
            " #{in_V} " +
            "</foreach>" +
            "</where>" ;


    // 2. count 查询
    @Select(value = {
            "<script>" +
                    "select count("+PRIMARY_KEY+") from `"+SYMBOL_TABLE+"`" +
                    "</script>"
    })
    long selectCount();
    // 3. count 查询
    @Select(value = {
            "<script>" +
                    "select count("+PRIMARY_KEY+") from `"+SYMBOL_TABLE+"`" +
                    IF_FOR_WHERE +
                    "</script>"
    })
    long selectCountByAnd(@Param("where") List<DynCondition> where);

    // sum 查询
    @Select(value = {
            "<script>" +
                    "select SUM(`${sum}`) from `"+SYMBOL_TABLE+"`" +
                    "</script>"
    })
    long selectSum(@Param("sum") String sum);
    // sum 查询
    @Select(value = {
            "<script>" +
                    "select SUM(`${sum}`) from `"+SYMBOL_TABLE+"`" +
                    IF_FOR_WHERE +
                    "</script>"
    })
    long selectSumByAnd(@Param("sum") String sum, @Param("where") List<DynCondition> where);

    // avg all
    @Select(value = {
            "<script>" +
                    "select AVG(`${avg}`) from `"+SYMBOL_TABLE+"`" +
                    "</script>"
    })
    long selectAvg(@Param("avg") String avg);
    // avg 查询
    @Select(value = {
            "<script>" +
                    "select AVG(`${avg}`) from `"+SYMBOL_TABLE+"`" +
                    IF_FOR_WHERE +
                    "</script>"
    })
    long selectAvgByAnd(@Param("avg") String avg, @Param("where") List<DynCondition> where);

    // max all
    @Select(value = {
            "<script>" +
                    "select MAX(`${max}`) from `"+SYMBOL_TABLE+"`" +
                    "</script>"
    })
    long selectMax(@Param("max") String max);
    // avg 查询
    @Select(value = {
            "<script>" +
                    "select MAX(`${max}`) from `"+SYMBOL_TABLE+"`" +
                    IF_FOR_WHERE +
                    "</script>"
    })
    long selectMaxByAnd(@Param("max") String max, @Param("where") List<DynCondition> where);

    // min all
    @Select(value = {
            "<script>" +
                    "select MIN(`${min}`) from `"+SYMBOL_TABLE+"`" +
                    "</script>"
    })
    long selectMin(@Param("min") String min);
    // avg 查询
    @Select(value = {
            "<script>" +
                    "select MIN(`${min}`) from `"+SYMBOL_TABLE+"`" +
                    IF_FOR_WHERE +
                    "</script>"
    })
    long selectMinByAnd(@Param("min") String min, @Param("where") List<DynCondition> where);

    @Select(value = {
            "<script>" +
                    "select `${group}`" +
                    " from `"+SYMBOL_TABLE+"`" +
                    IF_FOR_WHERE +
                    " Group By `${group}`"+
                    "</script>"
    })
    long selectGroupByAnd(@Param("group") String group, @Param("where") List<DynCondition> where);


    // 根据条件查询一条
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    IF_FOR_WHERE +
    " limit 1" +
    "</script>"
    })
    T findOneByAnd(@Param("where") List<DynCondition> where);
    // 根据条件查询一条 排序
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    IF_FOR_WHERE +
    " order by " +
    ORDER_BY +
    " limit 1" +
    "</script>"
    })
    T findOneByAndOrder(@Param("where") List<DynCondition> where, @Param("order") List<OrderBy> order);



    // 根据条件查询 AND
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    IF_FOR_WHERE +
    "</script>"
    })
    ArrayList<T> findByAnd(@Param("where") List<DynCondition> where);

    // 根据条件查询 AND 排序
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    IF_FOR_WHERE +
    " order by " +
    ORDER_BY +
    "</script>"
    })
    ArrayList<T> findByAndOrder(@Param("where") List<DynCondition> where, @Param("order") List<OrderBy> order);

    // 根据条件查询 IN primaryKey
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE_IN_PRIMARY_KEY +
    "</script>"
    })
    ArrayList<T> findInId(@Param("whereIn") long[] whereIn);
    // 根据条件查询 IN primaryKey 排序
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE_IN_PRIMARY_KEY +
    " order by " +
    ORDER_BY +
    "</script>"
    })
    ArrayList<T> findInIdOrder(@Param("whereIn") long[] whereIn, @Param("order") List<OrderBy> order);

    //填入的是对象的属性名，作为 Map 的 key 值 筛选 和排序。
    @MapKey("id")
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE_IN_PRIMARY_KEY +
    "</script>"
    })
    Map<Integer, T> findMapIdIn(@Param("whereIn") long[] whereIn);

    // 根据参数来 IN 查询 //,javaType=long,jdbcType=NUMERIC
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE_IN+
    "</script>"
    })
    ArrayList<T> findInField(@Param("where_name") String whereName, @Param("whereIn") Object[] whereIn);

    // 根据参数来 IN 查询 排序
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    WHERE_IN+
    " order by " +
    ORDER_BY+
    "</script>"
    })
    ArrayList<T> findInFieldOrder(@Param("where_name") String whereName, @Param("whereIn") Object[] whereIn, @Param("order") List<OrderBy> order);

    //填入的是对象的属性名，作为 Map 的 key 值。刷选
    @MapKey("id")
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    IF_FOR_WHERE +
    "</script>"
    })
    Map<Integer, T> findMapId(@Param("where") List<DynCondition> where);//重写


    //填入的是对象的属性名，作为 Map 的 key 值 筛选 和排序。
    @MapKey("id")
    @Select(value = {
    "<script>" +
    "select * from `"+SYMBOL_TABLE+"`" +
    IF_FOR_WHERE +
    " order by " +
    ORDER_BY +
    "</script>"
    })
    Map<Integer, T> findMapIdOrder(@Param("where") List<DynCondition> where, @Param("order") List<OrderBy> order);


}