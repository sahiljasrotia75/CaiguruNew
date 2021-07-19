package com.example.caiguru.commonScreens.selectCities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.select_cities_adapter.view.*

class SelectCitiesAdapter(var context: SelectCitiesActivity) :
    RecyclerView.Adapter<SelectCitiesAdapter.Viewholder>() {
    var arrayCitiesModel = ArrayList<CitiesModel>()
    var click = context as selectedCityInteface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.select_cities_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return arrayCitiesModel.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val mData = arrayCitiesModel[position]
        holder.itemView.txtcities.text = mData.name
        holder.itemView.txtcities.setTextColor(context.resources.getColor(R.color.hard_grey))//set text color

        if (mData.hasselected) {
            holder.itemView.txtcities.text = mData.name
            holder.itemView.txtcities.setTextColor(context.resources.getColor(R.color.purple))//set text color
           // click.selectedCities(mSucessfullData)
        } else {

            holder.itemView.txtcities.text = mData.name
            holder.itemView.txtcities.setTextColor(context.resources.getColor(R.color.hard_grey))//set text color
           //click.removeCities(mSucessfullData)
        }

        //set click
        holder.itemView.setOnClickListener {
            if (mData.hasselected) {
                click.removeCities(mData)
            }else{
                click.selectedCities(mData)
            }
            //arrayCitiesModel[position].hasselected = !arrayCitiesModel[position].hasselected
           // notifyDataSetChanged()
        }
    }

    fun Update(it: ArrayList<CitiesModel>) {

        arrayCitiesModel = it
        notifyDataSetChanged()

    }

    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {

    }

    interface selectedCityInteface {

        fun selectedCities(mData: CitiesModel)
        fun removeCities(mData: CitiesModel)
    }

}
