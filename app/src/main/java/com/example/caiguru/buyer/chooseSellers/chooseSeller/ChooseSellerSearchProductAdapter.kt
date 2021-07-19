package com.example.caiguru.buyer.chooseSellers.chooseSeller

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
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.*

class ChooseSellerSearchProductAdapter(
    var context: Context

) : RecyclerView.Adapter<ChooseSellerSearchProductAdapter.ViewHolder>() {

    private  var addressHomeAddres=DeliveryZoneModel()
    private lateinit var searchText: String
    var child = ArrayList<CustomerChildModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.customer_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        if (list.payment_methods.isNotEmpty()){
            holder.itemView.txtCashMethods.visibility = View.VISIBLE
            holder.itemView.txtCashMethods.text=Constant.ModifyCaseOpenListSetPaymentMethods(context,list.payment_methods)
        }
        if (list.delivery_type == "1") {
            holder.itemView.layoutParent.setBackgroundResource(R.color.yellow_light)
            holder.itemView.textname.text = list.name
            Glide.with(context)
                .load(levelget(list.level).levelImage)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.imgBatchs)
            holder.itemView.reputation.text =
                "${context.getString(R.string.rupatation)}: " + Constant.getReputationString(
                    context,
                    list.reputation
                )

            Glide.with(context).load(list.image) .placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)

        }else{
            holder.itemView.layoutParent.setBackgroundResource(R.color.light_purple)
            holder.itemView.textname.text = list.name
            Glide.with(context)
                .load(levelget(list.level).levelImage)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.imgBatchs)

            holder.itemView.reputation.text =
                "${context.getString(R.string.rupatation)}: " + Constant.getReputationString(
                    context,
                    list.reputation
                )
            Glide.with(context).load(list.image).placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)
        }


        //click on item
        holder.itemView.setOnClickListener {
            //click.sendData(list)
            val intent = Intent(context, ChooseSellerShoppingListActivity::class.java)
            intent.putExtra("keymodelData", list)
            intent.putExtra("searchKeymodelData", searchText)
            intent.putExtra("addressHomeAddres", addressHomeAddres)
            context.startActivity(intent)

        }

    }

    fun updateData(
        productsList: ArrayList<CustomerChildModel>,
        search: String,
        lat: String,
        long: String,
        addressdatas: String

        ) {
        addressHomeAddres.lat = lat
        addressHomeAddres.long = long
        addressHomeAddres.address = addressdatas
        searchText = search
        child = productsList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

    fun removeItems() {
        child.clear()
        notifyDataSetChanged()
    }


}
