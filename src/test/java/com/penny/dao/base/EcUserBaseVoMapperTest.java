package com.penny.dao.base;

import com.penny.vo.base.EcUserBaseVo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("EcUser資料表測試")
class EcUserMapperTest {
    @Autowired
    private EcUserBaseVoMapper ecUserBaseVoMapper;

    EcUserBaseVo ecUser1 = null;
    EcUserBaseVo ecUser2 = null;

    Integer insertResult1 = null;
    Integer insertResult2 = null;

    @BeforeAll
    public void start() {
        ecUser1 = new EcUserBaseVo(
                1L,
                "test name 1",
                "test email 1",
                "test hashed password 1",
                "test introduction 1");

        ecUser2 = new EcUserBaseVo(
                2L,
                "test name 2",
                "test email 2",
                "test hashed password 2",
                "test introduction 2");

        insertResult1 = ecUserBaseVoMapper.insert(ecUser1);
        insertResult2 = ecUserBaseVoMapper.insert(ecUser2);
    }


    @Test
    @DisplayName("新增會員資料")
    void addEcUserTest(){
        Assertions.assertEquals(1, insertResult1);
        Assertions.assertEquals(1, insertResult2);
    }

    @Test
    @DisplayName("用 ecUserId 搜尋會員資料")
    void getAllEcUserTest(){
        EcUserBaseVo foundEcUser1 = ecUserBaseVoMapper.selectByPrimaryKey(1L);

        Assertions.assertNotNull(foundEcUser1);
        Assertions.assertEquals(1L, foundEcUser1.getEcUserId());
        Assertions.assertEquals("test name 1", foundEcUser1.getEcUserName());
        Assertions.assertEquals("test email 1", foundEcUser1.getEcUserName());
        Assertions.assertEquals("test hashed password 1", foundEcUser1.getHashedPassword());
        Assertions.assertEquals("test introduction 1", foundEcUser1.getEcUserIntroduction());
    }


}
