package com.example.swhackerton

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SignUpActivity : AppCompatActivity() {

    private val signUpButton: AppCompatButton by lazy {
        findViewById(R.id.signUpButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initSignUpButton()
    }

    private fun initSignUpButton() {
        signUpButton.setOnClickListener {
            finish()
        }
    }
}