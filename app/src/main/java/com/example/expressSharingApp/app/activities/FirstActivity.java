package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.expressSharingApp.R;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button btn = (Button) findViewById(R.id.start_btn);
        btnConfig(btn , this);
    }
    private void btnConfig(Button btn , Context c){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissions()){
                    Intent intent = new Intent(c , MainActivity.class);
                    intent.putExtra("path" , Environment.getExternalStorageDirectory().getAbsolutePath());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(intent);
                }else{
                    requestPermissions();
                }
            }
        });
    }
    private boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(FirstActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) return false;
        return true;
    }

    private void requestPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(FirstActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(FirstActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
    }
}