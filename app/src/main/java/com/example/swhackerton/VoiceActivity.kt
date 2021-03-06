package com.example.swhackerton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swhackerton.adapter.MemberAdapter
import com.example.swhackerton.databinding.ActivityVoiceBinding
import com.example.swhackerton.model.Member
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.remotemonster.sdk.*

class VoiceActivity : AppCompatActivity() {
    private lateinit var channelId : String
    private val currentMemberDB = Firebase.database.reference.child("Rooms")
    lateinit var binding : ActivityVoiceBinding
    private lateinit var adapter: MemberAdapter
    var memberArray: MutableList<Member> = arrayListOf()
    private var remonConference = RemonConference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_voice)
        channelId = intent.getStringExtra("channelId").toString()

        currentMemberDB.child(channelId).child("title").get().addOnSuccessListener {
            findViewById<TextView>(R.id.roomTitleTextView).text = it.value.toString()
        }

        var config = Config()
        config.context = this
        config.serviceId = "16a92cfd-5f71-4e3e-98ac-fca7c57330fc"
        config.key = "abcf3d2a9832a908f39d29af40914b5c6d0f86d3a8309b23f8b4961f9c1182d7"

        remonConference.create(channelId, config) {
            participant ->

            participant.localView = null
        }.on("onRoomCreated") {
            participant ->
            participant.tag = 0
        }.on("onUserJoined") {
            participant ->

            participant.on("onComplete") {
                participant ->
            }
        }.on("onUserLeft") {
            participant ->

        }.close {

        }.error {
            error:RemonException ->
        }

        memberArray.clear()
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getMemberByKey(snapshot.key.orEmpty())
                }
                println("DEBUG : VoiceActivity OnChildAdded")
                initMemberRecyclerView()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                println("DEBUG : VoiceActivity ONCHILDChanged")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                println("DEBUG : VoiceActivity OnChildRemoved")
                if (snapshot.key?.isNotEmpty() == true) {
                    removeMemberByKey(snapshot.key.orEmpty())
                }
                initMemberRecyclerView()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        currentMemberDB.child(channelId).child("Members").addChildEventListener(childEventListener)

        initMemberRecyclerView()
        initExitBtn()
    }

    private fun initMemberRecyclerView() {
        adapter = MemberAdapter()
        binding.memberRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.memberRecyclerView.adapter = adapter
    }

    private fun initExitBtn() {
        val btnExit = findViewById<AppCompatButton>(R.id.exitButton)
        btnExit.setOnClickListener {
            finish()
        }
    }

    private fun getMemberByKey(userId: String) {
        currentMemberDB.child(channelId).child("Members").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                memberArray.add(
                    Member(userId, snapshot.value.toString())
                )
                adapter.submitList(memberArray)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun removeMemberByKey(userId: String) {
        currentMemberDB.child(channelId).child("Members").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                memberArray.remove(memberArray.find {
                    it.userId == userId
                })
                adapter.submitList(memberArray)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun finish() {
        remonConference.leave()

        val userId = Firebase.auth.currentUser?.uid
        currentMemberDB.child(channelId).child("Members").child(userId.toString()).removeValue()

        super.finish()
    }
}