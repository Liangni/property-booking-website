package com.penny.service;

import com.amazonaws.HttpMethod;
import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyPictureVoMapper;
import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.dao.base.PictureDtBaseVoMapper;
import com.penny.dao.base.PropertyBaseVoMapper;
import com.penny.dao.base.PropertyPictureBaseVoMapper;
import com.penny.enums.PictureDtSize;
import com.penny.exception.FieldConflictException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.exception.UnauthorizedException;
import com.penny.request.picture.PropertyImageUploadRequest;
import com.penny.s3.S3Buckets;
import com.penny.s3.S3Service;
import com.penny.vo.EcUserVo;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.PictureBaseVo;
import com.penny.vo.base.PictureDtBaseVo;
import com.penny.vo.base.PropertyBaseVo;
import com.penny.vo.base.PropertyPictureBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final PictureBaseVoMapper pictureBaseVoMapper;

    private final PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;

    private final PropertyPictureVoMapper propertyPictureVoMapper;

    private final PictureDtBaseVoMapper pictureDtBaseVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final EcUserService ecUserService;

    private final S3Service s3Service;

    private final S3Buckets s3Buckets;

    /**
     * 根據上傳圖像請求獲取圖像上傳 URL。
     *
     * @param uploadImageRequest 包含房源ID和圖片附檔名的上傳圖像請求。
     * @return 返回一個包含不同尺寸圖像上傳 URL 的 map，鍵是尺寸標識，值是對應的預簽名 URL。
     * @throws FieldConflictException 如果房源ID或圖片附檔名為空，則拋出 FieldConflictException 異常。
     */
    public Map<String, Object> getPropertyImageUploadUrlMap(PropertyImageUploadRequest uploadImageRequest) {
        // 檢驗參數
        Long propertyId = uploadImageRequest.getPropertyId();
        String fileExtension = uploadImageRequest.getFileExtension();
        Integer pictureOrder = uploadImageRequest.getPictureOrder();

        if (propertyId == null) { throw new FieldConflictException("propertyId is required");}
        if (fileExtension == null) { throw new FieldConflictException("fileExtension is required");}
        if (pictureOrder == null) { throw new FieldConflictException("pictureOrder is required"); }

        // 檢驗權限
        EcUserVo loginUser = ecUserService.getLoginUser();
        PropertyBaseVo property = propertyBaseVoMapper.selectByPrimaryKey(propertyId);

        if (!loginUser.getEcUserId().equals(property.getHostId())) {
            throw new UnauthorizedException("login user is not authorized for the operation");
        }

        // 創建圖片
        String pictureBucketPath = generateBucketPath(propertyId, "original", fileExtension);
        PictureBaseVo pictureBaseVo = PictureBaseVo
                .builder()
                .pictureStoragePath(pictureBucketPath)
                .pictureIsUploaded(false)
                .build();

        // 將圖片資料存入資料庫
        pictureBaseVoMapper.insertSelective(pictureBaseVo);
        Long pictureId = pictureBaseVo.getPictureId();

        // 檢查房源與圖片關係是否存在資料庫
        PropertyPictureVo propertyPictureVo = propertyPictureVoMapper.selectByPropertyIdAndPictureOrder(propertyId, pictureOrder);

        if (propertyPictureVo != null) {
            // 若存在，在資料庫更新房源圖片關係的圖片 id
            propertyPictureVo.setPictureId(pictureId);
            propertyPictureBaseVoMapper.updateByPrimaryKeySelective(propertyPictureVo);
        } else {
            // 若不存在，在資料庫新增房源圖片關係
            PropertyPictureBaseVo newPropertyPictureBaseVo = PropertyPictureBaseVo
                    .builder()
                    .propertyId(propertyId)
                    .pictureId(pictureId) // 插入剛創建的圖片 id
                    .propertyPictureOrder(pictureOrder) // 插入新的圖片排序值
                    .build();
            propertyPictureBaseVoMapper.insertSelective(newPropertyPictureBaseVo);
        }

        // 初始化回傳結果 map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pictureId", pictureId);

        // 將上傳原始尺寸圖像的預簽名URL 加進回傳結果
        resultMap.put(
                "sizeOriginal",
                s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), pictureBucketPath, HttpMethod.PUT)
        );

        // 創建不同尺寸的圖片DT及其對應的預簽名URL
        PictureDtSize.stream()
                .forEach(size -> {
                    int pictureSizeNum = size.getNum();
                    String picDtBucketPath = generateBucketPath(propertyId, String.valueOf(pictureSizeNum), fileExtension);
                    PictureDtBaseVo pictureDtBaseVo = PictureDtBaseVo
                            .builder()
                            .pictureDtStoragePath(picDtBucketPath)
                            .pictureDtSize(pictureSizeNum)
                            .pictureId(pictureId)
                            .pictureDtIsUploaded(false)
                            .build();
                    pictureDtBaseVoMapper.insertSelective(pictureDtBaseVo);

                    // 將上傳指定尺寸圖像的預簽名URL 加進 result map
                    resultMap.put(
                            "size%s".formatted(pictureSizeNum),
                            s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), picDtBucketPath, HttpMethod.PUT)
                    );
                });

        return resultMap;
    }

    /**
     * 獲取已發佈的房源的指定大小的圖片下載 URL 列表。
     *
     * @param propertyId 房源的唯一識別號
     * @param sizeNum    圖片大小編號
     * @return 圖片下載 URL 列表
     * @throws FieldConflictException 如果 propertyId 或 sizeNum 為 null，則拋出此異常
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發佈，則拋出此異常
     */
    public List<Map<String, Object>> listPublishedPropertyImageDownloadUrls(Long propertyId, Integer sizeNum) {
        // 檢驗 propertyId, sizeNum
        if (propertyId == null || sizeNum == null) {
            throw new FieldConflictException("propertyId and sizeNum are required");
        }

        // 檢查房源是否為 isPublished
        PropertyBaseVo property = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(property == null || !property.getIsPublished()) {
            throw new ResourceNotFoundException("property with property %s not found".formatted(propertyId));
        }

        // 找尋房源圖片並排序
        List<PropertyPictureVo> propertyPictureVoList = listPropertyPictureInOrder(propertyId);

        // 使用抽取的方法獲取圖片下載 URL 列表
        return getPropertyImageDownloadUrlMapList(propertyPictureVoList, sizeNum);
    }

    /**
     * 根據房源ID和圖片尺寸編號獲取圖片下載 URL 列表。
     *
     * @param propertyId 要查詢的房源ID。
     * @param sizeNum 要查詢的圖片尺寸編號。
     * @return 返回一個包含圖片下載 URL 的列表，如果找不到符合條件的圖片，則返回空列表。
     * @throws FieldConflictException 如果房源ID或圖片尺寸編號為空，則拋出 FieldConflictException 異常。
     */
    public List<Map<String, Object>> listPropertyImageDownloadUrls(Long propertyId, Integer sizeNum) {
        // 檢驗 propertyId, sizeNum
        if (propertyId == null || sizeNum == null) {
            throw new FieldConflictException("propertyId and sizeNum are required");
        }

        // 找尋房源圖片並排序
        List<PropertyPictureVo> propertyPictureVoList = listPropertyPictureInOrder(propertyId);

        // 使用抽取的方法獲取圖片下載 URL 列表
        return getPropertyImageDownloadUrlMapList(propertyPictureVoList, sizeNum);
    }

    /**
     * 從資料庫中依據 propertyId 找尋房源圖片並按照順序排序。
     *
     * @param propertyId 房源ID
     * @return 排序後的房源圖片列表
     */
    private List<PropertyPictureVo> listPropertyPictureInOrder(Long propertyId) {
        // 從資料庫找尋 propertyId 相符的房源圖片
        List<PropertyPictureVo> propertyPictureVoList = propertyPictureVoMapper.listByPropertyId(propertyId);

        // 依 propertyPictureOrder 排序房源圖片
        Collections.sort(propertyPictureVoList, Comparator.comparingLong(PropertyPictureVo::getPropertyPictureOrder));

        return propertyPictureVoList;
    }

    /**
     * 取得房源圖片下載 URL 列表。
     *
     * @param propertyPictureVoList 房屋圖片列表
     * @param sizeNum 圖片大小編號
     * @return 包含圖片下載 URL 的 Map 列表
     */
    private List<Map<String, Object>> getPropertyImageDownloadUrlMapList(List<PropertyPictureVo> propertyPictureVoList, Integer sizeNum) {
        return propertyPictureVoList
                .stream()
                // 將每個房屋圖片 map 為對應的圖片下載 URL Map
                .map((propertyPictureVo) ->
                        preparePictureDownloadUrlMap(propertyPictureVo.getPictureId(), sizeNum, propertyPictureVo.getPropertyPictureOrder()
                    )
                )
                .toList();
    }

    /**
     * 準備圖片下載 URL Map。
     *
     * @param pictureId 圖片 ID
     * @param sizeNum 圖片大小編號
     * @param pictureOrder 圖片順序
     * @return 包含圖片下載 URL 的 Map
     */
    private Map<String, Object> preparePictureDownloadUrlMap(Long pictureId, Integer sizeNum, Integer pictureOrder) {
        Map<String, Object> propertyPictureMap = new HashMap<>();
        propertyPictureMap.put("pictureOrder", pictureOrder);
        propertyPictureMap.put("sizeNum", sizeNum);
        propertyPictureMap.put("downloadUrl",
                s3Service.generatePreSignedUrl(
                        s3Buckets.getCustomer(),
                        getPictureDtStoragePath(pictureId, sizeNum),
                        HttpMethod.GET
                )
        );
        return propertyPictureMap;
    }

    /**
     * 從資料庫中找出指定圖片大小的圖片下載 URL。
     *
     * @param pictureId      圖片ID
     * @param pictureDtSizeNum 圖片大小編號
     * @return 圖片下載 URL，如果找不到則返回 null
     */
    private String getPictureDtStoragePath(Long pictureId, Integer pictureDtSizeNum) {
        // 找出 pictureId 與 sizeNum 相符的所有圖片DT物件
        PictureDtVo pictureDtVo = pictureDtVoMapper.selectByPictureIdAndSizeNum(pictureId, pictureDtSizeNum);
        if (pictureDtVo == null) return null;

        // 取得圖片DT物件的Url
        return pictureDtVo.getPictureDtStoragePath();
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
