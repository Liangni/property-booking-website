package com.penny.service;

import com.amazonaws.HttpMethod;
import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyPictureVoMapper;
import com.penny.dao.PropertyVoMapper;
import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.dao.base.PictureDtBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.PropertyPictureBaseVoMapper;
import com.penny.enums.PictureDtSize;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.property.PropertySearchParam;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.s3.S3Buckets;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyVoMapper propertyVoMapper;

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final PictureBaseVoMapper pictureBaseVoMapper;

    private final PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;

    private final PropertyPictureVoMapper propertyPictureVoMapper;

    private final PictureDtBaseVoMapper pictureDtBaseVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final S3Service s3Service;

    private final S3Buckets s3Buckets;

    /**
     * 根據給定的搜尋參數，獲取房源資訊。
     *
     * @param request 房源搜尋參數
     * @return 符合搜尋條件的房源列表
     * @throws FieldConflictException 如果 numOfAvailableDays 和 startAvailableDateString 同時存在
     * @throws FieldConflictException 如果 startAvailableDateString 存在而 endAvailableDateString 不存在
     * @throws FieldConflictException 如果 startAvailableDateString 或 endAvailableDateString 格式錯誤
     */
    public List<PropertyVo> getProperties(PropertySearchParam request) {
        Integer numOfAvailableDays = request.getNumOfAvailableDays();
        String startAvailableDateString = request.getStartAvailableDateString();
        String endAvailableDateString = request.getEndAvailableDateString();

        if (numOfAvailableDays != null && startAvailableDateString != null) {
            throw new FieldConflictException("numOfAvailableDays and startAvailableDateString cannot coexist");
        }

        if (startAvailableDateString != null && endAvailableDateString == null) {
            throw new FieldConflictException("startAvailableDateString cannot exist without endAvailableDateString");
        }

        if(!isValidDateString(startAvailableDateString) || !isValidDateString(endAvailableDateString)) {
            throw new FieldConflictException("invalid startAvailableDateString and endAvailableDateString format");
        }

        if (numOfAvailableDays != null) {
            return propertyVoMapper.listByNumOfAvailableDays(request);
        }

        if (startAvailableDateString != null) {
            request.setStartAvailableDate(parseDateString(startAvailableDateString));
            request.setEndAvailableDate(parseDateString(endAvailableDateString));

            return propertyVoMapper.listByStartAndEndAvailableDate(request);
        }

        return propertyVoMapper.listByPropertyAttributes(request);
    }

    /**
     * 根據房源 ID 獲取房源基本資訊。
     *
     * @param propertyId 房源 ID
     * @return 房源基本資訊
     * @throws ResourceNotFoundException 如果找不到對應的房源
     */
    public PropertyBaseVo getProperty(Long propertyId) {
        return Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() -> new ResourceNotFoundException("property %s is not found".formatted(propertyId)));
    }

    /**
     * 根據上傳圖像請求獲取圖像上傳 URL。
     *
     * @param uploadImageRequest 包含房源ID和圖片附檔名的上傳圖像請求。
     * @return 返回一個包含不同尺寸圖像上傳 URL 的 map，鍵是尺寸標識，值是對應的預簽名 URL。
     * @throws FieldConflictException 如果房源ID或圖片附檔名為空，則拋出 FieldConflictException 異常。
     */
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

    /**
     * 根據房源ID和圖片尺寸編號獲取圖片下載 URL 列表。
     *
     * @param propertyId 要查詢的房源ID。
     * @param sizeNum 要查詢的圖片尺寸編號。
     * @return 返回一個包含圖片下載 URL 的列表，如果找不到符合條件的圖片，則返回空列表。
     * @throws FieldConflictException 如果房源ID或圖片尺寸編號為空，則拋出 FieldConflictException 異常。
     */
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
     * 檢查日期字串是否符合指定的格式（yyyy-MM-dd）。
     *
     * @param dateString 要檢查的日期字串
     * @return 如果日期字串符合指定的格式，則返回 true；否則返回 false
     */
    private boolean isValidDateString(String dateString) {
        // 定義日期格式的正規表達式
        String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";

        // 創建 Pattern 物件
        Pattern  DATE_PATTERN = Pattern.compile(DATE_REGEX);

        return !DATE_PATTERN.matcher(dateString).matches();
    }

    /**
     * 將日期字串解析為 LocalDate 物件。
     *
     * @param dateString 日期字串，格式為 "yyyy-MM-dd"
     * @return 解析後的 LocalDate 物件
     * @throws DateTimeParseException 如果日期字串無法解析為 LocalDate，則拋出此異常
     */
    private LocalDate parseDateString(String dateString) {
        // 定義日期格式模式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // 將日期字串解析為 LocalDate
            return LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            // 如果日期字串無法解析，則拋出 DateTimeParseException 異常
            throw new DateTimeParseException("日期字串無法解析為 LocalDate：" + dateString, dateString, 0);
        }
    }

    /**
     * 生成存儲桶路徑。
     *
     * @param propertyId 房源ID。
     * @param size 尺寸標識。
     * @param extension 文件擴展名。
     * @return 返回生成的存儲桶路徑。
     */
    private String generateBucketPath(Long propertyId, String size,  String extension) {
        return "properties/%s/%s/%s".formatted(propertyId, "size-" + size, UUID.randomUUID() + "." + extension);
    }
}
