package com.qin.common;

import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
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

//@Component
@Intercepts({
//    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    // 拦截不到这个有缓存的查询 , 不知道为什么
//    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "createCacheKey", args = {MappedStatement.class, Object.class, RowBounds.class, BoundSql.class }),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class ,CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "queryCursor", args = {MappedStatement.class, Object.class, RowBounds.class}),
//    @Signature(type = Executor.class, method = "flushStatements", args = {}),
//    @Signature(type = Executor.class, method = "commit", args = {boolean.class}),
//    @Signature(type = Executor.class, method = "rollback", args = {boolean.class}),
//    @Signature(type = Executor.class, method = "isCached", args = {MappedStatement.class, CacheKey.class }),
//    @Signature(type = Executor.class, method = "clearLocalCache", args = {}),
//    @Signature(type = Executor.class, method = "deferLoad", args = {MappedStatement.class, MetaObject.class, String.class, CacheKey.class, Class.class}),
//    @Signature(type = Executor.class, method = "getTransaction", args = {}),
//    @Signature(type = Executor.class, method = "close", args = {boolean.class}),
//    @Signature(type = Executor.class, method = "isClosed", args = {}),
//    @Signature(type = Executor.class, method = "setExecutorWrapper", args = {Executor.class}),
//
//    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
//    @Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class }),
//    @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class }),
//    @Signature(type = StatementHandler.class, method = "update", args = {Statement.class }),
//    @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class }),
//    @Signature(type = StatementHandler.class, method = "queryCursor", args = {Statement.class }),
//    @Signature(type = StatementHandler.class, method = "getBoundSql", args = {}),
//    @Signature(type = StatementHandler.class, method = "getParameterHandler", args = {}),
//
//    @Signature(type = ParameterHandler.class, method = "getParameterObject", args = {}),
//    @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
//
//
//    @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class }),
//    @Signature(type = ResultSetHandler.class, method = "handleCursorResultSets", args = {Statement.class }),
//    @Signature(type = ResultSetHandler.class, method = "handleOutputParameters", args = {CallableStatement.class }),

})
@Log4j2
public class MybatisIntercept implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target= invocation.getTarget();
        log.info("invocation function name: " + invocation.getMethod().getName() +"===" + target.getClass());
        log.info(invocation.getArgs().length);
        if ( target instanceof Executor) {
            Object[] args = invocation.getArgs();
            Object mappedStatement = args[0];
            if (mappedStatement instanceof MappedStatement) {
                BoundSql boundSql = ((MappedStatement) mappedStatement).getBoundSql(args[1]);
                String mapperId = ((MappedStatement) mappedStatement).getId();
                Class<?> mapper = Class.forName(mapperId.substring(0, mapperId.lastIndexOf(".") + 1));
                final TableInfo annotation = mapper.getAnnotation(TableInfo.class);
                changeSqlByTableInfo((MappedStatement) mappedStatement, annotation);
            }
//            return invocation.getMethod().invoke(target, args);
//        } else {
        }

        return invocation.proceed();

    }

    private void changeSqlByTableInfo (MappedStatement mappedStatement, TableInfo annotation) throws IllegalAccessException {
        if (annotation != null) {
            final String tableName = annotation.value();
            final String primaryKey = annotation.primaryKey();
            final boolean timeFlag = annotation.timeFlag();
            final String createdTimeField = annotation.createdTimeField();
            final String updatedTimeField = annotation.updatedTimeField();
            int table_index;
            SqlSource sqlSource = mappedStatement.getSqlSource();
            Class<? extends MappedStatement> mappedStatementClass = mappedStatement.getClass();
            Field sqlSourceField = null;
            for (Field field : mappedStatementClass.getFields()) {
                if (field.getName().equals("sqlSource")) sqlSourceField = field;
            }
            if (sqlSourceField != null) {
                sqlSourceField.setAccessible(true);
//                BoundSql boundSql = sqlSource.getBoundSql();
//                sqlSource.ge
//                table_index = newSql.indexOf("=table=");
//                int created_i = 0;
//                final LocalDateTime now = LocalDateTime.now();
//                if (table_index != -1 && timeFlag && SqlCommandType.INSERT.equals(sqlCommandType)) {
//                    created_i = newSql.indexOf(")");
//                    if (created_i != -1) newSql.replace(created_i, created_i + 1, ",`" + createdTimeField + "`, `"+updatedTimeField+"` )");
//                    created_i = newSql.lastIndexOf("),(");
//                    if(created_i == -1) newSql.replace(newSql.length() - 1, newSql.length(), ",'" +
//                            LocalDateUtils.dateTimeFormatter.format(now) + "', '"+
//                            LocalDateUtils.dateTimeFormatter.format(now) + "' )");
//                } else if(table_index != -1 && timeFlag && SqlCommandType.UPDATE.equals(sqlCommandType)) {
//                    final String updatedTime = "SET `"+updatedTimeField+"` = '"+ LocalDateUtils.dateTimeFormatter.format(now) + "',";
//                    final int i = newSql.indexOf(updatedTimeField);
//                    if (i < 0 || i > newSql.indexOf("WHERE")) {
//                        final int updated_i = newSql.indexOf("SET");
//                        if(updated_i != -1) newSql.replace(updated_i,updated_i + "set".length(), updatedTime);
//                    }
//                }
//
//                if(table_index != -1) newSql.replace(table_index,table_index + "=table=".length(), tableName);
//
//                if (created_i == -1) {
//                    sqlField.set(boundSql, newSql.toString().replaceAll("=id=", primaryKey).replaceAll("\\),\\(",
//                            ",'" + LocalDateUtils.dateTimeFormatter.format(now) + "', '"+ LocalDateUtils.dateTimeFormatter.format(now) + "' ),("));
//                } else {
//                    sqlField.set(boundSql, newSql.toString().replaceAll("=id=", primaryKey));
//                }
            }
        }
    }

//    @Override
//    public Object plugin(Object target) {
//        if (target instanceof StatementHandler) {
//            return Plugin.wrap(target, this);
//        }
//        return target;
//    }


//    public static Map<String, Object> reflect(Object obj) throws IllegalAccessException {
//        HashMap<String, Object> hashMap = new HashMap<>();
//        Class<?> cls = obj.getClass();
//        Field[] fields = cls.getDeclaredFields();
//        for (Field f : fields) {
//            f.setAccessible(true);
//            MyIgnore ignore = f.getAnnotation(MyIgnore.class);
//            Object o = f.get(obj);
//            if(ignore == null ||
//                    (!ignore.value() &&
//                            (   (!(o instanceof Number) && o != null) ||
//                                    (o != null && ((Number)o).intValue() != 0)
//                            )
//                    )
//            ) {
//                hashMap.put(Common.camelToUnderline(f.getName()), o);
//            }
//
//        }
//        return  hashMap;
//    }

}
