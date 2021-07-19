package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.choose_seller_parent_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.cardView
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.recyclerChild
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.txtList
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.updown

class SavedProductParentAdapter(
    var context: FragmentActivity
) :
    RecyclerView.Adapter<SavedProductParentAdapter.ViewHolder>() {
    private var mData = ArrayList<ShoppingListModel>()
  //  var click = listen as loadmore//click
    lateinit var recyclerAdapter: SavedProductChildAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.saved_products_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtList.text = categoryData(data.cat_id)

        if (data.isExpanded) {
            holder.itemView.innerLayoutChilds.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.up_arrow_yellow)

        } else {
            holder.itemView.innerLayoutChilds.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.down_arrow_yellow)
        }

        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }

        //************************SavedProductChildAdapter****************************//

        recyclerAdapter =
            SavedProductChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.updateData(data.products)
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

    fun updateData(it: ArrayList<ShoppingListModel>) {

        if (it != null) {
            mData= it
        }
        notifyDataSetChanged()
    }

    //get  all array data
    fun getChildArrayData(): ArrayList<PostShoppingModel> {
        val localArray = ArrayList<PostShoppingModel>()
        for (item in mData){
            localArray.addAll(item.products)
        }
        return localArray
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)



}
