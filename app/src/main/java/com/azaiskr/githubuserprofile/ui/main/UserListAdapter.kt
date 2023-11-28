package com.azaiskr.githubuserprofile.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azaiskr.githubuserprofile.data.remote.response.ItemsItem
import com.azaiskr.githubuserprofile.databinding.UserListItemBinding
import com.bumptech.glide.Glide

class UserListAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<ItemsItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: ItemsItem) {
            binding.userListName.text = user.login.toString()
            binding.userListId.text = user.id.toString()
            Glide.with(binding.root.context)
                .load(user.avatarUrl)
                .into(binding.userListImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bindData(user)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(user.login.toString())
        }
    }

    interface OnItemClickListener {
        fun onItemClick(username: String)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(
                oldItem: ItemsItem,
                newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ItemsItem,
                newItem: ItemsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
