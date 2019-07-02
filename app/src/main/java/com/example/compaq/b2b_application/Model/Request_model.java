package com.example.compaq.b2b_application.Model;

public class Request_model  {
  public  String companyname,id,status,logo,settings;
    public Request_model(String company_name, String id, String status, String logo,String settings) {
        this.companyname=company_name;
        this.id=id;
        this.status=status;
        this.logo=logo;
        this.settings=settings;

    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }
}
