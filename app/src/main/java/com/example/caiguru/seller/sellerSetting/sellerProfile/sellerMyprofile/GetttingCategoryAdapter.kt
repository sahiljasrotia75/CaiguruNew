package com.example.caiguru.seller.sellerSetting.sellerProfile.sellerMyprofile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import kotlinx.android.synthetic.main.get_category_adapter.view.*

class GetttingCategoryAdapter(var context: Context) :
    RecyclerView.Adapter<GetttingCategoryAdapter.ViewHolder>() {
    var list = ArrayList<SellerCategoryModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.get_category_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mData = list[position]
        holder.itemView.btncategory.text = mData.name
    }

    fun update(categoryData: java.util.ArrayList<SellerCategoryModel>) {
        list = categoryData
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}
