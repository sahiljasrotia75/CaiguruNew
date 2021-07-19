package com.example.caiguru.buyer.buyerOrder.finalizeOrder

import android.content.Intent
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
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardBuyerActivity
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_finalize_order.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FinalizeOrderActivity : AppCompatActivity() {
    private lateinit var mvmodel: FinalizeOrderViewModel
    private lateinit var mFinalizeAdapter: FinalizeOrderAdapter
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_order)
        mvmodel = ViewModelProvider(this).get(FinalizeOrderViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        setObserver()
        Pagination()
        setClick()
    }


    private fun setClick() {
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, DashBoardBuyerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }
    }

    private fun setObserver() {

        mvmodel.mSucessfulFinalizeOrder().observe(this, Observer {
            progressPagination.visibility = View.GONE
            progressBarFinalize.visibility = View.GONE
            if (it.size > 0) {
                recyclerFinalize.visibility = View.VISIBLE
                noFeedLayout.visibility = View.GONE
            } else {
                recyclerFinalize.visibility = View.GONE
                noFeedLayout.visibility = View.VISIBLE
            }
            mFinalizeAdapter.upDate(it)

        })

        mvmodel.mFailure().observe(this, Observer {
            progressPagination.visibility = View.GONE
            progressBarFinalize.visibility = View.GONE
            recyclerFinalize.visibility = View.GONE
            noFeedLayout.visibility = View.VISIBLE
        })
    }

    private fun Pagination() {
        //***********************pagination**********************//
        recyclerFinalize.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                page++
                //************get Finalize order
                mvmodel.getFinalizeOrder(page)
                progressPagination.visibility = View.VISIBLE
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getFinalizeOrder(page)
        progressBarFinalize.visibility = View.VISIBLE

    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getString(R.string.finalize_orders)
        whatsAppTextICons.visibility = View.VISIBLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        whatsAppTextICons.visibility = View.VISIBLE
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

    // set adapter finalize
    private fun setAdapter() {
        mFinalizeAdapter = FinalizeOrderAdapter(this)
        recyclerFinalize.layoutManager = layoutpag
        recyclerFinalize.adapter = mFinalizeAdapter
    }
}