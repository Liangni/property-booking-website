package com.penny.service;

import com.penny.dao.*;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.exception.AuthorizationException;
import com.penny.redis.RedisService;
import com.penny.request.*;
import com.penny.response.ReadEcUserPropertyResponse;
import com.penny.vo.*;
import com.penny.vo.base.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyVoMapper propertyVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final EcUserService ecUserService;

    private final SearchPropertyRequestDTOMapper searchPropertyRequestDTOMapper;

    private final RedisService redisService;

    /**
     * 根據給定的搜尋參數，獲取房源資訊。
     *
     * @param request 房源搜尋參數
     * @return 符合搜尋條件的房源列表
     * @throws RequestValidationException 如果 numOfAvailableDays 和 startAvailableDateString 同時存在
     * @throws RequestValidationException 如果 startAvailableDateString 存在而 endAvailableDateString 不存在
     * @throws RequestValidationException 如果 startAvailableDateString 或 endAvailableDateString 格式錯誤
     */
    public List<PropertyVo> listPublishedProperty(SearchPropertyRequest request) {
        // 從查詢參數中獲取可預定天數、開始預定日期字串和結束預定日期字串
        Integer numOfAvailableDays = request.getNumOfAvailableDays();
        String startAvailableDateString = request.getStartAvailableDateString();
        String endAvailableDateString = request.getEndAvailableDateString();

        // 檢查參數中是否存在衝突情況
        if (numOfAvailableDays != null && startAvailableDateString != null) {
            throw new RequestValidationException("numOfAvailableDays and startAvailableDateString cannot coexist");
        }

        // 如果存在開始預定日期但沒有結束預定日期，則拋出異常
        if (startAvailableDateString != null && endAvailableDateString == null) {
            throw new RequestValidationException("startAvailableDateString cannot exist without endAvailableDateString");
        }

        // 如果存在結束預定日期但沒有開始預定日期，則拋出異常
        if (startAvailableDateString == null && endAvailableDateString != null) {
            throw new RequestValidationException("startAvailableDateString cannot exist without endAvailableDateString");
        }

        // 初始化結果列表
        List<PropertyVo> resultList;

        // 根據情況執行不同的查詢操作並賦值給結果列表
        SearchPropertyRequestDTO searchRequestDTO = searchPropertyRequestDTOMapper.apply(request);
        if (numOfAvailableDays != null) {
            resultList =  propertyVoMapper.listByNumOfAvailableDays(searchRequestDTO);
        } else if (startAvailableDateString != null) {
            resultList =  propertyVoMapper.listByStartAndEndAvailableDate(searchRequestDTO);
        } else  {
            resultList = propertyVoMapper.listByPropertyAttributes(searchRequestDTO);
        }

        // 將結果列表中已發佈的房源過濾出來並返回
        return resultList
            .stream()
            .filter(PropertyVo::getIsPublished)
            .collect(Collectors.toList());
    }

    /**
     * 根據房源 ID 獲取房源基本資訊。
     *
     * @param propertyId 房源 ID
     * @return 房源基本資訊
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發佈，則拋出此異常
     */
    public PropertyBaseVo getPublishedProperty(Long propertyId) {
        // 根據房源 ID 查詢房源基本資訊
        PropertyBaseVo property = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property with id %s not found".formatted(propertyId)));

        // 如果房源未發佈，則拋出異常
        if (!property.getIsPublished()) {
            throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));
        }

        return property;
    }

    /**
     * 創建房源。
     *
     * @param createRequest 要用於創建房源的創建請求
     */
    public void createProperty(CreatePropertyRequest createRequest){

        PropertyBaseVo propertyBaseVo = PropertyBaseVo
                .builder()
                .propertyTitle(createRequest.getPropertyTitle())
                .propertyDescription(createRequest.getPropertyDescription())
                .maxNumOfGuests(createRequest.getMaxNumOfGuests())
                .numOfBathrooms(createRequest.getNumOfBathrooms())
                .numOfBeds(createRequest.getNumOfBeds())
                .numOfBedrooms(createRequest.getNumOfBedrooms())
                .priceOnWeekdays(createRequest.getPriceOnWeekdays())
                .priceOnWeekends(createRequest.getPriceOnWeekends())
                .isPublished(createRequest.getIsPublished())
                .propertyMainSubTypeId(createRequest.getPropertyMainSubTypeId())
                .addressId(createRequest.getAddressId())
                .hostId(ecUserService.getLoginUser().getEcUserId())
                .build();

        // 如果房源未發佈，則直接新增資料庫並返回
        if (!propertyBaseVo.getIsPublished()) {
            propertyBaseVoMapper.insertSelective(propertyBaseVo);
            return;
        }

        // 如果房源已發佈，驗證必要欄位
        validatePublicProperty(propertyBaseVo);

        // 從 Redis 中刪除 key 為 "initialPagePropertyList" 的字串，確保首頁呈現最新資訊
        redisService.deleteStr("initialPagePropertyList");

        // 新增房源
        propertyBaseVoMapper.insertSelective(propertyBaseVo);
    }

    /**
     * 根據指定的房源 ID 和更新請求更新房源資訊。
     *
     * @param propertyId 要更新的房源 ID
     * @param updateRequest 更新請求物件，包含新的房源資訊
     *
     * @throws ResourceNotFoundException 如果找不到指定 ID 的房源
     * @throws AuthorizationException 如果登入使用者沒有執行操作的權限
     */
    public void updateProperty(Long propertyId, UpdatePropertyRequest updateRequest) {
        // 根據房源 ID 取得對應的 PropertyBaseVo 物件，如果不存在則拋出 ResourceNotFoundException 異常
        PropertyBaseVo propertyBaseVo = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "property with id %s not found".formatted(propertyId)
                ));

        // 檢查當前登入使用者是否具有執行操作的權限，如果不是則拋出 UnauthorizedException 異常
        if(!propertyBaseVo.getHostId().equals(ecUserService.getLoginUser().getEcUserId())) throw new AuthorizationException("login user is not authorized to do the operation");

        // 檢查房源是否有變更的標誌
        boolean changes = false;

        // 根據更新請求更新房源資訊
        if (updateRequest.getPropertyTitle() != null && !updateRequest.getPropertyTitle().equals(propertyBaseVo.getPropertyTitle())) {
            propertyBaseVo.setPropertyTitle(updateRequest.getPropertyTitle());
            changes = true;
        }

        if (updateRequest.getPropertyDescription() != null && !updateRequest.getPropertyDescription().equals(propertyBaseVo.getPropertyDescription())) {
            propertyBaseVo.setPropertyDescription(updateRequest.getPropertyDescription());
            changes = true;
        }

        if (updateRequest.getMaxNumOfGuests() != null && !updateRequest.getMaxNumOfGuests() .equals(propertyBaseVo.getMaxNumOfGuests())) {
            propertyBaseVo.setMaxNumOfGuests(updateRequest.getMaxNumOfGuests());
            changes = true;
        }

        if (updateRequest.getNumOfBathrooms() != null && !updateRequest.getNumOfBathrooms() .equals(propertyBaseVo.getNumOfBathrooms())) {
            propertyBaseVo.setNumOfBathrooms(updateRequest.getNumOfBathrooms());
            changes = true;
        }

        if (updateRequest.getNumOfBeds() != null && !updateRequest.getNumOfBeds() .equals(propertyBaseVo.getNumOfBeds())) {
            propertyBaseVo.setNumOfBeds(updateRequest.getNumOfBeds());
            changes = true;
        }

        if (updateRequest.getNumOfBedrooms() != null && !updateRequest.getNumOfBedrooms() .equals(propertyBaseVo.getNumOfBedrooms())) {
            propertyBaseVo.setNumOfBedrooms(updateRequest.getNumOfBedrooms());
            changes = true;
        }

        if (updateRequest.getPriceOnWeekends() != null && !updateRequest.getPriceOnWeekends() .equals(propertyBaseVo.getPriceOnWeekends())) {
            propertyBaseVo.setPriceOnWeekends(updateRequest.getPriceOnWeekends());
            changes = true;
        }

        if (updateRequest.getPriceOnWeekdays() != null && !updateRequest.getPriceOnWeekdays() .equals(propertyBaseVo.getPriceOnWeekdays())) {
            propertyBaseVo.setPriceOnWeekdays(updateRequest.getPriceOnWeekdays());
            changes = true;
        }

        if (updateRequest.getPropertyShareTypeId() != null && !updateRequest.getPropertyShareTypeId() .equals(propertyBaseVo.getPropertyShareTypeId() )) {
            propertyBaseVo.setPropertyShareTypeId(updateRequest.getPropertyShareTypeId() );
            changes = true;
        }

        if (updateRequest.getPropertyMainSubTypeId() != null && !updateRequest.getPropertyMainSubTypeId() .equals(propertyBaseVo.getPropertyMainSubTypeId() )) {
            propertyBaseVo.setPropertyMainSubTypeId(updateRequest.getPropertyMainSubTypeId());
            changes = true;
        }

        if (updateRequest.getIsPublished() != null && !updateRequest.getIsPublished() .equals(propertyBaseVo.getIsPublished() )) {
            propertyBaseVo.setIsPublished(updateRequest.getIsPublished());
            changes = true;
        }

        // 如果未發生任何房源資訊變更，則拋出 RequestValidationException 異常
        if (!changes) {
            throw new RequestValidationException("no data changes found");
        }

        // 如果房源未發佈，則直接更新資料庫並返回
        if (!propertyBaseVo.getIsPublished()) {
            propertyBaseVoMapper.updateByPrimaryKey(propertyBaseVo);
            return;
        }

        /// 如果房源已發佈，驗證必要欄位
        validatePublicProperty(propertyBaseVo);

        // 從 Redis 中刪除 key 為 "initialPagePropertyList" 的字串，確保首頁呈現最新資訊
        redisService.deleteStr("initialPagePropertyList");

        // 更新資料庫中的房源資訊
        propertyBaseVoMapper.updateByPrimaryKey(propertyBaseVo);
    }

    /**
     * 根據使用者 ID 列出該用戶擁有的房源列表。
     *
     * @param ecUserId 使用者的 ID
     * @return 返回房源列表
     * @throws AuthorizationException 如果登錄使用者無權執行操作，則拋出授權異常
     */
    public List<ReadEcUserPropertyResponse> listPropertyByEcUserId(Long ecUserId) {
         if (!ecUserId.equals(ecUserService.getLoginUser().getEcUserId())) {
             throw new AuthorizationException("login user is not authorized to do the operation");
         }

         List<PropertyVo> propertyVoList = propertyVoMapper.listByHostId(ecUserId);
         return propertyVoList.stream().map(propertyVo -> ReadEcUserPropertyResponse
                 .builder()
                 .propertyId(propertyVo.getPropertyId())
                 .propertyTitle(propertyVo.getPropertyTitle())
                 .districtId(propertyVo.getDistrictId())
                 .districtName(propertyVo.getDistrictName())
                 .parentDistrictId(propertyVo.getParentDistrictId())
                 .parentDistrictName(propertyVo.getDistrictName())
                 .build()).toList();
    }

    /**
     * 驗證已發布房源（當 isPublished 為 true 時）。
     *
     * @param propertyBaseVo 房源物件
     * @throws RequestValidationException 當有必填欄位為空時拋出驗證異常
     */
    private void validatePublicProperty(PropertyBaseVo propertyBaseVo) {
        // 儲存空值欄位的清單
        List<String> listOfFieldWithNullValue = new ArrayList<>();

        // 檢查每個屬性是否為空，並將空值欄位加入清單
        if (propertyBaseVo.getPropertyTitle() == null) listOfFieldWithNullValue.add("propertyTitle");
        if (propertyBaseVo.getMaxNumOfGuests() == null) listOfFieldWithNullValue.add("maxNumOfGuests");
        if (propertyBaseVo.getNumOfBedrooms() == null) listOfFieldWithNullValue.add("numOfBedrooms");
        if (propertyBaseVo.getNumOfBeds() == null) listOfFieldWithNullValue.add("numOfBeds");
        if (propertyBaseVo.getPriceOnWeekdays() == null) listOfFieldWithNullValue.add("priceOnWeekdays");
        if (propertyBaseVo.getPriceOnWeekends() == null) listOfFieldWithNullValue.add("priceOnWeekends");
        if (propertyBaseVo.getPropertyMainSubTypeId() == null) listOfFieldWithNullValue.add("propertyMainSubTypeId");
        if (propertyBaseVo.getPropertyShareTypeId() == null) listOfFieldWithNullValue.add("propertyShareTypeId");
        if (propertyBaseVo.getAddressId() == null) listOfFieldWithNullValue.add("addressId");

        // 如果空值欄位清單不為空，則拋出驗證異常
        if (!listOfFieldWithNullValue.isEmpty()) throw new RequestValidationException("When isPublished is true, the following fields are required : %s".formatted(listOfFieldWithNullValue));
    }
}
