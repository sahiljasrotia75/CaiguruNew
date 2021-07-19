package com.example.caiguru.buyer.buyerLists.buyerShopListModify

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_shop_list_parent_adapter.view.*

class BuyerShopListParentAdapter(var context: Context) :
    RecyclerView.Adapter<BuyerShopListParentAdapter.ViewHolder>() {
    var list: ArrayList<PostShoppingModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.buyer_shop_list_parent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.productname.text = model.name+"${ "("+Constant.convertUnits(context,model.unit)+")"}"
        holder.itemView.quantity.text = model.qty

        if (model.image.isEmpty()) {

            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.img)
        } else {

            Glide.with(context)
                .load(model.image)
                .placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.img)
        }
    }
    //update the adapter
    fun update(it: ArrayList<PostShoppingModel>) {
        list =it
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}