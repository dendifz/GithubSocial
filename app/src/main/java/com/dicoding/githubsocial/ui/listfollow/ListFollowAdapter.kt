package com.dicoding.githubsocial.ui.listfollow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsocial.data.model.ListUser
import com.dicoding.githubsocial.databinding.UserListBinding
import com.dicoding.githubsocial.util.AutoUpdatableAdapter
import kotlin.properties.Delegates

class ListFollowAdapter :
    RecyclerView.Adapter<ListFollowAdapter.ListViewHolder>(), AutoUpdatableAdapter {

    var items: List<ListUser> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.id == n.id }
    }

    class ListViewHolder(var binding: UserListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatar, id, type) = items[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgUserPhoto)

        holder.binding.apply {
            tvUserUsername.text = username
            tvUserId.text = id.toString()
            tvUserType.text = type
        }
    }

    override fun getItemCount(): Int = items.size
}