package com.example.caiguru.buyer.homeBuyers.commentBuyers

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.layout_shopping_list.view.*
import java.util.ArrayList

class ShoppingListHomeBuyerAdapter(var context: Context) :
    RecyclerView.Adapter<ShoppingListHomeBuyerAdapter.ViewHolder>() {
    var data = ArrayList<DialogModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_shopping_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]

        holder.itemView.AllTextSet.setText(
            model.name + "" + "(" + Constant.convertUnits(
                context,
                model.unit
            ) + ")" + " " + ":" + " " + "$" + Constant.roundValue(
                model.priceWithComission.toDouble()
            )
        )
    }

    fun update(
        products: ArrayList<DialogModel>
    ) {
        data = products
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



}
