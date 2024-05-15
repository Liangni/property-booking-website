package com.penny.controller;

import com.penny.exception.AuthorizationException;
import com.penny.request.CreateEcUserPictureRequest;
import com.penny.response.ReadEcUserPropertyResponse;
import com.penny.response.ReadEcUserResponse;
import com.penny.service.*;
import com.penny.vo.BookingOrderVo;
import com.penny.vo.PropertyVo;
import com.penny.vo.WishPropertyVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/ec-users")
@RequiredArgsConstructor
public class EcUserController {
    private final EcUserService ecUserService;

    private final WishPropertyService wishPropertyService;

    private final BookingOrderService bookingOrderService;

    private final PropertyService propertyService;

    private final PictureService pictureService;

    /**
     * 根據使用者 ID 獲取使用者資訊。
     *
     * @param ecUserId 使用者的 ID
     * @return ResponseEntity 包含使用者資訊的 ResponseEntity
     */
    @GetMapping("{ecUserId}")
    public ResponseEntity<ReadEcUserResponse> getEcUser(
            @PathVariable Long ecUserId
    ) {
        return ResponseEntity.ok(ecUserService.getEcUser(ecUserId));
    }

    /**
     * 根據使用者 ID 列出願望房源列表。
     *
     * @param ecUserId 使用者 ID
     * @return ResponseEntity 包含願望房源列表的 ResponseEntity
     */
    @GetMapping("{ecUserId}/wish-properties")
    public ResponseEntity<List<WishPropertyVo>> listEcUserWishProperty(
            @PathVariable Long ecUserId
    ) {
        return ResponseEntity.ok(wishPropertyService.listWishPropertyByEcUserId(ecUserId));
    }

    /**
     * 根據指定的 ecUserId 和 isHost 參數，獲取相應的預訂訂單列表。
     *
     * @param ecUserId 使用者 ID，必須提供以查詢相關的預訂訂單。
     * @param isHost 是否為出租人（可選）。默認值為 false，表示查詢租客的訂單；如果設置為 true，則查詢出租人的訂單。
     *
     * @return ResponseEntity 包含預訂訂單列表的 ResponseEntity。
     */
    @GetMapping("{ecUserId}/booking-orders")
    public ResponseEntity<List<BookingOrderVo>> listEcUserBookingOrders(
            @PathVariable Long ecUserId,
            @RequestParam(defaultValue = "false") Boolean isHost
    ) {
        return ResponseEntity.ok(bookingOrderService.getBookingOrders(ecUserId, isHost));
    }

    /**
     * 根據使用者 ID 列出房源列表。
     *
     * @param ecUserId 使用者 ID
     * @return ResponseEntity 包含房源列表的 ResponseEntity
     */
    @GetMapping("{ecUserId}/properties")
    public ResponseEntity<List<ReadEcUserPropertyResponse>> listEcUserProperties(
            @PathVariable Long ecUserId
    ) {
        return ResponseEntity.ok(propertyService.listPropertyByEcUserId(ecUserId));
    }

    /**
     * 根據使用者 ID 列出使用者圖片上傳 URL。
     *
     * @param ecUserId 使用者的 ID
     * @param fileExtension 檔案擴展名
     * @return 返回包含圖片上傳 URL 的 ResponseEntity
     */
    @GetMapping("{ecUserId}/picture-upload-urls")
    public ResponseEntity<Map<String, Object>> listEcUserPictureUploadUrl(
            @PathVariable Long ecUserId,
            @RequestParam String fileExtension
    ) {
        return ResponseEntity.ok(pictureService.listEcUserPictureUploadUrl(ecUserId, fileExtension));
    }

    /**
     * 創建使用者圖片。
     *
     * @param ecUserId 使用者的 ID
     * @param createRequest 創建使用者圖片的請求
     * @return 返回操作結果為成功的 ResponseEntity，返回 "ok" 字符串
     */
    @PostMapping("{ecUserId}/ec-user-pictures")
    public ResponseEntity<String> createEcUserPicture(
            @PathVariable Long ecUserId,
            @RequestBody CreateEcUserPictureRequest createRequest
    ) {
        pictureService.createEcUserPicture(ecUserId, createRequest);
        return ResponseEntity.ok("ok");
    }

    /**
     * 根據使用者 ID 獲取使用者圖片下載 URL。
     *
     * @param ecUserId 使用者的 ID
     * @return 返回包含圖片下載 URL Map 的 ResponseEntity
     */
    @GetMapping("{ecUserId}/picture-download-urls")
    public ResponseEntity<Map<String, Object>> getEcUserPictureDownloadUrl(
            @PathVariable Long ecUserId
    ) {

        return ResponseEntity.ok(pictureService.getEcUserPictureDownloadUrl(ecUserId));
    }
}
