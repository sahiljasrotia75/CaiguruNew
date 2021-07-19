package com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.recycler_buyer_shop_list.view.*

class BuyerShopAdapter(
    var context: Context,
    buyerShoppingFragment: BuyerShoppingFragment
) :
    RecyclerView.Adapter<BuyerShopAdapter.ViewHolder>() {
    var list: ArrayList<BuyerShopModel> = ArrayList()

    var click = buyerShoppingFragment as modifyClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_buyer_shop_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        if (model.modify == "1") {
            holder.itemView.bestquotetext.text=context.getString(R.string.QUOTE_PENDING)
            holder.itemView.bestquote.text = " "
        } else {
            holder.itemView.bestquote.text = "$" + Constant.roundValue(model.best_quote.toDouble())
            holder.itemView.bestquotetext.text=context.getString(R.string.Best_Quote)
        }
        holder.itemView.shopListname.text = model.listingname
        holder.itemView.setOnClickListener {
            click.modify(model)
        }
    }


    fun update(it: java.util.ArrayList<BuyerShopModel>) {
        list = it
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}

interface modifyClick {
    fun modify(model: BuyerShopModel)
}