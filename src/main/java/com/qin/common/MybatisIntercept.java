package com.qin.common;

import com.qin.common.dao.IgnoreFiled;
import com.qin.utils.LocalDateTimeUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
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
        log.info("invocation function name: " + invocation.getMethod().getName() +"===" + target.getClass());
        log.info(invocation.getArgs().length);
        Object[] args = invocation.getArgs();
        Object mappedStatement = args[0];
        if (mappedStatement instanceof MappedStatement) { // 做表名和主键的处理
            String mapperId = ((MappedStatement) mappedStatement).getId();
            String mapperClassPath = mapperId.substring(0, mapperId.lastIndexOf("."));
            Class<?> mapper = Class.forName(mapperClassPath);
            final TableInfo annotation = mapper.getAnnotation(TableInfo.class);
            final String tableName = annotation.value();
            final String primaryKey = annotation.primaryKey();
            Map parameterMap = (Map<String,Object>)args[1];
            ((Map<String, Object>) parameterMap).put("_tableName_", tableName);
            ((Map<String, Object>) parameterMap).put("_tablePrimaryKey_", primaryKey);
            if (!primaryKey.equals("id")) ((MappedStatement) mappedStatement).getKeyProperties()[0] = "obj." + primaryKey; // 修改自增主键
            // 下面 是会修改表的方法才执行的
            if (invocation.getArgs().length == 2) {
                if (mapperId.endsWith(".insertObj")) {
                    String[] insertFields = annotation.fields();
                    StringBuilder _insertField_ = new StringBuilder();
                    extracted(insertFields, _insertField_);
                    ((Map<String, Object>) parameterMap).put("_insertObjSql_", _insertField_.toString());
                } else if (mapperId.endsWith(".updateChangeFieldsById")){
                    StringBuilder _updateObj_ = new StringBuilder();
                    Object newObj  = parameterMap.get("newObj");
                    Object OldObj  = parameterMap.get("oldObj");
                    _updateObj_.append("SET ");
                    if (!changedFields(newObj, OldObj, _updateObj_)) return 0;
                    ((Map<String, Object>) parameterMap).put("_updateObj_", _updateObj_.toString());
                }
            }
        }

        return invocation.proceed();

    }

    private static void extracted(String[] insertFields, StringBuilder _insertField_) {
        StringBuilder _insertValue_ = new StringBuilder();
        _insertField_.append("(");
        _insertValue_.append("(");
        int index = insertFields.length - 1;
        for (int i = 0; i < index; i++) {
            _insertField_.append("`").append(camelToUnderline(insertFields[i])).append("`,");
            _insertValue_.append("#{obj.").append(insertFields[i]).append("},");
        }
        _insertField_.append("`").append(camelToUnderline(insertFields[index])).append("`");
        _insertValue_.append("#{obj.").append(insertFields[index]).append("}");
        _insertField_.append(")");
        _insertValue_.append(")");
        _insertField_.append(" VALUE ").append(_insertValue_);
    }

    private boolean changedFields(Object newObj, Object oldObj, StringBuilder updateObj) throws IllegalAccessException {
            Class<?> cls = newObj.getClass();
            boolean flag = false;
            Field[] fields = cls.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                Object n = f.get(newObj);
                log.info(n);
                log.info(f.get(oldObj));
                if(!Objects.equals(n,f.get(oldObj))) {
                    log.info(n);
                    log.info(n);
//                if (!n.equals(f.get(oldObj))) {
                    updateObj.append("`").append(camelToUnderline(f.getName())).append("` = #{newObj.").append(f.getName()).append("} ,");
                    flag = true;
                }
            }
            updateObj.deleteCharAt(updateObj.length()-1);
            return flag;
    }

//    @Override
//    public Object plugin(Object target) {
//        if (target instanceof StatementHandler) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }
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
