package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller.SearchSavedProduct

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller.SavedProductChildAdapter
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_search_saved_product.*
import kotlinx.android.synthetic.main.activity_search_saved_product.btnSavedProducts
import kotlinx.android.synthetic.main.activity_search_saved_product.edtSearch
import kotlinx.android.synthetic.main.activity_search_saved_product.itemNoData
import kotlinx.android.synthetic.main.activity_search_saved_product.progressBarSavedProdct
import kotlinx.android.synthetic.main.activity_search_seller_proucts.searchBack
import kotlinx.android.synthetic.main.activity_search_seller_proucts.txtCancel
import java.lang.Exception

class SearchSavedProductActivity : AppCompatActivity() {
    private lateinit var mSerachProductAdapter: SavedProductChildAdapter
    private lateinit var mvmodel: SearchSavedProductViewModel
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    val manager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_saved_product)
        mvmodel = ViewModelProvider(this).get(SearchSavedProductViewModel::class.java)
        setClick()
        setObserver()
        setAdapter()
     //   setPagination()
        edtSearch.requestFocus()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        itemNoData.visibility = View.VISIBLE
    }

    private fun setPagination() {
        //***********************pagination**********************//
        recyclerSearchSavedProduct.addOnScrollListener(object : CloseListPagination(manager) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                page++
                paginationProggress.visibility = View.VISIBLE
                val categoryData = intent.getParcelableExtra<SellerCategoryModel>("KeyCategory")
                mvmodel.getSearchData(
                    edtSearch.text.toString(), categoryData,
                    page
                )
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems

            }

        })
    }
    private fun setObserver() {

        mvmodel.mFailure().observe(this, Observer {
            Constant.showToast(it, this)
            recyclerSearchSavedProduct.visibility = View.GONE
            progressBarSavedProdct.visibility = View.GONE
            paginationProggress.visibility = View.GONE
            itemNoData.visibility = View.VISIBLE
        })
        mvmodel.mSucessfulSavedlist().observe(this, Observer {
            progressBarSavedProdct.visibility = View.GONE
            paginationProggress.visibility = View.GONE
            if (it.size > 0) {
                mSerachProductAdapter.updateData(it)
                recyclerSearchSavedProduct.visibility = View.VISIBLE
                itemNoData.visibility = View.GONE
                isLoadingMoreItems = false
            } else {
                recyclerSearchSavedProduct.visibility = View.GONE
                itemNoData.visibility = View.VISIBLE
            }
        })


    }
    //parentSavedPrdctRecycler adapter
    private fun setAdapter() {
        recyclerSearchSavedProduct.layoutManager = manager
        mSerachProductAdapter = SavedProductChildAdapter(this)
        recyclerSearchSavedProduct.adapter = mSerachProductAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0)
    }

    private fun setClick() {
        //set the click on back
        searchBack.setOnClickListener {
            Constant.hideSoftKeyboard(it, this)
            finish()
        }

        //cancel click
        txtCancel.setOnClickListener {
            edtSearch.setText("")

        }
        //***************search listner**********************//
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {

                    itemNoData.visibility = View.VISIBLE
                    recyclerSearchSavedProduct.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                if (s.isEmpty()) {

                    itemNoData.visibility = View.VISIBLE
                    recyclerSearchSavedProduct.visibility = View.GONE
                }
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isEmpty()) {

                    itemNoData.visibility = View.VISIBLE
                    recyclerSearchSavedProduct.visibility = View.GONE
                }

            }
        })

        //************done button listner
        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (edtSearch.text.toString().isNotEmpty()) {
                    val categoryData = intent.getParcelableExtra<SellerCategoryModel>("KeyCategory")
                    page = 1
                    progressBarSavedProdct.visibility = View.VISIBLE
                    recyclerSearchSavedProduct.visibility = View.VISIBLE
                    mvmodel.getSearchData(
                        edtSearch.text.toString(), categoryData,
                        page
                    )

                } else {
                    recyclerSearchSavedProduct.visibility = View.GONE
                    progressBarSavedProdct.visibility = View.GONE
                    itemNoData.visibility = View.VISIBLE
                }

            }
            Constant.hideSoftKeyboard(v, this)
            false
        }

//set the click on the btn
        btnSavedProducts.setOnClickListener {
            val myLocalProductArray =
                intent.getParcelableArrayListExtra<PostShoppingModel>("KeyProductArray")!!

            var isSavedProduct = false
            //  var isDuplicateProduct = false
            val SavedArray = mSerachProductAdapter.getAllArrayData()
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
    }
}