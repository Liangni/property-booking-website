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
                    .pictureUrl("www.test.image" + i + ".com")
                    .build();

            pictureBaseVoMapper.insertSelective(picture);
            pictureIdList.add(picture.getPictureId());
        }
    }

    @Test
    @DisplayName("用 pictureId 列表搜尋房源圖片")
    void ListByPictureIdList() {
        List<Long> expectPictureIdList = pictureIdList.subList(0,1);
        List<PictureVo> returnPictureList = pictureVoMapper.ListByPictureIdList(expectPictureIdList);
        Assertions.assertNotNull(returnPictureList);

        List<Long> returnPictureIdList = returnPictureList
                .stream()
                .map(PictureVo::getPictureId)
                .toList();
        Assertions.assertTrue(returnPictureIdList.containsAll(expectPictureIdList));
        Assertions.assertFalse(returnPictureIdList.containsAll(pictureIdList));
    }
}
