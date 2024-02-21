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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EcUserBaseVo other = (EcUserBaseVo) that;
        return (this.getEcUserId() == null ? other.getEcUserId() == null : this.getEcUserId().equals(other.getEcUserId()))
            && (this.getEcUserName() == null ? other.getEcUserName() == null : this.getEcUserName().equals(other.getEcUserName()))
            && (this.getEmail() == null ? other.getEmail() == null : this.getEmail().equals(other.getEmail()))
            && (this.getHashedPassword() == null ? other.getHashedPassword() == null : this.getHashedPassword().equals(other.getHashedPassword()))
            && (this.getEcUserIntroduction() == null ? other.getEcUserIntroduction() == null : this.getEcUserIntroduction().equals(other.getEcUserIntroduction()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEcUserId() == null) ? 0 : getEcUserId().hashCode());
        result = prime * result + ((getEcUserName() == null) ? 0 : getEcUserName().hashCode());
        result = prime * result + ((getEmail() == null) ? 0 : getEmail().hashCode());
        result = prime * result + ((getHashedPassword() == null) ? 0 : getHashedPassword().hashCode());
        result = prime * result + ((getEcUserIntroduction() == null) ? 0 : getEcUserIntroduction().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ecUserId=").append(ecUserId);
        sb.append(", ecUserName=").append(ecUserName);
        sb.append(", email=").append(email);
        sb.append(", hashedPassword=").append(hashedPassword);
        sb.append(", ecUserIntroduction=").append(ecUserIntroduction);
        sb.append("]");
        return sb.toString();
    }
}