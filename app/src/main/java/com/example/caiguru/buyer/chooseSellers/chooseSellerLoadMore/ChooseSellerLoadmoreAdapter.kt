package com.example.caiguru.buyer.chooseSellers.chooseSellerLoadMore

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingListActivity
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.choose_sellershopping_list_child_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.*

class ChooseSellerLoadmoreAdapter(var context: Context) :
    RecyclerView.Adapter<ChooseSellerLoadmoreAdapter.Viewholder>() {

    private  var homeDeliveryaddress= DeliveryZoneModel()
    private lateinit var searchtext: String
    var child = ArrayList<CustomerChildModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.load_more_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
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
                "${context.getString(R.string.rupatation)}: " + list.reputation

            if (list.image.isEmpty()) {

                Glide.with(context).load(R.drawable.product_placeholder)
                    .into(holder.itemView.productImage)
            } else {
                Glide.with(context).load(list.image).placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)
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
                "${context.getString(R.string.rupatation)}: " + list.reputation

            if (list.image.isEmpty()) {

                Glide.with(context).load(R.drawable.product_placeholder)
                    .into(holder.itemView.productImage)

            } else {

                Glide.with(context).load(list.image).placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)

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
    fun update(
        chooseSellerLoadMoreModel: java.util.ArrayList<CustomerChildModel>,
        search: String,
        address: DeliveryZoneModel
    ) {
        child = chooseSellerLoadMoreModel
        searchtext = search
        homeDeliveryaddress=address
        notifyDataSetChanged()
    }
    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
