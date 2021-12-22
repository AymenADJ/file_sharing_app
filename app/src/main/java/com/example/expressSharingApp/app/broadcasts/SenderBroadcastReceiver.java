package com.example.expressSharingApp.app.broadcasts;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.activities.SendingFilesActivity;
import com.example.expressSharingApp.app.repository.WifiDirectDevice;

public class SenderBroadcastReceiver extends BroadcastReceiver {
    SendingFilesActivity activity;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;

    public SenderBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, SendingFilesActivity sendingFilesActivity) {
        this.activity = sendingFilesActivity;
        this.manager = manager;
        this.channel = channel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi P2P is enabled
                // then discover peers
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manager.discoverPeers(this.channel, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(activity, "Discovery succeeded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i) {
                            Toast.makeText(activity, "Discovery failed : Please enable location", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Toast.makeText(activity, "Please grant the location permission", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Wi-Fi P2P is not enabled
                Toast.makeText(activity, "Please enable the wifi", Toast.LENGTH_SHORT).show();
                TextView mssg = activity.findViewById(R.id.no_device_mssg);
                mssg.setVisibility(View.VISIBLE);
                mssg.setText("Enable the wifi");
                WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifiManager.setWifiEnabled(true);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manager.requestPeers(this.channel, new WifiP2pManager.PeerListListener() {
                        @Override
                        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                            activity.devices.clear();
                            for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                                activity.devices.add(new WifiDirectDevice(device.deviceName, device.deviceAddress));
                            }
                            if (activity.devices.size() == 0) {
                                Toast.makeText(activity, "No device found", Toast.LENGTH_SHORT).show();
                                TextView mssg = activity.findViewById(R.id.no_device_mssg);
                                mssg.setVisibility(View.VISIBLE);
                                mssg.setText("No device found");
                            } else {
                                activity.findViewById(R.id.no_device_mssg).setVisibility(View.GONE);
                            }
                            activity.adapter.notifyDataSetChanged();
                        }
                    });
                }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}
