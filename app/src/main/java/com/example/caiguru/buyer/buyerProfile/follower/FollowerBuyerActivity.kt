package com.example.caiguru.buyer.buyerProfile.follower

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
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_follower.*
import kotlinx.android.synthetic.main.activity_follower.NoDataFollower
import kotlinx.android.synthetic.main.activity_follower.parentLayoutes
import kotlinx.android.synthetic.main.activity_follower.progressPaginations
import java.util.ArrayList

class FollowerBuyerActivity : AppCompatActivity(),FollowerBuyerAdapter.followUnfollowInterface {

    private var dataSize=ArrayList<FollowUnfollowModel>()
    private lateinit var mAdapter: FollowerBuyerAdapter
    private lateinit var mvmodel: FollowerBuyerViewModel
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follower)
      //  mvmodel = ViewModelProviders.of(this)[FollowerBuyerViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(FollowerBuyerViewModel::class.java)
        SettingUpToolbar()
        Pagination()
        //******************observer of set follower data*************//
     //   mvmodel.setFollower(page.toString())
        setAdapter()
        //get the data
        //******************observer of set follower data*************//
        mvmodel.mSucessfulFollower().observe(this, Observer {
            dataSize=it
            txtFollowers.text = it.size.toString()
            mAdapter.updateData(it)
            parentLayoutes.visibility = View.VISIBLE
            progressPaginations.visibility = View.GONE
            progressPaginations1.visibility = View.GONE
            NoDataFollower.visibility = View.GONE
            isLoadingMoreItems = false
        })
        //failllure
        mvmodel.mFailureFollower().observe(this, Observer {

         //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            parentLayoutes.visibility = View.VISIBLE
            progressPaginations.visibility = View.GONE
            NoDataFollower.visibility = View.GONE
            progressPaginations1.visibility = View.GONE
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Follwers)
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
      //  val manager = LinearLayoutManager(this)
        followerRecycler.layoutManager = layoutpag
        mAdapter = FollowerBuyerAdapter(this)
        followerRecycler.adapter = mAdapter
    }

    //*********************follow un follow service****************//
    override fun followClick(
        position: Int,
        list: FollowUnfollowModel,
        followed: String
    ) {
        mvmodel.setFollowUnfollow(list, position,followed)

    }

    override fun unfollowClick(
        position: Int,
        list: FollowUnfollowModel,
        followed: String
    ) {

        mvmodel.setFollowUnfollow(list, position,followed)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onResume() {
        super.onResume()
        page=1
        mvmodel.setFollower(page.toString())
    }

    private fun Pagination() {
        //***********************pagination**********************//
        followerRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPaginations1.visibility = View.VISIBLE
                page++
                //************get notification api
                mvmodel.setFollower(page.toString())
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
