package com.example.quizzify1.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.quizzify1.R
import com.google.firebase.auth.FirebaseAuth

class Loginintro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginintro)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val btn: Button = findViewById(R.id.btnLetsStart)

        btn.setOnClickListener {
            if (auth.currentUser != null) {
                Toast.makeText(this, "User is already logged in!", Toast.LENGTH_SHORT).show()
                redirect(name = "MAIN")
            } else {
                redirect(name = "LOGIN")
            }
        }
    }
    private fun redirect(name:String){
        val intent : Intent = when (name){
            "LOGIN" -> Intent(this, LoginActivity::class.java)
            "MAIN" -> Intent(this, ChooseActivity::class.java)
            else -> throw Exception("No path exists")
        }
        startActivity(intent)
        finish()
    }
}