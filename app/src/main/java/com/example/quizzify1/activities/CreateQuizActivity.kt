package com.example.quizzify1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzify1.R
import com.example.quizzify1.models.QuestionModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var etQuestion: EditText
    private lateinit var etOpt1: EditText
    private lateinit var etOpt2: EditText
    private lateinit var etOpt3: EditText
    private lateinit var etOpt4: EditText
    private lateinit var etAns: EditText
    private lateinit var btn: Button


    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createquiz)

        etQuestion = findViewById(R.id.etQuestion)
        etOpt1 = findViewById(R.id.etOpt1)
        etOpt2 = findViewById(R.id.etOpt2)
        etOpt3 = findViewById(R.id.etOpt3)
        etOpt4 = findViewById(R.id.etOpt4)
        etAns = findViewById(R.id.etAns)
        btn = findViewById(R.id.btn1)


        dbRef = FirebaseDatabase.getInstance().getReference("Questions")


        btn.setOnClickListener {
            saveQuestion()
        }
    }

    private fun saveQuestion() {
        val ques = etQuestion.text.toString()
        val opt1 = etOpt1.text.toString()
        val opt2 = etOpt2.text.toString()
        val opt3 = etOpt3.text.toString()
        val opt4 = etOpt4.text.toString()
        val ans = etAns.text.toString()

        if (ques.isEmpty()) {
            etQuestion.error = " Please Enter The Question "
        } else if (opt1.isEmpty()) {
            etQuestion.error = " Please Enter Option 1 "
        } else if (opt2.isEmpty()) {
            etQuestion.error = " Please Enter Option 2 "
        } else if (opt3.isEmpty()) {
            etQuestion.error = " Please Enter Option 3 "
        } else if (opt4.isEmpty()) {
            etQuestion.error = " Please Enter Option 4 "
        } else if (ans.isEmpty()) {
            etQuestion.error = " Please Enter The Correct Answer "
        } else {
            val questionId = dbRef.push().key!!

            val question = QuestionModel(questionId, ques, opt1, opt2, opt3, opt4, ans)

            dbRef.child(questionId).setValue(question)
                .addOnCompleteListener {
                    Toast.makeText(this, "Question is Created", Toast.LENGTH_LONG).show()

                    etQuestion.text.clear()
                    etOpt1.text.clear()
                    etOpt2.text.clear()
                    etOpt3.text.clear()
                    etOpt4.text.clear()
                    etAns.text.clear()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                }
        }

    }
}
