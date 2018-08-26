package com.apps.poultryapp.Login.Model;

public class Corrals {
    public int id;
    public String name;
    public String warehouse;
    public String age;
    public String company;
    public String created_at;
    public String updated_at;

    public Corrals(int id, String name, String warehouse, String age, String company, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.warehouse = warehouse;
        this.age = age;
        this.company = company;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
