package com.example.quizzify1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.compose.foundation.layout.Box
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizzify1.R
import com.example.quizzify1.adapters.QuizAdapter
import com.example.quizzify1.models.Quiz
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var adapter: QuizAdapter
    private var quizList = mutableListOf<Quiz>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        populateDummyData()
        setUpViews()
    }

    private fun populateDummyData() {
        quizList.add(Quiz("12-12-2023", "12-12-2023"))
        quizList.add(Quiz("13-12-2023", "13-12-2023"))
        quizList.add(Quiz("14-12-2023", "14-12-2023"))
        quizList.add(Quiz("15-12-2023", "15-12-2023"))
        quizList.add(Quiz("16-12-2023", "16-12-2023"))
        quizList.add(Quiz("17-12-2023", "17-12-2023"))
        quizList.add(Quiz("18-12-2023", "18-12-2023"))
    }

    fun setUpViews() {
        setUpDrawerLayout()
        setUpRecyclerView()

    }


    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        val quizRecyclerView: androidx.recyclerview.widget.RecyclerView =
            findViewById(R.id.quizRecyclerView)
        quizRecyclerView.layoutManager = GridLayoutManager(this, 2)
        quizRecyclerView.adapter = adapter
    }

    fun setUpDrawerLayout() {
        val appBar: com.google.android.material.appbar.MaterialToolbar = findViewById(R.id.appBar)
        setSupportActionBar(appBar)
        val mainDrawer: androidx.drawerlayout.widget.DrawerLayout = findViewById(R.id.mainDrawer)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, mainDrawer,
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }
}