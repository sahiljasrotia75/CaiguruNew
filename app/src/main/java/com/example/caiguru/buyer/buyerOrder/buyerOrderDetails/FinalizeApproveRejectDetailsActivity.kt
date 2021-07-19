package com.example.caiguru.buyer.buyerOrder.buyerOrderDetails

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.finalize_approve_reject_details.*
import kotlinx.android.synthetic.main.finalize_approve_reject_details.addressLayout
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception

class FinalizeApproveRejectDetailsActivity : AppCompatActivity() {
    private var orderModel = OrderListModel()
    private lateinit var mvmodel: FinalizeDetailViewModel
    private lateinit var adapter: FinalizeOrderDetailAdapter
    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.finalize_approve_reject_details)
        mvmodel = ViewModelProvider(this).get(FinalizeDetailViewModel::class.java)
        val localData = intent.getParcelableExtra<FinalizeModel>("keyMyOrderApproveReject")!!
        initData()
        SettingUpToolbar(localData)
        setClick(localData)
        setAdapter()
        setObserver()
        spannableTextHeader(localData.seller_name)
    }

    private fun setClick(localData: FinalizeModel) {
        txtCashHeading.setOnClickListener {
            Constant.cashOnDeliveryDialog(this)
        }
        addressLayout.setOnClickListener {
            try {
                if (orderModel.long.isNotEmpty() && orderModel.lat.isNotEmpty()) {
                    val gmmIntentUri: Uri =
                        Uri.parse("google.navigation:q=" + orderModel.lat + "," + orderModel.long)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                    return@setOnClickListener
                } else {
                    Toast.makeText(this, getString(R.string.no_address_found), Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //set the click on the rejected list
        btnreject.setOnClickListener {
            mvmodel.change_buyer_request_reject_status(localData.id)
            progressBar2.visibility = View.VISIBLE
        }

        //*****************api of change_request_status***************//
        //set the click on the button request approval
        btnapprove.setOnClickListener {
            mvmodel.change_buyer_request_status(localData.id)
            progressBar2.visibility = View.VISIBLE
        }

        whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }
    }

    fun setObserver() {
        mvmodel.mSucessfulGetbuyerList().observe(this, Observer {
            progressBar2.visibility = View.GONE
            if (it != null) {
                parentLayout.visibility = View.VISIBLE
                txtNoData.visibility = View.GONE
                try {
                    txtCashDelivery.setText("$" + Constant.roundValue(it.amount.toDouble() - it.credits.toDouble()))

                    spannableText(txtCashDelivery.text.toString())
                    txtMinutAgos.text =
                        "${getString(R.string.Purchased_Order)} ${
                            Constant.timesAgoLogic(
                                it.created_at,
                                this
                            )
                        }."
//                    txtPartilPayment.setText(
//                        Constant.roundValue(it.credits.toDouble()) + getString(
//                            R.string.credits
//                        )
//                    )

                    //txtTotals.setText("$" + Constant.roundValue(it.amount.toDouble()))//set the total price


                    txtAddress.text =  it.address
//                    val gson = Gson()
//                    val model1: ArrayList<AddAddressModel> =
//                        gson.fromJson(
//                            address,
//                            object : TypeToken<ArrayList<AddAddressModel>>() {}.type
//                        )
//                    for (item in model1) {
//                        txtAddress.text = item.address
//                    }
                    orderModel = it
                } catch (e: Exception) {
                    e.printStackTrace()
                }


//                val productdetail = it.products
//
//                val gson = Gson()
//                val model1: ArrayList<PostShoppingModel> =
//                    gson.fromJson(
//                        productdetail,
//                        object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
//                    )
                adapter.update(it.products)

            } else {
                txtNoData.visibility = View.VISIBLE
                parentLayout.visibility = View.GONE

            }

        })


        //failure case
        mvmodel.mFailureGetbuyerList().observe(this, Observer {
            progressBar2.visibility = View.GONE
            txtNoData.visibility = View.VISIBLE
            parentLayout.visibility = View.GONE
            Constant.showToast(it, this)
        })


        //****************set the observer of approval
        mvmodel.mSucessfulRequestApprovals().observe(this, Observer {
            progressBar2.visibility = View.GONE
            alertDialog(it)
        })
        //failure
        mvmodel.mFailureRequestApprovals().observe(this, Observer {
            progressBar2.visibility = View.GONE
            alertDialog1(it)
        })

        //*********set the obaserver of rejected list
        mvmodel.mSucessfulRejectedRequest().observe(this, Observer {
            progressBar2.visibility = View.GONE
            alertDialog(it)


        })
        mvmodel.mFailureRejectedRequest().observe(this, Observer {
            progressBar2.visibility = View.GONE
            alertDialog1(it)


        })



    }

    private fun spannableText(price: String) {
        val textStart = getString(R.string.you_must_charge)
        val textMiddle = price
        val textEnd = getString(R.string.to_your_client)

        val allText = textStart + " " + textMiddle + " " + textEnd
        val spannable = SpannableString(allText)
        val boldSpan = StyleSpan(Typeface.BOLD)

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            textStart.length ,
            allText.length- textEnd.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan,
            textStart.length ,
            allText.length- textEnd.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        txtChargeHeader.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    private fun spannableTextHeader(name: String) {
        val textStart = getString(R.string.textHeader1)
        val textMiddle = name+"."
        val textEnd = getString(R.string.text_header2)

        val allText = textStart + " " + textMiddle + " " + textEnd
        val spannable = SpannableString(allText)
        val boldSpan = StyleSpan(Typeface.BOLD)

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            textMiddle.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            textMiddle.length,
            textEnd.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            textStart.length + 1,
            allText.length - textEnd.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan,
            textStart.length + 1,
            allText.length - textEnd.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        txtHeader2.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    //Alert dialog
    private fun alertDialog(it: String) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setCancelable(false)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            setResult(RESULT_OK, intent)
            finish()
        }
        mDialog.show()

    }  //Alert dialog

    private fun alertDialog1(it: String) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setCancelable(false)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()

        }
        mDialog.show()

    }

    private fun initData() {
        val img = getResources().getDrawable(R.drawable.ic_baseline_info_24)
        txtCashHeading.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            img,
            null
        )
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar(localData: FinalizeModel) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        whatsAppTextICons.visibility = View.VISIBLE
        text.text = localData.seller_name
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapter() {
        val layoutpag = LinearLayoutManager(this)
        adapter = FinalizeOrderDetailAdapter(this)
        recyclerOrderDetail.layoutManager = layoutpag
        recyclerOrderDetail.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val localData = intent.getParcelableExtra<FinalizeModel>("keyMyOrderApproveReject")!!
        mvmodel.get_buyer_request_detail(localData.id,localData.list_type)
        progressBar2.visibility = View.VISIBLE
    }

}