package com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.databinding.ActivityToBeDeliveredOrderListBinding
import com.example.caiguru.seller.sellerOrder.orderCompleted.OrderCompletedActivity
import com.example.caiguru.seller.sellerOrder.orderRejected.OrderRejectedActivity
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.*
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.activeOrder
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.addressLayout
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.minutesAgoList
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.nestedScrollLayout
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.noData
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.progressedBar
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.recyclerOrderList
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.txtAddressbuyer
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.txtComissions
import kotlinx.android.synthetic.main.delivered_confirm_dialog.*
import kotlinx.android.synthetic.main.requested_list_custom_dialog.*
import kotlinx.android.synthetic.main.requested_list_custom_dialog.no
import kotlinx.android.synthetic.main.requested_list_custom_dialog.yes
import java.util.ArrayList

class ToBeDeliveredOrderListActivity : AppCompatActivity() {

    private var latitude: String = ""
    private var longitude: String = ""
    private var sellerDataModel = SellerApprovalModel()
    private lateinit var mbinding: ActivityToBeDeliveredOrderListBinding
    private var dataGlobal = ArrayList<OrderListModel>()
    private lateinit var adapter: ToBeDeliveredOrderListParentAdapter
    private lateinit var mvmodel: ToBeDeliveredOrderListViewModel
    private var buyerName: String = ""
    private lateinit var text: TextView
    private var reqID: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_to_be_delivered_order_list)
        mvmodel = ViewModelProviders.of(this)[ToBeDeliveredOrderListViewModel::class.java]

        if (intent.hasExtra("keyBuyerData")) {
            sellerDataModel = intent.getParcelableExtra<SellerApprovalModel>("keyBuyerData")!!
            buyerName = sellerDataModel.buyer_name
            reqID = sellerDataModel.id

            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${
                    Constant.timesAgoLogic(
                        sellerDataModel.created_at,
                        this
                    )
                }."
//************status 2=button show status4=waiting for response status6 =dispute
            if (sellerDataModel.req_status == "2") {
                mbinding.btnLayout.visibility = View.VISIBLE
                mbinding.WaitingText.visibility = View.VISIBLE
                WaitingText.text = getString(R.string.press_delivery_btn_text)
            }
            //cancelled order
            else  if (sellerDataModel.req_status == "7") {
                mbinding.btnLayout.visibility = View.INVISIBLE
                mbinding.minutesAgoList.visibility = View.INVISIBLE
                mbinding.WaitingText.visibility = View.VISIBLE
                WaitingText.text = getString(R.string.Waiting_from_response_from_buyer)
            }else {
                mbinding.btnLayout.visibility = View.INVISIBLE
                mbinding.WaitingText.visibility = View.VISIBLE
                WaitingText.text = getString(R.string.Waiting_from_response_from_buyer)
            }
            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${
                    Constant.timesAgoLogic(
                        sellerDataModel.created_at,
                        this
                    )
                }."

//*************notification data
        } else if (intent.hasExtra("keySourceAcceptedListByBuyer5")) {
            val data =
                intent.getParcelableExtra<NotificationModel>("keySourceAcceptedListByBuyer5")!!
            buyerName = data.name
            sellerDataModel.id = data.source_id
            reqID = data.source_id
            sellerDataModel.list_type = data.list_type
            //notification read api
            mvmodel.notificationRead(data.notification_id)
            minutesAgoList.text =

                "${getString(R.string.Purchased_Order)} ${
                    Constant.timesAgoLogic(
                        data.created_at,
                        this
                    )
                }."

            if (data.action_taken == "1") {
                mbinding.btnnext.visibility = View.VISIBLE
            } else {
                mbinding.btnnext.visibility = View.GONE
            }

        } else if (intent.hasExtra("keyBuyerDataCompleted")) {
            mbinding.btnLayout.visibility = View.GONE
            sellerDataModel =
                intent.getParcelableExtra<SellerApprovalModel>("keyBuyerDataCompleted")!!
            buyerName = sellerDataModel.buyer_name
            reqID = sellerDataModel.id
            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${
                    Constant.timesAgoLogic(
                        sellerDataModel.created_at,
                        this
                    )
                }."

        } else {
            mbinding.btnLayout.visibility = View.VISIBLE
            mbinding.WaitingText.visibility = View.VISIBLE
            WaitingText.text = getString(R.string.press_delivery_btn_text)
            sellerDataModel.id = intent.getStringExtra("source_id")!!
            reqID = intent.getStringExtra("source_id")!!
            sellerDataModel.list_type = intent.getStringExtra("list_type")!!
            //  val source = intent.getStringExtra("source")
            buyerName = intent.getStringExtra("name")!!
            sellerDataModel.buyer_name = buyerName
            //  val image = intent.getStringExtra("image")
            // val level = intent.getStringExtra("level")
            // val listingname = intent.getStringExtra("listingname")
            // val reputation = intent.getStringExtra("reputation")
            val created_at = intent.getStringExtra("created_at")

            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${
                    created_at?.let {
                        Constant.timesAgoLogic(
                            it,
                            this
                        )
                    }
                }."
        }

        //******************Api get_request_detail*******************//
        mvmodel.getrequestDetails(sellerDataModel)
        SettingUpToolbar()
        setAdapter()

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeOrder.background = resources.getDrawable(R.color.green)
            activeOrder.text = resources.getText(R.string.active)

        } else {
            activeOrder.background = resources.getDrawable(R.color.red)
            activeOrder.text = resources.getText(R.string.inactive)
        }

        //********************observer of get_request_detail***********//
        //sucessful
        mvmodel.mSucessfulOrderList().observe(this, Observer {
            try {
                dataGlobal = it
                nestedScrollLayout.visibility = View.VISIBLE
                progressedBar.visibility = View.GONE
                adapter.updateData(it)//parent adapter
                for (item in it) {
                    txtAddressbuyer.text = item.address
                    txtComissions.text =
                        Constant.roundValue(item.credits.toDouble()) + " " + getString(R.string.cr)
                    //set the cash on delivery
                    mbinding.txtCashOnDelivery.text =
                        Constant.roundValue(item.amount.toDouble() - item.credits.toDouble())
                    if (item.delivery_type == "1") {
                        mbinding.headingAddress.text =
                            getString(R.string.The_order_will_be_picked_up_at_your_address)
                    } else {

                        mbinding.headingAddress.text = getString(R.string.Delivery_Address)
                    }

                    longitude = item.long
                    latitude = item.lat
                }
            } catch
                (e: Exception) {
                e.printStackTrace()
            }
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            noData.visibility = View.VISIBLE
            nestedScrollLayout.visibility = View.GONE
        })
        //*******************set the api of ahange request**************//
        btnnext.setOnClickListener {
            setDeliveredConfirmDialog()
        }

        //**********************set the observer of change request
        mvmodel.mSucessfulRequestApprovals().observe(this, Observer {
            val intent = Intent(this, OrderCompletedActivity::class.java)
            btnWait.visibility = View.GONE
            btnnext.visibility = View.VISIBLE
            intent.putExtra("keyAllData", dataGlobal)
            intent.putExtra("keyName", sellerDataModel)
            startActivity(intent)
            finish()
        })

        mvmodel.mFailureRequestApprovals().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            btnWait.visibility = View.GONE
            btnnext.visibility = View.VISIBLE
        })
        setClicks()
    }

    private fun setClicks() {
        addressLayout.setOnClickListener {
            try {
                if (mbinding.txtAddressbuyer.text.isNotEmpty() && latitude.isNotEmpty()) {
                    val gmmIntentUri: Uri =
                        Uri.parse("google.navigation:q=" + latitude + "," + longitude)
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
        btnRejecteds.setOnClickListener {
            setAlertDialog()

        }
    }

    private fun setAlertDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.delivered_confirm_dialog)
        dialog.show()
        dialog.txtHeader.text=getString(R.string.are_you_sure)
        dialog.txtInformation.text=getString(R.string.cancel_alert)

        //yes click
        //*****************set the api of requsted buying list*******************//
        dialog.yes.setOnClickListener {
            val intent = Intent(this, OrderRejectedActivity::class.java)
            intent.putExtra("model", reqID)
            intent.putExtra("statusTobeDelivered", "7")
            intent.putExtra("hideBackButton", "Yes")
            startActivityForResult(intent, 100)
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }
        dialog.no.setOnClickListener {
            dialog.dismiss()
        }

    }

    // ********************delivered confirm popup**********************//
    private fun setDeliveredConfirmDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.delivered_confirm_dialog)
        dialog.show()

        //yes click
        //*****************set the api of requsted buying list*******************//
        dialog.yes.setOnClickListener {
            mvmodel.changeRequestStatus(sellerDataModel)
            btnWait.visibility = View.VISIBLE
            btnnext.visibility = View.GONE
            dialog.dismiss()
        }
        dialog.no.setOnClickListener {
            dialog.dismiss()
        }
    }

    //.........setuptool bar..............//
    @SuppressLint("SetTextI18n")
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = buyerName
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

    //listAdapter
    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = ToBeDeliveredOrderListParentAdapter(this)
        recyclerOrderList.layoutManager = manager
        recyclerOrderList.adapter = adapter
    }
}
