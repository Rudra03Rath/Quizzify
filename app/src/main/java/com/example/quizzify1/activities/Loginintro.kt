package com.example.quizzify1.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App!")
        builder.setMessage("Are you sure you want to leave the App?")
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            super.onBackPressed()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int ->
            // Do nothing, stay in the app and Enjoy
        }
        builder.show()
    }
}