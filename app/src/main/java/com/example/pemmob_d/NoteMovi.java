package com.example.pemmob_d;

public class NoteMovi {
    private String movi_id;
    private String movi_Judul;
    private int movi_Thn;
    private String movi_Desc;

    public NoteMovi(){

    }
    public NoteMovi(String movi_Judul, int movi_Thn, String movi_Desc) {
        this.movi_Judul = movi_Judul;
        this.movi_Thn = movi_Thn;
        this.movi_Desc = movi_Desc;
    }

    public String getMovi_Judul() {
        return movi_Judul;
    }

    public void setMovi_Judul(String movi_Judul) {
        this.movi_Judul = movi_Judul;
    }

    public int getMovi_Thn() {
        return movi_Thn;
    }

    public void setMovi_Thn(int movi_Thn) {
        this.movi_Thn = movi_Thn;
    }

    public String getMovi_Desc() {
        return movi_Desc;
    }

    public void setMovi_Desc(String movi_Desc) {
        this.movi_Desc = movi_Desc;
    }

    public String getMovi_id() {
        return movi_id;
    }

    public void setMovi_id(String movi_id) {
        this.movi_id = movi_id;
    }
}
