package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.viewStoreProductFullScren

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import kotlinx.android.synthetic.main.view_product_shopping_adapter.view.*
import java.util.ArrayList

class ViewProductFullScreenAdapter(var context: Context) :
    RecyclerView.Adapter<ViewProductFullScreenAdapter.ViewHolder>() {
    var data = ArrayList<PostShoppingModel>()
    var isClickable = true
    var click = context as clickInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.view_product_shopping_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]

        if (model.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide
                .with(context)
                .load(model.image)
                .placeholder(R.drawable.product_placeholder)
                .apply(RequestOptions().override(200, 200).centerCrop())
                .into(holder.itemView.productImage)
        }
        holder.itemView.productImage.background =
            context.getDrawable(R.drawable.buyer_home_product_curve_rectangle)

        holder.itemView.productImage.setOnClickListener {
            click.openImagesInterfaces(data, position)
        }

        if (data[0].count > 4 && data[0].count != data.size) {
            if (position == data.size - 1) {
                holder.itemView.imgLoadMore.visibility = View.VISIBLE
            } else {
                holder.itemView.imgLoadMore.visibility = View.GONE
            }

        } else {
            holder.itemView.imgLoadMore.visibility = View.GONE
        }

        if (isClickable){
            holder.itemView.imgLoadMore.isClickable = true
        }else{
            holder.itemView.imgLoadMore.isClickable = false
        }
        //set the click on  load more
        holder.itemView.imgLoadMore.setOnClickListener {
            val localModel = ShoppingListModel()
            localModel.page = model.page
            localModel.cat_id = model.cat_id
            click.LoadMoreClick(localModel, position, data[0].usedProds)
            holder.itemView.imgLoadMore.isClickable = false
            isClickable=false
        }
    }

    fun update(
        products: ArrayList<PostShoppingModel>
    ) {
        isClickable=true
        data = products
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface clickInterface {
        fun openImagesInterfaces(
            list: ArrayList<PostShoppingModel>,
            position: Int
        )

        fun LoadMoreClick(data: ShoppingListModel, position: Int, usedProds: String)
    }

}
