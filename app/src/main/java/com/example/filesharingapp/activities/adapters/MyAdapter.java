package com.example.filesharingapp.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filesharingapp.R;

import java.io.File;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    File[] filesAndFolders;
    public MyAdapter(Context context , File[] filesAndFolders){
        this.context = context;
        this.filesAndFolders = filesAndFolders;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.one_item_list, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        File selectedFile = filesAndFolders[position];
        holder.fileName.setText(selectedFile.getName());
        holder.fileSize.setText(Long.toString(selectedFile.getTotalSpace()));
        if(selectedFile.isFile()){
            holder.imageView.setImageResource(R.drawable.ic_baseline_insert_drive_file_24);
        }else{
        holder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);}
    }

    @Override
    public int getItemCount() {
        return filesAndFolders.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        TextView fileSize;
        ImageView imageView;
        public ViewHolder(View itemView){
            super(itemView);
            fileName = itemView.findViewById(R.id.folder_name);
            fileSize = itemView.findViewById(R.id.file_size);
            imageView = itemView.findViewById(R.id.folder_image);
        }

    }
}
