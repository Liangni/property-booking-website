package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PictureBaseVo {
    private Long pictureId;

    private String pictureStoragePath;

    private Boolean pictureIsUploaded;
}