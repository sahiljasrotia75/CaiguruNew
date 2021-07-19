package com.example.caiguru.buyer.buyerOrder.buyerCancelledOrder

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
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_cancelled.*
import kotlinx.android.synthetic.main.activity_buyer_cancelled.progresapproval
import kotlinx.android.synthetic.main.activity_buyer_cancelled.recyclerldelivered
import kotlinx.android.synthetic.main.activity_buyer_order_complete.*
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.toolbar.*

class BuyerCancelledActivity : AppCompatActivity() {
    private lateinit var adapter: BuyerCancelAdapter
    private lateinit var text: TextView
    private lateinit var mvmodel: BuyerCancelledViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_cancelled)
        mvmodel = ViewModelProvider(this).get(BuyerCancelledViewModel::class.java)
        SettingUpToolbar()
        Pagination()
        setAdapter()
        setObserver()
        setClick()
    }

    private fun setClick() {
        whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }
    }

    private fun setObserver() {

        mvmodel.mSucessfulFinalizeOrder().observe(this, Observer {
            progresapproval.visibility = View.GONE
            progresPAG.visibility = View.GONE
            if (it.size > 0) {
                recyclerldelivered.visibility = View.VISIBLE
                txtNoData.visibility = View.GONE
            } else {
                recyclerldelivered.visibility = View.GONE
                txtNoData.visibility = View.VISIBLE
            }

            adapter.upDate(it)

        })

        mvmodel.mFailure().observe(this, Observer {
            Constant.showToast(it, this)
            progresapproval.visibility = View.GONE
            progresPAG.visibility = View.GONE
            recyclerldelivered.visibility = View.GONE
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
        whatsAppTextICons.visibility = View.VISIBLE
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
        adapter = BuyerCancelAdapter(this)
        recyclerldelivered.layoutManager = layoutpag
        recyclerldelivered.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getDeliveredOrder(page)
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
                mvmodel.getDeliveredOrder(page)
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