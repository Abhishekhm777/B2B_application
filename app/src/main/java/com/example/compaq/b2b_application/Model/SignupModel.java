package com.example.compaq.b2b_application.Model;

public class SignupModel {
    String logoId,company,gst,person,email,mobile,password;

    public SignupModel(String logoId,String company, String gst, String person, String email, String mobile, String password) {
        this.company = company;
        this.gst = gst;
        this.person = person;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.logoId=logoId;
    }

    public String getLogoId() {
        return logoId;
    }

    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
