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
        int offset = (page - 1) * limit;

        List<DistrictVo> results = districtVoMapper.selectByNameKeyword(keyword, offset, limit);
        if (results.isEmpty()) throw new ResourceNotFoundException("district with name [%s] not found".formatted(keyword));

        int totalResultCount = districtVoMapper.countSelectByNameKeyword(keyword);
        int totalPages = (int) Math.ceil((double) totalResultCount / limit);
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalResults", totalResultCount);
        pagination.put("currentPage", page);
        pagination.put("totalPages", totalPages);
        pagination.put("pageSize", limit);

        Map<String, Object> response = new HashMap<>();
        response.put("results", results);
        response.put("pagination", pagination);

        return response;
    }

}
