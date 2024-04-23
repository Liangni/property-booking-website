package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PictureDtBaseVo {
    private Long pictureDtId;

    private Integer pictureDtSize;

    private String pictureDtStoragePath;

    private Boolean pictureDtIsUploaded;

    private Long pictureId;
}