package com.example.caiguru.seller.sellerSetting.sellerProfile.sellerEditProfile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import kotlinx.android.synthetic.main.edit_category_adapter.view.*

class EditCategoryAdapter(var context: Context):RecyclerView.Adapter<EditCategoryAdapter.ViewHolder>() {
    var list = ArrayList<SellerCategoryModel>()
    var click = context as removeCategoryInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.edit_category_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mData = list[position]
        holder.itemView.btneditcategory.text = mData.name

    }

    fun update(categoryData: ArrayList<SellerCategoryModel>) {
        list=categoryData
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    interface removeCategoryInterface {
        fun removeSelectedCategory(mData: SellerCategoryModel)
    }
}