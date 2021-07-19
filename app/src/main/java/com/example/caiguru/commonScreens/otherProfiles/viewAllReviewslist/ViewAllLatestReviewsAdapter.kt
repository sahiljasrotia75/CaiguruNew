package com.example.caiguru.commonScreens.otherProfiles.viewAllReviewslist

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.homeBuyers.tagUser.TagModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.MySpannable
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.ReviewsModel
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.customer_list_child_adapter.view.imgBatchs
import kotlinx.android.synthetic.main.latest_reviews_adapter.view.*
import kotlinx.android.synthetic.main.notification_alert_custom_dialog.view.*
import kotlinx.android.synthetic.main.notification_alert_custom_dialog.view.okBtn
import kotlinx.android.synthetic.main.other_profile_comment_view_custom_dialog.*
import kotlinx.android.synthetic.main.other_profile_comment_view_custom_dialog.view.*
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.textnames
import kotlinx.android.synthetic.main.pending_approvals_adapter.view.userPic


class ViewAllLatestReviewsAdapter(
    var context: Context

) : RecyclerView.Adapter<ViewAllLatestReviewsAdapter.ViewHolder>() {

    private lateinit var dialogs: Dialog
    var mData = ArrayList<ReviewsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.latest_reviews_adapter, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("DefaultLocale")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val list = mData[position]
        //  1:Buyer, 2:Seller
        if (list.type=="1"){
            //set the level
            if (list.level.isEmpty()) {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.user_placeholder))
                    .into(holder.itemView.imgBatchs)
            } else {
                Glide.with(context)
                    .load(buyerlevelget(list.level).levelImage)
                    .into(holder.itemView.imgBatchs)
            }
        }
        else{
            //set the level
            if (list.level.isEmpty()) {
                Glide.with(context)
                    .load(context.getDrawable(R.drawable.user_placeholder))
                    .into(holder.itemView.imgBatchs)
            } else {
                Glide.with(context)
                    .load(sellerlevelget(list.level).levelImage)
                    .into(holder.itemView.imgBatchs)
            }
        }

        holder.itemView.timeAgos.text = Constant.timesAgoLogic(list.created_at, context)
        //set profile
        if (list.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context).load(list.image).into(holder.itemView.userPic)
        }

        //set the imoji image
        if (list.rating.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.imoji)
        } else {
            Glide.with(context).load(setImoji(list.rating)).into(holder.itemView.imoji)
        }
        if (list.comment.isNotEmpty()) {
            val txtcomment = holder.itemView.textnames
            holder.itemView.textnames.setText(list.comment)
            makeTextViewResizable(list, txtcomment, 2, context.getString(R.string.ReadMore), false)
        } else {

            val userName1 =
                list.name.substring(0, 1).toUpperCase() + list.name.substring(1)
            val userName = userName1
            val buyerName1 = SpannableString(list.name)
            val rating = SetRating(list.rating)
            val providingTxt = context.getString(R.string.provide_a)
            val ratingtxt = context.getString(R.string.rating)
            val text2 =
                "$userName $providingTxt $rating $ratingtxt"
            val spannable = SpannableString(text2)
            val boldSpan = StyleSpan(Typeface.BOLD)

            spannable.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                0,
                buyerName1.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                text2.length - rating.length - ratingtxt.length - 1,
                text2.length - ratingtxt.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

            )
            spannable.setSpan(boldSpan, 0, buyerName1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.itemView.textnames.setText(spannable, TextView.BufferType.SPANNABLE)

        }

        //open the profile
        holder.itemView.userPic.setOnClickListener {
            if (list.type == "2") {
                val localModel = FinalizeModel()
                localModel.seller_id = list.user_id
                localModel.cat_id = ""
                localModel.seller_name = list.name
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore", localModel)
                context.startActivity(intent)
            } else {
                val model = ReviewsModel()
                model.name = list.name
                model.user_id = list.user_id
                model.type = list.type
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyHomeCommentProfile", model)
                context.startActivity(intent)
            }
        }

    }

    //read more click
    private fun makeTextViewResizable(
        model: ReviewsModel,
        tv: TextView,
        maxLine: Int,
        expandText: String,
        viewMore: Boolean
    ) {
        try {

            if (tv.getTag() == null) {
                tv.setTag(tv.getText())
            }
            val vto = tv.getViewTreeObserver()
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                @SuppressWarnings("deprecation")
                @Override
                override fun onGlobalLayout() {

                    var text = ""
                    var lineEndIndex = 0
                    val obs = tv.getViewTreeObserver()
                    obs.removeGlobalOnLayoutListener(this)
                    if (maxLine == 0) {
                        lineEndIndex = tv.getLayout().getLineEnd(0)
                        text = tv.getText().subSequence(
                            0,
                            lineEndIndex - expandText.length + 1
                        ).toString() + " " + expandText.trim()
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1)
                        text = tv.getText().subSequence(
                            0,
                            lineEndIndex - expandText.length
                        ).toString() + "" + expandText.trim()
                    } else {
                        lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        //text = tv.getText().subSequence(0, lineEndIndex).toString() + " " + expandText.trim();
                        text = tv.getText().subSequence(0, lineEndIndex).toString();
                    }

                    tv.setText(text)

                    tv.setMovementMethod(LinkMovementMethod.getInstance())
                    tv.setText(
                        addClickablePartTextViewResizable(
                            model,
                            tv.getText().toString(), tv, lineEndIndex, expandText.trim(),
                            viewMore
                        ), TextView.BufferType.NORMAL
                    )
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //*************Read more click
    private fun addClickablePartTextViewResizable(
        model: ReviewsModel,
        strSpanned: String, tv: TextView,
        maxLine: Int, spanableText: String, viewMore: Boolean
    ): SpannableStringBuilder? {
        val str = strSpanned.toString()
        val ssb = SpannableStringBuilder(strSpanned)
        if (str.contains(spanableText)) {
            ssb.setSpan(object : MySpannable(false) {
                override fun onClick(widget: View) {
                    customNotificationDialog(model)

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length, 0)
        }
        return ssb
    }

    private fun customNotificationDialog(list: ReviewsModel) {
        dialogs = Dialog(context)
        //  dialogs.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogs.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogs.setContentView(R.layout.other_profile_comment_view_custom_dialog)
        dialogs.show()
//        val view =
//            LayoutInflater.from(context)
//                .inflate(R.layout.other_profile_comment_view_custom_dialog, null)
//        dialogs.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val mBuilder = AlertDialog.Builder(context)
//            .setView(view)
//            .create()
        dialogs.comment.text = list.comment

        val userName1 =
            list.name.substring(0, 1).toUpperCase() + list.name.substring(1)
        val userName = userName1
        val buyerName1 = SpannableString(list.name)

        val rating = SetRating(list.rating)
        val providingTxt = context.getString(R.string.provide_a)
        val ratingtxt = context.getString(R.string.rating)
        val text2 =
            "$userName $providingTxt $rating $ratingtxt"
        val spannable = SpannableString(text2)
        val boldSpan = StyleSpan(Typeface.BOLD)

        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            0,
            buyerName1.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            text2.length - rating.length - ratingtxt.length - 1,
            text2.length - ratingtxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE

        )
        spannable.setSpan(boldSpan, 0, buyerName1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialogs.review.setText(spannable, TextView.BufferType.SPANNABLE)

        dialogs.okBtn.setOnClickListener {
            dialogs.dismiss()
        }

    }


    private fun setImoji(rating: String): Int {
        val ratingDataArray = Constant.sellerRatingData(context)
        for (item in ratingDataArray) {
            if (item.rating == rating) {
                return item.rateImageSelected
            }
        }
        return 0
    }

    private fun SetRating(rating: String): String {
        val ratingArray = Constant.sellerRatingData(context)
        for (item in ratingArray) {
            if (item.rating == rating) {
                return item.rateStatusText
            }
        }

        return ""
    }

    private fun buyerlevelget(level: String): BuyerLevelModel {
        val BuyerLevel = Constant.BuyerLevel(context)
        for (category in BuyerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }
    //seller level
    private fun sellerlevelget(level: String): SellerLevelModel {
        val BuyerLevel = Constant.SellerLevel(context)
        for (category in BuyerLevel) {

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
        return  context.getString(R.string.mix_category_product)
    }

    fun upDate(it: ArrayList<ReviewsModel>) {
        mData = it
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}
