package com.waqas.social.media.platform.utils;

import com.waqas.social.media.platform.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.waqas.social.media.platform.utils.Constants.JWT_BLACKLIST_KEY;

@Service
public class RedisUtility {

    @Qualifier("redisEndpintsTemplate")
    private final RedisTemplate<String, List<String>> redisEndpointsByAccountType;

    @Qualifier("getStringRedisTemplate")
    private final RedisTemplate<String, String> redisJwtBlacklistTemplate;

    private final UserService userService;

    public RedisUtility(RedisTemplate<String, List<String>> redisEndpointsByAccountType, RedisTemplate<String, String> redisJwtBlacklistTemplate, @Lazy UserService userService) {
        this.redisEndpointsByAccountType = redisEndpointsByAccountType;
        this.redisJwtBlacklistTemplate = redisJwtBlacklistTemplate;
        this.userService = userService;
    }

    public void setEndpointsByAccountTypeId(Long id, List<String> endpoints) {
        String key = Constants.ACCOUNT_ID + id;
        deleteEndpointByAccountTypeId(id);
        redisEndpointsByAccountType.opsForValue().set(key, endpoints);
    }

    public List<String> getEndpointByAccountTypeId(Long id) {
        return redisEndpointsByAccountType.opsForValue().get(Constants.ACCOUNT_ID + id);
    }

    public void deleteEndpointByAccountTypeId(Long id) {
        String key = Constants.ACCOUNT_ID + id;
        redisEndpointsByAccountType.delete(key);
    }

    public void addJwtTokenToBlacklist(String jwtToken) {
        redisJwtBlacklistTemplate.opsForValue().set(JWT_BLACKLIST_KEY + ":" + jwtToken, jwtToken);
        long remainingTokenExpiryTimeInMillis = userService.getRemainingExpiryTimeFromToken(jwtToken);
        if (remainingTokenExpiryTimeInMillis > 0) {
            redisJwtBlacklistTemplate.expire(JWT_BLACKLIST_KEY + ":" + jwtToken, remainingTokenExpiryTimeInMillis, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isJwtTokenBlacklisted(String jwtToken) {
        return Boolean.TRUE.equals(redisJwtBlacklistTemplate.hasKey(JWT_BLACKLIST_KEY + ":" + jwtToken));
    }
}
