package com.example.caiguru.seller.shoppingListSellers.loadmorecloseList

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.SellerClosedListActivity
import kotlinx.android.synthetic.main.load_more_closelist_adapter.view.*

class LoadmoreCloseListAdapter(var context: Context):RecyclerView.Adapter<LoadmoreCloseListAdapter.ViewHolder>() {

    var child = ArrayList<ListParentModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.load_more_closelist_adapter, parent, false)
        return ViewHolder(view)    }

    override fun getItemCount(): Int {
        return child.size    }

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

    fun update(it: java.util.ArrayList<ListParentModel>) {
        child = it
        notifyDataSetChanged()

    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

    }

}
