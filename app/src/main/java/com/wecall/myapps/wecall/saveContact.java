package com.wecall.myapps.wecall;

import java.util.Date;

/**
 * Created by Jacob on 11/30/16.
 */

public class saveContact {
    String contact_name;
    String string_last_time_contacted;
    int int_last_time_contacted;
    String string_phone_number;
    String correctlyFormatedDate;
    String photoID;
    int int_phone_number;

    //  Constructor 1
    saveContact() {
        contact_name = "";
        string_last_time_contacted = "";
        int_last_time_contacted = 0;
    }


    //  Constructor 1 parameter
    saveContact(String contact_name) {
        this.contact_name = contact_name;

    }


    //  Constructor 2 parameters
    saveContact(String contact_name, String string_last_time_contacted) {
        this.contact_name = contact_name;
        this.string_last_time_contacted = string_last_time_contacted;
    }


    //  Constructor 3 params
    saveContact(String contact_name, String string_last_time_contacted, String string_phone_number) {
        this.contact_name = contact_name;
        this.string_last_time_contacted = string_last_time_contacted;
        this.string_phone_number = string_phone_number;
    }


    //  Constructor 4 params
    saveContact(String contact_name, String string_last_time_contacted, String string_phone_number, String correctlyFormatedDate) {
        this.contact_name = contact_name;
        this.string_last_time_contacted = string_last_time_contacted;
        this.string_phone_number = string_phone_number;
        this.correctlyFormatedDate = correctlyFormatedDate;
    }


    //  Constructor 5 params
    //  This is the one that is used
    saveContact(String contact_name, String string_last_time_contacted, String string_phone_number, String correctlyFormatedDate, String photoID) {
        this.contact_name = contact_name;
        this.string_last_time_contacted = string_last_time_contacted;
        this.string_phone_number = string_phone_number;
        this.correctlyFormatedDate = correctlyFormatedDate;
        this.photoID = photoID;
    }


    //  Getters and setters


    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getCorrectlyFormatedDate() {
        return correctlyFormatedDate;
    }

    public void setCorrectlyFormatedDate(String correctlyFormatedDate) {
        this.correctlyFormatedDate = correctlyFormatedDate;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getString_last_time_contacted() {
        return string_last_time_contacted;
    }

    public int getInt_last_time_contacted() {
        return int_last_time_contacted;
    }

    public String getString_phone_number(){
        return string_phone_number;
    }

    public void setString_phone_number(String string_phone_number) {
        this.string_phone_number = string_phone_number;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setString_last_time_contacted(String string_last_time_contacted) {
        this.string_last_time_contacted = string_last_time_contacted;
    }

    public void setInt_last_time_contacted(int int_last_time_contacted) {
        this.int_last_time_contacted = int_last_time_contacted;
    }
}
