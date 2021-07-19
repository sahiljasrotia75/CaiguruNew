package com.example.caiguru.buyer.homeBuyers.homeBuyer.viewTagMoreUser

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.homeBuyers.tagUser.TagModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.tag_user_adapter.view.*
import kotlin.collections.ArrayList

class ViewMoreAdapter(var context: Context) :
    RecyclerView.Adapter<ViewMoreAdapter.ViewHolder>() {
    var data = ArrayList<HomeViewMoreModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.view_more_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        holder.itemView.textname.text = model.shared_by_name
        //************set the image
        if (model.shared_by_image.isEmpty()) {
            Glide.with(context)
                .load(context.getDrawable(R.drawable.user_placeholder))
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context)
                .load(model.shared_by_image)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.userPic)
        }
//***************set the batches
        if (model.type=="1"){
            if (model.shared_by_level.isEmpty()) {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.user_placeholder))
                    .into(holder.itemView.imgBatchs)
            } else {
                Glide.with(context)
                    .load(levelgetBuyer(model.shared_by_level).levelImage)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchs)
            }
        }else{
            if (model.shared_by_level.isEmpty()) {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.user_placeholder))
                    .into(holder.itemView.imgBatchs)
            } else {
                Glide.with(context)
                    .load(levelgetSeller(model.shared_by_level).levelImage)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchs)
            }
        }
//        //open the profile
//        holder.itemView.userPic.setOnClickListener {
//            val data = TagModel()
//            data.user_id =model.shared_by
//            data.name =model.shared_by_name
//            data.type =model.type
//            val intent = Intent(context, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyTageUserProfile", data)
//            context.startActivity(intent)
//
//        }
//        holder.itemView.textname.setOnClickListener {
//            val data = TagModel()
//            data.user_id =model.shared_by
//            data.name =model.shared_by_name
//            data.type =model.type
//            val intent = Intent(context, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyTageUserProfile", data)
//            context.startActivity(intent)
//
//        }
        holder.itemView.setOnClickListener {
            if (model.type=="2"){
                val localModel = FinalizeModel()
                localModel.seller_id=model.shared_by
                localModel.cat_id=""
                localModel.seller_name=model.shared_by_name
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                context.startActivity(intent)
            }else{
                val data = TagModel()
                data.user_id =model.shared_by
                data.name =model.shared_by_name
                data.type =model.type
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyTageUserProfile", data)
                context.startActivity(intent)
            }


        }

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
    //get the level of seller image
    private fun levelgetSeller(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(context)
        for (category in sellerLevel) {
            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    fun update(products: ArrayList<HomeViewMoreModel>) {
        data = products
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }


}
