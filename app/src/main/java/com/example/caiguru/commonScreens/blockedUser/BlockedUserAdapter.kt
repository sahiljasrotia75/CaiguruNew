package com.example.caiguru.commonScreens.blockedUser

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.blocked_user_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.reputation
import kotlinx.android.synthetic.main.followers_adapter.view.userPicFollow

class BlockedUserAdapter(
    var context: Context

) : RecyclerView.Adapter<BlockedUserAdapter.ViewHolder>() {

    var child = ArrayList<BlockedUserModel>()
    var click = context as BlockedUserInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.blocked_user_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.reputation.text =
            "${context.getString(R.string.rupatation)}: " + Constant.getReputationString(
                context,
                list.reputation
            )

        if (list.buyer_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPicFollow)
        } else {
            Glide.with(context).load(list.buyer_image).into(holder.itemView.userPicFollow)
        }
        holder.itemView.textname.text = list.buyer_name
//            if (list.seller_level.isEmpty()) {
//
//                Glide.with(context).load(R.drawable.product_placeholder)
//                    .into(holder.itemView.imgBatchsFollow)
//            } else {
//                Glide.with(context)
//                    .load(levelget(list.seller_level).levelImage)
//                    .into(holder.itemView.imgBatchsFollow)
//            }

//set the click on the follow
        holder.itemView.btnUnblock.setOnClickListener {
            click.blockedUserClick(list)
        }


    }
    //get the level of image
//    private fun levelget(level: String): SellerLevelModel {
//        val sellerLevel = Constant.SellerLevel(context)
//        for (category in sellerLevel) {
//            if (category.levelType == level.trim()) {
//                return category
//            }
//        }
//        return SellerLevelModel()
//    }
//
//    //get the level of image
//    private fun levelgetBuyer(level: String): BuyerLevelModel {
//        val sellerLevel = Constant.BuyerLevel(context)
//        for (category in sellerLevel) {
//            if (category.levelType == level.trim()) {
//
//                return category
//            }
//        }
//        return BuyerLevelModel()
//    }


    fun updateData(it: ArrayList<BlockedUserModel>) {
        child = it
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)



    interface BlockedUserInterface {


        fun blockedUserClick(list: BlockedUserModel)
    }


}
