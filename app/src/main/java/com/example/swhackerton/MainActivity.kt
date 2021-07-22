package com.example.swhackerton

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
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
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
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

        //initUserData()
        initRoomRecyclerView()
        //initJoinRoom()
        initCreateRoomBtn()
        initOpenMenuBtn()
    }

    private fun initUserData() {
        val userId = auth.currentUser?.uid.toString()
        currentUserDB.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user = User(
                    snapshot.child("name").value.toString(),
                    snapshot.child("nickName").value.toString(),
                    snapshot.child("sex").value.toString(),
                    snapshot.child("stuNum").value.toString(),
                    userId
                )
                println("USER : " + user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        println("USER : " + user)
    }

    private fun initRoomRecyclerView() {
        adapter = RoomAdapter()
        binding.roomRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.roomRecyclerView.adapter = adapter
    }


    private fun initJoinRoom() {
        val joinRoom = findViewById<MaterialCardView>(R.id.card_view)

        joinRoom.setOnClickListener {
            startActivity(Intent(this, VoiceActivity::class.java))
        }
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
            currentUserDB.child(userId).get().addOnSuccessListener {
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

            val btnClose = view.findViewById<AppCompatButton>(R.id.idBtnDismiss)
            btnClose.setOnClickListener {
                if (title == "") {
                    Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val Room = mutableMapOf<String, Any>()

                Room["title"] = title
                Room["OwnerNickname"] = auth.currentUser?.email.toString()
                Room["OwnerUid"] = auth.currentUser?.uid.toString()

                currentRoomDB.child(title).updateChildren(Room)
                dialog.dismiss()
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
                        snapshot.child("OwnerUid").value.toString()
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
