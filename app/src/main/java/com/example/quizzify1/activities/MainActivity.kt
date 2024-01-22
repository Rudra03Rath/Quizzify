package com.example.quizzify1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizzify1.R
import com.example.quizzify1.adapters.QuizAdapter
import com.example.quizzify1.models.Quiz
import com.google.firebase.auth.FirebaseAuth

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
        quizList.add(Quiz("001", "PHYSICS"))
        quizList.add(Quiz("002", "CHEMISTRY"))
        quizList.add(Quiz("003", "MATHEMATICS"))
        quizList.add(Quiz("004", "BIOLOGY"))
        quizList.add(Quiz("005", "GK"))
        quizList.add(Quiz("006", "MISCELLANEOUS"))
    }

    fun setUpViews() {
        setUpDrawerLayout()
        setUpRecyclerView()
    }


    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        Log.i("data", "quizList -> ${quizList[0].title}")
        val quizRecyclerView: androidx.recyclerview.widget.RecyclerView =
            findViewById(R.id.quizRecyclerView)
        quizRecyclerView.layoutManager = GridLayoutManager(this, 2)
        quizRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : QuizAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val title = quizList[position].title
                val intent = Intent(this@MainActivity, QuestionActivity::class.java)
                intent.putExtra("title", title)
                startActivity(intent)

            }

        })
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
        val nav_view: com.google.android.material.navigation.NavigationView =
            findViewById(R.id.navigationView)

        val headerView = nav_view.getHeaderView(0)
        val usernameTextView: TextView = headerView.findViewById(R.id.username)
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        val index: Int = userEmail!!.indexOf('@')
        val str = userEmail.subSequence(0, index).toString()
        usernameTextView.text = str.capitalize()

        val btnLogOut: MenuItem = nav_view.menu.findItem(R.id.logOut)
        btnLogOut.setOnMenuItemClickListener {
            showLogoutConfirmationDialog()
            true
        }
    }


    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)

    }
}