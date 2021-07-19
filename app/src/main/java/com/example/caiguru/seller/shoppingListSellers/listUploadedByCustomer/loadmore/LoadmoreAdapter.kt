package com.example.caiguru.buyer.chooseSellers.loadmore

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList.CustomerRequestListActivity
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.choose_sellershopping_list_child_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.*

class LoadmoreAdapter(var context: Context) : RecyclerView.Adapter<LoadmoreAdapter.Viewholder>() {

    var child = ArrayList<CustomerChildModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.load_more_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val list = child[position]
        holder.itemView.textname.text = list.name
        //set the level  image
        Glide.with(context)
            .load(levelget(list.level).levelImage)
            .placeholder(R.drawable.user_placeholder)
            .into(holder.itemView.imgBatchs)


        if (list.image.isEmpty()) {

            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)

        } else {

            Glide.with(context).load(list.image).placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)

        }

        //click on item
        holder.itemView.setOnClickListener {
            //click.sendData(list)
            val intent = Intent(context, CustomerRequestListActivity::class.java)
            intent.putExtra("keymodel", list)
            context.startActivity(intent)

        }

        holder.itemView.reputation.text =
            "${context.getString(R.string.rupatation)}: " + list.reputation
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

    fun update(it: ArrayList<CustomerChildModel>) {
        child = it
        notifyDataSetChanged()

    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
