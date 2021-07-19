package com.example.caiguru.buyer.buyerProfile.buyerMyOrder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.BuyerOrderDetailsActivity
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails.UnderReviewOrderDetailsActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_my_order_adapter.view.*
import kotlin.collections.ArrayList

class BuyerMyOrderAdapter(
    var context: Context

) : RecyclerView.Adapter<BuyerMyOrderAdapter.ViewHolder>() {

    var child = ArrayList<OrderModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.buyer_my_order_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        // Status: 1:Pending Approval, 2:Accepted, 3:Rejected, 4:SellerCompleted, 5:Buyer_Confirmed or Delivered(D), 6: Under_Review
        holder.itemView.ShoppingListName.text = list.seller_name
        if (list.status == "1") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text = context.getString(R.string.Pending_Approvals)

        } else if (list.status == "2") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text =
                context.getString(R.string.Accepted)
        } else if (list.status == "3") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text = context.getString(R.string.Rejected)
        } else if (list.status == "4") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text = context.getString(R.string.SellerCompleted)
        } else if (list.status == "5") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text = context.getString(R.string.Buyer_Confirmed)
        } else if (list.status == "7") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text = context.getString(R.string.seller_cancelled)
        } else if (list.status == "8") {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text =
                context.getString(R.string.buyer_confirmed_order_cancelled)
        } else {
            holder.itemView.txtOrder.text =
                "${context.getString(R.string.Ordered_on)} ${
                    Constant.ATconvert(
                        context,
                        Constant.ConvertAmPmFormat(
                            context,
                            Constant.orderdateTimeFormat(list.created_at)
                        )
                    )
                }"
            holder.itemView.status.text = context.getString(R.string.Under_Review)
        }
        holder.itemView.setOnClickListener {
            if (list.status == "6") {
                val intent = Intent(context, UnderReviewOrderDetailsActivity::class.java)
                intent.putExtra("keydetails", list)
                context.startActivity(intent)
                //open the screen when order is delivered
            } else if (list.status == "5") {
                val intent = Intent(context, UnderReviewOrderDetailsActivity::class.java)
                intent.putExtra("keydetails", list)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, BuyerOrderDetailsActivity::class.java)
                intent.putExtra("keydetails", list)
                context.startActivity(intent)
            }
        }
    }

    fun update(it: ArrayList<OrderModel>) {
        child = it
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}