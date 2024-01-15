package com.example.quizzify1.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.quizzify1.R

class CategoryActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val btn1 : Button = findViewById(R.id.btnMCQ)
        val btn2 : Button = findViewById(R.id.btnTF)

        btn1.setOnClickListener {
            val intent = Intent(this,CreateQuizActivity::class.java)
            startActivity(intent)
        }

        btn2.setOnClickListener {
            val intent = Intent(this,CreateQuizTFActivity::class.java)
            startActivity(intent)
        }
    }
}
