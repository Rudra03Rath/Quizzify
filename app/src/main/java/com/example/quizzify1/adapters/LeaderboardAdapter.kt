package com.example.quizzify1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzify1.R
import com.example.quizzify1.models.LeaderboardModel

class LeaderboardAdapter(private var leaderboardList: List<LeaderboardModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_QUIZ = 1
    private val VIEW_TYPE_USER = 2

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizTitleTextView: TextView = itemView.findViewById(R.id.quizTitleTextView)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userTextView: TextView = itemView.findViewById(R.id.leaderboardItemTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_QUIZ -> {
                val view = inflater.inflate(R.layout.item_quiz_title, parent, false)
                QuizViewHolder(view)
            }
            VIEW_TYPE_USER -> {
                val view = inflater.inflate(R.layout.item_leaderboard, parent, false)
                UserViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_QUIZ -> {
                val quizViewHolder = holder as QuizViewHolder
                quizViewHolder.quizTitleTextView.text = leaderboardList[position].quizTitle
            }
            VIEW_TYPE_USER -> {
                val userViewHolder = holder as UserViewHolder
                val leaderboardItem = leaderboardList[position]
                userViewHolder.userTextView.text =
                    "${leaderboardItem.userName}: ${leaderboardItem.score}"
            }
        }
    }

    override fun getItemCount(): Int {
        return leaderboardList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (leaderboardList[position].type == 1) VIEW_TYPE_QUIZ else VIEW_TYPE_USER
    }

    fun updateData(newList: List<LeaderboardModel>) {
        leaderboardList = newList
        notifyDataSetChanged()
    }
}

