package com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeViewModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_search_seller_proucts.*
import kotlinx.android.synthetic.main.refute_reason_dialog.*


class SearchSellerProuctsActivity : AppCompatActivity(), FeedsSearchAdapter.setLikeInterface,
    SearchSellerProductAdapter.historyInterface {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mAdapterSearchFeeds: FeedsSearchAdapter
    private lateinit var mAdapter: SearchSellerProductAdapter
    private lateinit var mvmodel: SearchViewModel
    var sellerSelected: Boolean = true
    var productSelected: Boolean = false
    var search_type: String = "2"
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    private lateinit var dialog: Dialog
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_seller_proucts)
        mvmodel = ViewModelProvider(this).get(SearchViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        setHistoryAdapterData()//search history adapter
        setClick()
        setSearchAdapter2()
        getAddress()
        setObserVer()
        setPagination()
        initData()
        edtSearch.requestFocus()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

    }

    private fun initData() {
        if (Constant.getSearchData(this).size <= 0) {
            searchProductRecycler.visibility = View.GONE
            searchProductHistoryRecycler.visibility = View.GONE
            itemNoData.visibility = View.VISIBLE
        } else {
            searchProductRecycler.visibility = View.GONE
            itemNoData.visibility = View.GONE
            searchProductHistoryRecycler.visibility = View.VISIBLE
        }
    }

    private fun setPagination() {
        //***********************pagination**********************//
        searchProductRecycler.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                page++
                paginationProgress.visibility = View.VISIBLE
                mvmodel.getSearchData(edtSearch.text.toString(), getAddress(), search_type, page)

            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems

            }

        })
    }

    private fun setObserVer() {
        mvmodel.mSucessFulSearch().observe(this, Observer {
            searchProgress.visibility = View.GONE
            paginationProgress.visibility = View.GONE
            if (page == 1 && it.size > 0) {
                mAdapterSearchFeeds.update(
                    it,
                    getAddress().address,
                    getAddress().lat,
                    getAddress().long
                )
                isLoadingMoreItems = false

                if (edtSearch.text.toString().isNotEmpty()) {
                    storeSearchResultInSharedPref(edtSearch.text.toString())
                }
                searchProductRecycler.visibility = View.VISIBLE
                searchProductHistoryRecycler.visibility = View.GONE
                itemNoData.visibility = View.GONE
            } else {
                itemNoData.setText(getString(R.string.no_result_found))
                itemNoData.visibility = View.VISIBLE
                searchProductRecycler.visibility = View.GONE
                searchProductHistoryRecycler.visibility = View.GONE
            }

        })

        mvmodel.mFailureSearch().observe(this, Observer {
            searchProgress.visibility = View.GONE
            paginationProgress.visibility = View.GONE
            searchProductRecycler.visibility = View.GONE
            searchProductHistoryRecycler.visibility = View.GONE
            itemNoData.visibility = View.VISIBLE
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        //set the report observer
        mvmodel.mFailureReposrtlist().observe(this, Observer {
            Constant.showToast(it,this)
            dialog.dismiss()

        })
        mvmodel.mSucessfulREportList().observe(this, Observer {
            Constant.showToast(it,this)
            dialog.dismiss()

        })

    }

    private fun getAddress(): DeliveryZoneModel {
        val model = intent.getParcelableExtra<DeliveryZoneModel>("KeyAddressModel")!!
        if (model.lat.isEmpty()) {
            if (Constant.getProfileData(this).lat != "") {
                model.lat = Constant.getProfileData(this).lat
                model.long = Constant.getProfileData(this).long
                model.address =
                    Constant.getProfileData(this).full_address
            }
        }
        return model
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0)
    }

    private fun setClick() {
        checkBoxClick()
        //set the click on back
        searchBack.setOnClickListener {
            Constant.hideSoftKeyboard(it, this)
            finish()
        }
        //cancel click
        txtCancel.setOnClickListener {
            edtSearch.setText("")
            initData()
            mAdapter.updateData(Constant.getSearchData(this@SearchSellerProuctsActivity))
        }

        //***************search listner**********************//
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                if (s.isEmpty()) {
                    initData()
                    mAdapter.updateData(Constant.getSearchData(this@SearchSellerProuctsActivity))
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                if (s.isEmpty()) {
                    initData()
                    mAdapter.updateData(Constant.getSearchData(this@SearchSellerProuctsActivity))

                }
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isEmpty()) {
                    initData()
                    mAdapter.updateData(Constant.getSearchData(this@SearchSellerProuctsActivity))

                }

            }
        })

        //************done button listner
        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (edtSearch.text.toString().isNotEmpty()) {
                    page = 1
                    searchProgress.visibility = View.VISIBLE
                    searchProductRecycler.visibility = View.VISIBLE
                    searchProductHistoryRecycler.visibility = View.GONE
                    mvmodel.getSearchData(
                        edtSearch.text.toString(),
                        getAddress(),
                        search_type,
                        page
                    )

                } else {
                    searchProductRecycler.visibility = View.INVISIBLE
                    searchProgress.visibility = View.GONE
                    searchProductHistoryRecycler.visibility = View.VISIBLE
                    mAdapter.updateData(Constant.getSearchData(this@SearchSellerProuctsActivity))
                }

            }
            Constant.hideSoftKeyboard(v, this)
            false
        }
    }

    //put the saerch data  in array
    private fun storeSearchResultInSharedPref(searchText: String) {
        try {
            val arrayData = Constant.getSearchData(this)
            if (arrayData.size == 10) {
                arrayData.removeAt(arrayData.size - 1)
            }
            val model = SearchModel()
            model.searchName = searchText
            arrayData.add(0, model)

            val myArray = ArrayList<SearchModel>()
            myArray.clear()
            myArray.add(model)

            if (arrayData.size > 1) {
                for (item in arrayData) {
                    if (item.searchName != searchText) {
                        myArray.add(item)
                    }
                }
            }


            val gson = Gson()
            val json = gson.toJson(myArray)

            Constant.getPrefs(this).edit().putString(Constant.SearchNameKey, json).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // 1:Seller, 2:Product
    private fun checkBoxClick() {
        //seller select click
        SellerImageChecked.setOnClickListener {
            search_type = "2"
            if (sellerSelected == true) {
                sellerSelected = true
                SellerImageChecked.setImageDrawable(getDrawable(R.drawable.ic_radio_button_checked_black_24dp))
            } else {
                sellerSelected = true
                productSelected = false
                SellerImageChecked.setImageDrawable(getDrawable(R.drawable.ic_radio_button_checked_black_24dp))
                //productSelected
                ProductImageChecked.setImageDrawable(getDrawable(R.drawable.ic_radio_button_unchecked_white_24dp))
            }
            //set the api on click
            if (edtSearch.text.toString().trim().isNotEmpty()) {
                page = 1
                mvmodel.getSearchData(edtSearch.text.toString(), getAddress(), search_type, page)
                searchProgress.visibility = View.VISIBLE
            }

        }

        //*************product select click
        ProductImageChecked.setOnClickListener {
            search_type = "1"
            if (productSelected == true) {
                productSelected = true
                ProductImageChecked.setImageDrawable(getDrawable(R.drawable.ic_radio_button_checked_black_24dp))
            } else {
                productSelected = true
                sellerSelected = false
                SellerImageChecked.setImageDrawable(getDrawable(R.drawable.ic_radio_button_unchecked_white_24dp))
                //productSelected
                ProductImageChecked.setImageDrawable(getDrawable(R.drawable.ic_radio_button_checked_black_24dp))
            }
            //set the api on click
            if (edtSearch.text.toString().trim().isNotEmpty()) {
                page = 1
                mvmodel.getSearchData(edtSearch.text.toString(), getAddress(), search_type, page)
                searchProgress.visibility = View.VISIBLE
            }
        }
    }

    //****************history adapter
    fun setHistoryAdapterData() {
        val manager = LinearLayoutManager(this)
        searchProductHistoryRecycler.layoutManager = manager
        mAdapter = SearchSellerProductAdapter(this)
        searchProductHistoryRecycler.adapter = mAdapter
        mAdapter.updateData(Constant.getSearchData(this))

    }

    //***********search adapter
    private fun setSearchAdapter2() {
        // val manager = LinearLayoutManager(this)
        searchProductRecycler.layoutManager = layoutpag
        mAdapterSearchFeeds = FeedsSearchAdapter(this)
        searchProductRecycler.adapter = mAdapterSearchFeeds
    }


    override fun setLike(hasLiked: Boolean, model: HomeModel) {
        // 1: Like, 0: Dislike
        var Like: String = ""
        if (hasLiked) {
            Like = "1"
            homeViewModel.setLikeDisLike(Like, model.post_id, model.is_like)
        } else {
            Like = "0"
            homeViewModel.setLikeDisLike(Like, model.post_id, model.is_like)
        }
    }

    // *****************share kit open*************//
    override fun shareShoppingList(
        model: HomeModel,
        allComission: String
    ) {
        var allText = ""
        for (item in model.products) {
            if (allText.isEmpty()) {

                allText =
                    "${getString(R.string.namesShare)}: ${item.name}, ${getString(R.string.Unit)}: ${Constant.convertUnits(
                        this,
                        item.unit
                    )}, ${getString(
                        R.string.Price
                    )}: $${item.priceWithComission}"

            } else {
                allText =
                    allText + " \n" + "${getString(R.string.namesShare)}: ${item.name}, ${getString(
                        R.string.Unit
                    )}: ${Constant.convertUnits(
                        this,
                        item.unit
                    )}, ${getString(
                        R.string.Price
                    )}: $${item.priceWithComission}"
            }
        }

        val message =
            "${getString(R.string.share_List_text)}\n$allText\n${getString(R.string.ShareLinkText)}: ${Constant.getProfileData(
                this
            ).sharelink}"
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, message)
        startActivityForResult(Intent.createChooser(share, getString(R.string.Share)), 1)
    }

    //*************set the click on the history
    override fun historyClick(searchName: String) {
        edtSearch.setText(searchName)
        page = 1
        mvmodel.getSearchData(searchName, getAddress(), search_type, page)
        searchProgress.visibility = View.VISIBLE
    }

    //report list listner
    override fun reportListClick(model: HomeModel) {
        reportListClickDialog(model)

    }

    private fun reportListClickDialog(model: HomeModel) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setCancelable(true)
        mDialog.setMessage(getString(R.string.report_text3))
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            openReasonPopup(model)
            dialog.cancel()
        }
        mDialog.setNegativeButton(
            getString(R.string.cancel)
        ) { dialog, which ->
            dialog.cancel()
        }
        mDialog.show()

    }

    private fun openReasonPopup(data: HomeModel) {
         dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.refute_reason_dialog)
        dialog.edtReason.hint = getString(R.string.enter_reason)
        dialog.show()
        dialog.edtReason.filters = arrayOf<InputFilter>(
            HideEmoji(this)
        )

        dialog.SubmitBtn.setOnClickListener {
            if (dialog.edtReason.text.isEmpty()) {

                Constant.showToast(getString(R.string.enter_reason), this)

            } else {
                mvmodel.reportList(dialog.edtReason.text.toString().trim(), data)
                dialog.waitBtn.visibility = View.VISIBLE
                dialog.SubmitBtn.visibility = View.GONE
            }

        }

    }
}