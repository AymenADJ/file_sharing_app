package com.example.expressSharingApp.activities.adapters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expressSharingApp.R;
import com.example.expressSharingApp.activities.DiscoveryPeersActivity;
import com.example.expressSharingApp.activities.repository.WifiDirectDevice;

import java.util.ArrayList;

public class PeersAdapter extends RecyclerView.Adapter<PeersAdapter.PeersViewHolder> {
    DiscoveryPeersActivity context;
    ArrayList<WifiDirectDevice> devices;

    public PeersAdapter(Context context, ArrayList<WifiDirectDevice> wifiP2pDevices) {
        this.devices = wifiP2pDevices;
        this.context = (DiscoveryPeersActivity) context;
    }

    @NonNull
    @Override
    public PeersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_peer, parent, false);
        return new PeersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PeersViewHolder holder, int position) {
       if(!devices.isEmpty()){
           WifiDirectDevice device = devices.get(position);
           holder.name.setText(device.getName());
           holder.address.setText(device.getAddress());
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   WifiP2pConfig config = new WifiP2pConfig();
                   config.deviceAddress = device.getAddress();
                   if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                       context.manager.connect(context.channel, config, new WifiP2pManager.ActionListener() {
                           @Override
                           public void onSuccess() {
                               Toast.makeText(context, "connection succeeded", Toast.LENGTH_SHORT).show();
                               // start sending data to this device
                           }

                           @Override
                           public void onFailure(int i) {
                               Toast.makeText(context, "connection failed", Toast.LENGTH_SHORT).show();
                           }
                       });
                   }
               }
           });
       }
    }

    @Override
    public int getItemCount() {
        return devices.isEmpty()?0:devices.size();
    }

    public class PeersViewHolder extends RecyclerView.ViewHolder{
        public TextView name , address;
        public PeersViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.peer_name);
            address = itemView.findViewById(R.id.peer_address);
        }
    }
}
