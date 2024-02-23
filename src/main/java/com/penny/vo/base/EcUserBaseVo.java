package com.penny.vo.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EcUserBaseVo {
    private Long ecUserId;

    private String ecUserName;

    private String ecUserEmail;

    private String ecUserHashedPassword;

    private String ecUserIntroduction;
}