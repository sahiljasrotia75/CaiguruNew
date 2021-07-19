package com.example.caiguru.buyer.buyerLists.buyerDeliveryDetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R

import kotlinx.android.synthetic.main.delivery_details_adapter.view.*

class DeliveryDetailsAdapter(var context: Context) :
    RecyclerView.Adapter<DeliveryDetailsAdapter.ViewHolder>() {
    var arrayModel = ArrayList<String>()
    var days: String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.delivery_details_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.itemView.deliverytime.text = Constant.getDayString(context, arrayModel[position])
        holder.itemView.deliverytime.text = arrayModel[position]
    }


    fun Update(daysParentModel: ArrayList<String>) {

        arrayModel = daysParentModel
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
