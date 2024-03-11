package com.penny.service;

import com.penny.dao.PictureDtVoMapper;
import com.penny.dao.PropertyVoMapper;
import com.penny.daoParam.propertyVoMapper.ListByNumOfAvailableDaysParam;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PropertyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InitialPageService {
    private static final int DEFAULT_PROPERTY_PAGE = 1;

    private static final int DEFAULT_PROPERTY_LIMIT = 10;

    private static final int DEFAULT_NUM_OF_AVAILABLE_DAY = 5;

    private static final int DEFAULT_PICTURE_DT_PAGE = 1;

    private static final int DEFAULT_PICTURE_DT_LIMIT = 10;

    private static final String DEFAULT_PICTURE_DT_SIZE = "small";

    private final PropertyVoMapper propertyVoMapper;

    private final PictureDtVoMapper pictureDtVoMapper;

    @Autowired
    public InitialPageService(PropertyVoMapper propertyVoMapper, PictureDtVoMapper pictureDtVoMapper){
        this.propertyVoMapper = propertyVoMapper;
        this.pictureDtVoMapper = pictureDtVoMapper;
    }

    public Map<String, Object> getInitialPageData(){
        int propertyOffset = calculateOffset(DEFAULT_PROPERTY_PAGE, DEFAULT_PROPERTY_LIMIT);
        List<String> returnFieldList = new ArrayList<>();
        returnFieldList.add("propertyId");
        returnFieldList.add("district");
        returnFieldList.add("priceOnWeekdays");
        List<String> sortFieldList = List.of("district", "nearestAvailableDay");
        List<String> sortOrderList = List.of("asc", "asc");

        ListByNumOfAvailableDaysParam param  = ListByNumOfAvailableDaysParam
                .builder()
                .numOfAvailableDay(DEFAULT_NUM_OF_AVAILABLE_DAY)
                .returnFieldList(returnFieldList)
                .sortFieldList(sortFieldList)
                .sortOrderList(sortOrderList)
                .offset(propertyOffset)
                .limit(DEFAULT_PROPERTY_LIMIT)
                .build();
        List<PropertyVo> propertyVoList = propertyVoMapper.listByNumOfAvailableDays(param);

        int pictureDtOffset = calculateOffset(DEFAULT_PICTURE_DT_PAGE, DEFAULT_PICTURE_DT_LIMIT);
        for(PropertyVo propertyVo: propertyVoList) {
            //
            Long propertyId = propertyVo.getPropertyId();
            List<PictureDtVo> pictureDtVoList = pictureDtVoMapper.listByPropertyId(propertyId, DEFAULT_PICTURE_DT_SIZE, pictureDtOffset, DEFAULT_PICTURE_DT_LIMIT);
            propertyVo.setPictureDtVoList(pictureDtVoList);

            //

        }



        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("result", propertyVoList);
        return resultMap;
    }

    /**
     * 根據頁碼和每頁數量計算分頁查詢時的偏移量。
     *
     * @param page  目標頁碼。
     * @param limit 每頁數量。
     * @return 分頁查詢時的偏移量。
     */
    private int calculateOffset(int page, int limit) {
        return (page - 1) * limit;
    }
}
