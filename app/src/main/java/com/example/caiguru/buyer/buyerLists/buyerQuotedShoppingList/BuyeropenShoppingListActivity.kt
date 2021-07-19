package com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerShopListApproveRejectActivity
import com.example.caiguru.buyer.buyerLists.buyerShopListModify.BuyerShopListModifyViewModel
import com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment.BuyerShopModel
import com.example.caiguru.databinding.ActivityBuyeropenShoppingListBinding
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyeropen_shopping_list.*

class BuyeropenShoppingListActivity : AppCompatActivity(), openList {


    private var buyerShopModel = BuyerShopModel()
    private lateinit var text: TextView
    private lateinit var adapter: BuyerOpenShoppingListAdapter
    private var listName: String = ""
    private lateinit var mbinding: ActivityBuyeropenShoppingListBinding
    private lateinit var mvmodel: BuyerOpenshopListModifyViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_buyeropen_shopping_list)
        //    mvmodel = ViewModelProviders.of(this)[BuyerOpenshopListModifyViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerOpenshopListModifyViewModel::class.java)

        buyerShopModel = intent.getParcelableExtra<BuyerShopModel>("keyname")!!
        listName = buyerShopModel.listingname

        setAdapter()
        setObserver()
        SettingUpToolbar()
        //  mvmodel.list_all_request(buyerShopModel.id)
        Pagination()
    }

    private fun setAdapter() {
        // val manager = LinearLayoutManager(this)
        adapter = BuyerOpenShoppingListAdapter(this@BuyeropenShoppingListActivity)
        mbinding.recyclerChilds.layoutManager = layoutpag
        mbinding.recyclerChilds.adapter = adapter
    }


    private fun setObserver() {
        mvmodel.mSucessfulshopListData().observe(this, Observer {
            mbinding.progressBarPagination.visibility = View.GONE
            if (it.isEmpty()) {
                mbinding.noDatabuyerList.visibility = View.VISIBLE
                mbinding.progress.visibility = View.GONE
            } else {

                adapter.update(it)
                mbinding.progress.visibility = View.GONE
            }
            isLoadingMoreItems = false
        })

        mvmodel.mFailureShopList().observe(this, Observer {
            mbinding.noDatabuyerList.visibility = View.VISIBLE
            mbinding.progress.visibility = View.GONE
            mbinding.progressBarPagination.visibility = View.GONE
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = listName
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        //inflate menu
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

    override fun open(list: BuyerShopOpenModel) {
        val intent = Intent(this, BuyerShopListApproveRejectActivity::class.java)
        intent.putExtra("keymodel", list)
        intent.putExtra("fragmentmodel", intent.getParcelableExtra<BuyerShopModel>("keyname"))
        startActivityForResult(intent, 65)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 75) {
            val model = data!!.getParcelableExtra<BuyerShopModel>("fragmentmodel")
            val intent = Intent()
            // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("fragmentmodel", model)
            setResult(75, intent)
//            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.list_all_request(buyerShopModel.id, page.toString())
    }

    private fun Pagination() {
        //***********************pagination**********************//
        mbinding.recyclerChilds.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressBarPagination.visibility = View.VISIBLE
                page++
                //************get notification api
                mvmodel.list_all_request(buyerShopModel.id, page.toString())
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