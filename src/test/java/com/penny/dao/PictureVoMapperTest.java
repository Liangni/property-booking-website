package com.penny.dao;

import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.vo.PictureVo;
import com.penny.vo.base.PictureBaseVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
@DisplayName("Picture資料表測試")
public class PictureVoMapperTest {

    @Autowired
    PictureBaseVoMapper pictureBaseVoMapper;
    @Autowired
    PictureVoMapper pictureVoMapper;
    List<Long> pictureIdList = new ArrayList<>();

    @BeforeEach
    void setup() {
        for (int i = 0; i < 3; i++) {
            PictureBaseVo picture = PictureVo
                    .builder()
                    .pictureStoragePath("www.test.image" + i + ".com")
                    .build();

            pictureBaseVoMapper.insertSelective(picture);
            pictureIdList.add(picture.getPictureId());
        }
    }

    @Test
    @DisplayName("用 pictureId 列表搜尋房源圖片")
    void listByPictureIdList() {
        // 期望的圖片ID列表，取前2個圖片ID
        List<Long> expectPictureIdList = pictureIdList.subList(0, 2);

        // 根據期望的圖片ID列表搜索圖片
        List<PictureVo> returnPictureList = pictureVoMapper.listByPictureIdList(expectPictureIdList);

        // 斷言返回的圖片列表不為空
        Assertions.assertFalse(returnPictureList.isEmpty());

        // 將返回的圖片列表轉換為圖片ID列表
        List<Long> returnPictureIdList = returnPictureList
                        .stream()
                        .map(PictureVo::getPictureId)
                        .toList();

        // 斷言返回的圖片ID列表包含所有期望的圖片ID列表
        Assertions.assertTrue(returnPictureIdList.containsAll(expectPictureIdList));

        // 斷言返回的圖片ID列表不包含指定的圖片ID（pictureIdList.get(2)）
        Assertions.assertFalse(returnPictureIdList.contains(pictureIdList.get(2)));
    }
}
