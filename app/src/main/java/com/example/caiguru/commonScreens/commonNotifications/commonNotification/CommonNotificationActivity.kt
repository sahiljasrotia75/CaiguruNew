package com.example.caiguru.commonScreens.commonNotifications.commonNotification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import kotlinx.android.synthetic.main.activity_buyer_notification.*
import kotlinx.android.synthetic.main.activity_buyer_notification.progressPagination

class CommonNotificationActivity : AppCompatActivity() {

    private lateinit var textData: TextView
    private lateinit var viewModel: CommonNotificationViewModel
    private lateinit var text: TextView
    private lateinit var mAdapterNotification: CommonNotificationAdapter
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_notification)
        viewModel = ViewModelProviders.of(this)[CommonNotificationViewModel::class.java]

        SettingUpToolbar()
        setAdapterNotifications()
        textData=findViewById(R.id.NODatadescriptionText)
        textData.text=getString(R.string.no_notification)
     //api set in the on resume
        //***********notification observer***********//
        viewModel.mSucessfulNotification().observe(this, Observer {
            progressPagination.visibility = View.GONE

            if (it.isEmpty()) {
                progressPagination.visibility = View.GONE
                recyclerNotificationBuyer.visibility = View.GONE
                notificationNoData.visibility = View.VISIBLE
            } else {
                isLoadingMoreItems=false
                mAdapterNotification.update(it)
            }

        })
        //************failure observer************//
        viewModel.mFailure().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            progressPagination.visibility = View.GONE
            notificationNoData.visibility = View.VISIBLE
            recyclerNotificationBuyer.visibility = View.GONE

        })

        //***********************pagination**********************//
        recyclerNotificationBuyer.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPagination.visibility = View.VISIBLE
                page++
                //************get notification api
                viewModel.getNotification(page)
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems

            }

        })

    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Notifications)
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

    private fun setAdapterNotifications() {
        recyclerNotificationBuyer.layoutManager = layoutpag
        mAdapterNotification = CommonNotificationAdapter(this)
        recyclerNotificationBuyer.adapter = mAdapterNotification
    }

    override fun onResume() {
        super.onResume()
        //************get notification api
        page=1
        viewModel.getNotification(page)
        progressPagination.visibility = View.VISIBLE
    }
}
