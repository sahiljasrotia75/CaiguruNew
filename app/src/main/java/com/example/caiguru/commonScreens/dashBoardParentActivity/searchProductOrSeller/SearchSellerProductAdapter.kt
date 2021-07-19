package com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.search_product_recycler_layout.view.*

class SearchSellerProductAdapter(
    var context: Context

) : RecyclerView.Adapter<SearchSellerProductAdapter.ViewHolder>() {
    var child = ArrayList<SearchModel>()
var click =context as historyInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.search_product_recycler_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.txtSearch.text = list.searchName


        //***************set the click on the search
        holder.itemView.setOnClickListener {
            click.historyClick(list.searchName)
        }
    }

    //******** get the updated data
    fun updateData(it: ArrayList<SearchModel>) {

        child = it
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface historyInterface {
        fun historyClick(searchName: String)
    }
}
