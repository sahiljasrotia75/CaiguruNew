package com.example.caiguru.seller.sellerOrder.orderCompleted

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.shopping_list_posted_adapter.view.*
import java.lang.Exception
import java.util.ArrayList

class OrderCompletedAdapter(
    var context: OrderCompletedActivity
) : RecyclerView.Adapter<OrderCompletedAdapter.Viewholder>() {
    var arraymodel = ArrayList<ShoppingListModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.shopping_list_posted_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return arraymodel.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = arraymodel[position]
        holder.itemView.textname.text = data.name + " "
        holder.itemView.textunit.text = "(" + Constant.convertUnits(context,data.unit) + ")" + " "
        try {
            if (data.price.isEmpty() || (data.price).toFloat().toInt()<=0) {
                holder.itemView.textprice.text = ":" + " " + context.getString(R.string.NA)
            }
            else {
                holder.itemView.textprice.text =
                    ":" + " " + "$" + Constant.roundValue(data.priceWithComission.toDouble()) + " " + "X" + " " + data.qty

            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun update(model: ArrayList<OrderListModel>) {
        for (item in model) {
            arraymodel = item.products
        }
        notifyDataSetChanged()

    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view)



}
