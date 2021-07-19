package com.example.caiguru.buyer.buyerProfile.buyerCreditEarned

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.sellerSetting.sellerCreditEarned.EarnedReferedFriendModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.top_reffered_user_adapter.view.*

class TopRefferedUserAdapter(var context: Context) :
    RecyclerView.Adapter<TopRefferedUserAdapter.ViewHolder>() {
    private var mData = ArrayList<EarnedReferedFriendModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.top_reffered_user_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtUserName.text = data.buyer_name
        holder.itemView.joindate.text =
            context.getString(R.string.Joined_on) + " " + Constant.ATconvert(context,Constant.ConvertAmPmFormat(context,Constant.orderdateTimeFormat(data.joined_on)))
        //  holder.itemView.userPic = data.full_address
        //holder.itemView.txtFullTotal.text = data.totalOrder
        //set the level  image
//        Glide.with(context)
//            .load(levelget(data.buyer_level).levelImage)
//            .into(holder.itemView.imgBatchs1)
        holder.itemView.imgBatchs1.visibility=View.INVISIBLE
        if (data.buyer_image.isEmpty()) {

            Glide.with(context).load(R.drawable.user_placeholder)
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context).load(data.buyer_image) .placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)
        }

        try {
            holder.itemView.credits.text =
                (Constant.roundValue(data.credits.toDouble())) + context.getString(R.string.credits)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }



    fun update(it: java.util.ArrayList<EarnedReferedFriendModel>) {
        if (it != null) {
            mData = it
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
