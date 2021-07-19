package com.example.caiguru.buyer.homeBuyers.homeBuyer

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.AddAddressModel
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews.BuyerHomeViewActivity
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerViewDetails.BuyerDeliveryDetailsActivity
import com.example.caiguru.buyer.homeBuyers.homeBuyer.viewTagMoreUser.HomeBuyerViewMorePeopleActivity
import com.example.caiguru.buyer.homeBuyers.tagUser.TagBuyerHomeActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.dcreiption_alert_dialog.btnUnderStood
import kotlinx.android.synthetic.main.dcreiption_alert_dialog.imgSelector
import kotlinx.android.synthetic.main.dcreiption_alert_dialog2.*
import kotlinx.android.synthetic.main.layout_home_adapter.view.*


class HomeAdapter(var context: Context, var listner: setLikeInterface) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private var adressModel = AddAddressModel()
    private var getProfileModel = GetProfileModel()
    private var hasLiked: Boolean = false
    private lateinit var adapter: ShoppingChildAdapter
    var mData: ArrayList<HomeModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_home_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mData[position]
        getProfileModel = Constant.getProfileData(context)
        val allComission =
            (model.comission_per.toDouble() + getProfileModel.plateform_commission.toDouble()).toString()
        holder.itemView.txtLike.text = model.likes
        holder.itemView.txtComment.text = model.comments
        holder.itemView.ComissionList.text =
            "${context.getString(R.string.Comission)}: ${model.comission_per}%"
        //****************payment methods logo
//        val paymentMethods = model.payment_methods.split(",")
//        if (paymentMethods != null) {
//            holder.itemView.creditDebit.visibility = View.INVISIBLE
//            holder.itemView.cashlogo.visibility = View.INVISIBLE
//                for (item in paymentMethods) {
//                    if (item == "1") {
//                        holder.itemView.cashlogo.visibility = View.VISIBLE
//                    } else  {
//                        holder.itemView.creditDebit.visibility = View.VISIBLE
//                    }
//                }
//        }

        if (model.is_shared == "1") {
            holder.itemView.ShoppingListDate.text =
                " ${Constant.timesAgoLogic(model.created_at, context)}"
        } else {
            holder.itemView.ShoppingListDate.text =
                " ${Constant.timesAgoLogic(model.shared_at, context)}"
            holder.itemView.ShoppingListDate1.text =
                " ${Constant.timesAgoLogic(model.created_at, context)}"
        }
        //set purchase button visibility
        if (model.is_buy_btn_show == "1") {
            holder.itemView.purchase.visibility = View.VISIBLE
            // set processing button visibility
            if (model.is_processing == "1") {
                holder.itemView.purchase.text = context.getString(R.string.Processing)
                holder.itemView.purchase.setBackgroundDrawable(context.getDrawable(R.drawable.rectangle_curve_loginbuttonprocessing))
                holder.itemView.purchase.setTextColor(context.resources.getColor(R.color.white))
                holder.itemView.purchase.isClickable = true
                holder.itemView.purchase.isEnabled = true
            } else {
                holder.itemView.purchase.text = context.getString(R.string.Purchase)
                holder.itemView.purchase.setBackgroundDrawable(context.getDrawable(R.drawable.rectangle_curve_loginbutton))
                holder.itemView.purchase.setTextColor(context.resources.getColor(R.color.purple))
                holder.itemView.purchase.isClickable = true
                holder.itemView.purchase.isEnabled = true

            }
        } else {
            holder.itemView.purchase.visibility = View.INVISIBLE

        }


        if (model.seller_image.isEmpty()) {
            Glide.with(context)
                .load(context.getDrawable(R.drawable.user_placeholder))
                .into(holder.itemView.img)
        } else {
            Glide.with(context)
                .load(model.seller_image)
                .placeholder(R.drawable.user_placeholder)
                .into(holder.itemView.img)
        }



        holder.itemView.shopListName.text = model.listingname


        // showing the image in the feed
