package com.example.swhackerton.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.swhackerton.databinding.ItemMemberBinding
import com.example.swhackerton.model.Member

class MemberAdapter : ListAdapter<Member, MemberAdapter.MemberItemViewHolder>(diffUtil) {
    inner class MemberItemViewHolder(private val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(memberModel: Member) {
            binding.memberNickname.text = memberModel.nickname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
        return MemberItemViewHolder(ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Member>() {
            override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem == newItem
            }

        }
    }

}