package com.example.swhackerton

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val signUpButton: AppCompatButton by lazy {
        findViewById(R.id.signUpButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth

        initSignUpButton()
    }

    // 회원가입 시스템
    private fun initSignUpButton() {
        signUpButton.setOnClickListener {
            val email = getInputEmail()
            val password = getInputPassword()
            val stuNum = getInputStuNum()
            val name = getInputName()
            val nickName = getInputNickName()
            val sex = getInputSex()
            val gameCheckBox = getInputGame()
            val eatCheckBox = getInputEat()
            val studyCheckBox = getInputStudy()

            if (email.isNotEmpty() && password.isNotEmpty() && stuNum.isNotEmpty() && name.isNotEmpty() && nickName.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val userId = task.result?.user?.uid.orEmpty()
                            val currentUserDB =
                                Firebase.database.reference.child("Users").child(userId)
                            val user = mutableMapOf<String, Any>()
                            val category = mutableMapOf<String, Any>()

                            user["userId"] = userId
                            user["stuNum"] = stuNum
                            user["name"] = name
                            user["nickName"] = nickName
                            user["sex"] = sex

                            category["game"] = gameCheckBox
                            category["eat"] = eatCheckBox
                            category["study"] = studyCheckBox

                            currentUserDB.updateChildren(user)
                            currentUserDB.child("category").updateChildren(category)

                            auth.signOut()

                            if (auth.currentUser == null) {
                                Toast.makeText(
                                    this,
                                    "회원가입에 성공했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "이미 가입한 이메일이거나, 잘못된 이메일 및 패스워드 형식입니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                android.widget.Toast.makeText(
                    this,
                    "비어 있는 항목이 있습니다.",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getInputEmail(): String {
        return findViewById<EditText>(R.id.emailEditText).text.toString()
    }

    private fun getInputPassword(): String {
        return findViewById<EditText>(R.id.passwordEditText).text.toString()
    }

    private fun getInputStuNum(): String {
        return findViewById<EditText>(R.id.stuNumEditText).text.toString()
    }

    private fun getInputName(): String {
        return findViewById<EditText>(R.id.nameEditText).text.toString()
    }

    private fun getInputNickName(): String {
        return findViewById<EditText>(R.id.nickNameEditText).text.toString()
    }

    private fun getInputSex(): String {
        if(findViewById<RadioButton>(R.id.maleRadioButton).isChecked) {
            return "남"
        }
        return "여"
    }

    private fun getInputGame(): Boolean {
        if(findViewById<CheckBox>(R.id.gameCheckBox).isChecked)
            return true
        return false
    }

    private fun getInputEat(): Boolean {
        if(findViewById<CheckBox>(R.id.eatCheckBox).isChecked)
            return true
        return false
    }

    private fun getInputStudy(): Boolean {
        if(findViewById<CheckBox>(R.id.studyCheckBox).isChecked)
            return true
        return false
    }

}