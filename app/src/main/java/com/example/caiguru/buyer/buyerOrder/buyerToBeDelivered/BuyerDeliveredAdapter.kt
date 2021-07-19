package com.example.caiguru.buyer.buyerOrder.buyerToBeDelivered

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
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import android.text.style.StyleSpan
import android.widget.TextView
import com.example.caiguru.buyer.buyerOrder.buyerOrderDetails.FinalizeOrderDetailsActivity
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails.UnderReviewOrderDetailsActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation.CommonNotificationConfirmationActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.seller_order_cancelled_adapter.view.*
import kotlinx.android.synthetic.main.seller_to_be_delivered_adapter.view.txtDeliveredName


class BuyerDeliveredAdapter(
    var context: Context

) : RecyclerView.Adapter<BuyerDeliveredAdapter.ViewHolder>() {

    var mData = ArrayList<FinalizeModel>()
    var click = context as setAdapterInterface
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
        val list = mData[position]

        //set the level  image
        Glide.with(context)
            .load(levelget(list.seller_level).levelImage)
            .into(holder.itemView.imgBatchs)

        val endText = context.getString(R.string.still_has_to_deliver)
        val name = list.seller_name
        val allString = name + " " + endText

        val spannable = SpannableString(allString)
        val boldSpan = StyleSpan(Typeface.BOLD)
        //set the click on the spannable
//        val clickableSpanforSigleText = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//
//                val intent = Intent(context, SellerStoreActivity::class.java)
//                intent.putExtra("keyOpenSellerStore", list)
//                context.startActivity(intent)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }
        //whole text click
//        val clickableSpanforWholeText = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                click.setAdapterClick(list)
//                if (list.req_status == "7") {
//                    val model = FinalizeModel()
//                    model.id = list.id
//                    model.list_type = list.list_type
//
//                    val intent =
//                        Intent(context, CommonNotificationConfirmationActivity::class.java)
//                    intent.putExtra("keyFinalizeOrder", list)
//                    intent.putExtra("KeySourceCancellOrderConfirm", model)
//                    context.startActivity(intent)
//                }
//                else if (list.req_status == "6") {
//                    val model = OrderModel()
//                    model.req_id = list.id
//                    model.list_type = list.list_type
//                    val intent = Intent(context, UnderReviewOrderDetailsActivity::class.java)
//                    intent.putExtra("keydetails", model)
//                    context.startActivity(intent)
//                }
//                else {
//                    val intent = Intent(context, FinalizeOrderDetailsActivity::class.java)
//                    intent.putExtra("KeyBuyerDetails", list)
//                    intent.putExtra("KeyShowCaNcelBtnFromDeliveryTab", "")
//                    context.startActivity(intent)
//                }
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }

        //name click
//        spannable.setSpan(
//            clickableSpanforSigleText, 0,
//            name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        //full text click
//        spannable.setSpan(
//            clickableSpanforWholeText, name.length,
//            endText.length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )

        //holder.itemView.txtDeliveredName.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            0,
            name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.medium_grey)),
            name.length,
            endText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan, 0,
            name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.itemView.txtDeliveredName.setText(spannable, TextView.BufferType.SPANNABLE)

        if (list.seller_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.imged)
        } else {
            Glide.with(context).load(list.seller_image)
                .into(holder.itemView.imged)
        }

        holder.itemView.timeAgoos.text = Constant.timesAgoLogic(list.created_at, context)


        //  holder.itemView.relativeImage.setOnClickListener {
//            val intent = Intent(context, SellerStoreActivity::class.java)
//            intent.putExtra("keyOpenSellerStore", mData[position])
//            context.startActivity(intent)

        // }

        holder.itemView.setOnClickListener {
            click.setAdapterClick(list)

        }
    }


    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(context)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
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

    fun upDate(it: ArrayList<FinalizeModel>) {
        mData = it
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface setAdapterInterface {
        fun setAdapterClick(list: FinalizeModel)
    }
}
