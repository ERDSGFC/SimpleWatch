package com.qin.pojo.po;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author qin
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
public class UserPO {

    private long id;

    private String userName;

    private String userPassword;

    private byte status;

    private byte loginState;

    private byte type;

    private long loginCount;

    private java.time.OffsetDateTime joinTime;

    private LocalDateTime loginTime;

    private LocalDateTime updatedTime;

    private LocalDateTime deletedTime;
}
