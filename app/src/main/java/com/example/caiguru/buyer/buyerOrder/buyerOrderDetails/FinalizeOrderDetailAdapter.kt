package com.example.caiguru.buyer.buyerOrder.buyerOrderDetails

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
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.finalize_order_details.view.*

class FinalizeOrderDetailAdapter(
    var context: Context

) : RecyclerView.Adapter<FinalizeOrderDetailAdapter.ViewHolder>() {

    var child = ArrayList<ShoppingListModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.finalize_order_details, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return child.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.productName.text = list.name + "(" + Constant.convertUnits(context,list.unit) + ")"
        try {
            if (list.price.isEmpty()|| list.price == "0") {
                holder.itemView.price.text = context.getString(R.string.NA)
            } else {
              //  val totalPrice = list.price.toDouble() / 100 * comissionPers.toDouble()
                holder.itemView.price.text = "$" +Constant.roundValue(list.priceWithComission.toDouble())
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
        holder.itemView.txtQty.text = list.qty

        if (list.image.isEmpty()) {
            Glide.with(context)
                .load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide.with(context)
                .load(list.image)
                .placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        }

        holder.itemView.productImage.setOnClickListener {
            val data = ArrayList<PostShoppingModel>()


            if (list.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change code image
                for (item in child) {
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

    fun update(
        it: ArrayList<ShoppingListModel>
    ) {
        child = it
        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}