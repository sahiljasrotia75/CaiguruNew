package com.example.caiguru.buyer.homeBuyers.tagUser

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_tag_buyer_home.*
import java.util.ArrayList

class TagBuyerHomeActivity : AppCompatActivity(), TagUserAdapter.selectUnSelectInterface {


    private var tagPosition: Int = 0
    private var allDataGlobal = ArrayList<TagModel>()
    private lateinit var textData: TextView
    private var data = HomeModel()
    private lateinit var viewModel: TagViewModel
    private lateinit var adapterUserTag: TagUserAdapter
    private lateinit var adapterShoppingList: TagShoppingAdapter
    private lateinit var text: TextView
    var search: String = ""
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_buyer_home)
      //  viewModel = ViewModelProviders.of(this)[TagViewModel::class.java]
        viewModel = ViewModelProvider(this).get(TagViewModel::class.java)

        SettingUpToolbar()
        setShoppingListAdapter()
        setTagUserAdapterAdapter()
        data = intent.getParcelableExtra<HomeModel>("keyTag")!!
        val allComission= intent.getStringExtra("keyTagCommission")!!
        shopListNames.text = data.listingname
        setShoppingAdapter(data,allComission)
        textData = findViewById(R.id.NODatadescriptionText)
        textData.text = getString(R.string.no_follower)
        //****************get_tag_users api list************//

        viewModel.getTagUser(data.post_id, search, page)
        //set the observer of getTagUser
        getTagUserObserver()//first time api all data observer
        Pagination()//pagination
        SearchFollower()//search follower click
        cancleClick()//search cancle click
        tagUnTagObserver()//tag un Tag observer
    }

    //api status 1: Tagged, 2:Untagged
    override fun getSelectUnselectStatus(
        model: TagModel,
        action: String,
        position: Int
    ) {
        tagPosition = position
        lateinit var Tagstatus: String
        if (action.equals("Tag")) {
            Tagstatus = "1"
            viewModel.postTaggedUser(model.user_id, data.post_id, Tagstatus)//tag
        } else {
            //UnTag api
            Tagstatus = "2"
            viewModel.postTaggedUser(model.user_id, data.post_id, Tagstatus)//unTag
        }
    }

    fun tagUnTagObserver() {
        viewModel.mSucessfulPostTag().observe(this, Observer {
            allDataGlobal[tagPosition].hasTagged = false
            adapterUserTag.update(allDataGlobal)
        })
        //failure
        viewModel.mErrorPostTag().observe(this, Observer {
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
        })
    }

    private fun cancleClick() {
        txtcancle.setOnClickListener {
            searchFollow.setText("")
            viewModel.getTagUser(data.post_id, search, page)
            progressTag.visibility = View.VISIBLE

            val imm: InputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(searchFollow.getWindowToken(), 0)
        }
    }

    private fun SearchFollower() {
        //**********************set the click on the search button*********************//
        searchFollow.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search = s.toString()
                if (s!!.length > 0) {
                    viewModel.getTagUser(data.post_id, search, page)
                    progressTag.visibility = View.VISIBLE

                } else {
                    //by default open this when the text is not search
                    viewModel.getTagUser(data.post_id, search, page)
                    progressTag.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun Pagination() {
        //***********************pagination**********************//
        tagRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressTag.visibility = View.VISIBLE
                page++
                //************get notification api
                viewModel.getTagUser(data.post_id, search, page)
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }

    private fun getTagUserObserver() {
        viewModel.mSuccessfulgetTagUser().observe(this, Observer {
            progressTag.visibility = View.GONE
            if (it.isEmpty()) {
                NoDataTag.visibility = View.VISIBLE
                tagRecycler.visibility = View.GONE
            } else {
                allDataGlobal = it
                adapterUserTag.update(it)
                 isLoadingMoreItems = false
                NoDataTag.visibility = View.GONE
                tagRecycler.visibility = View.VISIBLE
            }
        })

        viewModel.mError().observe(this, Observer {
            progressTag.visibility = View.GONE
            NoDataTag.visibility = View.VISIBLE
            tagRecycler.visibility = View.GONE
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
        })
    }

    private fun setShoppingAdapter(
        data: HomeModel,
        allComission: String
    ) {
        // adapterShoppingList.update(data.products)
        if (data.products.size > 3) {
            val newList = ArrayList<DialogModel>()
            for (i in 0 until 3) {
                newList.add(data.products[i])
            }
            viewMore.visibility = View.VISIBLE
            adapterShoppingList.update(newList,allComission)
        } else {
            viewMore.visibility = View.GONE
            adapterShoppingList.update(data.products, allComission)
        }

        viewMore.setOnClickListener {
            adapterShoppingList.update(data.products, allComission)
            viewMore.visibility = View.GONE
        }

    }

    private fun setShoppingListAdapter() {
        val manager = LinearLayoutManager(this)
        adapterShoppingList = TagShoppingAdapter(this)
        rvShoppingList.layoutManager = manager
        rvShoppingList.adapter = adapterShoppingList
    }

    private fun setTagUserAdapterAdapter() {
        val manager = LinearLayoutManager(this)
        adapterUserTag = TagUserAdapter(this, this)
        tagRecycler.layoutManager = manager
        tagRecycler.adapter = adapterUserTag
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Tag)
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

    override fun onResume() {
        super.onResume()
        page = 1
        viewModel.getTagUser(data.post_id, search, page)
        progressTag.visibility = View.VISIBLE
    }


}
