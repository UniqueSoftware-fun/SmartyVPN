package com.smartyvpn.tfsmarty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smartyvpn.tfsmarty.R;
import com.smartyvpn.tfsmarty.interfaces.NavItemClickListener;
import com.smartyvpn.tfsmarty.model.Server;

import java.util.ArrayList;

public class ServerListRVAdapter extends RecyclerView.Adapter<ServerListRVAdapter.MyViewHolder> {

    private ArrayList<Server> serverLists;
    private Context mContext;
    private NavItemClickListener listener;

    public ServerListRVAdapter(ArrayList<Server> serverLists, Context context) {
        this.serverLists = serverLists;
        this.mContext = context;
        listener = (NavItemClickListener) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.servers_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.serverCountry.setText(serverLists.get(position).getCountry());
//        holder.tv_ip_address.setText(serverLists.get(position).getIp_address());
//        holder.tv_protocol.setText(serverLists.get(position).getType());

        Glide.with(mContext)
                .load("https://flagcdn.com/w320/"+serverLists.get(position).getFlagUrl()+".png")
                .placeholder(R.drawable.ic_server_flag_icon)
                .into(holder.serverIcon);

        holder.serverItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.clickedItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return serverLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout serverItemLayout;
        ImageView serverIcon;
        TextView serverCountry,tv_ip_address,tv_protocol;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serverItemLayout = itemView.findViewById(R.id.serverItemLayout);
            serverIcon = itemView.findViewById(R.id.server_flag_image);
            serverCountry = itemView.findViewById(R.id.tv_country_name);
            tv_ip_address = itemView.findViewById(R.id.tv_ip_address);
            tv_protocol = itemView.findViewById(R.id.tv_protocol);
        }
    }
}
