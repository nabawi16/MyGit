package com.nabawi.mygit.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nabawi.mygit.data.model.UserEntity
import com.nabawi.mygit.databinding.ItemUserBinding

class UsersAdapter : ListAdapter<UserEntity, UsersAdapter.UserViewHolder>(UserDiffCallback()) {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userEntity: UserEntity) {
            binding.apply {
                root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(userEntity)
                }
                tvItemUsername.text = userEntity.username
                tvItemUserUrl.text = userEntity.htmlUrl
                Glide.with(itemView)
                    .load(userEntity.avatarUrl)
                    .centerCrop()
                    .into(ivItemProfile)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}
