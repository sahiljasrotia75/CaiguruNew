package com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_closed_list.*
import kotlinx.android.synthetic.main.activity_seller_closed_list.activeHome
import kotlinx.android.synthetic.main.nodata.*

class CloseListActivity:AppCompatActivity() {

    private lateinit var mvmodel: ClosedViewModel
    private lateinit var text: TextView
    private lateinit var mAdapter: ClosedParentAdapter

    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_closed_list)
    //    mvmodel = ViewModelProviders.of(this)[ClosedViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(ClosedViewModel::class.java)
        settingUpToolbar()

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeHome.background = resources.getDrawable(R.color.green)
            activeHome.text = resources.getText(R.string.active)

        } else {
            activeHome.background = resources.getDrawable(R.color.red)
            activeHome.text = resources.getText(R.string.inactive)
        }

        mvmodel.getSellerCloseList(page.toString())
        progressPagination1.visibility = View.VISIBLE

        //sucessful case
        mvmodel.mSucessful().observe(this, Observer {
            progressLayout1.visibility = View.GONE
            progressPagination1.visibility = View.GONE
            isLoadingMoreItems = false
            //    isLoadingMoreItems = false
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE

            } else {
                mAdapter.updateData(it)
            }
        })
        //case of failure
        mvmodel.mFailure().observe(this, Observer {
           // Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(  it.message,this)
            progressLayout1.visibility = View.GONE
            txtNoData.visibility = View.VISIBLE

        })
    }
    //.........setuptool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Closed_List)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    //close list parent adapter
    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        rvClosedlistParent.layoutManager = manager
        mAdapter = ClosedParentAdapter(this)
        rvClosedlistParent.adapter = mAdapter
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

    override fun onResume() {
        super.onResume()

        mvmodel.getSellerCloseList(page.toString())
        progressLayout1.visibility = View.VISIBLE
        setAdapter()

    }
}