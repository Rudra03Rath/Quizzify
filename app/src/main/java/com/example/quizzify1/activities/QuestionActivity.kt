package com.example.quizzify1.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.google.gson.Gson


class QuestionActivity : AppCompatActivity() {
    private var quesDataList = mutableListOf<QuestionModel>()
    var questions: MutableMap<String, QuestionModel> = emptyMap<String, QuestionModel>().toMutableMap()
    var index = 0
    private lateinit var title: String
    private lateinit var next : Button
    private lateinit var prev : Button
    private lateinit var submit : Button
    private lateinit var textViewTimer: TextView
    private lateinit var timer: CountDownTimer
    private var totalTimeInMillis: Long = 180000// (x min = x*60*1000 milisec)
    private var timeLeftInMillis: Long = totalTimeInMillis
    private val countDownInterval: Long = 1000



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        title = intent.getStringExtra("title").toString()
        next = findViewById(R.id.btnNext)
        prev = findViewById(R.id.btnPrev)
        submit  = findViewById(R.id.btnSubmit)
        textViewTimer = findViewById(R.id.textViewTimer)

        setUpFirestore()
        setupEventListener()
        //initTimer()
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit Quiz")
        builder.setMessage("Are you sure you want to leave the quiz?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            timer.cancel()
            super.onBackPressed()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // Do nothing, stay in the quiz and Enjoy
        }
        builder.show()
    }


    private fun calculateTotalTime(numberOfQuestions: Int): Long {
        val durationPerQuesInMillis: Long = 30000 // 30 seconds
        return numberOfQuestions * durationPerQuesInMillis
    }


    private fun initTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished

                updateTimerUI()
            }

            override fun onFinish() {
                submitQuiz()
            }
        }
        timer.start()
    }

    private fun submitQuiz() {
        Log.d("FinalQuiz", questions.toString())

        val intent = Intent(this,ResultActivity::class.java)
        val json = Gson().toJson(questions)
        intent.putExtra("QUIZ", json)
        intent.putExtra("title", title)
        startActivity(intent)
        finish()
    }

    private fun updateTimerUI() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeFormatted = String.format("%02d:%02d", minutes, seconds)

        textViewTimer.text = "Time: $timeFormatted"
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

            timer.cancel()
            submitQuiz()
        }

    }

    private fun setUpFirestore() {
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Questions")
            .child(title)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                quesDataList.clear()
                if (snapshot.exists()) {
                    for (dataSnap in snapshot.children) {
                        val quesData = dataSnap.getValue(QuestionModel::class.java)
                        quesData?.let {
                            it.options?.let { options ->
                                //the list of options
                            }
                            quesDataList.add(it)
                        }
                    }
                    questions.clear()
                    for (question in quesDataList) {
                        question.questionId?.let { id ->
                            questions[id] = question
                        }
                    }
                    val numberOfQuestions = quesDataList.size
                    totalTimeInMillis = calculateTotalTime(numberOfQuestions)
                    timeLeftInMillis = totalTimeInMillis
                    initTimer()
                    bindViews(quesDataList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Nothing to say
            }
        })
        dbRef.get()
    }

    private fun bindViews(quesDataList: MutableList<QuestionModel>) {


        next.visibility = View.GONE
        prev.visibility = View.GONE
        submit.visibility = View.GONE
        prev.isEnabled = true

        if (index == 0) {
            next.visibility = View.VISIBLE
            prev.visibility = View.VISIBLE
            prev.isEnabled = false
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
            var url = it.quesImg.toString()
            if (url != "null") {
                //url = url.substring(5, url.length - 1)
                qtnImg.visibility = View.VISIBLE
                Glide.with(this).load(url).into(qtnImg)
            } else {
                qtnImg.visibility = View.GONE
            }
            val optionAdapter = OptionAdapter(this, it) { ques ->
                if (ques.questionId != null)
                    questions[ques.questionId!!] = ques
            }
            optList.layoutManager = LinearLayoutManager(this)
            optList.adapter = optionAdapter
            optList.setHasFixedSize(true)
        }
    }
}