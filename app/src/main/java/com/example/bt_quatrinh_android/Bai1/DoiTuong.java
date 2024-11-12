package com.example.bt_quatrinh_android.Bai1;

public class DoiTuong {
    public String sdt;
    public String hovaten;
    public String contactID;
    public DoiTuong(String sdt,String hovaten,String contactID)
    {
        this.sdt = sdt;
        this.hovaten=hovaten;
        this.contactID=contactID;
    }
    public String getName()
    {
        return this.hovaten;
    }

    public String sdt()
    {
        return this.sdt;
    }

    public String contactID()
    {
        return this.contactID;
    }
}
