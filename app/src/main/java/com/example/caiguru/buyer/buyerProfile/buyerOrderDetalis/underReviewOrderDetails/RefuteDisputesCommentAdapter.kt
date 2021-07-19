package com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.buyer.homeBuyers.commentBuyers.CommentModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.refute_disputes_comment_adapter.view.*

class RefuteDisputesCommentAdapter(
    var context: Context

) : RecyclerView.Adapter<RefuteDisputesCommentAdapter.ViewHolder>() {

    var child = ArrayList<CommentModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.refute_disputes_comment_adapter, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return child.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]

        if (list.type == "buyer") {
            holder.itemView.txtDisputeTitle.text = context.getString(R.string.dispute_reason)
            holder.itemView.CommentDate.text =  Constant.ATconvert(context,Constant.ConvertAmPmFormat(context,Constant.orderdateTimeFormat(list.created_at)                     )
            )
            holder.itemView.txtComment.text = list.comment
        } else if (list.type == "seller") {
            holder.itemView.txtDisputeTitle.text = context.getString(R.string.seller_resolution)
            holder.itemView.CommentDate.text =  Constant.ATconvert(context,Constant.ConvertAmPmFormat(context,Constant.orderdateTimeFormat(list.created_at)                     )
            )
            holder.itemView.txtComment.text = list.comment
        } else {
            //admin Comment
            holder.itemView.txtDisputeTitle.text = context.getString(R.string.admin_response)
            holder.itemView.CommentDate.text =  Constant.ATconvert(context,Constant.ConvertAmPmFormat(context,Constant.orderdateTimeFormat(list.created_at)                     )
            )
            holder.itemView.txtComment.text = list.comment
        }
    }

    fun update(
        it: ArrayList<CommentModel>
    ) {
        child = it
        notifyDataSetChanged()

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}