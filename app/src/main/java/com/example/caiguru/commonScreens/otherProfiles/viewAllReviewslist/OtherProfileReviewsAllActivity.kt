package com.example.caiguru.commonScreens.otherProfiles.viewAllReviewslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.homeSeller.GetProfileModel
import kotlinx.android.synthetic.main.activity_other_profile_reviews_all.*

class OtherProfileReviewsAllActivity : AppCompatActivity() {
    private lateinit var adapter: ViewAllLatestReviewsAdapter
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_profile_reviews_all)
        settingUpToolbar()
        setAdapter()
        initData()
    }

    private fun initData() {
        val dataModel = intent.getParcelableExtra<GetProfileModel>("keyViewAllData")
        if (dataModel!!.reviews.size > 0) {
            adapter.upDate(dataModel.reviews)
        }else{
            TxtNoDataSearch.visibility=View.VISIBLE
        }

    }

    //.........setuptool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Ratings)
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
        adapter = ViewAllLatestReviewsAdapter(this)
        ViewAllReviewsRecycler.layoutManager = manager
        ViewAllReviewsRecycler.adapter = adapter
    }
}
