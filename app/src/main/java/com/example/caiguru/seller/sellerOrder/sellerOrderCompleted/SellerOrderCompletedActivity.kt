package com.example.caiguru.seller.sellerOrder.sellerOrderCompleted

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
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_order_canelled.progresapproval
import kotlinx.android.synthetic.main.activity_seller_order_canelled.recyclerldelivered
import kotlinx.android.synthetic.main.activity_seller_order_completed.*
import kotlinx.android.synthetic.main.activity_to_be_delivered.activeOrder
import kotlinx.android.synthetic.main.nodata.*

class SellerOrderCompletedActivity : AppCompatActivity() {

    private lateinit var adapter: SellerOrderCompletedAdapter
    private lateinit var text: TextView
    private lateinit var mvmodel: SellerOrderCompletedViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_order_completed)
        mvmodel = ViewModelProviders.of(this)[SellerOrderCompletedViewModel::class.java]
        Pagination()
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

        //****************api get_seller_requests*******************//
       // mvmodel.getSellerRequest(page.toString())

        //*************************get seller request observer******************//
        //sucessful
        mvmodel.mSucessfulPendingApproval().observe(this, Observer {
            progresapproval.visibility = View.GONE
            progresPagina.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
            } else {
                adapter.upDate(it)
            }
            isLoadingMoreItems = false
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {

            progresapproval.visibility = View.GONE
            progresPagina.visibility = View.GONE
        //    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
        text.text = getText(R.string.Completed)
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
        adapter = SellerOrderCompletedAdapter(this)
        recyclerldelivered.layoutManager = layoutpag
        recyclerldelivered.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getSellerRequest(page.toString())
        progresapproval.visibility = View.VISIBLE

    }

    private fun Pagination() {
        //***********************pagination**********************//
        recyclerldelivered.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progresPagina.visibility = View.VISIBLE
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