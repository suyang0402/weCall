package com.wecall.myapps.wecall;

/**
 * Created by Jacob on 12/7/16.
 */

public class ContactsLocation {
    private String Name;
    private String City;

    ContactsLocation(String name, String city){
        Name = name;
        City = city;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
