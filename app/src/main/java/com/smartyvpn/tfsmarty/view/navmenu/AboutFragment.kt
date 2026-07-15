package com.smartyvpn.tfsmarty.view.navmenu

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.smartyvpn.tfsmarty.R
import kotlinx.android.synthetic.main.fragment_about.view.bt_close


/*
 * Created by BanoStudio. on 15/10/2022.
 */

class AboutFragment : DialogFragment() {

    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_about, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mView.bt_close.setOnClickListener {
            dismiss()
        }

        return mView
    }

}