package com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList

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
import kotlinx.android.synthetic.main.seller_closed_list_child_adapter.view.*


class ClosedListChildAdapter(var context: Context) :
    RecyclerView.Adapter<ClosedListChildAdapter.Viewholder>() {
    var arrayslot1 = ArrayList<DialogModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.seller_closed_list_child_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return arrayslot1.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = arrayslot1[position]
        holder.itemView.txtproductName.text = data.name + "(" + Constant.convertUnits(context,data.unit )+ ")"
        holder.itemView.txtrice.text = "$" + Constant.roundValue(data.priceWithComission.toDouble())
        Glide.with(context)
            .load(data.image)
            .placeholder(R.drawable.product_placeholder)
            .into(holder.itemView.addimg)

        holder.itemView.addimg.setOnClickListener {
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

    fun updateData(childProductList: java.util.ArrayList<DialogModel>) {
        arrayslot1 = childProductList
        notifyDataSetChanged()

    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view)
}
