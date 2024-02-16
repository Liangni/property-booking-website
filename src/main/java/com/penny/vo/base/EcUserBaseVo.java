package com.penny.vo.base;

public class EcUserBaseVo {
    private Long userId;

    private String name;

    private String email;

    private String password;

    private String city;

    private Long countryId;

    public EcUserBaseVo(Long userId, String name, String email, String password, String city, Long countryId) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.city = city;
        this.countryId = countryId;
    }

    public EcUserBaseVo() {
        super();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }
}