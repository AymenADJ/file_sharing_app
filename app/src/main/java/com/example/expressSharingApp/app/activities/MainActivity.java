package com.example.expressSharingApp.app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.adapters.FilesAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    TextView numSelected;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FileSharingApp);
        setContentView(R.layout.activity_main);

        path = Environment.getExternalStorageDirectory().toString();
        RecyclerView fileRecyclerView = (RecyclerView) findViewById(R.id.all_files_recycleview);
        TextView noFilesMssg = (TextView) findViewById(R.id.no_files_mssg);
        path = getIntent().getStringExtra("path");

        File root = new File(path);
        File[] files = root.listFiles();
        if (files == null || files.length == 0) {
            fileRecyclerView.setVisibility(View.GONE);
            noFilesMssg.setVisibility(View.VISIBLE);
            return;
        }

        noFilesMssg.setVisibility(View.GONE);
        fileRecyclerView.setVisibility(View.VISIBLE);
        fileRecyclerView.setLayoutManager(new

                LinearLayoutManager(this));
        FilesAdapter manager = new FilesAdapter(getApplicationContext(), files);
        fileRecyclerView.setAdapter(manager);

        numSelected = (TextView)

                findViewById(R.id.num_selected);
        numSelected.setText(manager.getNumSeletedFiles() + " files");

        fab = (FloatingActionButton)

                findViewById(R.id.fab_send);

        fabConfig(manager.getSelectedFiles());

        // change the the number of selected files
        fileRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6,
                                       int i7) {
                numSelected = (TextView) findViewById(R.id.num_selected);
                numSelected.setText(manager.getNumSeletedFiles() + " files");
            }
        });

    }

    private boolean checkPermissions() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result != PackageManager.PERMISSION_GRANTED) return false;
        return true;
    }

    private void requestPermissions() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
    }

    private void fabConfig(ArrayList<File> selectedFiles) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedFiles.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SendingFilesActivity.class);
                    Bundle b = new Bundle();
                    b.putStringArrayList("paths", fileToPath(selectedFiles));
                    intent.putExtra("files", b);
                    startActivity(intent);
                }
            }
        });
    }

    private ArrayList<String> fileToPath(ArrayList<File> files) {
        ArrayList<String> paths = new ArrayList<String>();
        for (File file : files) {
            paths.add(file.getAbsolutePath());
        }
        return paths;
    }
}