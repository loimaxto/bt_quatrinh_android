package com.example.bt2.model;

import android.telephony.PhoneNumberUtils;

public class Customer {
    private String phoneNumber;
    private String CreatedDate;
    private String updatedDate;
    private int point;
    private String note;

    //Contructor
    public Customer(String phoneNumber, String CreatedDate, String updatedPointsDate, int point, String note) {
        this.phoneNumber = phoneNumber;
        this.CreatedDate = CreatedDate;
        this.updatedDate = updatedPointsDate;
        this.point = point;
        this.note = note;
    }

//Getters, Setters
public String getPhoneNumber(){
        return phoneNumber;
}
public String getCreatedDate(){
        return CreatedDate;
}
public String getUpdatedDate(){
        return updatedDate;
}
public int getPoint(){
        return point;
}
public String getNote(){
        return note;
}

public void setPhoneNumber(String PhoneNumber){
    this.phoneNumber = PhoneNumber;
    }
 public void setCreatedDate(String CreatedDate){
        this.CreatedDate = CreatedDate;
 }
 public void setUpdatedDate(String UpdatedDate){
        this.updatedDate = UpdatedDate;
 }
 public void setPoint(int point){
        this.point = point;
 }
 public void setNote(String note){
        this.note = note;
 }
}
