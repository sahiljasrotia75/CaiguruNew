package com.example.caiguru.commonScreens.commonNotifications.rateSeller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import kotlinx.android.synthetic.main.rate_seller_adapter.view.*

class SellerRatingAdapter(
    var context: Context

) : RecyclerView.Adapter<SellerRatingAdapter.ViewHolder>() {
    var click = context as setRatingDataInterface
    var child = ArrayList<SellerRateModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.rate_seller_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]

        if (list.hasSelected) {
           // click.setRating(child, position)
            holder.itemView.statusText.text = list.rateStatusText
            Glide.with(context)
                .load(list.rateImageSelected)
                .into(holder.itemView.imgReview)
        } else {
            holder.itemView.statusText.text = list.rateStatusText
            Glide.with(context)
                .load(list.rateImageUnSelected)
                .into(holder.itemView.imgReview)
        }


        holder.itemView.setOnClickListener {

            if (list.hasSelected) {
                child[position].hasSelected = false
            } else {
                for (item in child) {
                    item.hasSelected = false
                }
                child[position].hasSelected = true
            }
            click.setRating(child, position)
            notifyDataSetChanged()
        }
    }

    fun update(it: ArrayList<SellerRateModel>) {
        child = it
        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface setRatingDataInterface {
        fun setRating(
            child: ArrayList<SellerRateModel>,
            position: Int
        )
    }


}