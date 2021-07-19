package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import kotlinx.android.synthetic.main.payment_method_adapter.view.*

class PaymentMethodAdapter(var context: Context) :
    RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder>() {
    var list = ArrayList<PaymentMethodModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.payment_method_adapter, parent, false)
        return ViewHolder(
            view
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.itemView.titlePayment.text = model.paymentName
        if (model.isSelected) {
            holder.itemView.imagepayment.setImageResource(model.SelectedDrawable)
        } else {
            holder.itemView.imagepayment.setImageResource(model.UnSelectedDrawable)
        }
        //set the clik on the drawable
        holder.itemView.setOnClickListener {
            model.isSelected = !model.isSelected
            notifyDataSetChanged()
        }
    }

    fun update(categoryData: ArrayList<PaymentMethodModel>) {
        list = categoryData
        notifyDataSetChanged()

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun getSelectedData(): String {
        var selectedPayment = ""
        for (item in list) {
            if (item.isSelected) {
                if (selectedPayment.isEmpty()) {
                    selectedPayment = item.paymentName
                } else {
                    selectedPayment = "$selectedPayment, ${item.paymentName}"
                }
            }
        }
        return selectedPayment
    }

}