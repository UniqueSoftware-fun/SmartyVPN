package com.smartyvpn.tfsmarty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.onesignal.OneSignal
import com.google.android.gms.ads.MobileAds
import com.pixplicity.easyprefs.library.Prefs

@HiltAndroidApp
class VPNApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initPref()
        initGoogleAds()
        initOneSignal()

    }

    private fun initOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
        OneSignal.initWithContext(this)
        OneSignal.setAppId("13f9238e-9268-4e38-93bf-256e186f2299")
    }

    private fun initGoogleAds() {
        MobileAds.initialize(this)
    }

    private fun initPref() {
        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
    
}