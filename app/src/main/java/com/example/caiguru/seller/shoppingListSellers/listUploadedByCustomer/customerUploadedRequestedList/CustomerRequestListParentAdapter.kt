package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedDeliveryDetails.CustomerListDeliveryDetailActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod.PaymentMethodAdapter
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod.PaymentMethodModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.cutomer_upload_openlist_parent_adapter.view.*
import kotlinx.android.synthetic.main.openlist_parentadapter.view.cardView
import kotlinx.android.synthetic.main.openlist_parentadapter.view.recyclerChild
import kotlinx.android.synthetic.main.openlist_parentadapter.view.relative
import kotlinx.android.synthetic.main.openlist_parentadapter.view.updown
import kotlinx.android.synthetic.main.payment_option_dialog.*
import kotlinx.android.synthetic.main.seekbar_alert_dialog.btnOk


class CustomerRequestListParentAdapter(var context: Context) :
    RecyclerView.Adapter<CustomerRequestListParentAdapter.Viewholder>(),
    CustomerRequestListChildAdapter.UpdateParent {
    private var allPaymentdata: String = ""
    private lateinit var paymentmethodAdapter: PaymentMethodAdapter
    private var localSeekBar: Int = 10
    private var seller_minimum_commission: Int = 0
    private lateinit var vHolder: Viewholder
    var model1 = ArrayList<PostShoppingModel>()
    private var total = 0.0
    var model = CustomerChildModel()
    var childSet = 0
    var data = GetProfileModel()
    var click = context as reportListInterface
    lateinit var recyclerAdapter: CustomerRequestListChildAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {

        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.cutomer_upload_openlist_parent_adapter, parent, false)
        val gson = Gson()
        model1 =
            gson.fromJson(
                model.product_details,
                object : TypeToken<ArrayList<PostShoppingModel>>() {}.type

            )
        return Viewholder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }


    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        vHolder = holder
        val mData = model
        //**************profile data
        val gson = Gson()
        val json = Constant.getPrefs(context).getString(Constant.PROFILE, "")
        data = gson.fromJson(json, GetProfileModel::class.java)
        seller_minimum_commission = data.seller_minimum_commission.toInt()
        //***************************hide or un hide the seekbar
        if (data.enable_seller_commission == "1") {
            localSeekBar = holder.itemView.seekbarCustom.progress
            holder.itemView.txtComission.visibility = View.VISIBLE
            holder.itemView.seekbarCustom.visibility = View.VISIBLE
            holder.itemView.comission.visibility = View.VISIBLE
        } else {
            localSeekBar = 0
            holder.itemView.txtComission.visibility = View.GONE
            holder.itemView.seekbarCustom.visibility = View.GONE
            holder.itemView.comission.visibility = View.GONE
        }

        holder.itemView.txtComission.text = "(" + seller_minimum_commission + "%" + ")"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.itemView.seekbarCustom.min = seller_minimum_commission
        }
        holder.itemView.seekbarCustom.progress = seller_minimum_commission

        holder.itemView.txtlist.text = categoryData(mData.cat_id)

        if (mData.isExpanded) {
            holder.itemView.txtlist.text = categoryData(mData.cat_id)
            holder.itemView.relative.visibility = View.VISIBLE
            holder.itemView.updown.setImageResource(R.drawable.ic_up_arrow)

        } else {
            holder.itemView.txtlist.text = categoryData(mData.cat_id)
            holder.itemView.relative.visibility = View.GONE
            holder.itemView.updown.setImageResource(R.drawable.ic_down_arrow_grey)
        }

        //set the click on Cardview
        holder.itemView.cardView.setOnClickListener {
            model.isExpanded = !mData.isExpanded
            notifyDataSetChanged()
        }
        //*******************Child Adapter*****************//
        if (childSet == 0) {
            childSet = 1
            recyclerAdapter = CustomerRequestListChildAdapter(context, this)
            val manager = LinearLayoutManager(context)
            holder.itemView.recyclerChild.layoutManager = manager //set layout in recycler
            holder.itemView.recyclerChild.adapter = recyclerAdapter


            val gson = Gson()
            model1 =
                gson.fromJson(
                    mData.product_details,
                    object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
                )
            if (data.enable_seller_commission == "1") {
                localSeekBar = holder.itemView.seekbarCustom.progress
                recyclerAdapter.updateData(model1, localSeekBar)
            } else {
                recyclerAdapter.updateData(model1, localSeekBar)
            }

        }

        //set the text
        val udata = context.getString(R.string.Delivery_Details)
        val text = SpannableString(udata)
        text.setSpan(UnderlineSpan(), 0, udata.length, 0)
        holder.itemView.deliveryDetails.text = text

        //set the report list text
        val reporttext = context.getString(R.string.report_list)
        val textReport = SpannableString(reporttext)
        textReport.setSpan(UnderlineSpan(), 0, textReport.length, 0)
        holder.itemView.txtReportList.text = textReport


        // Set a SeekBar change listener
        holder.itemView.seekbarCustom.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                holder.itemView.txtComission.text = "(" + i + "%" + ")"
                seekBar.max = data.seller_maximum_commission.toInt()
                seller_minimum_commission = data.seller_minimum_commission.toInt()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.itemView.seekbarCustom.min = seller_minimum_commission
                }
                //add the seek bar
                localSeekBar = seekBar.progress
                model.comission = i.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                for (item in model1) {
                    if (item.price.isNotEmpty()) {
                        setSekBarAlertDialoG(seekBar)
                        break
                    }
                }
                if (seekBar.progress < seller_minimum_commission) {
                    seekBarAlertToast()
                }
            }
        })


        //set the click on the viwew details
        holder.itemView.deliveryDetails.setOnClickListener {
            val intent = Intent(context, CustomerListDeliveryDetailActivity::class.java)
            intent.putExtra("model", model)
            context.startActivity(intent)
        }
        //*************set the click on repoortList
        holder.itemView.txtReportList.setOnClickListener {
            val mDialog = AlertDialog.Builder(context)
            mDialog.setTitle(context.getString(R.string.alert))
            mDialog.setCancelable(true)
            mDialog.setMessage(context.getString(R.string.report_text3))
            mDialog.setPositiveButton(
                context.getString(R.string.ok)
            ) { dialog, which ->
                click.reportList(model)
                dialog.cancel()
            }
            mDialog.setNegativeButton(
                context.getString(R.string.cancel)
            ) { dialog, which ->
                dialog.cancel()
            }
            mDialog.show()


        }

        if (mData.amount.isNotEmpty()) {
            holder.itemView.price.text = "$" + (Constant.roundValue(mData.amount.toDouble()))

        } else {
            holder.itemView.price.text = "$" + "0.00"
        }
        //set the progressbar
        if (localSeekBar > 0) {
            holder.itemView.seekbarCustom.setProgress(localSeekBar)
        }
        //***************set the text*************//
        if (allPaymentdata.isNotEmpty()) {
            mData.payment_methods = ConvertPuchasedOrder(allPaymentdata)
            holder.itemView.edtSelectPaymentMethod.text = allPaymentdata
        }
        //**************set the click on the payment methods
        holder.itemView.edtSelectPaymentMethod.setOnClickListener {
            val alltext = holder.itemView.edtSelectPaymentMethod.text.toString().trim()
            openPaymentDialog(alltext)

        }
    }


    private fun setSekBarAlertDialoG(seekBar: SeekBar) {

        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.seekbar_alert_dialog)
        dialog.setCancelable(false)
        dialog.show()

        dialog.btnOk.setOnClickListener {
            recyclerAdapter.updateData(model1, seekBar.progress)
            dialog.dismiss()
        }

    }

    private fun seekBarAlertToast() {
        Toast.makeText(
            context,
            context.getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
            Toast.LENGTH_SHORT
        ).show()

    }

    //update the adapter
    fun update(it: CustomerChildModel) {
        model = it
        notifyDataSetChanged()
        if (childSet == 1) {
            val gson = Gson()
            model1 =
                gson.fromJson(
                    it.product_details,
                    object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
                )
            if (data.enable_seller_commission == "1") {
                recyclerAdapter.updateData(model1, localSeekBar)
            } else {
                recyclerAdapter.updateData(model1, localSeekBar)
            }
        }
    }


    //***************** Matching id's *********************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(context)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return  context.getString(R.string.mix_category_product)
    }


    class Viewholder(view: View) : RecyclerView.ViewHolder(view) {


    }

    @SuppressLint("SetTextI18n")
    override fun updateParent(
        price: String,
        priceWithComission: String,
        position: Int,
        s: String
    ) {
        val gson = Gson()
        model1[position].price = price
        model1[position].priceWithComission = priceWithComission

        val json = gson.toJson(model1)
        model.product_details = json
        total = 0.0
        for (i in 0 until model1.size) {
            if (model1[i].priceWithComission.isEmpty()) {
                model1[i].priceWithComission = "0"
            }
            if (model1[i].priceWithComission.startsWith(".")) {
                model1[i].priceWithComission = "0${model1[i].priceWithComission}"
            }
            total += model1[i].priceWithComission.trim().toDouble() * model1[i].qty.trim()
                .toDouble()
        }
        if (s == "yes") {
            childSet = 1
        }

        vHolder.itemView.price.text = "$" + (Constant.roundValue(total))
        model.totals = total.toString()


    }


    fun UpdateImage(image: String, position: Int) {
        model1[position].image = image
        model1[position].isUploadedImage = false
        recyclerAdapter.updateData(model1, localSeekBar)

    }

    fun UpdateImageFailProgress(uploadedImage: Boolean) {
        recyclerAdapter.UpdateImageFail(uploadedImage)
    }

    fun UpdateInProgress(childData: PostShoppingModel, childPosition: Int) {
        recyclerAdapter.UpdateInProgress(childData, childPosition)

    }

    //*******************open the payment methods**************//
    private fun openPaymentDialog(alltext: String) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.payment_option_dialog)
        dialog.show()
        val manager = LinearLayoutManager(context)
        dialog.paymentMethodRecycler.layoutManager = manager
        paymentmethodAdapter = PaymentMethodAdapter(context)
        dialog.paymentMethodRecycler.adapter = paymentmethodAdapter
        if (alltext.isEmpty()) {
            paymentmethodAdapter.update(Constant.setUpPaymentMethods(context))
        } else {
            //prefilling case
            paymentmethodAdapter.update(setUpPrefillingText(alltext))
        }
        //**************ok button
        dialog.btnOk.setOnClickListener {
            if (paymentmethodAdapter.getSelectedData().isEmpty()) {
//                Toast.makeText(
//                    context,
//                    context.getString(R.string.Please_select_at_least_one),
//                    Toast.LENGTH_SHORT
//                ).show()
                Constant.showToast(  context.getString(R.string.Please_select_at_least_one),context)
                return@setOnClickListener
            } else {
                allPaymentdata = paymentmethodAdapter.getSelectedData()
                model.payment_methods = ConvertPuchasedOrder(allPaymentdata)
                vHolder.itemView.edtSelectPaymentMethod.text = allPaymentdata
                notifyItemChanged(1)
                dialog.dismiss()
            }
        }
    }

    //prefilling in the dialog
    private fun setUpPrefillingText(alltext: String): ArrayList<PaymentMethodModel> {
        val localArray = Constant.setUpPaymentMethods(context)
        val removeSpaceString = alltext.replace(", ", ",")
        val allmyData = removeSpaceString.split(",")
        for (items in localArray) {
            for (myString in allmyData) {
                if (items.paymentName == myString) {
                    items.isSelected = true
                }
            }
        }
        return localArray
    }

    //*************send the data to the api******************//
    private fun ConvertPuchasedOrder(purchaseText: String): String {
        val localArray = Constant.setUpPaymentMethods(context)
        val removeSpaceString = purchaseText.replace(", ", ",")
        val allmyData = removeSpaceString.split(",")
        var selectedPayment = ""
        for (items in localArray) {
            for (myString in allmyData) {
                if (items.paymentName == myString) {
                    if (selectedPayment.isEmpty()) {
                        selectedPayment = items.paymentId
                    } else {
                        selectedPayment = "$selectedPayment,${items.paymentId}"
                    }
                }
            }
        }
        return selectedPayment
    }

    interface reportListInterface {
        fun reportList(data: CustomerChildModel)
    }
}
