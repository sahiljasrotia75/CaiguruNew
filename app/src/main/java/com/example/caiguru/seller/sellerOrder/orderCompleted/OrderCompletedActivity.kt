package com.example.caiguru.seller.sellerOrder.orderCompleted

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityOrderCompletedBinding
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_order_completed.*
import java.lang.Exception

class OrderCompletedActivity : AppCompatActivity() {


    private lateinit var adapter: OrderCompletedAdapter
    private lateinit var text: TextView
    private lateinit var mbinding: ActivityOrderCompletedBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_order_completed)
        SettingUpToolbar()
        val model = intent.getParcelableArrayListExtra<OrderListModel>("keyAllData")!!
        val model1 = intent.getParcelableExtra<SellerApprovalModel>("keyName")!!
        setAdapter()
        mbinding.heading.text =
            getString(R.string.You_have_complete_the_order_for) + " " + model1.buyer_name

        for (item in model) {
            mbinding.addressSet.text = item.address
            mbinding.listname.text = item.listingname

            try {
                mbinding.comissionSet.text =
                    Constant.roundValue(item.credits.toDouble()) + " " + getString(R.string.credits)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (item.delivery_type == "1") {
                mbinding.headingAddress.text =
                    getString(R.string.The_order_will_be_picked_up_at_your_address)
            } else {
                mbinding.headingAddress.text = getString(R.string.Delivery_Address)
            }
        }
        adapter.update(model)
        //set the click on the button
        mbinding.btnnext.setOnClickListener {
            finish()
        }
    }
    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        if (intent.hasExtra("keyName")) {
            text.text = getText(R.string.order_completed)
        } else {
            text.text = getText(R.string.Shopping_list_posted)
        }


    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = OrderCompletedAdapter(this)
        recyclershopping.layoutManager = manager
        recyclershopping.adapter = adapter
    }


}
