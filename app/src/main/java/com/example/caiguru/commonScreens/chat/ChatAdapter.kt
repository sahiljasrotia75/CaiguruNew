package com.example.caiguru.commonScreens.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.chat_item.view.*

class ChatAdapter(var context: Context) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    var chatList = ArrayList<ModelChat>()
    var listener = context as ChatListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = chatList[position]

        holder.itemView.senderName.text = model.name
        if (model.last_message.isNotEmpty()) {
            holder.itemView.message.text = model.last_message
        } else {
            holder.itemView.message.text = context.getString(R.string.no_message)
        }

        holder.itemView.timeAgo.text = Constant.timesAgoLogic(model.updated_at, context)
        if (model.type == "2") {
            val data = levelget(model.level)
            holder.itemView.imgBatchs.setImageResource(data.levelImage)
        } else {
            val data = levelgetBuyer(model.level)
            holder.itemView.imgBatchs.setImageResource(data.levelImage)
        }
        Glide.with(context).load(model.image).into(holder.itemView.userPic)

        val count = model.msg_count.toInt()
        if (count > 0) {
            holder.itemView.unreadCount.text = model.msg_count
            holder.itemView.unreadCount.visibility = View.VISIBLE
        } else {
            holder.itemView.unreadCount.visibility = View.INVISIBLE
        }

        holder.itemView.setOnClickListener {
            listener.onChatItemClick(model)
        }

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun update(it: List<ModelChat>?) {
        chatList = it as ArrayList<ModelChat>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ChatListener {
        fun onChatItemClick(model: ModelChat)
    }

    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(context)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    private fun levelgetBuyer(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(context)
        for (category in sellerLevel) {
            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }
}
