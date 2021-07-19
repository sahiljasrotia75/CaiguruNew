package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.add_price_custom_dialog.view.*
import kotlinx.android.synthetic.main.cutomer_upload_openlist_child_adapter.view.*
import java.lang.Exception

class CustomerRequestListChildAdapter(
    var context: Context,
    customerRequestListParentAdapter: CustomerRequestListParentAdapter
) :
    RecyclerView.Adapter<CustomerRequestListChildAdapter.Viewholder>() {
    private var localSeekBars: Int = 0
    var data = GetProfileModel()
    private var isUploadedImage: Boolean = false
    var aarayModel = ArrayList<PostShoppingModel>()
    var update = customerRequestListParentAdapter as UpdateParent
    var click = context as UpdateChildImage
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.cutomer_upload_openlist_child_adapter, parent, false)
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return aarayModel.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val mData = aarayModel[position]
        val gson = Gson()
        val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
        data = gson.fromJson(json, GetProfileModel::class.java)
        holder.itemView.productName.text =
            mData.name + "(" + Constant.convertUnits(context, mData.unit) + ")"
        holder.itemView.quantity.text = mData.qty
        if (mData.price == "0" || mData.price.isEmpty()) {
            holder.itemView.productprice.setText("")
        } else {
            val allComission=data.plateform_commission.toDouble() + localSeekBars

            val percentage =
                mData.price.toDouble() / 100 * allComission

            val allTotals = mData.price.toDouble() + percentage

            mData.PriceComission=Constant.roundValue(allTotals)
            holder.itemView.productprice.setText(mData.PriceComission)
            update.updateParent(mData.price,mData.PriceComission, position, "yes")
        }
//open the dilog
        holder.itemView.productprice.setOnClickListener {
            addProductCustomDialog(mData, position)
        }

        if (mData.image.isEmpty()) {
            Glide.with(context).load(R.drawable.product_placeholder)
                .into(holder.itemView.productImage)
            holder.itemView.removeImage.visibility = View.GONE
        } else {
            Glide.with(context)
                .load(mData.image)
                .into(holder.itemView.productImage)
            // Picasso.with(context).load(mData.image).rotate(-270.0f).fit().into(holder.itemView.productImage);
            holder.itemView.removeImage.visibility = View.VISIBLE
        }

        holder.itemView.removeImage.setOnClickListener {
            aarayModel[position].image = ""
            notifyItemChanged(position)
        }
        if (aarayModel[position].progressBar == true) {
            holder.itemView.productProgressBar.visibility = View.VISIBLE
        } else {
            holder.itemView.productProgressBar.visibility = View.GONE
        }
        holder.itemView.productImage.setOnClickListener {
            if (isUploadedImage == true) {
//                Toast.makeText(
//                    context,
//                    context.getString(R.string.upoading_image),
//                    Toast.LENGTH_SHORT
//                ).show()
                Constant.showToast(  context.getString(R.string.upoading_image),context)

            } else {

                click.upDateImage(position, mData)
            }

        }

    }

    // add price custom dialog
    private fun addProductCustomDialog(
        mData: PostShoppingModel,
        position: Int
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.add_price_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(context)
            .setView(view)
            .create()
        mBuilder.show()
        //prefilling
        view.PriceIncludedText.visibility=View.VISIBLE
        if (mData.price.isEmpty() || mData.price.toDouble() <= 0.0) {
            view.edtPrice.setText("")
        } else {
            view.edtPrice.setText(mData.price)
        }
        //set the click on the button
        view.btnAddPrice.setOnClickListener {
            if (view.edtPrice.text.isEmpty()) {
                Toast.makeText(
                    context,
                    context.getString(R.string.Please_add_product_price),
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                var price = view.edtPrice.text.toString().trim()
                price = price.replace(",", ".")
//                val allComission=data.plateform_commission.toDouble() + localSeekBars
//                val percentage =
//                    price.toDouble() / 100 * allComission
//                val allTotals = price.toDouble() + percentage
                mData.price = Constant.roundValue(price.toDouble())
                aarayModel.set(position, mData)
                notifyDataSetChanged()
               // update.updateParent(mData.price, position, "yes")
                mBuilder.dismiss()
            }
        }

    }

    fun updateData(
        childModel: ArrayList<PostShoppingModel>,
        localSeekBar: Int
    ) {
        try {
            localSeekBars = localSeekBar
           // isUploadedImage = false
            aarayModel = childModel
            notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    class Viewholder(view: View) : RecyclerView.ViewHolder(view)


    interface UpdateParent {
        fun updateParent(
            p0: String,
            priceWithComission: String,
            position: Int,
            s: String
        )
    }

    interface UpdateChildImage {

        fun upDateImage(
            position: Int,
            mData: PostShoppingModel
        )

        fun removeImage(position: Int, mData: PostShoppingModel)
    }

    fun UpdateImageFail(uploadedImage: Boolean) {
        isUploadedImage = uploadedImage
        notifyDataSetChanged()
    }

    //progress bar show
    fun UpdateInProgress(childData: PostShoppingModel, childPosition: Int) {
        aarayModel[childPosition].progressBar = childData.progressBar
        notifyDataSetChanged()
    }

}


