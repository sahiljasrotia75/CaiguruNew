package com.example.caiguru.buyer.buyerProfile.follower

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.ReviewsModel
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.reputation
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.textname
import kotlinx.android.synthetic.main.followers_adapter.view.*

class FollowerBuyerAdapter(
    var context: Context

) : RecyclerView.Adapter<FollowerBuyerAdapter.ViewHolder>() {

    var child = ArrayList<FollowUnfollowModel>()
    private var isFollowed: String = ""
    var click = context as followUnfollowInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.followers_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        // type 1=buyer type 2=seller
        if (list.type == "1") {
            holder.itemView.textname.text = list.name
            //set the level  image
            holder.itemView.reputation.text =
                "${context.getString(R.string.rupatation)}: " + Constant.getReputationString(
                    context,
                    list.reputation
                )

            if (list.buyer_level.isEmpty()) {

                Glide.with(context).load(R.drawable.product_placeholder)
                    .into(holder.itemView.imgBatchsFollow)
            } else {
                Glide.with(context)
                    .load(levelgetBuyer(list.buyer_level).levelImage)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchsFollow)
            }

            if (list.image.isEmpty()) {
                Glide.with(context).load(R.drawable.user_placeholder)
                    .into(holder.itemView.userPicFollow)
            } else {
                Glide.with(context).load(list.image).placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.userPicFollow)
            }

        } else {
            holder.itemView.textname.text = list.name
            //set the level  image
            holder.itemView.reputation.text =
                "${context.getString(R.string.rupatation)}: " + Constant.getReputationString(
                    context,
                    list.reputation
                )

            if (list.seller_level.isEmpty()) {

                Glide.with(context).load(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchsFollow)
            } else {
                Glide.with(context)
                    .load(levelget(list.seller_level).levelImage)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchsFollow)
            }

            if (list.image.isEmpty()) {
                Glide.with(context).load(R.drawable.user_placeholder)
                    .into(holder.itemView.userPicFollow)
            } else {
                Glide.with(context).load(list.image).placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.userPicFollow)
            }
        }


        //follow
        if (list.isFollowed == "1") {
            //hasFollow = falseGONE
            holder.itemView.follow.visibility = View.INVISIBLE
            holder.itemView.btnunFollow.visibility = View.VISIBLE

        } else {
            holder.itemView.follow.visibility = View.VISIBLE
            holder.itemView.btnunFollow.visibility = View.INVISIBLE
        }
        //set the click on the follow
        holder.itemView.follow.setOnClickListener {
            isFollowed = "1"
            click.followClick(position, list, isFollowed)
        }
        //unfollow click
        holder.itemView.btnunFollow.setOnClickListener {
            isFollowed = "2"
            click.unfollowClick(position, list, isFollowed)
        }

        //set the click on the image to open the profile
        holder.itemView.userPicFollow.setOnClickListener {
            if (list.type == "2") {
                val localModel = FinalizeModel()
                localModel.seller_id = list.user_id
                localModel.cat_id = ""
                localModel.seller_name = list.name
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore", localModel)
                context.startActivity(intent)
            } else {
                val model = ReviewsModel()
                model.name = list.name
                model.user_id = list.user_id
                model.type = list.type
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyHomeCommentProfile", model)
                context.startActivity(intent)
            }
        }
    }

    //get the level of image
    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(context)
        for (category in sellerLevel) {
            if (category.levelType == level.trim()) {
                return category
            }
        }
        return SellerLevelModel()
    }

    //get the level of image
    private fun levelgetBuyer(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(context)
        for (category in sellerLevel) {
            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }


    fun updateData(it: ArrayList<FollowUnfollowModel>) {
        child = it
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    interface followUnfollowInterface {
        fun followClick(
            position: Int,
            list: FollowUnfollowModel,
            followed: String
        )

        fun unfollowClick(
            position: Int,
            list: FollowUnfollowModel,
            followed: String
        )
    }


}
