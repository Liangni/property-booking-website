package com.penny.service;

import com.penny.dao.EcUserVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.UnauthorizedException;
import com.penny.vo.EcUserVo;
import com.penny.vo.base.PropertyBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcUserService {
    private final EcUserVoMapper ecUserVoMapper;

    /**
     * 驗證房源擁有權。
     *
     * @param hostId 出租人 ID
     * @throws UnauthorizedException 如果當前登入使用者沒有操作權限
     */
    public void validatePropertyOwnership(Long hostId) {
        // 取得當前登入使用者
        EcUserVo loginUser = getLoginUser();

        // 檢查當前登入使用者是否為房源擁有者
        if (!loginUser.getEcUserId().equals(hostId)) {
            throw new UnauthorizedException("login user is not authorized for the operation");
        }
    }

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
