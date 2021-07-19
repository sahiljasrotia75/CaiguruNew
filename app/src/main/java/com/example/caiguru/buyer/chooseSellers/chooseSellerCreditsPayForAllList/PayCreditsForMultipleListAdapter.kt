package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForAllList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.pay_credits_for_multiple_list_adapter.view.*

class PayCreditsForMultipleListAdapter(var context: PayCreditsMultipleListActivity) :
    RecyclerView.Adapter<PayCreditsForMultipleListAdapter.Viewholder>() {
    private  var homeDeliveryaddres = DeliveryZoneModel()
    private var deliveryType: String = ""
    var arrayData = ArrayList<ChooseSellerShoppingModel>()
    var days: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.pay_credits_for_multiple_list_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return arrayData.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val arrayModel = arrayData[position]
        holder.itemView.txtListName.text = arrayModel.listingname//list name
        holder.itemView.txtPrice.text = "$" +Constant.roundValue(arrayModel.cashOnDelivery.toDouble())
        //getting days
        for (item in arrayModel.delivery_daytime) {
            val days = updatedDayTime(arrayModel.delivery_daytime)
            holder.itemView.txtdeliveryDays.text = days
        }
        //getting type
        val type = arrayModel.delivery_type
        if (type == 1) {
            deliveryType = context.getString(R.string.Self_Pickup)
            holder.itemView.txtdeliverytype.text =
                context.getString(R.string.Pickup_at_seller_address)
            holder.itemView.headdeliveyDays.text = context.getString(R.string.Pickup_days)
            holder.itemView.headdeliveyAddress.text = context.getString(R.string.Pickup_Distance)
            holder.itemView.txtCashOnDelivery.text =
                context.getString(R.string.Payment_amount_on_pickup)
            //show only self pickup time
            arrayModel.pickup_details.days
            for (item in arrayModel.pickup_details.days) {

                val days = updatedDayTime(arrayModel.pickup_details.days)
                holder.itemView.txtdeliveryDays.text = days
            }
            //set the address
            if (arrayModel.distance == "") {

                holder.itemView.txtdeliveryAddress.text =
                    "0.0 " +" "+ "${context.getString(R.string.aways)}"

            } else {

                holder.itemView.txtdeliveryAddress.text  =
                    "("+arrayModel.distance + "${context.getString(R.string.aways)})"

            }
        } else {
            deliveryType = context.getString(R.string.Home_Delivery)
            holder.itemView.txtdeliverytype.text = deliveryType
            holder.itemView.headdeliveyDays.text = context.getString(R.string.Delivery_Days)
            holder.itemView.headdeliveyAddress.text = context.getString(R.string.Delivery_Address)
            holder.itemView.txtCashOnDelivery.text = context.getString(R.string.Cash_on_Delivery)
            //show only delivery details time
            arrayModel.delivery_daytime
            for (item in arrayModel.delivery_daytime) {
                val days = updatedDayTime(arrayModel.delivery_daytime)
                holder.itemView.txtdeliveryDays.text = days

            }
            //set the address
            holder.itemView.txtdeliveryAddress.text = homeDeliveryaddres.address
        }

    }


    private fun updatedDayTime(daysArraymodel: ArrayList<DaysParentModel>): String {
        var result: String = ""

        for (item in daysArraymodel) {

            for (child in item.value) {

                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                        Constant.getDayString(context,item.day) + "(" +  Constant.ConvertAmPmFormat(context!!,child.from) + "-" +Constant.ConvertAmPmFormat(context!!,child.to) + ")"

                    } else {
                        if (result.contains(item.day)) {
                            result + ", " + "(" + Constant.ConvertAmPmFormat(context!!,child.from) + "-" + Constant.ConvertAmPmFormat(context!!,child.to) + ")"
                        } else {
                            result + ", " +  Constant.getDayString(context,item.day)+ "(" + Constant.ConvertAmPmFormat(context!!,child.from) + "-" + Constant.ConvertAmPmFormat(context!!,child.to) + ")"
                        }
                    }
                }
            }

        }
        if (result.isEmpty()) {
            result = context.resources.getString(R.string.Delivery_Time)
        }

        return result
    }

    fun Update(
        daysParentModel: ArrayList<ChooseSellerShoppingModel>,
        homeDeliveryaddress: DeliveryZoneModel
    ) {
        homeDeliveryaddres = homeDeliveryaddress
        arrayData = daysParentModel
        notifyDataSetChanged()
    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {

    }
}