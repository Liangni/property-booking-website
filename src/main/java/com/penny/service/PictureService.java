package com.penny.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.penny.dao.EcUserPictureVoMapper;
import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyPictureVoMapper;
import com.penny.dao.base.*;
import com.penny.enums.PictureDtSizeEnum;
import com.penny.exception.RequestValidationException;
import com.penny.exception.ResourceNotFoundException;
import com.penny.exception.AuthorizationException;
import com.penny.request.CreateEcUserPictureRequest;
import com.penny.request.CreatePropertyPictureRequest;
import com.penny.s3.S3Buckets;
import com.penny.s3.S3Service;
import com.penny.vo.EcUserPictureVo;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PictureService {

    private final PictureDtSizeEnum DEFAULT_EC_USER_PICTURE_DT_SIZE = PictureDtSizeEnum.SIZE_3;
    private final Set<String> PICTURE_EXTENSIONS = new HashSet<>(Set.of(
            "jpg", "jpeg", "png"
    ));

    private final PropertyBaseVoMapper propertyBaseVoMapper;

    private final PictureBaseVoMapper pictureBaseVoMapper;

    private final PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;

    private final PropertyPictureVoMapper propertyPictureVoMapper;

    private final PictureDtBaseVoMapper pictureDtBaseVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final EcUserPictureBaseVoMapper ecUserPictureBaseVoMapper;

    private final EcUserPictureVoMapper ecUserPictureVoMapper;

    private final EcUserService ecUserService;

    private final S3Service s3Service;

    private final S3Buckets s3Buckets;

    /**
     * 根據上傳圖像請求獲取圖像上傳 URL。
     *
     * @param propertyId 房源 ID
     * @param fileExtension 檔案副檔名
     * @return 返回一個包含不同尺寸圖像上傳 URL 的 map，鍵是尺寸標識，值是對應的預簽名 URL。
     * @throws RequestValidationException 如果房源ID或圖片附檔名為空，則拋出 RequestValidationException 異常。
     */
    @Transactional
    public Map<String, Object> getPropertyPictureUploadUrlMap(Long propertyId, String fileExtension) {
        // 檢驗參數
        if (!isPictureFileExtension(fileExtension)) throw new RequestValidationException("fileExtension can only be %s".formatted(PICTURE_EXTENSIONS.toString()));

        // 檢查房源是否存在
        PropertyBaseVo propertyBaseVo = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() ->  new ResourceNotFoundException("property with id %s not found".formatted(propertyId)));

        // 檢驗登入使用者是否為房源出租人
        ecUserService.validatePropertyOwnership(propertyBaseVo.getHostId());

        // 創建圖片
        String lowerCaseFileExtension = fileExtension.toLowerCase();
        String pictureBucketPath = generatePropertyPictureBucketPath(propertyId, "original", lowerCaseFileExtension);
        PictureBaseVo pictureBaseVo = PictureBaseVo
                .builder()
                .pictureStoragePath(pictureBucketPath)
                .pictureIsUploaded(false)
                .build();

        // 將圖片資料存入資料庫
        pictureBaseVoMapper.insertSelective(pictureBaseVo);
        Long pictureId = pictureBaseVo.getPictureId();

        // 初始化回傳結果 map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pictureId", pictureId);

        // 將原始尺寸圖像的預簽名上傳 URL 加進回傳結果
        resultMap.put(
                "sizeOriginal",
                s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), pictureBucketPath, HttpMethod.PUT)
        );

        // 創建不同尺寸的圖片DT及其對應的預簽名URL
        PictureDtSizeEnum.stream()
                .forEach(size -> {
                    int pictureSizeNum = size.getDisplayNum();
                    String picDtBucketPath = generatePropertyPictureBucketPath(propertyId, String.valueOf(pictureSizeNum), lowerCaseFileExtension);

                    // 創建圖片 DT
                    PictureDtBaseVo pictureDtBaseVo = PictureDtBaseVo
                            .builder()
                            .pictureDtStoragePath(picDtBucketPath)
                            .pictureDtSize(pictureSizeNum)
                            .pictureId(pictureId)
                            .pictureDtIsUploaded(false)
                            .build();
                    pictureDtBaseVoMapper.insertSelective(pictureDtBaseVo);

                    // 將指定尺寸圖像的預簽名上傳 URL 加進回傳結果
                    resultMap.put(
                            "size%s".formatted(pictureSizeNum),
                            s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), picDtBucketPath, HttpMethod.PUT)
                    );
                });

        return resultMap;
    }

    /**
     * 更新房源圖片。
     *
     * @param propertyId 房源 ID
     * @param updateRequest 更新房源圖片的請求物件，包含要更新的房源和圖片資訊。
     * @throws RequestValidationException 如果必要的屬性值為空，將拋出此異常。
     * @throws AuthorizationException 如果使用者未經授權執行操作，將拋出此異常。
     * @throws ResourceNotFoundException 如果找不到指定的圖片，將拋出此異常。
     */
    @Transactional
    public void createPropertyPicture(Long propertyId, CreatePropertyPictureRequest updateRequest) {
        Long pictureId = updateRequest.getPictureId();
        Integer pictureOrder = updateRequest.getPictureOrder();

        // 檢查房源是否存在
        PropertyBaseVo propertyBaseVo = Optional.ofNullable(propertyBaseVoMapper.selectByPrimaryKey(propertyId))
                .orElseThrow(() ->  new ResourceNotFoundException("property with id %s not found".formatted(propertyId)));

        // 檢驗登入使用者是否為房源出租人
        ecUserService.validatePropertyOwnership(propertyBaseVo.getHostId());

        // 檢查圖片是否存在
        PictureBaseVo pictureBaseVo = Optional.ofNullable(pictureBaseVoMapper.selectByPrimaryKey(pictureId))
                .orElseThrow(() -> new ResourceNotFoundException("picture with id %s not found".formatted(pictureId)));

        // 檢查圖片 dt 是否存在
        List<PictureDtVo> pictureDtVoList = pictureDtVoMapper.selectByPictureId(pictureId);
        if(pictureDtVoList.isEmpty()) throw new ResourceNotFoundException("resized picture with pictureId %s not found".formatted(pictureId));

        // 檢查圖片是否上傳
        try {
            s3Service.getObjects(s3Buckets.getCustomer(), pictureBaseVo.getPictureStoragePath());
        } catch (SdkClientException e) {
            throw new RequestValidationException("picture with original size not uploaded");
        }

        // 檢查圖片 dt 是否已上傳
        pictureDtVoList.forEach(pictureDtVo -> {
            try {
                s3Service.getObjects(s3Buckets.getCustomer(), pictureDtVo.getPictureDtStoragePath());
            } catch (SdkClientException e) {
                throw new RequestValidationException("resized picture with pictureId %s not uploaded".formatted(pictureId));
            }
        });

        // 更新圖片上傳狀態
        PictureBaseVo updatePictureVo = PictureBaseVo
                .builder()
                .pictureId(pictureId)
                .pictureIsUploaded(true)
                .build();

        pictureBaseVoMapper.updateByPrimaryKeySelective(updatePictureVo);

        // 更新圖片詳細資訊上傳狀態
        pictureDtVoMapper.setIsUploadedTrueByPictureId(pictureId);

        // 檢查房源與相同圖片順序是否已存在圖片
        PropertyPictureVo propertyPictureVo = propertyPictureVoMapper.selectByPropertyIdAndPictureOrder(propertyId, pictureOrder);

        if (propertyPictureVo != null) {
            // 若存在，更新房源圖片關係的圖片 id
            propertyPictureVo.setPictureId(pictureId);
            propertyPictureBaseVoMapper.updateByPrimaryKeySelective(propertyPictureVo);
        } else {
            // 若不存在，新增房源圖片關係
            PropertyPictureBaseVo newPropertyPictureBaseVo = PropertyPictureBaseVo
                    .builder()
                    .propertyId(propertyId)
                    .pictureId(pictureId)
                    .propertyPictureOrder(pictureOrder)
                    .build();
            propertyPictureBaseVoMapper.insertSelective(newPropertyPictureBaseVo);
        }
    }

    /**
     * 獲取已發佈的房源的指定大小的圖片下載 URL 列表。
     *
     * @param propertyId 房源的唯一識別號
     * @param sizeNum    圖片大小編號
     * @return 圖片下載 URL 列表
     * @throws RequestValidationException 如果 propertyId 或 sizeNum 為 null，則拋出此異常
     * @throws ResourceNotFoundException 如果找不到指定的房源或房源未發佈，則拋出此異常
     */
    public List<Map<String, Object>> listPublishedPropertyPictureDownloadUrl(Long propertyId, Integer sizeNum) {

        // 檢查房源是否為 isPublished
        PropertyBaseVo property = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(property == null || !property.getIsPublished()) {
            throw new ResourceNotFoundException("property with property %s not found".formatted(propertyId));
        }

        // 找尋房源圖片並排序
        List<PropertyPictureVo> propertyPictureVoList = listPropertyPictureInOrder(propertyId);

        // 使用抽取的方法獲取圖片下載 URL 列表
        return listPropertyPictureDtDownloadUrlMap(propertyPictureVoList, sizeNum);
    }

    /**
     * 根據房源ID和圖片尺寸編號獲取圖片下載 URL 列表。
     *
     * @param propertyId 要查詢的房源ID。
     * @param sizeNum 要查詢的圖片尺寸編號。
     * @return 返回一個包含圖片下載 URL 的列表，如果找不到符合條件的圖片，則返回空列表。
     * @throws RequestValidationException 如果房源ID或圖片尺寸編號為空，則拋出 RequestValidationException 異常。
     */
    public List<Map<String, Object>> listPropertyPictureDownloadUrl(Long propertyId, Integer sizeNum) {
        // 檢驗 propertyId, sizeNum
        if (propertyId == null || sizeNum == null) {
            throw new RequestValidationException("propertyId and sizeNum are required");
        }

        // 檢查房源是否存在
        PropertyBaseVo propertyBaseVo = propertyBaseVoMapper.selectByPrimaryKey(propertyId);
        if(propertyBaseVo == null) {
            throw new ResourceNotFoundException("property with id %s not found".formatted(propertyId));
        }

        // 檢驗登入使用者是否為房源出租人
        ecUserService.validatePropertyOwnership(propertyBaseVo.getHostId());

        // 找尋房源圖片並排序
        List<PropertyPictureVo> propertyPictureVoList = listPropertyPictureInOrder(propertyId);

        // 使用抽取的方法獲取圖片下載 URL 列表
        return listPropertyPictureDtDownloadUrlMap(propertyPictureVoList, sizeNum);
    }

    /**
     * 根據使用者 ID 列出使用者圖片上傳 URL。
     *
     * @param ecUserId 使用者的 ID
     * @param fileExtension 檔案擴展名
     * @return 返回包含圖片上傳 URL 的 Map
     * @throws RequestValidationException 如果檔案擴展名不合法，則拋出請求驗證異常
     * @throws AuthorizationException 如果登入使用者無權執行操作，則拋出授權異常
     */
    @Transactional
    public Map<String, Object> listEcUserPictureUploadUrl(Long ecUserId, String fileExtension) {
        int defaultSizeNum = DEFAULT_EC_USER_PICTURE_DT_SIZE.getDisplayNum();

        // 檢驗參數
        if (!isPictureFileExtension(fileExtension)) throw new RequestValidationException("fileExtension can only be %s".formatted(PICTURE_EXTENSIONS.toString()));

        // 檢查使用者是否有權限執行操作
        if (!ecUserId.equals(ecUserService.getLoginUser().getEcUserId())) throw new AuthorizationException("login user is not authorized for the operation");

        // 創建圖片
        String lowerCaseFileExtension = fileExtension.toLowerCase();
        String pictureBucketPath = generateEcUserPictureBucketPath(ecUserId, "original", lowerCaseFileExtension);
        PictureBaseVo pictureBaseVo = PictureBaseVo
                .builder()
                .pictureStoragePath(pictureBucketPath)
                .pictureIsUploaded(false)
                .build();

        // 將圖片資料存入資料庫
        pictureBaseVoMapper.insertSelective(pictureBaseVo);
        Long pictureId = pictureBaseVo.getPictureId();

        // 初始化回傳結果 map
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pictureId", pictureId);

        // 將原始尺寸圖像的預簽名上傳 URL 加進回傳結果
        resultMap.put(
                "sizeOriginal",
                s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), pictureBucketPath, HttpMethod.PUT)
        );

        // 新增圖片 dt
        String picDtBucketPath = generateEcUserPictureBucketPath(ecUserId, String.valueOf(defaultSizeNum), lowerCaseFileExtension);
        PictureDtBaseVo pictureDtBaseVo = PictureDtBaseVo
                .builder()
                .pictureDtStoragePath(picDtBucketPath)
                .pictureDtSize(defaultSizeNum)
                .pictureId(pictureId)
                .pictureDtIsUploaded(false)
                .build();
        pictureDtBaseVoMapper.insertSelective(pictureDtBaseVo);

        // 將指定尺寸圖像的預簽名上傳 URL 加進回傳結果
        resultMap.put(
                "size%s".formatted(defaultSizeNum),
                s3Service.generatePreSignedUrl(s3Buckets.getCustomer(), picDtBucketPath, HttpMethod.PUT)
        );

        return resultMap;
    }

    /**
     * 創建使用者圖片。
     *
     * @param ecUserId 使用者的 ID
     * @param createRequest 創建使用者圖片的請求
     * @throws AuthorizationException 如果登入使用者無權執行操作，則拋出授權異常
     * @throws ResourceNotFoundException 如果找不到指定的圖片或圖片 dt，則拋出資源未找到異常
     * @throws RequestValidationException 如果圖片未上傳成功，則拋出請求驗證異常
     */
    @Transactional
    public void createEcUserPicture(Long ecUserId, CreateEcUserPictureRequest createRequest) {
        Long pictureId = createRequest.getPictureId();
        int defaultSizeNum = DEFAULT_EC_USER_PICTURE_DT_SIZE.getDisplayNum();

        // 檢查使用者是否有權限執行操作
        if (!ecUserId.equals(ecUserService.getLoginUser().getEcUserId())) throw new AuthorizationException("login user is not authorized for the operation");

        //  檢查資料庫是否有指定圖片
        PictureBaseVo pictureBaseVo = Optional.ofNullable(pictureBaseVoMapper.selectByPrimaryKey(pictureId))
                .orElseThrow(() -> new ResourceNotFoundException("picture with id %s not found".formatted(pictureId)));

        //  檢查資料庫是否有指定圖片 dt
        PictureDtVo pictureDtVo = Optional.ofNullable(pictureDtVoMapper.selectByPictureIdAndSizeNum(pictureBaseVo.getPictureId(), defaultSizeNum))
                .orElseThrow(() -> new ResourceNotFoundException("resized picture with pictureId %s not found".formatted(pictureId)));

        // 檢查圖片是否已上傳
        try {
           s3Service.getObjects(s3Buckets.getCustomer(), pictureBaseVo.getPictureStoragePath());
        } catch (SdkClientException e) {
            throw new RequestValidationException("picture with original size not uploaded");
        }

        // 檢查圖片 dt 是否已上傳
        try {
            s3Service.getObjects(s3Buckets.getCustomer(), pictureDtVo.getPictureDtStoragePath());
        } catch (SdkClientException e) {
            throw new RequestValidationException("resized picture with pictureId %s not uploaded".formatted(pictureId));
        }

        // 更新圖片上傳狀態
        PictureBaseVo updatePictureVo = PictureBaseVo
                .builder()
                .pictureId(pictureId)
                .pictureIsUploaded(true)
                .build();

        pictureBaseVoMapper.updateByPrimaryKeySelective(updatePictureVo);

        // 更新圖片詳細資訊上傳狀態
        pictureDtVoMapper.setIsUploadedTrueByPictureId(pictureId);

        // 檢查 ecUserId 是否已存在使用者圖片
        EcUserPictureVo ecUserPictureVo = ecUserPictureVoMapper.selectByEcUserId(ecUserId);

        if (ecUserPictureVo != null) {
            // 若存在，更新房源圖片關係的圖片 id
            ecUserPictureVo.setPictureId(pictureId);
            ecUserPictureBaseVoMapper.updateByPrimaryKeySelective(ecUserPictureVo);
        } else {
            // 若不存在，新增房源圖片關係
            EcUserPictureBaseVo newEcUserPictureBaseVo = EcUserPictureBaseVo
                    .builder()
                    .ecUserId(ecUserId)
                    .pictureId(pictureId)
                    .build();

            ecUserPictureBaseVoMapper.insertSelective(newEcUserPictureBaseVo);
        }
    }

    /**
     * 根據使用者 ID 獲取使用者圖片下載 URL。
     *
     * @param ecUserId 使用者的 ID
     * @return 返回包含圖片下載 URL 的 Map
     * @throws AuthorizationException 如果登入使用者無權執行操作，則拋出授權異常
     * @throws ResourceNotFoundException 如果找不到指定的使用者圖片，則拋出資源未找到異常
     * @throws RequestValidationException 如果圖片未成功上傳，則拋出請求驗證異常
     */
    public Map<String, Object> getEcUserPictureDownloadUrl(Long ecUserId) {
        int defaultSizeNum = DEFAULT_EC_USER_PICTURE_DT_SIZE.getDisplayNum();

        // 找尋使用者圖片
        EcUserPictureBaseVo ecUserPictureBaseVo = Optional.ofNullable(ecUserPictureVoMapper.selectByEcUserId(ecUserId))
                .orElseThrow(() -> new ResourceNotFoundException("ecUser picture with ecUserId %s not found".formatted(ecUserId)));

        // 找出 pictureId 與 sizeNum 相符的所有圖片DT物件
        PictureDtVo pictureDtVo = pictureDtVoMapper.selectByPictureIdAndSizeNum(ecUserPictureBaseVo.getPictureId(), defaultSizeNum);

        if(!pictureDtVo.getPictureDtIsUploaded()) throw new RequestValidationException("picture with ecUserId %s not uploaded".formatted(ecUserId));

        // 取得圖片DT物件的Url
        String pictureDtStoragePath =  pictureDtVo.getPictureDtStoragePath();

        // 將圖像的預簽名上傳 URL 加進回傳結果
        return Map.of(
                "sizeNum", defaultSizeNum,
                "downloadUrl", s3Service.generatePreSignedUrl(
                s3Buckets.getCustomer(),
                pictureDtStoragePath,
                HttpMethod.GET)
        );
    }

    /**
     * 檢查給定的檔案副檔名是否代表常見的圖片檔案副檔名。
     *
     * @param fileExtension 要檢查的檔案副檔名（例如 "jpg", "png", "jpeg" 等）
     * @return 如果檔案副檔名是常見的圖片檔案副檔名則返回 true，否則返回 false
     */
    private boolean isPictureFileExtension(String fileExtension) {
        // 將檔案副檔名轉換為小寫（進行大小寫不敏感的比較）
        String normalizedExtension = fileExtension.toLowerCase();

        // 檢查正規化後的副檔名是否在圖片檔案副檔名集合中
        return PICTURE_EXTENSIONS.contains(normalizedExtension);
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
    private List<Map<String, Object>> listPropertyPictureDtDownloadUrlMap(List<PropertyPictureVo> propertyPictureVoList, Integer sizeNum) {
        return propertyPictureVoList
                .stream()
                // 將每個房屋圖片 map 為對應的圖片下載 URL Map
                .map((propertyPictureVo) -> {
                    Long pictureId = propertyPictureVo.getPictureId();
                    Integer pictureOrder = propertyPictureVo.getPropertyPictureOrder();
                    return preparePictureDtDownloadUrlMap(pictureId, sizeNum, pictureOrder);
                })
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
    private Map<String, Object> preparePictureDtDownloadUrlMap(Long pictureId, Integer sizeNum, Integer pictureOrder) {
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
    private String generatePropertyPictureBucketPath(Long propertyId, String size,  String extension) {
        return "properties/%s/%s/%s".formatted(propertyId, "size-" + size, UUID.randomUUID() + "." + extension);
    }

    /**
     * 生成存儲桶路徑。
     *
     * @param ecUserId 使用者ID。
     * @param size 尺寸標識。
     * @param extension 文件擴展名。
     * @return 返回生成的存儲桶路徑。
     */
    private String generateEcUserPictureBucketPath(Long ecUserId, String size,  String extension) {
        return "ecUsers/%s/%s/%s".formatted(ecUserId, "size-" + size, UUID.randomUUID() + "." + extension);
    }
}
