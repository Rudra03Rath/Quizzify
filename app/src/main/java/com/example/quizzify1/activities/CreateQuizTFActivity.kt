package com.example.quizzify1.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzify1.R
import com.example.quizzify1.models.QuestionModel
import com.example.quizzify1.models.QuestionModelTF
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateQuizTFActivity : AppCompatActivity() {

    private lateinit var etSub: EditText
    private lateinit var etQues: EditText
    private lateinit var etTrue: EditText
    private lateinit var etFalse: EditText
    private lateinit var etAns: EditText
    private lateinit var btn: Button

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createquiz_tf)

        etSub = findViewById(R.id.etSub)
        etQues = findViewById(R.id.etQuestion)
        etTrue = findViewById(R.id.etTrue)
        etFalse = findViewById(R.id.etFalse)
        etAns = findViewById(R.id.etAns)
        btn = findViewById(R.id.btn1)

        dbRef = FirebaseDatabase.getInstance().getReference("TrueOrFalse Questions")

        btn.setOnClickListener {
            saveQuestion()
        }

    }

    private fun saveQuestion() {
        val sub = etSub.text.toString()
        val ques = etQues.text.toString()
        val tr = etTrue.text.toString()
        val fls = etFalse.text.toString()
        val ans = etAns.text.toString()

        if (ques.isEmpty()) {
            etQues.error = " Please Enter The Question "
        } else if (sub.isEmpty()) {
            etSub.error = " Please Enter The Subject "
        } else if (ans.isEmpty()) {
            etAns.error = " Please Enter The Correct Answer "
        } else {
            if ((ans == tr) || (ans == fls)) {

                val questionId = dbRef.push().key!!
                val question = QuestionModelTF(questionId, ques, ans)

                dbRef.child(sub).setValue(question)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Question is Created", Toast.LENGTH_LONG).show()

                        etQues.text.clear()
                        etSub.text.clear()
                        etAns.text.clear()

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(this, "Please Enter Valid Answer", Toast.LENGTH_LONG).show()
            }
        }
    }
}