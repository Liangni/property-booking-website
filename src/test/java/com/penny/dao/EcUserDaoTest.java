package com.penny.dao;

import com.penny.vo.EcUserVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("EcUser資料表測試")
class EcUserDaoTest {

    @Autowired
    private EcUserDao ecUserDao;

    @Test
    @DisplayName("檢核會員資料是否存在")
    void testEcUser() {
        EcUserVo ecUser = ecUserDao.getUserByPk(1L);
        assertNotNull(ecUser);
    }
}