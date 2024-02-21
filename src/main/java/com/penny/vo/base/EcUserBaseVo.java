package com.penny.vo.base;

public class EcUserBaseVo {
    private Long ecUserId;

    private String ecUserName;

    private String email;

    private String hashedPassword;

    private String ecUserIntroduction;

    public EcUserBaseVo(Long ecUserId, String ecUserName, String email, String hashedPassword, String ecUserIntroduction) {
        this.ecUserId = ecUserId;
        this.ecUserName = ecUserName;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.ecUserIntroduction = ecUserIntroduction;
    }

    public EcUserBaseVo() {
        super();
    }

    public Long getEcUserId() {
        return ecUserId;
    }

    public void setEcUserId(Long ecUserId) {
        this.ecUserId = ecUserId;
    }

    public String getEcUserName() {
        return ecUserName;
    }

    public void setEcUserName(String ecUserName) {
        this.ecUserName = ecUserName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getEcUserIntroduction() {
        return ecUserIntroduction;
    }

    public void setEcUserIntroduction(String ecUserIntroduction) {
        this.ecUserIntroduction = ecUserIntroduction;
    }
}