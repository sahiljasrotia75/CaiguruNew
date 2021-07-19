package com.example.caiguru.commonScreens.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.choose_seller_parent_adapter.view.*
import kotlinx.android.synthetic.main.choose_seller_parent_adapter.view.cardView
import kotlinx.android.synthetic.main.choose_seller_parent_adapter.view.recyclerChild
import kotlinx.android.synthetic.main.choose_seller_parent_adapter.view.updown

class ChatParentAdapter(var context: Context) : RecyclerView.Adapter<ChatParentAdapter.ViewHolder>() {

    private var mData = ArrayList<ChatParent>()
    private val listener = context as ParentClickListener

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.choose_seller_parent_adapter, parent, false))
    }

    override fun getItemCount(): Int {
       return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mData[position]
        //holder.itemView.txtList.text = categoryData(model.catId)
        holder.itemView.txtList.text = model.name
        setChatAdapter(context, holder, model.channels)

        if (model.isExpanded) {
            holder.itemView.innerLayoutChilds.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)

        } else {
            holder.itemView.innerLayoutChilds.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }


        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !model.isExpanded
            notifyDataSetChanged()
            listener.onParentClick(position)
        }
    }

    fun update(it: List<ChatParent>?) {
        mData  = it as ArrayList<ChatParent>
        notifyDataSetChanged()
    }

    private fun setChatAdapter(
        context: Context,
        holder: ViewHolder,
        channels: ArrayList<ModelChat>
    ) {
        val mChatAdapter = ChatAdapter(context)
        val manager  = LinearLayoutManager(context)
//        holder.itemView.recyclerChild.addItemDecoration(
//            DividerItemDecoration(
//                context,
//                DividerItemDecoration.VERTICAL
//            )
//        )

        holder.itemView.recyclerChild.layoutManager = manager
        holder.itemView.recyclerChild.adapter = mChatAdapter
        mChatAdapter.update(channels)
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

    interface ParentClickListener{
        fun onParentClick(expandedPosition: Int)
    }
}