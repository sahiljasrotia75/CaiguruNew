package com.example.caiguru.buyer.chooseSellers.chooseSellerSuggestProduct

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.suggest_product_for_next_time_adapter.view.*

class SuggestProductForNextTimeAdapter(var context: Context) :
    RecyclerView.Adapter<SuggestProductForNextTimeAdapter.ViewHolder>() {
    private var mData = ArrayList<SuggestedProductModel>()
    var click = context as deleteDataInterface//click
    private lateinit var suggestedProduct: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.suggest_product_for_next_time_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mData[position]
        holder.itemView.productname.text = data.position + "${position + 1}"
       // mData[position].suggestedProductName = holder.itemView.edtaddProduct.text.toString()
        holder.itemView.imgdeleteData.setOnClickListener {
            holder.itemView.edtaddProduct.setText("")
            click.deleteData(mData, position)
        }


        //**********************set the click on the search button*********************//
        holder.itemView.edtaddProduct.addTextChangedListener(object : TextWatcher {



            override fun afterTextChanged(s: Editable?) {
                suggestedProduct= holder.itemView.edtaddProduct.text.toString()
                //notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                data.suggestedProductName = s.toString()
//                notifyDataSetChanged()
            }

        })
    }


    fun getAllData(): ArrayList<SuggestedProductModel> {
        return mData

    }


    fun update(it: ArrayList<SuggestedProductModel>) {

        if (it != null) {
            mData = it
        }
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface deleteDataInterface {

        fun deleteData(
            mData: ArrayList<SuggestedProductModel>,
            position: Int
        )
    }
}
