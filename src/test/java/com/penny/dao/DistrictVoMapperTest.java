package com.penny.dao;

import com.penny.dao.base.AdministrativeAreaBaseVoMapper;
import com.penny.dao.base.DistrictBaseVoMapper;
import com.penny.vo.DistrictVo;
import com.penny.vo.base.AdministrativeAreaBaseVo;
import com.penny.vo.base.DistrictBaseVo;
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
    @Autowired
    private DistrictBaseVoMapper districtBaseVoMapper;
    @Autowired
    private AdministrativeAreaBaseVoMapper administrativeAreaBaseVoMapper;

    String testKeyword = "test";

    @BeforeEach
    void setup(){
        // 新增行政區劃分層級資料
        AdministrativeAreaBaseVo administrativeArea = AdministrativeAreaBaseVo.builder()
                .administrativeAreaName("test name")
                .administrativeAreaLevel(100L)
                .build();
        administrativeAreaBaseVoMapper.insertSelective(administrativeArea);

        // 獲取新行政區劃分層級資料 id
        long administrativeAreaId =(long) administrativeArea.getAdministrativeAreaId();

        // 以行政區劃分層級資料 id 新增地區資料
        DistrictBaseVo district = DistrictVo.builder()
                .districtName("test name")
                .administrativeAreaId(administrativeAreaId) // 帶入行政區劃分層級資料 id
                .build();

        districtBaseVoMapper.insertSelective(district);
    }


    @Test
    @DisplayName("用 district_name 關鍵字搜尋地區")
    void selectByNameKeywordTest(){
        List<DistrictVo> districtVoList = districtVoMapper.selectByNameKeyword(testKeyword, 0, 5);

        Assertions.assertNotNull(districtVoList);
        Assertions.assertNotEquals(0, districtVoList.size());
    }

    @Test
    @DisplayName("計算用 district_name 關鍵字搜尋地區所找到的資料筆數")
    void selectAndCountByNameKeywordTest(){
        Integer count = districtVoMapper.countSelectByNameKeyword(testKeyword);

        Assertions.assertNotNull(count);
        Assertions.assertNotEquals(0, count);
    }
}
