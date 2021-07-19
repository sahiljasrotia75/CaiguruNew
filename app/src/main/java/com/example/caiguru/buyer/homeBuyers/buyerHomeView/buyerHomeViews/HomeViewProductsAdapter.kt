package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews

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
import kotlinx.android.synthetic.main.choose_sellershopping_list_child_adapter.view.*

class HomeViewProductsAdapter(var context: Context) :
    RecyclerView.Adapter<HomeViewProductsAdapter.ViewHolder>() {

    private var comissionPers: String = ""
    private lateinit var isBuyBtnShows: String
    var list: ArrayList<PostShoppingModel> = ArrayList()
    var click = context as setTotalInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.choose_sellershopping_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]


        holder.itemView.productName.text =
            model.name + " " + "(" + Constant.convertUnits(context, model.unit) + ")"
        holder.itemView.qty.text = model.qty

        if (model.price.isEmpty() || model.price <= "0") {
            holder.itemView.prices.text = "$0"
        } else {
            holder.itemView.prices.text =
                "$" + (Constant.roundValue(model.priceWithComission.toDouble()))
        }
        if (model.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder).placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide.with(context)
                .load(model.image)
                .placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        }
        holder.itemView.productImage.setOnClickListener {

            if (model.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change image code
                intent.putExtra("keyImage", list)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        }
        if (isBuyBtnShows == "1") {
            holder.itemView.qty.visibility = View.VISIBLE
            holder.itemView.addBtn.visibility = View.VISIBLE
            holder.itemView.subtractbtn.visibility = View.VISIBLE
        } else {
            holder.itemView.qty.visibility = View.GONE
            holder.itemView.addBtn.visibility = View.GONE
            holder.itemView.subtractbtn.visibility = View.GONE
        }
        //set the click on the add quantity
        holder.itemView.addBtn.setOnClickListener {
            list[position].qty = (list[position].qty.toInt() + 1).toString()
            list[position].total = Total(list).toString()//set the total
            list[position].partialComission = setTotalPartialComission(list).toString()
            click.setTotal(list, position)
            notifyItemChanged(position)
        }
        //set the click on the subtrcat the quantity
        holder.itemView.subtractbtn.setOnClickListener {
            if (list[position].qty.toInt() > 0) {
                list[position].qty = (list[position].qty.toInt() - 1).toString()
            }
            list[position].total = Total(list).toString()//set the total
            list[position].partialComission =
                setTotalPartialComission(list).toString()//partial payments
            click.setTotal(list, position)
            notifyItemChanged(position)
        }
    }

    fun getAllData(): ArrayList<PostShoppingModel> {
        return list
    }

    //    private fun Total(list: ArrayList<PostShoppingModel>): Double {
//        var total = 0.0
//        for (item in list) {
//
//            if (total == 0.0) {
//                val totalComission =
//                    comissionPers.toDouble() + getProfileModel.plateform_commission.toDouble()
//                val prices = item.price.toDouble() / 100 * totalComission
//                val maxPrice = item.price.toDouble() + prices
//                total = maxPrice * item.qty.toDouble()
//            } else {
//                val totalComission =
//                    comissionPers.toDouble() + getProfileModel.plateform_commission.toDouble()
//                val prices = item.price.toDouble() / 100 * totalComission
//                val maxPrice = item.price.toDouble() + prices
//                total += maxPrice * item.qty.toDouble()
//            }
//        }
//        return Constant.roundValue(total).toDouble()
//
//    }
    private fun Total(list: ArrayList<PostShoppingModel>): Double {
        var total = 0.0
        for (item in list) {

            if (total == 0.0) {
                total = item.priceWithComission.toDouble() * item.qty.toDouble()
            } else {
                total += item.priceWithComission.toDouble() * item.qty.toDouble()
            }
        }
        return Constant.roundValue(total).toDouble()

    }

    fun update(
        it: ArrayList<PostShoppingModel>,
        isBuyBtnShow: String,
        comissionPer: String
    ) {
        isBuyBtnShows = isBuyBtnShow
        list = it
        comissionPers = comissionPer
        notifyDataSetChanged()

    }

    //    private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
//        //set the  total comission of the products
//        var totalPatialComission = 0.0
//        for (item in data) {
//            if (totalPatialComission == 0.0) {
//                val allTotal = item.price.toDouble()  * item.qty.toDouble()
//                val totalComission=comissionPers.toDouble()+getProfileModel.plateform_commission.toDouble()
//                totalPatialComission = allTotal / 100 * totalComission
//            } else {
//                val allTotal = item.price.toDouble()  * item.qty.toDouble()
//                val totalComission=comissionPers.toDouble()+getProfileModel.plateform_commission.toDouble()
//                totalPatialComission += allTotal / 100 * totalComission
//            }
//        }
//        return totalPatialComission
//    }
    private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
        //set the  total comission of the products
        var totalPatialComission = 0.0
        for (item in data) {
            if (totalPatialComission == 0.0) {
                val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                //  val totalComission=comissionPers.toDouble()+getProfileModel.plateform_commission.toDouble()
                totalPatialComission = allTotal * item.qty.toDouble()
            } else {
                val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
//                val totalComission =
//                    comissionPers.toDouble() + getProfileModel.plateform_commission.toDouble()
                totalPatialComission += allTotal * item.qty.toDouble()
            }
        }
        return totalPatialComission
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface setTotalInterface {


        fun setTotal(
            list: ArrayList<PostShoppingModel>,
            position: Int
        )
    }

}