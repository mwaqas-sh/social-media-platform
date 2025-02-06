package com.waqas.social.media.platform.config;

import com.waqas.social.media.platform.domain.ValidateToken;
import com.waqas.social.media.platform.service.UserService;
import com.waqas.social.media.platform.utils.Constants;
import com.waqas.social.media.platform.utils.JwtHelper;
import com.waqas.social.media.platform.utils.RedisUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestFilter extends OncePerRequestFilter {

    private static final Logger application = LoggerFactory.getLogger(RequestFilter.class);
    private final UserService userService;
    private final RedisUtility redisUtility;

    private UsernamePasswordAuthenticationToken authentication = null;

    private List<String> endpoints = new ArrayList();

    @Value(value = "${jwt.secret}")
    String jwtSecret;

    public RequestFilter(UserService userService, RedisUtility redisUtility) {
        this.userService = userService;
        this.redisUtility = redisUtility;
        endpoints.add("/users/register");
        endpoints.add("/users/login");
        endpoints.add("/posts/test");
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        try {
            final ValidateToken validateToken = validateToken(request);
            if (validateToken.isAllowed()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } else {
                application.info("JWT token validation Failed {}", request.getRequestURI());
                SecurityContextHolder.clearContext();
                if (validateToken.getUserAccountId() == -2L) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } catch (final ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            application.info("exception () {} ", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private ValidateToken validateJwtToken(String token, String requestedEndpoint) {
        boolean isAuthroized = false;
        Long userIdClaim = -1L;
        Long accountTypeClaim = -1L;
        try {
            Claims claims = userService.getJwtClaims(token);
            userIdClaim = claims.get("userId", Long.class);
            application.info("userId claim: {}", userIdClaim);
            boolean jwtTokenBlacklisted = redisUtility.isJwtTokenBlacklisted(token);
            if (!jwtTokenBlacklisted) {
                if (endpoints.stream()
                        .anyMatch(endpoint -> endpoint.contains(requestedEndpoint))) {
                    isAuthroized = true;
                }
            } else {
                application.info("jwt token is blacklisted {}", token);
            }

        } catch (Exception ex) {
            // Token signature is invalid
            application.error("jwt Token signature is invalid {}", ex.getMessage());
        }
        if (isAuthroized) {
            getAuthentication(token);
            return new ValidateToken(accountTypeClaim, true);
        } else {
            application.info("returning jwt with -1 {}", requestedEndpoint);
            return new ValidateToken(accountTypeClaim, false); // Token is valid, but the requested endpoint is not allowed
        }
    }

    private ValidateToken validateToken(final HttpServletRequest request) {
        try {
            final String authorization = request.getHeader(Constants.AUTHORIZATION);
            if (authorization != null && !authorization.isEmpty()) {
                final String token = authorization.split(" ")[1];
                String requestToBeFiltered = request.getHeader(Constants.TO_BE_VALIDATE_URL) != null
                        ? request.getHeader(Constants.TO_BE_VALIDATE_URL)
                        : request.getRequestURI();
                if (requestToBeFiltered.matches(".*\\d.*")) {
                    requestToBeFiltered = requestToBeFiltered.replaceAll("[0-9]+", "");
                }
                application.info("requestToBeFiltered {}", requestToBeFiltered);

                return validateJwtToken(token, requestToBeFiltered);
            } else {
                application.info("Auth header missing {}", request.getRequestURI());
            }
        } catch (Exception ex) {
            application.error("Exception ", ex);
        }
        return new ValidateToken(-1, false);
    }

    private void getAuthentication(final String token) {
        authentication = new UsernamePasswordAuthenticationToken(token, null, new ArrayList<>());
    }

}
