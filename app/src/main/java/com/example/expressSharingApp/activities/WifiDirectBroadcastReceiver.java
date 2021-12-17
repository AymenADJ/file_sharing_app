package com.example.expressSharingApp.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    DiscoveryPeersActivity discoveryPeersActivity;
    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, DiscoveryPeersActivity discoveryPeersActivity) {
    this.discoveryPeersActivity = discoveryPeersActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE , -1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                //wifi p2p enabled
                if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)){
                    // the peer list has changed
                    if(this.discoveryPeersActivity.manager!=null){
                        if (ActivityCompat.checkSelfPermission(this.discoveryPeersActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        discoveryPeersActivity.manager.requestPeers(discoveryPeersActivity.channel, new WifiP2pManager.PeerListListener() {
                                @Override
                                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                                    // peer list contains all the discrovered peers
                                    //add discovred peers in the recyclerview
                                }
                            });

                    }
                }
                if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
                    // connection state changed
                }
                if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
                    //wifi state changed
                }
            }else{
                //wifi p2p is not enabled
            }
        }

    }
}
