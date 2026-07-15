package com.smartyvpn.tfsmarty.view.slides

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.smartyvpn.tfsmarty.utils.AppSettings
import com.smartyvpn.tfsmarty.R
import com.smartyvpn.tfsmarty.view.screens.ControllerActivity
import com.onesignal.OneSignal
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.fragment_slide_third.*

class SlideThirdFragment : Fragment(R.layout.fragment_slide_third) {

    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        OneSignal.promptForPushNotifications();

        //then last step
        third_next_button.setOnClickListener {
            Prefs.putString(AppSettings.FIRST_TIME_KEY, "firsttime")

            val intent = Intent(mContext, ControllerActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}