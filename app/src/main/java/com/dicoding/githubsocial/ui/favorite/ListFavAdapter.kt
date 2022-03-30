package com.dicoding.githubsocial.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsocial.data.source.local.entity.FavoriteUserEntity
import com.dicoding.githubsocial.databinding.ItemRowFavoriteBinding
import com.dicoding.githubsocial.util.AutoUpdatableAdapter
import kotlin.properties.Delegates

class ListFavAdapter :
    RecyclerView.Adapter<ListFavAdapter.ListViewHolder>(), AutoUpdatableAdapter {

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var hapusCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
        this.hapusCallback = onItemClickCallback
    }

    var items: List<FavoriteUserEntity> by Delegates.observable(emptyList()) { _, oldList, newList ->
        autoNotify(oldList, newList) { o, n -> o.id == n.id }
    }

    class ListViewHolder(var binding: ItemRowFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            ItemRowFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, name, username, avatar, type, _) = items[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .circleCrop()
            .into(holder.binding.imgItemPhoto)

        holder.binding.apply {
            tvItemName.text = name
            tvItemId.text = StringBuilder("ID : ").append(id.toString())
            tvItemUsername.text = username
            tvItemType.text = type
            tvItemBtnHapus.setOnClickListener {
                hapusCallback.hapusClicked(items[holder.adapterPosition])
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(items[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUserEntity)
        fun hapusClicked(data: FavoriteUserEntity)
    }
}