package com.android.traveljournalapp;

public class User {

    public String username, email;

    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String email)  {
        this.email = email;
    }
}
