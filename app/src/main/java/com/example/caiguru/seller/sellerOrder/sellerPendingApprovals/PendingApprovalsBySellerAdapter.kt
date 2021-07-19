package com.example.caiguru.seller.sellerOrder.sellerPendingApprovals

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.*
import android.text.style.StyleSpan
import android.widget.TextView
import android.text.method.LinkMovementMethod
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity


class PendingApprovalsBySellerAdapter(
    var context: Context

) : RecyclerView.Adapter<PendingApprovalsBySellerAdapter.ViewHolder>() {
    //private lateinit var list: SellerApprovalModel
    var text2: String = ""
    var mData = ArrayList<SellerApprovalModel>()
    var click = context as setAdapteClick
    lateinit var spannable: SpannableString
    lateinit var buyerName1: SpannableString
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.pending_approvals_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val list = mData[position]
        //level image set
        Glide.with(context)
            .load(levelget(list.buyer_level).levelImage)
            .into(holder.itemView.imgBatchs)


        //set the level  image
        if (list.delivery_type == "2") {
            holder.itemView.backColorLayout.setBackgroundColor((context.resources.getColor(R.color.light_purple)))
        }
        else {
            holder.itemView.backColorLayout.setBackgroundColor((context.resources.getColor(R.color.yellow_light)))
        }
        //*************seller buyer text set spannable
        //In Response: list_type 1:Seller, 2:Buyer
        if (list.list_type == "1") {
            buyerName1 = SpannableString(list.buyer_name)
//            text2 =
//                "${mData[position].buyer_name} ${context.getString(R.string.ordered_your_shopping_list_in)} \"${categoryData(
//                    mData[position].cat_id
//                )}\"."
            text2 =
                "${mData[position].buyer_name} ${context.getString(R.string.ordered_one_or_more_of_your_products)}."
            spannable = SpannableString(text2)
        } else {
            buyerName1 = SpannableString(list.buyer_name)
            text2 =
                "${mData[position].buyer_name} ${context.getString(R.string.action_pending_for)} \"${categoryData(
                    mData[position].cat_id
                )}\""
            spannable = SpannableString(text2)

        }
        val boldSpan = StyleSpan(Typeface.BOLD)
        //set the click on the spannable
        val clickableSpanforSigleText = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                mData[position].list_type = "2"
                intent.putExtra("keydata", mData[position])
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }

        //set the click on the spannable for hole text
        val clickableSpanWholeText = object : ClickableSpan() {
            override fun onClick(textView: View) {
                try {
                    click.setClick(mData[position])
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannable.setSpan(
            clickableSpanforSigleText, 0,
            buyerName1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //for all text
        spannable.setSpan(
            clickableSpanWholeText, buyerName1.length,
            text2.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        holder.itemView.textnames.movementMethod = LinkMovementMethod.getInstance()
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            0,
            buyerName1.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            buyerName1.length,
            text2.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(boldSpan, 0, buyerName1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        holder.itemView.textnames.setText(spannable, TextView.BufferType.SPANNABLE)

        if (list.buyer_image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context).load(list.buyer_image).into(holder.itemView.userPic)
        }
        //set the notification
        if (list.is_read == "0") {
            holder.itemView.dot.visibility = View.VISIBLE
        }
        else {
            holder.itemView.dot.visibility = View.GONE
        }

        holder.itemView.timeAgo.text = Constant.timesAgoLogic(list.created_at, context)

        //set the click on the adapter
        holder.itemView.setOnClickListener {
            click.setClick(mData[position])

        }
        //set the clicck on the image
        holder.itemView.relativeImage.setOnClickListener {

            val intent = Intent(context, OtherProfileViewActivity::class.java)
            mData[position].list_type = "2"
            intent.putExtra("keydata", mData[position])
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


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface setAdapteClick {
        fun setClick(list: SellerApprovalModel)

    }

}
