package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.broadcasts.WifiDirectBroadcastReceiver;
import com.example.expressSharingApp.app.adapters.PeersAdapter;
import com.example.expressSharingApp.app.repository.WifiDirectDevice;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class SendingFilesActivity extends AppCompatActivity {
    public PeersAdapter adapter;
    public WifiP2pManager.Channel channel;
    public WifiP2pManager manager;
    IntentFilter intentFilter;
    BroadcastReceiver receiver;
    public ArrayList<WifiDirectDevice> devices = new ArrayList<>();
    ImageButton refresh;

    Button test_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_sending_files);

        RecyclerView recyclerView = findViewById(R.id.list_peers_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        Bundle bundle = getIntent().getBundleExtra("files");
        adapter = new PeersAdapter(this, devices, (ArrayList<String>) bundle.get("paths"));
        recyclerView.setAdapter(adapter);

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

        test_dialog = (Button) findViewById(R.id.test_dialog);
        test_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendingFiles((ArrayList<String>) bundle.get("paths"));
            }
        });
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
        //elements
        ConstraintLayout dialog = new ConstraintLayout(this);
        View view = LayoutInflater.from(dialog.getContext()).inflate(R.layout.sending_files_progression ,dialog , false);
        dialog.addView(view);
        Button done = view.findViewById(R.id.done_btn);
        Button cancel = view.findViewById(R.id.cancel_button);
        ProgressBar progressBar = view.findViewById(R.id.progress_horizontal);
        TextView percentage = view.findViewById(R.id.percentage);
        cancelBtnConfig(cancel);

        //show an alert message that contains the progress bar
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SendingFilesActivity.this);
        doneBtnConfig(done , alertDialog);
        alertDialog.setCancelable(false);
        alertDialog.setView(dialog);
        changeElements(done , cancel , progressBar, percentage,100);
        alertDialog.show();

        //sending files code here ...
        //..
        //.. changing elements while sending files
        //.. stream files
        //.. how to send files ? the methode to use ?
    }

    private void doneBtnConfig(Button done , AlertDialog.Builder dialog) {
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

    private void changeElements(Button done , Button cancel , ProgressBar progressBar , TextView percentage,int progress) {
        percentage.setText(progress + "%");
        progressBar.setProgress(progress);
        if (progressBar.getProgress() == 100) {
            done.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
        } else {
            done.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
        }
    }
}