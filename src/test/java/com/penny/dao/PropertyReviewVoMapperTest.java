package com.penny.dao;

import com.penny.dao.base.*;
import com.penny.vo.base.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
@Transactional
@DisplayName("PropertyReview 資料表測試")
public class PropertyReviewVoMapperTest {
    @Autowired
    EcUserBaseVoMapper ecUserBaseVoMapper;
    @Autowired
    AdministrativeAreaBaseVoMapper administrativeAreaBaseVoMapper;
    @Autowired
    DistrictBaseVoMapper districtBaseVoMapper;
    @Autowired
    AddressBaseVoMapper addressBaseVoMapper;
    @Autowired
    PropertyBaseVoMapper propertyBaseVoMapper;
    @Autowired
    PropertyReviewBaseVoMapper propertyReviewBaseVoMapper;
    @Autowired
    PropertyReviewVoMapper propertyReviewVoMapper;

    long propertyId;

    /**
     * 在每次測試執行之前設置測試環境。
     * 新增使用者資料、行政區劃分層級資料、地區資料、地址資料、房源資料和房源評論資料，用於測試
     */
    @BeforeEach
    void setup() {
        // 新增使用者資料
        EcUserBaseVo ecUser = EcUserBaseVo.builder()
                .ecUserName("test user")
                .ecUserEmail("test@email.com")
                .ecUserHashedPassword("1234567")
                .build();
        ecUserBaseVoMapper.insertSelective(ecUser);
        long ecUserId = (long) ecUser.getEcUserId(); // 獲取使用者資料 id

        // 新增行政區劃分層級資料
        AdministrativeAreaBaseVo administrativeArea101 = AdministrativeAreaBaseVo.builder()
                .administrativeAreaName("test admin area 101")
                .administrativeAreaLevel(101L)
                .build();
        administrativeAreaBaseVoMapper.insertSelective(administrativeArea101);
        long administrativeArea101Id =(long) administrativeArea101.getAdministrativeAreaId();  // 獲取新行政區劃分層級資料 id

        // 新增地區資料
        DistrictBaseVo district1 = DistrictBaseVo.builder()
                .districtName("test district 1")
                .administrativeAreaId(administrativeArea101Id) // 帶入行政區劃分層級資料 id
                .build();

        districtBaseVoMapper.insertSelective(district1);
        long district1Id = (long) district1.getDistrictId();  // 獲取地區資料 id

        // 新增地址資料
        AddressBaseVo address1 = AddressBaseVo.builder()
                .street("test street 1")
                .adminAreaLevel3DistrictId(district1Id) // 帶入地區資料 id
                .build();
        addressBaseVoMapper.insertSelective(address1);
        long address1Id = (long) address1.getAddressId(); // 獲取地址資料 id

        // 新增房源資料
        PropertyBaseVo property = PropertyBaseVo.builder()
                .propertyTitle("test property")
                .addressId(address1Id) // 帶入地址資料 id
                .hostId(ecUserId) // 帶入使用者資料 id
                .build();
        propertyBaseVoMapper.insertSelective(property);
        propertyId = property.getPropertyId();

        // 新增房源評論資料
        PropertyReviewBaseVo propertyReview = PropertyReviewBaseVo
                .builder()
                .customerId(ecUserId)
                .propertyId(propertyId)
                .propertyReviewCleanliness(3)
                .propertyReviewAccuracy(3)
                .propertyReviewCommunication(3)
                .propertyReviewCheckin(3)
                .propertyReviewLocation(3)
                .propertyReviewValue(3)
                .propertyReviewCreatedAt(LocalDateTime.now())
                .build();

        propertyReviewBaseVoMapper.insertSelective(propertyReview);
    }

    @Test
    @DisplayName("用 propertyId 列表計算 propertyReview 數量")
    void countByPropertyIdListTest() {
        // 使用房源ID列表計算房源評論數
        List<Map<String, Object>> countList = propertyReviewVoMapper.countByPropertyIdList(List.of(propertyId));
        // 斷言計算結果列表的大小為1
        Assertions.assertEquals(countList.size(), 1);
        // 斷言第一個計算結果中的"count"鍵的值為1
        Assertions.assertEquals(countList.get(0).get("count"), 1L);
    }

    @Test
    @DisplayName("用 propertyId 計算 propertyReview 數量")
    void countByPropertyIdTest() {
        // 使用房源ID計算房源評論數
        Long count = propertyReviewVoMapper.countByPropertyId(propertyId);
        // 斷言計算結果Map不為空
        Assertions.assertNotNull(count);
        // 斷言計算結果Map中鍵為"count"的值等於1
        Assertions.assertEquals(count, 1L);
    }
}
