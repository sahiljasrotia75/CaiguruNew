package com.example.caiguru.buyer.buyerOrder

import com.example.caiguru.seller.shoppingListSellers.shoppingListSeller.ShopModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.buyerCancelledOrder.BuyerCancelledActivity
import com.example.caiguru.buyer.buyerOrder.buyerOrderComplete.BuyerOrderCompleteActivity
import com.example.caiguru.buyer.buyerOrder.buyerPendingApproval.BuyerPendingApprovalActivity
import com.example.caiguru.buyer.buyerOrder.buyerToBeDelivered.BuyerToBeDeliveredActivity
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeOrderActivity
import com.example.caiguru.seller.sellerOrder.sellerOrderCanelled.SellerOrderCancelledActivity
import com.example.caiguru.seller.sellerOrder.sellerOrderCompleted.SellerOrderCompletedActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerPendingApprovalsActivity
import com.example.caiguru.seller.sellerOrder.sellerToBeDelivered.SellerToBeDeliveredActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.recycler_shop_list.view.*

class BuyerOrderAdapter(var context: Context, val list: ArrayList<ShopModel>) :
    RecyclerView.Adapter<BuyerOrderAdapter.ViewHolder>() {

    //  private  var finalizeCounts: String=""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(context).inflate(R.layout.recycler_shop_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelShopList = list[position]

        holder.itemView.ivShop.setImageResource(modelShopList.shopImage)
        holder.itemView.ivNext.setImageResource(modelShopList.shopNext)
        holder.itemView.tvShopList.text = modelShopList.shopText

//set the click of hide button
        if (position == 0) {
            if (Constant.getProfileData(context).finalizeCount.isNotEmpty()) {

                if (Constant.getProfileData(context).finalizeCount.toInt() > 0) {
                    holder.itemView.imgOrderCount.visibility = View.VISIBLE
                    holder.itemView.imgOrderCount.text =
                        Constant.getProfileData(context).finalizeCount
                } else {
                    holder.itemView.imgOrderCount.visibility = View.GONE

                }
            } else {
                holder.itemView.imgOrderCount.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener {
            if (position == 0) {
                val intent = Intent(context, FinalizeOrderActivity::class.java)
                context.startActivity(intent)
            } else if (position == 1) {
                val intent = Intent(context, BuyerPendingApprovalActivity::class.java)
                context.startActivity(intent)
            } else if (position == 2) {
                val intent = Intent(context, BuyerToBeDeliveredActivity::class.java)
                context.startActivity(intent)
            } else if (position == 3) {
                val intent = Intent(context, BuyerOrderCompleteActivity::class.java)
                context.startActivity(intent)
            } else if (position == 4) {
                val intent = Intent(context, BuyerCancelledActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

//    fun upDate(finalizeCount: String) {
//        finalizeCounts=finalizeCount
//        notifyDataSetChanged()
//    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}
