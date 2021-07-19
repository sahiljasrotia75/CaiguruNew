package com.example.caiguru.buyer.buyerProfile.followings

import android.app.Activity
import android.content.Intent
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
import com.example.caiguru.buyer.buyerProfile.follower.FollowUnfollowModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_following_buyer.*
import java.util.ArrayList

class FollowingBuyerActivity : AppCompatActivity(), FollowingBuyerAdapter.followUnfollowInterface {

    private var FollowerSize = ArrayList<FollowUnfollowModel>()
    private lateinit var mAdapter: FollowingBuyerAdapter
    private lateinit var mvmodel: FollowingBuyerViewModel
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_following_buyer)
       // mvmodel = ViewModelProviders.of(this)[FollowingBuyerViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(FollowingBuyerViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        Pagination()
        //get the data
        mvmodel.mSucessfulFollowing().observe(this, Observer {
            FollowerSize = it
            isLoadingMoreItems = false
            //txtFollowings.text = it.size.toString()
            txtFollowings.text = FollowingCount(it).toString()
            mAdapter.updateData(it)
            parentLayouts.visibility = View.VISIBLE
            progressPagination.visibility = View.GONE
            NODataFollower.visibility = View.GONE
            progressPagination22.visibility = View.GONE

        })
        mvmodel.mFailure().observe(this, Observer {
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            parentLayouts.visibility = View.GONE
            progressPagination.visibility = View.GONE
            NODataFollower.visibility = View.VISIBLE
            progressPagination22.visibility = View.GONE
        })
    }

    private fun FollowingCount(it: ArrayList<FollowUnfollowModel>): Int{
        var count=0
        for (item in it){
            if (item.isFollowed=="1"){

                count+=1
            }
        }
        return count
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Followings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                val Following = FollowerSize.size
                val intent = Intent()
                intent.putExtra("keyname", Following)
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAdapter() {
       // val manager = LinearLayoutManager(this)
        followingRecycler.layoutManager = layoutpag
        mAdapter = FollowingBuyerAdapter(this)
        followingRecycler.adapter = mAdapter
    }

    override fun followClick(
        position: Int,
        list: FollowUnfollowModel,
        followed: String
    ) {
        mvmodel.setFollowUnfollow(list, position, followed)
    }

    override fun unfollowClick(
        position: Int,
        list: FollowUnfollowModel,
        followed: String
    ) {
        mvmodel.setFollowUnfollow(list, position, followed)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val Following = FollowerSize.size
        val intent = Intent()
        intent.putExtra("keyname", Following)
        setResult(Activity.RESULT_OK, intent)//set the result
        finish()
    }

    override fun onResume() {
        super.onResume()
        page=1
        mvmodel.setFollowing(page)
    }
    private fun Pagination() {
        //***********************pagination**********************//
        followingRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPagination22.visibility = View.VISIBLE
                page++
                //************get notification api
                mvmodel.setFollowing(page)
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
