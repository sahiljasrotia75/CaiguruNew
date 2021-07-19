package com.example.caiguru.buyer.homeBuyers.homeBuyer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.caiguru.R
import com.example.caiguru.commonScreens.imageFullScreenDisplay.ImageFullScreenActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.home_buyer_adapter_shopping_list.view.*
import java.util.ArrayList

class ShoppingChildAdapter(var context: Context) :
    RecyclerView.Adapter<ShoppingChildAdapter.ViewHolder>() {
    private var getProfileModel = GetProfileModel()
    private var comissionPers: String = ""
    var data = ArrayList<DialogModel>()

    //var click = context as openImages
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.home_buyer_adapter_shopping_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = data[position]
        val gson = Gson()
        val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
        getProfileModel = gson.fromJson(json, GetProfileModel::class.java)
        holder.itemView.productName.text = "${model.name}"
        holder.itemView.productUnit.text = "(${Constant.convertUnits(context, model.unit)})"
        holder.itemView.productPrice.text =
            "$${(Constant.roundValue(model.priceWithComission.toDouble()))}"
        if (model.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide
                .with(context)
                .load(model.image)
                .placeholder(R.drawable.product_placeholder)
               .apply(RequestOptions().override(200, 200).centerCrop())
                .into(holder.itemView.productImage)
        }
        holder.itemView.productImage.background=context.getDrawable(R.drawable.buyer_home_product_curve_rectangle)

        holder.itemView.productImage.setOnClickListener {
            val list = ArrayList<PostShoppingModel>()
            if (model.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change code image
                for (item in data) {
                    if (model.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                        val model = PostShoppingModel()
                        model.image = item.image
                        model.unit = item.unit
                        model.priceWithComission = item.priceWithComission
                        model.name = item.name
                        list.add(model)
                    }
                }
                intent.putExtra("keyImage", list)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
                Constant.apiHitOrNot = 1
                //   click.openImagesInterfaces(list, position)
            }

        }
    }

    fun update(
        products: ArrayList<DialogModel>,
        comissionPer: String
    ) {
        data = products
        comissionPers = comissionPer
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

//    interface openImages {
//        fun openImagesInterfaces(
//            list: ArrayList<PostShoppingModel>,
//            position: Int
//        )
//    }

}
