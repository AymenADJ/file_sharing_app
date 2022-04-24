package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.broadcasts.ReceiveBroadcastReceiver;
import com.example.expressSharingApp.app.repository.Client;
import com.example.expressSharingApp.app.repository.Utilities;

import java.net.InetAddress;

public class ReceiveActivity extends AppCompatActivity {
    // connection elements
    public WifiP2pManager.Channel channel;
    private int port = 4000;
    public WifiP2pManager manager;
    IntentFilter intentFilter;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new ReceiveBroadcastReceiver(manager, channel, this, port);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        Utilities utility = new Utilities();
        utility.openWifi(this);
        utility.requestLocationPermission(this);
        utility.openLocation(this);
        registerReceiver(receiver, intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}