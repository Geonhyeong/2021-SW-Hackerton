package com.example.swhackerton.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swhackerton.databinding.ItemRoomBinding
import com.example.swhackerton.model.Room

class RoomAdapter: ListAdapter<Room, RoomAdapter.RoomItemViewHolder>(diffUtil) {

    inner class RoomItemViewHolder(private val binding: ItemRoomBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(roomModel: Room) {
            binding.roomTitle.text = roomModel.title
            binding.roomOwner.text = roomModel.OwnerNickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomItemViewHolder {
        return RoomItemViewHolder(ItemRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RoomItemViewHolder, position: Int) {
        holder.bind(currentList[position])
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