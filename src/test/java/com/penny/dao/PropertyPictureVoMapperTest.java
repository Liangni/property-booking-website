package com.penny.dao;

import com.penny.dao.base.*;
import com.penny.vo.PropertyPictureVo;
import com.penny.vo.base.*;
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
@DisplayName("PropertyPicture資料表測試")
public class PropertyPictureVoMapperTest {
    @Autowired
    private PropertyPictureVoMapper propertyPictureVoMapper;
    @Autowired
    private PropertyPictureBaseVoMapper propertyPictureBaseVoMapper;
    @Autowired
    private PictureBaseVoMapper pictureBaseVoMapper;
    @Autowired
    private PropertyBaseVoMapper propertyBaseVoMapper;
    @Autowired
    private DistrictBaseVoMapper districtBaseVoMapper;
    @Autowired
    private AdministrativeAreaBaseVoMapper administrativeAreaBaseVoMapper;
    @Autowired
    private EcUserBaseVoMapper ecUserBaseVoMapper;
    @Autowired
    private AddressBaseVoMapper addressBaseVoMapper;

    List<Long> propertyIdList = new ArrayList<>();

    @BeforeEach
    void setup(){
        // 新增行政區劃分層級資料
        AdministrativeAreaBaseVo administrativeArea101 = AdministrativeAreaBaseVo.builder()
                .administrativeAreaName("test admin area 101")
                .administrativeAreaLevel(101L)
                .build();
        administrativeAreaBaseVoMapper.insertSelective(administrativeArea101);
        long administrativeArea101Id =(long) administrativeArea101.getAdministrativeAreaId();  // 獲取新行政區劃分層級資料 id

        // 新增地區資料
        DistrictBaseVo district1 = DistrictBaseVo.builder()
                .districtName("test district 1")
                .administrativeAreaId(administrativeArea101Id) // 帶入行政區劃分層級資料 id
                .build();

        districtBaseVoMapper.insertSelective(district1);
        long district1Id = (long) district1.getDistrictId();  // 獲取地區資料 id

        // 新增地址資料
        AddressBaseVo address1 = AddressBaseVo.builder()
                .street("test street 1")
                .adminAreaLevel1DistrictId(district1Id) // 帶入地區資料 id
                .build();
        addressBaseVoMapper.insertSelective(address1);
        long address1Id = (long) address1.getAddressId(); // 獲取地址資料 id

        // 新增使用者資料
        EcUserBaseVo ecUser = EcUserBaseVo.builder()
                .ecUserName("test user")
                .ecUserEmail("test@email.com")
                .ecUserHashedPassword("1234567")
                .build();
        ecUserBaseVoMapper.insertSelective(ecUser);
        long ecUserId = (long) ecUser.getEcUserId(); // 獲取使用者資料 id

        // 新增房源資料
        for(int i=1; i < 4; i++) {
            PropertyBaseVo property = PropertyBaseVo.builder()
                    .propertyTitle("Property " + i)
                    .addressId(address1Id) // 帶入地址資料 id
                    .hostId(ecUserId) // 帶入使用者資料 id
                    .build();
            propertyBaseVoMapper.insertSelective(property);
            propertyIdList.add(property.getPropertyId());
        }

        // 新增 picture、propertyPicture 資料
        List<Long> pictureIdList = new ArrayList<>();
        for (int i = 1; i < 4; i ++) {
            // 新增 picture
            String picUrl = "www.fake.image"+ i +".com";
            PictureBaseVo picture = PictureBaseVo
                    .builder()
                    .pictureUrl(picUrl)
                    .build();
            pictureBaseVoMapper.insertSelective(picture);
            long picId = picture.getPictureId();

            // 新增 property_picture
            PropertyPictureBaseVo propertyPic = PropertyPictureBaseVo
                    .builder()
                    .propertyId(propertyIdList.get(i - 1))
                    .pictureId(picId)
                    .propertyPictureOrder(1L)
                    .build();
            propertyPictureBaseVoMapper.insertSelective(propertyPic);
        }
    }

    @Test
    @DisplayName("用 propertyId 列表搜尋房源圖片")
    void ListByPropertyIdListTest() {
        List<PropertyPictureVo> propertyPicList = propertyPictureVoMapper.ListByPropertyIdList(propertyIdList);
        System.out.println(propertyPicList);
    }
}
