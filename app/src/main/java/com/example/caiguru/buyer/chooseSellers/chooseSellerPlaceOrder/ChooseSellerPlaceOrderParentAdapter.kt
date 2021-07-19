package com.example.caiguru.buyer.chooseSellers.chooseSellerPlaceOrder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.choose_seller_place_order_adapter.view.*
import kotlin.collections.ArrayList

class ChooseSellerPlaceOrderParentAdapter(
    var context: ChooseSellerPlaceOrderActivity
) : RecyclerView.Adapter<ChooseSellerPlaceOrderParentAdapter.Viewholder>() {
    private  var comission_per: String=""
    private lateinit var recyclerAdapter: ChooseSellerPlaceOrderChildAdapter
    var arraymodel = ArrayList<ChooseSellerShoppingModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.choose_seller_place_order_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return arraymodel.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = arraymodel[position]

        holder.itemView.listnames.text = data.listingname + " "
        holder.itemView.txtPrices.text = "$" + Constant.roundValue(data.total.toDouble())
        //************************CustomerListChildAdapter****************************//

        recyclerAdapter = ChooseSellerPlaceOrderChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        val arrayProducts = ArrayList<PostShoppingModel>()
        for (item in data.product_details) {
            if (item.qty != "0") {
                val produts = PostShoppingModel()
                produts.name = item.name
                produts.unit = item.unit
                produts.price = item.price
                produts.qty = item.qty
                produts.priceWithComission = item.priceWithComission
                arrayProducts.add(produts)
            }
        }
        comission_per = data.comission_per
        recyclerAdapter.updateData(arrayProducts,comission_per)


    }

    fun upDateData(payCreditArrayModel: ArrayList<ChooseSellerShoppingModel>) {
        arraymodel = payCreditArrayModel
        notifyDataSetChanged()
    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view)



}
