package com.example.expressSharingApp.app.broadcasts;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.activities.ReceiveActivity;
import com.example.expressSharingApp.app.repository.FileServerAsyncTask;
import com.example.expressSharingApp.app.repository.WifiDirectDevice;

public class WifiServerBroadcastReceiver extends BroadcastReceiver {
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    ReceiveActivity activity;
    int port;
    String host;

    public WifiServerBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, ReceiveActivity activity, int port, String host) {
        this.activity = activity;
        this.channel = channel;
        this.manager = manager;
        this.port = port;
        this.host = host;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
            } else {
                // Wi-Fi P2P is not enabled
                Toast.makeText(activity, "Please enable the wifi", Toast.LENGTH_SHORT).show();
                TextView mssg = activity.findViewById(R.id.no_device_mssg);
                mssg.setVisibility(View.VISIBLE);
                mssg.setText("Enable the wifi");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                    Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
                }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
