package com.penny.dao;

import com.penny.dao.base.*;
import com.penny.request.SearchPropertyRequest;
import com.penny.request.SearchPropertyRequestDTO;
import com.penny.request.SearchPropertyRequestDTOMapper;
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
    @Autowired
    private SearchPropertyRequestDTOMapper searchPropertyRequestDTOMapper;

    private static final int NUM_OF_BOOKING_DAY = 10;

    private static final int NUM_OF_AVAILABLE_DAYS = 3;

    private static final int FIRST_AVAILABLE_DAY_FROM_NOW = 2;

    private static final int BASE_MAX_NUM_OF_GUESTS = 3;

    private static final int BASE_PRICE = 100;

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
        List<Long> adminAreaIdList = insertAdminAreas(adminAreaSize);
        List<Long> parentAdminAreaIdList = adminAreaIdList.subList(0, adminAreaSize - 1);
        Long childAdminAreaId = adminAreaIdList.get(adminAreaSize);

        // 新增地區資料
        List<Long> parentDistrictIdList = new ArrayList<>();
        int districtCount = 1;
        for (Long parentAdminAreaId: parentAdminAreaIdList) {
            Long districtId = insertDistrict(
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
            Long childDistrictId = insertDistrict(
                    "test child district " + addressCount,
                    childAdminAreaId,
                    parentDistrictId);

            Long addressId = insertAddress("test address" + addressCount, childDistrictId);
            addressIdList.add(addressId);
        }

        // 新增使用者資料
        Long ecUserId = insertEcUser("test user", "test username","test email", "test password");

        // 新增房源資料
        // 房源1, 3 共享同個地址
        // 房源2 與其他兩者不同地址
        propertyIdList.add(insertProperty("Property 1", addressIdList.get(0), ecUserId));
        propertyIdList.add(insertProperty("Property 2", addressIdList.get(1), ecUserId));
        propertyIdList.add(insertProperty("Property 3", addressIdList.get(0), ecUserId));

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
                insertBookingAvailability(propertyId, currentDate, bookingStatus);
            }
            // 將當前日期向前推進一天
            currentDate = currentDate.plusDays(1);
        }

    }

    /**
     * 測試根據可預訂天數搜索房源的方法 listByNumOfAvailableDays。
     */
    @Test
    @DisplayName("用連續可訂天數來搜尋房源")
    void listByNumOfAvailableDaysTest(){
        SearchPropertyRequest searchPropertyRequest = SearchPropertyRequest
                .builder()
                .numOfAvailableDays(NUM_OF_AVAILABLE_DAYS)
                .build();

        // 使用指定的參數調用 PropertyVoMapper 的 listByNumOfAvailableDays 方法
        SearchPropertyRequestDTO searchPropertyRequestDTO = searchPropertyRequestDTOMapper.apply(searchPropertyRequest);
        List<PropertyVo> propertyList = propertyVoMapper.listByNumOfAvailableDays(searchPropertyRequestDTO);

        // 確認返回的房源列表不為空，並且包含了指定屬性的房源
        Assertions.assertNotEquals(0, propertyList.size());

        for (PropertyVo property : propertyList) {
            Assertions.assertNotNull(property.getStartAvailableDate());
            Assertions.assertNotNull(property.getEndAvailableDate());
            Assertions.assertNotNull(property.getPropertyId());
            Assertions.assertNotNull(property.getStreet());
            Assertions.assertNotNull(property.getDistrictId());
        }

        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();

        // 斷言實際房源 ID 列表中包含了預期房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(propertyIdList));
    }

    /**
     * 測試根據開始和結束可預訂日期搜索房源的方法 listByStartAndEndAvailableDay。
     */
    @Test
    @DisplayName("用開始和結束可預訂日來搜尋房源")
    void listByStartAndEndAvailableDayTest(){

        LocalDate currentDate = LocalDate.now();
        LocalDate startAvailableDate = currentDate.plusDays(FIRST_AVAILABLE_DAY_FROM_NOW);
        LocalDate endAvailableDate = currentDate.plusDays(FIRST_AVAILABLE_DAY_FROM_NOW + NUM_OF_AVAILABLE_DAYS);

        // 準備要使用的參數物件
        SearchPropertyRequestDTO searchRequestDTO = SearchPropertyRequestDTO
                .builder()
                .startAvailableDate(startAvailableDate)
                .endAvailableDate(endAvailableDate)
                .build();
        List<PropertyVo> propertyList = propertyVoMapper.listByStartAndEndAvailableDate(searchRequestDTO);

        // 確認返回的房源列表不為空，並且包含了指定屬性的房源
        Assertions.assertNotEquals(0, propertyList.size());

        Assertions.assertNotEquals(0, propertyList.size());
        for (PropertyVo property : propertyList) {
            Assertions.assertNotNull(property.getStartAvailableDate());
            Assertions.assertNotNull(property.getEndAvailableDate());
            Assertions.assertNotNull(property.getPropertyId());
            Assertions.assertNotNull(property.getStreet());
            Assertions.assertNotNull(property.getParentDistrictId());
        }

        // 從返回的列表中提取房源 ID
        List<Long> actualPropertyIdList = propertyList
                .stream()
                .map(PropertyVo::getPropertyId)
                .toList();
        List<Long> expectPropertyIdList = propertyIdListWithSameAvailableDate;

        // 斷言實際房源 ID 列表中包含了預期房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(expectPropertyIdList));
    }

    /**
     * 測試根據房屋屬性搜索房源的方法 listByPropertyAttribute。
     */
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

            // 從資料庫中選擇房源並更新基本資訊
            PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
            propertyBaseVo.setMaxNumOfGuests(BASE_MAX_NUM_OF_GUESTS);
            propertyBaseVo.setPriceOnWeekdays(BASE_PRICE);
            propertyBaseVo.setPriceOnWeekends(BASE_PRICE);
            propertyBaseVoMapper.updateByPrimaryKey(propertyBaseVo);
        }

        SearchPropertyRequestDTO searchRequestDTO = SearchPropertyRequestDTO
                .builder()
                .numOfGuests(BASE_MAX_NUM_OF_GUESTS)
                .maxPrice(BASE_PRICE)
                .minPrice(BASE_PRICE)
                .build();

        // 根據篩選參數查詢房源列表
        List<PropertyVo> propertyList = propertyVoMapper.listByPropertyAttributes(searchRequestDTO);

        // 斷言返回的房源列表不為空，並且包含了指定屬性的房源
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

        // 斷言實際房源 ID 列表中包含了子房源 ID 列表中的所有元素
        Assertions.assertTrue(actualPropertyIdList.containsAll(subPropertyIdList));
    }


    /**
     * 插入行政區劃分資料至資料庫
     *
     * @param numOfRowToInsert 要插入的行數
     * @return 插入的行政區劃分 ID 列表
     */
    private List<Long> insertAdminAreas(int numOfRowToInsert) {
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

    /**
     * 插入行政區資料至資料庫
     *
     * @param districtName        行政區名稱
     * @param administrativeAreaId 行政區劃分 ID
     * @param parentDistrictId    父行政區 ID
     * @return 新插入的行政區 ID
     */
    private Long insertDistrict(String districtName, Long administrativeAreaId, Long parentDistrictId) {
        DistrictBaseVo district = DistrictBaseVo.builder()
                .districtName(districtName)
                .administrativeAreaId(administrativeAreaId)
                .parentDistrictId(parentDistrictId)
                .build();

        districtBaseVoMapper.insertSelective(district);
        return district.getDistrictId();
    }

    /**
     * 插入地址資料至資料庫
     *
     * @param addressName 地址名稱
     * @param districtId  行政區 ID
     * @return 新插入的地址 ID
     */
    private Long insertAddress(String addressName, Long districtId) {
        AddressBaseVo address = AddressBaseVo.builder()
                .street(addressName)
                .adminAreaLevel3DistrictId(districtId)
                .build();

        addressBaseVoMapper.insertSelective(address);
        return address.getAddressId();
    }

    /**
     * 插入使用者資料至資料庫
     *
     * @param name     使用者名稱
     * @param email    電子郵件地址
     * @param password 密碼
     * @return 新插入的使用者 ID
     */
    private Long insertEcUser(String name, String username,String email, String password) {
        EcUserBaseVo ecUser = EcUserBaseVo.builder()
                .ecUserName(name)
                .ecUserEmail(email)
                .ecUserUsername(username)
                .ecUserPassword(password)
                .build();

        ecUserBaseVoMapper.insertSelective(ecUser);
        return ecUser.getEcUserId();
    }

    /**
     * 插入房源資料至資料庫
     *
     * @param name      房源名稱
     * @param addressId 地址 ID
     * @param ecUserId  用戶 ID
     * @return 新插入的房源 ID
     */
    private Long insertProperty(String name, Long addressId, Long ecUserId) {
        PropertyBaseVo property = PropertyBaseVo.builder()
                .propertyTitle(name)
                .addressId(addressId)
                .hostId(ecUserId)
                .build();

        propertyBaseVoMapper.insertSelective(property);
        return property.getPropertyId();
    }

    /**
     * 插入可預訂性資料至資料庫
     *
     * @param propertyId 房源 ID
     * @param date       日期
     * @param status     狀態
     */
    private void insertBookingAvailability(Long propertyId, LocalDate date, String status){
        BookingAvailabilityBaseVo bookingAvailabilityBaseVo = BookingAvailabilityBaseVo.builder()
                .propertyId(propertyId)
                .bookingAvailabilityDate(date)
                .bookingAvailabilityStatus(status)
                .build();

        bookingAvailabilityBaseVoMapper.insertSelective(bookingAvailabilityBaseVo);
    }

    /**
     * 插入設備類型資料至資料庫
     *
     * @param name 設備類型名稱
     * @return 新插入的設備類型 ID
     */
    private  Long insertAmenityType(String name) {
        AmenityTypeBaseVo amenityTypeBaseVo = AmenityTypeBaseVo
                .builder()
                .amenityTypeName(name)
                .build();
        amenityTypeBaseVoMapper.insertSelective(amenityTypeBaseVo);
        return amenityTypeBaseVo.getAmenityTypeId();
    }

    /**
     * 插入設備資料至資料庫
     *
     * @param name           設施名稱
     * @param amenityTypeId  設備類型 ID
     * @return 新插入的設備 ID
     */
    private Long insertAmenity(String name, Long amenityTypeId) {
        AmenityBaseVo amenityBaseVo = AmenityBaseVo
                .builder()
                .amenityTypeId(amenityTypeId)
                .amenityName(name)
                .build();

        amenityBaseVoMapper.insertSelective(amenityBaseVo);
        return amenityBaseVo.getAmenityId();
    }

    /**
     * 將房源和設備的關聯插入到資料庫
     *
     * @param propertyId 房源的ID。
     * @param amenityId 設備的ID。
     */
    private void insertPropertyAmenity(Long propertyId, Long amenityId) {
        PropertyAmenityBaseVo propertyAmenityBaseVo = PropertyAmenityBaseVo
                .builder()
                .propertyId(propertyId)
                .amenityId(amenityId)
                .build();
        propertyAmenityBaseVoMapper.insertSelective(propertyAmenityBaseVo);
    }

}
