package com.example.caiguru.commonScreens.otherProfiles.viewAllShoppingList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileShoppingListChildAdapter
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.cardView
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.recyclerChild
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.updown
import kotlinx.android.synthetic.main.other_profile_shoppinglist_parent_adapter.view.*

class ViewAllShoppingListParentAdapter(var context: Context) :
    RecyclerView.Adapter<ViewAllShoppingListParentAdapter.ViewHolder>() {
    private var mData = ArrayList<ShoppingListModel>()
    lateinit var recyclerAdapter: OtherProfileShoppingListChildAdapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context)
            .inflate(R.layout.other_profile_shoppinglist_parent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtListCategory.text = categoryData(data.cat_id)
        if (data.price.isEmpty() || data.price == "0") {
            holder.itemView.price.text = context.getString(R.string.NA)

        } else {
            try {
                holder.itemView.price.text = Constant.roundValue(data.price.toDouble())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (data.isExpanded) {
            holder.itemView.relativeLayout.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.relativeLayout.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }
        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }
        //************************CustomerListChildAdapter****************************//
        recyclerAdapter = OtherProfileShoppingListChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.update(data.product_details)

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

    fun update(it: ArrayList<ShoppingListModel>) {

        if (it != null) {
            mData = it
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}
