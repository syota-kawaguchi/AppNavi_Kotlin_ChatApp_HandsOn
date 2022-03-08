package com.example.handsonchatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ChatLogAdapter(private val list: List<ChatLogItem>, private val listener: AdapterUtil.ListListener<ChatLogItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int = if (list[position].isFrom) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout : Int = if (viewType == 0) R.layout.chat_from_row else R.layout.chat_to_row
        val itemView: View = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatLogViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.textview_chat_log).text = list[position].message
        val url = list[position].profileImageUrl
        val targetImageView = holder.itemView.findViewById<ImageView>(R.id.imageView_chat_log)
        if (url != null && url != "") {
            Picasso.get().load(url).into(targetImageView)
        }
        holder.itemView.setOnClickListener {
            listener.onClickItem(it, list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}

class ChatLogViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {
    val chatLog : TextView = itemView.findViewById<TextView>(R.id.textview_chat_log)
    val profileImageUrl : ImageView = itemView.findViewById<ImageView>(R.id.imageView_chat_log)
}

class ChatLogItem(val username: String, val message: String, val profileImageUrl: String, val isFrom: Boolean) {
    constructor() : this("", "", "", false)
}