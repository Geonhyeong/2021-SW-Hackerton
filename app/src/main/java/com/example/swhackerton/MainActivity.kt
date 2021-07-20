package com.example.swhackerton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.swhackerton.databinding.ActivityMainBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        initJoinRoom()
        initCreateRoomBtn()
        initSignOutBtn()
    }

    fun initJoinRoom() {
        var joinRoom = findViewById<MaterialCardView>(R.id.card_view01)

        joinRoom.setOnClickListener {
            startActivity(Intent(this, VoiceActivity::class.java))
        }
    }


    fun initCreateRoomBtn() {
        var extendedfab = findViewById<ExtendedFloatingActionButton>(R.id.extended_fab)

        extendedfab.setOnClickListener {
            println("Todo:: Create room")
            Toast.makeText(
                this,
                "Create Room!",
                Toast.LENGTH_SHORT
            ).show()
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