package com.waqas.social.media.platform.utils;

public class Constants {

    public static final String ACCOUNT_ID = "ACCOUNT_ID_";
    public static final String SUCCESS_DELETED = "Successfully Deleted";
    public static final String SUCCESS_UPDATED = "Successfully Updated";
    public static final String FAILURE = "Failure";
    public static final String SUCCESS = "Success";
    public static final String NO_RECORD_FOUND = "No Record Found";

    public static final String USER_NOT_ACTIVE = "Your account has been locked ";
    public static final String USER_NOT_FOUND = "User not found for this id : ";
    public static final String USER_NOT_VERIFIED = "User not verified";

    public static final String VALIDATION_ERROR = "Validation Error";
    public static final String CONSTRAINT_ERROR = "Constraint Error";
    public static final String DATA_INTEGRITY_ERROR = "Data integrity Error";

    public static final String LOGOUT_MESSAGE = "Successfully logged out!";

    // Security Config
    public static final String AUTHORIZATION = "Authorization";

    public static final String[] SWAGGER_ALLOWED_PATHS = {"/v3/api-docs","/v3/api-docs/**", "/configuration/ui", "/swagger-resources",
            "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui"
            , "/swagger-ui/**", "/swagger-resources/configuration/security"};

    public static final String[] ALLOWED_PATHS = {"/users/register","/users/login","/users/logout"};

    public static final long USER_SESSION_TOKEN_EXPIRY = 7200000; //current expiry is 2h Time in Millis i.e. 1 minute = 60,000 milliseconds
    public static final long REFRESH_TOKEN_EXPIRY = 14400000;
    public static final String TO_BE_VALIDATE_URL = "to_be_validate_url";

    public static final String JWT_BLACKLIST_KEY = "blacklisted_jwt";
}
