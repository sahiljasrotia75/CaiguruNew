package com.example.caiguru.seller.sellerChooseCategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.registor_category_adapter.view.*
import kotlin.collections.ArrayList

class SellerCategoryAdapter(
    var context: Context,
    var catArray: ArrayList<SellerCategoryModel>

) :
    RecyclerView.Adapter<SellerCategoryAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.seller_choose_category_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return catArray.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {

        val model = catArray[position]
        holder.itemView.nametext.text = model.name
        holder.itemView.selectedImage.setImageResource(model.imagewhite)
        holder.itemView.tickImage.visibility = View.GONE

        if (model.hasselected) {
            holder.itemView.layoutClick.setBackgroundResource(model.purplebackground)
            holder.itemView.selectedImage.setImageResource(model.imageyellow)
            holder.itemView.nametext.setTextColor(context.resources.getColor(R.color.purple))//set text color
            holder.itemView.tickImage.visibility = View.VISIBLE
        } else {
            holder.itemView.layoutClick.setBackgroundResource(model.greybackground)
            holder.itemView.nametext.setTextColor(context.resources.getColor(R.color.hard_grey))
            holder.itemView.selectedImage.setImageResource(model.imagewhite)
            holder.itemView.tickImage.visibility = View.GONE

        }
        //set click
        holder.itemView.setOnClickListener {

            if (model.hasselected) {
                catArray[position].hasselected = false
            } else {
                for (item in catArray) {
                    item.hasselected = false
                }
                catArray[position].hasselected = true
            }
            notifyDataSetChanged()
        }

    }

    class Viewholder(mbinding: View) : RecyclerView.ViewHolder(mbinding)


    fun checkIfLessThanThree(): Boolean {
        var count = 0
        for (item in catArray) {
            if (item.hasselected) {
                count++
            }
        }
        return count < 1
    }

    fun getSelectedCategories(): SellerCategoryModel {
        var selectedCat: SellerCategoryModel? = null
        for (item in catArray) {
            if (item.hasselected) {
                selectedCat = item
            }
        }
        return selectedCat!!
    }
    fun prefillingData(category: String) {
        for (item in catArray) {
            if (item.name == category) {
                item.hasselected = true
                notifyDataSetChanged()
            }
        }

    }
}


