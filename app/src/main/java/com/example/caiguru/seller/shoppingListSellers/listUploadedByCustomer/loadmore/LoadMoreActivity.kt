package com.example.caiguru.buyer.chooseSellers.loadmore

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.loadmore.LoadMoreViewModel
import com.example.caiguru.databinding.ActivityLoadMoreBinding
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_load_more.*
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.seller_customer_list.*

class LoadMoreActivity : AppCompatActivity() {

    private lateinit var mViewModel: LoadMoreViewModel
    lateinit var mbinding: ActivityLoadMoreBinding
    lateinit var adapter: LoadmoreAdapter
    lateinit var text: TextView
    private var mData = CustomerParentModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_load_more)
       // mViewModel = ViewModelProviders.of(this)[LoadMoreViewModel::class.java]
        mViewModel = ViewModelProvider(this).get(LoadMoreViewModel::class.java)
        mData = intent.getParcelableExtra<CustomerParentModel>("cat_id")!!
        mViewModel.loadmoreBuyerlist(mData.cat_id)
        progressBar.visibility = View.VISIBLE
        SettingUpToolbar()
        setAdapter()
        setObserver()
    }
    private fun setObserver() {
        //sucessful case
        mViewModel.getList().observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoDataLoadMore.visibility = View.VISIBLE
                adapter.update(it)
            } else {
                txtNoDataLoadMore.visibility = View.INVISIBLE
                adapter.update(it)
            }
        })
        //failure
        mViewModel.getListFailure().observe(this, Observer {
            progressPagination.visibility = View.GONE
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
            txtNoData.visibility = View.VISIBLE
            progressedBars.visibility = View.GONE
        })
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
    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.loadmore)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = LoadmoreAdapter(this)
        mbinding.rvLoad.layoutManager = manager
        mbinding.rvLoad.adapter = adapter
    }
}