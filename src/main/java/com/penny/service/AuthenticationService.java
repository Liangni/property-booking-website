package com.penny.service;

import com.penny.dao.EcUserVoMapper;
import com.penny.dao.base.EcUserBaseVoMapper;
import com.penny.exception.ResourceDuplicateException;
import com.penny.response.AuthenticationResponse;
import com.penny.security.JwtUtil;
import com.penny.vo.EcUserVo;
import com.penny.vo.base.EcUserBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final EcUserBaseVoMapper ecUserBaseVoMapper;

    private final EcUserVoMapper ecUserVoMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    /**
     * 註冊新使用者並返回相應的身份驗證響應。
     *
     * @param request 註冊請求，包含新使用者的詳細資訊
     * @return 身份驗證回應物件，包含生成的 JWT 字串
     */
    public AuthenticationResponse register(EcUserBaseVo request) {
        // 檢查帳號, 密碼, email是否重複
        EcUserVo existUser;
        String requestUsername = request.getEcUserUsername();
        existUser = ecUserVoMapper.selectByUsername(requestUsername);

        String encodedRequestPassword = passwordEncoder.encode(request.getEcUserPassword()); // 加密密碼
        if (existUser == null) existUser = ecUserVoMapper.selectByPassword(encodedRequestPassword);

        String requestEmail = request.getEcUserEmail();
        if(existUser == null) existUser =ecUserVoMapper.selectByEmail(requestEmail);

        if(existUser != null) throw new ResourceDuplicateException("username, password, or email already exists");

        // 創建新的 EcUserBaseVo 物件，將請求中的使用者詳細資訊設定為新使用者的屬性
        EcUserBaseVo ecUserBaseVo= EcUserBaseVo
                .builder()
                .ecUserUsername(requestUsername)
                .ecUserPassword(encodedRequestPassword)
                .ecUserName(request.getEcUserName())
                .ecUserEmail(requestEmail)
                .build();

        // 將新使用者的詳細資訊插入到資料庫中
        ecUserBaseVoMapper.insertSelective(ecUserBaseVo);

        // 使用 JWT 工具類生成 JWT 字串，用於新使用者的身份驗證
        String token = jwtUtil.generateToken(ecUserBaseVo);

        return new AuthenticationResponse(token);
    }

    /**
     * 進行使用者身份驗證並返回相應的身份驗證響應。
     *
     * @param request 身份驗證請求，包含使用者名稱和密碼
     * @return 身份驗證回應物件，包含生成的 JWT 字串
     */
    public AuthenticationResponse authenticate(EcUserBaseVo request){
        // 進行使用者身份驗證
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEcUserUsername(),
                        request.getEcUserPassword()
                )
        );

        // 根據使用者名稱從資料庫中檢索使用者詳細資訊
        EcUserVo ecUserVo = Optional.ofNullable(ecUserVoMapper.selectByUsername(request.getEcUserUsername()))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        // 使用 JWT 工具類生成 JWT 字串，用於使用者的身份驗證
        String token = jwtUtil.generateToken(ecUserVo);

        // 返回身份驗證回應物件，其中包含生成的 JWT 字串
        return new AuthenticationResponse(token);
    }
}
