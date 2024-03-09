package com.penny.dao;

import com.penny.vo.PictureDtVo;

import java.util.List;

public interface PictureDtVoMapper {
    /**
     * 根據提供的圖片ID列表，檢索圖片詳細資訊的列表。
     *
     * @param pictureIdList 圖片ID的列表。
     * @return 與提供的圖片ID相關的圖片詳細資訊的列表。
     */
    List<PictureDtVo> ListByPictureIdList(List<Long> pictureIdList);
}
