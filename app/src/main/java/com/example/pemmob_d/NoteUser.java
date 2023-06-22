package com.example.pemmob_d;

public class NoteUser {
    private String user_name;
    private String user_username;
    private String user_email;
    private String user_number;
    public NoteUser() {
    }
    public NoteUser(String user_name, String user_username, String user_email, String user_number) {
        this.user_name = user_name;
        this.user_username = user_username;
        this.user_email = user_email;
        this.user_number = user_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_username() {
        return user_username;
    }

    public void setUser_username(String user_username) {
        this.user_username = user_username;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

}
