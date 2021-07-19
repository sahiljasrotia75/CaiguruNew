package com.example.caiguru.seller.shoppingListSellers.openListt.openList

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails.SellerOpenListActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.SellerPostShoppingListActivity
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_open_list.*
import kotlin.collections.ArrayList

class OpenListActivity : AppCompatActivity(), OpenListParentAdapter.closeListInterface,
    OpenListChildAdapter.playPauseInterface {

    private var cat_id: String = ""
    private var updateData = ArrayList<ListParentModel>()

    private var adapterPosition: Int = 0
    lateinit var mViewModel: OpenViewModel
    private lateinit var text: TextView
    private lateinit var mAdapter: OpenListParentAdapter
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_list)
        mViewModel = ViewModelProviders.of(this)[OpenViewModel::class.java]
        setAdapter()
        val model = ArrayList<ListParentModel>()
        val data = intent.getParcelableExtra<ListParentModel>("keymodel")!!
        data.isExpanded = true
        cat_id = CategoryMatching(data.cat_id)
        model.add(data)

        SettingUpToolbar()

////******************api of open list*******************//
//        page = 1
//        mViewModel.getSellerShoppingList("1")
//        progressPagination.visibility = View.VISIBLE

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeShop.background = resources.getDrawable(R.color.green)
            activeShop.text = resources.getText(R.string.active)
        } else {
            activeShop.background = resources.getDrawable(R.color.red)
            activeShop.text = resources.getText(R.string.inactive)
        }

        mAdapter.update(model)

//        // Observer for Setting list
//        mViewModel.sendList().observe(this, Observer {
//            progressPagination.visibility = View.GONE
//            progress.visibility = View.GONE
//            isLoadingMoreItems = false
//
//            if (it.isEmpty()) {
//                txtNoData.visibility = View.VISIBLE
//            } else {
//                updateData = it
//                mAdapter.update(it)
//            }
//        })
//        //failure
//        mViewModel.sendListDataFailure().observe(this, Observer {
//            progressPagination.visibility = View.GONE
//            progress.visibility = View.GONE
//            txtNoData.visibility = View.VISIBLE
//            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
//
//        })

        //**********************web service observer of close list********************//
        //case of sucessfull
        mViewModel.mSucessfullcloseList().observe(this, Observer {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            progressPagination.visibility = View.GONE
            updateData.removeAt(adapterPosition)
            mAdapter.update(updateData)
            val intent = Intent(this, SellerOpenListActivity::class.java)
            intent.putExtra("close", adapterPosition)
            setResult(Activity.RESULT_OK, intent)
            finish()

        })
        //case of failure
        mViewModel.mFailureCloseList().observe(this, Observer {
            //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            progressPagination.visibility = View.GONE
        })

        mViewModel.mSucessfulPlayPause().observe(this, Observer {
            //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            progressPagination.visibility = View.GONE
        })

        //***********************pagination**********************//
        rvOpenList.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                // progress.visibility = View.VISIBLE
                //         mViewModel.getSellerShoppingList(page.toString())
                page++
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })

        firstTimeScreenOpen()

    }

    private fun firstTimeScreenOpen() {
        val isFirstTime = Constant.getPrefs(this).getString(Constant.firstTimeOpenScreen, "")
        if (isFirstTime!!.isNotEmpty()) {
            firstTimeLayout.visibility = View.GONE
        } else {
            val string1 = getString(R.string.you_can_hide_a_product_from_buyer_by_pressing_on)
            val string2 = getString(R.string.pause)
            val allText = string1 + " " + string2
            val spannable = SpannableString(allText)
            val boldSpan = StyleSpan(Typeface.BOLD)
            spannable.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.purple)),
                string1.length+1,
                string2.length+ string1.length+1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
//            spannable.setSpan(
//                ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
//                0,
//                string2.length,
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//            )

            spannable.setSpan(boldSpan,  string1.length+1,
                string2.length+ string1.length+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            txtalert.setText(spannable, TextView.BufferType.SPANNABLE)

            firstTimeLayout.visibility = View.VISIBLE
        }

        //set the click
        allLayoutClick.setOnClickListener {
            Constant.getPrefs(this).edit().putString(Constant.firstTimeOpenScreen, "true").apply()
            firstTimeLayout.visibility = View.GONE
        }
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = cat_id
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

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

    private fun setAdapter() {
        // val manager = LinearLayoutManager(this)
        rvOpenList.layoutManager = layoutpag
        mAdapter = OpenListParentAdapter(this)
        rvOpenList.adapter = mAdapter

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

    //************************close list web service*********************//
    override fun closeList(mData: ArrayList<ListParentModel>, position: Int) {
        adapterPosition = position
        updateData = mData
        mViewModel.closeList(mData, position)
        progressPagination.visibility = View.VISIBLE
    }

    override fun modifyList(data: ListParentModel, allProductArray: ArrayList<DialogModel>) {

        val intent = Intent(this, SellerPostShoppingListActivity::class.java)
        val gson = Gson()
        val json2 = gson.toJson(allProductArray)
        data.product_details=json2
        intent.putExtra("model", data)
       // intent.putParcelableArrayListExtra("modifyProductArray", allProductArray)
        intent.putExtra("prefillMOdify", "true")
        startActivity(intent)
        finish()

    }

    override fun copyList(data: ListParentModel, allProductArray: ArrayList<DialogModel>) {
        val intent = Intent(this, SellerPostShoppingListActivity::class.java)
        val gson = Gson()
        val json2 = gson.toJson(allProductArray)
        data.product_details=json2
        intent.putExtra("model", data)
     //   intent.putParcelableArrayListExtra("modifyProductArray", allProductArray)
        intent.putExtra("copyData", "true")
        startActivity(intent)
        finish()
    }

    override fun onResume() {
        super.onResume()
        //******************api of open list*******************//
        //   page = 1
        //   mViewModel.getSellerShoppingList(page.toString())
        //  progressPagination.visibility = View.VISIBLE

    }

    override fun PlayPauseClick(list: DialogModel) {
        progressPagination.visibility = View.VISIBLE
        mViewModel.SetPlayPauseApi(list)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val isFirstTime = Constant.getPrefs(this).getString(Constant.firstTimeOpenScreen, "")
        if (isFirstTime!!.isEmpty()) {
            firstTimeLayout.visibility = View.GONE
            Constant.getPrefs(this).edit().putString(Constant.firstTimeOpenScreen, "true").apply()
        }else{
            finish()
        }
    }
}
