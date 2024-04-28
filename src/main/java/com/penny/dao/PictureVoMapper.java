package com.penny.dao;

import com.penny.vo.PictureVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PictureVoMapper {
    /**
     * 根據圖片ID列表查詢圖片。
     *
     * @param pictureIdList 圖片ID列表
     * @return 符合圖片ID列表的圖片列表
     */
    List<PictureVo> listByPictureIdList(List<Long> pictureIdList);
}
