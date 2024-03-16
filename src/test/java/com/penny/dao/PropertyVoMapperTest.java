package com.penny.dao;

import com.penny.dao.base.*;
import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.vo.PropertyVo;
import com.penny.vo.base.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

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
    @Autowired
    private AmenityTypeBaseVoMapper amenityTypeBaseVoMapper;
    @Autowired
    private AmenityBaseVoMapper amenityBaseVoMapper;
    @Autowired
    private PropertyAmenityBaseVoMapper propertyAmenityBaseVoMapper;

    private static final int NUM_OF_BOOKING_DAY = 10;

    private static final int NUM_OF_AVAILABLE_DAYS = 3;

    private static final int FIRST_AVAILABLE_DAY_FROM_NOW = 2;

    private static final int BASE_MAX_NUM_OF_GUESTS = 3;

    private static final long BASE_PRICE = 100;

    private static final List<Long> propertyIdList = new ArrayList<>();

    private static List<Long>  propertyIdListWithSameAvailableDate = new ArrayList<>();

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
        // 新增 3 筆行政區劃分層級的資料
        int adminAreaSize = 3;
        List<Long> adminAreaIdList = insertTestAdminAreas(adminAreaSize);
        List<Long> parentAdminAreaIdList = adminAreaIdList.subList(0, adminAreaSize - 1);
        Long childAdminAreaId = adminAreaIdList.get(adminAreaSize);

        // 新增地區資料
        List<Long> parentDistrictIdList = new ArrayList<>();
        int districtCount = 1;
        for (Long parentAdminAreaId: parentAdminAreaIdList) {
            Long districtId = insertTestDistrict(
                    "test parent district " + districtCount,
                    parentAdminAreaId,
                    null);
            parentDistrictIdList.add(districtId);
            districtCount ++;
        }

        // 新增子地區與地址資料
        List<Long> addressIdList = new ArrayList<>();
        int addressCount = 1;
        for (Long parentDistrictId : parentDistrictIdList) {
            Long childDistrictId = insertTestDistrict(
                    "test child district " + addressCount,
                    childAdminAreaId,
                    parentDistrictId);

            Long addressId = insertTestAddress("test address" + addressCount, childDistrictId);
            addressIdList.add(addressId);
        }

        // 新增使用者資料
        Long ecUserId = insertTestEcUser("test user", "test email", "test password");

        // 新增房源資料
        // 房源1, 3 共享同個地址
        // 房源2 與其他兩者不同地址
        propertyIdList.add(insertTestProperty("Property 1", addressIdList.get(0), ecUserId));
        propertyIdList.add(insertTestProperty("Property 2", addressIdList.get(1), ecUserId));
        propertyIdList.add(insertTestProperty("Property 3", addressIdList.get(0), ecUserId));

        // 設定兩種可預訂期間：
        // 1. 從今天開始數 FIRST_AVAILABLE_DAY_FROM_NOW 天至往後推 NUM_OF_AVAILABLE_DAY 個天數是「可預訂」
        // 2. 從第 1 個選項往後推 5 天是「可預訂」
        List<List<Integer>> availableRanges = List.of(
                List.of(FIRST_AVAILABLE_DAY_FROM_NOW, FIRST_AVAILABLE_DAY_FROM_NOW + NUM_OF_AVAILABLE_DAYS),
                List.of(FIRST_AVAILABLE_DAY_FROM_NOW + 5, FIRST_AVAILABLE_DAY_FROM_NOW + 5 + NUM_OF_AVAILABLE_DAYS)
        );

        // 新增今日後 NUM_OF_BOOKING_DAY 天數的預定資料
        LocalDate currentDate = LocalDate.now();

        // 選擇具有相同可用日期的房源 ID 子列表
        propertyIdListWithSameAvailableDate = propertyIdList.subList(0,2);

        // 對每個天數進行迭代
        for (int day = 0; day < NUM_OF_BOOKING_DAY ; day ++) {

            // 對每個房源進行迭代
            for (int propertyIndex = 0; propertyIndex < propertyIdList.size(); propertyIndex++) {
                Long propertyId = propertyIdList.get(propertyIndex);

                List<Integer> range;
                if (propertyIdListWithSameAvailableDate.contains(propertyId)) {
                    range = availableRanges.get(0); // 房源1, 2 可預訂日相同
                } else {
                    range = availableRanges.get(1); // 房源 3 可預訂日較前者遲 5 天
                }

                String bookingStatus = "booked";
                // 檢查當前天數是否在可預訂範圍內，並根據情況設置預定狀態
                if (day >= range.get(0) && day <= range.get(1)) {
                    bookingStatus = "available";
                }

                // 新增房源預定日期資料
                insertTestBookingAvailability(propertyId, currentDate, bookingStatus);
            }
            // 將當前日期向前推進一天
            currentDate = currentDate.plusDays(1);
        }

    }

    /**
     * 測試根據連續可訂天數搜尋房源的方法
     * 此測試驗證根據指定的連續可訂天數搜尋房源的功能
     * 它確保返回的列表房源具有指定屬性，並根據給定的排序屬性進行排序
     */
    @Test
    @DisplayName("用連續可訂天數來搜尋房源")
    void listByNumOfAvailableDaysTest(){
        // 準備過濾屬性 map
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("numOfAvailableDays", NUM_OF_AVAILABLE_DAYS);

        // 準備要返回和排序的 field 列表
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("address");
        returnFieldList.add("district");
        returnFieldList.add("propertyId");


        Map<String, String> sortMap = new HashMap<>();
        sortMap.put("district", "asc");
        sortMap.put("nearestAvailableDay", "asc");

        // 準備要使用的參數物件
        SelectPropertyParam param = SelectPropertyParam.builder()
                .filterMap(filterMap)
                .returnFieldList(returnFieldList)
                .sortMap(sortMap)
                .offset(0)
                .limit(10)
                .build();

        // 使用指定的參數調用 PropertyVoMapper 的 listByNumOfAvailableDays 方法
        List<PropertyVo> propertyList = propertyVoMapper.listByFilter(param);

        // 確認返回的房源列表不為空，並且包含了指定 field 的房源
        Assertions.assertNotEquals(0, propertyList.size());
        for (PropertyVo property : propertyList) {
            Assertions.assertNotNull(property.getPropertyId());
            Assertions.assertNotNull(property.getStreet());
            Assertions.assertNotNull(property.getAdminAreaLevel3DistrictId());
        }
        System.out.println(propertyList.get(1).toString());
        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();

        // 斷言實際房源 ID 列表中包含了預期房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(propertyIdList));
        // 斷言房源 ID 基於排序標準的預期順序
        Assertions.assertTrue(
                actualPropertyIdList.indexOf(propertyIdList.get(0)) < actualPropertyIdList.indexOf(propertyIdList.get(2))
                && actualPropertyIdList.indexOf(propertyIdList.get(2)) < actualPropertyIdList.indexOf(propertyIdList.get(1))
        );
    }
    /**
     * 測試根據連續可預訂天數來計算房源數量
     * 此測試確保根據連續可預訂天數統計的房源數量不為零
     */
    @Test
    @DisplayName("用連續可預訂天數來計算房源數量")
    void countByNumOfAvailableDaysTest(){
        // 調用 countByFilter 方法，統計房源數量
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("numOfAvailableDays", NUM_OF_AVAILABLE_DAYS);

        Long count = propertyVoMapper.countByFilter(filterMap);

        // 斷言房源數量不為空
        Assertions.assertNotNull(count);
        // 斷言房源數量大於等於測試房源數量
        Assertions.assertTrue(count >= propertyIdList.size());
    }

    @Test
    @DisplayName("用開始和結束可預訂日來搜尋房源")
    void listByStartAndEndAvailableDayTest(){

        // 準備要返回和排序的 field 列表
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("address");
        returnFieldList.add("district");
        returnFieldList.add("propertyId");
        returnFieldList.add("startAvailableDate");

        Map<String, String> sortMap = new HashMap<>();
        sortMap.put("district", "asc");

        Map<String, Object> filterMap = new HashMap<>();
        LocalDate currentDate = LocalDate.now();

        LocalDate startAvailableDate = currentDate.plusDays(FIRST_AVAILABLE_DAY_FROM_NOW);
        filterMap.put("startAvailableDate", startAvailableDate);

        LocalDate endAvailableDate = currentDate.plusDays(FIRST_AVAILABLE_DAY_FROM_NOW + NUM_OF_AVAILABLE_DAYS);
        filterMap.put("endAvailableDate", endAvailableDate);

        // 準備要使用的參數物件
        SelectPropertyParam param = SelectPropertyParam.builder()
                .filterMap(filterMap)
                .returnFieldList(returnFieldList)
                .sortMap(sortMap)
                .offset(0)
                .limit(10)
                .build();
        List<PropertyVo> propertyList = propertyVoMapper.listByFilter(param);

        // 確認返回的房源列表不為空，並且包含了指定 field 的房源
        Assertions.assertNotEquals(0, propertyList.size());
        // 確認返回的房源列表不為空，並且包含了指定 field 的房源
        Assertions.assertNotEquals(0, propertyList.size());
        for (PropertyVo property : propertyList) {
            Assertions.assertNotNull(property.getPropertyId());
            Assertions.assertNotNull(property.getStreet());
            Assertions.assertNotNull(property.getParentDistrictId());
            Assertions.assertNotNull(property.getStartAvailableDate());
        }

        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();
        List<Long> expectPropertyIdList = propertyIdListWithSameAvailableDate;
        System.out.println(actualPropertyIdList);
        // 斷言實際房源 ID 列表中包含了預期房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(expectPropertyIdList));
        // 斷言房源 ID 基於排序標準的預期順序
        Assertions.assertTrue(actualPropertyIdList.indexOf(expectPropertyIdList.get(0)) < actualPropertyIdList.indexOf(expectPropertyIdList.get(1)));
    }

    @Test
    @DisplayName("用房屋屬性來搜尋房源")
    void listByPropertyAttributeTest(){
        // 準備測試資料
        // 插入測試的設施類型和設施
        Long amenityTypeId = insertAmenityType("test amenity type");
        Long amenityId = insertAmenity("test amenity", amenityTypeId);

        // 從房源 ID 列表中選擇前兩個房源 ID 作為子房源 ID 列表
        List<Long> subPropertyIdList = propertyIdList.subList(0, 2);

        // 對於每個子房源 ID，插入房源設施，並更新基本屬性值
        for (Long propertyId: subPropertyIdList) {
            insertPropertyAmenity(propertyId, amenityId);

            // 從數據庫中選擇並更新房源基本信息
            PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
            propertyBaseVo.setMaxNumOfGuests(BASE_MAX_NUM_OF_GUESTS);
            propertyBaseVo.setPriceOnWeekdays(BASE_PRICE);
            propertyBaseVo.setPriceOnWeekends(BASE_PRICE);
            propertyBaseVoMapper.updateByPrimaryKey(propertyBaseVo);
        }

        // 構建房源篩選條件的 map
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("numOfGuests", BASE_MAX_NUM_OF_GUESTS);
        filterMap.put("maxPrice", BASE_PRICE);
        filterMap.put("minPrice", BASE_PRICE);

        // 指定返回的房源字段列表
        List<String> returnFiledList = List.of("propertyId", "numOfGuests", "priceOnWeekdays", "priceOnWeekends");

        // 構建房源篩選參數
        SelectPropertyParam param = SelectPropertyParam
                .builder()
                .filterMap(filterMap)
                .returnFieldList(returnFiledList)
                .sortMap(new HashMap<>())
                .build();

        // 根據篩選參數查詢房源列表
        List<PropertyVo> propertyList = propertyVoMapper.listByFilter(param);

        // 斷言返回的房源列表不為空，並且包含了指定字段的房源
        Assertions.assertNotEquals(0, propertyList.size());

        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();

        // 斷言實際房源 ID 列表中包含了子房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(subPropertyIdList));
    }

    @Test
    @DisplayName("用開始和結束可預訂日來計算房源數量")
    void countByStartAndEndAvailableDayTest(){
        Map<String, Object> filterMap = new HashMap<>();
        // 獲取當前日期
        LocalDate currentDate = LocalDate.now();
        // 計算開始可預訂日期
        filterMap.put("startAvailableDate", currentDate.plusDays(FIRST_AVAILABLE_DAY_FROM_NOW));
        // 計算結束可預訂日期
        filterMap.put("endAvailableDate",currentDate.plusDays(FIRST_AVAILABLE_DAY_FROM_NOW + NUM_OF_AVAILABLE_DAYS));

        // 調用方法計算房源數量
        Long numOfPropertyCount = propertyVoMapper.countByFilter(filterMap);

        // 斷言房源數量不為空
        Assertions.assertNotNull(numOfPropertyCount);
        // 斷言房源數量大於等於具有相同可用日期的房源數量
        Assertions.assertTrue(numOfPropertyCount >= propertyIdListWithSameAvailableDate.size());
    }

    private List<Long> insertTestAdminAreas(int numOfRowToInsert) {
        int numOfBaseLevel = 101;
        List<Long> administrativeAreaIdList = new ArrayList<>();
        for (int i = numOfBaseLevel; i <= numOfBaseLevel + numOfRowToInsert; i++) {
            AdministrativeAreaBaseVo administrativeArea = AdministrativeAreaBaseVo.builder()
                    .administrativeAreaName("test admin area " + i)
                    .administrativeAreaLevel((long) i)
                    .build();
            administrativeAreaBaseVoMapper.insertSelective(administrativeArea);
            administrativeAreaIdList.add(administrativeArea.getAdministrativeAreaId());
        }
        return administrativeAreaIdList;
    }

    private Long insertTestDistrict(String districtName, Long administrativeAreaId, Long parentDistrictId) {
        DistrictBaseVo district = DistrictBaseVo.builder()
                .districtName(districtName)
                .administrativeAreaId(administrativeAreaId)
                .parentDistrictId(parentDistrictId)
                .build();

        districtBaseVoMapper.insertSelective(district);
        return district.getDistrictId();
    }

    private Long insertTestAddress(String addressName, Long districtId) {
        AddressBaseVo address = AddressBaseVo.builder()
                .street(addressName)
                .adminAreaLevel3DistrictId(districtId)
                .build();

        addressBaseVoMapper.insertSelective(address);
        return address.getAddressId();
    }

    private Long insertTestEcUser(String name, String email, String password) {
        EcUserBaseVo ecUser = EcUserBaseVo.builder()
                .ecUserName(name)
                .ecUserEmail(email)
                .ecUserHashedPassword(password)
                .build();

        ecUserBaseVoMapper.insertSelective(ecUser);
        return ecUser.getEcUserId();
    }

    private Long insertTestProperty(String name, Long addressId, Long ecUserId) {
        PropertyBaseVo property = PropertyBaseVo.builder()
                .propertyTitle(name)
                .addressId(addressId)
                .hostId(ecUserId)
                .build();

        propertyBaseVoMapper.insertSelective(property);
        return property.getPropertyId();
    }

    private void insertTestBookingAvailability(Long propertyId, LocalDate date, String status){
        BookingAvailabilityBaseVo bookingAvailabilityBaseVo = BookingAvailabilityBaseVo.builder()
                .propertyId(propertyId)
                .bookingAvailabilityDate(date)
                .bookingAvailabilityStatus(status)
                .build();

        bookingAvailabilityBaseVoMapper.insertSelective(bookingAvailabilityBaseVo);
    }

    private  Long insertAmenityType(String name) {
        AmenityTypeBaseVo amenityTypeBaseVo = AmenityTypeBaseVo
                .builder()
                .amenityTypeName(name)
                .build();
        amenityTypeBaseVoMapper.insertSelective(amenityTypeBaseVo);
        return amenityTypeBaseVo.getAmenityTypeId();
    }

    private Long insertAmenity(String name, Long amenityTypeId) {
        AmenityBaseVo amenityBaseVo = AmenityBaseVo
                .builder()
                .amenityTypeId(amenityTypeId)
                .amenityName(name)
                .build();

        amenityBaseVoMapper.insertSelective(amenityBaseVo);
        return amenityBaseVo.getAmenityId();
    }

    private void insertPropertyAmenity(Long propertyId, Long amenityId) {
        PropertyAmenityBaseVo propertyAmenityBaseVo = PropertyAmenityBaseVo
                .builder()
                .propertyId(propertyId)
                .amenityId(amenityId)
                .build();
        propertyAmenityBaseVoMapper.insertSelective(propertyAmenityBaseVo);
    }

}
