package com.penny.dao.base;

import com.penny.vo.base.EcUserBaseVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("EcUser資料表測試")
class EcUserMapperTest {
    @Autowired
    private EcUserBaseVoMapper ecUserBaseVoMapper;

    @Test
    @DisplayName("新增會員資料")
    void givenEcUserObject_whenSave_thenReturnSavedEcUser(){

        // given - setup or precondition
        EcUserBaseVo ecUserBaseVo = new EcUserBaseVo(
                null,
                "test name",
                "test@email.com",
                "password",
                "test city",
                1L);

        // when - action or the testing
        int result = ecUserBaseVoMapper.insertSelective(ecUserBaseVo);

        // then - very output
        Assertions.assertEquals(1, result);
    }
}
