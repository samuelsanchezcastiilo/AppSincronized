package com.apps.poultryapp.Login.Model;

public class WareHouse {
    public int id;
    public String name;
    public String batch;
    public String company;
    public String created_at;
    public String updated_at;

    public WareHouse(int id, String name, String batch, String company, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.batch = batch;
        this.company = company;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
