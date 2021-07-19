package com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.caiguru.R
import constant_Webservices.Constant

class SpinnerAdapterss(var context: Context?, var units: Array<String>) :
    BaseAdapter() {
    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.spinner_adapter, parent, false)
            vh =
                ItemRowHolder(
                    view
                )
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }
        val params = view.layoutParams
        params.height = 100
        view.layoutParams = params

        vh.label.text = Constant.convertUnits(context!!, units.get(position))
        return view
    }

    override fun getItem(position: Int): Any? {

        return position

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return units.size
    }


    private class ItemRowHolder(row: View?) {

        val label: TextView

        init {
            this.label = row?.findViewById(R.id.unit) as TextView
        }
    }
}
