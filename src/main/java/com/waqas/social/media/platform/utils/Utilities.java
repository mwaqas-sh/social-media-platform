package com.waqas.social.media.platform.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.security.SecureRandom;

public class Utilities {

    public static ResponseEntity<HttpRequestResult> sendHttpSuccessResponse(final String description, final Object data,
                                                                            final String refId) {
        final HttpRequestResult result = new HttpRequestResult();
        result.setStatus(HttpStatus.OK.value() + "");
        result.setDescription(description);
        result.setData(data);
        result.setRefId(refId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static ResponseEntity<HttpRequestResult> sendHttpBadRequestResponse(final String description, final Object data,
                                                                               final String refId) {
        final HttpRequestResult result = new HttpRequestResult();
        result.setStatus(HttpStatus.BAD_REQUEST.value() + "");
        result.setDescription(description);
        result.setData(data);
        result.setRefId(refId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static ResponseEntity<HttpRequestResult> sendHttpNotFoundResponse(final String description, final Object data,
                                                                             final String refId) {
        final HttpRequestResult result = new HttpRequestResult();
        result.setStatus(HttpStatus.NOT_FOUND.value() + "");
        result.setDescription(description);
        result.setData(data);
        result.setRefId(refId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static HttpServletRequest getRequest() {
        try {
            RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
            if (attribs != null) {
                return ((ServletRequestAttributes) attribs).getRequest();
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static String generateRandomPassword() {

        String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
        String DIGITS = "0123456789";
        String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";

        String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL_CHARACTERS;
        SecureRandom RANDOM = new SecureRandom();

        int length = RANDOM.nextInt(9) + 8; // Password length between 8 and 16

        StringBuilder password = new StringBuilder(length);

        // Add one random character from each required category
        password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));

        // Fill the remaining characters
        for (int i = 4; i < length; i++) {
            password.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
        }

        // Shuffle the password to ensure randomness
//        return shuffleString(password.toString());
        StringBuilder result = new StringBuilder(password.toString().length());
        int[] indexArray = new int[password.toString().length()];
        for (int i = 0; i < password.toString().length(); i++) {
            indexArray[i] = i;
        }

        for (int i = password.toString().length() - 1; i >= 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            result.append(password.toString().charAt(indexArray[index]));
            indexArray[index] = indexArray[i];
        }

        return result.toString();
    }

    public static String getAuthorizationHeaderFromRequest() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getHeader(Constants.AUTHORIZATION);
        }
        return null;
    }

}
