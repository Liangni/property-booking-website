package com.penny.service;

import com.penny.dao.EcUserVoMapper;
import com.penny.vo.EcUserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcUserService {
    private final EcUserVoMapper ecUserVoMapper;

    /**
     * 獲取當前登入使用者的資訊。
     *
     * @return 當前登入使用者的資訊
     */
    public EcUserVo getLoginUser() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ecUserVoMapper.selectByUsername(user.getUsername());
    }
}
