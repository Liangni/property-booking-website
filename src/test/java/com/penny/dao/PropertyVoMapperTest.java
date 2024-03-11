package com.penny.dao;

import com.penny.dao.base.*;
import com.penny.daoParam.propertyVoMapper.ListByNumOfAvailableDaysParam;
import com.penny.vo.PropertyVo;
import com.penny.vo.base.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@DisplayName("Property資料表測試")
public class PropertyVoMapperTest {

    @Autowired
    private PropertyBaseVoMapper propertyBaseVoMapper;
    @Autowired
    private PropertyVoMapper propertyVoMapper;
    @Autowired
    private DistrictBaseVoMapper districtBaseVoMapper;
    @Autowired
    private AdministrativeAreaBaseVoMapper administrativeAreaBaseVoMapper;
    @Autowired
    private EcUserBaseVoMapper ecUserBaseVoMapper;
    @Autowired
    private AddressBaseVoMapper addressBaseVoMapper;
    @Autowired
    private BookingAvailabilityBaseVoMapper bookingAvailabilityBaseVoMapper;

    private long property1Id;

    private long property2Id;

    private long property3Id;

    private static final int NUM_OF_BOOKING_DAY = 10;

    private static final int NUM_OF_AVAILABLE_DAY = 3;

    /**
     * 在每次測試運行之前設置測試環境，包括新增
     * - 行政區劃分層級資料
     * - 地區資料
     * - 子地區資料
     * - 地址資料
     * - 使用者資料
     * - 房源資料
     */
    @BeforeEach
    void setup(){
        // 新增行政區劃分層級 101 ~ 103 的資料
        List<Long> administrativeAreaIdList = new ArrayList<>();
        for (int i = 101; i <= 103; i++) {
            AdministrativeAreaBaseVo administrativeArea = AdministrativeAreaBaseVo.builder()
                    .administrativeAreaName("test admin area " + i)
                    .administrativeAreaLevel((long) i)
                    .build();
            administrativeAreaBaseVoMapper.insertSelective(administrativeArea);
            administrativeAreaIdList.add(administrativeArea.getAdministrativeAreaId());
        }

        // 新增地區資料
        List<Long> districtIdList = new ArrayList<>();
        for (int i = 1; i <=2; i++) {
            DistrictBaseVo district = DistrictBaseVo.builder()
                    .districtName("test district " + i)
                    .administrativeAreaId(administrativeAreaIdList.get(i - 1)) // 帶入行政區劃分層級為 101, 102 的 id
                    .build();

            districtBaseVoMapper.insertSelective(district);
            // 獲取地區資料 id
            districtIdList.add(district.getDistrictId());
        }

        // 新增子地區
        DistrictBaseVo district1Child = DistrictBaseVo.builder()
                .districtName("test district 1's child")
                .administrativeAreaId(administrativeAreaIdList.get(2)) // 帶入行政區劃分層級為 103 的 id
                .parentDistrictId(districtIdList.get(0)) // 隸屬於行政層級為 101 的地區
                .build();

        DistrictBaseVo district2Child = DistrictBaseVo.builder()
                .districtName("test district 2's child")
                .administrativeAreaId(administrativeAreaIdList.get(2)) // 帶入行政區劃分層級為 103 的 id
                .parentDistrictId(districtIdList.get(1)) // 隸屬於行政層級為 102 的地區
                .build();

        districtBaseVoMapper.insertSelective(district1Child);
        districtBaseVoMapper.insertSelective(district2Child);

        // 獲取子地區資料 id
        long district1ChildId = (long) district1Child.getDistrictId();
        long district2ChildId = (long) district2Child.getDistrictId();

        // 新增地址資料
        AddressBaseVo address1 = AddressBaseVo.builder()
                .street("test street 1")
                .adminAreaLevel1DistrictId(districtIdList.get(0)) // 帶入行政劃分層級為 101 的地區 id
                .adminAreaLevel3DistrictId(district1ChildId) // 帶入行政劃分層級為 103 的地區 id
                .build();

        AddressBaseVo address2 = AddressBaseVo.builder()
                .street("test street 2")
                .adminAreaLevel2DistrictId(districtIdList.get(1)) // 帶入行政劃分層級為 102 的地區 id
                .adminAreaLevel3DistrictId(district2ChildId) // 帶入行政劃分層級為 103 的地區 id
                .build();

        // 獲取地址資料 id
        addressBaseVoMapper.insertSelective(address1);
        long address1Id = (long) address1.getAddressId();
        addressBaseVoMapper.insertSelective(address2);
        long address2Id = (long) address2.getAddressId();

        // 新增使用者資料
        EcUserBaseVo ecUser = EcUserBaseVo.builder()
                .ecUserName("test user")
                .ecUserEmail("test@email.com")
                .ecUserHashedPassword("1234567")
                .build();

        ecUserBaseVoMapper.insertSelective(ecUser);

        // 獲取使用者資料 id
        long ecUserId = (long) ecUser.getEcUserId();

        // 新增房源資料
        // 房源1, 3 共享同個地址
        PropertyBaseVo property1 = PropertyBaseVo.builder()
                .propertyTitle("Property 1")
                .addressId(address1Id) // 帶入地址資料 id
                .hostId(ecUserId) // 帶入使用者資料 id
                .build();

        PropertyBaseVo property3 = PropertyBaseVo.builder()
                .propertyTitle("Property 3")
                .addressId(address1Id) // 帶入地址資料 id
                .hostId(ecUserId) // 帶入使用者資料 id
                .build();

        // 房源2 與其他兩者不同地址
        PropertyBaseVo property2 = PropertyBaseVo.builder()
                .propertyTitle("Property 2")
                .addressId(address2Id) // 帶入地址資料 id
                .hostId(ecUserId) // 帶入使用者資料 id
                .build();

        // 獲取房源資料 id
        propertyBaseVoMapper.insertSelective(property1);
        property1Id = (long) property1.getPropertyId();
        propertyBaseVoMapper.insertSelective(property2);
        property2Id = (long) property2.getPropertyId();
        propertyBaseVoMapper.insertSelective(property3);
        property3Id = (long) property3.getPropertyId();

        // 新增今日後 10 天的預定資料
        // 房源 1、房源 2 第 2~5 天是「可預訂」，其他天是「已預定」
        // 房源 3 第 7~10 天是「可預訂」，其他天是「已預定」
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 定義包含三個房源ID和對應可預訂日期範圍的列表
        List<Long> propertyIds = Arrays.asList(property1Id, property2Id, property3Id);
        List<List<Integer>> availableRanges = List.of(
                List.of(2, 2 + NUM_OF_AVAILABLE_DAY),
                List.of(2, 2 + NUM_OF_AVAILABLE_DAY),
                List.of(7, 7 + NUM_OF_AVAILABLE_DAY)
        );

        // 對每個日期進行迭代
        for (int i = 0; i < NUM_OF_BOOKING_DAY ; i ++) {
            // 對每個房源進行迭代
            for (int j = 0; j < propertyIds.size(); j++) {
                String bookingStatus = "booked";
                Long propertyId = propertyIds.get(j);
                List<Integer> range = availableRanges.get(j);
                // 檢查日期是否在可預訂範圍內，並根據情況設置預定狀態
                if (i >= range.get(0) && i <= range.get(1)) {
                    bookingStatus = "available";
                }
                // 構建預定資料物件並插入資料庫
                BookingAvailabilityBaseVo bookingAvailabilityBaseVo = BookingAvailabilityBaseVo.builder()
                        .propertyId(propertyId)
                        .bookingAvailabilityDate(currentDate)
                        .bookingAvailabilityStatus(bookingStatus)
                        .build();

                bookingAvailabilityBaseVoMapper.insertSelective(bookingAvailabilityBaseVo);
            }
            // 將日期向前推進一天
            calendar.add(Calendar.DATE, 1);
            currentDate = calendar.getTime();
        }

    }

