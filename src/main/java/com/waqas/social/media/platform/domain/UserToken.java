package com.waqas.social.media.platform.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserToken extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 1173835683206584781L;

    @Column(length = 500)
    private String sessionToken;

    private long sessionTokenExpiry;

    @Column(length = 500)
    private String refreshToken;

    private Long refreshTokenExpiry;

    private boolean active;

    private String error;

    private int refreshTokenCount;
}
