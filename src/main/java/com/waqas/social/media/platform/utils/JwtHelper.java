package com.waqas.social.media.platform.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtHelper {
    public static Integer getUserIdByToken(String jwtSecret) {
        String authorizationHeader = Utilities.getAuthorizationHeaderFromRequest();
        String token = authorizationHeader.split(" ")[1];
        return getJwtClaims(token, jwtSecret).get("userId", Integer.class);

    }

    public static Claims getJwtClaims(String token, String jwtSecret) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey(jwtSecret))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static Key getSignInKey(String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
