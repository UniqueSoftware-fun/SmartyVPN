package com.smartyvpn.tfsmarty.utils.ads;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import com.smartyvpn.tfsmarty.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


/**
 * @package Andrio Jutt Dev
 * @author Muhammad Adil
 * @copyright MAY 2023
 */

public class BannerManager {

    public void loadAdmobBanner(FrameLayout layout, AdView adView, Activity activity) {

//            adView.setAdSize(AdSize.BANNER);
        layout.setVisibility(View.VISIBLE);
        layout.addView(adView);
//            if (Ad_Utils.getAdIds().getBanner_id() != null && !Ad_Utils.getAdIds().getBanner_id().isEmpty()) {
        adView.setAdUnitId(activity.getResources().getString(R.string.banner_ad));
        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize(activity);
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);

        adView.loadAd(adRequest);
//            }

    }

    private AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


}
