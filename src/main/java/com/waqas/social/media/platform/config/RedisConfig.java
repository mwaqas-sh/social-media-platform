package com.waqas.social.media.platform.config;

import com.waqas.social.media.platform.domain.UserToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.username}")
    private String redisUserName;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Value("${spring.data.redis.withouttls}")
    private Boolean redisWithoutTls;

    @Bean
    public RedisTemplate<String, UserToken> redisUserTokenTemplate() {
        final RedisTemplate<String, UserToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(String.class));

        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
        return redisTemplate;

    }

    private JedisConnectionFactory getJedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        if (redisUserName != null && !redisUserName.isEmpty()) {
            configuration.setUsername(redisUserName);
        }
        if (redisPassword != null && !redisPassword.isEmpty()) {
            configuration.setPassword(redisPassword);
        }
        JedisClientConfiguration jedisClientConfiguration;
        if (Boolean.TRUE.equals(redisWithoutTls))
            jedisClientConfiguration = JedisClientConfiguration.builder().build();
        else
            jedisClientConfiguration = JedisClientConfiguration.builder().useSsl().build();

        JedisConnectionFactory factory = new JedisConnectionFactory(configuration, jedisClientConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    @Bean
    public RedisTemplate<String, Boolean> redisAuthorizationTemplate() {
        final RedisTemplate<String, Boolean> redisTemplate = new RedisTemplate<>();
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(String.class));
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
        return redisTemplate;

    }

    @Bean
    public RedisTemplate<String, List<String>> redisEndpointsTemplate() {
        final RedisTemplate<String, List<String>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(List.class));
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());
        return redisTemplate;
    }

}
