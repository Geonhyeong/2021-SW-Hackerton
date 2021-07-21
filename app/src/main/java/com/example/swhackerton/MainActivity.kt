package com.example.swhackerton

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.swhackerton.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val currentRoomDB = Firebase.database.reference.child("Rooms")
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
            val dialog = BottomSheetDialog(this)
            lateinit var title:String

            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val btnAddTopic = view.findViewById<AppCompatButton>(R.id.addTopicBtn)
            btnAddTopic.setOnClickListener {
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.alertdialog_edittext, null)

                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("title을 입력해주세요")
                    .setPositiveButton("확인") { dialog, which ->
                        val textView: TextView = view.findViewById(R.id.editText)
                        title = textView.text.toString()
                    }
                    .setNeutralButton("취소", null)
                    .create()

                alertDialog.setCancelable(false)
                alertDialog.setView(view)
                alertDialog.show()
            }

            val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
            btnClose.setOnClickListener {
                var Room = mutableMapOf<String, Any>()
                Room["title"] = title
                Room["Owner"] = auth.currentUser?.uid.toString()

                currentRoomDB.child(title).updateChildren(Room)

                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    fun initSignOutBtn() {
        var signOut = findViewById<Button>(R.id.signOut)
        signOut.setOnClickListener {
            auth.signOut()
            finish()
        }
    }
}