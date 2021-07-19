package com.example.caiguru.seller.shoppingListSellers.shoppingListSeller

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails.CloseListActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.SellerPostShoppingListActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.SellerCustomerListActivity
import com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails.SellerOpenListActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.ShoppingListPart1Activity
import kotlinx.android.synthetic.main.recycler_shop_list.view.*

class ShopAdapter(var context: Context, val list: ArrayList<ShopModel>) :
    RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(context).inflate(R.layout.recycler_shop_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelShopList = list[position]

        holder.itemView.ivShop.setImageResource(modelShopList.shopImage)
        holder.itemView.ivNext.setImageResource(modelShopList.shopNext)
        holder.itemView.tvShopList.text = modelShopList.shopText

        holder.itemView.setOnClickListener {
            if (position == 0) {
                val intent = Intent(context, ShoppingListPart1Activity::class.java)
                context.startActivity(intent)
            } else if (position == 1) {
                val intent = Intent(context,SellerCustomerListActivity::class.java)
                context.startActivity(intent)
            } else if (position == 2) {
                val intent = Intent(context, SellerOpenListActivity::class.java)
                context.startActivity(intent)
            }else if (position == 3) {
                val intent = Intent(context, CloseListActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}
