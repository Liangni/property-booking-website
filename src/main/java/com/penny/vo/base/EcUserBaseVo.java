package com.penny.vo.base;

public class EcUserBaseVo {
    private Long ecUserId;

    private String ecUserName;

    private String ecUserEmail;

    private String ecUserHashedPassword;

    private String ecUserIntroduction;

    public EcUserBaseVo(Long ecUserId, String ecUserName, String ecUserEmail, String ecUserHashedPassword, String ecUserIntroduction) {
        this.ecUserId = ecUserId;
        this.ecUserName = ecUserName;
        this.ecUserEmail = ecUserEmail;
        this.ecUserHashedPassword = ecUserHashedPassword;
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

    public String getEcUserEmail() {
        return ecUserEmail;
    }

    public void setEcUserEmail(String ecUserEmail) {
        this.ecUserEmail = ecUserEmail;
    }

    public String getEcUserHashedPassword() {
        return ecUserHashedPassword;
    }

    public void setEcUserHashedPassword(String ecUserHashedPassword) {
        this.ecUserHashedPassword = ecUserHashedPassword;
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
            && (this.getEcUserEmail() == null ? other.getEcUserEmail() == null : this.getEcUserEmail().equals(other.getEcUserEmail()))
            && (this.getEcUserHashedPassword() == null ? other.getEcUserHashedPassword() == null : this.getEcUserHashedPassword().equals(other.getEcUserHashedPassword()))
            && (this.getEcUserIntroduction() == null ? other.getEcUserIntroduction() == null : this.getEcUserIntroduction().equals(other.getEcUserIntroduction()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEcUserId() == null) ? 0 : getEcUserId().hashCode());
        result = prime * result + ((getEcUserName() == null) ? 0 : getEcUserName().hashCode());
        result = prime * result + ((getEcUserEmail() == null) ? 0 : getEcUserEmail().hashCode());
        result = prime * result + ((getEcUserHashedPassword() == null) ? 0 : getEcUserHashedPassword().hashCode());
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
        sb.append(", ecUserEmail=").append(ecUserEmail);
        sb.append(", ecUserHashedPassword=").append(ecUserHashedPassword);
        sb.append(", ecUserIntroduction=").append(ecUserIntroduction);
        sb.append("]");
        return sb.toString();
    }
}