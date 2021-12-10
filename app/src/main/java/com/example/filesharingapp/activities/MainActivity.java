package com.example.filesharingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.filesharingapp.R;

public class MainActivity extends AppCompatActivity {
    Button sendBtn , receiveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn = (Button) findViewById(R.id.send_btn);
        sendBtn.setHeight(getApplicationContext().getResources().getDisplayMetrics().heightPixels /2);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send files
                if(checkPermission()) {
                    Intent intent= new Intent(MainActivity.this , SelectFilesActivity.class);
                    String path = Environment.getExternalStorageDirectory().toString();
                    intent.putExtra("path",path);
                    startActivity(intent);
                }
                else{
                    requestPermissions();
                }
                }
            private boolean checkPermission() {
                int result = ContextCompat.checkSelfPermission(MainActivity.this , Manifest.permission.READ_EXTERNAL_STORAGE);
                if(result == PackageManager.PERMISSION_GRANTED) return true;
                return false;
            }
            private void requestPermissions() {
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this , Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(MainActivity.this, "You should granted this permission", Toast.LENGTH_SHORT).show();
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE} , 111);
                }
            }
        });
        receiveBtn = (Button) findViewById(R.id.receive_btn);
        receiveBtn.setHeight(getApplicationContext().getResources().getDisplayMetrics().heightPixels /2);
        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // receive files
            }
        });
    }
}