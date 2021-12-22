package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.broadcasts.ReceiverBroadcastReceiver;
import com.example.expressSharingApp.app.repository.Utilities;

public class ReceiveActivity extends AppCompatActivity {
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

        manager = (WifiP2pManager) getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
        if(manager == null){
            Toast.makeText(this, "manager null", Toast.LENGTH_SHORT).show();
        }else{
            channel = manager.initialize(this, getMainLooper(), null);
            if(channel != null){
                receiver = new ReceiverBroadcastReceiver(manager, channel, this, port, host);
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
            }else{
                Toast.makeText(ReceiveActivity.this, "channel null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (manager!= null)        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(manager!=null)unregisterReceiver(receiver);

    }

}