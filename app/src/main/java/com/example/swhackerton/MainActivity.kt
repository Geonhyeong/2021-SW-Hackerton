package com.example.swhackerton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSignOutBtn()
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun initSignOutBtn() {
        var signOut = findViewById<Button>(R.id.signOut)
        signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}