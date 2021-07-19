package com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList

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
import kotlinx.android.synthetic.main.approval_order_list_child_adapter.view.*
import java.lang.Exception


class ToBeDeliveredOrderListChildAdapter(var context: Context) :
    RecyclerView.Adapter<ToBeDeliveredOrderListChildAdapter.Viewholder>() {
    var arrayslot1 = ArrayList<ShoppingListModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.approval_order_list_child_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return arrayslot1.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = arrayslot1[position]
        holder.itemView.productName.text =
            data.name + " " + "(" + Constant.convertUnits(context, data.unit) + ")"

        if (data.price.isEmpty() || data.price.toFloat().toInt() <= 0) {
            holder.itemView.price.text = context.getString(R.string.NA)
        } else {
            try {
                holder.itemView.price.text =
                    "$" + Constant.roundValue(data.priceWithComission.toDouble())
            } catch (e: Exception) {

            }
        }
        holder.itemView.txtQty.text = data.qty
        if (data.image.isEmpty()) {

            Glide.with(context)
                .load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)

        } else {
            Glide.with(context)
                .load(data.image)
                .placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        }

        holder.itemView.productImage.setOnClickListener {
            val list = ArrayList<PostShoppingModel>()
            if (data.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change code image
                for (item in arrayslot1) {
                    val model = PostShoppingModel()
                    model.image = item.image
                    model.unit = item.unit
                    model.priceWithComission = item.priceWithComission
                    model.name = item.name
                    list.add(model)
                }
                intent.putExtra("keyImage", list)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }

        }

    }


    fun updateData(childProductList: ArrayList<ShoppingListModel>) {
        arrayslot1 = childProductList
        notifyDataSetChanged()

    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view)
}
