package com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList

import android.annotation.SuppressLint
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
import com.example.caiguru.databinding.ActivitySellerCancelledOrderListBinding
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_cancelled_order_list.*
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.*
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.activeOrder
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.headingAddress
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.nestedScrollLayout
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.noData
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.progressedBar
import kotlinx.android.synthetic.main.activity_to_be_delivered_order_list.recyclerOrderList

class SellerCancelledOrderListActivity : AppCompatActivity() {

    private lateinit var mbinding: ActivitySellerCancelledOrderListBinding
    private lateinit var buyerName: String
    private lateinit var mvmodel: SellerCancelledOrderViewModel
    private lateinit var adapter: CancelledOrderParentAdapter
    private lateinit var text: TextView
    var id: String = ""
    var list_type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_seller_cancelled_order_list)
        mvmodel = ViewModelProviders.of(this)[SellerCancelledOrderViewModel::class.java]
        setCancelOrderData()
        if (intent.hasExtra("keySourceRejectedListByBuyer6")) {
            val data =
                intent.getParcelableExtra<NotificationModel>("keySourceRejectedListByBuyer6")!!
            buyerName = data.name
            id = data.source_id
            list_type = data.list_type
            //notification read api
            mvmodel.notificationRead(data.notification_id)
        } else if (intent.hasExtra("keySourceRejectedListByBuyer18")) {
            val data =
                intent.getParcelableExtra<NotificationModel>("keySourceRejectedListByBuyer18")!!
            buyerName = data.name
            id = data.source_id
            list_type = data.list_type
            //notification read api
            mvmodel.notificationRead(data.notification_id)
        } else if (intent.hasExtra("keySourceRejectedListByBuyer17")) {
            val data =
                intent.getParcelableExtra<NotificationModel>("keySourceRejectedListByBuyer17")!!
            buyerName = data.name
            id = data.source_id
            list_type = data.list_type
            //notification read api
            mvmodel.notificationRead(data.notification_id)
        } else if (intent.hasExtra("keyCancled")) {
            val data = intent.getParcelableExtra<SellerApprovalModel>("keyCancled")!!
            buyerName = data.buyer_name
            id = data.id
            list_type = data.list_type
        } else {
            id = intent.getStringExtra("source_id")!!
            list_type = intent.getStringExtra("list_type")!!
            //  val source = intent.getStringExtra("source")
            buyerName = intent.getStringExtra("name")!!
//            val image = intent.getStringExtra("image")
//            val level = intent.getStringExtra("level")
//            val listingname = intent.getStringExtra("listingname")
//            val reputation = intent.getStringExtra("reputation")
//            val created_at = intent.getStringExtra("created_at")
        }

        onlineOfflineList()
        SettingUpToolbar()
        setAdapter()

        //******************Api get_request_detail*******************//
        mvmodel.getrequestDetails(id, list_type)
        observerSet()
    }

    private fun setCancelOrderData() {
        if (intent.hasExtra("keySourceRejectedListByBuyer18")) {
            txtCancelOrder.text = getString(R.string.buyer_accepted_cancellation)
        }else if (intent.hasExtra("keySourceRejectedListByBuyer17")) {
            txtCancelOrder.text = getString(R.string.buyer_cancel_order)
        }
        else {
            txtCancelOrder.text = getString(R.string.buyer_rejected)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observerSet() {
        //********************observer of get_request_detail***********//
        //sucessful
        mvmodel.mSucessfulOrderList().observe(this, Observer {
            nestedScrollLayout.visibility = View.VISIBLE
            progressedBar.visibility = View.GONE
            adapter.updateData(it)//parent adapter
            for (item in it) {
                mbinding.txtAddressbuyer.text = item.address

                try {
                    mbinding.txtComissions.text =
                        Constant.roundValue(item.credits.toDouble()) + " " + getString(R.string.cr)

                    //set the cash on delivery
                    mbinding.txtCashOnDelivery.text =
                        Constant.roundValue(item.amount.toDouble() - item.credits.toDouble())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (item.delivery_type == "1") {
                    headingAddress.text =
                        getString(R.string.The_order_will_be_picked_up_at_your_address)
                } else {

                    headingAddress.text = getString(R.string.Delivery_Address)
                }
            }
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            noData.visibility = View.VISIBLE
            nestedScrollLayout.visibility = View.GONE
        })
    }

    private fun onlineOfflineList() {

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeOrder.background = resources.getDrawable(R.color.green)
            activeOrder.text = resources.getText(R.string.active)

        } else {
            activeOrder.background = resources.getDrawable(R.color.red)
            activeOrder.text = resources.getText(R.string.inactive)
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
        //  text.text = buyerName+getString(R.string.s)+ " " + getString(R.string.Order_List)
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
        adapter = CancelledOrderParentAdapter(this)
        recyclerOrderList.layoutManager = manager
        recyclerOrderList.adapter = adapter
    }
}
