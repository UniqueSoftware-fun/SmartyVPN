package com.smartyvpn.tfsmarty.utils

class AppSettings {
    companion object {
        //this flag will be handled by subscription
        var isUserPaid = false

        // enable admob or facebook ads, by default admob ads are enable
        // set flags true or false
        val enableAdmobAds = true
        val enableFacebookAds = false


        //Subscription id's
        val LICENSE_KEY = "License Key put here"
        val one_month_subscription_id = "put your one month subscription id here"
        val three_month_subscription_id = "put your three months subscription id here"
        val one_year_subscription_id = "put your one year subscription id here"


        //slides key
        const val FIRST_TIME_KEY: String = "firsttime"

        //Check Paid or Not
         var paid: String = "paid"
         var free: String = "free"
    }
}