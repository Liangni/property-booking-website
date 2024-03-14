package com.penny.vo;

import com.penny.vo.base.PropertyBaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PropertyVo extends PropertyBaseVo {
    private String apartmentFloor;

    private String street;

    private Integer adminAreaLevel3DistrictId;

    private String adminAreaLevel3DistrictName;

    private Long districtId;

    private Long parentDistrictId;

    private String districtName;

    private String parentDistrictName;

    private Date startAvailableDate;

    private Date endAvailableDate;

    private List<PictureDtVo> pictureDtVoList;

    private Long reviewCount;
}
