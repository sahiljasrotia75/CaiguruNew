package com.example.caiguru.buyer.buyerProfile.buyerSettingFragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerEditProfile.BuyerEditProfileActivity
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.BuyerMyOrderActivity
import com.example.caiguru.commonScreens.blockedUser.BlockedUserActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.CommonNotificationActivity
import com.example.caiguru.commonScreens.help.HelpActivity
import com.example.caiguru.commonScreens.selectCities.SelectCitiesActivity
import com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment.SettingModel
import kotlinx.android.synthetic.main.layout_buyer_setting_adapter.view.*
import java.util.ArrayList

class BuyerSettingAdapter(
    var context: Context, val list: ArrayList<SettingModel>, context2: clickLogout
) :
    RecyclerView.Adapter<BuyerSettingAdapter.ViewHolder>() {

    var click = context2 as clickLogout

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.layout_buyer_setting_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelShopList = list[position]

        holder.itemView.ivNext.setImageResource(modelShopList.image)
        holder.itemView.profileListname.text = modelShopList.name

        holder.itemView.setOnClickListener {
           // var intent: Intent = Intent()
            if (position == 0) {
                val intent = Intent(context, BuyerEditProfileActivity::class.java)
                context.startActivity(intent)
            } else if (position == 1) {
//                val intent = Intent(context, SellerBuyCreditsActivity::class.java)
//                intent.putExtra("profileKey",profileDataModel)
//                context.startActivity(intent)
                click.openCreditsActivity()
            }
            else if (position == 2) {
                val intent = Intent(context, BlockedUserActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 3) {
                val intent = Intent(context, SelectCitiesActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 4) {
                val intent = Intent(context, CommonNotificationActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 5) {
                val intent = Intent(context, BuyerMyOrderActivity::class.java)
                context.startActivity(intent)

            }  else if (position == 6) {
                val intent = Intent(context, HelpActivity::class.java)
                context.startActivity(intent)
            }
            else if (position == 7) {
                click.logout()
            }

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface clickLogout {
        fun logout()
        fun openCreditsActivity()
    }
}
