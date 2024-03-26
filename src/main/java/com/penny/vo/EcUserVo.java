package com.penny.vo;

import com.penny.vo.base.EcUserBaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EcUserVo extends EcUserBaseVo {
    private List<String> roles;
}
