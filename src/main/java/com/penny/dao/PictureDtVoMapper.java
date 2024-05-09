package com.penny.dao;

import com.penny.vo.PictureDtVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PictureDtVoMapper {
    /**
     * 根據提供的圖片ID列表，檢索圖片詳細資訊的列表
     *
     * @param pictureIdList 圖片ID的列表
     * @return 與提供的圖片ID相關的圖片詳細資訊的列表
     */
    List<PictureDtVo> listByPictureIdList(List<Long> pictureIdList);
    /**
     * 根據屬性 ID 和尺寸查詢圖片詳細資訊列表。
     *
     * @param propertyId 房源 ID，用於指定要查詢的屬性。
     * @param size 尺寸，用於指定要查詢的圖片的尺寸
     * @return 符合條件的圖片詳細資訊列表
     */
    List<PictureDtVo> listByPropertyIdAndSize(Long propertyId, Integer size, Integer offset, Integer limit);
    /**
     * 根據圖片ID和尺寸編號選擇圖片詳細資訊。
     *
     * @param pictureId 要查詢的圖片的ID。
     * @param sizeNum 要查詢的圖片的尺寸編號。
     * @return 返回與給定圖片ID和尺寸編號相關的圖片詳細資訊，如果找不到則返回null。
     */
    PictureDtVo selectByPictureIdAndSizeNum(Long pictureId, Integer sizeNum);


    List<PictureDtVo> selectByPictureId(Long pictureId);

    /**
     * 根據圖片 ID 將圖片詳細資訊設置為已上傳。
     *
     * @param pictureId 圖片的唯一識別符。
     * @return 更新的圖片詳細資訊數量。
     */
    Integer setIsUploadedTrueByPictureId(Long pictureId);
}
