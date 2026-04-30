package com.example.nike

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nike.databinding.ItemFollowingBinding

class FollowingAdapter(private val userList: List<UserData>) :
    RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    // 1. ViewHolder: 개별 아이템 뷰를 잡고 있는 보관함
    inner class FollowingViewHolder(private val binding: ItemFollowingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserData) {
            // Glide를 사용하여 서버 이미지 URL을 이미지뷰에 로드
            Glide.with(binding.root.context)
                .load(user.avatar) // 서버에서 준 이미지 주소
                .into(binding.ivFollowingProfile)
        }
    }

    // 2. onCreateViewHolder: 아이템 레이아웃(XML)을 가져와서 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ItemFollowingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowingViewHolder(binding)
    }

    // 3. onBindViewHolder: 생성된 뷰홀더에 실제 데이터를 연결
    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    // 4. getItemCount: 전체 아이템이 몇 개인지 알려줌
    override fun getItemCount(): Int = userList.size
}