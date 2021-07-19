package com.example.caiguru.seller.sellerSetting.sellerBuyCredits

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.buy_credits_adapter.view.*
import java.util.ArrayList

class BuyCreditsAdapter(var context: Context) :
    RecyclerView.Adapter<BuyCreditsAdapter.ViewHolder>() {
    var click = context as GetCreditsPackInterface
    private var mData = ArrayList<BuyCreditsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.buy_credits_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.credits.text = data.credits
        holder.itemView.buycredits.text = data.total

        if (data.hasselected) {
            holder.itemView.buycreditlayout.setBackgroundDrawable(context.getDrawable(R.drawable.curve_rectangle_credit_border_yellow))
            holder.itemView.selectedCredits.visibility = View.VISIBLE
            holder.itemView.credits.setTextColor(context.resources.getColor(R.color.purple))
            //set  the interface by default
            click.getCreditsPack(data, position)//send the data by the interface
        } else {
            holder.itemView.credits.setTextColor(context.resources.getColor(R.color.hard_grey))
            holder.itemView.selectedCredits.visibility = View.GONE
            holder.itemView.buycreditlayout.setBackgroundResource(R.color.light_purple)
        }


        //set click
        holder.itemView.setOnClickListener {
            if (data.hasselected) {
                mData[position].hasselected = false
            } else {
                for (item in mData) {
                    item.hasselected = false
                }
                mData[position].hasselected = true
            }
            click.getCreditsPack(data, position)//send the data by the interface
            notifyDataSetChanged()
        }

    }

    fun update(it: ArrayList<BuyCreditsModel>) {
        mData = it
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface GetCreditsPackInterface {
        fun getCreditsPack(
            data: BuyCreditsModel,
            position: Int
        )


    }
}