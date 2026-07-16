package com.smartyvpn.tfsmarty.view.serverlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartyvpn.tfsmarty.R;
import com.smartyvpn.tfsmarty.SharedPreference;
import com.smartyvpn.tfsmarty.adapter.ServerListRVAdapter;
import com.smartyvpn.tfsmarty.interfaces.ChangeServer;
import com.smartyvpn.tfsmarty.interfaces.NavItemClickListener;
import com.smartyvpn.tfsmarty.model.Server;
import com.smartyvpn.tfsmarty.utils.AppSettings;
import com.smartyvpn.tfsmarty.utils.ads.BannerManager;
import com.smartyvpn.tfsmarty.view.screens.ControllerActivity;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class FreeServerListActivity extends AppCompatActivity implements NavItemClickListener {


    private ChangeServer changeServer;

    private RecyclerView serverListRv;
    private ArrayList<Server> serverLists;
    private ServerListRVAdapter serverListRVAdapter;

    private SharedPreference preference;

    private AdView mAdView;

    FrameLayout layouts;

    ImageView changeServerInfoBtn,server_back_button;

    private Dialog infoAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_server_list);

        preference = new SharedPreference(this);

        if (!AppSettings.Companion.isUserPaid()){

            layouts = findViewById(R.id.adsBanner);
            mAdView = new AdView(this);
            BannerManager bannerManager = new BannerManager();
            bannerManager.loadAdmobBanner(layouts,mAdView,this);
        }else {
            layouts = findViewById(R.id.adsBanner);
            layouts.setVisibility(View.GONE);
        }

        server_back_button = findViewById(R.id.server_back_button);
        server_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FreeServerListActivity.this, ControllerActivity.class));
                finish();
            }
        });

        changeServerInfoBtn = findViewById(R.id.change_server_info_btn);

        changeServerInfoBtn.setOnClickListener(v -> {
            infoDialog();
        });

        serverListRv = findViewById(R.id.recyclerview);
        serverListRv.setHasFixedSize(true);

        serverListRv.setLayoutManager(new LinearLayoutManager(this));

        serverLists = getServerList();

        // Server List recycler view initialize
        if (serverLists != null) {
            serverListRVAdapter = new ServerListRVAdapter(serverLists, FreeServerListActivity.this);
            serverListRv.setAdapter(serverListRVAdapter);
        }

    }
    private ArrayList getServerList() {

        ArrayList<Server> servers = new ArrayList<>();

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference().child("VPNS");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                servers.clear();
//                if (snapshot.exists() && snapshot.getChildrenCount()>0){
//                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                        Server server = dataSnapshot.getValue(Server.class);
//                        servers.add(server);
//                    }
//                }
//
//                if (servers.size()>0){
//                    if (serverLists != null) {
//                        serverListRVAdapter = new ServerListRVAdapter(serverLists, FreeServerListActivity.this);
//                        serverListRv.setAdapter(serverListRVAdapter);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        servers.add(new Server("United States - Atlanta", "us", "united_states.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("United Kingdom - Glasgow", "gb", "united_kingdom.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Iceland - Reykjavík", "is", "iceland.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Greece - Athens", "gr", "greece.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Switzerland - Zürich", "ch", "switzerland.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Chile - Santiago", "cl", "chile.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Estonia - Tallinn", "ee", "estonia.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Canada - Toronto", "ca", "canada.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Australia - Perth", "au", "australia.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Ireland - Dublin", "ie", "ireland.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Japan - Tokyo", "jp", "japan.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("New Zealand - Auckland", "nz", "new_zealand.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("France - Paris", "fr", "france.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("South Korea - Seoul", "kr", "south_korea.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Spain - Madrid", "es", "spain.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Finland - Helsinki", "fi", "finland.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Latvia - Riga", "lv", "latvia.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("United Arab Emirates - Dubai", "ae", "united_arab_emirates.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Costa Rica - San Jose", "cr", "costa_rica.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("Portugal - Lisbon", "pt", "portugal.ovpn", "vpn", "vpn", "free"));
        servers.add(new Server("India", "in", "india.ovpn", "vpn", "vpn", "free"));



        return servers;
    }



    @Override
    public void clickedItem(int index) {

//        changeServer.newServer(serverLists.get(index));
        preference.saveServer(serverLists.get(index));
        Intent intent = new Intent(FreeServerListActivity.this, ControllerActivity.class);
        startActivity(intent);
        finish();
    }


    private void infoDialog() {

        infoAlertDialog = new Dialog(this);
        infoAlertDialog.setContentView(R.layout.info_dialog);
        infoAlertDialog.setCancelable(false);
        infoAlertDialog.setCanceledOnTouchOutside(false);

        Button okayButton = infoAlertDialog.findViewById(R.id.info_dialog_btn);
        TextView infoTextview = infoAlertDialog.findViewById(R.id.info_dialog_details);

        infoTextview.setMovementMethod(LinkMovementMethod.getInstance());

        okayButton.setOnClickListener(v -> {
            infoAlertDialog.dismiss();
        });

        infoAlertDialog.getWindow().setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        infoAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        infoAlertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (infoAlertDialog != null) {
            if (infoAlertDialog.isShowing()) {
                infoAlertDialog.dismiss();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FreeServerListActivity.this, ControllerActivity.class));
        finish();
    }
}