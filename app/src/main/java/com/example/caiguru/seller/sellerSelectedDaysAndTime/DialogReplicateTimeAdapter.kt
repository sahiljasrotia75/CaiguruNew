package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.dialog_replicate_time_layout.view.*
import kotlin.collections.ArrayList


class DialogReplicateTimeAdapter(
    var context: Context,
    var mData: ArrayList<DialogDaysModel>
) : RecyclerView.Adapter<DialogReplicateTimeAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.dialog_replicate_time_layout, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {

        return mData.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = mData[position]
            holder.itemView.DaysPosition1.text = Constant.getDayString(context, data.dayName)
            if (data.isSelected) {
                holder.itemView.DaysPosition1.setBackgroundResource(R.drawable.timer_dialog_rectangle_selector)
            } else {
                holder.itemView.DaysPosition1.setBackgroundResource(R.drawable.curve_rectangle_dialog_uselected_yellow)
            }






        //set the click
        holder.itemView.setOnClickListener {
            //multiple selection
            mData[position].isSelected = !mData[position].isSelected
            notifyDataSetChanged()

//            if (data.isSelected) {
//                mData[position].isSelected =  false
//            } else {
//                for (item in mData) {
//                    item.isSelected = false
//                }
//                mData[position].isSelected = true
//            }
            //notifyDataSetChanged()
        }
    }

    fun getAllData(): ArrayList<DialogDaysModel> {
        return mData
    }

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)


}
