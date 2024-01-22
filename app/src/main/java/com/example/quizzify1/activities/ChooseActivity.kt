package com.example.quizzify1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzify1.R
import com.google.firebase.database.FirebaseDatabase

class ChooseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        val btn1 : Button = findViewById(R.id.btnCreateQuiz)
        val btn2 : Button = findViewById(R.id.btnPracQues)

        btn1.setOnClickListener {
            val intent = Intent(this, CreateQuizActivity::class.java)
            startActivity(intent)
        }
        btn2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}

/*
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_leaderboard)

    leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView)
    databaseReference = FirebaseDatabase.getInstance().getReference("Leaderboard")

    setUpRecyclerView()
    displayLeaderboard()
}*/
