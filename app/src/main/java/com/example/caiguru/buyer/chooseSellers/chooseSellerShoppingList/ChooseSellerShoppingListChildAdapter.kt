package com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import kotlinx.android.synthetic.main.choose_sellershopping_list_child_adapter.view.*
import com.example.caiguru.commonScreens.imageFullScreenDisplay.ImageFullScreenActivity
import constant_Webservices.Constant


class ChooseSellerShoppingListChildAdapter(
    var context: Context,
    chooseSellerShoppingListParentAdapter: ChooseSellerShoppingListParentAdapter

) : RecyclerView.Adapter<ChooseSellerShoppingListChildAdapter.ViewHolder>() {
    private var comission_per: String = ""
    private var product_detail: ArrayList<ChooseSellerShoppingModel> = ArrayList()
    private var gPosition: Int = -1
    var click = chooseSellerShoppingListParentAdapter as UpdateTotal
    var child = ArrayList<PostShoppingModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.choose_sellershopping_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
//        val gson = Gson()
//        val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
//        getProfileModel = gson.fromJson(json, GetProfileModel::class.java)

        click.SendChildAllData(list)

        //changing ********************************************************************************
        holder.itemView.productName.text =
            list.name +" "+ "(" + Constant.convertUnits(context, list.unit) + ")"
        //***************code change adding comission of every products
//        var totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
//        val totalPrice = list.price.toDouble() / 100 * totalComission
        holder.itemView.prices.text = "$" + Constant.roundValue(list.priceWithComission.toDouble())
        holder.itemView.qty.text = list.qty
        if (list.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide.with(context).load(list.image).placeholder(R.drawable.product_placeholder).into(holder.itemView.productImage)
        }
        //set the click on the image
        holder.itemView.productImage.setOnClickListener {
            if (list.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change in code ysp
                intent.putExtra("keyImage", child)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }
        }

        holder.itemView.subtractbtn.setOnClickListener {
            if (child[position].qty.toInt() > 0) {
                child[position].qty = (child[position].qty.toInt() - 1).toString()
            }
            product_detail[gPosition].total = setToTal(child).toString()
            product_detail[gPosition].credits = getCredit(setToTal(child))
            //partial comission
            product_detail[gPosition].PartialComission =Constant.roundValue( setTotalPartialComission(product_detail[gPosition].product_details))

            //cash on delivery
            val cashOnDelivery = setToTal(child) - setTotalPartialComission(product_detail[gPosition].product_details)
            product_detail[gPosition].cashOnDelivery=Constant.roundValue(cashOnDelivery)

            click.updateTotal(product_detail)
        }

        holder.itemView.addBtn.setOnClickListener {

            child[position].qty = (child[position].qty.toInt() + 1).toString()
            product_detail[gPosition].total = setToTal(child).toString()
            product_detail[gPosition].credits = getCredit(setToTal(child))
            //cash on delivery
           val cashOnDelivery = setToTal(child) - setTotalPartialComission(product_detail[gPosition].product_details)
            product_detail[gPosition].cashOnDelivery=Constant.roundValue(cashOnDelivery)
            //partial comission
            product_detail[gPosition].PartialComission =
                Constant.roundValue( setTotalPartialComission(product_detail[gPosition].product_details))

            click.updateTotal(product_detail)

            notifyItemChanged(position)
        }


    }

    private fun getCredit(toTal: Double): String {
        return (toTal / 100).toString()
    }

//    private fun setToTal(
//        child: ArrayList<PostShoppingModel>
//    ): Double {
//        var total = 0.0
//        for (item in child) {
//            if (total == 0.0) {
//                var totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
//                val prices = item.price.toDouble() / 100 * totalComission
//                val maxPrice = item.price.toDouble() + prices
//                total = maxPrice * item.qty.toDouble()
//            } else {
//                var totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
//                val prices = item.price.toDouble() / 100 * totalComission
//                val maxPrice = item.price.toDouble() + prices
//                total += maxPrice * item.qty.toDouble()
//            }
//        }
//        return total
//    }
private fun setToTal(
    child: ArrayList<PostShoppingModel>
): Double {
    var total = 0.0
    for (item in child) {
        if (total == 0.0) {
            total =  item.priceWithComission.toDouble() * item.qty.toDouble()
        } else {
            total += item.priceWithComission.toDouble() * item.qty.toDouble()
        }
    }
    return total
}

    fun updateData(
        product_details: ArrayList<ChooseSellerShoppingModel>,
        position: Int
    ) {
        product_detail = product_details
        gPosition = position
        comission_per = product_details[position].comission_per
        child = product_details[position].product_details
        notifyDataSetChanged()
    }

//    private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
//        //set the  total comission of the products
//        var totalPatialComission = 0.0
//        for (item in data) {
//            if (totalPatialComission == 0.0) {
//                val allTotal = item.price.toDouble()  * item.qty.toDouble()
//                var totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
//                totalPatialComission = allTotal / 100 * totalComission
//            } else {
//                val allTotal = item.price.toDouble()  * item.qty.toDouble()
//                var totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
//                totalPatialComission += allTotal / 100 * totalComission
//            }
//        }
//        return totalPatialComission
//    }
private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
    var totalPatialComission = 0.0
    for (item in data) {
        if (totalPatialComission == 0.0) {
            val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
            totalPatialComission = allTotal * item.qty.toDouble()
        } else {
            val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
            totalPatialComission += allTotal * item.qty.toDouble()
        }
    }
    return totalPatialComission
}
    fun getchildData(): ArrayList<PostShoppingModel> {
        return child

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    interface UpdateTotal {
        fun SendChildAllData(list: PostShoppingModel)
        fun updateTotal(
            toTal: ArrayList<ChooseSellerShoppingModel>
        )
    }

}
