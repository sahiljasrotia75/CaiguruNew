package com.example.caiguru.buyer.postList.shoppingListPosted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.shopping_list_posted_adapter.view.textname
import java.util.ArrayList

class BuyerShopListPostAdapter(
    var context: BuyerShoppingListPostedActivity,
    var arraymodel: ArrayList<ShoppingListModel>
) : RecyclerView.Adapter<BuyerShopListPostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.buyer_shopping_list_posted_adapter, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return arraymodel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = arraymodel[position]
        holder.itemView.textname.text = data.name + " : " + data.qty + " " + Constant.convertUnits(context,data.unit)
//        holder.itemView.textunit.text = " " +
//        holder.itemView.textquantity.text = " " +
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

}
