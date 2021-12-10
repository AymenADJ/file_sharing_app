package com.example.filesharingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filesharingapp.R;
import com.example.filesharingapp.activities.adapters.MyAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class SelectFilesActivity extends AppCompatActivity {
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_files);

        RecyclerView fileRecyclerView = (RecyclerView) findViewById(R.id.all_files_recycleview);
        TextView noFilesMssg = (TextView) findViewById(R.id.no_files_mssg);
        TextView selectFile = (TextView) findViewById(R.id.select_file_titre);
        String path = getIntent().getStringExtra("path");
        File root = new File(path);
        File[] files = root.listFiles();
        if (files == null || files.length == 0) {
            fileRecyclerView.setVisibility(View.GONE);
            selectFile.setVisibility(View.GONE);
            noFilesMssg.setVisibility(View.VISIBLE);
            return;
        }
        noFilesMssg.setVisibility(View.GONE);
        fileRecyclerView.setVisibility(View.VISIBLE);
        selectFile.setVisibility(View.VISIBLE);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter manager = new MyAdapter(getApplicationContext(), files);
        fileRecyclerView.setAdapter(manager);

        fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fabConfig(manager.getSelectedFiles());

    }

    private void fabConfig(ArrayList<File> selectedFiles) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!selectedFiles.isEmpty()){
                    Toast.makeText(SelectFilesActivity.this, selectedFiles.toString(), Toast.LENGTH_SHORT).show();}
                }
            });
        }
}