package com.example.quizzify1.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzify1.R
import com.example.quizzify1.models.QuestionModel

class OptionAdapter(
    val context: Context,
    val question: QuestionModel,
    val callback: (QuestionModel) -> Unit
) :
    RecyclerView.Adapter<OptionAdapter.OptionViewHolder>() {

    private var options: List<String?> =
        listOf(question.opt1, question.opt2, question.opt3, question.opt4)

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var optionView = itemView.findViewById<TextView>(R.id.quiz_option)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false)
        return OptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.optionView.text = options[position]
        holder.itemView.setOnClickListener {
            question.userAns = options[position]
            callback(question)
            notifyDataSetChanged()
        }

        if (question.userAns == options[position]) {
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected_bg)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.option_item_bg)
        }
    }
}
