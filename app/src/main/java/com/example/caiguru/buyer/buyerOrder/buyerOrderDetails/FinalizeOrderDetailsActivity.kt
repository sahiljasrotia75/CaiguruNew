package com.example.caiguru.buyer.buyerOrder.buyerOrderDetails

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.seller.sellerOrder.orderRejected.OrderRejectedActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.*
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.parentLayout
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.progressBar2
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.recyclerOrderDetail
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.txtAddress
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.txtCashDelivery
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.txtCashHeading
import kotlinx.android.synthetic.main.delivered_confirm_dialog.*
import kotlinx.android.synthetic.main.finalize_approve_reject_details.*
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import java.lang.Exception

class FinalizeOrderDetailsActivity : AppCompatActivity() {
    private lateinit var mvmodel: FinalizeDetailViewModel
    private lateinit var adapter: FinalizeOrderDetailAdapter
    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_finalize_order_details)
        mvmodel = ViewModelProvider(this).get(FinalizeDetailViewModel::class.java)

        initData()
        SettingUpToolbar()
        setClick()
        setAdapter()
        setObserver()
        showBtnHide()
    }

    private fun showBtnHide() {
        if (intent.hasExtra("KeyShowCaNcelBtnFromDeliveryTab")) {
            btnCancel.visibility = View.VISIBLE
        } else {
            btnCancel.visibility = View.GONE
        }

    }

    private fun setClick() {
        txtCashHeading.setOnClickListener {
            Constant.cashOnDeliveryDialog(this)
        }
        whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }

        //set the cancel click
        btnCancel.setOnClickListener {
            setAlertDialog()

        }
    }

    private fun setAlertDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.delivered_confirm_dialog)
        dialog.show()
        dialog.txtHeader.text=getString(R.string.are_you_sure)
        dialog.txtInformation.text=getString(R.string.cancel_alert_Buyer_side)

        //yes click
        //*****************set the api of requsted buying list*******************//
        dialog.yes.setOnClickListener {
            val data = intent.getParcelableExtra<FinalizeModel>("KeyBuyerDetails")!!

            mvmodel.CancelList(data.id, data.list_type)
            btnCancel.isClickable=false
            btnCancel.text=getString(R.string.pleasewait)
            dialog.dismiss()
        }
        dialog.no.setOnClickListener {
            dialog.dismiss()
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

                    txtPartilPayment.setText(
                        Constant.roundValue(it.credits.toDouble()) + getString(
                            R.string.credits
                        )
                    )

                    txtTotals.setText("$" + Constant.roundValue(it.amount.toDouble()))//set the total price


                    txtAddress.text = it.address
//                    val gson = Gson()
//                    val model1: ArrayList<AddAddressModel> =
//                        gson.fromJson(
//                            address,
//                            object : TypeToken<ArrayList<AddAddressModel>>() {}.type
//                        )
//                    for (item in model1) {
//                        txtAddress.text=item.address
//                    }

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
//********CancelOrder
        mvmodel.mSucessfulCancelList().observe(this, Observer {
            btnCancel.isClickable=true
            btnCancel.text=getString(R.string.cancel)
            finish()
        })
        mvmodel.mFailureCancelLIst().observe(this, Observer {
            btnCancel.isClickable=true
            btnCancel.text=getString(R.string.cancel)

        })
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
    private fun SettingUpToolbar() {
        val data = intent.getParcelableExtra<FinalizeModel>("KeyBuyerDetails")!!
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        whatsAppTextICons.visibility = View.VISIBLE
        text.text = data.seller_name
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
        val data = intent.getParcelableExtra<FinalizeModel>("KeyBuyerDetails")!!
        mvmodel.get_buyer_request_detail(data.id, data.list_type)
        progressBar2.visibility = View.VISIBLE
    }

}