package com.example.expressSharingApp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.activities.adapters.PeersAdapter;
import com.example.expressSharingApp.activities.repository.WifiDirectDevice;

import java.util.ArrayList;

public class DiscoveryPeersActivity extends AppCompatActivity {
    PeersAdapter adapter;
    public WifiP2pManager.Channel channel;
    public WifiP2pManager manager;
    IntentFilter intentFilter;
    BroadcastReceiver receiver;
    public ArrayList<WifiDirectDevice> devices = new ArrayList<>();
    ImageButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_discovery_peers);

        RecyclerView recyclerView = findViewById(R.id.list_peers_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new PeersAdapter(this, devices);
        recyclerView.setAdapter(adapter);
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(manager, channel, this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        refresh = (ImageButton) findViewById(R.id.refresh_discovery);
        refreshBtnConfig(refresh);

        // get the selected files list from the SelectedFiles activity
//       String [] files = getIntent().getStringArrayExtra("filesList");
//        Toast.makeText(this, files.toString(), Toast.LENGTH_SHORT).show();
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

    private void refreshBtnConfig(ImageButton refresh){
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerReceiver(receiver,intentFilter);
            }
        });
    }
}