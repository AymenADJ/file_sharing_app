package com.example.expressSharingApp.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.expressSharingApp.R;

public class ReceiveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_receive);

    }
}