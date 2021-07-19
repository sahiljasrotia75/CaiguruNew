package com.example.caiguru.buyer.homeBuyers.commentBuyers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.homeBuyers.tagUser.TagModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.comment_buyer_home_layout.view.*
import kotlinx.android.synthetic.main.comment_buyer_home_layout.view.imgBatchs
import kotlinx.android.synthetic.main.comment_buyer_home_layout.view.textname
import kotlinx.android.synthetic.main.comment_buyer_home_layout.view.userPic
import java.util.ArrayList

class CommentBuyerHomeAdapter(var context: Context) :
    RecyclerView.Adapter<CommentBuyerHomeAdapter.ViewHolder>() {
    var data = ArrayList<CommentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.comment_buyer_home_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]

        holder.itemView.textname.text = model.name
        holder.itemView.Comment.text=    model.comment.substring(0, 1).toUpperCase() +  model.comment.substring(1)
       // holder.itemView.Comment.text = URLDecoder.decode(model.comment, "UTF-8")


        holder.itemView.hoursAgo.text = Constant.timesAgoLogic(model.created_at, context)
        //set user iamge
        if (model.image.isEmpty()) {
            Glide.with(context)
                .load(context.getDrawable(R.drawable.user_placeholder))
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context)
                .load(model.image)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.userPic)
        }
        //set user imgBatchs
        if (model.level.isEmpty()) {
            Glide.with(context)
                .load(context.getDrawable(R.drawable.user_placeholder))
                .into(holder.itemView.imgBatchs)
        } else {
            Glide.with(context)
                .load(levelget(model.level).levelImage)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.imgBatchs)
        }

        //open the profile
        holder.itemView.userPic.setOnClickListener {
            val tagModel=TagModel()
            tagModel.user_id=model.comment_by
            tagModel.name=model.name
            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyBuyerHomeCommentProfile", tagModel)
            context.startActivity(intent)

        }
        holder.itemView.textname.setOnClickListener {
            val tagModel=TagModel()
            tagModel.user_id=model.comment_by
            tagModel.name=model.name
            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyBuyerHomeCommentProfile", tagModel)
            context.startActivity(intent)
        }

    }

    private fun levelget(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(context)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }

    fun update(it: ArrayList<CommentModel>) {
        data = it
        notifyDataSetChanged()

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
