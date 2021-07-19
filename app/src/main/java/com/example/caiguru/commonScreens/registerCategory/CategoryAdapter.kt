




package com.example.caiguru.commonScreens.registerCategory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.registor_category_adapter.view.*
import kotlin.collections.ArrayList

class CategoryAdapter(var context: RegisterCategoryActivity, var catArray: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.Viewholder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view = LayoutInflater.from(context).inflate(R.layout.registor_category_adapter, parent, false)
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
            holder.itemView.layoutClick.setBackgroundResource(model.yellowbackground)
            holder.itemView.selectedImage.setImageResource(model.imageyellow)
            holder.itemView.nametext.setTextColor(context.resources.getColor(R.color.yellow))//set text color
            holder.itemView.tickImage.visibility = View.VISIBLE
        } else {
            holder.itemView.layoutClick.setBackgroundResource(model.whitebackground)
            holder.itemView.nametext.setTextColor(Color.WHITE)
            holder.itemView.selectedImage.setImageResource(model.imagewhite)
            holder.itemView.tickImage.visibility = View.GONE

        }
        //set click
        holder.itemView.setOnClickListener {
            catArray[position].hasselected = !catArray[position].hasselected
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

        return count < 3
    }

    fun getSelectedCategories(): String {
        var selectedCat = ""
        for (item in catArray) {
            if (item.hasselected) {
                selectedCat = if (selectedCat.isEmpty()){
                    item.category_id
                }else{
                    "$selectedCat,${item.category_id}"
                }
            }
        }

        return selectedCat
    }

    fun update_categories(): String {
        return getSelectedCategories()
    }
}


