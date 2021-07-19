package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.add_price_custom_dialog.view.*
import kotlinx.android.synthetic.main.store_child_adapter.view.*
import kotlinx.android.synthetic.main.store_child_adapter.view.txtLoadMore
import kotlinx.android.synthetic.main.store_parent_adapter.view.*
import kotlinx.android.synthetic.main.view_product_shopping_adapter.view.*

class StoreChildAdapter(
    var context: Context


) : RecyclerView.Adapter<StoreChildAdapter.ViewHolder>() {

    var child = ArrayList<PostShoppingModel>()
    private var setLoadMoreText: String = "true"

    var click = context as setInterFaceProduct
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.store_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.txtProductname.text =
            list.name
        var endTxt = "$0"
        if (list.price.isEmpty() || list.price <= "0") {
            endTxt = "$0"
        } else {
            endTxt =
                "$" + (Constant.roundValue(list.priceWithComission.toDouble()))
        }
        val startTxt = context.getString(R.string.price_by) + " " + Constant.convertUnits(
            context,
            list.unit
        ) + ":" + " "
        val allText = startTxt + endTxt
        val spannable = SpannableString(allText)

        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.hard_grey)),
            0,
            startTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.purple)),
            startTxt.length,
            endTxt.length + startTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            boldSpan, startTxt.length,
            endTxt.length + startTxt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        holder.itemView.txtUnit.setText(spannable, TextView.BufferType.SPANNABLE)

//        holder.itemView.txtUnit.text =
//            context.getString(R.string.price_by) + " " + Constant.convertUnits(
//                context,
//                list.unit
//            ) + ":"

        if (list.total.isEmpty()) {
            holder.itemView.txtPriceAddingQty.text = "$0"
        } else {
            holder.itemView.txtPriceAddingQty.text =
                "$" + Constant.roundValue(list.total.toDouble())
        }

        Glide.with(context).load(list.image).placeholder(R.drawable.product_placeholder)
            .into(holder.itemView.imgProduct)

        holder.itemView.qty.text = list.qty
//set the price in another text
//        if (list.price.isEmpty() || list.price <= "0") {
//            holder.itemView.price.text = "$0"
//        } else {
//            holder.itemView.price.text =
//                "$" + (Constant.roundValue(list.priceWithComission.toDouble()))
//        }

        //click on the image
        holder.itemView.imgProduct.setOnClickListener {
            click.viewFullScreenProductClick(child, position)

        }

        //set the subtract product click
        holder.itemView.subtractbtn.setOnClickListener {
            if (list.qty.toInt() > 0) {
                list.qty = (list.qty.toInt() - 1).toString()
                list.total = Total(list).toString()//set the total
                list.LocalTotal = AllTotal(child).toString()//set the total
                list.partialComission = setTotalPartialComission(child).toString()//partial payments
                click.productAddClick(child, list, position)
                notifyItemChanged(position)
            }

        }

        //set the add product click
        holder.itemView.addBtn.setOnClickListener {
            list.qty = (list.qty.toInt() + 1).toString()
            list.total = Total(list).toString()//set the total
            list.LocalTotal = AllTotal(child).toString()//set the total
            list.partialComission = setTotalPartialComission(child).toString()
            click.productAddClick(child, list, position)
            notifyItemChanged(position)
        }

        //add the high qty  add through popup
        holder.itemView.qty.setOnClickListener {

            addPriceCustomDialog(list, position)
        }
        //set the click on the load more
        holder.itemView.txtLoadMore.setOnClickListener {
            click.loadMoreSearchClick()
            holder.itemView.txtLoadMore.text = context.getString(R.string.pleasewait)
            holder.itemView.txtLoadMore.isClickable = false
        }
        //show the load more btn in case of search
        if (child[0].searchCaseShowBtn.equals("showLoadMoreBtn")) {
            if (child[0].count > 4 && child[0].count != child.size) {
                if (position == child.size - 1) {
                    holder.itemView.txtLoadMore.visibility = View.VISIBLE
                } else {
                    holder.itemView.txtLoadMore.visibility = View.GONE
                }
            } else {
                holder.itemView.txtLoadMore.visibility = View.GONE
            }
        }
        if (setLoadMoreText.equals("true")) {
            holder.itemView.txtLoadMore.text = context.getString(R.string.load_more_products_store)
            holder.itemView.txtLoadMore.isClickable = true
        }
    }

    // add price custom dialog
    private fun addPriceCustomDialog(
        model: PostShoppingModel,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_price_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(view)
            .create()
        //prefilling
        view.productheading.text = context.getString(R.string.add_qty)
        view.edtPrice.hint = context.getString(R.string.enter_your_qty)
        // Set an EditText view to get user input
        view.edtPrice.setInputType((InputType.TYPE_CLASS_NUMBER))
        if (model.qty.toInt() <= 0) {
            view.edtPrice.setText("")
        } else {
            view.edtPrice.setText(model.qty)
        }

        mBuilder.show()
        //set the click on the button
        view.btnAddPrice.setOnClickListener {
            if (view.edtPrice.text.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.Please_add_product_price),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (view.edtPrice.text.toString() == "0") {
                Toast.makeText(
                    context,
                    context.getString(R.string.qty_nust_be_greater_than_zero),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                model.qty = view.edtPrice.text.toString()
                model.total = Total(model).toString()//set the total
                model.LocalTotal = AllTotal(child).toString()//set the total
                model.partialComission = setTotalPartialComission(child).toString()
                click.productAddClick(child, model, position)
                notifyItemChanged(position)
                mBuilder.dismiss()
            }
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
            if (item.price != item.priceWithComission) {
                if (totalPatialComission == 0.0) {
                    val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                    totalPatialComission = allTotal * item.qty.toDouble()
                } else {
                    val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                    totalPatialComission += allTotal * item.qty.toDouble()
                }
            } else {
                if (totalPatialComission == 0.0) {
                    val allTotal = 1
                    totalPatialComission = allTotal * item.qty.toDouble()
                } else {
                    val allTotal = 1
                    totalPatialComission += allTotal * item.qty.toDouble()
                }
            }

        }
        return totalPatialComission
    }

    fun updateData(
        productsList: ArrayList<PostShoppingModel>
    ) {
        child = productsList
        notifyDataSetChanged()
    }

    fun clearCartSingleProduct(productId: String) {
        for (item in child) {
            if (item.id == productId) {
                item.qty = "0"
                item.LocalTotal = "0"
                item.total = "0"
                item.partialComission = "0"
            }
        }
        notifyDataSetChanged()
    }

    fun setQty(list: PostShoppingModel) {
        for (item in child) {
            if (item.id == list.id) {
                item.qty = list.qty
                item.LocalTotal = list.LocalTotal
                item.total = list.total
                item.partialComission = list.partialComission
            }
        }
        notifyDataSetChanged()
    }

    fun clearCart() {
        for (childitem in child) {
            childitem.qty = "0"
            childitem.LocalTotal = "0"
            childitem.total = "0"
            childitem.partialComission = "0"
        }
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface setInterFaceProduct {
        fun productAddClick(
            data: ArrayList<PostShoppingModel>,
            list: PostShoppingModel,
            position: Int
        )

        fun viewFullScreenProductClick(child: ArrayList<PostShoppingModel>, position: Int)
        fun loadMoreSearchClick()
    }
}
