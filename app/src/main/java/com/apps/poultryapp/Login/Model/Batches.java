package com.apps.poultryapp.Login.Model;

public class Batches {
    public int id;
    public String name;
    public String company;
    public String finalized;
    public String created_at;
    public String updated_at;

    public Batches(int id, String name, String company, String finalized, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.finalized = finalized;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


}
