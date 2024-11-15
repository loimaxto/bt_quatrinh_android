package com.example.bt_quatrinh_android.BT3;

import android.graphics.Bitmap;

public class Image {
    private int id;
    private String date;
    private Bitmap bitmap;

    public Image(int id, String date, Bitmap bitmap) {
        this.id = id;
        this.date = date;
        this.bitmap = bitmap;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
