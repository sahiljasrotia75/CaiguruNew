package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller.SearchSavedProduct.SearchSavedProductActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_saved_product.*
import kotlinx.android.synthetic.main.activity_saved_product.itemNoData
import java.lang.Exception

class SavedProductActivity : AppCompatActivity() {
    private lateinit var mvmodel: SavedProductViewModel
    private lateinit var mSavedProductAdapter: SavedProductParentAdapter
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    val manager = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_product)
        mvmodel = ViewModelProvider(this).get(SavedProductViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        setClick()
        initData()
        // setPagination()
        setObserver()
    }

    private fun initData() {
        if (intent.hasExtra("KeyCategory")) {
            val categoryData = intent.getParcelableExtra<SellerCategoryModel>("KeyCategory")
            recyclerChild.visibility = View.GONE
            mvmodel.getSavedProductData(categoryData, page)
            progressBarSavedProdct.visibility = View.VISIBLE
            //txtList.text = categoryData.name
        }
    }

    private fun setPagination() {
        //***********************pagination**********************//
        recyclerChild.addOnScrollListener(object : CloseListPagination(manager) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                page++
                progressPaginationProdct.visibility = View.VISIBLE
                val categoryData = intent.getParcelableExtra<SellerCategoryModel>("KeyCategory")
                mvmodel.getSavedProductData(categoryData, page)
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems

            }

        })
    }

    private fun setClick() {
        btnSavedProducts.setOnClickListener {
            val myLocalProductArray =
                intent.getParcelableArrayListExtra<PostShoppingModel>("KeyProductArray")!!

            var isSavedProduct = false
            //  var isDuplicateProduct = false
            val SavedArray = mSavedProductAdapter.getChildArrayData()
            val localArray = ArrayList<PostShoppingModel>()
            val filterArray = ArrayList<PostShoppingModel>()

            for (saveditem in SavedArray) {
                if (saveditem.isProductSelected) {
                    isSavedProduct = true
                    //  isDuplicateProduct = true
                    saveditem.saved_product_id = saveditem.product_id
                    localArray.add(saveditem)
                }

            }
            try {


                if (myLocalProductArray.size > 0) {
                    for (saveditem in localArray) {
                        for (itemArray in myLocalProductArray) {
                            if (saveditem.product_id == itemArray.saved_product_id) {
                                saveditem.duplicateProduct = true
                                //localArray.remove(saveditem)
                            }
                        }

                    }
                }


                //set the filter array
                for (item in localArray) {
                    if (!item.duplicateProduct) {
                        item.status="1"
                        filterArray.add(item)
                    }
                }


                if (!isSavedProduct) {
                    Constant.showToast(getString(R.string.Please_tag), this)
                    return@setOnClickListener

                }
//            else if (!isDuplicateProduct) {
//                Constant.showToast(getString(R.string.already_saved_product), this)
//                return@setOnClickListener
//
//            }
                else {
                    val intent = Intent()
                    intent.putParcelableArrayListExtra("keySavedProductData", filterArray)
                    setResult(Activity.RESULT_OK, intent)//set the result
                    finish()
                }
            } catch (e: Exception) {
            }
        }

        //set the click on search
        imgSearch.setOnClickListener {
            val myLocalProductArray =
                intent.getParcelableArrayListExtra<PostShoppingModel>("KeyProductArray")

            val categoryData = intent.getParcelableExtra<SellerCategoryModel>("KeyCategory")

            val intent = Intent(this, SearchSavedProductActivity::class.java)
            intent.putExtra("KeyCategory", categoryData)
            intent.putParcelableArrayListExtra("KeyProductArray", myLocalProductArray)
            startActivityForResult(intent, 600)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 600) {
            val productSavedArray =
                data!!.getParcelableArrayListExtra<PostShoppingModel>("keySavedProductData")

            val intent = Intent()
            intent.putParcelableArrayListExtra("keySavedProductData", productSavedArray)
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }
    }

    private fun setObserver() {
        mvmodel.mSucessfulSavedlist().observe(this, Observer {

            progressPaginationProdct.visibility = View.GONE
            progressBarSavedProdct.visibility = View.GONE
            if (it.size > 0) {
                recyclerChild.visibility = View.VISIBLE
                mSavedProductAdapter.updateData(it)
                isLoadingMoreItems = false
                itemNoData.visibility = View.GONE

            } else {
                recyclerChild.visibility = View.GONE
                itemNoData.visibility = View.VISIBLE
            }

        })

        //failure
        mvmodel.mFailure().observe(this, Observer {
            Constant.showToast(it, this)
            progressBarSavedProdct.visibility = View.GONE
            recyclerChild.visibility = View.GONE
            itemNoData.visibility = View.VISIBLE
        })
    }

    //parentSavedPrdctRecycler adapter
    private fun setAdapter() {

        recyclerChild.layoutManager = manager
        mSavedProductAdapter = SavedProductParentAdapter(this)
        recyclerChild.adapter = mSavedProductAdapter
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.saved_products)
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


}