package com.example.quizzify1.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzify1.R
import com.example.quizzify1.models.QuestionModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateQuizActivity : AppCompatActivity() {

    private lateinit var etQuestion: EditText
    private lateinit var optionContainer: LinearLayout
    private lateinit var etAns: EditText
    private lateinit var btn: Button
    private lateinit var btnImg: Button
    private lateinit var img: ImageView
    private var uri: Uri? = null
    private lateinit var storageRef: FirebaseStorage

    private lateinit var dbRef: DatabaseReference
    private lateinit var radioGroupSubject: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createquiz)
        storageRef = FirebaseStorage.getInstance()

        etQuestion = findViewById(R.id.etQuestion)
        optionContainer = findViewById(R.id.optionsContainer)
        etAns = findViewById(R.id.etAns)
        btn = findViewById(R.id.btn1)
        btnImg = findViewById(R.id.btnImg)
        img = findViewById(R.id.img)
        radioGroupSubject = findViewById(R.id.radioGroupSubject)

        dbRef = FirebaseDatabase.getInstance().getReference("Questions")

        findViewById<Button>(R.id.btnAddOption).setOnClickListener {
            addOptionEditText()
        }

        val galleryimg = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                img.setImageURI(it)
                if (it != null) {
                    uri = it
                }
            })

        btnImg.setOnClickListener {
            img.visibility = View.VISIBLE
            galleryimg.launch("image/*")
        }

        btn.setOnClickListener {
            saveQuestion()
        }
    }

    private fun addOptionEditText() {
        val newEditText = EditText(this)
        newEditText.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        newEditText.hint = "New Option"
        optionContainer.addView(newEditText)
    }

    private fun saveQuestion() {
        val ques = etQuestion.text.toString()
        val selectedSubjectId = radioGroupSubject.checkedRadioButtonId
        val selectedSubject = findViewById<RadioButton>(selectedSubjectId)?.text.toString().uppercase()

        if (selectedSubject.isEmpty()) {
            Toast.makeText(this, "Please select a subject", Toast.LENGTH_LONG).show()
            return
        }
        val sub = selectedSubject.trim()
        val ans = etAns.text.toString()

        val options = mutableListOf<String>()
        for (i in 0 until optionContainer.childCount) {
            val optionEditText = optionContainer.getChildAt(i) as EditText
            val optionText = optionEditText.text.toString().trim()
            if (optionText.isNotEmpty()) {
                options.add(optionText)
            }
        }

        if (sub.isEmpty() || sub=="NULL") {
            Toast.makeText(this, "Please select a subject", Toast.LENGTH_SHORT).show()
        } else if (ques.isEmpty() && uri == null) {
            etQuestion.error = "Please Enter The Question"
        } else if (options.size < 2) {
            Toast.makeText(this, "Please Enter at least two options", Toast.LENGTH_SHORT).show()
        } else if (ans.isEmpty()) {
            etAns.error = "Please Enter The Correct Answer"
        } else if (uri == null) {
            if (options.contains(ans)) {
                val questionId = dbRef.push().key!!
                val question = QuestionModel(questionId, ques, options, ans)
                dbRef.child(sub).child(questionId).setValue(question)
                    .addOnCompleteListener {
                        Toast.makeText(this, "Question is Created", Toast.LENGTH_SHORT).show()
                        val refresh = Intent(this, CreateQuizActivity::class.java)
                        startActivity(refresh)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Correct Answer is not in the Options", Toast.LENGTH_SHORT).show()
            }
        } else {
            storageRef.getReference("Images").child(System.currentTimeMillis().toString())
                .putFile(uri!!)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val imgUrl = it.toString()
                            if (options.contains(ans)) {
                                val questionId = dbRef.push().key!!
                                val question = QuestionModel(questionId, ques, options, ans, imgUrl)
                                dbRef.child(sub).child(questionId).setValue(question)
                                    .addOnCompleteListener {
                                        Toast.makeText(this, "Question is Created", Toast.LENGTH_SHORT).show()
                                        val refresh = Intent(this, CreateQuizActivity::class.java)
                                        startActivity(refresh)
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Correct Answer is not in the Options", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
        }
    }

}
