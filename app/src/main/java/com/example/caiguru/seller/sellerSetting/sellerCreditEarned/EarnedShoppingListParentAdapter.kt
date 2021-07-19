package com.example.caiguru.seller.sellerSetting.sellerCreditEarned

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.earned_shopping_list_parent_adapter.view.*
import kotlinx.android.synthetic.main.openlist_parentadapter.view.updown
import java.util.ArrayList

class EarnedShoppingListParentAdapter(var context: Context) :
    RecyclerView.Adapter<EarnedShoppingListParentAdapter.ViewHolder>() {

    private lateinit var recyclerAdapter: EarnedShoppingListChildAdapter
    private var mData = ArrayList<EarnedReferedFriendModel>()
    // lateinit var recyclerAdapter: OpenListChildAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.earned_shopping_list_parent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtListName.text =
            "${data.listingname}"
        if (data.isExpanded) {
            holder.itemView.recyclerChildEarnedShoppingList.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.recyclerChildEarnedShoppingList.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }
        //set the click on Cardview
        holder.itemView.cardViews.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }

        //*******************Child Adapter*****************//
        recyclerAdapter = EarnedShoppingListChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChildEarnedShoppingList.layoutManager = manager
        holder.itemView.recyclerChildEarnedShoppingList.adapter = recyclerAdapter
        val model = ArrayList<EarnedReferedFriendModel>()
        model.add(data)
        recyclerAdapter.update(model)


    }


    fun updateData(it: ArrayList<EarnedReferedFriendModel>) {
        if (it != null) {
            mData=it
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }


}