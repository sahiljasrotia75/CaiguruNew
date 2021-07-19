package com.example.caiguru.buyer.buyerOrder.buyerOrderComplete

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
import constant_Webservices.Constant
import android.text.style.StyleSpan
import android.widget.TextView
import com.example.caiguru.buyer.buyerOrder.buyerOrderDetails.FinalizeOrderDetailsActivity
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList.ToBeDeliveredOrderListActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.seller_order_cancelled_adapter.view.*


class BuyerCompleteAdapter(
    var context: Context

) : RecyclerView.Adapter<BuyerCompleteAdapter.ViewHolder>() {
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
        holder.itemView.timeAgoos.text = Constant.timesAgoLogic(list.created_at, context)
        Glide.with(context)
            .load(levelget(list.seller_level).levelImage)
            .into(holder.itemView.imgBatchs)

       val  textOrder = context.getString(R.string.your_order_with)

        val buyerName = list.seller_name
        val buyerName1 = SpannableString(list.seller_name)
        val text2 =
            "${textOrder} $buyerName ${context.getString(
                R.string.has_been_completed
            )}"
        val spannable = SpannableString(text2)
        val boldSpan = StyleSpan(Typeface.BOLD)

        //set the click on the spannable for hole text
//        val clickableSpanWholeText = object : ClickableSpan() {
//
//            override fun onClick(textView: View) {
//                val intent = Intent(context, ToBeDeliveredOrderListActivity::class.java)
//                intent.putExtra("keyBuyerDataCompleted", mData[position])
//                context.startActivity(intent)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }
        //set the click on the spannable
//        val clickableSpanforSigleText = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                val intent = Intent(context, OtherProfileViewActivity::class.java)
//                intent.putExtra("keyCompleted", mData[position])
//                context.startActivity(intent)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//
//        }

//        val clickableSpanStartingText = object : ClickableSpan() {
//            override fun onClick(textView: View) {
//                val intent = Intent(context, ToBeDeliveredOrderListActivity::class.java)
//                intent.putExtra("keyBuyerData", mData[position])
//                context.startActivity(intent)
//            }
//
//            override fun updateDrawState(ds: TextPaint) {
//                super.updateDrawState(ds)
//                ds.isUnderlineText = false
//            }
//        }
//        spannable.setSpan(
//            clickableSpanStartingText, 0,
//            textOrder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//set the  click
//        spannable.setSpan(
//            clickableSpanforSigleText, textOrder.length + 1,
//            buyerName1.length + textOrder.length + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )

        //set the  click
//        spannable.setSpan(
//            clickableSpanWholeText, buyerName1.length,
//            text2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
//        )


       // holder.itemView.txtDeliveredName.movementMethod = LinkMovementMethod.getInstance()
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

        if (list.seller_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.imged)
        } else {
            Glide.with(context).load(list.seller_image)
                .into(holder.itemView.imged)
        }


        //set the click on the image
        holder.itemView.relativeImage.setOnClickListener {
            val intent = Intent(context, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore", mData[position])
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FinalizeOrderDetailsActivity::class.java)
            intent.putExtra("KeyBuyerDetails", list)
            context.startActivities(arrayOf(intent))
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


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
