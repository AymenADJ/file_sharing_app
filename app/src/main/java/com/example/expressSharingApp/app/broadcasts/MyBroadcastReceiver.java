package com.example.expressSharingApp.app.broadcasts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.activities.SendingFilesActivity;
import com.example.expressSharingApp.app.repository.Client;
import com.example.expressSharingApp.app.repository.Server;
import com.example.expressSharingApp.app.repository.WifiDirectDevice;

import java.util.ArrayList;

public class MyBroadcastReceiver extends android.content.BroadcastReceiver {
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    SendingFilesActivity activity;
    int port;

    public MyBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, SendingFilesActivity activity, int port  ) {
        this.activity = activity;
        this.channel = channel;
        this.manager = manager;
        this.port = port;
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
                    activity.devices.clear();
                    manager.discoverPeers(this.channel, new WifiP2pManager.ActionListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(activity, "Discovery succeeded", Toast.LENGTH_SHORT).show();
                            manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                                @RequiresApi(api = Build.VERSION_CODES.Q)
                                @Override
                                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                                    activity.devices.clear();
                                    for (WifiP2pDevice device : wifiP2pDeviceList.getDeviceList()) {
                                        activity.devices.add(new WifiDirectDevice(device.deviceName, device.deviceAddress));
                                    }
                                    if (wifiP2pDeviceList.getDeviceList().size() == 0) {
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
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                        if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                            Toast.makeText(activity, "Server", Toast.LENGTH_SHORT).show();
                            Server server = new Server();
                            server.messages.put("test" , "hello my client !");
                            server.run();
                        } else if (wifiP2pInfo.groupFormed) {
                            Toast.makeText(activity, "Client", Toast.LENGTH_SHORT).show();
                            Client client = new Client();
                            Toast.makeText(activity, client.messages.toString(), Toast.LENGTH_SHORT).show();
                            client.run();
                        }
                    }
                });
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
        }
    }
}
