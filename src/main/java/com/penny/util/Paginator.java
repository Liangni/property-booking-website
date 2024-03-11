package com.penny.util;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Paginator {

    /**
     * 根據頁碼和每頁數量計算分頁查詢時的偏移量。
     *
     * @param page  目標頁碼。
     * @param limit 每頁數量。
     * @return 分頁查詢時的偏移量。
     */
    public int calculateOffset(int page, int limit) {
        return (page - 1) * limit;
    }

    /**
     * 根據總記錄數和每頁數量計算總頁數。
     *
     * @param totalResultCount 總記錄數。
     * @param limit            每頁數量。
     * @return 總頁數。
     */
    public int calculateTotalPages(int totalResultCount, int limit) {
        return (int) Math.ceil((double) totalResultCount / limit);
    }
    public int calculateTotalPages(long totalResultCount, int limit) {
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
    public Map<String, Object> buildPaginationMap(int totalResultCount, int page, int totalPages, int limit) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalResults", totalResultCount);
        pagination.put("currentPage", page);
        pagination.put("totalPages", totalPages);
        pagination.put("pageSize", limit);
        return pagination;
    }
    public Map<String, Object> buildPaginationMap(long totalResultCount, long page, long totalPages, int limit) {
        Map<String, Object> pagination = new HashMap<>();
        pagination.put("totalResults", totalResultCount);
        pagination.put("currentPage", page);
        pagination.put("totalPages", totalPages);
        pagination.put("pageSize", limit);
        return pagination;
    }
}
