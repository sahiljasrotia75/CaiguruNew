package com.example.caiguru.seller.sellerOrder.sellerToBeDelivered

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification.ResolutionActivity
import com.example.caiguru.databinding.ActivityToBeDeliveredBinding
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList.ToBeDeliveredOrderListActivity
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_to_be_delivered.*
import kotlinx.android.synthetic.main.nodata.*

class SellerToBeDeliveredActivity : AppCompatActivity(), SellerToBeDeliveredAdapter.setAdapteClick {

    private lateinit var mvmodel: SellerToBeDeliveredViewModel
    private lateinit var adapter: SellerToBeDeliveredAdapter
    private lateinit var text: TextView
    private lateinit var mbinding: ActivityToBeDeliveredBinding
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_to_be_delivered)
        //  mvmodel = ViewModelProviders.of(this)[SellerToBeDeliveredViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(SellerToBeDeliveredViewModel::class.java)
        SettingUpToolbar()
        Pagination()
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

        //****************api get_seller_requests*******************//
        //  mvmodel.getSellerRequest()

        //*************************get seller request observer******************//
        //sucessful
        mvmodel.mSucessfulPendingApproval().observe(this, Observer {
            mbinding.progresapproval.visibility = View.GONE
            mbinding.progresapprovalPagination.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
            } else {
                adapter.upDate(it)
            }
            isLoadingMoreItems = false
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            mbinding.progresapprovalPagination.visibility = View.GONE
            mbinding.progresapproval.visibility = View.GONE
            //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            txtNoData.visibility = View.VISIBLE
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.To_be_Delivered)
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
        // val manager = LinearLayoutManager(this)
        adapter = SellerToBeDeliveredAdapter(this)
        mbinding.recyclerldelivered.layoutManager = layoutpag
        mbinding.recyclerldelivered.adapter = adapter
    }

    override fun setClick(list: SellerApprovalModel) {
        if (list.req_status == "2") {
            val intent = Intent(this, ToBeDeliveredOrderListActivity::class.java)
            intent.putExtra("keyBuyerData", list)
            startActivity(intent)
            finish()
        } else if (list.req_status == "4") {
            val intent = Intent(this, ToBeDeliveredOrderListActivity::class.java)
            intent.putExtra("keyBuyerData", list)
            startActivity(intent)
            finish()
        } else if (list.req_status == "7") {
            val intent = Intent(this, ToBeDeliveredOrderListActivity::class.java)
            intent.putExtra("keyBuyerData", list)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, ResolutionActivity::class.java)
            intent.putExtra("keysellerDisputePending", list)
            startActivity(intent)
            finish()
        }


//        val intent = Intent(this, ToBeDeliveredOrderListActivity::class.java)
//        intent.putExtra("keyBuyerData", list)
//        startActivity(intent)
//        finish()
        //startActivityForResult(intent, 200)
    }

    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 200 && resultCode == RESULT_OK) {
//            finish()
//
//
//        }
//
//
//    }
    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getSellerRequest(page.toString())
        mbinding.progresapproval.visibility = View.VISIBLE

    }

    private fun Pagination() {
        //***********************pagination**********************//
        mbinding.recyclerldelivered.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progresapprovalPagination.visibility = View.VISIBLE
                page++
                mvmodel.getSellerRequest(page.toString())
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }
}
