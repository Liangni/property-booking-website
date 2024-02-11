package com.penny.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EcUserVo {
    // 會員編號
    private Long userId;
    // 會員名稱
    private String name;
    // 會員信箱
    private String email;
    // 會員密碼
    private String password;
    // 會員城市
    private String city;
    // 國家編號
    private Long countryId;
}
