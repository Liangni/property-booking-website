package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.vo.DistrictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DistrictService {
    private final DistrictVoMapper districtVoMapper;

    @Autowired
    public DistrictService(DistrictVoMapper districtVoMapper){
        this.districtVoMapper = districtVoMapper;
    }

    public Map<String, Object> getDistrictsByKeyword(String keyword, int page, int limit){
        String replaced = keyword.replace("台", "臺");
        int offset = calculateOffset(page, limit);

        List<DistrictVo> fetchResults = districtVoMapper.selectByNameKeyword(replaced, offset, limit);
        if (fetchResults.isEmpty()) {
            throw new ResourceNotFoundException("district with name [" + keyword + "] not found");
        }

        int totalResultCount = districtVoMapper.countSelectByNameKeyword(keyword);
        int totalPages = calculateTotalPages(totalResultCount, limit);

        Map<String, Object> pagination = buildPaginationMap(totalResultCount, page, totalPages, limit);

        return buildResponseMap(fetchResults, pagination);
    }

    private int calculateOffset(int page, int limit) {
        return (page - 1) * limit;
    }

    private int calculateTotalPages(int totalResultCount, int limit) {
        return (int) Math.ceil((double) totalResultCount / limit);
    }

    private Map<String, Object> buildPaginationMap(int totalResultCount, int page, int totalPages, int limit) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalResults", totalResultCount);
        pagination.put("currentPage", page);
        pagination.put("totalPages", totalPages);
        pagination.put("pageSize", limit);
        return pagination;
    }

    private Map<String, Object> buildResponseMap(List<DistrictVo> results, Map<String, Object> pagination) {
        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("pagination", pagination);
        return response;
    }

}
