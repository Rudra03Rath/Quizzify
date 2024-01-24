package com.example.quizzify1.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzify1.R
import com.example.quizzify1.adapters.LeaderboardAdapter
import com.example.quizzify1.models.LeaderboardModel
import com.google.firebase.database.*

class LeaderBoardActivity : AppCompatActivity() {

    private lateinit var leaderboardRecyclerView: RecyclerView
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView)
        dbRef = FirebaseDatabase.getInstance().getReference("Leaderboard")

        setUpRecyclerView()
        displayLeaderboard()
    }

    private fun setUpRecyclerView() {
        val adapter = LeaderboardAdapter(mutableListOf())
        leaderboardRecyclerView.adapter = adapter
        leaderboardRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun displayLeaderboard() {
        val query: Query = dbRef.orderByValue()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val leaderboardList = mutableListOf<LeaderboardModel>()

                for (quizSnapshot in dataSnapshot.children) {
                    val quizTitle = quizSnapshot.key

                    if (quizTitle != null) {
                        leaderboardList.add(LeaderboardModel("", quizTitle, -1, 1))
                    }

                    val users = mutableListOf<LeaderboardModel>()

                    for (userSnapshot in quizSnapshot.children) {
                        val userName = userSnapshot.key
                        val score = userSnapshot.value as Long?

                        if (userName != null && quizTitle != null && score != null) {
                            users.add(LeaderboardModel(userName, quizTitle, score.toInt(), 0))
                        }
                    }

                    users.sortByDescending { it.score }
                    leaderboardList.addAll(users)
                }
                (leaderboardRecyclerView.adapter as? LeaderboardAdapter)?.updateData(leaderboardList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Kuch bhi
            }
        })
    }


}
