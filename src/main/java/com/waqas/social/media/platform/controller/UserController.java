package com.waqas.social.media.platform.controller;

import com.waqas.social.media.platform.request.UserLoginRequest;
import com.waqas.social.media.platform.request.UserRequest;
import com.waqas.social.media.platform.service.UserService;
import com.waqas.social.media.platform.utils.HttpRequestResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    private static final Logger application = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "register new user")
    @PostMapping(path = "/register")
    public ResponseEntity<HttpRequestResult> registerUser(@Valid @RequestBody final UserRequest userRequest) {
        application.info("calling /users/register");
        return userService.registerUser(userRequest);
    }

    @Operation(summary = "User Login")
    @PostMapping(path = "/login")
    public ResponseEntity<HttpRequestResult> login(@Valid @RequestBody final UserLoginRequest userLoginRequest) {
        application.info("calling /users/login");
        return userService.loginUser(userLoginRequest);
    }

    @Operation(summary = "User Logout")
    @PostMapping("/logout")
    public ResponseEntity<HttpRequestResult> logout(
            @RequestHeader(value = "Authorization") final String authorization) {
        application.info("calling /users/logout");
        return userService.logoutUser(authorization);
    }


    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<HttpRequestResult> getUserByID(@PathVariable Long id) {
        application.info("Get User By Id {}", id);
        return userService.getUserById(id);
    }

}
