package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.store_parent_adapter.view.*


class StoreParentAdapter(
    var context: FragmentActivity
) :
    RecyclerView.Adapter<StoreParentAdapter.ViewHolder>() {
    private  var setLoadMoreText: String="true"
    private var mData = ArrayList<ShoppingListModel>()
    var setAdapterView = "grid"

    //  var click = listen as loadmore//click
    lateinit var recyclerAdapter: StoreChildAdapter
    var click = context as setLoadMoreInterfec
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.store_parent_adapter, parent, false)
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
        if (setAdapterView == "grid") {
            recyclerAdapter = StoreChildAdapter(context)
            holder.itemView.recyclerChild.setLayoutManager(GridLayoutManager(context, 2))
            holder.itemView.recyclerChild.adapter = recyclerAdapter
            recyclerAdapter.updateData(data.products)
        } else {
            recyclerAdapter = StoreChildAdapter(context)
            val manager = LinearLayoutManager(context)
            holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
            holder.itemView.recyclerChild.adapter = recyclerAdapter
            recyclerAdapter.updateData(data.products)
        }

        if (setLoadMoreText.equals("true")){
            holder.itemView.txtLoadMore.text=context.getString(R.string.load_more_products_store)
            holder.itemView.txtLoadMore.isClickable=true
        }

        //check the product product load more
        if (data.count > 4 && data.count != data.products.size) {
            holder.itemView.txtLoadMore.visibility = View.VISIBLE
        } else {
            holder.itemView.txtLoadMore.visibility = View.GONE
        }
        //set the  click on the Load More
        holder.itemView.txtLoadMore.setOnClickListener {
            Log.e("arraySizeAdapter", data.products.size.toString())
            click.LoadMoreClick(data, position)
            holder.itemView.txtLoadMore.text=context.getString(R.string.pleasewait)
            holder.itemView.txtLoadMore.isClickable=false
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
        return context.getString(R.string.mix_category_product)
    }

    fun updateData(it: ArrayList<ShoppingListModel>) {
        setLoadMoreText="true"
        if (it != null) {
            mData = it

        }
        updateArrayPageInChild()//set the page count in child array
        notifyDataSetChanged()
    }

    //get  all array data
//    fun getChildArrayData(): ArrayList<PostShoppingModel> {
//        val localArray = ArrayList<PostShoppingModel>()
//        for (item in mData) {
//            localArray.addAll(item.products)
//        }
//        return localArray
//    }

    fun setAdapterViews(changeView: String) {

        setAdapterView = changeView
        notifyDataSetChanged()
    }

    fun clearCart() {
        for (parentItem in mData) {
            for (childitem in parentItem.products) {
                childitem.qty = "0"
                childitem.LocalTotal = "0"
                childitem.total = "0"
                childitem.partialComission = "0"
            }
        }
        notifyDataSetChanged()

    }

    fun clearCartSingleProduct(productId: String) {
        for (parentItem in mData) {
            for (childitem in parentItem.products) {
                if (childitem.id == productId) {
                    childitem.qty = "0"
                    childitem.LocalTotal = "0"
                    childitem.total = "0"
                    childitem.partialComission = "0"
                }

            }
        }
        notifyDataSetChanged()

    }

    fun setQty(list: PostShoppingModel) {
        for (parentItem in mData) {
            for (childitem in parentItem.products) {
                if (childitem.id == list.id) {
                    childitem.qty = list.qty
                    childitem.LocalTotal = list.LocalTotal
                    childitem.total = list.total
                    childitem.partialComission = list.partialComission
                }

            }
        }
        notifyDataSetChanged()

    }

    fun updateArrayPageInChild() {
        for (item in mData) {
            for (child in item.products) {
                child.page=item.page
                child.cat_id=item.cat_id
                child.count=item.count
                child.usedProds=item.usedProds
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface setLoadMoreInterfec {
        fun LoadMoreClick(data: ShoppingListModel, position: Int)
    }
}
