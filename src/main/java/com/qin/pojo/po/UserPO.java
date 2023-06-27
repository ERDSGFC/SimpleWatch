package com.qin.pojo.po;

import com.qin.common.BasePO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author qinhanhan
 * @since 2023-06-24 13:04:18
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserPO extends BasePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -10868950213536710L;

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

