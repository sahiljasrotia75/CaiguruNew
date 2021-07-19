package com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails.ListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.open_parent_adapter.view.*
import java.util.ArrayList

class OpenParentAdapter(var context: Context): RecyclerView.Adapter<OpenParentAdapter.ViewHolder>() {

    private var mData = ArrayList<ListModel>()
    lateinit var recyclerAdapter: OpenChildAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.open_parent_adapter, parent, false)
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
            holder.itemView.innerLayoutChild1.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.innerLayoutChild1.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }
        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }

        //************************CustomerListChildAdapter****************************//


        recyclerAdapter = OpenChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.rvOpenChild.layoutManager = manager //set layout in recycler
        holder.itemView.rvOpenChild.adapter = recyclerAdapter
        recyclerAdapter.update(data.lists)
    }

    //***************** Matching id's *********************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return context.getString(R.string.mix_category_product)
    }

    fun updateData(it: ArrayList<ListModel>) {
        it.let {
            mData = it
        }
        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
