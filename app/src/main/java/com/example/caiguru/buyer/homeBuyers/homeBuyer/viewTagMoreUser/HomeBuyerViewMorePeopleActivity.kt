package com.example.caiguru.buyer.homeBuyers.homeBuyer.viewTagMoreUser

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
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_view_more_people.*

class HomeBuyerViewMorePeopleActivity : AppCompatActivity() {
    private lateinit var viewmodel: ViewMoreViewModel
    private lateinit var adapterUserTag: ViewMoreAdapter
    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more_people)
      //  viewmodel = ViewModelProviders.of(this)[ViewMoreViewModel::class.java]
        viewmodel = ViewModelProvider(this).get(ViewMoreViewModel::class.java)
        SettingUpToolbar()
        setTagUserAdapterAdapter()

        val modelData = intent.getParcelableExtra<HomeModel>("keyHomeBuyerViewPeople")

        if (modelData != null) {
            viewmodel.get_shared_users(modelData.post_id, modelData.shared_by)
        }
        progressTag1.visibility = View.VISIBLE
        //***************set the observer of get_shared_users******************//
        viewmodel.mSuccessfulgetTagUser().observe(this, Observer {
            progressTag1.visibility = View.GONE
            if (it.isEmpty()) {
                NoDataTag1.visibility = View.VISIBLE
                ViewMoreRecycler.visibility = View.GONE
            } else {
                adapterUserTag.update(it)
            }

        })


        viewmodel.mError().observe(this, Observer {
            progressTag1.visibility = View.GONE
            NoDataTag1.visibility = View.VISIBLE
            ViewMoreRecycler.visibility = View.GONE
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)

        })
    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.user_tag)
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

    private fun setTagUserAdapterAdapter() {
        val manager = LinearLayoutManager(this)
        adapterUserTag = ViewMoreAdapter(this)
        ViewMoreRecycler.layoutManager = manager
        ViewMoreRecycler.adapter = adapterUserTag
    }
}
