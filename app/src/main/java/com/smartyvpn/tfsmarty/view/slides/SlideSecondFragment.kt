package com.smartyvpn.tfsmarty.view.slides

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smartyvpn.tfsmarty.R
import kotlinx.android.synthetic.main.fragment_slide_second.*

class SlideSecondFragment : Fragment(R.layout.fragment_slide_second) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        second_next_button.setOnClickListener {
            findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)
        }

        second_back_button.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}