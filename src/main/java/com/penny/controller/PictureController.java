package com.penny.controller;

import com.penny.request.picture.GetPropertyImageUploadUrlRequest;
import com.penny.request.picture.UpdatePropertyPictureRequest;
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
    @PostMapping("property-image-upload-urls")
    public ResponseEntity<Map<String, Object>> getPropertyImageUploadUrlMap(
            @RequestBody GetPropertyImageUploadUrlRequest request
    ) {
        return ResponseEntity.ok(pictureService.getPropertyImageUploadUrlMap(request));
    }

    /**
     * 更新房源圖片信息。
     *
     * @param updateRequest 更新圖片請求。
     * @return ResponseEntity，表示請求已成功處理。
     */
    @PutMapping("property-pictures")
    public ResponseEntity<String> updatePropertyPicture(
            @RequestBody UpdatePropertyPictureRequest updateRequest
            ) {
        pictureService.updatePropertyPicture(updateRequest);
        return ResponseEntity.ok("ok");
    }
}
