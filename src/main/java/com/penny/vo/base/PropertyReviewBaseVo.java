package com.penny.vo.base;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyReviewBaseVo {
    private Long propertyReviewId;

    private Integer propertyReviewCleanliness;

    private Integer propertyReviewAccuracy;

    private Integer propertyReviewCheckin;

    private Integer propertyReviewCommunication;

    private Integer propertyReviewLocation;

    private Integer propertyReviewValue;

    private String propertyReviewComment;

    private LocalDateTime propertyReviewCreatedAt;

    private Long propertyId;

    private Long customerId;
}