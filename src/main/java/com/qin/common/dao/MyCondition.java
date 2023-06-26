package com.qin.common.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 * 查询条件类
 */
public class MyCondition {
    String field;
    String condition;
    Object value;
    boolean flag =  false;

    public MyCondition(String field, String condition, Object value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }

    public MyCondition(String field, String condition, Object value, boolean flag) {
        this.field = field;
        this.condition = condition;
        this.value = value;
        this.flag = flag;
    }

    public static MyCondition setCondition(String field, String condition, Object value){
        return new MyCondition(field, condition, value);
    }

    public static MyCondition setCondition(String field, Object value){
        return setCondition(field, "=", value);
    }

    public static void setCondition(ArrayList<MyCondition> myConditions, Map<String, Object> map, String field){
        setCondition(myConditions, map, field, field);
    }

    public static void setCondition(ArrayList<MyCondition> myConditions, Map<String, Object> map, String field, String key){
        Object value;
        if ((value = map.get(key)) != null) {
            myConditions.add(setCondition(field, value));
        }
    }

    public static MyCondition setConditionArray(String field, Object value){
        return new MyCondition(field, "In", value, true);
    }

    public static MyCondition setConditionArray(String field, long[] value){
        return new MyCondition(field, "In", value, true);
    }


}
