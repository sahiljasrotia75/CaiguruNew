package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.cart_adapter.view.*
import kotlinx.android.synthetic.main.cart_adapter.view.addBtn
import kotlinx.android.synthetic.main.cart_adapter.view.qty
import kotlinx.android.synthetic.main.cart_adapter.view.subtractbtn


class CartAdapter(
    var context: Context

) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    var click = context as setProductAddInterface
    var child = ArrayList<PostShoppingModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.cart_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        list.total = Total(list).toString()//set the total
        list.LocalTotal = AllTotal(child).toString()//set the total
        list.partialComission = setTotalPartialComission(child).toString()//partial payments

        holder.itemView.productName.text =
            list.name + " " + "(" + Constant.convertUnits(context, list.unit) + ")"

        holder.itemView.prices.text = "$" + Constant.roundValue(list.priceWithComission.toDouble())
        holder.itemView.qty.text = list.qty

        if (list.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        } else {
            Glide.with(context).load(list.image).placeholder(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
        }

        if (list.total.isEmpty()) {
            holder.itemView.txtAllOverTotal.text = "$0"
        } else {
            holder.itemView.txtAllOverTotal.text =
                "$" + Constant.roundValue(list.total.toDouble())
        }
        //set the click on the image
//        holder.itemView.productImage.setOnClickListener {
//
//
//        }
        //set the clik on the delete
        holder.itemView.imgDelete.setOnClickListener {
            val mDialog = AlertDialog.Builder(context)
            mDialog.setTitle(context.getString(R.string.alert))
            mDialog.setMessage(context.getString(R.string.remove_product_cart))
            mDialog.setPositiveButton(
                context.getString(R.string.yes)
            ) { dialog, which ->
                val productId=child[position].id
                child.removeAt(position)
                if (child.size > 0) {
                    for (item in child) {
                     //   item.total = Total(list).toString()//set the total
                        item.LocalTotal = AllTotal(child).toString()//set the total
                        item.partialComission =
                            setTotalPartialComission(child).toString()//partial payments
                    }

                }
                click.deleteProduct( child,productId)
                notifyDataSetChanged()
                dialog.cancel()
            }
            mDialog.setNegativeButton(
                context.getString(R.string.no)
            ) { dialog, which ->
                dialog.cancel()

            }
            mDialog.show()
        }

        //set the subtract product click
        holder.itemView.subtractbtn.setOnClickListener {
            if (list.qty.toInt() > 0) {
                list.qty = (list.qty.toInt() - 1).toString()
                list.total = Total(list).toString()//set the total
                list.LocalTotal = AllTotal(child).toString()//set the total
                list.partialComission = setTotalPartialComission(child).toString()//partial payments
                click.CartproductAddClick(child, list, position)
                notifyItemChanged(position)
            }

        }

        //set the add product click
        holder.itemView.addBtn.setOnClickListener {
            list.qty = (list.qty.toInt() + 1).toString()
            list.total = Total(list).toString()//set the total
            list.LocalTotal = AllTotal(child).toString()//set the total
            list.partialComission = setTotalPartialComission(child).toString()
            click.CartproductAddClick(child, list, position)
            notifyItemChanged(position)
        }

    }

    private fun Total(list: PostShoppingModel): Double {
        var total = 0.0

        total += list.priceWithComission.toDouble() * list.qty.toDouble()
        return Constant.roundValue(total).toDouble()

    }

    private fun AllTotal(list: ArrayList<PostShoppingModel>): Double {
        var total = 0.0
        for (item in list) {

            if (total == 0.0) {
                total = item.priceWithComission.toDouble() * item.qty.toDouble()
            } else {
                total += item.priceWithComission.toDouble() * item.qty.toDouble()
            }
        }
        return Constant.roundValue(total).toDouble()

    }

    private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
        //set the  total comission of the products
        var totalPatialComission = 0.0
        for (item in data) {
            if (totalPatialComission == 0.0) {
                val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                totalPatialComission = allTotal * item.qty.toDouble()
            } else {
                val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                totalPatialComission += allTotal * item.qty.toDouble()
            }
        }
        return totalPatialComission
    }

    fun update(it: ArrayList<PostShoppingModel>) {
        child = it
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


    interface setProductAddInterface {
        fun deleteProduct(
            child: ArrayList<PostShoppingModel>,
            productId: String
        )

        fun CartproductAddClick(
            toTal: ArrayList<PostShoppingModel>,
            list: PostShoppingModel,
            position: Int
        )
    }

}
