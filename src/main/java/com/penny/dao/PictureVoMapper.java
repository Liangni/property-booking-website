package com.penny.dao;

import com.penny.vo.PictureVo;

import java.util.List;

public interface PictureVoMapper {
    List<PictureVo> ListByPictureIdList(List<Long> pictureIdList);
}
