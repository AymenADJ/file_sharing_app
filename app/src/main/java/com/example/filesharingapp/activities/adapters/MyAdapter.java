package com.example.filesharingapp.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filesharingapp.R;
import com.example.filesharingapp.activities.SelectFilesActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    File[] filesAndFolders;
    ArrayList<File> selectedFiles = new ArrayList<File>();

    public MyAdapter(Context context, File[] filesAndFolders) {
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }
    public ArrayList<File> getSelectedFiles(){
        return selectedFiles;
    }
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
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
              Uri uri = Uri.parse("content://"+selectedFile.getAbsolutePath());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName;
        TextView filePath;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.folder_name);
            filePath = itemView.findViewById(R.id.file_path);
            imageView = itemView.findViewById(R.id.folder_image);
        }

    }
}
