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
import kotlinx.android.synthetic.main.connection_item.view.*

class ConnectionsAdapter(var context: Context) :
    RecyclerView.Adapter<ConnectionsAdapter.ViewHolder>() {
    var connectionsList = ArrayList<ModelChat>()
    var listener = context as ChatAdapter.ChatListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.connection_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = connectionsList[position]

        holder.itemView.userName.text = model.name
        Glide.with(context).load(model.image).into(holder.itemView.profilePic)
        if (model.type == "2") {
            val data = levelget(model.level)
            holder.itemView.userTag.setImageResource(data.levelImage)
        } else {
            val data = levelgetBuyer(model.level)
            holder.itemView.userTag.setImageResource(data.levelImage)
        }
        holder.itemView.txtListName.text = model.listingname
        holder.itemView.setOnClickListener {
            listener.onChatItemClick(model)
        }
    }

    override fun getItemCount(): Int {
        return connectionsList.size
    }

    fun update(it: List<ModelChat>?) {
        connectionsList = it as ArrayList<ModelChat>
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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