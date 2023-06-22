package com.example.pemmob_d;

public class NoteStatus {
    private String user_id;
    private String movi_id;
    private String movi_status;

    public NoteStatus() {

    }
    public NoteStatus(String user_id, String movi_id, String movi_status) {
        this.user_id = user_id;
        this.movi_id = movi_id;
        this.movi_status = movi_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMovi_id() {
        return movi_id;
    }

    public void setMovi_id(String movi_id) {
        this.movi_id = movi_id;
    }

    public String getMovi_status() {
        return movi_status;
    }

    public void setMovi_status(String movi_status) {
        this.movi_status = movi_status;
    }
}
