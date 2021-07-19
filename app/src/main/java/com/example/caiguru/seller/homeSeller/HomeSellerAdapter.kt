package com.example.caiguru.seller.homeSeller

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
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import android.text.style.StyleSpan
import android.widget.TextView
import android.text.method.LinkMovementMethod
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import androidx.fragment.app.FragmentActivity
import com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification.ResolutionActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.SellerApprovalOrderListActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList.CustomerRequestListActivity
import kotlinx.android.synthetic.main.home_seller_adapter.view.*
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.backColorLayout
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.textnames
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.timeAgo
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.userPic


class HomeSellerAdapter(var context: FragmentActivity) :
    RecyclerView.Adapter<HomeSellerAdapter.ViewHolder>() {
    private lateinit var list: HomeSellerModel
    var text2: String = ""
    var mData = ArrayList<HomeSellerModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.home_seller_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list = mData[position]
        //set the notification
        if (list.is_read == "0") {
            holder.itemView.dots.visibility = View.VISIBLE
        } else {
            holder.itemView.dots.visibility = View.GONE
        }
        //level image set
        Glide.with(context)
            .load(levelget(list.level).levelImage)
            .into(holder.itemView.imgBatchs)

        lateinit var spannable: SpannableString

        lateinit var buyerName1: SpannableString
        var buyerName: String = ""
        //set the level  image
        if (list.list_type == "1") {
            holder.itemView.backColorLayout.setBackgroundColor((context.resources.getColor(R.color.light_purple)))
        } else {
            holder.itemView.backColorLayout.setBackgroundColor((context.resources.getColor(R.color.yellow_light)))
        }
        //*************seller buyer text set spannable
        //In Response: list_type 1:pending approval seller list, 2:list uploaded by customer
//seller list dispute
        if (list.list_type == "1" && list.status == "6") {
            buyerName = list.name
            buyerName1 = SpannableString(list.name)
            text2 =
                "$buyerName ${context.getString(R.string.has_added_a_disputes)} \"${categoryData(
                    list.cat_id
                )}\"."
            spannable = SpannableString(text2)
        } else if (list.list_type == "1" && list.status != "6") {
            buyerName = list.name
            buyerName1 = SpannableString(list.name)
            text2 =
                "$buyerName ${context.getString(R.string.ordered_one_or_more_of_your_products)}."
            spannable = SpannableString(text2)
            //buyer list dispute
        } else if (list.list_type == "2" && list.status == "6") {
            buyerName = list.name
            buyerName1 = SpannableString(list.name)
            text2 =
                "$buyerName ${context.getString(R.string.has_added_a_disputes)} \"${categoryData(
                    list.cat_id
                )}\"."
            spannable = SpannableString(text2)

        }else  {
            buyerName = list.name
            buyerName1 = SpannableString(list.name)
//            text2 =
//                "$buyerName ${context.getString(R.string.has_uploaded_shopping_list)} \"${categoryData(
//                    list.cat_id
//                )}\"."
            text2 =
                "$buyerName ${context.getString(R.string.has_uploaded_a_shopping_list_to_be_quoted)}"
            spannable = SpannableString(text2)

        }

//        if (list.list_type == "1") {
//            buyerName = list.name
//            buyerName1 = SpannableString(list.name)
//            text2 = "$buyerName ${context.getString(R.string.ordered_your_shopping_list_in)} \"${categoryData(
//                    list.cat_id
//                ).name}\"."
//            spannable = SpannableString(text2)
//        } else {
//            buyerName = list.name
//            buyerName1 = SpannableString(list.name)
//            text2 = "$buyerName ${context.getString(R.string.has_uploaded_shopping_list)} \"${categoryData(
//                    list.cat_id
//                ).name}\"."
//            spannable = SpannableString(text2)
//
//        }
        //set the click on the spannable
        val clickableSpanforSigleText = object : ClickableSpan() {
            override fun onClick(textView: View) {

                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyHomeSellerProfile", mData[position])
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
                if (mData[position].list_type == "1" && mData[position].status != "6") {
                    //seller list pending approval
                    val intent = Intent(context, SellerApprovalOrderListActivity::class.java)
                    intent.putExtra("keyHomeFragmentData", mData[position])
                    context.startActivity(intent)
                }
                //dispute added in buyer list
                else if (mData[position].list_type == "1" && mData[position].status == "6") {
//both staisfy then click
                    val intent = Intent(context, ResolutionActivity::class.java)
                    intent.putExtra("keyHomeFragmentData", mData[position])
                    context.startActivity(intent)

                }
                //dispute added in buyer list
                else if (mData[position].list_type == "2" && mData[position].status == "6") {

                    val intent = Intent(context, ResolutionActivity::class.java)
                    intent.putExtra("keyHomeFragmentData", mData[position])
                    context.startActivity(intent)

                }
                else {
                    //list uploaded by customer
                    val intent = Intent(context, CustomerRequestListActivity::class.java)
                    intent.putExtra("keyHomeFragmentData", mData[position])
                    context.startActivity(intent)
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

        }

        val boldSpan = StyleSpan(Typeface.BOLD)
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

        if (list.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context).load(list.image).into(holder.itemView.userPic)
        }
        //set the notification
        if (list.is_read == "0") {
            holder.itemView.dots.visibility = View.VISIBLE
        } else {
            holder.itemView.dots.visibility = View.GONE
        }

        holder.itemView.timeAgo.text = Constant.timesAgoLogic(list.created_at, context)

        //set the click on the adapter
        holder.itemView.setOnClickListener {

            if (mData[position].list_type == "1" && mData[position].status != "6") {
                //seller list pending approval
                val intent = Intent(context, SellerApprovalOrderListActivity::class.java)
                intent.putExtra("keyHomeFragmentData", mData[position])
                context.startActivity(intent)
            } else if (mData[position].list_type == "1" && mData[position].status == "6") {
                val intent = Intent(context, ResolutionActivity::class.java)
                intent.putExtra("keyHomeFragmentData", mData[position])
                context.startActivity(intent)
            }
            //dispute added in buyer list
            else if (mData[position].list_type == "2" && mData[position].status == "6") {

                val intent = Intent(context, ResolutionActivity::class.java)
                intent.putExtra("keyHomeFragmentData", mData[position])
                context.startActivity(intent)

            }else {
                //list uploaded by customer
                val intent = Intent(context, CustomerRequestListActivity::class.java)
                intent.putExtra("keyHomeFragmentData", mData[position])
                context.startActivity(intent)
            }
        }
        //set the click on the image
        holder.itemView.relativeImages.setOnClickListener {

            val intent = Intent(context, OtherProfileViewActivity::class.java)
            intent.putExtra("keyHomeSellerProfile", mData[position])
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

    fun upDate(it: ArrayList<HomeSellerModel>) {
        mData = it
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
