package com.example.swhackerton

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.Voice
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swhackerton.adapter.RoomAdapter
import com.example.swhackerton.databinding.ActivityMainBinding
import com.example.swhackerton.model.Room
import com.example.swhackerton.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val currentRoomDB = Firebase.database.reference.child("Rooms")
    private val currentUserDB = Firebase.database.reference.child("Users")
    lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RoomAdapter
    var roomArray: MutableList<Room> = arrayListOf()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        roomArray.clear()
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key?.isNotEmpty() == true) {
                    getRoomByKey(snapshot.key.orEmpty())
                }
                println("DEBUG : MainActivity OnChildAdded")
                initRoomRecyclerView()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                println("DEBUG : MainActivity OnChildChanged")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        currentRoomDB.addChildEventListener(childEventListener)

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                search(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                search(newText.toString())
                return true
            }

        })

        initRoomRecyclerView()
        initCreateRoomBtn()
        initOpenMenuBtn()
    }

    private fun initRoomRecyclerView() {
        adapter = RoomAdapter()
        binding.roomRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.roomRecyclerView.adapter = adapter
    }



    private fun initOpenMenuBtn() {
        val openMenuButton = findViewById<AppCompatButton>(R.id.openMenuButton)

        openMenuButton.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_menu, null)

            val emailTextView = view.findViewById<TextView>(R.id.emailTextView)
            val nickNameTextView = view.findViewById<TextView>(R.id.nickNameTextView)

            emailTextView.setText(auth.currentUser?.email.toString())

            val userId = auth.currentUser?.uid.toString()
            currentUserDB.child(userId).child("nickName").get().addOnSuccessListener {
                nickNameTextView.setText(it.value.toString())
            }

            val closeButton = view.findViewById<AppCompatButton>(R.id.closeButton)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            val signOutButton = view.findViewById<AppCompatButton>(R.id.signOutButton)
            signOutButton.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun initCreateRoomBtn() {
        val extendedfab = findViewById<AppCompatButton>(R.id.extended_fab)

        extendedfab.setOnClickListener {
            val dialog = BottomSheetDialog(this)
            lateinit var title: String

            val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            val btnAddTopic = view.findViewById<AppCompatButton>(R.id.addTopicBtn)
            val titleTextView = view.findViewById<TextView>(R.id.titleTextView)

            title = ""
            titleTextView.setText("")

            btnAddTopic.setOnClickListener {
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.alertdialog_edittext, null)
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle("방 제목을 입력해주세요")
                    .setPositiveButton("확인") { dialog, which ->
                        val textView: TextView = view.findViewById(R.id.editText)
                        title = textView.text.toString()
                        titleTextView.setText(title)
                    }
                    .setNeutralButton("취소", null)
                    .create()

                alertDialog.setCancelable(false)
                alertDialog.setView(view)
                alertDialog.show()
            }

            val closeButton = view.findViewById<AppCompatButton>(R.id.closeButton)
            closeButton.setOnClickListener {
                dialog.dismiss()
            }

            val btnCreateRoom = view.findViewById<AppCompatButton>(R.id.idBtnDismiss)
            btnCreateRoom.setOnClickListener {
                if (title == "") {
                    Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val Room = mutableMapOf<String, Any>()
                val Member = mutableMapOf<String, Any>()
                val userId = auth.currentUser?.uid.toString()
                val channelNumber = (1000..1000000).random().toString()

                currentUserDB.child(userId).child("nickName").get().addOnSuccessListener {
                    Room["title"] = title
                    Room["OwnerUid"] = userId
                    Room["OwnerNickname"] = it.value.toString()
                    Member[userId] = it.value.toString()

                    currentRoomDB.child(channelNumber).updateChildren(Room)
                    currentRoomDB.child(channelNumber).child("Members").updateChildren(Member)

                    dialog.dismiss()

                    val intent = Intent(this, VoiceActivity::class.java)
                    intent.putExtra("channelId", channelNumber)
                    startActivity(intent)
                }
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }

    private fun getRoomByKey(roomTitle: String) {
        currentRoomDB.child(roomTitle).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                roomArray.add(
                    Room(
                        snapshot.child("title").value.toString(),
                        snapshot.child("OwnerNickname").value.toString(),
                        snapshot.child("OwnerUid").value.toString(),
                        snapshot.key.toString()
                    )
                )
                adapter.submitList(roomArray)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun search(keyword: String) {
        val searchRooms = roomArray.filter {
            it.title.contains(keyword, true)
        }
        adapter.submitList(searchRooms)
    }
}
