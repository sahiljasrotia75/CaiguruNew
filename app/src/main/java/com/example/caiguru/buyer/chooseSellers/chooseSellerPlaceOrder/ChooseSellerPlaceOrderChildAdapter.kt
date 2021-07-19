package com.example.caiguru.buyer.chooseSellers.chooseSellerPlaceOrder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.layout_shopping_list.view.*

class ChooseSellerPlaceOrderChildAdapter(var context: ChooseSellerPlaceOrderActivity) :
    RecyclerView.Adapter<ChooseSellerPlaceOrderChildAdapter.ViewHolder>() {
    private  var comissionPers: String=""
    var child = ArrayList<PostShoppingModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.layout_shopping_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = child[position]
        holder.itemView.AllTextSet.setText(
            model.name + "" + "(" + Constant.convertUnits(context,model.unit )+ ")" + " " + ":" + " " + "$" + Constant.roundValue(model.priceWithComission.toDouble())+" "+"X"+" "+model.qty
        )
    }
    fun updateData(
        arraymodel: ArrayList<PostShoppingModel>,
        comissionPer: String
    ) {
        child = arraymodel
        comissionPers=comissionPer
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
