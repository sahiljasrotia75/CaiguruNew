package com.example.caiguru.buyer.buyerOrder.finalizeOrder

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
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation.CommonNotificationConfirmationActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList.SellerCancelledOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.seller_order_cancelled_adapter.view.*
import kotlinx.android.synthetic.main.seller_to_be_delivered_adapter.view.txtDeliveredName


class FinalizeOrderAdapter(
    var context: Context

) : RecyclerView.Adapter<FinalizeOrderAdapter.ViewHolder>() {

    var mData = ArrayList<FinalizeModel>()

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

        val startTitle = context.getString(R.string.finalize_your_purchase)
        val name = list.seller_name
        val allString = startTitle + " " + name

        val spannable = SpannableString(allString)
        val boldSpan = StyleSpan(Typeface.BOLD)
        //set the click on the spannable
//        val clickableSpanforSigleText = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//
//                val intent = Intent(context, OtherProfileViewActivity::class.java)
//                intent.putExtra("keyCancled", mData[position])
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
//
//                val intent = Intent(context, SellerCancelledOrderListActivity::class.java)
//                intent.putExtra("keyCancled", mData[position])
//                context.startActivity(intent)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }

        //name click
//        spannable.setSpan(
//            clickableSpanforSigleText, startTitle.length + 1,
//            name.length + startTitle.length+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        //full text click
//        spannable.setSpan(
//            clickableSpanforWholeText, 0,
//            startTitle.length,
//            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )

        //  holder.itemView.txtDeliveredName.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            0,
            startTitle.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            startTitle.length + 1,
            name.length + startTitle.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            boldSpan, startTitle.length + 1,
            name.length + startTitle.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
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

        //set the click on the image
        holder.itemView.relativeImage.setOnClickListener {
//            val localModel = FinalizeModel()
//            localModel.seller_id=list.seller_id
//            localModel.cat_id=list.cat_id
//            localModel.seller_name=list.seller_name
//            val intent = Intent(context, SellerStoreActivity::class.java)
//            intent.putExtra("keyOpenSellerStore",localModel)
//            intent.putExtra("KeyOpenSellerPrefillData","")
//            context.startActivity(intent)

        }

        holder.itemView.setOnClickListener {
            if (list.req_status == "9") {
                val localModel = FinalizeModel()
                localModel.seller_id=list.seller_id
                localModel.cat_id=list.cat_id
                localModel.seller_name=list.seller_name
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                intent.putExtra("KeyOpenSellerPrefillData","")

                context.startActivity(intent)
            } else {
                val intent =
                    Intent(context, CommonNotificationConfirmationActivity::class.java)
                intent.putExtra("keyFinalizeOrder", list)
                context.startActivity(intent)
            }

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


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}


}
