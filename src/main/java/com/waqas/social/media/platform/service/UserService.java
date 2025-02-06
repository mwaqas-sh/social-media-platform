package com.waqas.social.media.platform.service;

import com.waqas.social.media.platform.domain.User;
import com.waqas.social.media.platform.domain.UserToken;
import com.waqas.social.media.platform.repository.UserRepository;
import com.waqas.social.media.platform.repository.UserTokenRepository;
import com.waqas.social.media.platform.request.UserLoginRequest;
import com.waqas.social.media.platform.request.UserRequest;
import com.waqas.social.media.platform.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.waqas.social.media.platform.utils.Constants.*;


@Service
public class UserService {

    private static final Logger application = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final UserTokenRepository userTokenRepository;

    private final RedisUtility redisUtility;

    private final PasswordEncoder passwordEncoder;
    @Value(value = "${jwt.secret}")
    String jwtSecret;

    public UserService(UserRepository userRepository, UserTokenRepository userTokenRepository, RedisUtility redisUtility, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.redisUtility = redisUtility;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<HttpRequestResult> registerUser(UserRequest userRequest) {
        User existingUser = userRepository.findByEmail(userRequest.getEmail());
        if (existingUser != null) {
            return Utilities.sendHttpBadRequestResponse(Constants.FAILURE, "A duplicate email was found. Please use another email address", "");
        }
        User user = createUser(userRequest);
        return Utilities.sendHttpSuccessResponse(SUCCESS, user, "");
    }

    private User createUser(UserRequest userRequest) {
        long currentDate = Instant.now().toEpochMilli();
        User user = new User();
        user.setCreatedDate(currentDate);
        user.setLastModifiedDate(currentDate);
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setProfilePicture(userRequest.getProfilePicture());
        user.setBio(userRequest.getBio());
        user.setActive(true);
        userRepository.save(user);
        return user;
    }

    public ResponseEntity<HttpRequestResult> getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            application.info(" Get User Detail successfully");
            return Utilities.sendHttpSuccessResponse(SUCCESS, user, "");
        }
        return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_FOUND, null, "");
    }

    public ResponseEntity<HttpRequestResult> loginUser(final UserLoginRequest userLoginRequest) {
        String userEmail = userLoginRequest.getEmail();
        Date currentDate = new Date();
        application.debug("Authenticating user");

        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return Utilities.sendHttpBadRequestResponse(Constants.NO_RECORD_FOUND, null, "");
        }

        if (!user.isActive() && (currentDate.getTime() - user.getLastModifiedDate() < 86400000)) {
            application.info("User is no more active");
            return Utilities.sendHttpBadRequestResponse(Constants.USER_NOT_ACTIVE, null, "");
        }

        if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
            user = getRegisteredUserToken(user);
            user = userRepository.save(user);

            application.info("User verified");
            return Utilities.sendHttpSuccessResponse(SUCCESS, user, "");
        } else {
            application.info("User not verified");
            return Utilities.sendHttpBadRequestResponse(USER_NOT_VERIFIED, null, "");
        }
    }

    public ResponseEntity<HttpRequestResult> logoutUser(final String authorization) {
        final String token = authorization.split(" ")[1];
        final UserToken userToken = userTokenRepository.findBySessionTokenAndActiveTrue(token);
        if (userToken != null) {
            userToken.setActive(Boolean.FALSE);
            userTokenRepository.save(userToken);
            redisUtility.addJwtTokenToBlacklist(token);
        }
        application.info("users logout successfully");
        return Utilities.sendHttpSuccessResponse(Constants.LOGOUT_MESSAGE, null, "");
    }


    protected User getRegisteredUserToken(User user) {
        final UserToken userToken = new UserToken();
        String jwtToken = generateJWTToken(user);
        userToken.setSessionToken(jwtToken);
        userToken.setRefreshToken(jwtToken);
        userToken.setSessionTokenExpiry(USER_SESSION_TOKEN_EXPIRY);
        userToken.setRefreshTokenExpiry(Constants.REFRESH_TOKEN_EXPIRY);
        userToken.setActive(true);
        userToken.setRefreshTokenCount(1);
        userToken.setCreatedDate(Instant.now().toEpochMilli());
        final List<UserToken> userTokens = user.getUserTokens() != null ? user.getUserTokens()
                : new ArrayList<>();
        userTokens.add(userToken);
        user.setActive(Boolean.TRUE);
        user.setUserTokens(userTokens);
        user = userRepository.save(user);
        user.setUserToken(userToken);
        return user;
    }

    public String generateJWTToken(User user) {
        // Create and sign JWT token
        application.info("generating JWTToken with user id: {}", user.getId());

        return Jwts.builder()
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + USER_SESSION_TOKEN_EXPIRY))
                .signWith(JwtHelper.getSignInKey(jwtSecret), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getJwtClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public long getRemainingExpiryTimeFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(JwtHelper.getSignInKey(jwtSecret)).parseClaimsJws(token).getBody();
            long nowInMillis = Instant.now().toEpochMilli();
            Instant expiryTimeInstant = Instant.ofEpochMilli(claims.getExpiration().getTime());
            long expiryInMillis = expiryTimeInstant.toEpochMilli();
            return Math.max(expiryInMillis - nowInMillis, 0); // Ensures non-negative value
        } catch (JwtException ex) {
            return 0;
        }
    }


}
