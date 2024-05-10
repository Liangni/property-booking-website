package com.penny.vo;

import com.penny.vo.base.AddressBaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AddressVo extends AddressBaseVo {
    private String districtName;
}
