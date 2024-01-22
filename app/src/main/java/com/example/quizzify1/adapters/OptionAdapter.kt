package com.example.quizzify1.adapters

import android.content.Context
import android.util.Log
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

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var optionView = itemView.findViewById<TextView>(R.id.etOption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false)
        return OptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return question.options?.size ?: 0
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.optionView.text = question.options?.get(position)
        holder.itemView.setOnClickListener {
            question.userAns = question.options?.get(position)
            callback(question)
            notifyDataSetChanged()
            Log.d("OptionAdapter", "Selected option: ${question.userAns}")
        }

        if (question.userAns == question.options?.get(position)) {
            holder.itemView.setBackgroundResource(R.drawable.option_item_selected_bg)
        } else {
            holder.itemView.setBackgroundResource(R.drawable.option_item_bg)
        }
    }
}
