package com.penny.security;

import com.penny.dao.EcUserVoMapper;
import com.penny.exception.AuthenticationException;
import com.penny.vo.EcUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final EcUserVoMapper ecUserVoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("enter loadUserByUsername");
        EcUserVo ecUserVo = Optional.ofNullable(ecUserVoMapper.selectByUsername(username))
                .orElseThrow(() -> new AuthenticationException("Username not found"));
        // 下方 User 類是 UserDetails interface 的 implementation
        // 第三個參數為 Role，因應用程式不需要驗證 Role 權限，因此帶 new ArrayList<>()
        return new User(ecUserVo.getEcUserUsername(), ecUserVo.getEcUserPassword(), new ArrayList<>());
    }

}
