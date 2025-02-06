package com.waqas.social.media.platform.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ValidateToken {
    private long userAccountId;
    private boolean isAllowed;
}