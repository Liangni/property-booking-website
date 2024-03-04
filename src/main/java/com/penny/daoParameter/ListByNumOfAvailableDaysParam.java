package com.penny.daoParameter;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
public class ListByNumOfAvailableDaysParam {
    @Getter
    public enum ReturnFieldEnum {
        PROPERTY_ID("propertyId"),
        PROPERTY_TITLE("propertyTitle"),
        PROPERTY_DESCRIPTION("propertyDescription"),
        NUM_OF_BEDROOMS("numOfBedrooms"),
        NUM_OF_BEDS("numOfBeds"),
        NUM_OF_BATHROOMS("numOfBathrooms"),
        PRICE_ON_WEEKDAYS("priceOnWeekdays"),
        PRICE_ON_WEEKENDS("priceOnWeekends"),
        IS_PUBLISHED("isPublished"),
        AVERAGE_RATING("averageRating"),
        PROPERTY_GROUP_TYPE_ID("propertyGroupTypeId"),
        PROPERTY_SHARE_TYPE_ID("propertyShareTypeId"),
        ADDRESS_ID("addressId"),
        ADDRESS("address"),
        DISTRICT("district");

        private final String label;

        ReturnFieldEnum(String label) {
            this.label = label;
        }

        public static ReturnFieldEnum valueOfLabel(String label) {
            for (ReturnFieldEnum field : ReturnFieldEnum.values()) {
                if (field.label.equals(label)) {
                    return field;
                }
            }
            return null;
        }
    }
    @Getter
    public enum SortFieldEnum {
        DISTRICT("district"),
        NEAREST_AVAILABLE_DAY("nearestAvailableDay");

        private final String label;

        SortFieldEnum(String label) {
            this.label = label;
        }

        public static SortFieldEnum valueOfLabel(String label) {
            for (SortFieldEnum field : SortFieldEnum.values()) {
                if (field.label.equals(label)) {
                    return field;
                }
            }
            return null;
        }

    }
    public enum SortOrderEnum {
        ASC("asc"),
        DESC("desc");

        private final String label;

        SortOrderEnum(String label) {
            this.label = label;
        }
    }

    @Data
    @Builder
    public class SortObject {
        private final SortFieldEnum field;
        private final SortOrderEnum order;
    }

    private Integer numOfAvailableDay;
    private List<String> returnFieldList; // 希望是[${ReturnFieldEnum}]
    private List<String> sortFieldList; // 希望是 [{ field: ${SortFieldEnum}, order: ${SortOrderEnum}}]
    private Integer offset;
    private Integer limit;

}
