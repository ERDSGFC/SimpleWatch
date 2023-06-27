package com.qin.common;

import java.lang.annotation.*;

/**
 * 数据库表注解 表明当前Mapper 的表信息
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableInfo {
    /**
     * 表名称
     * @return table name
     */
    String value();

    /**
     * 插入时的字段
     * @return insert sql fields
     */
    String[] fields();
    /**
     * 主键 名称
     * @return table primary key name
     */
    String primaryKey() default "id";

    /**
     * 是否自动接管时间操作 包括删除修改添加时间
     * @return 是否自动接管 时间
     */
    boolean timeFlag() default false;

    /**
     * 数据添加时间
     * @return data created time
     */
    String createdTimeField() default "created_time";

    /**
     * 数据修改时间
     * @return data updated time
     */
    String updatedTimeField() default "updated_time";
}
