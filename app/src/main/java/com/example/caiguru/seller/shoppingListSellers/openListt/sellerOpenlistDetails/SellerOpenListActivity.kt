package com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails.ListModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.OpenListActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_selleropen_list.*
import kotlinx.android.synthetic.main.nodata.*
import java.lang.Exception

class SellerOpenListActivity : AppCompatActivity(), OpenChildAdapter.childClickInterface {

    private lateinit var mvmodel: SellerOpenViewModel
    private lateinit var text: TextView
    private var savedData = ArrayList<ListModel>()
    private lateinit var mAdapter: OpenParentAdapter
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selleropen_list)
        mvmodel = ViewModelProviders.of(this)[SellerOpenViewModel::class.java]
        settingUpToolbar()
        setAdapter()

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeShop.background = resources.getDrawable(R.color.green)
            activeShop.text = resources.getText(R.string.active)

        } else {

            activeShop.background = resources.getDrawable(R.color.red)
            activeShop.text = resources.getText(R.string.inactive)
        }

        //sucessful case
        mvmodel.mSucessful().observe(this, Observer {
            progressPagination.visibility = View.GONE
            progress.visibility = View.GONE
            isLoadingMoreItems = false

            if (it.isEmpty()) {

                if (savedData.isEmpty()) {
                    txtNoData.visibility = View.VISIBLE
                } else {
                    txtNoData.visibility = View.GONE
                }

            } else {
                try {
                    if (savedData.size > 0) {
                        savedData.clear()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                savedData.addAll(it)
                mAdapter.updateData(it)
            }
        })
        //case of failure
        mvmodel.mFailure().observe(this, Observer {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast( it.message,this)
            progressPagination.visibility = View.GONE
            progress.visibility = View.GONE
            txtNoData.visibility = View.VISIBLE

        })

        //***********************pagination**********************//
        rvOpenListParent.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                //   progress.visibility = View.VISIBLE
                //   mvmodel.getSellerShoppingList(page.toString())
                page++
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })

    }


    //open list parent adapter
    private fun setAdapter() {
        // val manager = LinearLayoutManager(this)
        rvOpenListParent.layoutManager = layoutpag
        mAdapter = OpenParentAdapter(this)
        rvOpenListParent.adapter = mAdapter
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
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.open_list)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    override fun onResume() {
        super.onResume()
        //******************api of open list*******************//
        page = 1
        mvmodel.getSellerShoppingList(page.toString())
        progressPagination.visibility = View.VISIBLE
        setAdapter()
    }

    override fun childClick(mData: ListParentModel, position: Int) {
        val intent = Intent(this, OpenListActivity::class.java)
        intent.putExtra("keymodel", mData)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode==Activity.RESULT_OK) {
            if (data != null) {
                val position = data.getIntExtra("close", -1)
                if (position != -1) {
                    savedData.removeAt(position)
                }
            }
        }
    }
}