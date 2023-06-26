package com.qin.common.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 * mybtis 的动态排序方式
 */
public class OrderBy {
    final static String ASC = "asc";
    final static String DESC = "desc";
    String field;
    String sortType;

    public OrderBy(String field, String sortType) {
        this.field = field;
        this.sortType = sortType;
    }

    public static OrderBy setCondition(String field, String value){
        return new OrderBy(field, value);
    }

    public static OrderBy setConditionASC(String field){
        return new OrderBy(field, ASC);
    }

    public static OrderBy setConditionDESC(String field){
        return new OrderBy(field, DESC);
    }

    public static void setCondition(ArrayList<OrderBy> order, Map<String, Object> map, String field){
        Object value;
        if ((value = map.get(field)) != null) {
            order.add(setCondition(field, value.toString()));
        }
    }

    public static void setConditionASC(ArrayList<OrderBy> order, Map<String, Object> map, String field){
        if (map.containsKey(field)) {
            order.add(setCondition(field, ASC));
        }
    }

    public static void setConditionDESC(ArrayList<OrderBy> order, Map<String, Object> map, String field){
        if (map.containsKey(field)) {
            order.add(setCondition(field, DESC));
        }
    }
}
