package com.penny.dao;

import com.penny.dao.base.PictureBaseVoMapper;
import com.penny.dao.base.PictureDtBaseVoMapper;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PictureVo;
import com.penny.vo.base.PictureBaseVo;
import com.penny.vo.base.PictureDtBaseVo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@DisplayName("PictureDt資料表測試")
public class PictureDtVoMapperTest {

    @Autowired
    PictureBaseVoMapper pictureBaseVoMapper;
    @Autowired
    PictureDtBaseVoMapper pictureDtBaseVoMapper;
    @Autowired
    PictureDtVoMapper pictureDtVoMapper;
    List<Long> pictureIdList = new ArrayList<>();
    List<String> sizeList = List.of("Large", "medium", "small");

    /**
     * 設置測試環境，創建並插入圖片及其詳細資訊
     */
    @BeforeEach
    void setup() {
        // 循環創建三個圖片
        for (int i = 0; i < 3; i++) {
            // 創建圖片並插入到資料庫中
            PictureBaseVo picture = PictureBaseVo
                    .builder()
                    .pictureUrl("www.test.image" + i + ".com")
                    .build();
            pictureBaseVoMapper.insertSelective(picture);
            long pictureId = picture.getPictureId();
            pictureIdList.add(picture.getPictureId()); // 將圖片ID添加到列表中

            // 創建圖片詳細資訊並插入到資料庫中
            PictureDtBaseVo pictureDt = null;
            for(String size: sizeList) {
                pictureDt = PictureDtBaseVo
                        .builder()
                        .pictureId(pictureId)
                        .pictureDtUrl("www.test.image" + "dt" + i + ".com")
                        .pictureDtSize(size)
                        .build();

                pictureDtBaseVoMapper.insertSelective(pictureDt);
            }
        }
    }

    @Test
    @DisplayName("用 pictureId 列表搜尋房源圖片")
    void ListByPictureIdList() {
        // 期望的圖片ID列表，取前2個圖片ID
        List<Long> expectPictureIdList = pictureIdList.subList(0, 2);

        // 根據期望的圖片ID列表搜索PictureDt
        List<PictureDtVo> returnPictureDtList = pictureDtVoMapper.ListByPictureIdList(expectPictureIdList);

        // 斷言返回的PictureDt列表不為空
        Assertions.assertFalse(returnPictureDtList.isEmpty());

        // 將返回的PictureDt列表轉換為圖片ID列表
        List<Long> returnPictureIdList = returnPictureDtList
                .stream()
                .map(PictureDtVo::getPictureId)
                .toList();

        // 斷言返回的圖片ID列表包含所有期望的圖片ID列表
        Assertions.assertTrue(returnPictureIdList.containsAll(expectPictureIdList));

        // 斷言返回的圖片ID列表不包含指定的圖片ID（pictureIdList.get(2)）
        Assertions.assertFalse(returnPictureIdList.contains(pictureIdList.get(2)));
    }

}
