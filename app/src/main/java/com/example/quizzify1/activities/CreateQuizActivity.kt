package com.example.quizzify1.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
    private lateinit var etSub: EditText
    private lateinit var etOpt1: EditText
    private lateinit var etOpt2: EditText
    private lateinit var etOpt3: EditText
    private lateinit var etOpt4: EditText
    private lateinit var etAns: EditText
    private lateinit var btn: Button
    private lateinit var btnImg: Button
    private lateinit var img : ImageView
    private var uri : Uri? = null
    private lateinit var storageRef : FirebaseStorage

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createquiz)
        storageRef = FirebaseStorage.getInstance()

        etSub = findViewById(R.id.etSub)
        etQuestion = findViewById(R.id.etQuestion)
        etOpt1 = findViewById(R.id.etOpt1)
        etOpt2 = findViewById(R.id.etOpt2)
        etOpt3 = findViewById(R.id.etOpt3)
        etOpt4 = findViewById(R.id.etOpt4)
        etAns = findViewById(R.id.etAns)
        btn = findViewById(R.id.btn1)
        btnImg = findViewById(R.id.btnImg)
        img = findViewById(R.id.img)



        dbRef = FirebaseDatabase.getInstance().getReference("MCQ Questions")


        btn.setOnClickListener {
            saveQuestion()
        }
        /*btnImg.setOnClickListener {
            val intent = Intent(this,UploadImgActivity::class.java)
            startActivity(intent)
        }*/

        val galleryimg =  registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                img.setImageURI(it)
                if (it != null) {
                    uri= it
                }
            })

        btnImg.setOnClickListener {
            img.visibility = View.VISIBLE
            galleryimg.launch("image/*")
            btnImg.visibility = View.GONE

        }



    }

    private fun saveQuestion() {
        val ques = etQuestion.text.toString()
        val sub = etSub.text.trim().toString().uppercase()
        val opt1 = etOpt1.text.toString()
        val opt2 = etOpt2.text.toString()
        val opt3 = etOpt3.text.toString()
        val opt4 = etOpt4.text.toString()
        val ans = etAns.text.toString()



        if (sub.isEmpty()) {
            etSub.error = " Please Enter The Subject "
        } else if (ques.isEmpty() && uri==null) {
            etQuestion.error = " Please Enter The Question "
        } else if (opt1.isEmpty()) {
            etOpt1.error = " Please Enter Option 1 "
        } else if (opt2.isEmpty()) {
            etOpt2.error = " Please Enter Option 2 "
        } else if (opt3.isEmpty()) {
            etOpt3.error = " Please Enter Option 3 "
        } else if (opt4.isEmpty()) {
            etOpt4.error = " Please Enter Option 4 "
        } else if (ans.isEmpty()) {
            etAns.error = " Please Enter The Correct Answer "
        } else if (uri == null){
            if ((ans == opt1) || (ans == opt2) || (ans == opt3) || (ans == opt4)) {
                val questionId = dbRef.push().key!!

                val question = QuestionModel(
                    questionId,
                    ques,
                    opt1,
                    opt2,
                    opt3,
                    opt4,
                    ans
                )

                dbRef.child(sub).child(questionId).setValue(question)
//                dbRef.child("Chem").child(questionId).setValue(question)
                    .addOnCompleteListener {
                        Toast.makeText(
                            this,
                            "Question is Created",
                            Toast.LENGTH_LONG
                        ).show()

                        etQuestion.text.clear()
                        etSub.text.clear()
                        etOpt1.text.clear()
                        etOpt2.text.clear()
                        etOpt3.text.clear()
                        etOpt4.text.clear()
                        etAns.text.clear()

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                    }
            } else {
                Toast.makeText(
                    this,
                    "Correct Answer is not in the Options",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

        } else{

                storageRef.getReference("Images").child(System.currentTimeMillis().toString())
                    .putFile(uri!!)
                    .addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {
                                val imgUrl = mapOf(
                                    "url" to it.toString()
                                )




                                if ((ans == opt1) || (ans == opt2) || (ans == opt3) || (ans == opt4)) {
                                    val questionId = dbRef.push().key!!

                                    val question = QuestionModel(
                                        questionId,
                                        ques,
                                        opt1,
                                        opt2,
                                        opt3,
                                        opt4,
                                        ans,
                                        imgUrl.toString()
                                    )

                                    dbRef.child(sub).child(questionId).setValue(question)
//                dbRef.child("Chem").child(questionId).setValue(question)
                                        .addOnCompleteListener {
                                            Toast.makeText(
                                                this,
                                                "Question is Created",
                                                Toast.LENGTH_LONG
                                            ).show()

                                            etQuestion.text.clear()
                                            etSub.text.clear()
                                            etOpt1.text.clear()
                                            etOpt2.text.clear()
                                            etOpt3.text.clear()
                                            etOpt4.text.clear()
                                            etAns.text.clear()
                                            img.visibility = View.GONE
                                            btnImg.visibility = View.VISIBLE

                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                                        }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Correct Answer is not in the Options",
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                    }
        }
    }

}

