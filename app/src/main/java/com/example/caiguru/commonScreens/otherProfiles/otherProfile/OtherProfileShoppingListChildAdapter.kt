package com.example.caiguru.commonScreens.otherProfiles.otherProfile

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
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.order_details_adapter.view.*
import kotlinx.android.synthetic.main.order_details_adapter.view.productImage
import java.lang.Exception

class OtherProfileShoppingListChildAdapter(
    var context: Context

) : RecyclerView.Adapter<OtherProfileShoppingListChildAdapter.ViewHolder>() {

    var child = ArrayList<DialogModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.other_profile_order_details_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.productNameUnit.text =
            list.name + "(" + Constant.convertUnits(context, list.unit) + ")"
        if (list.price.isEmpty() || list.price == "0") {
            holder.itemView.price.visibility = View.INVISIBLE

        } else {
            try {
                holder.itemView.price.text = "$" + Constant.roundValue(list.price.toDouble())
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        if (list.qty.isEmpty()) {
            holder.itemView.quantity.visibility = View.INVISIBLE
        } else {
            holder.itemView.quantity.text = list.qty
        }



        if (list.image.isEmpty()) {
            Glide.with(context)
                .load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)

        } else {
            Glide.with(context)
                .load(list.image)
                .into(holder.itemView.productImage)
        }
        holder.itemView.productImage.setOnClickListener {
            val data = ArrayList<PostShoppingModel>()
            for (item in child) {
                if (item.image != "http://18.229.181.113/dev/public/users/defaultprod.png" && item.image.isNotEmpty()) {
                    val model = PostShoppingModel()
                    model.image = item.image
                    model.unit = item.unit
                    model.priceWithComission = item.priceWithComission
                    model.name = item.name
                    data.add(model)
                }
            }
            if (data.size>0){
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                intent.putExtra("keyImage", data)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
//            if (list.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
//                val intent = Intent(context, ImageFullScreenActivity::class.java)
//                //change code image
//                for (item in child) {
//                    val model = PostShoppingModel()
//                    model.image = item.image
//                    model.unit = item.unit
//                    model.price = item.price
//                    model.name = item.name
//                    data.add(model)
//                }
//                intent.putExtra("keyImage", data)
//                intent.putExtra("keyImagePosition", position)
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                context.startActivity(intent)
//            }
        }
    }

    fun update(it: ArrayList<DialogModel>) {
        child = it
        notifyDataSetChanged()

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}