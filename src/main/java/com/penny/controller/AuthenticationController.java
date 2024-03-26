package com.penny.controller;

import com.penny.response.AuthenticationResponse;
import com.penny.service.AuthenticationService;
import com.penny.vo.base.EcUserBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody EcUserBaseVo request
    ) {

        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody EcUserBaseVo request
    ) throws AuthenticationException {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
