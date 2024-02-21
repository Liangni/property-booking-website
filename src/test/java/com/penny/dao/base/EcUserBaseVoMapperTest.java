package com.penny.dao.base;

import com.penny.vo.base.EcUserBaseVo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@DisplayName("EcUser資料表測試")
class EcUserBaseVoMapperTest {
    @Autowired
    private EcUserBaseVoMapper ecUserBaseVoMapper;

    EcUserBaseVo ecUser = null;
    Integer insertResult = null;

    @BeforeEach
    void setup() {
        ecUser = new EcUserBaseVo(
                null,
                "test name 1",
                "test email 1",
                "test hashed password 1",
                "test introduction 1");

        insertResult = ecUserBaseVoMapper.insertSelective(ecUser);
    }

    @Test
    @DisplayName("新增會員資料")
    void addEcUserTest(){
        Assertions.assertEquals(1, insertResult);
    }

    @Test
    @DisplayName("用 id 搜尋會員資料")
    void getEcUserByIdTest(){
        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(1L);
        // 資料庫回應搜尋結果應為一筆資料，且內容與 ecUser 資料相同
        Assertions.assertNotNull(foundEcUser);
        Assertions.assertEquals(ecUser, foundEcUser);
    }

    @Test
    @DisplayName("更新會員資料")
    void updateEcUserTest(){
        EcUserBaseVo updatedEcUserData = new EcUserBaseVo(
                1L,
                "updated test name",
                "updated test email",
                "updated test hashed password",
                "updated test introduction"
        );

        // 更新會員資料
        Integer updateResult = ecUserBaseVoMapper.updateByPrimaryKey(updatedEcUserData);
        // 資料庫回應更新結果應為 1
        Assertions.assertNotNull(updateResult);
        Assertions.assertEquals(1, updateResult);

        // 搜尋該筆資料
        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(1L);
        // 資料庫回應搜尋結果應為一筆資料，且內容與 updatedEcUserData 相同
        Assertions.assertNotNull(foundEcUser);
        Assertions.assertEquals(updatedEcUserData, foundEcUser);
    }

    @Test
    @DisplayName("更新會員 email")
    void updateEcUserEmailTest(){
        EcUserBaseVo updatedEcUserData = new EcUserBaseVo();
        updatedEcUserData.setEcUserId(1L);
        updatedEcUserData.setEmail("updated test email");

        // 更新會員資料
        Integer updateResult = ecUserBaseVoMapper.updateByPrimaryKeySelective(updatedEcUserData);
        // 資料庫回應更新結果應為 1
        Assertions.assertNotNull(updateResult);
        Assertions.assertEquals(1, updateResult);

        // 搜尋該筆資料
        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(1L);
        // 搜尋到的物件 email 和 ecUser email 不同
        Assertions.assertNotEquals(foundEcUser.getEmail(), ecUser.getEmail());

        // 搜尋到的物件 email 以外屬性資料 和 ecUser 仍相同
        Assertions.assertEquals(foundEcUser.getEcUserId(), ecUser.getEcUserId());
        Assertions.assertEquals(foundEcUser.getEcUserName(), ecUser.getEcUserName());
        Assertions.assertEquals(foundEcUser.getHashedPassword(), ecUser.getHashedPassword());
        Assertions.assertEquals(foundEcUser.getEcUserIntroduction(), ecUser.getEcUserIntroduction());
    }

    @Test
    @DisplayName("刪除會員資料")
    void deleteEcUserTest(){
        Integer deleteResult = ecUserBaseVoMapper.deleteByPrimaryKey(1L);
        Assertions.assertNotNull(deleteResult);

        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(1L);
        Assertions.assertNull(foundEcUser);
    }

}