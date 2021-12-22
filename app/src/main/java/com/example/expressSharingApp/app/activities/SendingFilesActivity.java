package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.broadcasts.SenderBroadcastReceiver;
import com.example.expressSharingApp.app.adapters.PeersAdapter;
import com.example.expressSharingApp.app.repository.FileServerAsyncTask;
import com.example.expressSharingApp.app.repository.Utilities;
import com.example.expressSharingApp.app.repository.WifiDirectDevice;

import java.util.ArrayList;

public class SendingFilesActivity extends AppCompatActivity {
    public PeersAdapter adapter;
    public WifiP2pManager.Channel channel;
    public WifiP2pManager manager;
    IntentFilter intentFilter;
    BroadcastReceiver receiver;
    int port = 9999; // 9999 = sender | 5000 = receiver (my notation)
    String host = "Sender";
    public ArrayList<WifiDirectDevice> devices = new ArrayList<>();
    public ArrayList<String> files = new ArrayList<>();
    ImageButton refresh;

    ConstraintLayout dialog;
    public Button done;
    public Button cancel;
    public ProgressBar progressBar;
    public TextView percentage;
    public TextView currentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_sending_files);

        RecyclerView recyclerView = findViewById(R.id.list_peers_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        Bundle bundle = getIntent().getBundleExtra("files");
        files.addAll((ArrayList<String>) bundle.get("paths"));
        adapter = new PeersAdapter(this, devices, files);
        recyclerView.setAdapter(adapter);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new SenderBroadcastReceiver(manager, channel, this);
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

        dialog = new ConstraintLayout(this);
        View view = LayoutInflater.from(dialog.getContext()).inflate(R.layout.sending_files_progression, dialog, false);
        dialog.addView(view);
        done = view.findViewById(R.id.done_btn);
        cancel = view.findViewById(R.id.cancel_button);
        progressBar = view.findViewById(R.id.progress_horizontal);
        percentage = view.findViewById(R.id.percentage);
        currentFile = view.findViewById(R.id.current_file);
        cancelBtnConfig(cancel);
        doneBtnConfig(done);


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

    // Sending files
    public void sendingFiles(ArrayList<String> files) {

        //show an alert message that contains the progress bar
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendingFilesActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setView(dialog);
        changeElements(0, files.get(0));
        alertDialog.show();

        //sending files
        FileServerAsyncTask task = new FileServerAsyncTask(this, port, host);
        task.execute();
    }

    private void doneBtnConfig(Button done) {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Toast.makeText(SendingFilesActivity.this, "Sending files has been effectively done", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelBtnConfig(Button cancel) {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // cancel sending files implementation
                //code
                //..
            }
        });
    }

    public void changeElements(int progress, String filePath) {
        percentage.setText(progress + "%");
        progressBar.setProgress(progress);
        if (progressBar.getProgress() == 100) {
            done.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
        } else {
            done.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
        }
        currentFile.setText(filePath);
    }
}