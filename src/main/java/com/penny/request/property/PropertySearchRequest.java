package com.penny.request.property;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PropertySearchRequest {
    private Map<String, Object> filterMap;
    private List<String> returnFieldList;
    private Map<String, String> sortMap;
    private Integer page;
    private Integer limit;
}
