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

                "jp",
                "japan.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "us",
                "usa.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "kr",
                "korea.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "ee",
                "estonia.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "in",
                "india.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "vn",
                "vietnam.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "th",
                "thailand.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "pe",
                "peru.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "kh",
                "cambodia.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "id",
                "indonesia.ovpn",
                "vpn",
                "vpn",
                "free"
        ));

                "ru",
                "russia.ovpn",
                "vpn",
                "vpn",
                "free"
        ));



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