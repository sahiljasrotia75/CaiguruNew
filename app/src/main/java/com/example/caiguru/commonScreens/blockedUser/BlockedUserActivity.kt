package com.example.caiguru.commonScreens.blockedUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import kotlinx.android.synthetic.main.activity_blocked_user.*
import kotlinx.android.synthetic.main.nodata.*

class BlockedUserActivity : AppCompatActivity(), BlockedUserAdapter.BlockedUserInterface {
    private lateinit var mvmodel: BlockedUserViewModel
    private lateinit var mAdapter: BlockedUserAdapter
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocked_user)
       // mvmodel = ViewModelProviders.of(this)[BlockedUserViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BlockedUserViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        Pagination()
        NODatadescriptionText.text=getText(R.string.no_block_user)
        //get all data block user
        mvmodel.mSucessfulgetAllDataBlockedUser().observe(this, Observer {
            progressPaginations.visibility = View.INVISIBLE
            progressPaginationBlocked.visibility = View.INVISIBLE
            if (it.size<=0){
                NoDataFollower.visibility = View.VISIBLE
                blockedRecycler.visibility = View.INVISIBLE
                totalBlockedUser.visibility = View.INVISIBLE
                blockeduser.visibility = View.INVISIBLE
            }else{
                blockedRecycler.visibility = View.VISIBLE
                totalBlockedUser.text = (it.size).toString()
                mAdapter.updateData(it)
                NoDataFollower.visibility = View.INVISIBLE
                totalBlockedUser.visibility = View.VISIBLE
                blockeduser.visibility = View.VISIBLE
            }
            isLoadingMoreItems = false
        })

        //faillure
        mvmodel.mFailureAllDataBlockedUser().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            progressPaginations.visibility = View.INVISIBLE
            NoDataFollower.visibility = View.VISIBLE
            blockedRecycler.visibility = View.INVISIBLE
            totalBlockedUser.visibility = View.INVISIBLE
            blockeduser.visibility = View.INVISIBLE
            progressPaginationBlocked.visibility = View.INVISIBLE

        })
        //*****************unblocked user
        //********blocked user sucessful
        mvmodel.getSucessfulBlockedUser().observe(this, Observer {
         //   progressPaginations.visibility = View.INVISIBLE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            page = 1
            mvmodel.getallBlockedSerData(page.toString())
        })
        //********blocked user failure
        mvmodel.errorgetBlockedUser().observe(this, Observer {
            progressPaginations.visibility = View.INVISIBLE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.blocked_user)
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
        blockedRecycler.layoutManager = layoutpag
        mAdapter = BlockedUserAdapter(this)
        blockedRecycler.adapter = mAdapter
    }

    override fun blockedUserClick(list: BlockedUserModel) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setCancelable(true)
        mDialog.setMessage(getString(R.string.user_block_text2))
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            mvmodel.blockedUser(list)
            progressPaginations.visibility = View.VISIBLE
            dialog.cancel()
        }
        mDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, which ->
            dialog.cancel()
        }
        mDialog.show()
    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getallBlockedSerData(page.toString())
        progressPaginations.visibility = View.VISIBLE
    }
    private fun Pagination() {
        //***********************pagination**********************//
        blockedRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPaginationBlocked.visibility = View.VISIBLE
                page++
                //************get notification api
                mvmodel.getallBlockedSerData(page.toString())
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

