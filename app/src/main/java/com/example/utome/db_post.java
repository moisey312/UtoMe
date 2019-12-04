package com.example.utome;

public class db_post{
    public String deliverer_uId;
    public int raiting;
    public int status;
    public String uId;
    public int id;
    public String address;
    public String category;
    public float distance;
    public String descriprion;
    public String name;
    public int pay;
    public int price;

    public db_post() {
    }

    public db_post(String descriprion,String name) {
        this.name = name;
        this.descriprion = descriprion;
    }

    public db_post(String address,String category,int distance,String descriprion,String name,int pay,int price) {
        this.address = address;
        this.name = name;
        this.pay = pay;
        this.category = category;
        this.descriprion = descriprion;
        this.distance = distance;
        this.price = price;
    }
}
