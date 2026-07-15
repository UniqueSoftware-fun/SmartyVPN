package com.smartyvpn.tfsmarty.view.slides

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smartyvpn.tfsmarty.R
import kotlinx.android.synthetic.main.fragment_slide_first.*


/*
 * Created by BanoStudio. on 15/10/2022.
 */


class SlideFirstFragment : Fragment(R.layout.fragment_slide_first) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        first_next_button.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }

    }
}