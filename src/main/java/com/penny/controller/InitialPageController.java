package com.penny.controller;

import com.penny.service.InitialPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/v1/initial-page")
@RequiredArgsConstructor
public class InitialPageController {
    private final InitialPageService initialPageService;

    /**
     * 獲取初始頁面資料。
     *
     * @return 包含初始頁面資料的 Map
     */
    @GetMapping("init")
    public Map<String, Object> getInitialPage(
    ) {
        return initialPageService.getInitialPageData();
    }
}
