package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.district.DistrictSearchRequest;
import com.penny.vo.DistrictVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DistrictService {
    private static final int DEFAULT_PAGE = 1;

    private static final int DEFAULT_LIMIT = 10;

    private final DistrictVoMapper districtVoMapper;

    @Autowired
    public DistrictService(DistrictVoMapper districtVoMapper){
        this.districtVoMapper = districtVoMapper;
    }

    public Map<String, Object> getDistrictsByKeyword(DistrictSearchRequest districtSearchRequest){
        Map<String, Object> resultMap = new HashMap<>();
        String keyword = districtSearchRequest.getKeyword();
        int page = Optional.ofNullable(districtSearchRequest.getPage()).orElse(DEFAULT_PAGE);
        int limit = Optional.ofNullable(districtSearchRequest.getLimit()).orElse(DEFAULT_LIMIT);

        String replaced = keyword.replace("台", "臺");

        int offset = calculateOffset(page, limit);

        List<DistrictVo> fetchResults = Optional.ofNullable(districtVoMapper.selectByNameKeyword(replaced, offset, limit))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("district with name [%s] not found", keyword)));

        int totalResultCount = districtVoMapper.countSelectByNameKeyword(replaced);
        int totalPages = calculateTotalPages(totalResultCount, limit);

        Map<String, Object> pagination = buildPaginationMap(totalResultCount, page, totalPages, limit);

        resultMap.put("results", fetchResults);
        resultMap.put("pagination", pagination);
        return resultMap;
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

}
