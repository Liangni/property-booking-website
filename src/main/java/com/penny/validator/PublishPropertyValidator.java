package com.penny.validator;

import com.penny.annotation.PublishPropertyCheck;
import com.penny.request.CreatePropertyRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PublishPropertyValidator implements ConstraintValidator<PublishPropertyCheck, CreatePropertyRequest> {

    /**
     * 驗證 `CreatePropertyRequest` 物件的有效性。
     *
     * @param createRequest 要進行驗證的屬性創建請求
     * @param context       約束驗證器上下文
     * @return 如果 `CreatePropertyRequest` 驗證通過，則返回 true；否則返回 false
     */
    @Override
    public boolean isValid(CreatePropertyRequest createRequest, ConstraintValidatorContext context) {
        // 如果 createRequest 為 null，則不進行驗證
        if (createRequest == null) {
            return true;
        }

        // 如果 isPublished 為 true，則特定屬性不應為 null
        if (createRequest.getIsPublished() != null && createRequest.getIsPublished()) {
            boolean requiredFieldIsNotNull = createRequest.getMaxNumOfGuests() != null
                    && createRequest.getNumOfBedrooms() != null
                    && createRequest.getNumOfBeds() != null
                    && createRequest.getPriceOnWeekdays() != null
                    && createRequest.getPriceOnWeekends() != null
                    && createRequest.getPropertyMainSubTypeId() != null
                    && createRequest.getPropertyShareTypeId() != null
                    && createRequest.getAddressId() != null;

            if (requiredFieldIsNotNull) return true;

            context.disableDefaultConstraintViolation();  // 禁用默認錯誤訊息
            context.buildConstraintViolationWithTemplate("When isPublished is set to true, the following fields are required :\nmaxNumOfGuests, numOfBedRooms, numOfBeds, priceOnWeekdays, priceOnWeekends, propertyMainSubTypeId, propertyShareTypeId and addressId")
                    .addConstraintViolation();  // 添加自定義錯誤訊息;

            return false;
        }

        return true;  // 如果 isPublished 為 false，則不進行額外驗證
    }
}
