package com.food.loginsignupapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    String name;
    String age;
    String email;
    String password;

    public User(){}

    public User(String name, String age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }
}
