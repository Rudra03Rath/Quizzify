package com.example.quizzify1.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzify1.R
import com.example.quizzify1.adapters.LeaderboardAdapter
import com.example.quizzify1.models.LeaderboardItem
import com.google.firebase.database.*

class LeaderBoardActivity : AppCompatActivity() {

    private lateinit var leaderboardRecyclerView: RecyclerView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView)
        databaseReference = FirebaseDatabase.getInstance().getReference("Leaderboard")

        setUpRecyclerView()
        displayLeaderboard()
    }

    private fun setUpRecyclerView() {
        val adapter = LeaderboardAdapter(mutableListOf())
        leaderboardRecyclerView.adapter = adapter
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun displayLeaderboard() {
        val query: Query = databaseReference.orderByValue()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val leaderboardList = mutableListOf<LeaderboardItem>()

                for (quizSnapshot in dataSnapshot.children) {
                    val quizTitle = quizSnapshot.key

                    // Add a quiz title as a separator in the list
                    if (quizTitle != null) {
                        leaderboardList.add(LeaderboardItem("", quizTitle, -1, 1))
                    }

                    val users = mutableListOf<LeaderboardItem>()

                    for (userSnapshot in quizSnapshot.children) {
                        val userName = userSnapshot.key
                        val score = userSnapshot.value as Long?

                        if (userName != null && quizTitle != null && score != null) {
                            users.add(LeaderboardItem(userName, quizTitle, score.toInt(), 0))
                        }
                    }

                    // Sort users by score in decreasing order
                    users.sortByDescending { it.score }

                    // Add sorted users to the leaderboard list
                    leaderboardList.addAll(users)
                }

                // Update the adapter with the new data
                (leaderboardRecyclerView.adapter as? LeaderboardAdapter)?.updateData(leaderboardList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }


}
