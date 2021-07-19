package com.example.caiguru.commonScreens.dashBoardParentActivity.dashboardStartingNotification.sellerUnSeenNotification


import android.content.Context
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import constant_Webservices.Constant
import java.util.*
import kotlin.collections.ArrayList

class CustomPagerAdapter(var context: Context, var mData: ArrayList<NotificationModel>) :
    PagerAdapter() {

    var click = context as setBuyerInterface
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`

    }

    override fun getCount(): Int {
        return mData.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context)
            .inflate(R.layout.viewpager_adapter_layout, container, false)
        val statusText = itemView.findViewById(R.id.statusText) as TextView
        val title = itemView.findViewById(R.id.productheading) as TextView
        val btnProceed = itemView.findViewById(R.id.btnProceed) as Button
        val currentLanguage = Locale.getDefault().language
        if (currentLanguage == "es") {
            statusText.setText(mData[position].msg_es)
        } else {
            statusText.setText(mData[position].msg_en)
        }


        val type = Constant.getPrefs(context).getString(Constant.type, "")
        if (type == "1") {
            title.text = context.getString(R.string.Order_Complete_Request)
        } else {
            title.text = context.getString(R.string.Order_Confirmed_Request)
        }

//        if (mData[position].action_taken == "1") {
//            btnProceed.visibility=View.VISIBLE
//        } else {
//            btnProceed.visibility=View.GONE
//        }
        btnProceed.setOnClickListener {
            click.buyerNotificationClick(mData[position])
        }

        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    interface setBuyerInterface {

        fun buyerNotificationClick(notificationModel: NotificationModel)
    }
}