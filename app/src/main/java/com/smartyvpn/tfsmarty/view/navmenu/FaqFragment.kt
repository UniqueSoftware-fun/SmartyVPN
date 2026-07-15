package com.smartyvpn.tfsmarty.view.navmenu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartyvpn.tfsmarty.R
import kotlinx.android.synthetic.main.expansion_panel_recycler_cell.view.answer_text
import kotlinx.android.synthetic.main.expansion_panel_recycler_cell.view.question_text
import kotlinx.android.synthetic.main.fragment_faq.recyclerView
import kotlinx.android.synthetic.main.toolbar.appbar_back_button
import kotlinx.android.synthetic.main.toolbar.appbar_title

/*
* Show the Following Questions
* that user can face during
* using the PrivateVPN
* */

/*
 * Created by BanoStudio. on 15/10/2022.
 */

class FaqFragment : Fragment(R.layout.fragment_faq) {

    private lateinit var mContext: Context

    private lateinit var questionsArray: Array<String>
    private lateinit var answersArray: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        questionsArray = resources.getStringArray(R.array.faq_questions)
        answersArray = resources.getStringArray(R.array.faq_answers)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configAppbar()
        initFaqsRecyclerView()
    }

    private fun initFaqsRecyclerView() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = FaqsAdapter(questionsArray, answersArray)
        }
    }

    private fun configAppbar() {
        appbar_title.text = resources.getString(R.string.faqs_frag_title)
        appbar_back_button.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    class FaqsAdapter constructor(
        private val questionsList: Array<String>,
        private val answersList: Array<String>,
    ) : RecyclerView.Adapter<FaqsAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.expansion_panel_recycler_cell, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItem(
                question = questionsList[position],
                answer = answersList[position]
            )
        }

        override fun getItemCount(): Int {
            return questionsList.size
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItem(question: String, answer: String) {
                itemView.question_text.text = question
                itemView.answer_text.text = answer
            }
        }

    }
}