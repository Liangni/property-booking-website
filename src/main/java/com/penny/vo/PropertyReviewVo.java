package com.penny.vo;

import com.penny.vo.base.PropertyReviewBaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class PropertyReviewVo extends PropertyReviewBaseVo {
    private Double propertyAverageRating;
}
