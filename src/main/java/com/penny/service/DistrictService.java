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

    /**
     * 根據提供的行政區搜尋請求參數，返回符合條件的行政區資訊。
     *
     * @param districtSearchRequest 行政區搜尋請求物件，包含搜尋關鍵字、分頁等資訊。
     * @return 包含符合條件的行政區資訊和分頁資訊的 map。
     * @throws ResourceNotFoundException 如果根據關鍵字未找到相應的行政區資源時拋出。
     */
    public Map<String, Object> getDistrictsByKeyword(DistrictSearchRequest districtSearchRequest){
        Map<String, Object> resultMap = new HashMap<>();
        String keyword = districtSearchRequest.getKeyword();
        int page = Optional.ofNullable(districtSearchRequest.getPage()).orElse(DEFAULT_PAGE);
        int limit = Optional.ofNullable(districtSearchRequest.getLimit()).orElse(DEFAULT_LIMIT);

        String replaced = keyword.replace("台", "臺");

        int offset = calculateOffset(page, limit);

        List<DistrictVo> fetchResults = Optional.ofNullable(districtVoMapper.listByNameKeyword(replaced, offset, limit))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("district with name [%s] not found", keyword)));

        int totalResultCount = districtVoMapper.countByNameKeyword(replaced);
        int totalPages = calculateTotalPages(totalResultCount, limit);

        Map<String, Object> pagination = buildPaginationMap(totalResultCount, page, totalPages, limit);

        resultMap.put("results", fetchResults);
        resultMap.put("pagination", pagination);
        return resultMap;
    }

    /**
     * 根據頁碼和每頁數量計算分頁查詢時的偏移量。
     *
     * @param page  目標頁碼。
     * @param limit 每頁數量。
     * @return 分頁查詢時的偏移量。
     */
    private int calculateOffset(int page, int limit) {
        return (page - 1) * limit;
    }

    /**
     * 根據總記錄數和每頁數量計算總頁數。
     *
     * @param totalResultCount 總記錄數。
     * @param limit            每頁數量。
     * @return 總頁數。
     */
    private int calculateTotalPages(int totalResultCount, int limit) {
        return (int) Math.ceil((double) totalResultCount / limit);
    }

    /**
     * 構建分頁資訊的 map。
     *
     * @param totalResultCount 總記錄數。
     * @param page             目前頁碼。
     * @param totalPages       總頁數。
     * @param limit            每頁數量。
     * @return 紀錄分頁資訊的map。
     */
    private Map<String, Object> buildPaginationMap(int totalResultCount, int page, int totalPages, int limit) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalResults", totalResultCount);
        pagination.put("currentPage", page);
        pagination.put("totalPages", totalPages);
        pagination.put("pageSize", limit);
        return pagination;
    }

}
