package com.penny.dao;

import com.penny.vo.EcUserVo;
import com.penny.vo.base.EcUserBaseVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EcUserVoMapper {
    /**
     * 根據使用者名稱查詢使用者
     *
     * @param username 要查詢的使用者名稱
     * @return 符合查詢條件的使用者
     */
    EcUserVo selectByUsername(String username);

    /**
     * 根據信箱查詢使用者
     *
     * @param email 要查詢的信箱
     * @return 符合查詢條件的使用者
     */
    EcUserVo selectByEmail(String email);

    /**
     * 根據密碼查詢使用者
     *
     * @param password 要查詢的密碼
     * @return 符合查詢條件的使用者
     */
    EcUserVo selectByPassword(String password);
}

