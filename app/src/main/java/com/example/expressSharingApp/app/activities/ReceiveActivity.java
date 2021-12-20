package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.broadcasts.WifiDirectBroadcastReceiver;
import com.example.expressSharingApp.app.broadcasts.WifiServerBroadcastReceiver;
import com.example.expressSharingApp.app.repository.FileServerAsyncTask;

public class ReceiveActivity extends AppCompatActivity  {
    String host = "Receiver";
    int port = 5000; // 5000: receiver | 9999 : sender (my notation)
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    IntentFilter intentFilter;
    BroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_receive);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiServerBroadcastReceiver(manager, channel, this , port , host);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);

    }
    public void setTextView(String text){
        TextView receiveMssg = (TextView) findViewById(R.id.receive_mssg);
        receiveMssg.setText(text);
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