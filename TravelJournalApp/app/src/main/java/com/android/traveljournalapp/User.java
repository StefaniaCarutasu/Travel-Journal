package com.android.traveljournalapp;

public class User {

    public String username, email, bio;

    private final static String DEFAULT_BIO = "Hi! I am a new user!";

    public User() {}

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.bio = DEFAULT_BIO;
    }

    public User(String username, String email, String bio) {
        this.username = username;
        this.email = email;
        this.bio = bio;
    }

/*public User(String email)  {
        this.email = email;
    }*/
}
