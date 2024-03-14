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

    private static final int NUM_OF_BOOKING_DAY = 10;

    private static final int NUM_OF_AVAILABLE_DAYS = 3;

    private static final int FIRST_AVAILABLE_DAY_FROM_NOW = 2;

    private static final int MAX_NUM_OF_GUESTS = 3;

    private static final List<Long> propertyIdList = new ArrayList<>();

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
        propertyIdList.add(insertTestProperty("Property 1", addressIdList.get(0), ecUserId, MAX_NUM_OF_GUESTS));
        propertyIdList.add(insertTestProperty("Property 2", addressIdList.get(1), ecUserId, MAX_NUM_OF_GUESTS));
        propertyIdList.add(insertTestProperty("Property 3", addressIdList.get(0), ecUserId, MAX_NUM_OF_GUESTS));

        // 設定兩種可預訂期間：
        // 1. 從今天開始數 FIRST_AVAILABLE_DAY_FROM_NOW 天至往後推 NUM_OF_AVAILABLE_DAY 個天數是「可預訂」
        // 2. 從第 1 個選項往後推 5 天是「可預訂」
        List<List<Integer>> availableRanges = List.of(
                List.of(FIRST_AVAILABLE_DAY_FROM_NOW, FIRST_AVAILABLE_DAY_FROM_NOW + NUM_OF_AVAILABLE_DAYS),
                List.of(FIRST_AVAILABLE_DAY_FROM_NOW + 5, FIRST_AVAILABLE_DAY_FROM_NOW + 5 + NUM_OF_AVAILABLE_DAYS)
        );

        // 新增今日後 NUM_OF_BOOKING_DAY 天數的預定資料
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        // 對每個天數進行迭代
        for (int day = 0; day < NUM_OF_BOOKING_DAY ; day ++) {

            // 對每個房源進行迭代
            for (int propertyIndex = 0; propertyIndex < propertyIdList.size(); propertyIndex++) {

                List<Integer> range;
                if (propertyIndex == 0 || propertyIndex == 1) {
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
                insertTestBookingAvailability(propertyIdList.get(propertyIndex), currentDate, bookingStatus);
            }
            // 將當前日期向前推進一天
            calendar.add(Calendar.DATE, 1);
            currentDate = calendar.getTime();
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

        List<String> sortFieldList = new ArrayList<>();
        sortFieldList.add("district");
        sortFieldList.add("nearestAvailableDay");

        List<String> sortOrderList = new ArrayList<>();
        sortOrderList.add("asc");
        sortOrderList.add("asc");

        // 準備要使用的參數物件
        SelectPropertyParam param = SelectPropertyParam.builder()
                .filterMap(filterMap)
                .returnFieldList(returnFieldList)
                .sortFieldList(sortFieldList)
                .sortOrderList(sortOrderList)
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
        // 調用 countByNumOfAvailableDays 方法，統計房源數量
        Long count = propertyVoMapper.countByNumOfAvailableDays(NUM_OF_AVAILABLE_DAYS);
        // 斷言房源數量不等於零
        Assertions.assertNotEquals(count, 0L);
    }

    @Test
    @DisplayName("用開始和結束可預訂日來搜尋房源")
    void listByStartAndEndAvailableDayTest(){

        // 準備要返回和排序的 field 列表
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("address");
        returnFieldList.add("district");
        returnFieldList.add("propertyId");

        List<String> sortFieldList = new ArrayList<>();
        sortFieldList.add("district");

        List<String> sortOrderList = new ArrayList<>();
        sortOrderList.add("asc");

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("numOfGuests", 2);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        calendar.add(Calendar.DATE, FIRST_AVAILABLE_DAY_FROM_NOW);
        Date startAvailableDate = calendar.getTime();
        filterMap.put("startAvailableDay", startAvailableDate);

        calendar.add(Calendar.DATE, NUM_OF_AVAILABLE_DAYS);
        Date endAvailableDate =  calendar.getTime();
        filterMap.put("endAvailableDay", endAvailableDate);

        // 準備要使用的參數物件
        SelectPropertyParam param = SelectPropertyParam.builder()
                .filterMap(filterMap)
                .returnFieldList(returnFieldList)
                .sortFieldList(sortFieldList)
                .sortOrderList(sortOrderList)
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
        }

        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();
        List<Long> expectPropertyIdList = propertyIdList.subList(0,2);

        // 斷言實際房源 ID 列表中包含了預期房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(expectPropertyIdList));
        // 斷言房源 ID 基於排序標準的預期順序
        Assertions.assertTrue(actualPropertyIdList.indexOf(expectPropertyIdList.get(0)) < actualPropertyIdList.indexOf(expectPropertyIdList.get(1)));
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

    private Long insertTestProperty(String name, Long addressId, Long ecUserId, Integer maxNumOfGuests) {
        PropertyBaseVo property = PropertyBaseVo.builder()
                .propertyTitle(name)
                .addressId(addressId)
                .hostId(ecUserId)
                .maxNumOfGuests(maxNumOfGuests)
                .build();

        propertyBaseVoMapper.insertSelective(property);
        return property.getPropertyId();
    }

    private void insertTestBookingAvailability(Long propertyId, Date date, String status){
        BookingAvailabilityBaseVo bookingAvailabilityBaseVo = BookingAvailabilityBaseVo.builder()
                .propertyId(propertyId)
                .bookingAvailabilityDate(date)
                .bookingAvailabilityStatus(status)
                .build();

        bookingAvailabilityBaseVoMapper.insertSelective(bookingAvailabilityBaseVo);
    }

}
