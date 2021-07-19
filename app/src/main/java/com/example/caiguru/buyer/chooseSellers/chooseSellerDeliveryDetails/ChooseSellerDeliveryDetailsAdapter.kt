package com.example.caiguru.buyer.chooseSellers.chooseSellerDeliveryDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.choose_seller_delivery_details_adapter.view.*

class ChooseSellerDeliveryDetailsAdapter(var context: ChooseSellerDeliveryDetailsActivity) :
    RecyclerView.Adapter<ChooseSellerDeliveryDetailsAdapter.Viewholder>() {
    var arrayModel = ArrayList<String>()
    var days: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.choose_seller_delivery_details_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return arrayModel.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        holder.itemView.txtdeliverytimes.text =arrayModel[position]

    }

    fun Update(daysParentModel: ArrayList<String>) {

        arrayModel = daysParentModel
        notifyDataSetChanged()
    }





    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {

    }
}