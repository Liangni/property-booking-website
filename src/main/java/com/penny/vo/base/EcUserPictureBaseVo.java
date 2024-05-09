package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcUserPictureBaseVo {
    private Long ecUserPictureId;

    private Long ecUserId;

    private Long pictureId;
}