package com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.loadmorecloseList.LoadMoreCloseListActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.closed_parent_adapter.view.*
import java.util.ArrayList

class ClosedParentAdapter(var context: Context) :
    RecyclerView.Adapter<ClosedParentAdapter.ViewHolder>() {

    private var mData = ArrayList<ListModel>()
    lateinit var recyclerAdapter: ClosedChildAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.closed_parent_adapter, parent, false)
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


        recyclerAdapter = ClosedChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.rvClosedChild.layoutManager = manager //set layout in recycler
        holder.itemView.rvClosedChild.adapter = recyclerAdapter
        recyclerAdapter.update(data.lists)


        //************************************Load More - Check*************************************//

        if (data.count != 0) {
            holder.itemView.loadmore1.visibility = View.VISIBLE
        } else {
            holder.itemView.loadmore1.visibility = View.GONE
        }

        holder.itemView.loadmore1.setOnClickListener {
            val intent = Intent(context, LoadMoreCloseListActivity::class.java)
            intent.putExtra("cat_id", data)
            context.startActivity(intent)
        }
    }

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

    fun updateData(it: ArrayList<ListModel>) {

        it.let {
            mData = it
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
