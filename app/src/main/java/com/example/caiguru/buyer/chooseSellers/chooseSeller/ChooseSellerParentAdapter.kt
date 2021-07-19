package com.example.caiguru.buyer.chooseSellers.chooseSeller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.choose_seller_parent_adapter.view.*
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.cardView
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.recyclerChild
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.txtList
import kotlinx.android.synthetic.main.customer_list_parent_adapter.view.updown

class ChooseSellerParentAdapter(
    var context: FragmentActivity,
    listen: loadmore
) :
    RecyclerView.Adapter<ChooseSellerParentAdapter.ViewHolder>() {
    var homeDeliveryaddres = DeliveryZoneModel()
    private lateinit var search: String
    private var mData = ArrayList<CustomerParentModel>()
    var click = listen as loadmore//click
    lateinit var recyclerAdapter: ChooseSellerChildAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.choose_seller_parent_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.txtList.text = categoryData(data.cat_id)
        //send the id of every holder click

        //************************** load more work ********************************//
//        holder.itemView.loadmores.visibility = View.VISIBLE
//        holder.itemView.loadmores.setOnClickListener {
//            click.loadmoreClick(data, position)
//        }
        if (data.more !=0) {

            holder.itemView.loadmores.visibility = View.VISIBLE
            holder.itemView.loadmores.setOnClickListener {
                click.loadmoreClick(data, position)
            }

        } else {

            holder.itemView.loadmores.visibility = View.GONE
        }


        if (data.isExpanded) {
            holder.itemView.innerLayoutChilds.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)

        } else {
            holder.itemView.innerLayoutChilds.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }

        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            mData[position].isExpanded = !data.isExpanded
            notifyDataSetChanged()
        }

        //************************CustomerListChildAdapter****************************//

        recyclerAdapter =
            ChooseSellerChildAdapter(context, data)
        val manager = LinearLayoutManager(context)
        holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
        holder.itemView.recyclerChild.adapter = recyclerAdapter
        recyclerAdapter.updateData(data, search, homeDeliveryaddres)
    }


    //***************** Matching id's *********************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return context.getString(R.string.mix_category_product)
    }


    fun update(
        it: ArrayList<CustomerParentModel>,
        searchText: String,
        lat: String,
        long: String,
        addressData: String
    ) {
        homeDeliveryaddres.lat = lat
        homeDeliveryaddres.long = long
        homeDeliveryaddres.address = addressData
        search = searchText
        if (it != null) {
            mData = it
        }
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface loadmore {
        fun loadmoreClick(data: CustomerParentModel, position: Int)
    }

}
