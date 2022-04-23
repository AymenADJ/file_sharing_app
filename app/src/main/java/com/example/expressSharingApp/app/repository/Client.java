package com.example.expressSharingApp.app.repository;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;

public class Client extends Thread{
    public int port = 4000;
    public Socket socket;
    public InputStream ips;
    public HashMap<String , String> messages = new HashMap<String , String>();


    public void run (){
        try{
            socket = new Socket("Host30" , port);
            ips = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(ips);
            messages = (HashMap<String, String>) ois.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        Client client = new Client();
        client.start();
    }
}
