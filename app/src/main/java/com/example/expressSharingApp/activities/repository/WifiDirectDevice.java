package com.example.expressSharingApp.activities.repository;

public class WifiDirectDevice {
    private String name;
    private String address;
    public WifiDirectDevice(String name , String address){
        this.name = name ;
        this.address = address;
    }
    public String getName(){return name;}
    public String getAddress(){return address;}
}
