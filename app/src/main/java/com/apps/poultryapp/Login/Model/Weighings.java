package com.apps.poultryapp.Login.Model;

public class Weighings {

    public String id;
    public String name;
    public String brids;
    public String age;
    public String company;
    public String corral;
    public String created_at;
    public String updated_at;

    public Weighings(String id, String name, String brids, String age, String company, String corral, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.brids = brids;
        this.age = age;
        this.company = company;
        this.corral = corral;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