//        if (model.delivery_type == "1") {
//            holder.itemView.selfPick.setImageDrawable(context.getDrawable(R.drawable.ic_pick_up))
//        } else {
//            holder.itemView.selfPick.setImageDrawable(context.getDrawable(R.drawable.ic_delivery_truck))
//        }

        if (model.is_shared == "1") {
            holder.itemView.imgLayout1.visibility = View.GONE
            //set image
            if (model.seller_image.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.user_placeholder)
                    .into(holder.itemView.img)
            } else {
                Glide.with(context)
                    .load(model.seller_image)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.img)
            }

            //set batch
            if (model.seller_level.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchedd)
            } else {

                Glide.with(context)
                    .load(SellerLevelSet(model.seller_level))
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchedd)
            }

            holder.itemView.CardView.setBackgroundDrawable(context.getDrawable(R.drawable.curve_rectangle_one_light_yellow))

            val name = model.seller_name
            val sepratedText =
                "${model.seller_name} ${context.getString(R.string.has_uploaded_a_purposal)}"
            val spannable = SpannableString(sepratedText)
            //set the click on the spannable
            val clickableSpanStart = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    if (model.is_shared == "1") {
                        val isDescriptionAlert1 =
                            Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

                        val isDescriptionAlert2 =
                            Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!
                        if (isDescriptionAlert1.isEmpty()) {
                            openDescriptionAlert1(mData[position])
                        } else if (isDescriptionAlert2.isEmpty()) {
                            openDescriptionAlert2(mData[position])
                        } else {
                            val localModel = FinalizeModel()
                            localModel.seller_id = model.seller_id
                            localModel.cat_id = model.cat_id
                            localModel.seller_name = model.seller_name
                            localModel.lat = adressModel.lat
                            localModel.long = adressModel.long
                            localModel.address = adressModel.address
                            val intent = Intent(context, SellerStoreActivity::class.java)
                            intent.putExtra("keyOpenSellerStore", localModel)
                            context.startActivity(intent)
                            Constant.apiHitOrNot = 1
                        }

                    } else {
                        val intent = Intent(context, OtherProfileViewActivity::class.java)
                        intent.putExtra("keyHomeBuyerSharedList", mData[position])
                        context.startActivity(intent)
                        Constant.apiHitOrNot = 1
                    }

                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            //set the click on the spannable
            spannable.setSpan(
                clickableSpanStart, 0,
                name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.itemView.name.movementMethod = LinkMovementMethod.getInstance()

            spannable.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                0,
                name.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
                name.length,
                sepratedText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val boldSpan = StyleSpan(Typeface.BOLD)
            spannable.setSpan(boldSpan, 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            holder.itemView.name.setText(spannable, TextView.BufferType.SPANNABLE)

        } else {

            holder.itemView.imgLayout1.visibility = View.VISIBLE
            //set image
            if (model.shared_by_image.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.user_placeholder)
                    .into(holder.itemView.img)
            } else {
                Glide.with(context)
                    .load(model.shared_by_image)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.img)
            }
            //set batch
            if (model.shared_by_level.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchedd)
            } else {
                Glide.with(context)
                    .load(BuyerLevelProfile(model.shared_by_level))
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchedd)
            }

            holder.itemView.CardView.setBackgroundDrawable(context.getDrawable(R.drawable.curve_rectangle_one_purple))

            if (model.s_count == "0") {
                val name = model.shared_by_name
                val sepratedText =
                    "${model.shared_by_name} ${context.getString(R.string.has_tagged)}"
                val spannable = SpannableString(sepratedText)
                //set the click on the spannable
                val clickableSpanStart = object : ClickableSpan() {
                    override fun onClick(textView: View) {

                        if (model.is_shared == "1") {
                            val isDescriptionAlert1 =
                                Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

                            val isDescriptionAlert2 =
                                Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!
                            if (isDescriptionAlert1.isEmpty()) {
                                openDescriptionAlert1(mData[position])
                            } else if (isDescriptionAlert2.isEmpty()) {
                                openDescriptionAlert2(mData[position])
                            } else {
                                val localModel = FinalizeModel()
                                localModel.seller_id = model.seller_id
                                localModel.cat_id = model.cat_id
                                localModel.seller_name = model.seller_name
                                localModel.lat = adressModel.lat
                                localModel.long = adressModel.long
                                localModel.address = adressModel.address
                                val intent = Intent(context, SellerStoreActivity::class.java)
                                intent.putExtra("keyOpenSellerStore", localModel)
                                context.startActivity(intent)
                                Constant.apiHitOrNot = 1
                            }

                        } else {
                            val intent = Intent(context, OtherProfileViewActivity::class.java)
                            intent.putExtra("keyHomeBuyerSharedList", mData[position])
                            context.startActivity(intent)
                            Constant.apiHitOrNot = 1
                        }

                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }
                spannable.setSpan(
                    clickableSpanStart, 0,
                    name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.itemView.name.movementMethod = LinkMovementMethod.getInstance()

                spannable.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                    0,
                    name.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
                    name.length,
                    sepratedText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                val boldSpan = StyleSpan(Typeface.BOLD)
                spannable.setSpan(boldSpan, 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.itemView.name.setText(spannable, TextView.BufferType.SPANNABLE)

            } else {
                val name = model.shared_by_name
                val andText = context.getString(R.string.and)
                val taggedText = context.getString(R.string.have_tagged)
                val totalPeople = model.s_count + " " + context.getString(R.string.more_people)
                val sepratedText =
                    "$name $andText $totalPeople $taggedText"
                val spannable = SpannableString(sepratedText)
                val boldSpan = StyleSpan(Typeface.BOLD)
                val boldSpan1 = StyleSpan(Typeface.BOLD)
                //set the click on the spannable
                val clickableSpanStart = object : ClickableSpan() {
                    override fun onClick(textView: View) {

                        if (model.is_shared == "1") {
                            val isDescriptionAlert1 =
                                Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

                            val isDescriptionAlert2 =
                                Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!
                            if (isDescriptionAlert1.isEmpty()) {
                                openDescriptionAlert1(mData[position])
                            } else if (isDescriptionAlert2.isEmpty()) {
                                openDescriptionAlert2(mData[position])
                            } else {
                                val localModel = FinalizeModel()
                                localModel.seller_id = model.seller_id
                                localModel.cat_id = model.cat_id
                                localModel.seller_name = model.seller_name
                                localModel.lat = adressModel.lat
                                localModel.long = adressModel.long
                                localModel.address = adressModel.address
                                val intent = Intent(context, SellerStoreActivity::class.java)
                                intent.putExtra("keyOpenSellerStore", localModel)
                                context.startActivity(intent)
                                Constant.apiHitOrNot = 1
                            }

                        } else {
                            val intent = Intent(context, OtherProfileViewActivity::class.java)
                            intent.putExtra("keyHomeBuyerSharedList", mData[position])
                            context.startActivity(intent)
                            Constant.apiHitOrNot = 1
                        }

                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }

                //set the click on the spannable total number of user
                val clickableSpanTotalUser = object : ClickableSpan() {
                    override fun onClick(textView: View) {

                        val intent = Intent(context, HomeBuyerViewMorePeopleActivity::class.java)
                        intent.putExtra("keyHomeBuyerViewPeople", mData[position])
                        context.startActivity(intent)

                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false
                    }
                }


                spannable.setSpan(
                    clickableSpanStart, 0,
                    name.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                //***********set the click on the people text**********//
                //                val sepratedText =
//                    "name andText totalPeople taggedText"
                spannable.setSpan(
                    clickableSpanTotalUser, name.length + andText.length,
                    sepratedText.length - taggedText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                    0,
                    name.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                    name.length + andText.length,
                    sepratedText.length - taggedText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                spannable.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
                    name.length,
                    andText.length + name.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                holder.itemView.name.movementMethod = LinkMovementMethod.getInstance()
//                val sepratedText =
//                    "name andText totalPeople taggedText"

                spannable.setSpan(
                    boldSpan1, name.length + andText.length + 1,
                    sepratedText.length - taggedText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannable.setSpan(boldSpan, 0, name.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.itemView.name.setText(spannable, TextView.BufferType.SPANNABLE)
            }

//**************************************************tag list inner view  text **************//
            if (model.seller_image.isEmpty()) {

                Glide.with(context)
                    .load(R.drawable.user_placeholder)
                    .into(holder.itemView.img1)
            } else {

                Glide.with(context)
                    .load(model.seller_image)
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.img1)
            }
            //set batch
            if (model.seller_level.isEmpty()) {
                Glide.with(context)
                    .load(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchedd1)
            } else {
                Glide.with(context)
                    .load(SellerLevelSet(model.seller_level))
                    .placeholder(R.drawable.user_placeholder)
                    .into(holder.itemView.imgBatchedd1)
            }
            val name1 = model.seller_name
            val sepratedText1 =
                "${model.seller_name} ${context.getString(R.string.has_uploaded)}"
            val spannable1 = SpannableString(sepratedText1)
            //set the click on the spannable
            val clickableSpanStart11 = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    if (model.is_shared == "1") {
                        val isDescriptionAlert1 =
                            Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

                        val isDescriptionAlert2 =
                            Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!

                        if (isDescriptionAlert1.isEmpty()) {
                            openDescriptionAlert1(mData[position])
                        } else if (isDescriptionAlert2.isEmpty()) {
                            openDescriptionAlert2(mData[position])
                        } else {
                            val localModel = FinalizeModel()
                            localModel.seller_id = model.seller_id
                            localModel.cat_id = model.cat_id
                            localModel.seller_name = model.seller_name
                            localModel.lat = adressModel.lat
                            localModel.long = adressModel.long
                            localModel.address = adressModel.address
                            val intent = Intent(context, SellerStoreActivity::class.java)
                            intent.putExtra("keyOpenSellerStore", localModel)
                            context.startActivity(intent)
                            Constant.apiHitOrNot = 1
                        }
                    } else {
                        val intent = Intent(context, OtherProfileViewActivity::class.java)
                        intent.putExtra("keyHomeBuyerPurposalList", mData[position])
                        context.startActivity(intent)
                        Constant.apiHitOrNot = 1
                    }


                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }


            //set the click on the spannable
            spannable1.setSpan(
                clickableSpanStart11, 0,
                name1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            holder.itemView.nameShare.movementMethod = LinkMovementMethod.getInstance()

            spannable1.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.purple)),
                0,
                name1.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable1.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
                name1.length,
                sepratedText1.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val boldSpan1 = StyleSpan(Typeface.BOLD)
            spannable1.setSpan(boldSpan1, 0, name1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            holder.itemView.nameShare.setText(spannable1, TextView.BufferType.SPANNABLE)

        }
        //***********Set the click on the tag
        holder.itemView.viewTag.setOnClickListener {
            val intent = Intent(context, TagBuyerHomeActivity::class.java)
            intent.putExtra("keyTag", model)
            intent.putExtra("keyTagCommission", allComission)
            context.startActivity(intent)

        }
        //************set the click on the comment
//        holder.itemView.viewMessages.setOnClickListener {
//            val intent = Intent(context, CommentBuyerHomeActivity::class.java)
//            intent.putExtra("keyComment", mData[position])
//            intent.putExtra("keyCommentCommission", allComission)
//            context.startActivity(intent)
//        }
        //************set the click on the shared list
        holder.itemView.viewShare.setOnClickListener {
            listner.shareShoppingList(mData[position], allComission)
        }

        //************set the click on the view list seen or unseen
//        holder.itemView.viewSeenLayout.setOnClickListener {
//            val intent = Intent(context, BuyerHomeViewActivity::class.java)
//            intent.putExtra("keyViewDetail", mData[position])
//            intent.putExtra("keyViewDetailAddress", adressModel)
//            context.startActivity(intent)
//            Constant.apiHitOrNot = 1
//        }

        //purchase new click
        holder.itemView.purchase.setOnClickListener {
            val isDescriptionAlert1 =
                Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

            val isDescriptionAlert2 =
                Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!

            if (isDescriptionAlert1.isEmpty()) {
                openDescriptionAlert1(mData[position])
            } else if (isDescriptionAlert2.isEmpty()) {
                openDescriptionAlert2(mData[position])
            } else {
                val localModel = FinalizeModel()
                localModel.seller_id = model.seller_id
                localModel.cat_id = model.cat_id
                localModel.seller_name = model.seller_name
                localModel.lat = adressModel.lat
                localModel.long = adressModel.long
                localModel.address = adressModel.address
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore", localModel)
                context.startActivity(intent)
                Constant.apiHitOrNot = 1
            }


        }


// commeting the code for store click in future may be use
//        holder.itemView.purchase.setOnClickListener {
//            val isDescriptionAlert1 =
//                Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!
//
//            val isDescriptionAlert2 =
//                Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!
//
//            if (isDescriptionAlert1.isEmpty()) {
//                openDescriptionAlert1(mData[position])
//            } else if (isDescriptionAlert2.isEmpty()) {
//                openDescriptionAlert2(mData[position])
//            } else {
//                val intent = Intent(context, BuyerHomeViewActivity::class.java)
//                intent.putExtra("keyViewDetail", mData[position])
//                intent.putExtra("keyViewDetailAddress", adressModel)
//                context.startActivity(intent)
//                Constant.apiHitOrNot = 1
//            }
//
//
//        }

        holder.itemView.relativeImaged.setOnClickListener {
            if (model.is_shared == "1") {
                val isDescriptionAlert1 =
                    Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

                val isDescriptionAlert2 =
                    Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!
                if (isDescriptionAlert1.isEmpty()) {
                    openDescriptionAlert1(mData[position])
                } else if (isDescriptionAlert2.isEmpty()) {
                    openDescriptionAlert2(mData[position])
                } else {
                    val localModel = FinalizeModel()
                    localModel.seller_id = model.seller_id
                    localModel.cat_id = model.cat_id
                    localModel.seller_name = model.seller_name
                    localModel.lat = adressModel.lat
                    localModel.long = adressModel.long
                    localModel.address = adressModel.address
                    val intent = Intent(context, SellerStoreActivity::class.java)
                    intent.putExtra("keyOpenSellerStore", localModel)
                    context.startActivity(intent)
                    Constant.apiHitOrNot = 1
                }
            } else {
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyHomeBuyerSharedList", mData[position])
                context.startActivity(intent)
                Constant.apiHitOrNot = 1
            }
        }
        //***************open the profile in share
        holder.itemView.relativeImage1.setOnClickListener {
            if (model.is_shared == "1") {
                val isDescriptionAlert1 = Constant.getPrefs(context).getString(Constant.isDescriptionAlert1, "")!!

                val isDescriptionAlert2 = Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!

                if (isDescriptionAlert1.isEmpty()) {
                    openDescriptionAlert1(mData[position])
                } else if (isDescriptionAlert2.isEmpty()) {
                    openDescriptionAlert2(mData[position])
                } else {
                    val localModel = FinalizeModel()
                    localModel.seller_id = model.seller_id
                    localModel.cat_id = model.cat_id
                    localModel.seller_name = model.seller_name
                    localModel.lat = adressModel.lat
                    localModel.long = adressModel.long
                    localModel.address = adressModel.address
                    val intent = Intent(context, SellerStoreActivity::class.java)
                    intent.putExtra("keyOpenSellerStore", localModel)
                    context.startActivity(intent)
                    Constant.apiHitOrNot = 1
                }
            } else {
                val intent = Intent(context, OtherProfileViewActivity::class.java)
                intent.putExtra("keyHomeBuyerPurposalList", mData[position])
                context.startActivity(intent)
                Constant.apiHitOrNot = 1
            }
        }

        //************set the click on the Like
        holder.itemView.viewLike.setOnClickListener {
            if (model.is_like == "0") {
                hasLiked = true
                holder.itemView.thumb.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_selected))
                model.is_like = "1"
                mData[position].likes = (mData[position].likes.toInt() + 1).toString()
            } else {
                //unlike
                model.is_like = "0"
                hasLiked = false
                holder.itemView.thumb.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_unselected))
                if (mData[position].likes.toInt() > 0) {
                    mData[position].likes = (mData[position].likes.toInt() - 1).toString()
                }
            }
            listner.setLike(hasLiked, model)
            notifyItemChanged(position, model)
        }
        //liked 0 or unliked 1
//        if (model.is_like == "0") {
//            holder.itemView.thumb.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_unselected))
//
//        } else {
//            holder.itemView.thumb.setImageDrawable(context.getDrawable(R.drawable.ic_thumb_selected))
//        }
        // 1 shared 0.not shared
        if (model.is_tag == "0") {
            holder.itemView.tag_image.setImageDrawable(context.getDrawable(R.drawable.ic_tag_unselected))

        } else {
            holder.itemView.tag_image.setImageDrawable(context.getDrawable(R.drawable.ic_tag_selected))
        }
        // 1 view 0.not view
//        if (model.is_view == "0") {
//            holder.itemView.view.setImageDrawable(context.getDrawable(R.drawable.ic_view_unselected))
//
//        } else {
//            holder.itemView.view.setImageDrawable(context.getDrawable(R.drawable.ic_view_selected))
//        }
        //is comment 1 comment 0.not comment
//        if (model.is_comment == "0") {
//            holder.itemView.comment.setImageDrawable(context.getDrawable(R.drawable.ic_comment_unselected))
//
//        } else {
//            holder.itemView.comment.setImageDrawable(context.getDrawable(R.drawable.ic_comment_selected))
//        }
        //set the click on more info
        holder.itemView.TxtMoreInfo.setOnClickListener {
            val intent = Intent(context, BuyerDeliveryDetailsActivity::class.java)
            intent.putExtra("keyGetFeeds", mData[position])
            intent.putExtra("KeyAddress", adressModel)
            context.startActivity(intent)
            Constant.apiHitOrNot = 1
        }
        //set the click on the report

        holder.itemView.imgReportList.setOnClickListener { //creating a popup menu
            val popup = PopupMenu(context, holder.itemView.imgReportList)
            //inflating menu from xml resource
            popup.inflate(R.menu.options_menu)
            //adding click listener
            popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    return when (item.getItemId()) {
                        R.id.menu1 -> {
                            listner.reportListClick(model)
                            true
                        }//handle menu1 click

//                        R.id.menu2 ->                         //handle menu2 click
//                            true
//                        R.id.menu3 ->                         //handle menu3 click
//                            true
                        else -> false
                    }
                }
            })
            //displaying the popup
            popup.show()
        }


        //*************set the adapter*************//
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = ShoppingChildAdapter(context)
        holder.itemView.rvShoppingList.layoutManager = manager
        holder.itemView.rvShoppingList.adapter = adapter
        adapter.update(model.products, model.comission_per)
    }

    private fun BuyerLevelProfile(sellerLevel: String): Int {
        val buyer_level = Constant.BuyerLevel(context)
        for (item in buyer_level) {
            if (item.levelType == sellerLevel) {
                return item.levelImage
            }
        }
        return 0
    }

    private fun SellerLevelSet(sellerLevel: String): Int {
        val SellerLevel = Constant.SellerLevel(context)
        for (item in SellerLevel) {
            if (item.levelType == sellerLevel) {
                return item.levelImage
            }
        }
        return 0
    }

    fun update(
        it: ArrayList<HomeModel>,
        localAddress: String,
        localLatitude: String,
        localLongitude: String
    ) {
        adressModel.address = localAddress
        adressModel.lat = localLatitude
        adressModel.long = localLongitude
        mData = it
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    interface setLikeInterface {
        fun setLike(hasLiked: Boolean, model: HomeModel)
        fun shareShoppingList(
            homeModel: HomeModel,
            allComission: String
        )

        fun reportListClick(model: HomeModel)

    }

    private fun openDescriptionAlert1(model: HomeModel) {
        val dialog = Dialog(context)
        var isSelected = "0"
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dcreiption_alert_dialog)
        //   dialog.txtTitle.text = context.getString(R.string.purchase_alert1)
        dialog.show()
        val isDescriptionAlert2 =
            Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!

        dialog.btnUnderStood.setOnClickListener {
            if (isSelected == "1") {
                Constant.getPrefs(context).edit().putString(Constant.isDescriptionAlert1, "true")
                    .apply()
            }
//            val isDescriptionAlert2 =
//                Constant.getPrefs(context).getString(Constant.isDescriptionAlert2, "")!!

            if (isDescriptionAlert2.isEmpty()) {
                openDescriptionAlert2(model)
            } else {
                val localModel = FinalizeModel()
                localModel.seller_id = model.seller_id
                localModel.cat_id = model.cat_id
                localModel.seller_name = model.seller_name
                localModel.lat = adressModel.lat
                localModel.long = adressModel.long
                localModel.address = adressModel.address
                val intent = Intent(context, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore", localModel)
                context.startActivity(intent)
                Constant.apiHitOrNot = 1
            }
            dialog.dismiss()
        }
        dialog.imgSelector.setOnClickListener {
            if (isSelected == "0") {
                isSelected = "1"
                dialog.imgSelector.setImageResource(R.drawable.ic_selected_checkbox_purple)
            } else {
                dialog.imgSelector.setImageResource(R.drawable.ic_unselected_checkbox_purple)
                isSelected = "0"

            }
        }

    }

    private fun openDescriptionAlert2(model: HomeModel) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dcreiption_alert_dialog2)
        dialog.show()
        var isSelected = true
        //set the click
        dialog.btnUnderStood1.setOnClickListener {
            if (isSelected == false) {
                Constant.getPrefs(context).edit().putString(Constant.isDescriptionAlert2, "true")
                    .apply()
            }
            val localModel = FinalizeModel()
            localModel.seller_id = model.seller_id
            localModel.cat_id = model.cat_id
            localModel.seller_name = model.seller_name
            localModel.lat = adressModel.lat
            localModel.long = adressModel.long
            localModel.address = adressModel.address
            val intent = Intent(context, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore", localModel)
            context.startActivity(intent)
            Constant.apiHitOrNot = 1
            dialog.dismiss()
        }
        dialog.imgSelector1.setOnClickListener {
            if (isSelected) {
                isSelected = false
                dialog.imgSelector1.setImageResource(R.drawable.ic_selected_checkbox_purple)
            } else {
                dialog.imgSelector1.setImageResource(R.drawable.ic_unselected_checkbox_purple)
                isSelected = true

            }
        }
    }
}























































