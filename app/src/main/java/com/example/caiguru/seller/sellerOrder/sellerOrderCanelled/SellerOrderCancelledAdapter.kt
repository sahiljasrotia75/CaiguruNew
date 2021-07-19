package com.example.caiguru.seller.sellerOrder.sellerOrderCanelled

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import android.text.style.StyleSpan
import android.widget.TextView
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList.SellerCancelledOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import kotlinx.android.synthetic.main.seller_order_cancelled_adapter.view.*
import kotlinx.android.synthetic.main.seller_to_be_delivered_adapter.view.txtDeliveredName


class SellerOrderCancelledAdapter(
    var context: Context

) : RecyclerView.Adapter<SellerOrderCancelledAdapter.ViewHolder>() {

    private lateinit var orderText: String
    private lateinit var list: SellerApprovalModel
    var mData = ArrayList<SellerApprovalModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.seller_order_cancelled_adapter, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list = mData[position]
        //set the level  image
        Glide.with(context)
            .load(levelget(list.buyer_level).levelImage)
            .into(holder.itemView.imgBatchs)

        val buyerName = list.buyer_name
        val buyerName1 = SpannableString(list.buyer_name)
        orderText = context.getString(R.string.Order_from)
        val text2 =
            "$orderText $buyerName ${
                context.getString(
                    R.string.has_been_cancelled
                )
            }"

        val spannable = SpannableString(text2)
        val boldSpan = StyleSpan(Typeface.BOLD)
        //set the click on the spannable
        val clickableSpanforSigleText = object : ClickableSpan() {
            override fun onClick(textView: View) {

//                var position = 0
//                for (i in 0 until mData.size) {
//                    val text2 =
//                        "$orderText ${mData[i].buyer_name} in \"${categoryData(mData[i].cat_id).name}\" ${context.getString(
//                            R.string.has_been_cancelled
//                        )}"
//                    val data = textView.txtDeliveredName.text.trim().toString()
//                    if (text2.equals(data)) {
//                        position = i
//                    }
//                }
//                if (list.list_type == "1") {
//                    val localModel = FinalizeModel()
//                    localModel.seller_id=list.seller_id
//                    localModel.cat_id=model.cat_id
//                    localModel.seller_name=model.seller_name
//                    val intent = Intent(context, SellerStoreActivity::class.java)
//                    intent.putExtra("keyOpenSellerStore",localModel)
//                    context.startActivity(intent)
                //} else {
                    val intent = Intent(context, OtherProfileViewActivity::class.java)
                    intent.putExtra("keyCancled", mData[position])
                    context.startActivity(intent)
               // }

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        //set the click start text
        val clickableSpanforStart = object : ClickableSpan() {
            override fun onClick(textView: View) {

//                var position = 0
//                for (i in 0 until mData.size) {
//                    val text2 =
//                        "$orderText ${mData[i].buyer_name} in \"${categoryData(mData[i].cat_id).name}\" ${context.getString(
//                            R.string.has_been_cancelled
//                        )}"
//                    val data = textView.txtDeliveredName.text.trim().toString()
//                    if (text2.equals(data)) {
//                        position = i
//                    }
//                }

                    val intent = Intent(context, SellerCancelledOrderListActivity::class.java)
                    intent.putExtra("keyCancled", mData[position])
                    context.startActivity(intent)


            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        //whole text click
        val clickableSpanforWholeText = object : ClickableSpan() {
            override fun onClick(textView: View) {

//                var position = 0
//                for (i in 0 until mData.size) {
//                    val text2 =
//                        "$orderText ${mData[i].buyer_name} in \"${categoryData(mData[i].cat_id).name}\" ${context.getString(
//                            R.string.has_been_cancelled
//                        )}"
//                    val data = textView.txtDeliveredName.text.trim().toString()
//                    if (text2.equals(data)) {
//                        position = i
//                    }
//                }
                val intent = Intent(context, SellerCancelledOrderListActivity::class.java)
                intent.putExtra("keyCancled", mData[position])
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
//start click
        spannable.setSpan(
            clickableSpanforStart, 0,
            buyerName1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //name click
        spannable.setSpan(
            clickableSpanforSigleText, orderText.length + 1,
            buyerName1.length + orderText.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //full text click
        spannable.setSpan(
            clickableSpanforWholeText, buyerName.length,
            text2.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        holder.itemView.txtDeliveredName.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            buyerName.length,
            text2.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            orderText.length + 1,
            buyerName1.length + orderText.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            0,
            orderText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(boldSpan, 11, buyerName1.length + 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.itemView.txtDeliveredName.setText(spannable, TextView.BufferType.SPANNABLE)

        if (list.buyer_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.imged)
        } else {
            Glide.with(context).load(list.buyer_image)
                .into(holder.itemView.imged)
        }
        holder.itemView.timeAgoos.text = Constant.timesAgoLogic(list.created_at, context)

        //set the click on the image
        holder.itemView.relativeImage.setOnClickListener {
            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyCancled", mData[position])
            context.startActivity(intent)

        }

    }


    private fun levelget(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(context)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
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

    fun upDate(it: ArrayList<SellerApprovalModel>) {
        mData = it
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
