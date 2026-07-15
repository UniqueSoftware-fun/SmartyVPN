package com.smartyvpn.tfsmarty.view.navmenu

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.smartyvpn.tfsmarty.R
import com.kikt.view.CustomRatingBar
import com.kikt.view.CustomRatingBar.OnStarChangeListener

/*
* Show the Rating Dialog
* of the PrivateVPN
*
* OR
*
* Get The User Experience
* about the PrivateVPN
* */
class RateUsFragment : DialogFragment() {

    private lateinit var mView: View
    private lateinit var mContext: Context

    lateinit var editText: EditText
    lateinit var poitiveButton: TextView
    lateinit var negativeButton: TextView
    lateinit var ratingBar: CustomRatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_rate_us, container, false)
        rateUs()
        return mView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    protected fun rateUs() {
        negativeButton = mView.findViewById<TextView>(R.id.dialog_rating_button_negative)
        ratingBar = mView.findViewById<CustomRatingBar>(R.id.dialog_rating_rating_bar)
        editText = mView.findViewById<EditText>(R.id.dialog_rating_feedback)
        poitiveButton = mView.findViewById<TextView>(R.id.dialog_rating_button_feedback_submit)
        ratingBar.onStarChangeListener = OnStarChangeListener { ratingBar, star ->
            if (star <= 4) {
                showFeedbacDialog()
            } else {
                //go to playStore
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "market://details?id=" + mContext.getApplicationContext().getPackageName()
                    )
                )
                startActivity(intent)
            }
        }
        negativeButton.setOnClickListener {
            dismiss()
        }
    }


    private fun showFeedbacDialog() {

        editText.visibility = View.VISIBLE
        poitiveButton.visibility = View.VISIBLE

        poitiveButton.setOnClickListener {
            if (editText.text.toString().isEmpty() || editText.text.toString().length < 10) {
                Toast.makeText(context,"Please explain the issue",Toast.LENGTH_SHORT).show()

                

            } else {
                // go to email
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_email_subject))
                intent.putExtra(Intent.EXTRA_TEXT, editText.text.toString())
                try {
                    startActivity(Intent.createChooser(intent, "Send E-mail"))
                } catch (ex: ActivityNotFoundException) {
                    Toast.makeText(context,"No email app found.",Toast.LENGTH_SHORT).show()
                } catch (ex: Exception) {
                    Toast.makeText(context,"Network Error.",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}