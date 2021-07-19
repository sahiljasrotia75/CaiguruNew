package com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.seller_closed_list_parent_adapter.view.*
import org.json.JSONArray
import java.util.ArrayList

class ClosedListParentAdapter(var context: SellerClosedListActivity) :
    RecyclerView.Adapter<ClosedListParentAdapter.Viewholder>() {
    private lateinit var recyclerAdapter: ClosedListChildAdapter
    var mData = ArrayList<ListParentModel>()
    val click = context as openAgainInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.seller_closed_list_parent_adapter, parent, false)
        return Viewholder(view)

    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val data = mData[position]
        holder.itemView.txtweekszone.text = CategoryMatching(data.cat_id)
      //  holder.itemView.credits.text = "${data.credits} ${context.getString(R.string.credits)}"
        holder.itemView.commission.text =
            " ${context.getString(R.string.Comission)}${"("}${data.comission_per}${"%"}${")"}"
        holder.itemView.txtshoppingList.text = data.listingname

        holder.itemView.txtamount.text = "${"$"}${Constant.roundValue(data.amount.toDouble())}"


        if (data.isExpanded) {
            holder.itemView.relativechild.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)

        } else {
            holder.itemView.relativechild.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }

        //......child adapter..........//
        recyclerAdapter = ClosedListChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager//set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        //send the data to inneradapter
        //getting the data from parent adapter and send it to the child adapter
        val childProductList = getChildProducts(data.product_details)
        recyclerAdapter.updateData(childProductList)
        holder.itemView.card3.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }
        ///set the click on whole button
        holder.itemView.btnopenagain.setOnClickListener {
            click.openAgainList(data, position)
        }
    }


    //update the data
    fun updateData(it: ArrayList<ListParentModel>) {

        it.let {
            mData = it
        }
        notifyDataSetChanged()
    }

    class Viewholder(view: View) : RecyclerView.ViewHolder(view)

    interface openAgainInterface {
        fun openAgainList(
            mData: ListParentModel,
            position: Int
        )
    }

    //***************** getting id *********************//
    fun CategoryMatching(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id) {

                return category.name
            }
        }
        return  context.getString(R.string.mix_category_product)
    }

    //****************************getting the data of the child from the parent********************//
    private fun getChildProducts(product_details: String): ArrayList<DialogModel> {
        val array = ArrayList<DialogModel>()

        val arr = JSONArray(product_details)
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val item = DialogModel()
            item.name = obj.optString("name")
            item.image = obj.optString("image")
            item.unit = obj.optString("unit")
            item.price = obj.optString("price")
            item.qty = obj.optString("qty")
            item.priceWithComission = obj.optString("priceWithComission")
            array.add(item)
        }
        return array

    }


}
