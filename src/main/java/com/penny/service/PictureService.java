package com.penny.service;

import com.amazonaws.HttpMethod;
import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyPictureVoMapper;
import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.dao.base.PictureDtBaseVoMapper;
import com.penny.dao.base.PropertyPictureBaseVoMapper;
import com.penny.enums.PictureDtSize;
import com.penny.exception.FieldConflictException;
import com.penny.request.property.PropertyUploadImageRequest;
import com.penny.s3.S3Buckets;
import com.penny.s3.S3Service;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.PictureBaseVo;
import com.penny.vo.base.PictureDtBaseVo;
import com.penny.vo.base.PropertyPictureBaseVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final PictureBaseVoMapper pictureBaseVoMapper;

    private final PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;

    private final PropertyPictureVoMapper propertyPictureVoMapper;

    private final PictureDtBaseVoMapper pictureDtBaseVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    private final S3Service s3Service;

    private final S3Buckets s3Buckets;

    /**
     * 根據上傳圖像請求獲取圖像上傳 URL。
     *
     * @param uploadImageRequest 包含房源ID和圖片附檔名的上傳圖像請求。
     * @return 返回一個包含不同尺寸圖像上傳 URL 的 map，鍵是尺寸標識，值是對應的預簽名 URL。
     * @throws FieldConflictException 如果房源ID或圖片附檔名為空，則拋出 FieldConflictException 異常。
     */
    public Map<String, String> getPropertyImageUploadUrlMap(PropertyUploadImageRequest uploadImageRequest) {
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
    public List<String> listPropertyImageDownloadUrls(Long propertyId, Integer sizeNum) {
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
