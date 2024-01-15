package com.example.quizzify1.activities

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
import com.example.quizzify1.models.QuestionModelTF
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CreateQuizTFActivity : AppCompatActivity() {

    private lateinit var etSub: EditText
    private lateinit var etQues: EditText
    private lateinit var etTrue: EditText
    private lateinit var etFalse: EditText
    private lateinit var etAns: EditText
    private lateinit var btn: Button
    private lateinit var btnImg: Button
    private lateinit var img: ImageView
    private var uri: Uri? = null
    private lateinit var storageRef: FirebaseStorage

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createquiz_tf)
        storageRef = FirebaseStorage.getInstance()

        etSub = findViewById(R.id.etSub)
        etQues = findViewById(R.id.etQuestion)
        etTrue = findViewById(R.id.etTrue)
        etFalse = findViewById(R.id.etFalse)
        etAns = findViewById(R.id.etAns)
        btn = findViewById(R.id.btn1)
        btnImg = findViewById(R.id.btnImg)
        img = findViewById(R.id.img)

        dbRef = FirebaseDatabase.getInstance().getReference("TrueOrFalse Questions")

        btn.setOnClickListener {
            saveQuestion()
        }

        val galleryimg = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                img.setImageURI(it)
                if (it != null) {
                    uri = it
                }
            })

        btnImg.setOnClickListener {
            img.visibility = View.VISIBLE
            galleryimg.launch("image/*")
            btnImg.visibility = View.GONE

        }

    }

    private fun saveQuestion() {
        val sub = etSub.text.trim().toString().uppercase()
        val ques = etQues.text.toString()
        val tr = etTrue.text.toString()
        val fls = etFalse.text.toString()
        val ans = etAns.text.toString()

        if (ques.isEmpty() && uri == null) {
            etQues.error = " Please Enter The Question "
        } else if (sub.isEmpty()) {
            etSub.error = " Please Enter The Subject "
        } else if (ans.isEmpty()) {
            etAns.error = " Please Enter The Correct Answer "
        } else if (uri == null) {
            if ((ans == tr) || (ans == fls)) {

                val questionId = dbRef.push().key!!
                val question = QuestionModelTF(questionId, ques, ans)

                dbRef.child(sub).child(questionId).setValue(question)
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
        } else {
            storageRef.getReference("Images").child(System.currentTimeMillis().toString())
                .putFile(uri!!)
                .addOnSuccessListener { task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            val imgUrl = mapOf(
                                "url" to it.toString()
                            )




                            if ((ans == tr) || (ans == fls)) {
                                val questionId = dbRef.push().key!!

                                val question = QuestionModelTF(
                                    questionId,
                                    ques,
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

                                        etQues.text.clear()
                                        etSub.text.clear()
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