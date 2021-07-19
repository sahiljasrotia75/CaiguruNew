package com.example.caiguru.seller.sellerOrder.sellerOrdersFragment
import com.example.caiguru.seller.shoppingListSellers.shoppingListSeller.ShopModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.sellerOrder.sellerOrderCanelled.SellerOrderCancelledActivity
import com.example.caiguru.seller.sellerOrder.sellerOrderCompleted.SellerOrderCompletedActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerPendingApprovalsActivity
import com.example.caiguru.seller.sellerOrder.sellerToBeDelivered.SellerToBeDeliveredActivity
import kotlinx.android.synthetic.main.recycler_shop_list.view.*

class SellerOrderAdapter(var context: Context, val list: ArrayList<ShopModel>) :
    RecyclerView.Adapter<SellerOrderAdapter.ViewHolder>() {

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

        holder.itemView.setOnClickListener {
//            var intent: Intent = Intent()
            if (position == 0) {
                val intent = Intent(context, SellerPendingApprovalsActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 1) {
                val intent = Intent(context,SellerToBeDeliveredActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 2) {
                val intent = Intent(context, SellerOrderCompletedActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 3) {

                val intent = Intent(context, SellerOrderCancelledActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)



}
