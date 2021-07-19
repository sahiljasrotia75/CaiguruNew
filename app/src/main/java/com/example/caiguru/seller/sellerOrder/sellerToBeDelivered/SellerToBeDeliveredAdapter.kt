package com.example.caiguru.seller.sellerOrder.sellerToBeDelivered

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
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import kotlinx.android.synthetic.main.seller_to_be_delivered_adapter.view.*
import kotlinx.android.synthetic.main.seller_to_be_delivered_adapter.view.relativeImage


class SellerToBeDeliveredAdapter(
    var context: Context

) : RecyclerView.Adapter<SellerToBeDeliveredAdapter.ViewHolder>() {
    private lateinit var OtherText: String
    private lateinit var orderText: String
    private lateinit var buyerName: String
    private lateinit var buyerName1: SpannableString
    private lateinit var text2: String
    private lateinit var list: SellerApprovalModel
    var click = context as setAdapteClick
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
        if (list.delivery_type == "2") {
            holder.itemView.layoutRecycler.setBackgroundColor((context.resources.getColor(R.color.light_purple)))
            buyerName = list.buyer_name
            buyerName1 = SpannableString(list.buyer_name)

            holder.itemView.timeAgos.text = Constant.timesAgoLogic(list.created_at,context)
            orderText = context.getString(R.string.Order_from)
            text2 =
                "$orderText $buyerName ${context.getString(
                    R.string.is_to_be_delivered
                )}"
        } else {
            holder.itemView.layoutRecycler.setBackgroundColor((context.resources.getColor(R.color.yellow_light)))

            buyerName = list.buyer_name
            buyerName1 = SpannableString(list.buyer_name)

            holder.itemView.timeAgos.text = Constant.timesAgoLogic(list.created_at,context)
            orderText = context.getString(R.string.Order_from)
            OtherText= "${context.getString(R.string.In)} \"${categoryData(list.cat_id)}\" ${context.getString(
                R.string.to_be_picked_up_at_your_address)}"
            text2 =
                "$orderText $buyerName ${context.getString(R.string.In)} \"$OtherText"

        }
        Glide.with(context)
            .load(levelget(list.buyer_level).levelImage)
            .into(holder.itemView.imgBatchs)


        val spannable = SpannableString(text2)
        val boldSpan = StyleSpan(Typeface.BOLD)
        //set the click on the spannable
        val clickableSpanforSigleText = object : ClickableSpan() {
            override fun onClick(textView: View) {
//                if (list.list_type=="1"){
//                    val localModel = FinalizeModel()
//                    localModel.seller_id=list.buyer_id
//                    localModel.cat_id=list.cat_id
//                    localModel.seller_name=list.buyer_name
//                    val intent = Intent(context, SellerStoreActivity::class.java)
//                    intent.putExtra("keyOpenSellerStore",localModel)
//                    context.startActivity(intent)
//                }else{
                    val intent = Intent(context, OtherProfileViewActivity::class.java)
                    intent.putExtra("keydata", mData[position])
                    context.startActivity(intent)
               // }

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        //set the click on the spannable for hole text
        val clickableSpanWholeText = object : ClickableSpan() {
            override fun onClick(textView: View) {
                click.setClick(mData[position])
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(
            clickableSpanforSigleText, orderText.length,
            buyerName1.length + orderText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //for all text
        spannable.setSpan(
            clickableSpanWholeText, 0,
            text2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

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
        text2 =
            "$orderText $buyerName ${context.getString(R.string.In)} \"${categoryData(list.cat_id)}\" ${context.getString(
                R.string.is_to_be_delivered
            )}"
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            0,
            orderText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )




        holder.itemView.txtDeliveredName.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(boldSpan,   orderText.length + 1,buyerName1.length + orderText.length + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.itemView.txtDeliveredName.setText(spannable, TextView.BufferType.SPANNABLE)

        if (list.buyer_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.imges)
        } else {
            Glide.with(context).load(list.buyer_image)
                .into(holder.itemView.imges)
        }
        //set the click on the adapter
        holder.itemView.setOnClickListener {
            click.setClick(mData[position])

        }
        //set the clicck on the image
        holder.itemView.relativeImage.setOnClickListener {

//            if (list.list_type=="1"){
//                val localModel = FinalizeModel()
//                localModel.seller_id=list.buyer_id
//                localModel.cat_id=list.cat_id
//                localModel.seller_name=list.buyer_name
//                val intent = Intent(context, SellerStoreActivity::class.java)
//                intent.putExtra("keyOpenSellerStore",localModel)
//                context.startActivity(intent)
//            }else{
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keydata", mData[position])
                context.startActivity(intent)
          //  }
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

    interface setAdapteClick {
        fun setClick(list: SellerApprovalModel)
    }
}
