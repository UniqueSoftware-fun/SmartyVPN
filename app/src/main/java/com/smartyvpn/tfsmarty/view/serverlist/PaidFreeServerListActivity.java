package com.smartyvpn.tfsmarty.view.serverlist;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartyvpn.tfsmarty.R;
import com.smartyvpn.tfsmarty.SharedPreference;
import com.smartyvpn.tfsmarty.adapter.PaidServerListRVAdapter;
import com.smartyvpn.tfsmarty.interfaces.NavItemClickListener;
import com.smartyvpn.tfsmarty.model.Server;
import com.smartyvpn.tfsmarty.utils.AppSettings;
import com.smartyvpn.tfsmarty.utils.ads.BannerManager;
import com.smartyvpn.tfsmarty.view.screens.ControllerActivity;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class PaidFreeServerListActivity extends AppCompatActivity implements NavItemClickListener {

    private RecyclerView serverListRv;
    private ArrayList<Server> serverLists;
    private PaidServerListRVAdapter serverListRVAdapter;

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
                startActivity(new Intent(PaidFreeServerListActivity.this, ControllerActivity.class));
                finish();
            }
        });

        changeServerInfoBtn = findViewById(R.id.change_server_info_btn);

        serverListRv = findViewById(R.id.recyclerview);
        serverListRv.setHasFixedSize(true);

        serverListRv.setLayoutManager(new LinearLayoutManager(this));

        serverLists = getServerList();

        changeServerInfoBtn.setOnClickListener(v -> {
            infoDialog();
        });

        // Server List recycler view initialize
        if (serverLists != null) {
            serverListRVAdapter = new PaidServerListRVAdapter(serverLists, this);
            serverListRv.setAdapter(serverListRVAdapter);
        }

    }
    private ArrayList getServerList() {

        ArrayList<Server> servers = new ArrayList<>();

        servers.add(new Server("United Kingdom - Manchester", "gb", "paid_united_kingdom.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("United Kingdom - London", "gb", "paid_united_kingdom_london.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Australia - Melburn", "au", "paid_australia.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Canada - Montreal", "ca", "paid_canada.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("United States - Ashburn", "us", "paid_united_states.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Switzerland - Zürich", "ch", "paid_switzerland.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Chile - Santiago", "cl", "paid_chile.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Estonia - Tallinn", "ee", "paid_estonia.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Ireland - Dublin", "ie", "paid_ireland.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("Japan - Tokyo", "jp", "paid_japan.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("New Zealand - Auckland", "nz", "paid_new_zealand.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("France - Paris", "fr", "paid_france.ovpn", "vpn", "vpn", "paid"));
        servers.add(new Server("India", "in", "paid_india.ovpn", "vpn", "vpn", "paid"));

        return servers;
    }



    @Override
    public void clickedItem(int index) {
        preference.saveServer(serverLists.get(index));
        Intent intent = new Intent(PaidFreeServerListActivity.this, ControllerActivity.class);
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
        startActivity(new Intent(PaidFreeServerListActivity.this, ControllerActivity.class));
        finish();
    }
}