package com.example.expressSharingApp.activities.repository;

public class WifiP2pDevice {
    private String name;
    private String address;
    public WifiP2pDevice(String name , String address){
        this.name = name ;
        this.address = address;
    }
    public String getName(){return name;}
    public String getAddress(){return address;}
}
