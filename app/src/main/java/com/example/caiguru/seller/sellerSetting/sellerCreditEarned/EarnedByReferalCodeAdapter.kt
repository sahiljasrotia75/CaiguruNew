package com.example.caiguru.seller.sellerSetting.sellerCreditEarned

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.earned_by_referal_code_adapter.view.*
import kotlinx.android.synthetic.main.earned_by_referal_code_adapter.view.userPic
import kotlin.collections.ArrayList

class EarnedByReferalCodeAdapter(var context: Context) :
    RecyclerView.Adapter<EarnedByReferalCodeAdapter.ViewHolder>() {
    private var mData = ArrayList<EarnedReferedFriendModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.earned_by_referal_code_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtUserName.text = data.buyer_name
     //   holder.itemView.textAddress.text = data.full_address
        //  holder.itemView.userPic = data.full_address
        holder.itemView.txtFullTotal.text = data.order_count
        //set the level  image
        holder.itemView.imgBatchs1.visibility = View.INVISIBLE
       holder.itemView.textAddress.visibility = View.GONE
//        Glide.with(context)
//            .load(levelget(data.buyer_level).levelImage)
//            .into(holder.itemView.imgBatchs1)


        if (data.buyer_image.isEmpty()) {

            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context).load(data.buyer_image).placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)
        }

        holder.itemView.txtCredit.text = Constant.roundValue(data.credits.toDouble()) + context.getString(R.string.cr)
    }



    fun update(it: java.util.ArrayList<EarnedReferedFriendModel>?) {
        if (it != null) {
            mData = it
        }
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}