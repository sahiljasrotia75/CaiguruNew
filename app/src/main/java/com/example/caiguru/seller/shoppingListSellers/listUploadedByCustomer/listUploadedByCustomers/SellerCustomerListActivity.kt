package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
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
import com.example.caiguru.commonScreens.selectCities.CitiesModel
import com.example.caiguru.commonScreens.selectCities.SelectCitiesActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.seller_customer_list.*

class SellerCustomerListActivity : AppCompatActivity() {
    lateinit var mViewModel: CustomerListViewModel
    private lateinit var text: TextView
    private lateinit var mAdapter: CustomerListParentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seller_customer_list)
     //   mViewModel = ViewModelProviders.of(this)[CustomerListViewModel::class.java]
        mViewModel = ViewModelProvider(this).get(CustomerListViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        //**************************** api list Uploaded by customer ***********************//
        mViewModel.listuploadedbycustomer()
        //sucessful case
        mViewModel.getList().observe(this, Observer {
            progressedBars.visibility = View.GONE
            progressPagination.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
                mAdapter.update(it)
            } else {
                txtNoData.visibility = View.INVISIBLE
                mAdapter.update(it)
            }
        })
        //failure
        mViewModel.getListFailure().observe(this, Observer {
            progressPagination.visibility = View.GONE
         //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
            txtNoData.visibility = View.VISIBLE
            progressedBars.visibility = View.GONE


        })
        val udata = getString(R.string.Change_City)
        val text = SpannableString(udata)
        text.setSpan(UnderlineSpan(), 0, udata.length, 0)
        listData.text = text

        //set the city
        //*******************by default city selected set************//

        listData.setOnClickListener {

          val list =  mViewModel.getSelectedCities()
            val intent = Intent(this, SelectCitiesActivity::class.java)
            intent.putExtra("cityKeyUploaded","yes")
            intent.putParcelableArrayListExtra("cities", list)
            startActivityForResult(intent, 1)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            val list = data!!.getParcelableArrayListExtra<CitiesModel>("cities")!!
            mViewModel.setCities(list)
            mViewModel.listuploadedbycustomer()
        }
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        rvCustomerList.layoutManager = manager
        mAdapter = CustomerListParentAdapter(this)
        rvCustomerList.adapter = mAdapter
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.customerlist)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        //inflate menu
        // toolbar.inflateMenu(R.menu.toolbar_main_menu);
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
        mViewModel.listuploadedbycustomer()
        progressPagination.visibility = View.VISIBLE
    }
}