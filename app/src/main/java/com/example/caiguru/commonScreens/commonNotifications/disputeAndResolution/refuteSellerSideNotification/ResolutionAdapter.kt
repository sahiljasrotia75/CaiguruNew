package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.shopping_list_posted_adapter.view.*

class ResolutionAdapter(
    var context: Context

) : RecyclerView.Adapter<ResolutionAdapter.ViewHolder>() {

    private  var comissionPers: String=""
    var child = ArrayList<ShoppingListModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.shopping_list_posted_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.textname.text = list.name + " "
        holder.itemView.textunit.text =
            "(" + Constant.convertUnits(context, list.unit) + ")" + " " + ":" + " "
//seller side qty compulsory list uploaded by customer
        //buyer side price compulsary choose seller
        if (list.price.isEmpty() || list.price.toFloat().toInt()<=0) {
            holder.itemView.textprice.text ="${context.getText(R.string.NA)}"
        }  else {
            holder.itemView.textprice.text = "$" +Constant.roundValue(list.priceWithComission.toDouble() )+ " " + "X" + " " + list.qty
//            holder.itemView.textprice.text =
//                "$" + Constant.roundValue(list.price.toDouble()) + " " + "X" + " " + list.qty
        }
    }

    fun update(
        it: ArrayList<ShoppingListModel>,
        comissionPer: String
    ) {
        comissionPers=comissionPer
        child = it
        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}