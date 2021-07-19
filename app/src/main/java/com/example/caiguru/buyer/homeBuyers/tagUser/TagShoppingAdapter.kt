package com.example.caiguru.buyer.homeBuyers.tagUser

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

class TagShoppingAdapter(var context: Context) :
    RecyclerView.Adapter<TagShoppingAdapter.ViewHolder>() {
    private  var allComissions: String=""
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
        val totalPrice = model.price.toDouble() / 100 * allComissions.toDouble()
        holder.itemView.AllTextSet.setText(
            model.name + "" + "(" + Constant.convertUnits(context,model.unit )+ ")" + " " + ":" + " " + "$" + Constant.roundValue(
                model.price.toDouble()+totalPrice
            )
        )
    }

    fun update(
        products: ArrayList<DialogModel>,
        allComission: String
    ) {
        allComissions=allComission
        data = products
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



}
