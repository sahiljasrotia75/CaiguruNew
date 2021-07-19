package com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsSingleListActivity
import com.example.caiguru.buyer.chooseSellers.chooseSellerDeliveryDetails.ChooseSellerDeliveryDetailsActivity
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.choose_sellershopping_list_parent_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.recyclerChild
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.updown
import kotlinx.android.synthetic.main.switch_dialog.*
import org.json.JSONArray
import org.json.JSONObject


class ChooseSellerShoppingListParentAdapter(var context: ChooseSellerShoppingListActivity) :
    RecyclerView.Adapter<ChooseSellerShoppingListParentAdapter.ViewHolder>(),
    ChooseSellerShoppingListChildAdapter.UpdateTotal {
    private lateinit var dialog: Dialog

    //  private var getProfileModel = GetProfileModel()
    private var comission_per: String = ""
    private var homeDeliveryaddres = DeliveryZoneModel()
    var total = 0.0
    private var mData = ArrayList<ChooseSellerShoppingModel>()
    lateinit var recyclerAdapter: ChooseSellerShoppingListChildAdapter
    var click = context as reportListInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context)
            .inflate(R.layout.choose_sellershopping_list_parent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        comission_per = data.comission_per
        holder.itemView.PartialComission.text =
            "${context.getString(R.string.partial_payment)} ${context.getString(R.string.partial_money)}"
//set the total credits
        try {
            val credits = Constant.getProfileData(context).credits.toDouble().toInt().toString()
            holder.itemView.txtTotalWalletCredit.text =
                "(${context.getString(R.string.credit_in_account)} ${credits} ${context.getString(
                    R.string.cr
                )})"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val img = context.getResources().getDrawable(R.drawable.ic_baseline_info_24)
//        holder.itemView.txtTotalWalletCredit.setCompoundDrawablesWithIntrinsicBounds(
//            null,
//            null,
//            img,
//            null
//        )
        //************cash on delivery info****************//
        holder.itemView.cashOnDelivery.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            img,
            null
        )

        if (data.cashOnDelivery.isEmpty()) {
            holder.itemView.txtCashOnDelivery.text =
                "$" + "0.0"
        } else {
            holder.itemView.txtCashOnDelivery.text =
                "$" + data.cashOnDelivery
        }

        if (data.PartialComission.isEmpty()) {
            holder.itemView.txtPartialpayments.text =
                "0.0" + context.getString(R.string.cr)
        } else {
            holder.itemView.txtPartialpayments.text =
                data.PartialComission + context.getString(R.string.cr)
        }

        holder.itemView.txtlist.text = categoryData(data.cat_id)
        //  holder.itemView.listName.text = data.listingname
//        holder.itemView.comission.text =
//            context.getString(R.string.Comission) + "(" + data.comission_per + "%" + ")"
        //set the text
        val udata = context.getString(R.string.Delivery_Details)
        val text = SpannableString(udata)
        text.setSpan(UnderlineSpan(), 0, udata.length, 0)
        holder.itemView.deliveryDetailed.text = text
        //report list text
        val reportText = context.getString(R.string.report_list)
        val textrepo = SpannableString(reportText)
        textrepo.setSpan(UnderlineSpan(), 0, textrepo.length, 0)
        holder.itemView.txtReport.text = textrepo
        if (data.isExpanded) {
            holder.itemView.relative.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.relative.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }

        //set the click on Cardview
        holder.itemView.cardVieW.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyItemChanged(position)
            // notifyDataSetChanged()
        }
        holder.itemView.minimum_order.text =
            "${context.getString(R.string.minimum_order_msg)} \$${Constant.roundValue(data.minimum_purchase_amount.toDouble())}"
        //************************CustomerListChildAdapter****************************//

        recyclerAdapter = ChooseSellerShoppingListChildAdapter(context, this)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
//*************scroll disable on clik
        holder.itemView.relative.setFocusableInTouchMode(false)
        holder.itemView.relative.setFocusable(false)

        recyclerAdapter.updateData(mData, position)


        //***********************purchase click for single list***************//
        holder.itemView.purchase.setOnClickListener {
            var minimumPurchsedAmount = 0.0
            var total = 0.0
            total = setToTal(mData[position].product_details)
            minimumPurchsedAmount = data.minimum_purchase_amount.toDouble()
            if (setToTal(mData[position].product_details) <= 0) {

                Constant.showToast(context.getString(R.string.Please_Select_Quantity), context)
            } else if (total < minimumPurchsedAmount) {
                Constant.showToast(
                    context.getString(R.string.Total_should_be_high_than_Minimum_order_amount),
                    context
                )
            } else {
                dialog = Dialog(context)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.switch_dialog)
                //default textShow
                dialog.txtHeader.text = context.getString(R.string.alert)
                dialog.txtMessage.text = context.getString(R.string.purchase_shopping_alert)
                dialog.yes.text = context.getString(R.string.buy)
                dialog.no.text = context.getString(R.string.cancel)
                dialog.show()
                dialog.yes.setOnClickListener {
                    val childData: ArrayList<PostShoppingModel> = recyclerAdapter.getchildData()
                    val jsonStr = getJsonString(childData, data, position)
                    val intent = Intent(context, PayCreditsSingleListActivity::class.java)
                    intent.putExtra("keydata", mData[position])
                    intent.putExtra("jsonStr", jsonStr)
                    intent.putExtra("homeDeliveryaddres", homeDeliveryaddres)
                    context.startActivity(intent)
                    dialog.dismiss()
                }
                //no click
                dialog.no.setOnClickListener {
                    dialog.dismiss()
                }
            }

        }

        //**********************delivery details click**********************//
        holder.itemView.deliveryDetailed.setOnClickListener {
            val intent = Intent(context, ChooseSellerDeliveryDetailsActivity::class.java)
            intent.putExtra("keydetails", data)
            context.startActivity(intent)
        }

        //**********************delivery details click**********************//
        holder.itemView.txtReport.setOnClickListener {
            val mDialog = AlertDialog.Builder(context)
            mDialog.setTitle(context.getString(R.string.alert))
            mDialog.setCancelable(true)
            mDialog.setMessage(context.getString(R.string.report_text3))
            mDialog.setPositiveButton(
                context.getString(R.string.ok)
            ) { dialog, which ->
                click.reportList(data)
                dialog.cancel()
            }
            mDialog.setNegativeButton(
                context.getString(R.string.cancel)
            ) { dialog, which ->
                dialog.cancel()
            }
            mDialog.show()


        }
