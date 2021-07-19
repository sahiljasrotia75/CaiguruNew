package com.example.caiguru.seller.shoppingListSellers.shoppingListPosted

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.layout_shopping_list_price_with_comission.view.*

class ShoppingListPostedAdapter(
    var context: ShoppingListPostedActivity, var arraymodel: ArrayList<ShoppingListModel>
) : RecyclerView.Adapter<ShoppingListPostedAdapter.Viewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.layout_shopping_list_price_with_comission, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return arraymodel.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = arraymodel[position]
     //   val price = Constant.roundValue(data.price.toDouble())
//        holder.itemView.AllTextSet.text=data.name + " "+"(" + Constant.convertUnits(context, data.unit) + ")" + " "+":" + " " + "$" + price
//        holder.itemView.textname.text = data.name + " "
//        holder.itemView.textunit.text = "(" + Constant.convertUnits(context, data.unit) + ")" + " "
//        holder.itemView.textprice.text = ":" + " " + "$" + price
        holder.itemView.txtUnits.text =
            data.name + "(" + Constant.convertUnits(context, data.unit) + ")"

        holder.itemView.txtPrices.text =
            context.getString(R.string.Price) + ":" + " " + "$" + (Constant.roundValue(data.price.toDouble()))

        holder.itemView.txtPriceWithComissions.text =
            context.getString(R.string.Price_with_comission_Without_star) + ":" + " " + "$" + data.priceWithComission

    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view)

}
