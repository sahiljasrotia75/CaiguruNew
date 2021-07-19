package com.example.caiguru.seller.shoppingListSellers.loadmorecloseList

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
import com.example.caiguru.databinding.LoadMoreCloseListBinding
import com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails.ListModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.load_more_close_list.progressBar
import kotlinx.android.synthetic.main.load_more_close_list.txtNoDataLoadMore
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.seller_customer_list.*
import kotlinx.android.synthetic.main.seller_customer_list.progressPagination

class LoadMoreCloseListActivity : AppCompatActivity() {

    lateinit var mbinding: LoadMoreCloseListBinding
    lateinit var text: TextView
    lateinit var adapter: LoadmoreCloseListAdapter
    private var mData = ListModel()
    private lateinit var mViewModel: LoadMoreCloseListViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 0
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.load_more_close_list)
       // mViewModel = ViewModelProviders.of(this)[LoadMoreCloseListViewModel::class.java]
        mViewModel = ViewModelProvider(this).get(LoadMoreCloseListViewModel::class.java)
        mData = intent.getParcelableExtra<ListModel>("cat_id")!!
        SettingUpToolbar()
        setAdapter()
        setObserver()
        pagination()
    }

    private fun pagination() {
        //***********************pagination**********************//
        mbinding.rvLoad.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressBar.visibility = View.VISIBLE
                page++
                //**********api of load more
                mViewModel.getallcloselist(mData.cat_id, page)
            }

            override fun isLastPage(): Boolean {
                return islastpageData

            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems

            }

        })

    }

    private fun setObserver() {
        //sucessful case
        mViewModel.mSucessful().observe(this, Observer {
            progressBar.visibility = View.GONE
            isLoadingMoreItems = true
            if (it.isEmpty()) {
                txtNoDataLoadMore.visibility = View.VISIBLE
                adapter.update(it)
            } else {
                txtNoDataLoadMore.visibility = View.INVISIBLE
                adapter.update(it)
            }

        })
        //failure
        mViewModel.mFailure().observe(this, Observer {
            progressPagination.visibility = View.GONE
         //   Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast( it.message,this)
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
        //   val manager = LinearLayoutManager(this)
        adapter = LoadmoreCloseListAdapter(this)
        mbinding.rvLoad.layoutManager = layoutpag
        mbinding.rvLoad.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        //**********api of load more
        page = 0
        mViewModel.getallcloselist(mData.cat_id, page)
        progressBar.visibility = View.VISIBLE
    }
}