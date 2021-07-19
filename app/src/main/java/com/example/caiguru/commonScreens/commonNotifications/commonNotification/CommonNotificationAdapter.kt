package com.example.caiguru.commonScreens.commonNotifications.commonNotification

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.BuyerOrderDetailsActivity
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails.UnderReviewOrderDetailsActivity
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerShopListApproveRejectActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation.CommonNotificationConfirmationActivity
import com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification.ResolutionActivity
import com.example.caiguru.commonScreens.commonNotifications.rateBuyer.RateBuyerActivity
import com.example.caiguru.commonScreens.commonNotifications.rateSeller.RateSellerActivity
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardBuyerActivity
import com.example.caiguru.commonScreens.earnCreditsConvert.CreditConvertActivity
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.SellerApprovalOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList.SellerCancelledOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList.ToBeDeliveredOrderListActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.notification_adapter.view.*
import kotlinx.android.synthetic.main.notification_alert_custom_dialog.*
import java.lang.Exception
import java.util.*


class CommonNotificationAdapter(var context: Context) :
    RecyclerView.Adapter<CommonNotificationAdapter.ViewHolder>() {

    private lateinit var dialog: Dialog
    private var mData = ArrayList<NotificationModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.notification_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        if (data.is_read == "0") {
            holder.itemView.notificationView.visibility = View.VISIBLE
        } else {
            holder.itemView.notificationView.visibility = View.GONE
        }

        holder.itemView.txtOrder.text = Constant.timesAgoLogic(data.created_at, context)
        val currentLanguage = Locale.getDefault().language
        if (currentLanguage == "es") {
            holder.itemView.notificationText.text = data.msg_es
        } else {
            holder.itemView.notificationText.text = data.msg_en
        }

//        val currentLanguage = Locale.getDefault().language
//        if (currentLanguage == "es") {
//            currentString = data.msg_es
//        } else {
//            currentString = data.msg_en
//        }

//        if (currentString.contains(context.getString(R.string.Congratulations))) {
//            val separated = currentString.split(context.getString(R.string.you)).toTypedArray()
//            name = separated[0] // this will contain "name of the user"
//            val allText = context.getString(R.string.you) + separated[1]
//            sepratedText = "$name$allText"
//        } else {
//            val separated = currentString.split(context.getString(R.string.has)).toTypedArray()
//            name = separated[0] // this will contain "name of the user"
//            val allText = context.getString(R.string.has) + separated[1]
//            sepratedText = "$name$allText"
//        }

        // val separated = currentString.split(data.name)
        // name = separated[0] // this will contain "name of the user"
//        name = data.name // this will contain "name of the user"
//        val allText = " " + separated[1]
//        sepratedText = "$name$allText"


//        val spannable = SpannableString(sepratedText)
//        spannable.setSpan(
//            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
//            0,
//            name.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        spannable.setSpan(
//            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
//            name.length,
//            sepratedText.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        holder.itemView.notificationText.setText(spannable, TextView.BufferType.SPANNABLE)

        holder.itemView.setOnClickListener {
            try {


                //source 1 when the seller quote to the buyer list notification send to buyer "seller submit the quote"
                if (data.source == "1") {

                    val intent = Intent(context, BuyerShopListApproveRejectActivity::class.java)
                    intent.putExtra("keySourceSubmitQuoteBySeller1", mData[position])
                    context.startActivity(intent)
                }
                //source-2 when seller approve the buyer quote notification send to buyer "accept your order request"
                else if (data.source == "2") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")
                    if (type == "2") {
                        val alert = context.getString(R.string.alert_text)
                        customNotificationDialog(alert)
                    } else {
                        val intent = Intent(context, BuyerOrderDetailsActivity::class.java)
                        intent.putExtra("keySourceAcceptedOrder2", mData[position])
                        context.startActivity(intent)
                    }

                }
                //source 3 when the seller reject the order of buyer quote send to buyer "reject your order request"
                else if (data.source == "3") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")
                    if (type == "2") {
                        val alert = context.getString(R.string.alert_text)
                        customNotificationDialog(alert)
                    } else {
                        val intent = Intent(context, BuyerOrderDetailsActivity::class.java)
                        intent.putExtra("keySourceRejectOrder2", mData[position])
                        context.startActivity(intent)
                    }
                }

                //source 4 when the seller complete the list notification to Buyer "seller has complete your order"
                else if (data.source == "4") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")

                    if (type == "2") {
                        val alert = context.getString(R.string.alert_text)
                        customNotificationDialog(alert)
                    } else {
                        val intent =
                            Intent(context, CommonNotificationConfirmationActivity::class.java)
                        intent.putExtra("keySource4", data)
                        context.startActivity(intent)
                    }
                }

                //source 5 when the buyer accept the request of seller that are quote on buyer list notification send to seller "buyer has accepted your order"
                else if (data.source == "5") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")

                    if (type == "1") {
                        val alert = context.getString(R.string.alert_text2)
                        customNotificationDialog(alert)
                    } else {
                        val intent =
                            Intent(context, ToBeDeliveredOrderListActivity::class.java)
                        intent.putExtra("keySourceAcceptedListByBuyer5", data)
                        context.startActivity(intent)
                    }
                }

                //source 6 when the buyer reject the request of seller that are quoted on buyer list notification send to  seller "buyer Reject your order"
                else if (data.source == "6") {
                    if (mData[position].action_taken == "1") {
                        val intent = Intent(context, SellerCancelledOrderListActivity::class.java)
                        intent.putExtra("keySourceRejectedListByBuyer6", mData[position])
                        context.startActivity(intent)
                    }
                }

                //source 7 when the buyer complete the list of seller notification send to seller "buyer confirmed your list "
                else if (data.source == "7") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")
                    if (type == "1") {//buyer
                        val alert = context.getString(R.string.alert_text2)
                        customNotificationDialog(alert)
                    } else {
                        if (mData[position].action_taken == "1") {
                            val intent = Intent(context, RateBuyerActivity::class.java)
                            intent.putExtra("keySource7", mData[position])
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.You_have_already_rated_the_buyer),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                // source 8 when the buyer create the dispute  the notification send to the seller " buyer added a dispute on your list"
                else if (data.source == "8") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")

                    if (type == "1") {
                        val alert = context.getString(R.string.alert_text2)
                        customNotificationDialog(alert)
                    } else {
                        val intent = Intent(context, ResolutionActivity::class.java)
                        intent.putExtra("keySourceDispute8", mData[position])
                        context.startActivity(intent)
                    }


                }
                //source 9 when the seller accept the dispute  the notification send to the buyer "seller has accepte the dispute"
                else if (data.source == "9") {

                    val intent = Intent(context, BuyerOrderDetailsActivity::class.java)
                    intent.putExtra("keySourceAcceptDisputeBySeller9", mData[position])
                    context.startActivity(intent)

                }
                //source 10 when the seller refute to the dispute of byer  notification send to buyer"seller has refute on your dispute"
                else if (data.source == "10") {

                    val intent = Intent(context, UnderReviewOrderDetailsActivity::class.java)
                    intent.putExtra("keySourceRefuteBySeller10", mData[position])
                    context.startActivity(intent)

                }
                //source 11 when the buyer is shared the shopping to the another buyer than another buyer get the notification"Share feed "
                else if (data.source == "11") {
                    val type = Constant.getPrefs(context).getString(Constant.type, "")

                    if (type == "2") {
                        val alert = context.getString(R.string.alert_text)
                        customNotificationDialog(alert)
                    } else {
                        val intent = Intent(context, DashBoardBuyerActivity::class.java)
                        intent.putExtra("KeySourceDashboard11", mData[position])
                        context.startActivity(intent)
                    }

                }
                //source 12 when the buyer submit the quote  the seller list  notification send to the seller "buyer submit the quote"
                else if (data.source == "12") {

                    val type = Constant.getPrefs(context).getString(Constant.type, "")
                    if (type == "1") {
                        val alert = context.getString(R.string.alert_text2)
                        customNotificationDialog(alert)
                    } else {
                        val intent = Intent(context, SellerApprovalOrderListActivity::class.java)
                        intent.putExtra("KeySourceBuyerSubmitQuote12", mData[position])
                        context.startActivity(intent)
                    }
                    // source 13  is used for when the  seller or buyer are complete their 1200 creedits
                } else if (data.source == "13") {
                    if (mData[position].action_taken == "1") {
                        val intent = Intent(context, CreditConvertActivity::class.java)
                        intent.putExtra("KeySourceCreditConvert12", mData[position])
                        context.startActivity(intent)
                    }

                } else if (data.source == "15") {
//source 15  rating to seller when the andmin has complete the list

                    if (mData[position].action_taken == "1") {

                        val type = Constant.getPrefs(context).getString(Constant.type, "")
                        if (type == "2") {
                            val alert = context.getString(R.string.alert_text)
                            customNotificationDialog(alert)
                        } else {
                            val intent = Intent(context, RateSellerActivity::class.java)
                            intent.putExtra("KeySourceNOtification", mData[position])
                            context.startActivity(intent)
                        }
                    }

                } else if (data.source == "16") {
                    // source 16 when the seller canceller the buyer order notification send to buyer
                    if (mData[position].action_taken == "1") {

                        val type = Constant.getPrefs(context).getString(Constant.type, "")
                        if (type == "2") {
                            val alert = context.getString(R.string.alert_text)
                            customNotificationDialog(alert)
                        } else {

                            val intent =
                                Intent(context, CommonNotificationConfirmationActivity::class.java)
                            intent.putExtra("keySource4", data)
                            intent.putExtra("KeySourceCancellOrderConfirm", data)
                            context.startActivity(intent)
                        }
                    }

                }
                else if (data.source == "17") {
                    // source 16 when the seller canceller the buyer order notification send to buyer
                    if (mData[position].action_taken == "1") {

                        val type = Constant.getPrefs(context).getString(Constant.type, "")
                        if (type == "1") {
                            val alert = context.getString(R.string.alert_text2)
                            customNotificationDialog(alert)
                        } else {

                            val intent =
                                Intent(context, SellerCancelledOrderListActivity::class.java)
                            intent.putExtra("keySourceRejectedListByBuyer17", mData[position])
                            context.startActivity(intent)
                        }
                    }

                }else if (data.source == "18") {
                    // source 16 when the seller canceller the buyer order notification send to buyer
                    if (mData[position].action_taken == "1") {

                        val type = Constant.getPrefs(context).getString(Constant.type, "")
                        if (type == "1") {
                            val alert = context.getString(R.string.alert_text2)
                            customNotificationDialog(alert)
                        } else {

                            val intent =
                                Intent(context, SellerCancelledOrderListActivity::class.java)
                            intent.putExtra("keySourceRejectedListByBuyer18", mData[position])
                            context.startActivity(intent)
                        }
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    context.getString(R.string.network_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


    private fun customNotificationDialog(alert: String) {
        dialog = Dialog(context)
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.notification_alert_custom_dialog)
        dialog.show()
        dialog.edtPrice.text = alert
        dialog.okBtn.setOnClickListener {
            dialog.dismiss()
        }

    }


    fun update(it: ArrayList<NotificationModel>) {
        mData = it
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


}