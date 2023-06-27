package com.qin.common.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 *  动态查询条件类
 */
public class DynCondition {
    String field;
    String condition;
    Object value;

    boolean whereInFlag =  false;

    public DynCondition(String field, String condition, Object value) {
        this.field = field;
        this.condition = condition;
        this.value = value;
    }

    public DynCondition(String field, String condition, Object value, boolean flag) {
        this.field = field;
        this.condition = condition;
        this.value = value;
        this.whereInFlag = flag;
    }

    public static DynCondition setCondition(String field, String condition, Object value){
        return new DynCondition(field, condition, value);
    }

    public static DynCondition setCondition(String field, Object value){
        return setCondition(field, "=", value);
    }

    public static void setCondition(ArrayList<DynCondition> DynConditions, Map<String, Object> map, String field){
        setCondition(DynConditions, map, field, field);
    }

    public static void setCondition(ArrayList<DynCondition> DynConditions, Map<String, Object> map, String field, String key){
        Object value;
        if ((value = map.get(key)) != null) {
            DynConditions.add(setCondition(field, value));
        }
    }

    public static DynCondition setConditionArray(String field, Object[] value){
        return new DynCondition(field, "In", value, true);
    }

    public static DynCondition setConditionArray(String field, long[] value){
        return new DynCondition(field, "In", value, true);
    }

    public static DynCondition setConditionArray(String field, int[] value){
        return new DynCondition(field, "In", value, true);
    }

    public static DynCondition setConditionArray(String field, byte[] value){
        return new DynCondition(field, "In", value, true);
    }

    public static DynCondition setConditionArray(String field, char[] value){
        return new DynCondition(field, "In", value, true);
    }
    public static DynCondition setConditionArray(String field, boolean[] value){
        return new DynCondition(field, "In", value, true);
    }
    public static DynCondition setConditionArray(String field, String[] value){
        return new DynCondition(field, "In", value, true);
    }

    public static DynCondition setConditionArray(String field, Number[] value){
        return new DynCondition(field, "In", value, true);
    }

}
