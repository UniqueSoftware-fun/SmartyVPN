package com.smartyvpn.tfsmarty.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.smartyvpn.tfsmarty.R;
import com.smartyvpn.tfsmarty.interfaces.NavItemClickListener;
import com.smartyvpn.tfsmarty.model.Server;
import com.smartyvpn.tfsmarty.utils.AppSettings;
import com.smartyvpn.tfsmarty.view.screens.InAppPurchaseScreen;

import java.util.ArrayList;

public class PaidServerListRVAdapter extends RecyclerView.Adapter<PaidServerListRVAdapter.MyViewHolder> {

    private ArrayList<Server> serverLists;
    private Context mContext;
    private NavItemClickListener listener;

    public PaidServerListRVAdapter(ArrayList<Server> serverLists, Context context) {
        this.serverLists = serverLists;
        this.mContext = context;
        listener = (NavItemClickListener) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.paid_server_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.serverCountry.setText(serverLists.get(position).getCountry());

        if (AppSettings.Companion.isUserPaid()){
            holder.lockLayout.setVisibility(View.GONE);
        }else{
            holder.lockLayout.setVisibility(View.VISIBLE);
        }

        holder.lockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, InAppPurchaseScreen.class));
            }
        });

        Glide.with(mContext)
                .load("https://flagcdn.com/w320/"+serverLists.get(position).getFlagUrl()+".png")
                .placeholder(R.drawable.ic_server_flag_icon)
                .into(holder.serverIcon);


        holder.serverItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppSettings.Companion.isUserPaid()){
                    listener.clickedItem(position);
                }else{
                    mContext.startActivity(new Intent(mContext, InAppPurchaseScreen.class));
                }

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

        RelativeLayout lockLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serverItemLayout = itemView.findViewById(R.id.serverItemLayout);
            serverIcon = itemView.findViewById(R.id.server_flag_image);
            serverCountry = itemView.findViewById(R.id.tv_country_name);
            tv_ip_address = itemView.findViewById(R.id.tv_ip_address);
            tv_protocol = itemView.findViewById(R.id.tv_protocol);
            lockLayout = itemView.findViewById(R.id.lockLayout);
        }
    }
}
