package com.penny.dao;

import com.penny.vo.DistrictVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
@DisplayName("District資料表測試")
public class DistrictVoMapperTest {
    @Autowired
    private DistrictVoMapper districtVoMapper;

    @Test
    @DisplayName("用 district_name 關鍵字搜尋地區")
    void getDistrictById(){
        List<DistrictVo> districtVoList = districtVoMapper.selectByNameKeyword("縣", 0, 5);

        Assertions.assertNotNull(districtVoList);
        Assertions.assertEquals(districtVoList.size(), 5);
    }
}
