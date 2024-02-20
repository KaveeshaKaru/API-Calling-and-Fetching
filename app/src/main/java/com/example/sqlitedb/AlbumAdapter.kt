package com.example.sqlitedb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitedb.databinding.ItemAlbumBinding

class AlbumAdapter : RecyclerView.Adapter<AlbumAdapter.ViewHolder>() {

    private var albumList: List<AlbumItem> = emptyList()

    fun setData(newAlbumList: List<AlbumItem>) {
        albumList = newAlbumList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAlbumBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val albumItem = albumList[position]
        holder.bind(albumItem)
    }

    override fun getItemCount(): Int = albumList.size

    class ViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(albumItem: AlbumItem) {
            binding.nameTextView.text = "Name: ${albumItem.name ?: "N/A"}"
            binding.printingNameTextView.text = "Printing Name: ${albumItem.printingName ?: "N/A"}"
            binding.priceTextView.text = "Price: ${albumItem.price ?: "N/A"}"

        }
    }
}