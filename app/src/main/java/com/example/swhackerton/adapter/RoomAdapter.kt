package com.example.swhackerton.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swhackerton.VoiceActivity
import com.example.swhackerton.databinding.ItemRoomBinding
import com.example.swhackerton.model.Room

class RoomAdapter: ListAdapter<Room, RoomAdapter.RoomItemViewHolder>(diffUtil) {

    lateinit var context: Context
    inner class RoomItemViewHolder(private val binding: ItemRoomBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(roomModel: Room) {
            binding.roomTitle.text = roomModel.title
            binding.roomOwner.text = "Host : " + roomModel.OwnerNickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomItemViewHolder {
        context = parent.context
        return RoomItemViewHolder(ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
        holder.bind(currentList[position])

        holder.itemView.setOnClickListener {
            openVoiceActivity("WTF")
        }
    }

    fun openVoiceActivity(channelId : String) {
        val i = Intent(context, VoiceActivity::class.java)
        i.putExtra("channelId", channelId)
        ContextCompat.startActivity(context, i, null)
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Room>() {
            override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }
}