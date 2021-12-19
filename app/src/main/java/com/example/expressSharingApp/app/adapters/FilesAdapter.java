package com.example.expressSharingApp.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.app.activities.SelectFilesActivity;

import java.io.File;
import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesViewHolder> {
    Context context;
    File[] filesAndFolders;
    ArrayList<File> selectedFiles = new ArrayList<File>();

    public FilesAdapter(Context context, File[] filesAndFolders) {
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }
    public ArrayList<File> getSelectedFiles(){
        return selectedFiles;
    }
    public int getNumSeletedFiles(){
        return selectedFiles.size();
    }
    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_file, parent, false);
        return new FilesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, int position) {
        File selectedFile = filesAndFolders[position];
        holder.fileName.setText(selectedFile.getName());
        holder.filePath.setText(selectedFile.getAbsolutePath());
        if (selectedFile.isFile()) {
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
        }
        if (selectedFile.isDirectory()) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFolder(selectedFile);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFile(selectedFile);
                }

            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
               public boolean onLongClick(View view) {
                   if(holder.itemView.isSelected()){
                       holder.itemView.setSelected(false);
                       selectedFiles.remove(selectedFile);
                   }else{
                   holder.itemView.setSelected(true);
                   selectedFiles.add(selectedFile);
                   }
                    return true;
               }
           }

            );
        }
    }

    private void openFile(File selectedFile) {
      if(selectedFile.exists()){
          try {
              Uri uri = Uri.parse("file://"+selectedFile.getAbsolutePath());
//              Toast.makeText(context, selectedFile.getParent(), Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(Intent.ACTION_VIEW);
              intent.setDataAndType(uri,"image/*");
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      else{
          Toast.makeText(context, "This file does not exist", Toast.LENGTH_SHORT).show();
      }
    }

    private void openFolder(File selectedFile) {
        Intent intent = new Intent(context, SelectFilesActivity.class);
        String path = selectedFile.getAbsolutePath();
        intent.putExtra("path", path);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class FilesViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView filePath;
        ImageView imageView;
        public FilesViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.folder_name);
            filePath = itemView.findViewById(R.id.file_path);
            imageView = itemView.findViewById(R.id.folder_image);
        }

    }
}
