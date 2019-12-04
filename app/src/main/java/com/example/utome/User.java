package com.example.utome;

import java.util.ArrayList;

public class User{
    public String name;
    public ArrayList<Post> u_posts;
    public String post_ids;
    public int raiting;

    public User() {
    }
    public User(String name) {
        this.name = name;
    }
}
