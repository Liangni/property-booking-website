package com.penny.controller;

import com.penny.request.CreateWishPropertyRequest;
import com.penny.service.WishPropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/wish-properties")
@RequiredArgsConstructor
public class WishPropertyController {
    private final WishPropertyService wishPropertyService;

    @PostMapping
    public ResponseEntity<String> createWishProperty(
            @RequestBody @Valid CreateWishPropertyRequest createRequest
    ) {
        wishPropertyService.createWishProperty(createRequest);
        return ResponseEntity.ok("ok");
    }
}
