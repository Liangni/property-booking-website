package com.penny.controller;

import com.penny.request.picture.PropertyImageUploadRequest;
import com.penny.request.picture.PropertyImageUploadStatusUpdateRequest;
import com.penny.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/pictures")
@RequiredArgsConstructor
public class PictureController {
    private final PictureService pictureService;

    /**
     * 獲取房源圖片的上傳URL。
     *
     * @param request 上傳圖片請求
     * @return 包含圖片上傳 URL 的 Map 的 ResponseEntity
     */
    @PostMapping("upload-property-image-urls")
    public ResponseEntity<Map<String, Object>> getPropertyImageUploadUrlMap(
            @RequestBody PropertyImageUploadRequest request
    ) {
        return ResponseEntity.ok(pictureService.getPropertyImageUploadUrlMap(request));
    }
}
