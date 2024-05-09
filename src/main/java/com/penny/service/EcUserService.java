package com.penny.service;

import com.penny.dao.EcUserVoMapper;
import com.penny.dao.base.EcUserBaseVoMapper;
import com.penny.exception.AuthorizationException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.response.ReadEcUserResponse;
import com.penny.vo.EcUserVo;
import com.penny.vo.base.EcUserBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EcUserService {
    private final EcUserBaseVoMapper ecUserBaseVoMapper;
    private final EcUserVoMapper ecUserVoMapper;

    /**
     * 根據使用者 ID 獲取使用者資訊。
     *
     * @param ecUserId 使用者的 ID
     * @return 返回包含使用者資訊的
     * @throws ResourceNotFoundException 如果找不到指定 ID 的使用者，則拋出資源未找到異常
     */
    public ReadEcUserResponse getEcUser(Long ecUserId) {
        EcUserBaseVo ecUserBaseVo = Optional.ofNullable(ecUserBaseVoMapper.selectByPrimaryKey(ecUserId))
                .orElseThrow(() -> new ResourceNotFoundException("ecUser with id %s not found".formatted(ecUserId)));

        return ReadEcUserResponse
                .builder()
                .ecUserName(ecUserBaseVo.getEcUserName())
                .ecUserUsername(ecUserBaseVo.getEcUserUsername())
                .ecUserIntroduction(ecUserBaseVo.getEcUserIntroduction())
                .build();
    }

    /**
     * 驗證房源擁有權。
     *
     * @param hostId 出租人 ID
     * @throws AuthorizationException 如果當前登入使用者沒有操作權限
     */
    public void validatePropertyOwnership(Long hostId) {
        // 取得當前登入使用者
        EcUserVo loginUser = getLoginUser();

        // 檢查當前登入使用者是否為房源擁有者
        if (!loginUser.getEcUserId().equals(hostId)) {
            throw new AuthorizationException("login user is not authorized for the operation");
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
