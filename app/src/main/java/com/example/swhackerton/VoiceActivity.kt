package com.example.swhackerton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swhackerton.adapter.MemberAdapter
import com.example.swhackerton.databinding.ActivityVoiceBinding
import com.example.swhackerton.model.Member
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class VoiceActivity : AppCompatActivity() {
    private lateinit var channelId : String
    private val currentMemberDB = Firebase.database.reference.child("Rooms")
    lateinit var binding : ActivityVoiceBinding
    private lateinit var adapter: MemberAdapter
    var memberArray: MutableList<Member> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView( this, R.layout.activity_voice)
        channelId = intent.getStringExtra("channelId").toString()

        currentMemberDB.child(channelId).child("title").get().addOnSuccessListener {
            findViewById<TextView>(R.id.roomTitleTextView).text = it.value.toString()
        }

        memberArray.clear()
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getMemberByKey(snapshot.key.orEmpty())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                println("DEBUT : VOICEACTIVIT ONCHILD")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getMemberByKey(snapshot.key.orEmpty())
                }
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
    }

    private fun initMemberRecyclerView() {
        adapter = MemberAdapter()
        binding.memberRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.memberRecyclerView.adapter = adapter
    }

    private fun getMemberByKey(userId: String) {
        currentMemberDB.child(channelId).child("Members").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                memberArray.add(
                    Member(userId, snapshot.value.toString())
                )
                println("DEBUG : " + snapshot.value.toString())
                adapter.submitList(memberArray)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}