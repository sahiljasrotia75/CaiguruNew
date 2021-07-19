package com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.SellerClosedListActivity
import kotlinx.android.synthetic.main.closed_child_adapter.view.*
import kotlin.collections.ArrayList

class ClosedChildAdapter(
    var context: Context
) : RecyclerView.Adapter<ClosedChildAdapter.ViewHolder>() {

    var child = ArrayList<ListParentModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.closed_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (child.size >= 5) {

            return 5
        }
        return child.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.textlistname.text = list.listingname

        holder.itemView.setOnClickListener {
            //click.sendData(list)
            val intent = Intent(context, SellerClosedListActivity::class.java)
            intent.putExtra("keymodel", list)
            context.startActivity(intent)

        }
    }

    fun update(data: ArrayList<ListParentModel>) {
        child = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
