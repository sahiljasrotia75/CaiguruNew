package com.example.caiguru.buyer.buyerLists.buyerShopApproveReject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.imageFullScreenDisplay.ImageFullScreenActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_shop_list_approve_reject_adapter.view.*

class BuyerShopListApproveRejectAdapter(var context: Context) :
    RecyclerView.Adapter<BuyerShopListApproveRejectAdapter.ViewHolder>() {

    var list: ArrayList<PostShoppingModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.buyer_shop_list_approve_reject_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.productname.text = model.name + "${"(" +Constant.convertUnits(context, model.unit) + ")"}"
        holder.itemView.quantity.text = model.qty
        if (model.price.isEmpty() || model.price <= "0") {
            holder.itemView.orderPrice.text = context.getString(R.string.NA)
        } else {
            // holder.itemView.orderPrice.text = "$" + model.price
            holder.itemView.orderPrice.text = "$" + (Constant.roundValue(model.priceWithComission.toDouble()))
        }


        if (model.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.img)
        } else {
            Glide.with(context)
                .load(model.image)
                .placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.img)

        }

        holder.itemView.img.setOnClickListener {




            val data = ArrayList<PostShoppingModel>()

            if (model.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change code image
                for (item in list) {
                    val model = PostShoppingModel()
                    model.image = item.image
                    model.unit = item.unit
                    model.priceWithComission = item.priceWithComission
                    model.name = item.name
                    data.add(model)
                }
                intent.putExtra("keyImage", data)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        }
    }


    fun update(it: java.util.ArrayList<PostShoppingModel>) {
        list = it
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}