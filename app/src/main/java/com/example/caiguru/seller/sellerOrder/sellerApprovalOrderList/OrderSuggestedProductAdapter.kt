package com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.order_suggested_product_adapter.view.*

class OrderSuggestedProductAdapter(var context: SellerApprovalOrderListActivity) :
    RecyclerView.Adapter<OrderSuggestedProductAdapter.Viewholder>() {
    var mData = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.order_suggested_product_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val mDatas = mData[position]
        holder.itemView.butnSuggestProduct.text = mDatas

    }
    fun Update(it: ArrayList<String>) {
        mData = it
        notifyDataSetChanged()
    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view)




}
