package com.example.caiguru.buyer.postList.buyerPostScreenPrefilled

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_post_shopping_adapter.view.*

class PostShoppingPrefillBuyerAdapter(var context: Context):RecyclerView.Adapter<PostShoppingPrefillBuyerAdapter.ViewHolder>(){
    var list: ArrayList<PostShoppingModel> = ArrayList()
    var click = context as editDataInterface//click

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.buyer_post_shopping_prefill_adapter, parent, false)
        return ViewHolder(
            view
        )

    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]




        if (data == null) {
            holder.itemView.imgEdtfieldMarker.visibility = View.INVISIBLE
            holder.itemView.imgdeleteData.visibility = View.INVISIBLE


        } else {
            holder.itemView.productid.text =
                "#${context.getString(R.string.product)} ${position + 1}"
            holder.itemView.txtproductname.text = data.name
            holder.itemView.quantity.text =  Constant.convertUnits(context,data.unit)
            holder.itemView.txtprice.text =  data.qty
            holder.itemView.imgEdtfieldMarker.visibility = View.VISIBLE
            holder.itemView.imgdeleteData.visibility = View.VISIBLE
        }
        //set the click on the delete items
        holder.itemView.imgdeleteData.setOnClickListener {
            click.deleteShoppingList(list, position)

        }

        holder.itemView.imgEdtfieldMarker.setOnClickListener {
            click.edtData(data, position)

        }
    }
    fun update(data: java.util.ArrayList<PostShoppingModel>?) {
        if (data != null) {
            list = data

        }
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
    interface editDataInterface {

        fun edtData(data: PostShoppingModel, position: Int)

        fun deleteShoppingList(
            list: ArrayList<PostShoppingModel>,
            position: Int
        )
    }

}
