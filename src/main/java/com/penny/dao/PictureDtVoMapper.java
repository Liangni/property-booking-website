package com.penny.dao;

import com.penny.vo.PictureDtVo;

import java.util.List;

public interface PictureDtVoMapper {
    /**
     * 根據提供的圖片ID列表，檢索圖片詳細資訊的列表
     *
     * @param pictureIdList 圖片ID的列表
     * @return 與提供的圖片ID相關的圖片詳細資訊的列表
     */
    List<PictureDtVo> listByPictureIdList(List<Long> pictureIdList);
    /**
     * 根據屬性 ID 和尺寸查詢圖片詳細信息列表。
     *
     * @param propertyId 房源 ID，用於指定要查詢的屬性。
     * @param size 尺寸，用於指定要查詢的圖片的尺寸
     * @return 符合條件的圖片詳細資訊列表
     */
    List<PictureDtVo> listByPropertyId(Long propertyId, String size, Integer offset, Integer limit);
}
