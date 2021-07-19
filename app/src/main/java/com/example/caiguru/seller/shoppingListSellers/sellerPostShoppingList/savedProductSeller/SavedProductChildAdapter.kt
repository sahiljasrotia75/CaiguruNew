package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.imageFullScreenDisplay.ImageFullScreenActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.saved_product_child_adapter.view.*
import kotlinx.android.synthetic.main.saved_product_child_adapter.view.productImage
import kotlinx.android.synthetic.main.saved_product_child_adapter.view.productName
import kotlinx.android.synthetic.main.saved_product_child_adapter.view.productUnit

class SavedProductChildAdapter(
    var context: Context


) : RecyclerView.Adapter<SavedProductChildAdapter.ViewHolder>() {

    var child = ArrayList<PostShoppingModel>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.saved_product_child_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return child.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = child[position]
        holder.itemView.productName.text =
            list.name + "(" + Constant.convertUnits(context, list.unit) + ")"

        holder.itemView.productUnit.text =
            "$" + Constant.roundValue(list.priceWithComission.toDouble())

        Glide.with(context).load(list.image).placeholder(R.drawable.product_placeholder)
            .into(holder.itemView.productImage)

        //set the check selected or unselected
        if (list.isProductSelected) {
            holder.itemView.productSelected.setImageDrawable(context.resources.getDrawable(R.drawable.ic_checkbox_selected_product))
        } else {
            holder.itemView.productSelected.setImageDrawable(context.resources.getDrawable(R.drawable.ic_un_checkbox_product))
        }

        //set the click on the check box
        holder.itemView.productSelected.setOnClickListener {
            list.isProductSelected = !list.isProductSelected
            notifyDataSetChanged()
        }
//click on the image
        holder.itemView.productImage.setOnClickListener {
            val array = ArrayList<PostShoppingModel>()
            if (list.image != "http://18.229.181.113/dev/public/users/defaultprod.png") {
                val intent = Intent(context, ImageFullScreenActivity::class.java)
                //change code image
                for (item in child) {
                    val model = PostShoppingModel()
                    model.image = item.image
                    model.unit = item.unit
                    model.priceWithComission = item.priceWithComission
                    model.name = item.name
                    array.add(model)
                }
                intent.putExtra("keyImage", array)
                intent.putExtra("keyImagePosition", position)
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                context.startActivity(intent)
            }


        }

    }

    fun updateData(
        productsList: ArrayList<PostShoppingModel>
    ) {
        child = productsList
        notifyDataSetChanged()
    }

    //get  all array data
    fun getAllArrayData(): ArrayList<PostShoppingModel> {
        return child
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)


}
