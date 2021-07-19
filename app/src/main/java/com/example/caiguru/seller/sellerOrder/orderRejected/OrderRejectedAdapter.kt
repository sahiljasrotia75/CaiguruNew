package com.example.caiguru.seller.sellerOrder.orderRejected

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.recycler_rejected_order.view.*
import java.util.ArrayList

class OrderRejectedAdapter(var context: Context, val list: ArrayList<OrderRejectedModel>) :
    RecyclerView.Adapter<OrderRejectedAdapter.ViewHolder>() {
    val listener = context as selectedData


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.recycler_rejected_order, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelRejectedList = list[position]

        holder.itemView.rbText.text = modelRejectedList.rejectText

        if (list[position].checked) {
            holder.itemView.rbText.isChecked = true
        } else {
            holder.itemView.rbText.isChecked = false
        }

        holder.itemView.rbText.setOnClickListener {

            if (list[position].checked) {
                list[position].checked = false
                listener.getSelectedPosition(
                    position,
                    modelRejectedList.rejectText,
                    modelRejectedList.checked
                )

            } else {
                for (item in 0 until list.size) {
                    list[item].checked = false
                }
                list[position].checked = true
                listener.getSelectedPosition(
                    position,
                    modelRejectedList.rejectText,
                    modelRejectedList.checked
                )
            }
            notifyDataSetChanged()
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



    interface selectedData {
        fun getSelectedPosition(position: Int, name: String, checked: Boolean)
    }

}





