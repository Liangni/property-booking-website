package com.penny.dao;

import com.penny.dao.base.*;
import com.penny.vo.PictureDtVo;
import com.penny.vo.PictureVo;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
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
    @Autowired
    EcUserBaseVoMapper ecUserBaseVoMapper;
    @Autowired
    AddressBaseVoMapper addressBaseVoMapper;
    @Autowired
    DistrictBaseVoMapper districtBaseVoMapper;
    @Autowired
    AdministrativeAreaBaseVoMapper administrativeAreaBaseVoMapper;
    @Autowired
    PropertyBaseVoMapper propertyBaseVoMapper;
    @Autowired
    PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;

    List<Long> pictureIdList = new ArrayList<>();
    List<String> sizeList = List.of("large", "medium", "small");

    long propertyId;

    int numOfPictureToInsert = 3;

    /**
     * 設置測試環境，創建並插入圖片及其詳細資訊
     */
    @BeforeEach
    void setup() {
        // 循環創建三個圖片
        for (int i = 1; i <= numOfPictureToInsert; i++) {
            // 創建圖片並插入到資料庫中
            PictureBaseVo picture = PictureBaseVo.builder().pictureUrl("www.test.image" + i + ".com").build();
            pictureBaseVoMapper.insertSelective(picture);
            long pictureId = picture.getPictureId();
            pictureIdList.add(picture.getPictureId()); // 將圖片ID添加到列表中

            // 創建圖片詳細資訊並插入到資料庫中
            PictureDtBaseVo pictureDt = null;
            for (String size : sizeList) {
                pictureDt = PictureDtBaseVo.builder().pictureId(pictureId).pictureDtUrl("www.test.image" + i + "." + size + ".com").pictureDtSize(size).build();

                pictureDtBaseVoMapper.insertSelective(pictureDt);
            }
        }
    }

    @Test
    @DisplayName("用 pictureId 列表搜尋房源圖片詳細資訊")
    void listByPictureIdList() {
        // 期望的圖片ID列表，取前2個圖片ID
        List<Long> expectPictureIdList = pictureIdList.subList(0, 2);

        // 根據期望的圖片ID列表搜索PictureDt
        List<PictureDtVo> returnPictureDtList = pictureDtVoMapper.listByPictureIdList(expectPictureIdList);

        // 斷言返回的PictureDt列表不為空
        Assertions.assertFalse(returnPictureDtList.isEmpty());

        // 將返回的PictureDt列表轉換為圖片ID列表
        List<Long> returnPictureIdList = returnPictureDtList.stream().map(PictureDtVo::getPictureId).toList();

        // 斷言返回的圖片ID列表包含所有期望的圖片ID列表
        Assertions.assertTrue(returnPictureIdList.containsAll(expectPictureIdList));

        // 斷言返回的圖片ID列表不包含指定的圖片ID（pictureIdList.get(2)）
        Assertions.assertFalse(returnPictureIdList.contains(pictureIdList.get(2)));
    }

    @Test
    @DisplayName("用 propertyId 搜尋房源圖片詳細資訊")
    void listByPropertyIdTest() {
        // 在測試執行前設置測試環境
        // 新增使用者資料
        EcUserBaseVo ecUser = EcUserBaseVo.builder().ecUserName("test user").ecUserEmail("test@email.com").ecUserHashedPassword("1234567").build();
        ecUserBaseVoMapper.insertSelective(ecUser);
        long ecUserId = (long) ecUser.getEcUserId(); // 獲取使用者資料 id

        // 新增行政區劃分層級資料
        AdministrativeAreaBaseVo administrativeArea101 = AdministrativeAreaBaseVo.builder().administrativeAreaName("test admin area 101").administrativeAreaLevel(101L).build();
        administrativeAreaBaseVoMapper.insertSelective(administrativeArea101);
        long administrativeArea101Id = (long) administrativeArea101.getAdministrativeAreaId();  // 獲取新行政區劃分層級資料 id

        // 新增地區資料
        DistrictBaseVo district1 = DistrictBaseVo.builder().districtName("test district 1").administrativeAreaId(administrativeArea101Id) // 帶入行政區劃分層級資料 id
                .build();

        districtBaseVoMapper.insertSelective(district1);
        long district1Id = (long) district1.getDistrictId();  // 獲取地區資料 id

        // 新增地址資料
        AddressBaseVo address1 = AddressBaseVo.builder().street("test street 1").adminAreaLevel1DistrictId(district1Id) // 帶入地區資料 id
                .build();
        addressBaseVoMapper.insertSelective(address1);
        long address1Id = (long) address1.getAddressId(); // 獲取地址資料 id

        // 新增房源資料
        PropertyBaseVo property = PropertyBaseVo.builder().propertyTitle("test property").addressId(address1Id) // 帶入地址資料 id
                .hostId(ecUserId) // 帶入使用者資料 id
                .build();
        propertyBaseVoMapper.insertSelective(property);
        propertyId = property.getPropertyId();

        // 在房源圖片表(property_picture) 將三張圖片與一個房源連結
        for (long pictureId : pictureIdList) {
            PropertyPictureBaseVo propertyPicture = PropertyPictureBaseVo.builder().propertyId(propertyId).pictureId(pictureId).propertyPictureOrder(1L).build();

            propertyPictureBaseVoMapper.insertSelective(propertyPicture);
        }

        // 進入測試
        // 從尺寸列表中取得第一個尺寸
        String size = sizeList.get(0);
        int offset = 0;
        int limit = numOfPictureToInsert + 1; // 確保取回的圖片詳細資訊列表數量大於插入的圖片數量
        // 根據房源ID和尺寸查詢圖片詳細資訊列表
        List<PictureDtVo> pictureDtList = pictureDtVoMapper.listByPropertyId(propertyId, size, offset, limit);

        // 斷言圖片詳細資訊列表不為空
        Assertions.assertFalse(pictureDtList.isEmpty());
        // 斷言圖片詳細資訊列表大小與預期新增圖片數量相等
        Assertions.assertEquals(pictureDtList.size(), numOfPictureToInsert);
        // 遍歷圖片詳細資訊列表中的每個圖片詳細資訊
        for (PictureDtVo pictureDt : pictureDtList) {
            // 斷言每個圖片詳細資訊的尺寸與指定尺寸相等
            Assertions.assertEquals(pictureDt.getPictureDtSize(), size);
        }
    }

}
