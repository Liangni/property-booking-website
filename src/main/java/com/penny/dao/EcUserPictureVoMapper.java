package com.penny.dao;

import com.penny.vo.EcUserPictureVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcUserPictureVoMapper {
    /**
     * 根據使用者 ID 查詢對應的使用者圖片。
     *
     * @param ecUserId 使用者的 ID
     * @return 返回使用者圖片
     */
    EcUserPictureVo selectByEcUserId(Long ecUserId);
}
