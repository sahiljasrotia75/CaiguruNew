package com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import kotlinx.android.synthetic.main.open_child_adapter.view.*

class OpenChildAdapter(var context: Context) :
    RecyclerView.Adapter<OpenChildAdapter.ViewHolder>() {
    var child = ArrayList<ListParentModel>()
    val click = context as childClickInterface


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.open_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.textlistname.text = list.listingname

        holder.itemView.setOnClickListener {

            click.childClick(list, position)
//            val intent = Intent(context, OpenListActivity::class.java)
//            intent.putExtra("keymodel", list)
//            context.startActivity(intent)

        }
    }

    fun update(data: ArrayList<ListParentModel>) {

        child = data
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface childClickInterface{
        fun childClick(
            mData: ListParentModel,
            position: Int
        )
    }
}
