package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.caiguru.R
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.seller_post_shopping_adapter.view.*
import java.util.*
import kotlin.collections.ArrayList


class PostShoppingAdapter(var context: Context) : RecyclerView.Adapter<PostShoppingAdapter.Viewholder>() {

    private var profileData = GetProfileModel()
    private var comissionPer: Int = 0
    var list: ArrayList<PostShoppingModel> = ArrayList()
    var click = context as editDataInterface//click

//    inner class MyViewHolder(var rowView: View) : RecyclerView.ViewHolder(
//        rowView
//    ) {
////
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.seller_post_shopping_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {

        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = list[position]

        if (position == 0) {
            holder.itemView.btnChnageOrder.visibility = View.VISIBLE
        } else {
            holder.itemView.btnChnageOrder.visibility = View.GONE
        }

        holder.itemView.btnChnageOrder.setOnClickListener {
            click.dragDropListClick(list)
        }

        val gson = Gson()
        val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
        profileData = gson.fromJson(json, GetProfileModel::class.java)
        data.setProductPosition = position + 1
        holder.itemView.productid.text =
            "#${context.getString(R.string.product)} ${data.setProductPosition}"
        //  holder.itemView.txtproductname.text = data.name
        holder.itemView.quantity.text =
            data.name + "(" + Constant.convertUnits(context, data.unit) + ")"

        if (data.price != "") {
            val allComission = profileData.plateform_commission.toDouble() + comissionPer.toDouble()
            holder.itemView.txtprice.text =
                context.getString(R.string.Price) + ":" + " " + "$" + (Constant.roundValue(data.price.toDouble()))
            val PriceComission = data.price.toDouble() / 100 * allComission
            val allTotal = data.price.toDouble() + PriceComission
            data.priceWithComission = Constant.roundValue(allTotal)
            holder.itemView.txtPriceWithComission.text =
                context.getString(R.string.Price_with_comission_Without_star) + ":" + " " + "$" + data.priceWithComission
        }

        holder.itemView.imgEdtfieldMarker.visibility = View.VISIBLE
        holder.itemView.imgdeleteData.visibility = View.VISIBLE
        Glide.with(context).load(data.image).apply(RequestOptions().override(150, 150).centerCrop()).into(holder.itemView.addimg)

        //set the click on the delete items
        holder.itemView.imgdeleteData.setOnClickListener {
            click.deleteShoppingList(list, position)

        }

        holder.itemView.imgEdtfieldMarker.setOnClickListener {
            click.edtData(data, position)
        }
    }

    fun getAllShoppingArrayData(): ArrayList<PostShoppingModel> {
        return list
    }

//    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
//        var myLocalPOsition = 0
//        if (fromPosition < toPosition) {
//            for (i in fromPosition until toPosition) {
//                Collections.swap(list, i, i + 1)
//            }
//        } else {
//            for (i in fromPosition downTo toPosition + 1) {
//                Collections.swap(list, i, i - 1)
//            }
//        }
//        for (item in list) {
//            item.setProductPosition = 0
//            myLocalPOsition += 1
//            item.setProductPosition = myLocalPOsition
//
//        }
//        notifyItemMoved(fromPosition, toPosition)
//    }

//    override fun onRowSelected(myViewHolder: PostShoppingAdapter.MyViewHolder) {
//        myViewHolder.rowView.setBackgroundColor(Color.GRAY)
//    }
//
//    override fun onRowClear(myViewHolder: PostShoppingAdapter.MyViewHolder) {
//        myViewHolder.rowView.setBackgroundColor(Color.WHITE)
//    }


    fun update(
        data: ArrayList<PostShoppingModel>,
        progress: Int
    ) {
        comissionPer = progress
        list = data
        notifyDataSetChanged()

    }

    class Viewholder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    interface editDataInterface {

        fun edtData(
            data: PostShoppingModel,
            position: Int
        )

        fun deleteShoppingList(
            list: ArrayList<PostShoppingModel>,
            position: Int
        )

        fun dragDropListClick(list: ArrayList<PostShoppingModel>)
    }

    fun getAllDataArraySize(): Int {
        return list.size
    }

}
