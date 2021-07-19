package com.example.caiguru.seller.sellerOrder.sellerOrderCanelled

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_order_canelled.*
import kotlinx.android.synthetic.main.activity_seller_order_canelled.progresapproval
import kotlinx.android.synthetic.main.activity_seller_order_canelled.recyclerldelivered
import kotlinx.android.synthetic.main.nodata.*

class SellerOrderCancelledActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var adapter: SellerOrderCancelledAdapter
    private lateinit var mvmodel: SellerOrderCancelledViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_order_canelled)
      //  mvmodel = ViewModelProviders.of(this)[SellerOrderCancelledViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(SellerOrderCancelledViewModel::class.java)
        Pagination()
        SettingUpToolbar()
        setAdapter()

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            //    mBinding.switchView.visibility = View.VISIBLE
            activeOrders.background = resources.getDrawable(R.color.green)
            activeOrders.text = resources.getText(R.string.active)

        } else {
            // mBinding.switchView.visibility = View.VISIBLE
            activeOrders.background = resources.getDrawable(R.color.red)
            activeOrders.text = resources.getText(R.string.inactive)
        }

        //*************************get seller request observer******************//
        //sucessful
        mvmodel.mSucessfulPendingApproval().observe(this, Observer {
            progresapproval.visibility = View.GONE
            progresPAG.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
            } else {
                adapter.upDate(it)
            }
            isLoadingMoreItems = false
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            progresPAG.visibility = View.GONE
            progresapproval.visibility = View.GONE
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(  it,this)
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
        text.text = getText(R.string.Cancelled)
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
        //val manager = LinearLayoutManager(this)
        adapter = SellerOrderCancelledAdapter(this)
        recyclerldelivered.layoutManager = layoutpag
        recyclerldelivered.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        page=1
        mvmodel.getSellerRequest(page.toString())
        progresapproval.visibility = View.VISIBLE

    }
    private fun Pagination() {
        //***********************pagination**********************//
        recyclerldelivered.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progresPAG.visibility = View.VISIBLE
                page++
                //************get notification api
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

