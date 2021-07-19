package com.example.caiguru.buyer.homeBuyers.tagUser

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
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.tag_user_adapter.view.*
import kotlin.collections.ArrayList

class TagUserAdapter(var context: Context, var listner: selectUnSelectInterface) :
    RecyclerView.Adapter<TagUserAdapter.ViewHolder>() {
    var data = ArrayList<TagModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.tag_user_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        holder.itemView.textname.text = model.name
        //************set the image
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
//***************set the batches
        if (model.type=="1"){
            if (model.buyer_level.isEmpty()) {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.user_placeholder))
                    .into(holder.itemView.imgBatchs)
            } else {
                Glide.with(context)
                    .load(levelgetBuyer(model.buyer_level).levelImage)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchs)
            }
        }else{
            if (model.buyer_level.isEmpty()) {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.user_placeholder))
                    .into(holder.itemView.imgBatchs)
            } else {
                Glide.with(context)
                    .load(levelgetSeller(model.buyer_level).levelImage)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchs)
            }
        }


        //set the click on the tag button
        holder.itemView.TagButton.setOnClickListener {
            data[position].is_tagged = "1"
            holder.itemView.TagButton.visibility = View.GONE
            holder.itemView.unTagButton.visibility = View.VISIBLE
            //show the loader while tag or tagged
            data[position].hasTagged = true
            listner.getSelectUnselectStatus(data[position], "Tag", position)
            notifyDataSetChanged()
        }
        //set the click on the ontagged button
        holder.itemView.unTagButton.setOnClickListener {
            data[position].is_tagged = "0"
            holder.itemView.TagButton.visibility = View.VISIBLE
            holder.itemView.unTagButton.visibility = View.GONE
            //show the loader while tag or tagged
            data[position].hasTagged = true
            listner.getSelectUnselectStatus(data[position], "UnTag", position)
            notifyDataSetChanged()
        }
        //show the loader while tag or tagged
        if (model.hasTagged) {
            holder.itemView.TagUnTagProgressBar.visibility = View.VISIBLE
            holder.itemView.TagButton.visibility = View.GONE
            holder.itemView.unTagButton.visibility = View.GONE
        } else {
            if (model.is_tagged == "0") {
                holder.itemView.TagUnTagProgressBar.visibility = View.GONE
                holder.itemView.TagButton.visibility = View.VISIBLE
                holder.itemView.unTagButton.visibility = View.GONE
            } else {
                holder.itemView.TagUnTagProgressBar.visibility = View.GONE
                holder.itemView.TagButton.visibility = View.GONE
                holder.itemView.unTagButton.visibility = View.VISIBLE
            }
        }
        //open the profile
        holder.itemView.userPic.setOnClickListener {
            if (model.type=="2"){
                val localModel = FinalizeModel()
                localModel.seller_id=model.user_id
                localModel.cat_id=""
                localModel.seller_name=model.name
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                context.startActivity(intent)
            }else{
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyTageUserProfile", model)
                context.startActivity(intent)
            }

        }
        holder.itemView.textname.setOnClickListener {
            if (model.type=="2"){
                val localModel = FinalizeModel()
                localModel.seller_id=model.user_id
                localModel.cat_id=""
                localModel.seller_name=model.name
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                context.startActivity(intent)
            }else{
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyTageUserProfile", model)
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

    fun update(products: ArrayList<TagModel>) {
        data = products
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface selectUnSelectInterface {

        fun getSelectUnselectStatus(
            data: TagModel,
            action: String,
            position: Int
        )
    }
}
