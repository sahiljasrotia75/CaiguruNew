package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList.CustomerRequestListActivity
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.*

class CustomerListChildAdapter(
    var context: Context

) : RecyclerView.Adapter<CustomerListChildAdapter.ViewHolder>() {

    private var more: Int = 0

    // var cat_id=""
    var child = ArrayList<CustomerChildModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.customer_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (child.size >= 5) {
            return 5
        }
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]

        //set the level  image
        Glide.with(context)
            .load(levelget(list.level).levelImage)
            .placeholder(R.drawable.user_placeholder)
            .into(holder.itemView.imgBatchs)

        holder.itemView.textname.text = list.name

        holder.itemView.reputation.text =
            "${context.getString(R.string.rupatation)}: " + list.reputation

        if (list.image.isEmpty()) {

            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPic)

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
        //set the click on the name to open the profile
        holder.itemView.textname.setOnClickListener {
            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyListUploadedByCustomerProfile", list)
            context.startActivity(intent)
        }
        //set the click on the image to open the profile
        holder.itemView.userPic.setOnClickListener {
            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyListUploadedByCustomerProfile", list)
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

    fun updateData(productsList: CustomerParentModel) {
        //  cat_id=productsList.cat_id
        child = productsList.lists
        more = productsList.more
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}
