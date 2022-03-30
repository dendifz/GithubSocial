package com.dicoding.githubsocial.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsocial.data.model.User
import com.dicoding.githubsocial.databinding.ItemRowUserBinding
import com.dicoding.githubsocial.util.AutoUpdatableAdapter
import kotlin.properties.Delegates


class SearchUserAdapter :
    RecyclerView.Adapter<SearchUserAdapter.ListViewHolder>(), AutoUpdatableAdapter {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var items: List<User> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.id == n.id }
    }

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, login, avatar, type) = items[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgItemPhoto)

        holder.binding.apply {
            tvItemUsername.text = StringBuilder("@").append(login)
            tvItemId.text = StringBuilder("ID : ").append(id.toString())
            tvItemType.text = type
            btnLihatProfile.setOnClickListener {
                onItemClickCallback.onItemClicked(items[holder.adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}