package com.example.caiguru.seller.sellerOrder.sellerOrderCompleted

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
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList.ToBeDeliveredOrderListActivity
import kotlinx.android.synthetic.main.seller_to_be_delivered_adapter.view.*


class SellerOrderCompletedAdapter(var context: Context) :
    RecyclerView.Adapter<SellerOrderCompletedAdapter.ViewHolder>() {

    private lateinit var textOrder: String
    private var list = SellerApprovalModel()
    var mData = ArrayList<SellerApprovalModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.seller_to_be_delivered_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list = mData[position]
        //set the level  image
        holder.itemView.timeAgos.text = Constant.timesAgoLogic(list.created_at, context)
        Glide.with(context)
            .load(levelget(list.buyer_level).levelImage)
            .into(holder.itemView.imgBatchs)
        textOrder = context.getString(R.string.Order_from)
        val buyerName = list.buyer_name
        val buyerName1 = SpannableString(list.buyer_name)
        val text2 =
            "${textOrder} $buyerName ${context.getString(
                R.string.has_been_completed
            )}"
        val spannable = SpannableString(text2)
        val boldSpan = StyleSpan(Typeface.BOLD)

        //set the click on the spannable for hole text
        val clickableSpanWholeText = object : ClickableSpan() {

            override fun onClick(textView: View) {
                val intent = Intent(context, ToBeDeliveredOrderListActivity::class.java)
                intent.putExtra("keyBuyerDataCompleted", mData[position])
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        //set the click on the spannable
        val clickableSpanforSigleText = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyCompleted", mData[position])
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }

        val clickableSpanStartingText = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(context, ToBeDeliveredOrderListActivity::class.java)
                intent.putExtra("keyBuyerData", mData[position])
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(
            clickableSpanStartingText, 0,
            textOrder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//set the  click
        spannable.setSpan(
            clickableSpanforSigleText, textOrder.length + 1,
            buyerName1.length + textOrder.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        //set the  click
        spannable.setSpan(
            clickableSpanWholeText, buyerName1.length,
            text2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        holder.itemView.txtDeliveredName.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            buyerName1.length,
            text2.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            0,
            buyerName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            textOrder.length + 1,
            buyerName.length + textOrder.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            boldSpan,
            textOrder.length + 1,
            buyerName1.length + textOrder.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.itemView.txtDeliveredName.setText(spannable, TextView.BufferType.SPANNABLE)

        if (list.buyer_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.imges)
        } else {
            Glide.with(context).load(list.buyer_image)
                .into(holder.itemView.imges)
        }

        //set the clik on the image
        holder.itemView.setOnClickListener {
            var position = 0
            for (i in 0 until mData.size) {
                val text2 =
                    "${textOrder} ${mData[i].buyer_name} ${context.getString(R.string.In)}\"${categoryData(
                        mData[i].cat_id
                    )}\" ${context.getString(
                        R.string.has_been_completed
                    )}"

                val dd = holder.itemView.txtDeliveredName.text.toString()
                if (text2 == dd) {
                    position = i
                }
            }
            val intent = Intent(context, ToBeDeliveredOrderListActivity::class.java)
            intent.putExtra("keyBuyerDataCompleted", mData[position])
            context.startActivity(intent)
        }
        //set the click on the image
        holder.itemView.relativeImage.setOnClickListener {

            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyCompleted", mData[position])
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
        return  context.getString(R.string.mix_category_product)
    }

    fun upDate(it: ArrayList<SellerApprovalModel>) {
        mData = it
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
