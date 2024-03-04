package com.penny.vo;

import com.penny.vo.base.PropertyBaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PropertyVo extends PropertyBaseVo {
    private String apartmentFloor;

    private String street;

    private Integer adminAreaLevel1DistrictId;

    private Integer adminAreaLevel2DistrictId;

    private Integer adminAreaLevel3DistrictId;

    private String adminAreaLevel1DistrictName;

    private String adminAreaLevel2DistrictName;

    private String adminAreaLevel3DistrictName;

    private Date startAvailableDate;

    private Date endAvailableDate;
}
