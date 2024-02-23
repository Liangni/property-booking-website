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
    Long insertedEcUserId = null;

    @BeforeEach
    void setup() {
        ecUser = EcUserBaseVo.builder()
                .ecUserId(null)
                .ecUserName("test name")
                .ecUserEmail("test email")
                .ecUserHashedPassword("test hashed password")
                .ecUserIntroduction("test introduction")
                .build();

        insertResult = ecUserBaseVoMapper.insert(ecUser);
        insertedEcUserId = (long) ecUser.getEcUserId();
    }

    @Test
    @DisplayName("新增會員資料")
    void addEcUserTest(){
        Assertions.assertEquals(1, insertResult);
    }

    @Test
    @DisplayName("用 id 搜尋會員資料")
    void getEcUserByIdTest(){
        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(insertedEcUserId);

        // 資料庫回應搜尋結果應為一筆資料，且內容與 ecUser 資料相同
        Assertions.assertNotNull(foundEcUser);
        Assertions.assertEquals(ecUser, foundEcUser);
    }

    @Test
    @DisplayName("更新會員資料")
    void updateEcUserTest(){
        EcUserBaseVo updatedEcUserData = EcUserBaseVo.builder()
                .ecUserId(insertedEcUserId)
                .ecUserName("updated test name")
                .ecUserEmail("updated test email")
                .ecUserHashedPassword("updated test hashed password")
                .ecUserIntroduction("updated test introduction")
                .build();


        // 更新會員資料
        Integer updateResult = ecUserBaseVoMapper.updateByPrimaryKey(updatedEcUserData);
        // 資料庫回應更新結果應為 1
        Assertions.assertNotNull(updateResult);
        Assertions.assertEquals(1, updateResult);

        // 搜尋該筆資料
        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(insertedEcUserId);
        // 資料庫回應搜尋結果應為一筆資料，且內容與 updatedEcUserData 相同
        Assertions.assertNotNull(foundEcUser);
        Assertions.assertEquals(updatedEcUserData, foundEcUser);
    }

    @Test
    @DisplayName("更新會員 email")
    void updateEcUserEmailTest(){
        EcUserBaseVo newEcUserData = EcUserBaseVo.builder().build();
        newEcUserData.setEcUserId(insertedEcUserId);
        newEcUserData.setEcUserEmail("updated test email");

        // 更新會員資料
        Integer updateResult = ecUserBaseVoMapper.updateByPrimaryKeySelective(newEcUserData);
        // 資料庫回應更新結果應為 1
        Assertions.assertNotNull(updateResult);
        Assertions.assertEquals(1, updateResult);

        // 搜尋該筆資料
        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(insertedEcUserId);
        // 搜尋到的物件 email 和 ecUser email 不同
        Assertions.assertNotEquals(foundEcUser.getEcUserEmail(), ecUser.getEcUserEmail());

        // 搜尋到的物件 email 以外屬性資料 和 ecUser 仍相同
        Assertions.assertEquals(foundEcUser.getEcUserId(), ecUser.getEcUserId());
        Assertions.assertEquals(foundEcUser.getEcUserName(), ecUser.getEcUserName());
        Assertions.assertEquals(foundEcUser.getEcUserHashedPassword(), ecUser.getEcUserHashedPassword());
        Assertions.assertEquals(foundEcUser.getEcUserIntroduction(), ecUser.getEcUserIntroduction());
    }

    @Test
    @DisplayName("刪除會員資料")
    void deleteEcUserTest(){
        Integer deleteResult = ecUserBaseVoMapper.deleteByPrimaryKey(insertedEcUserId);
        Assertions.assertNotNull(deleteResult);

        EcUserBaseVo foundEcUser = ecUserBaseVoMapper.selectByPrimaryKey(insertedEcUserId);
        Assertions.assertNull(foundEcUser);
    }

}
