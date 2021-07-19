package com.example.caiguru.buyer.chooseSellers.chooseSeller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingListActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.choose_sellershopping_list_child_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.*

class ChooseSellerChildAdapter(
    var context: Context,
    var data: CustomerParentModel


) : RecyclerView.Adapter<ChooseSellerChildAdapter.ViewHolder>() {

    var homeDeliveryaddress = DeliveryZoneModel()
    private var searchtext: String = ""
    var child = ArrayList<CustomerChildModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.customer_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (child.size>5){
            5
        }else{
            child.size
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        if (list.payment_methods.isNotEmpty()){
            holder.itemView.txtCashMethods.visibility = View.VISIBLE
            holder.itemView.txtCashMethods.text=Constant.ModifyCaseOpenListSetPaymentMethods(context,list.payment_methods)
        }
        if (list.delivery_type == "1") {

            if (list.distance.isEmpty() || list.distance == "0") {
                holder.itemView.distance.visibility = View.GONE
            } else {
                holder.itemView.distance.text =
                    "(${list.distance} ${context.getString(R.string.aways)})"
                holder.itemView.distance.visibility = View.VISIBLE
            }

            holder.itemView.layoutParent.setBackgroundResource(R.color.yellow_light)
            holder.itemView.textname.text = list.name
            //set the level  image
            Glide.with(context)
                .load(levelget(list.level).levelImage)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.imgBatchs)
            holder.itemView.reputation.text =
                "${context.getString(R.string.rupatation)}:  ${Constant.getReputationString(context,list.reputation)}"
            if (list.image.isEmpty()) {

                Glide.with(context).load(R.drawable.product_placeholder)
                    .into(holder.itemView.productImage)
            } else {
                Glide
                    .with(context)
                    .load(list.image)
                    .placeholder(R.drawable.user_placeholder)
                    .apply(RequestOptions().override(100, 100).centerCrop())
                    .into(holder.itemView.userPic)
//                Glide.with(context).load(list.image).into(holder.itemView.userPic)
            }
        } else {

            if (list.distance.isEmpty() || list.distance == "0") {
                holder.itemView.distance.visibility = View.GONE
            } else {
                holder.itemView.distance.text =
                    "(${list.distance} ${context.getString(R.string.aways)})"
                holder.itemView.distance.visibility = View.VISIBLE
            }
            holder.itemView.textname.text = list.name
            holder.itemView.layoutParent.setBackgroundResource(R.color.light_purple)
            //set the level  image
            Glide.with(context)
                .load(levelget(list.level).levelImage)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.imgBatchs)
            holder.itemView.reputation.text =
                "${context.getString(R.string.rupatation)}:  ${Constant.getReputationString(context,list.reputation)}"

            if (list.image.isEmpty()) {

                Glide.with(context).load(R.drawable.product_placeholder)
                    .into(holder.itemView.productImage)

            } else {

              //  Glide.with(context).load(list.image).into(holder.itemView.userPic)
                Glide
                    .with(context)
                    .load(list.image)
                    .placeholder(R.drawable.product_placeholder)
                    .apply(RequestOptions().override(100, 100).centerCrop())
                    .into(holder.itemView.userPic)

            }

        }

        //click on item
        holder.itemView.setOnClickListener {
            //click.sendData(list)
            val intent = Intent(context, ChooseSellerShoppingListActivity::class.java)
            intent.putExtra("keymodel", list)
            intent.putExtra("searchtext", searchtext)
            intent.putExtra("homeDeliveryaddress", homeDeliveryaddress)
            context.startActivity(intent)
        }

        //set the click on the name to open the profile
        holder.itemView.textname.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=list.seller_id
            localModel.cat_id=list.cat_id
            localModel.seller_name=list.name
            val intent = Intent(context, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            context.startActivity(intent)

//            val intent = Intent(context, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", list)
//            context.startActivity(intent)
        }
        //set the click on the image to open the profile
        holder.itemView.relativeImage.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=list.seller_id
            localModel.cat_id=list.cat_id
            localModel.seller_name=list.name
            val intent = Intent(context, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            context.startActivity(intent)


//            val intent = Intent(context, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", list)
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

    fun updateData(
        productsList: CustomerParentModel,
        search: String,
        homeDeliveryaddres: DeliveryZoneModel
    ) {
        homeDeliveryaddress = homeDeliveryaddres
        searchtext = search
        child = productsList.lists
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }



}
