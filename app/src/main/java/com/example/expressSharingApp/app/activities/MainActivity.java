package com.example.expressSharingApp.app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.expressSharingApp.R;

public class MainActivity extends AppCompatActivity {
    Button sendBtn, receiveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_main);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setHeight(getApplicationContext().getResources().getDisplayMetrics().heightPixels / 2);
        sendBtnConfig(sendBtn);
        receiveBtn = (Button) findViewById(R.id.receive_btn);
        receiveBtn.setHeight(getApplicationContext().getResources().getDisplayMetrics().heightPixels / 2);
        receiveBtnConfig(receiveBtn);
    }

    private void sendBtnConfig(Button sendBtn) {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send files
                if (checkPermissions()) {
                    Intent intent = new Intent(MainActivity.this, SelectFilesActivity.class);
                    String path = Environment.getExternalStorageDirectory().toString();
                    intent.putExtra("path", path);
                    startActivity(intent);
                } else {
                    requestPermissions();
                }
            }
        });
    }

    private boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) return false;
//        result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (result != PackageManager.PERMISSION_GRANTED) return false;
        return true;
    }

    private void requestPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 111);
//        }
    }

    private void receiveBtnConfig(Button receiveBtn) {
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // receive files
                if (checkPermissions()) {
                    Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
                    String path = Environment.getExternalStorageDirectory().toString();
                    startActivity(intent);
                } else {
                    requestPermissions();
                }
            }
        });
    }

}