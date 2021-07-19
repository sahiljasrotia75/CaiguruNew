package com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList

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
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_closed_list.*
import java.util.ArrayList

class  SellerClosedListActivity : AppCompatActivity(), ClosedListParentAdapter.openAgainInterface {

    private  var cat_id: String=""
    private lateinit var mvmodel: ClosedListViewModel
    private lateinit var text: TextView
    lateinit var closedListParentAdapter: ClosedListParentAdapter

    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_closed_list)
       // mvmodel = ViewModelProviders.of(this)[ClosedListViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(ClosedListViewModel::class.java)
        setAdapter()
        val model = ArrayList<ListParentModel>()
        val data = intent.getParcelableExtra<ListParentModel>("keymodel")!!
        data.isExpanded =  true
        cat_id = CategoryMatching(data.cat_id)
     //   CategoryMatching(data.cat_id)
        model.add(data)
        settingUpToolbar()

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeHome.background = resources.getDrawable(R.color.green)
            activeHome.text = resources.getText(R.string.active)

        } else {
            activeHome.background = resources.getDrawable(R.color.red)
            activeHome.text = resources.getText(R.string.inactive)
        }




        closedListParentAdapter.updateData(model)
        //      mvmodel.getSellerCloseList(page.toString())
//        progressPagination.visibility = View.VISIBLE
//        //****************************web service get seller closed list*****************//
//        //sucessful case
//        mvmodel.mSucessful().observe(this, Observer {
//            progressLayout.visibility = View.GONE
//
//
//            progressPagination.visibility = View.GONE
//            isLoadingMoreItems = false
//
//            //    isLoadingMoreItems = false
//            if (it.isEmpty()) {
//                txtNoData.visibility = View.VISIBLE
//
//            } else {
//                closedListParentAdapter.updateData(model)
//            }
//        })
//        //case of failure
//        mvmodel.mFailure().observe(this, Observer {
//            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//            progressLayout.visibility = View.GONE
//            txtNoData.visibility = View.VISIBLE
//
//        })

        //**************************************web services of open again list**************************//
        //sucessful case
        mvmodel.mSucessfulOpenAgainList().observe(this, Observer {
            progressLayout.visibility = View.GONE
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(  it,this)
          finish()

        })

        //case of failure
        mvmodel.mFailureOpenAgainList().observe(this, Observer {

        //    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(  it,this)
            progressLayout.visibility = View.GONE

        })
        //***********************pagination**********************//
        recycler.addOnScrollListener(object : CloseListPagination(layoutpag) {

            override fun loadMoreItems() {

                isLoadingMoreItems = true
             //   progressPagination.visibility = View.VISIBLE
          //      mvmodel.getSellerCloseList(page.toString())
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

    //***************** getting id *********************//
    fun CategoryMatching(id: String): String {
        val categoriesList = Constant.categoryData(this)
        for (category in categoriesList) {

            if (category.category_id == id) {

                return category.name
            }
        }
        return  getString(R.string.mix_category_product)
    }

    //close list parent adapter
    private fun setAdapter() {
        //  val manager = LinearLayoutManager(this)
        recycler.layoutManager = layoutpag
        closedListParentAdapter = ClosedListParentAdapter(this)
        recycler.adapter = closedListParentAdapter
    }

    //.........setuptool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = cat_id
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

    //**************************web service start of open again  list***************************//
    //click interface
    override fun openAgainList(
        mData: ListParentModel,
        position: Int
    ) {
        mvmodel.openAgainList(mData, position)// send the data
        progressLayout.visibility = View.VISIBLE
    }
}

