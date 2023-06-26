package com.qin.common.dao;

import com.qin.utils.JsonUtils;

public class MyChange {
    static final String JSON_REMOVE = "JSON_REMOVE";
    static final String JSON_SET = "JSON_SET";
    static final String JSON_REPLACE = "JSON_SET";
    static final String JSON_INSERT = "JSON_SET";
    static final String JSON_ARRAY_INSERT = "JSON_ARRAY_INSERT";

    String field;
//    ChangeFun changeFun;
    Object value;
    String jsonSql;
    public MyChange(String field, Object value) {
        this.field = field;
        this.value = value;
    }
    public MyChange(String field, String jsonSql) {
        this.field = field;
        this.jsonSql = jsonSql;
    }
    public static MyChange value(String string, Object view) {
        return new MyChange(string, view);
    }

    public static MyChange JSON_MERGE_PATCH(String field, String oldField, Object view) {
        return new MyChange(field, "JSON_MERGE_PATCH(`"+oldField+"`, cast('"+JsonUtils.toJsonString(view)+"' as json))");
    }

    public static MyChange JSON_MERGE_PATCH(String field, String oldField, String value, String... values) {
        StringBuilder str = new StringBuilder();
        str.append(", convert('").append(value).append("', json)");
        for (String v : values) {
            str.append(", convert('").append(v).append("', json)");
        }
        return new MyChange(field, "JSON_MERGE_PATCH(`"+oldField+"`"+str.toString()+"))");
    }
    public static MyChange JSON_MERGE_PRESERVE(String field, String oldField, String value, String... values) {
        StringBuilder str = new StringBuilder();
        str.append(", convert('").append(value).append("', json)");
        for (String v : values) {
            str.append(", convert('").append(v).append("', json)");
        }
        return new MyChange(field, "JSON_MERGE_PRESERVE(`"+oldField+"`"+str.toString()+")");
    }

    public static MyChange JSON_ARRAY_APPEND(String field, String oldField, PathValue value, PathValue... values) {
        return new MyChange(field, "JSON_ARRAY_APPEND(`"+oldField+"`"+joinStr(value, values)+")");
    }

    public static MyChange JSON_ARRAY_INSERT(String field, String oldField, PathValue value, PathValue... values) {
        return new MyChange(field, "JSON_ARRAY_INSERT(`"+oldField+"`"+joinStr(value,values)+")");
    }
    /**
     * 不存在就插入
     */
    public static MyChange JSON_INSERT(String field, String oldField, PathValue value, PathValue... values) {
        return new MyChange(field, "JSON_MERGE_INSERT(`"+oldField+"`"+joinStr(value, values)+")");
    }

    public static MyChange JSON_REMOVE(String field, String oldField, String value, String... values) {
        StringBuilder str = new StringBuilder();
        str.append(", '").append(value).append("'");
        for (String v : values) {
            str.append(", '").append(v).append("'");
        }
        return new MyChange(field, "JSON_MERGE_INSERT(`"+oldField+"`"+str.toString()+")");
    }

    public static MyChange JSON_REPLACE(String field, String oldField, PathValue value, PathValue... values) {
        return new MyChange(field, "JSON_REPLACE(`"+oldField+"`"+joinStr(value, values)+")");
    }
    public static MyChange JSON_SET(String field, String oldField, PathValue value, PathValue... values) {
        return new MyChange(field, "JSON_SET(`"+oldField+"`"+joinStr(value, values)+")");
    }
    public static String joinStr(PathValue pathValue, PathValue... pathValues) {
       StringBuilder str = new StringBuilder(); 
        str.append(", '").append(pathValue.getPath()).append("', convert('").append(pathValue.getValue()).append("', json)");
        for (PathValue value : pathValues) {
            str.append(", '").append(value.getPath()).append("', convert('").append(value.getValue()).append("', json)");
            
        }
        return str.toString();
    }
    public static MyChange JSON_REMOVE(String field, String oldField, PathValue... value) {
        return new MyChange(field, "JSON_MERGE_INSERT(`"+oldField+"`, ))");
    }
    public static class PathValue{
        String path;
        String value;
        /**
         * @return the path
         */
        public String getPath() {
            return path;
        }
        /**
         * @param path the path to set
         */
        public void setPath(String path) {
            this.path = path;
        }
        /**
         * @return the value
         */
        public String getValue() {
            return value;
        }
        /**
         * @param value the value to set
         */
        public void setValue(String value) {
            this.value = value;
        }
        
    }

    public static PathValue setPathValue(String path, String value) {
        PathValue pathValue = new PathValue();
        return pathValue;
    }
}
