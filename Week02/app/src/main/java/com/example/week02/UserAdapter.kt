package com.example.week02

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week02.databinding.ItemUserBinding

class UserAdapter(private val userList: MutableList<UserData>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    //private val userList = mutableListOf<UserData>()

    fun submitList(list: List<UserData>) {
        userList.clear()
        userList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserData) {
            val fullName = "${user.firstName} ${user.lastName}"
            binding.tvUserName.text = fullName

            Glide.with(binding.root.context)
                .load(user.avatar)
                .circleCrop()
                .into(binding.ivUserAvatar)
        }
    }
}