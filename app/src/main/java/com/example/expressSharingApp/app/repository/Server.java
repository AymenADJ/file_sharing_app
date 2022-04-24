package com.example.expressSharingApp.app.repository;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server extends Thread{
    private int port = 4000;
    private ServerSocket serverSocket;
    private Socket socket;
    private OutputStream ops;
    private HashMap<String , String> messages ;
    public Server(HashMap<String , String> messages){
        this.messages = messages;
    }
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            ops = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(ops);
            oos.writeObject(messages);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
