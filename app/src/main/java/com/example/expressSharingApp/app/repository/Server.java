package com.example.expressSharingApp.app.repository;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread{
    public int port = 4000;
    public ServerSocket serverSocket;
    public Socket socket;
    public OutputStream ops;
    public HashMap<String , String> messages = new HashMap<String , String>();
    public void run(){
        try{
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            ops = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(ops);
            oos.writeObject(messages);
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("sending files");
    }
    public static void main(String[]args){
        Server server = new Server();
        server.start();
    }
}
