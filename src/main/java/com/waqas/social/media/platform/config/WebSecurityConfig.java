package com.waqas.social.media.platform.config;

import com.waqas.social.media.platform.service.UserService;
import com.waqas.social.media.platform.utils.Constants;
import com.waqas.social.media.platform.utils.RedisUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final UserService userService;

    private final RedisUtility redisUtility;

    public WebSecurityConfig(UserService userService, RedisUtility redisUtility) {
        this.userService = userService;
        this.redisUtility = redisUtility;
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().requestMatchers(Constants.ALLOWED_PATHS)
                    .requestMatchers("/actuator/health");
            web.ignoring().requestMatchers(Constants.SWAGGER_ALLOWED_PATHS);
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(configurationSource()));
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new RequestFilter(userService, redisUtility), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated());
        return http.build();
    }

    private UrlBasedCorsConfigurationSource configurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setMaxAge(Long.MAX_VALUE);
        config.addExposedHeader(HttpHeaders.LOCATION);
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
