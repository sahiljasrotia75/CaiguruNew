package com.example.caiguru.buyer.chooseSellers.chooseSellerLoadMore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityChooseSellerLoadMoreBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel

class ChooseSellerLoadMoreActivity : AppCompatActivity() {

    private lateinit var adapter: ChooseSellerLoadmoreAdapter
    private lateinit var mbinding: ActivityChooseSellerLoadMoreBinding
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_seller_load_more)
        setAdapter()

        SettingUpToolbar()
        if (intent.hasExtra("keyLoadMoreChooseSeller")) {

            val search = intent.getStringExtra("keyLoadMoreChooseSellerSearch")!!
            val chooseSellerLoadMoreModel =
                intent.getParcelableArrayListExtra<CustomerChildModel>("keyLoadMoreChooseSeller")!!

            val address = intent.getParcelableExtra<DeliveryZoneModel>("keyLoadMoreAddress")!!
            chooseSellerLoadMoreModel.let { adapter.update(it, search, address) }
        }
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Customer_Lists)
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
        val manager = LinearLayoutManager(this)
        adapter = ChooseSellerLoadmoreAdapter(this)
        mbinding.LoadmoreAdapter.layoutManager = manager
        mbinding.LoadmoreAdapter.adapter = adapter
    }
}
