package com.example.caiguru.buyer.homeBuyers.commentBuyers

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_comment_buyer.*


class CommentBuyerHomeActivity : AppCompatActivity() {
    private var data = HomeModel()
    private lateinit var textData: TextView
    private lateinit var viewModel: CommentViewModel
    private lateinit var adapterComment: CommentBuyerHomeAdapter
    private lateinit var adapter: ShoppingListHomeBuyerAdapter
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_buyer)
        viewModel = ViewModelProvider(this).get(CommentViewModel::class.java)
        SettingUpToolbar()
        setShoppingListAdapter()
        setCommentAdapter()
        PaginationFeedComment()
        data = intent.getParcelableExtra<HomeModel>("keyComment")!!
        val allComission= intent.getStringExtra("keyCommentCommission")
        setShoppingAdapter(data)
        textData = findViewById(R.id.NODatadescriptionText)
        shopListName.text = data.listingname
        textData.text = getString(R.string.no_Comment)
        getFeedObserver()//set the observer of get feed
        CommentOnPost(data)//set the click on the comment screen
    }

    private fun CommentOnPost(data: HomeModel) {
        sendButton.setOnClickListener {
            if (edtComment.text.isEmpty()) {
//                Toast.makeText(this, getString(R.string.Please_enter_comment), Toast.LENGTH_SHORT)
//                    .show()
                Constant.showToast( getString(R.string.Please_enter_comment),this)
            } else {
                val comment = edtComment.text.toString().trim()
                //**************set comment feed Api***********//
                viewModel.setFeedComment(data.post_id, comment)
//                //*****************set the get_feed_comments api**********//
//                viewModel.getCommentFeed(data.post_id, page)

                noDataComment.visibility = View.GONE
                edtComment.setText("")
                val imm: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0)
            }

        }

    }

    private fun getFeedObserver() {
        //sucessful
        viewModel.mSuccessfulgetFeedComment().observe(this, Observer {
            ProgressBarComment.visibility = View.GONE
            AllLayoutParent.visibility = View.VISIBLE
            if (it.isEmpty()) {
                noDataComment.visibility = View.VISIBLE
            } else {
                noDataComment.visibility = View.GONE
                adapterComment.update(it)
                isLoadingMoreItems=false

            }

        })
        //failure
        viewModel.mError().observe(this, Observer {
            noDataComment.visibility = View.VISIBLE
            ProgressBarComment.visibility = View.GONE
            AllLayoutParent.visibility = View.VISIBLE
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast( it,this)
        })
    }

    private fun setShoppingAdapter(
        data: HomeModel
    ) {

        if (data.products.size > 3) {
            val newList = ArrayList<DialogModel>()
            for (i in 0 until 3) {
                newList.add(data.products[i])
            }
            viewMore.visibility = View.VISIBLE
            adapter.update(newList)
        } else {
            adapter.update(data.products)
            viewMore.visibility = View.GONE
        }


        viewMore.setOnClickListener {
            adapter.update(data.products)
            viewMore.visibility = View.GONE
        }


    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Comments)
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

    private fun setShoppingListAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = ShoppingListHomeBuyerAdapter(this)
        ShoppingListRecycler.layoutManager = manager
        ShoppingListRecycler.adapter = adapter
    }

    private fun PaginationFeedComment() {
        //***********************pagination**********************//
        commentRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                ProgressBarComment.visibility = View.VISIBLE
                page++
                //*****************set the get_feed_comments api**********//
                viewModel.getCommentFeed(data.post_id, page)

            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }

    private fun setCommentAdapter() {
        // val manager = LinearLayoutManager(this)
        adapterComment = CommentBuyerHomeAdapter(this)
        commentRecycler.layoutManager = layoutpag
        commentRecycler.adapter = adapterComment
    }

    override fun onResume() {
        super.onResume()
        page = 1
        //*****************set the get_feed_comments api**********//
        viewModel.getCommentFeed(data.post_id, page)
        ProgressBarComment.visibility = View.VISIBLE
    }

}
