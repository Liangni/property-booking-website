package com.penny.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penny.dao.PropertyVoMapper;
import com.penny.redis.RedisService;
import com.penny.request.SearchPropertyRequest;
import com.penny.request.SearchPropertyRequestDTO;
import com.penny.request.SearchPropertyRequestDTOMapper;
import com.penny.response.ReadInitialPagePropertyResponse;
import com.penny.response.ReadInitialPagePropertyResponseMapper;
import com.penny.util.JsonConverter;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.PropertyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitialPageService {
    private static final int DEFAULT_NUM_OF_AVAILABLE_DAYS = 5;
    private static final Integer DEFAULT_PICTURE_DT_SIZE_NUM = 1;

    private final PropertyVoMapper propertyVoMapper;

    private final PictureService pictureService;

    private final SearchPropertyRequestDTOMapper searchPropertyRequestDTOMapper;

    private final ReadInitialPagePropertyResponseMapper readInitialPagePropertyResponseMapper;

    private final RedisService redisService;

    private final JsonConverter jsonConverter;

    /**
     * 從 Redis 中獲取初始頁面的房源資料列表，如果 Redis 中不存在則進行查詢並存儲到 Redis 中後返回。
     *
     * @return 初始頁面的房源資料列表
     */
    public List<ReadInitialPagePropertyResponse> getInitialPageData(){
        // 準備房源查詢參數
        SearchPropertyRequest searchPropertyRequest = SearchPropertyRequest
                .builder()
                .numOfAvailableDays(DEFAULT_NUM_OF_AVAILABLE_DAYS)
                .build();

        // 從 Redis 中獲取房源列表
        List<PropertyVo> propertyVoList = getPropertyListFromRedis();

        // 如果 Redis 中的房源列表為空，則進行查詢並存儲到 Redis 中
        if (propertyVoList == null) {
            // 準備房源查詢參數的 DTO
            SearchPropertyRequestDTO searchPropertyRequestDTO =  searchPropertyRequestDTOMapper.apply(searchPropertyRequest);

            // 根據查詢參數 DTO 查詢房源列表
            propertyVoList = propertyVoMapper.listByNumOfAvailableDays(searchPropertyRequestDTO);

            // 將查詢到的房源列表存儲到 Redis 中
            savePropertyListToRedis(propertyVoList);
        }

        return  propertyVoList.stream()
                // 只保留已發布的房源資訊
                .filter(PropertyVo::getIsPublished)
                .map(propertyVo -> {
                    // 找尋房源圖片並排序
                    List<PropertyPictureVo> propertyPictureVoList = pictureService.listPropertyPictureInOrder(propertyVo.getPropertyId());

                    // 查詢房源圖片與 DEFAULT_SIZE 相應的圖片 DT，並取得下載圖片的預簽名 url
                    List<Map<String, Object>> propertyImageUrlList = pictureService.listPropertyPictureDtDownloadUrlMap(propertyPictureVoList, DEFAULT_PICTURE_DT_SIZE_NUM);

                    // 將預簽名 url 加入房源列表
                    return readInitialPagePropertyResponseMapper.apply(propertyVo, propertyImageUrlList);
                })
                .collect(Collectors.toList());
    }

    /**
     * 將房源列表存儲到 Redis 中。
     *
     * @param propertyVoList 要存儲的房源列表
     */

    private void savePropertyListToRedis(List<PropertyVo> propertyVoList) {
        // 轉換成 JSON 字串
        String propertyListJson = jsonConverter.convertToJson(propertyVoList);

        // 存儲到 Redis
        redisService.setStr("initialPagePropertyList", propertyListJson);
    }

    /**
     * 從 Redis 中取出房源列表並還原為 List<PropertyVo>。
     *
     * @return 從 Redis 中取出的房源列表  List<PropertyVo>
     */
    private List<PropertyVo> getPropertyListFromRedis() {
        // 從 Redis 中取出 JSON 字串
        String propertyListJson = redisService.getStr("initialPagePropertyList");;

        // 將 JSON 字串還原為 List<PropertyVo>
        return jsonConverter.convertJsonToObject(propertyListJson, new TypeReference<List<PropertyVo>>() {});
    }

}
