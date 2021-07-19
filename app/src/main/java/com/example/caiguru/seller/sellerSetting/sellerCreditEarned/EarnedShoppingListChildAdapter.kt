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
import kotlinx.android.synthetic.main.earned_shopping_list_child_adapter.view.*

class EarnedShoppingListChildAdapter(var context: Context) :
    RecyclerView.Adapter<EarnedShoppingListChildAdapter.ViewHolder>() {
    var child = ArrayList<EarnedReferedFriendModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.earned_shopping_list_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.imgBatchs.visibility = View.INVISIBLE

//        Glide.with(context)
//            .load(levelget(list.buyer_level).levelImage)
//            .into(holder.itemView.imgBatchs)


        if (list.buyer_image.isEmpty()) {

            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.userPic)
        } else {
            Glide.with(context).load(list.buyer_image).placeholder(R.drawable.user_placeholder).into(holder.itemView.userPic)
        }

        holder.itemView.txtUserName.text = list.buyer_name
       // holder.itemView.textAddress.text = list.full_address
        holder.itemView.textAddress.visibility = View.INVISIBLE
        holder.itemView.txtFullDate.text = Constant.ATconvert(
            context,
            Constant.ConvertAmPmFormat(context, Constant.orderdateTimeFormat(list.delivery_date))
        )
        holder.itemView.txtCredids.text =
            Constant.roundValue(list.credits.toDouble()) + context.getString(R.string.cr)
    }



    fun update(data: java.util.ArrayList<EarnedReferedFriendModel>) {
        child = data
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
