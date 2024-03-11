package com.penny.service;



import com.penny.dao.DistrictVoMapper;
import com.penny.exception.ResourceNotFoundException;
import com.penny.request.district.DistrictSearchRequest;
import com.penny.util.Paginator;
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

    private final Paginator paginator;

    @Autowired
    public DistrictService(DistrictVoMapper districtVoMapper, Paginator paginator){
        this.districtVoMapper = districtVoMapper;
        this.paginator = paginator;
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

        int offset = paginator.calculateOffset(page, limit);

        List<DistrictVo> fetchResults = Optional.ofNullable(districtVoMapper.listByNameKeyword(replaced, offset, limit))
                .orElseThrow(() -> new ResourceNotFoundException(String.format("district with name [%s] not found", keyword)));

        int totalResultCount = districtVoMapper.countByNameKeyword(replaced);
        int totalPages = paginator.calculateTotalPages(totalResultCount, limit);

        Map<String, Object> pagination = paginator.buildPaginationMap(totalResultCount, page, totalPages, limit);

        resultMap.put("result", fetchResults);
        resultMap.put("pagination", pagination);
        return resultMap;
    }

}
