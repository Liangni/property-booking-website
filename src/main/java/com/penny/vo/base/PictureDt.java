package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PictureDt {
    private Long pictureDtId;

    private String pictureDtUrl;

    private String pictureDtSize;

    private Long pictureId;
}