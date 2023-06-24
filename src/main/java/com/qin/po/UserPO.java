package com.qin.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2023-06-24 12:45:31
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserPO implements Serializable {
    private static final long serialVersionUID = -18388388392373006L;
    /**
     * 主键id
     */
    private long id;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 账号状态: 1：正常 0：异常
     */
    private byte status;
    /**
     * 登录状态：0：注册未登录，1：登录，-1：登出
     */
    private byte loginState;
    /**
     * 用户身份：0：无限，1：普通用户
     */
    private byte type;
    /**
     * 登录次数
     */
    private long loginCount;
    /**
     * 注册时间
     */
    private LocalDateTime joinTime;
    /**
     * 最后一次登录时间
     */
    private LocalDateTime loginTime;
    
    private LocalDateTime updatedTime;
    
    private LocalDateTime deletedTime;
}

