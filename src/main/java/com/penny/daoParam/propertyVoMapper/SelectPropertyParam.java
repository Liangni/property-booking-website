package com.penny.daoParam.propertyVoMapper;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SelectPropertyParam {

    private Map<String, Object> filterMap;
    private List<String> returnFieldList;
    private Map<String, String> sortMap;
    private Integer offset;
    private Integer limit;

}
