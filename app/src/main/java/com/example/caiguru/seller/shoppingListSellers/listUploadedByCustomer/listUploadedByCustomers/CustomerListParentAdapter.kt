package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.chooseSellers.loadmore.LoadMoreActivity
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.*

class CustomerListParentAdapter(var context: Context) :
    RecyclerView.Adapter<CustomerListParentAdapter.ViewHolder>() {

    private var mData = ArrayList<CustomerParentModel>()
    lateinit var recyclerAdapter: CustomerListChildAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.customer_list_parent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtList.text = categoryData(data.cat_id)
        //send the id of every holder click
        if (data.isExpanded) {
            holder.itemView.innerLayoutChild.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.innerLayoutChild.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.test)
        }
        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }

        //************************CustomerListChildAdapter****************************//

        recyclerAdapter = CustomerListChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.updateData(data)
        //************************************Load More- Check*************************************//
        if (data.more != 0) {
            holder.itemView.loadmore.visibility = View.VISIBLE
        } else {
            holder.itemView.loadmore.visibility = View.GONE
        }

        holder.itemView.loadmore.setOnClickListener {
            val intent = Intent(context, LoadMoreActivity::class.java)
            intent.putExtra("cat_id", data)
            context.startActivity(intent)
        }
    }
    //*************************************************************************************//


    //***************** Matching id's *********************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return  context.getString(R.string.mix_category_product)
    }


    fun update(it: java.util.ArrayList<CustomerParentModel>?) {

        if (it != null) {
            mData = it
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}
