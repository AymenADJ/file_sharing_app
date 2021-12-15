package com.example.filesharingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
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
    TextView numSelected ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_select_files);

        RecyclerView fileRecyclerView = (RecyclerView) findViewById(R.id.all_files_recycleview);
        TextView noFilesMssg = (TextView) findViewById(R.id.no_files_mssg);
        String path = getIntent().getStringExtra("path");
        File root = new File(path);
        File[] files = root.listFiles();
        if (files == null || files.length == 0) {
            fileRecyclerView.setVisibility(View.GONE);
            noFilesMssg.setVisibility(View.VISIBLE);
            return;
        }
        noFilesMssg.setVisibility(View.GONE);
        fileRecyclerView.setVisibility(View.VISIBLE);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter manager = new MyAdapter(getApplicationContext(), files);
        fileRecyclerView.setAdapter(manager);

        fab = (FloatingActionButton) findViewById(R.id.fab_send);
        fabConfig(manager.getSelectedFiles());

        numSelected =(TextView) findViewById(R.id.num_selected);
        numSelected.setText(manager.getNumSeletedFiles()+" files");
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