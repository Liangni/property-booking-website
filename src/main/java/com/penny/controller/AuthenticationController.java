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

    /**
     * 註冊新使用者。
     *
     * @param request 註冊請求
     * @return 包含認證回應實體的 ResponseEntity
     */
    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody EcUserBaseVo request
    ) {

        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * 使用者登入。
     *
     * @param request 登入請求
     * @return 包含認證回應實體的 ResponseEntity
     */
    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody EcUserBaseVo request
    )  {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
