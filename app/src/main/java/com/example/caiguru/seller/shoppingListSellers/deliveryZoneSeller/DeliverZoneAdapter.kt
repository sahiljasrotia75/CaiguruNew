package com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.delivery_zone_adapter.view.*
import java.util.ArrayList

class DeliverZoneAdapter(
    var context: Context
) : RecyclerView.Adapter<DeliverZoneAdapter.Viewholder>() {

    var list: ArrayList<DeliveryZoneModel> = ArrayList()

    var click = context as sendDataInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.delivery_zone_adapter, parent, false)
        return Viewholder(
            view
        )
    }

    override fun getItemCount(): Int {

        return list.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = list[position]

        holder.itemView.txtdeliveryzone.text = data.address
        if (list.size > 1) {
            holder.itemView.txtdelete.visibility = View.VISIBLE
            holder.itemView.txtdeliveryzone.text = data.address
        } else {
            holder.itemView.txtdelete.visibility = View.INVISIBLE
        }

        holder.itemView.txtdelete.setOnClickListener {
            list.removeAt(position)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            click.sendDataAdapter(data, position)
        }
    }

    fun update(data: ArrayList<DeliveryZoneModel>?) {
        if (data != null) {
            list = data
        }
        notifyDataSetChanged()
    }


    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView)
    //send the data to the activity by the interface

    interface sendDataInterface {
        fun sendDataAdapter(
            holder: DeliveryZoneModel,
            position: Int
        )
    }


}
