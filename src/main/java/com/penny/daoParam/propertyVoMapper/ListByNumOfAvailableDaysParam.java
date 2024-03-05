package com.penny.daoParam.propertyVoMapper;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ListByNumOfAvailableDaysParam {

    private Integer numOfAvailableDay;
    private List<String> returnFieldList;
    private List<String> sortFieldList;
    private List<String> sortOrderList;
    private Integer offset;
    private Integer limit;

}
