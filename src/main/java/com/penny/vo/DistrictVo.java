package com.penny.vo;

import com.penny.vo.base.DistrictBaseVo;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DistrictVo extends DistrictBaseVo {

    private String parentDistrictName;
}
