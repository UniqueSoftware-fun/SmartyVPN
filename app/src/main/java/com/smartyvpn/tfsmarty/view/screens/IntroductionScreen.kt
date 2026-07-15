package com.smartyvpn.tfsmarty.view.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smartyvpn.tfsmarty.R
import dagger.hilt.android.AndroidEntryPoint

/*
 * Created by BanoStudio. on 15/10/2022.
 */

@AndroidEntryPoint
class IntroductionScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
    }
}