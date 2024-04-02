package com.penny.service;

import com.amazonaws.HttpMethod;
import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyPictureVoMapper;
import com.penny.dao.PropertyVoMapper;
import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.dao.base.PictureDtBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.PropertyPictureBaseVoMapper;
import com.penny.daoParam.propertyVoMapper.SelectPropertyParam;
import com.penny.enums.PictureDtSize;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.property.PropertySearchRequest;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.s3.S3Buckets;
import com.penny.util.Paginator;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.PropertyVo;
import com.penny.vo.base.PictureBaseVo;
import com.penny.vo.base.PictureDtBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import com.penny.vo.base.PropertyPictureBaseVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_LIMIT = 10;

    private final PropertyVoMapper propertyVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final Paginator paginator;

    private final PictureBaseVoMapper pictureBaseVoMapper;

    private final PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;

    private final PropertyPictureVoMapper propertyPictureVoMapper;

    private final PictureDtBaseVoMapper pictureDtBaseVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final S3Service s3Service;

    private final S3Buckets s3Buckets;


    /**
     * 根據給定的搜尋請求，獲取房源列表並進行篩選。
     *
     * @param propertySearchRequest 搜尋請求物件，包含篩選條件、返回欄位、排序方式、分頁資訊等。
     * @return 包含房源列表和分頁資訊的結果 Map。
     */
    public Map<String, Object> getPropertiesByFilter(PropertySearchRequest propertySearchRequest) {

        // 確保所有的請求參數不為 null，避免空指針異常(NullPointerException)
        PropertySearchRequest processedPropertySearchRequest = PropertySearchRequest
                .builder()
                .filterMap(Optional.ofNullable(propertySearchRequest.getFilterMap()).orElse(new HashMap<>()))
                .returnFieldList(Optional.ofNullable(propertySearchRequest.getReturnFieldList()).orElse(new ArrayList<>()))
                .sortMap(Optional.ofNullable(propertySearchRequest.getSortMap()).orElse(new HashMap<>()))
                .page(Optional.ofNullable(propertySearchRequest.getPage()).orElse(DEFAULT_PAGE))
                .limit(Optional.ofNullable(propertySearchRequest.getLimit()).orElse(DEFAULT_LIMIT))
                .build();

        // 驗證參數合法性
        validateFilters(processedPropertySearchRequest);

        int page = processedPropertySearchRequest.getPage();
        int limit =processedPropertySearchRequest.getLimit();
        int offset = paginator.calculateOffset(page, limit);

        // 獲取房源列表
        List<PropertyVo> propertyVoList = fetchProperties(propertySearchRequest, offset, limit);

        // 轉換為精簡的房源Map列表
        List<Map<String, Object>> leanPropertyMapList = convertToLeanPropertyMapList(propertyVoList, processedPropertySearchRequest.getReturnFieldList());

        // 計算 pagination
        long totalResultCount = propertyVoMapper.countByFilter(processedPropertySearchRequest.getFilterMap());
        long totalPages = paginator.calculateTotalPages(totalResultCount, limit);
        Map<String, Object> pagination = paginator.buildPaginationMap(totalResultCount, page, totalPages, limit);

        // 建構結果 Map，包含房源列表和分頁資訊
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", leanPropertyMapList);
        resultMap.put("pagination", pagination);

        return resultMap;
    }

    public PropertyBaseVo getProperty(Long propertyId) {
        return Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property %s is not found".formatted(propertyId)))
;    }


    public Map<String, String> getImageUploadUrl(PropertyUploadImageRequest uploadImageRequest) {
        // 檢驗參數
        Long propertyId = uploadImageRequest.getPropertyId();
        String fileExtension = uploadImageRequest.getFileExtension();
        if (propertyId == null) { throw new FieldConflictException("propertyId is required");}
        if (fileExtension == null) { throw new FieldConflictException("fileExtension is required");}

        String pictureBucketPath;
        PictureBaseVo pictureBaseVo;

        // 創建圖片
        pictureBucketPath = generateBucketPath(propertyId, "original", fileExtension);
        pictureBaseVo = new PictureBaseVo(null, pictureBucketPath);
        pictureBaseVoMapper.insertSelective(pictureBaseVo);

        // 更新房源圖片關係表
        List<PropertyPictureVo> propertyPicList = propertyPictureVoMapper.listByPropertyId(propertyId);

        // 依 propertyPictureOrder 排序房源圖片，找出最大的 propertyPictureOrder 數字
        Collections.sort(propertyPicList, Comparator.comparingLong(PropertyPictureVo::getPropertyPictureOrder));
        // 獲取房源圖片列表中最後一張圖片的排序值，並將其增加1，以生成新的圖片排序值
        Long newPictureOrder = propertyPicList.get(propertyPicList.size() - 1).getPropertyPictureOrder() + 1L;


        // 新增房源圖片
        propertyPictureBaseVoMapper.insertSelective(PropertyPictureBaseVo
                .builder()
                    .propertyId(propertyId)
                    .pictureId(pictureBaseVo.getPictureId()) // 插入剛創建的圖片 id
                    .propertyPictureOrder(newPictureOrder) // 插入新的圖片排序值
                .build()
        );

        // 初始化結果
        Map<String, String> resultMap = new HashMap<>();
        // 將上傳原始尺寸圖像的預簽名URL 加進 result map
        resultMap.put(
                "sizeOriginal",
                s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), pictureBucketPath, HttpMethod.PUT)
        );

        // 創建圖片DT及其對應的預簽名URL
        PictureDtSize.stream()
                .forEach(size -> {
                    int pictureSizeNum = size.getNum();
                    String picDtBucketPath = generateBucketPath(propertyId, String.valueOf(pictureSizeNum), fileExtension);

                    pictureDtBaseVoMapper.insertSelective(PictureDtBaseVo
                            .builder()
                                    .pictureDtUrl(picDtBucketPath)
                                    .pictureDtSize(pictureSizeNum)
                                    .pictureId(pictureBaseVo.getPictureId())
                            .build()
                    );

                    // 將上傳指定尺寸圖像的預簽名URL 加進 result map
                    resultMap.put(
                            "size%s".formatted(pictureSizeNum),
                            s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), picDtBucketPath, HttpMethod.PUT)
                    );
                });

        return resultMap;
    }

    public List<String> getImageDownloadUrl(Long propertyId, Integer sizeNum) {
        // 檢驗 propertyId, sizeNum
        if (propertyId == null || sizeNum == null) {
           throw new FieldConflictException("propertyId and sizeNum are required in query string");
        }

        // 從房源圖片表找尋 propertyId 相符的房源圖片
        List<PropertyPictureVo> propertyPictureVoList = propertyPictureVoMapper.listByPropertyId(propertyId);
        // 依 propertyPictureOrder 排序房源圖片
        Collections.sort(propertyPictureVoList, Comparator.comparingLong(PropertyPictureVo::getPropertyPictureOrder));

        List<String> resultList = new ArrayList<>();
        for (PropertyPictureVo propertyPictureVo: propertyPictureVoList) {
            // 找出 pictureId 與 sizeNum 相符的所有圖片DT物件
            PictureDtVo pictureDtVo = pictureDtVoMapper.selectByPictureIdAndSizeNum(propertyPictureVo.getPictureId(), sizeNum);

            if (pictureDtVo != null) {
                // 取得圖片DT物件的Url
                String pictureDtDownloadPath = pictureDtVo.getPictureDtUrl();
                // 產生與DT物件的Url對應的預簽名Url
                resultList.add(s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), pictureDtDownloadPath, HttpMethod.GET));
            }
        }

        return resultList;
    }

    /**
     * 驗證房源搜尋請求中的篩選條件。
     *
     * @param propertySearchRequest 房源搜尋請求物件，包含要進行驗證的篩選條件。
     * @throws FieldConflictException 當篩選條件存在衝突時拋出異常。
     */
    private void validateFilters(PropertySearchRequest propertySearchRequest){
        Map<String, Object> requestFilterMap = propertySearchRequest.getFilterMap();
        Integer numOfAvailableDays = (Integer) requestFilterMap.get("numOfAvailableDays");
        String startAvailableDateString = (String) requestFilterMap.get("startAvailableDate");
        String endAvailableDateString = (String) requestFilterMap.get("endAvailableDate");

        // 檢查是否 numOfAvailableDays 和 startAvailableDate 同時存在
        boolean isNumOfAvailableDaysAndStartAvailableDatePresent =
                numOfAvailableDays != null && startAvailableDateString != null;

        // 檢查是否 numOfAvailableDays 和 endAvailableDate 同時存在
        boolean isNumOfAvailableDaysAndEndAvailableDatePresent =
                numOfAvailableDays != null && endAvailableDateString != null;

        // 如果有其中一種情況存在，則拋出異常
        if (isNumOfAvailableDaysAndStartAvailableDatePresent || isNumOfAvailableDaysAndEndAvailableDatePresent) {
            throw new FieldConflictException("filter numOfAvailableDays cannot co-exist with startAvailableDate or endAvailableDate");
        }

        // 確認 filterMap 中的 startAvailableDate 和 endAvailableDate 成對出現
        // 檢查是否只有其中一個存在且值不為 null
        boolean isOnlyOneDatePresentWithValueNotNull =
                (startAvailableDateString != null && endAvailableDateString == null) ||
                        (startAvailableDateString == null && endAvailableDateString != null);

        if (isOnlyOneDatePresentWithValueNotNull) {
            throw new FieldConflictException("filter startAvailableDate and endAvailableDate should exist together");
        }

        // 若 filterMap 中的 startAvailableDate 和 endAvailableDate 同時存在，確認 startAvailableDate 不晚於 endAvailableDate
        boolean isStartAndEndAvailableDatePresent = startAvailableDateString != null && endAvailableDateString != null;
        if (isStartAndEndAvailableDatePresent) {
            // 定義日期格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 解析日期字符串為 LocalDate
            LocalDate startAvailableDate = LocalDate.parse(startAvailableDateString, formatter);
            LocalDate endAvailableDate = LocalDate.parse(endAvailableDateString, formatter);

            if (startAvailableDate.isAfter(endAvailableDate)) {
                throw new FieldConflictException("filter startAvailableDate cannot be later than endAvailableDate");
            }

            requestFilterMap.put("startAvailableDate", startAvailableDate);
            requestFilterMap.put("endAvailableDate", endAvailableDate);
        }

        // 檢查頁碼
        if (propertySearchRequest.getPage() != null && propertySearchRequest.getPage() <= 0) {
            throw new FieldConflictException("page cannot be less than or equals to 0");
        }
    }

    /**
     * 根據房源搜索請求，獲取房源列表。
     *
     * @param propertySearchRequest 房源搜索請求物件，包含要應用於查詢的篩選條件、返回欄位和排序方式。
     * @param offset                查詢結果的偏移量。
     * @param limit                 查詢結果的限制數量。
     * @return 包含房源列表的結果 Map。
     */
    private List<PropertyVo> fetchProperties(PropertySearchRequest propertySearchRequest, int offset, int limit) {
        Map<String, Object> requestFilterMap = propertySearchRequest.getFilterMap();
        List<String> requestReturnFieldList = propertySearchRequest.getReturnFieldList();
        Map<String, String> requestSortMap = propertySearchRequest.getSortMap();

        SelectPropertyParam selectPropertyParam = SelectPropertyParam
                .builder()
                .filterMap(requestFilterMap)
                .returnFieldList(requestReturnFieldList)
                .sortMap(requestSortMap)
                .offset(offset)
                .limit(limit)
                .build();

        return propertyVoMapper.listByFilter(selectPropertyParam);
    }

    /**
     * 將房源物件列表轉換為精簡的房源 Map 列表。
     *
     * @param propertyVoList       房源物件列表。
     * @param requestReturnFieldList 返回的房源屬性欄位列表。
     * @return 包含精簡的房源 Map 的列表。
     */
    private List<Map<String, Object>> convertToLeanPropertyMapList(List<PropertyVo> propertyVoList, List<String> requestReturnFieldList) {
        List<Map<String, Object>> leanPropertyMapList = new ArrayList<>();

        for (PropertyVo propertyVo : propertyVoList) {
            Map<String, Object> propertyMap = new HashMap<>();

            for (String field : requestReturnFieldList) {
                // 處理地址屬性
                if (field.equals("address")) {
                    propertyMap.put("apartmentFloor", propertyVo.getApartmentFloor());
                    propertyMap.put("street", propertyVo.getStreet());
                    propertyMap.put("districtId", propertyVo.getAdminAreaLevel3DistrictId());
                    continue;
                }

                // 處理地區屬性
                if (field.equals("district")) {
                    propertyMap.put("districtId", propertyVo.getDistrictId());
                    propertyMap.put("districtName", propertyVo.getDistrictName());
                    propertyMap.put("parentDistrictId", propertyVo.getParentDistrictId());
                    propertyMap.put("parentDistrictName", propertyVo.getParentDistrictName());
                    continue;
                }

                // 處理其他屬性
                String getterMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);

                try {
                    Method getterMethod = propertyVo.getClass().getMethod(getterMethodName);
                    Object value = getterMethod.invoke(propertyVo);
                    propertyMap.put(field, value);
                } catch (Exception e) {
                    logger.error("PropertyVo does not have getter method for property: {}", field, e);
                }
            }

            leanPropertyMapList.add(propertyMap);
        }

        return leanPropertyMapList;
    }

    private String generateBucketPath(Long propertyId, String size,  String extension) {
        return "properties/%s/%s/%s".formatted(propertyId, "size-" + size, UUID.randomUUID() + "." + extension);
    }
}
