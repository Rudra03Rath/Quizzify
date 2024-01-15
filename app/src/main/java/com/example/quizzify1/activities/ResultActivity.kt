package com.example.quizzify1.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzify1.R
import com.example.quizzify1.models.QuestionModel
import com.example.quizzify1.models.Quiz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ResultActivity : AppCompatActivity() {
    private lateinit var questions: MutableMap<String, QuestionModel>
    lateinit var txtAnswer: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setUpViews()
    }

    private fun setUpViews() {
        txtAnswer = findViewById(R.id.txtAnswer)
        val quizData = intent.getStringExtra("QUIZ")
        val questionsMapType = object : TypeToken<Map<String, QuestionModel>>() {}.type
        questions = Gson().fromJson(quizData, questionsMapType)
        calculateScore()
        setAnswerView()
        Log.d("Quiz", quizData.toString())
        //Log.d("tag", quiz.toString())
    }

    private fun setAnswerView() {
        val builder = StringBuilder("")
        for (entry in questions.entries) {
            val question = entry.value
            builder.append("<font color'#18206F'><b>Question: ${question.ques}</b></font><br/><br/>")
            builder.append("<font color='#0000FF'>UserAnswer: ${question.userAns}</font><br/><br/>")
            builder.append("<font color='#009688'>Answer: ${question.ans}</font><br/><br/>")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAnswer.text = Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
        } else {
            txtAnswer.text = Html.fromHtml(builder.toString())
        }
    }

    private fun calculateScore() {
       var score = 0
        for (entry in questions.entries){
            val question = entry.value
            if (question.ans == question.userAns){
                score += 10
            }
        }
        var totalScore = (questions.size)*10
        var txtScore : TextView = findViewById(R.id.txtScore)
        txtScore.text = "Your Score : $score/$totalScore"

    }
}