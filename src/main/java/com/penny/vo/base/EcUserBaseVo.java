package com.penny.vo.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcUserBaseVo {
    private Long ecUserId;

    private String ecUserName;

    private String ecUserEmail;

    private String ecUserHashedPassword;

    private String ecUserIntroduction;
}