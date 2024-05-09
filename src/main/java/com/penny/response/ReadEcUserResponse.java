package com.penny.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReadEcUserResponse {

    private String ecUserName;

    private String ecUserUsername;

    private String ecUserIntroduction;
}
