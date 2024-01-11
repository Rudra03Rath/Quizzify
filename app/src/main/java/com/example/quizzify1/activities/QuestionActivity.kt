package com.example.quizzify1.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.quizzify1.R
import com.example.quizzify1.adapters.OptionAdapter
import com.example.quizzify1.models.QuestionModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log




class QuestionActivity : AppCompatActivity() {
    private var quesDataList = mutableListOf<QuestionModel>()
    var questions: MutableMap<String, QuestionModel>? = null
    var index = 0
    private lateinit var title: String
    private lateinit var next : Button
    private lateinit var prev : Button
    private lateinit var submit : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        title = intent.getStringExtra("title").toString()
        next = findViewById(R.id.btnNext)
         prev = findViewById(R.id.btnPrev)
         submit  = findViewById(R.id.btnSubmit)

        setUpFirestore()
        setupEventListener()
    }




    private fun setupEventListener() {
        prev.setOnClickListener {
            index--
            bindViews(quesDataList)
        }

        next.setOnClickListener {
            index++
            bindViews(quesDataList)
        }

        submit.setOnClickListener {
            Log.d("FinalQuiz", questions.toString())
        }
    }

    private fun setUpFirestore() {
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("MCQ Questions")
            .child(title)
        Log.i("dataaa", "dbref - $dbRef")
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                quesDataList.clear()
                if (snapshot.exists()) {
                    Log.i("dataaa", "dataExist")

                    for (dataSnap in snapshot.children) {

                        val quesData = dataSnap.getValue(QuestionModel::class.java)
                        Log.i("dataaa", "dataSnapQues - ${quesData!!.ques}")
                        quesDataList.add(quesData!!)

                    }
                    bindViews(quesDataList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun bindViews(quesDataList: MutableList<QuestionModel>) {

        if (index>=quesDataList.size)
        return

        next.visibility = View.GONE
        prev.visibility = View.GONE
        submit.visibility = View.GONE

        if (index == 0) {
            next.visibility = View.VISIBLE
        } else if (index == (quesDataList.size -1)) {
            submit.visibility = View.VISIBLE
            prev.visibility = View.VISIBLE
        } else {
            next.visibility = View.VISIBLE
            prev.visibility = View.VISIBLE
        }


        var question = quesDataList[index]
        question?.let {
            val qtn: TextView = findViewById(R.id.question)
            val qtnImg: ImageView = findViewById(R.id.quesImg)
            val optList: androidx.recyclerview.widget.RecyclerView = findViewById(R.id.optionList)
            qtn.text = it.ques
            val url = it.quesImg.toString()
            Glide.with(this).load(url).into(qtnImg)
            val optionAdapter = OptionAdapter(this, it)
            optList.layoutManager = LinearLayoutManager(this)
            optList.adapter = optionAdapter
            optList.setHasFixedSize(true)
        }


    }
}