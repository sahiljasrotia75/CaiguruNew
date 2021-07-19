package com.example.caiguru.buyer.buyerProfile.buyerMyOrder

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
import com.example.caiguru.buyer.buyerProfile.buyerEditProfile.BuyerEditProfileViewModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_my_order.*
import kotlinx.android.synthetic.main.nodata.*

class BuyerMyOrderActivity : AppCompatActivity() {

    private lateinit var mvmodel: MyOrderViewModel
    private lateinit var adapter: BuyerMyOrderAdapter
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_my_order)
      //  mvmodel = ViewModelProviders.of(this)[MyOrderViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(MyOrderViewModel::class.java)
        NODatadescriptionText.text = getString(R.string.No_order_at_the_moment)
        SettingUpToolbar()
        setAdapter()
        Pagination()
        //******************my order api observer************
        //sucessful
        mvmodel.mSucessfulshopListData().observe(this, Observer {
            progressPagination.visibility = View.GONE
            progressPagination1.visibility = View.INVISIBLE
            if (it.isEmpty()) {
                myOrderRecycler.visibility = View.INVISIBLE
                myOrderNoData.visibility = View.VISIBLE
                myOrderRecycler.visibility = View.INVISIBLE

            } else {
                adapter.update(it)
                myOrderRecycler.visibility = View.VISIBLE
            }
            isLoadingMoreItems = false
        })

        //failure
        mvmodel.mFailureShopList().observe(this, Observer {
            progressPagination.visibility = View.GONE
            myOrderNoData.visibility = View.VISIBLE
            myOrderRecycler.visibility = View.INVISIBLE
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            progressPagination1.visibility = View.INVISIBLE
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.myorder)
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
        adapter = BuyerMyOrderAdapter(this)
        myOrderRecycler.layoutManager = layoutpag
        myOrderRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        //**************api of my order**************//
        page=1
        mvmodel.getMyOrder(page.toString())
        progressPagination.visibility = View.VISIBLE
    }
    private fun Pagination() {
        //***********************pagination**********************//
        myOrderRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPagination1.visibility = View.VISIBLE
                page++
                //************get notification api
                mvmodel.getMyOrder(page.toString())
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
