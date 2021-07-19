package com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.approvals_order_list_parent_adapter.view.*
import kotlinx.android.synthetic.main.seller_closed_list_parent_adapter.view.recyclerChild
import kotlin.collections.ArrayList

class ToBeDeliveredOrderListParentAdapter(var context: ToBeDeliveredOrderListActivity) : RecyclerView.Adapter<ToBeDeliveredOrderListParentAdapter.Viewholder>() {
    private lateinit var recyclerAdapter: ToBeDeliveredOrderListChildAdapter
    var mData = ArrayList<OrderListModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.approvals_order_list_parent_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = mData[position]
        holder.itemView.txtshoppingListt.text = data.listingname
        holder.itemView.txtamounts.text = "$" + Constant.roundValue(data.amount.toDouble())
       // holder.itemView.txtCategories.text = CategoryMatching(data.cat_id)
        holder.itemView.txtCategories.text = context.getString(R.string.order)

        //......child adapter..........//
        recyclerAdapter = ToBeDeliveredOrderListChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager//set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.updateData(data.products)


    }

    class Viewholder(view: View) : RecyclerView.ViewHolder(view)


    //***************** getting id *********************//
    fun CategoryMatching(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id) {

                return category.name
            }
        }
        return  context.getString(R.string.mix_category_product)
    }

    fun updateData(it: ArrayList<OrderListModel>) {
        mData = it
        notifyDataSetChanged()
    }

}
