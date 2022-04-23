package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.adapters.PeersAdapter;
import com.example.expressSharingApp.app.broadcasts.MyBroadcastReceiver;
import com.example.expressSharingApp.app.repository.Utilities;
import com.example.expressSharingApp.app.repository.WifiDirectDevice;

import java.util.ArrayList;

public class SendingFilesActivity extends AppCompatActivity {
    // connection elements
    public WifiP2pManager.Channel channel;
    private int port = 4000;
    public WifiP2pManager manager;
    IntentFilter intentFilter;
    BroadcastReceiver receiver;
    public ArrayList<WifiDirectDevice> devices = new ArrayList<>();
    private ArrayList<String> files = new ArrayList<>();

    // UI elements
    ImageButton refresh;
    public PeersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_sending_files);

        RecyclerView recyclerView = findViewById(R.id.list_peers_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle bundle = getIntent().getBundleExtra("files");
        files.addAll((ArrayList<String>) bundle.get("paths"));
        adapter = new PeersAdapter(this, devices, files);
        recyclerView.setAdapter(adapter);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new MyBroadcastReceiver(manager, channel, this, port);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        refresh = (ImageButton) findViewById(R.id.refresh_discovery);
        refreshBtnConfig(refresh);

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

    private void refreshBtnConfig(ImageButton refresh) {
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerReceiver(receiver, intentFilter);
            }
        });
    }
}