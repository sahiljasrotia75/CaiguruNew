package com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList

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
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.buyer_open_shopping_list_adapter.view.*
import kotlinx.android.synthetic.main.choose_sellershopping_list_child_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.textname
import kotlin.math.roundToInt

class BuyerOpenShoppingListAdapter(
    var context: Context

) : RecyclerView.Adapter<BuyerOpenShoppingListAdapter.ViewHolder>() {

    var child = ArrayList<BuyerShopOpenModel>()
    var click = context as openList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.buyer_open_shopping_list_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.textname.text = list.seller_name
        holder.itemView.bestquote.text = "$" + Constant.roundValue(list.amount.toDouble())
        try {
            holder.itemView.commission.text = (list.credits.toDouble().roundToInt()).toString()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.itemView.reputationbuyer.text =
            "${context.getString(R.string.reputation)} " + Constant.getReputationString(
                context,
                list.reputation
            )

        //set the level  image
        Glide.with(context)
            .load(levelget(list.seller_level).levelImage)
            .placeholder(R.drawable.user_placeholder)
            .into(holder.itemView.imgBatchs)


        // holder.itemView.reputation.text = "${context.getString(R.string.rupatation)}: " + list.reputation

        if (list.seller_image.isEmpty()) {

            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide.with(context).load(list.seller_image) .placeholder(R.drawable.product_placeholder).into(holder.itemView.imgs)
        }


        //click on item
        holder.itemView.setOnClickListener {
            click.open(list)
//            //click.sendData(list)
//            val intent = Intent(context, BuyerShopListApproveRejectActivity::class.java)
//            intent.putExtra("keymodel", list)
//            context.startActivity(intent)

        }
        holder.itemView.imgs.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=list.seller_id
            localModel.cat_id=""
            localModel.seller_name=list.seller_name
            val intent = Intent(context, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            context.startActivity(intent)

//            val modelProfile=CustomerChildModel()
//            modelProfile.seller_id=list.seller_id
//            modelProfile.name=list.seller_name
//            val intent = Intent(context, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", modelProfile)
//            context.startActivity(intent)
        }
        holder.itemView.textname.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=list.seller_id
            localModel.cat_id=""
            localModel.seller_name=list.seller_name
            val intent = Intent(context, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            context.startActivity(intent)




//            val modelProfile=CustomerChildModel()
//            modelProfile.seller_id=list.seller_id
//            modelProfile.name=list.seller_name
//            val intent = Intent(context, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", modelProfile)
//            context.startActivity(intent)
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


    fun update(it: java.util.ArrayList<BuyerShopOpenModel>) {
        child = it
        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}

interface openList {
    fun open(list: BuyerShopOpenModel)
}