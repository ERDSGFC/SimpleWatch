package com.qin.common;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
@Log4j2
public class MybatisIntercept implements Interceptor {

    @SuppressWarnings("all")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target= invocation.getTarget();
        Object[] args = invocation.getArgs();
        Object mappedStatement = args[0];
        // 做表名和主键的处理
        String mapperId = ((MappedStatement) mappedStatement).getId();
        String mapperClassPath = mapperId.substring(0, mapperId.lastIndexOf("."));
        Class<?> mapper = Class.forName(mapperClassPath);
        final TableInfo annotation = mapper.getAnnotation(TableInfo.class);
        final String tableName = annotation.value();
        final String primaryKey = annotation.primaryKey();
        Map parameterMap = (Map<String,Object>)args[1];// 获取mapper 的参数列表, 并修改它
        ((Map<String, Object>) parameterMap).put("_TABLE_NAME_", tableName);
        ((Map<String, Object>) parameterMap).put("_TABlE_PRIMARY_KEY_", primaryKey);
        // 如果表主键不是id 而是自定义的
        if (invocation.getArgs().length == 4) {
            ((Map<String, Object>) parameterMap).put("_SELECT_FIELDS_", selectFields(annotation));
            if (annotation.logicDeleted()) {
                ((Map<String, Object>) parameterMap).put("_NO_DELETE_", "AND `"+annotation.deletedField()+"` = "+annotation.normal());
            } else {
                ((Map<String, Object>) parameterMap).put("_NO_DELETE_", "");
            }
        } else {// 下面 是会修改表的方法才执行的 update sql 和 delete sql
            if (!primaryKey.equals("id")) ((MappedStatement) mappedStatement).getKeyProperties()[0] = "obj." + primaryKey; // 修改自增主键
            StringBuilder sql = new StringBuilder();
            if (mapperId.endsWith(".insertObj")) {
                String[] insertFields = annotation.fields();
                if (!insertSql(insertFields, sql, annotation)) return 0;
                ((Map<String, Object>) parameterMap).put("_INSERT_FIELDS_SQL_", sql.toString());
            } else if (mapperId.endsWith(".updateChangeFieldsById")){
                Object newObj  = parameterMap.get("newObj");
                Object OldObj  = parameterMap.get("oldObj");
                sql.append("SET ");
                if (!changedFields(newObj, OldObj, sql, annotation)) return 0;
                ((Map<String, Object>) parameterMap).put("_UPDATE_OBJ_", sql.toString());
            } else if (mapperId.endsWith(".updateAllFieldsById")){
                Object newObj  = parameterMap.get("newObj");
                sql.append("SET ");
                if (!changedFields(newObj, sql, annotation)) return 0;
                ((Map<String, Object>) parameterMap).put("_UPDATE_OBJ_", sql.toString());
            }
            if (annotation.logicDeleted()) {
                ((Map<String, Object>) parameterMap).put("_DELETE_FLAG_", "UPDATE `"+annotation.value()+"` SET `"+annotation.deletedField()+"` = "+annotation.deleted());
            } else {
                ((Map<String, Object>) parameterMap).put("_DELETE_FLAG_", "DELETE FROM `"+annotation.value()+"`");
            }
        }
        return invocation.proceed();
    }

    private static String selectFields(TableInfo tableInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("`").append(camelToUnderline(tableInfo.primaryKey())).append("`, ");
        String[] fields = tableInfo.fields();
        for (String field : fields) {
            sql.append("`").append(camelToUnderline(field)).append("`, ");
        }
        if (tableInfo.timeFlag()) {
            sql.append("`").append(camelToUnderline(tableInfo.createdTimeField())).append("`, ")
                    .append("`").append(camelToUnderline(tableInfo.updatedTimeField())).append("`");
        }
        return sql.toString();
    }

    private static boolean insertSql(String[] insertFields, StringBuilder _insertField_, TableInfo tableInfo) {
        StringBuilder _insertValue_ = new StringBuilder();
        boolean flag = false;
        _insertField_.append("(");
        _insertValue_.append("(");
        int index = insertFields.length;
        if (index >0) {
            for (int i = 0; i < index; i++) {
                _insertField_.append("`").append(camelToUnderline(insertFields[i])).append("` ,");
                _insertValue_.append("#{obj.").append(insertFields[i]).append("} ,");
                flag = true;
            }
        }
        if (tableInfo.timeFlag()) {
            _insertField_.append("`").append(tableInfo.createdTimeField()).append("` ,`").append(tableInfo.updatedTimeField()).append("` ,");
            _insertValue_.append(LocalDateTime.now()).append(" ,").append(LocalDateTime.now()).append(" ,");
            flag = true;
        }
        if (tableInfo.logicDeleted()) {
            _insertField_.append("`").append(tableInfo.deletedField()).append("` ,`").append(tableInfo.deletedTimeField()).append("` ,");
            _insertValue_.append(tableInfo.normal()).append(" ,").append(tableInfo.deletedDefaultTime()).append(" ,");
            flag = true;
        }
        _insertField_.replace(_insertField_.length()-1,_insertField_.length(), ")");
        _insertValue_.replace(_insertValue_.length()-1,_insertValue_.length(), ")");
        _insertField_.append(" VALUE ").append(_insertValue_);
        return flag;
    }
    private boolean changedFields(Object newObj, StringBuilder sql, TableInfo tableInfo) throws IllegalAccessException {
        Class<?> cls = newObj.getClass();
        boolean flag = false;
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Object n = f.get(newObj);
            sql.append("`").append(camelToUnderline(f.getName())).append("` = #{newObj.").append(f.getName()).append("} ,");
            flag = true;
        }
        if (flag && tableInfo.timeFlag()) {
            sql.append("`").append(tableInfo.updatedTimeField()).append(LocalDateTime.now()).append(" ,");
        }
        sql.deleteCharAt(sql.length()-1);
        return flag;
    }
    private static boolean changedFields(Object newObj, Object oldObj, StringBuilder updateObj, TableInfo tableInfo) throws IllegalAccessException {
        Class<?> cls = newObj.getClass();
        boolean flag = false;
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            Object n = f.get(newObj);
            if(!Objects.equals(n,f.get(oldObj))) {
                updateObj.append("`").append(camelToUnderline(f.getName())).append("` = #{newObj.").append(f.getName()).append("} ,");
                flag = true;
            }
        }
        if (flag && tableInfo.timeFlag()) {
            updateObj.append("`").append(tableInfo.updatedTimeField()).append(LocalDateTime.now()).append(" ,");
        }
        updateObj.deleteCharAt(updateObj.length()-1);
        return flag;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public static String camelToUnderline(String str) {
        if (str == null) return "";
        int len = str.length();
        StringBuilder underline = new StringBuilder(len);
        underline.append(str.substring(0, 1).toLowerCase());
        for (int i = 1; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                underline.append("_").append(Character.toLowerCase(c));
            } else {
                underline.append(c);
            }
        }
        return underline.toString();
    }

}