//set the data
        if (data.total.isEmpty()) {
            holder.itemView.price.text = "$" + setToTal(mData[position].product_details).toString()

            mData[position].total = setToTal(mData[position].product_details).toString()
            mData[position].credits = getCredit(
                setToTal(mData[position].product_details),
                mData[position]
            )
        } else {
            holder.itemView.price.text = "$" +
                    (Constant.roundValue(data.total.toDouble()))
        }

///set the click on info  credits
//        holder.itemView.txtTotalWalletCredit.setOnClickListener {
//            Constant.customDialogShowTotalCredits(context)
//        }
        //****************set the click on cash on delivery
        holder.itemView.cashOnDelivery.setOnClickListener {
            Constant.cashOnDeliveryDialog(context)
        }
    }

    private fun getCredit(
        toTal: Double,
        chooseSellerShoppingModel: ChooseSellerShoppingModel
    ): String {
        return (toTal * chooseSellerShoppingModel.comission_per.toDouble() / 100).toString()
    }


    //    private fun setToTal(
//        child: ArrayList<PostShoppingModel>
//    ): Double {
//        var total = 0.0
//        for (item in child) {
//            if (total == 0.0) {
//                val totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
//                val prices = item.price.toDouble() / 100 * totalComission
//                val maxPrice = item.price.toDouble() + prices
//                total = maxPrice * item.qty.toDouble()
//            } else {
//                val totalComission=comission_per.toDouble()+getProfileModel.plateform_commission.toDouble()
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
                total = item.priceWithComission.toDouble() * item.qty.toDouble()
            } else {
                total += item.priceWithComission.toDouble() * item.qty.toDouble()
            }
        }
        return total
    }

    //single purchase
    private fun getJsonString(
        childData: ArrayList<PostShoppingModel>,
        data: ChooseSellerShoppingModel,
        position: Int
    ): String {

        val parentArray = JSONArray()
        val productArray = JSONArray()
        val jsonObj = JSONObject()
        for (i in 0 until mData[position].product_details.size) {
            val jsonObj = JSONObject()
            jsonObj.put("id", mData[position].product_details[i].id)
            jsonObj.put("qty", mData[position].product_details[i].qty)
            jsonObj.put("price", mData[position].product_details[i].price)
            jsonObj.put("priceWithComission", mData[position].product_details[i].priceWithComission)
            jsonObj.put("name", mData[position].product_details[i].name)
            jsonObj.put("image", mData[position].product_details[i].image)
            jsonObj.put("unit", mData[position].product_details[i].unit)
            productArray.put(jsonObj)
        }
        jsonObj.put("products", productArray)
        jsonObj.put("list_id", mData[position].id)
        jsonObj.put("delivery_type", mData[position].delivery_type)
        jsonObj.put("amount", mData[position].total)
//        jsonObj.put(
//            "credits",
//            ((mData[position].total.toDouble() * mData[position].comission_per.toDouble()) / 100).toString()
//        )
        jsonObj.put("credits", mData[position].PartialComission)
        return parentArray.put(jsonObj).toString()
    }


    //***************** Matching id's *********************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return context.getString(R.string.mix_category_product)
    }


    fun update(
        it: ArrayList<ChooseSellerShoppingModel>,
        homeDeliveryaddress: DeliveryZoneModel
    ) {
        homeDeliveryaddres = homeDeliveryaddress
        if (it != null) {
            mData = it
        }
        for (item in it[0].product_details) {
            if (total == 0.0) {
                total = item.price.toDouble() * item.qty.toDouble()
            } else {
                total = total + item.price.toDouble() * item.qty.toDouble()
            }
        }
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


    //*****************interface data************//
//
    override fun updateTotal(
        toTal: ArrayList<ChooseSellerShoppingModel>
    ) {

        mData = toTal
        notifyDataSetChanged()
    }


    override fun SendChildAllData(list: PostShoppingModel) {


    }

    fun getUpdatesArray(): ArrayList<ChooseSellerShoppingModel> {
        //  mSucessfullData.add(data)
        return mData
    }

    interface reportListInterface {
        fun reportList(data: ChooseSellerShoppingModel)
    }

}