    /**
     * 測試根據連續可訂天數搜尋房源的方法
     * 此測試驗證根據指定的連續可訂天數搜尋房源的功能
     * 它確保返回的列表包含具有指定 field 的房源，並根據給定的排序字段進行排序
     */
    @Test
    @DisplayName("用連續可訂天數來搜尋房源")
    void listByNumOfAvailableDaysTest(){
        // 準備要返回和排序的 field 列表
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("address");
        returnFieldList.add("district");
        returnFieldList.add("propertyId");

        List<String> sortFieldList = new ArrayList<>();
        sortFieldList.add("district");
        sortFieldList.add("nearestAvailableDay");

        List<String> sortOrderList = new ArrayList<>();
        sortOrderList.add("asc");
        sortOrderList.add("asc");

        // 準備要要使用的參數物件
        ListByNumOfAvailableDaysParam param = ListByNumOfAvailableDaysParam.builder()
                .numOfAvailableDay(NUM_OF_AVAILABLE_DAY)
                .returnFieldList(returnFieldList)
                .sortFieldList(sortFieldList)
                .sortOrderList(sortOrderList)
                .offset(0)
                .limit(10)
                .build();

        // 使用指定的參數調用 PropertyVoMapper 的 listByNumOfAvailableDays 方法
        List<PropertyVo> propertyList = propertyVoMapper.listByNumOfAvailableDays(param);

        // 確認返回的房源列表不為空，並且包含了指定 field 的房源
        Assertions.assertNotEquals(0, propertyList.size());
        for (PropertyVo property : propertyList) {
            Assertions.assertNotNull(property.getPropertyId());
            Assertions.assertNotNull(property.getStreet());
            Assertions.assertNotNull(property.getAdminAreaLevel3DistrictId());
        }

        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();
        List<Long> expectedPropertyIdList = Arrays.asList(property1Id, property2Id, property3Id);

        // 斷言實際房源 ID 列表中包含了預期房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(expectedPropertyIdList));
        // 斷言房源 ID 基於排序標準的預期順序
        Assertions.assertTrue(
                actualPropertyIdList.indexOf(property1Id) < actualPropertyIdList.indexOf(property3Id)
                && actualPropertyIdList.indexOf(property3Id) < actualPropertyIdList.indexOf(property2Id)
        );
    }

}
