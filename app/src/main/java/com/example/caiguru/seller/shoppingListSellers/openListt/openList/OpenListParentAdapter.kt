package com.example.caiguru.seller.shoppingListSellers.openListt.openList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.openlist_parentadapter.view.*
import org.json.JSONArray
import kotlin.collections.ArrayList

class OpenListParentAdapter(var context: Context) :
    RecyclerView.Adapter<OpenListParentAdapter.ViewHolder>() {

    private var mData = ArrayList<ListParentModel>()
    lateinit var recyclerAdapter: OpenListChildAdapter
    var click = context as closeListInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.openlist_parentadapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]

        holder.itemView.txtList.text = categoryData(data.cat_id)
        holder.itemView.commission.text =
            " ${context.getString(R.string.Comission)}${"("}${data.comission_per}${"%"}${")"}"
        holder.itemView.shoppingList.text = data.listingname


        if (data.isExpanded) {
            holder.itemView.relative.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)
        } else {
            holder.itemView.relative.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }

        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }

        if (data.allow_modify == "1") {
            holder.itemView.btnClose.visibility = View.VISIBLE
            holder.itemView.btnModify.visibility = View.VISIBLE
            holder.itemView.btnCopy.visibility = View.VISIBLE
        } else {
            holder.itemView.btnModify.visibility = View.GONE
            holder.itemView.btnCopy.visibility = View.VISIBLE
            holder.itemView.btnClose.visibility = View.VISIBLE
        }

        //click on modify
        holder.itemView.btnModify.setOnClickListener {
           // data.product_details
            click.modifyList(data, recyclerAdapter.getAllData())
        }
        //click on close
        holder.itemView.btnClose.setOnClickListener {
            click.closeList(mData, position)
        }

        //click on copy
        holder.itemView.btnCopy.setOnClickListener {
            click.copyList(data,recyclerAdapter.getAllData())
        }

        //*******************Child Adapter*****************//
        val productsList = getProducts(data.product_details)

        recyclerAdapter = OpenListChildAdapter(context)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.updateData(productsList)


    }

    private fun getProducts(product_details: String): ArrayList<DialogModel> {
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
            item.list_id = obj.optString("list_id")
            item.id = obj.optString("id")
            item.saved_product_id = obj.optString("saved_product_id")
            item.status = obj.optString("status")
            array.add(item)
        }
        return array
    }


    fun update(it: ArrayList<ListParentModel>) {
        it.let {
            mData = it
        }
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    //***************** Matching id's *********************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id) {

                return category.name
            }
        }
        return  context.getString(R.string.mix_category_product)
    }

    interface closeListInterface {
        fun closeList(
            mData: ArrayList<ListParentModel>,
            position: Int
        )

        fun modifyList(data: ListParentModel, allData: ArrayList<DialogModel>)
        fun copyList(data: ListParentModel, allData: ArrayList<DialogModel>)

    }

}