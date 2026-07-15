package com.smartyvpn.tfsmarty.utils.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

/**
 * @package Andrio Jutt Dev
 * @author Muhammad Adil
 * @copyright MAY 2023
 */

public class InterstitialManager {
    private Context mContext;
    private Activity mActivity;
    private InterstitialAd mInterstitialAd;
    private MyInterstitialAdListener mListener;

    private static final String TAG = InterstitialManager.class.getName();

    public InterstitialManager(Context context, Activity activity, String admobInterstitialId, MyInterstitialAdListener adListener) {
        mContext = context;
        mActivity = activity;
        mListener = adListener;
    }

    public void loadAdmobInterstitial(String id) {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(mContext, id, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull @NotNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
                mListener.OnAdLoaded();

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mListener.OnAdFailedToShowContent();
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: Ad failed to show content");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Log.d(TAG, "onAdShowedFullScreenContent: Ad show full screen content");
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mListener.OnAdDismissed();
                        Log.d(TAG, "onAdDismissedFullScreenContent: Ad dismissed");
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Log.d(TAG, "onAdImpression: Ad impression");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mListener.OnAdFailedToLoad();
                mInterstitialAd = null;
                Log.d(TAG, "onAdFailedToLoad: Ad failed to load error: " + loadAdError.toString());
            }
        });
    }

    public void showAdmobInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(mActivity);
        } else {
            Log.d(TAG, "showAdmobInterstitial: Ad is not loaded yet.");
        }
    }

    public Boolean isAdLoaded() {
        return mInterstitialAd != null;
    }

    public interface MyInterstitialAdListener {
        void OnAdLoaded();

        void OnAdFailedToLoad();

        void OnAdFailedToShowContent();

        void OnAdDismissed();
    }
}
