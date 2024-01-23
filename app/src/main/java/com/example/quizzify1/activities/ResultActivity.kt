package com.example.quizzify1.activities


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.capitalize
import androidx.core.content.ContextCompat
import com.example.quizzify1.R
import com.example.quizzify1.models.QuestionModel
import com.example.quizzify1.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class ResultActivity : AppCompatActivity() {
    private lateinit var questions: MutableMap<String, QuestionModel>
    lateinit var txtAnswer: TextView
    private var quizList = mutableListOf<Quiz>()
    private var score=0
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var pieChart: PieChart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        firebaseAuth = FirebaseAuth.getInstance()
        val btnLb : Button = findViewById(R.id.btnLeaderboard)
       // FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setUpViews()
      //  saveScoreToFirebase()
        btnLb.setOnClickListener {
            val intent = Intent(this, LeaderBoardActivity::class.java)
            startActivity(intent)
        }
        setUpPieChart()
    }

    private fun setUpPieChart() {
        pieChart = findViewById(R.id.pieChart)
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(android.R.color.transparent)
        pieChart.transparentCircleRadius = 61f
        pieChart.legend.isEnabled = false

        var correctCount = 0
        var skippedCount = 0

        for (entry in questions.entries) {
            val question = entry.value
            if (question.ans == question.userAns) {
                correctCount++
            } else if (question.userAns.isNullOrEmpty()) {
                skippedCount++
            }
        }

        val wrongCount = questions.size - correctCount - skippedCount

        val correctEntries = PieEntry(correctCount.toFloat(), 0F)
        val skippedEntries = PieEntry(skippedCount.toFloat(), 1F)
        val wrongEntries = PieEntry(wrongCount.toFloat(), 2F)

        val dataSet = PieDataSet(listOf(correctEntries, skippedEntries, wrongEntries), "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.colors = listOf(
            ContextCompat.getColor(this, R.color.green),  // Green for correct entries
            ContextCompat.getColor(this, R.color.blue),   // Blue for skipped entries
            ContextCompat.getColor(this, R.color.red)     // Red for wrong entries
        )

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(15f)
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.black))

        pieData.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${value.toInt()}%"
            }
        })

        pieChart.data = pieData
    }


    private fun setUpViews() {
        txtAnswer = findViewById(R.id.txtAnswer)
        val quizData = intent.getStringExtra("QUIZ")
        val questionsMapType = object : TypeToken<Map<String, QuestionModel>>() {}.type
        questions = Gson().fromJson(quizData, questionsMapType)
        calculateScore()
        setAnswerView()
        Log.d("Quiz", quizData.toString())
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
      // var score = 0
        for (entry in questions.entries){
            val question = entry.value
            if (question.ans == question.userAns){
                score += 10
            }
        }
        var totalScore = (questions.size)*10
        var txtScore : TextView = findViewById(R.id.txtScore)
        txtScore.text = "Your Score : $score/$totalScore"
        saveScoreToFirebase()

    }

    private fun saveScoreToFirebase() {
        val title = intent.getStringExtra("title")
        Log.d("title", title.toString())

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email
            Log.d("email", userEmail.toString())

            if (userEmail != null && title != null) {
                dbRef = FirebaseDatabase.getInstance().getReference("Leaderboard")
                val scoresRef = dbRef.child(title)
                val index: Int = userEmail.indexOf('@')
                scoresRef.child(userEmail.subSequence(0, index).toString().capitalize()).setValue(score)
            } else {
                Log.e("ResultActivity", "User email or title is null.")
            }
        } else {
            Log.e("ResultActivity", "User is not logged in.")
        }
    }

}
