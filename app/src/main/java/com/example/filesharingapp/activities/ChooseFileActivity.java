package com.example.filesharingapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filesharingapp.R;
import com.example.filesharingapp.activities.adapters.MyAdapter;

import org.w3c.dom.Text;

import java.io.File;

public class ChooseFileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);

        RecyclerView fileRecyclerView = (RecyclerView) findViewById(R.id.all_files_recycleview);
        TextView noFilesMssg = (TextView) findViewById(R.id.no_files_mssg);
        TextView chooseFile = (TextView) findViewById(R.id.choose_file_title);

        String path = getIntent().getStringExtra("path");
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        File root = new File(path);
        File[] files = root.listFiles();
        if(files == null ||files.length == 0){
            fileRecyclerView.setVisibility(View.GONE);
            chooseFile.setVisibility(View.GONE);
            noFilesMssg.setVisibility(View.VISIBLE);
            return ;
        }
        noFilesMssg.setVisibility(View.GONE);
        fileRecyclerView.setVisibility(View.VISIBLE);
        chooseFile.setVisibility(View.VISIBLE);

        fileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fileRecyclerView.setAdapter(new MyAdapter(getApplicationContext(),files));

    }
}