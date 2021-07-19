package com.example.caiguru.commonScreens.selectCities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.get_city_adapter.view.*

class GetttingSelectedCityAdapter(var context: SelectCitiesActivity) :
    RecyclerView.Adapter<GetttingSelectedCityAdapter.Viewholder>() {
    var arrayCitiesModel = ArrayList<CitiesModel>()
    var click = context as removeSelectedCityInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.get_city_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return arrayCitiesModel.size
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val mData = arrayCitiesModel[position]
        holder.itemView.butn.text = mData.name
        //set the click on the float button
        holder.itemView.floatbutton.setOnClickListener {
            click.removeSelectedCity(mData)
        }

    }

    fun upDateData(it: ArrayList<CitiesModel>) {
        arrayCitiesModel = it
        notifyDataSetChanged()
    }

    fun getSlectedCity():ArrayList<CitiesModel> {
        return arrayCitiesModel

    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {

    }

    interface removeSelectedCityInterface {
        fun removeSelectedCity(mData: CitiesModel)
    }
}
