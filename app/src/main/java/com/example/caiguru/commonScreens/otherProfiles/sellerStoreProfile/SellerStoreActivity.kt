package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.finalizePurchaseOrder.FinalizePurchaseOrderActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.viewStoreProductFullScren.ViewStoreFullScreenActivity
import com.example.caiguru.commonScreens.otherProfiles.viewAllReviewslist.OtherProfileReviewsAllActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_store.*
import kotlinx.android.synthetic.main.filter_dialog_layout.*
import kotlinx.android.synthetic.main.requested_list_custom_dialog.*
import kotlinx.android.synthetic.main.store_parent_adapter.view.*
import kotlinx.android.synthetic.main.switch_dialog.*
import kotlinx.android.synthetic.main.switch_dialog.no
import kotlinx.android.synthetic.main.switch_dialog.yes
import org.json.JSONArray
import org.json.JSONObject


class SellerStoreActivity : AppCompatActivity(), StoreParentAdapter.setLoadMoreInterfec,
    StoreChildAdapter.setInterFaceProduct, CartAdapter.setProductAddInterface {
    private var bLetsFinish: Boolean = false
    private var loadMoreAdapterPosition: Int = -1
    private var storeApiData = GetProfileModel()
    private var localProductArray = ArrayList<PostShoppingModel>()
    private var localSearchArray = ArrayList<PostShoppingModel>()
    private lateinit var mSearchAdapter: StoreChildAdapter
    private var localMOdelData = FinalizeModel()
    private lateinit var mAdapterCart: CartAdapter

    //    private lateinit var text: TextView
    private lateinit var mAdapterStore: StoreParentAdapter
    private lateinit var mvmodel: SellerStoreViewModel
    var isdrgDop = true
    private lateinit var ReputationText: String
    private lateinit var fulltext: String
    private var follow_status: Int = 0
    private var hasFollow: Boolean = false
    var setPriceFilter = "1"
    var setAdapterView = "grid"
    private var partialComission: String = ""
    private var cashOnDelivery: String = ""
    private var totalPriced: Double = 0.0

    //    var islastpageData: Boolean = false
//    var isLoadingMoreItems: Boolean = false
    var page = 1

    //  val SearchLayoutLinearmanager = LinearLayoutManager(this)
    var searchUsedProds = ""

    // val SearchLayoutGridManager = GridLayoutManager(this, 2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_store)
        mvmodel = ViewModelProvider(this).get(SellerStoreViewModel::class.java)
        setPriceArrowUpDown()
        setClick()
        blockCaiguruUser()
        setDefaultStoreAdapter()
        cartAdapter()
        initData()
        SettingUpToolbar()
        setObserver()
        // setScrolling()


        appbar.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(
                appBarLayout: AppBarLayout?,
                state: State?
            ) {
                if (state == State.COLLAPSED) {
                    LayoutBottom.visibility = View.GONE
                    setScrolling()
                } else if (state == State.EXPANDED) {
                    LayoutBottom.visibility = View.VISIBLE
                    setScrolling()
                }
            }
        })

    }


    private fun prefilCartData() {
        if (intent.hasExtra("KeyOpenSellerPrefillData")) {
            mvmodel.get_seller_cart(localMOdelData.seller_id)
        }
    }

    private fun setScrolling() {

        recyclerStore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && LayoutBottom.getVisibility() == View.VISIBLE) {
                    LayoutBottom.visibility = View.GONE
                } else if (dy < 0 && LayoutBottom.getVisibility() != View.VISIBLE) {
                    LayoutBottom.visibility = View.VISIBLE
                }
            }
        })

        recyclerSearchAdapter.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && LayoutBottom.getVisibility() == View.VISIBLE) {
                    LayoutBottom.visibility = View.GONE
                } else if (dy < 0 && LayoutBottom.getVisibility() != View.VISIBLE) {
                    LayoutBottom.visibility = View.VISIBLE
                }
            }
        })
//        recyclerStore.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//                when (p1!!.getAction()) {
//                    MotionEvent.ACTION_SCROLL, MotionEvent.ACTION_MOVE -> {
//                        LayoutBottom.visibility = View.INVISIBLE
//                    }
//                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
//                        LayoutBottom.visibility = View.VISIBLE
//                    }
//                }
//                return false
//            }
//        })
    }


    private fun initData() {
        if (intent.hasExtra("keyOpenSellerStore")) {
            localMOdelData = intent.getParcelableExtra("keyOpenSellerStore")!!
            if (localMOdelData.lat.isEmpty()) {
                localMOdelData.lat = Constant.getProfileData(this).lat
                localMOdelData.long = Constant.getProfileData(this).long
            }
            mvmodel.getsellerstore(localMOdelData, setPriceFilter)
            progressPagination.visibility = View.VISIBLE

        }
    }

    fun setObserver() {

        //**************store default observer
        mvmodel.getStoreDataSucessful().observe(this, Observer {
            if (localProductArray.size > 0) {
                for (observerArray in it.products) {
                    for (child in observerArray.products) {
                        for (item in localProductArray) {
                            if (item.id == child.id) {
                                child.total = item.total
                                child.qty = item.qty

                            }
                        }
                    }

                }
            }

            progressPagination.visibility = View.GONE
            storeApiData = it
            storeApiData.listInfo = it.listInfo
            storeApiData.listInfo.delivery_daytime = it.listInfo.delivery_daytime
            storeApiData.listInfo.pickup_details = it.listInfo.pickup_details
            storeApiData.listInfo.pickup_details.days = it.listInfo.pickup_details.days
            storeApiData.products = it.products
            if (it.products.size > 0) {
                it.products[0].isExpanded = true
                mAdapterStore.updateData(it.products)
                prefilCartData()
                noDataLayout.visibility = View.GONE
                mainLayout.visibility = View.VISIBLE
                LayoutBottom.visibility = View.VISIBLE
                TxtNoDataSearch.visibility = View.GONE
                //parentLayoutAll.visibility = View.VISIBLE
            } else {
                noDataLayout.visibility = View.VISIBLE
                mainLayout.visibility = View.GONE
                LayoutBottom.visibility = View.GONE
            }
            reputationTextSet(it)
            LevelTextSet(it)// set level text
            completedOrderTextSet(it)
            userProfileSet(it)
            userLevelImageBadgeSet(it)
            followOrUnfolowCheck(it)
            checkMyProfileOrNot()
            setClickReview(it)
            SetAllProductData()
            setbtnHideShowCheck(it)
            if (it.is_purchase_button_show == "0") {
                purchase.visibility = View.GONE
            }
        })

        mvmodel.mFailureStoredata().observe(this, Observer {
            TxtNoDataSearch.visibility = View.GONE
            Constant.showToast(it, this)
            progressPagination.visibility = View.GONE
            noDataLayout.visibility = View.VISIBLE
            mainLayout.visibility = View.GONE
            //parentLayoutAll.visibility = View.GONE
        })

        //*********************observer of follow or unfollow**************//
        mvmodel.mSucessfullFollowUnfollow().observe(this, Observer {

            if (it.follow_status == "1") {
                btnFollow.visibility = View.GONE
                btnunfollow.visibility = View.VISIBLE
                loader.visibility = View.GONE
            } else {
                btnFollow.visibility = View.VISIBLE
                loader.visibility = View.GONE
                btnunfollow.visibility = View.GONE
            }
        })
//failure
        mvmodel.mFailureeFollowUnfollow().observe(this, Observer {
            Constant.showToast(it, this)
            loader.visibility = View.GONE
        })

        //********blocked user sucessful
        mvmodel.getSucessfulBlockedUser().observe(this, Observer {
            progressPagination.visibility = View.INVISIBLE
            Constant.showToast(it, this)
            Constant.apiHitOrNot = 0
            finish()
        })
        //********blocked user failure
        mvmodel.errorgetBlockedUser().observe(this, Observer {
            progressPagination.visibility = View.INVISIBLE
            Constant.showToast(it, this)
        })


//*************search observer
        mvmodel.mScucessfulSearchProdcut().observe(this, Observer {
            searchUsedProds = it[0].usedProds

//            if (it[0].count > 4 && it[0].count != it.size) {
//                txtLoadMore.visibility = View.VISIBLE
//            } else {
//                txtLoadMore.visibility = View.GONE
//            }



            progressPagination.visibility = View.INVISIBLE
            if (it.size > 0) {
                localSearchArray.clear()
                localSearchArray.addAll(it)
                //set the qty and price prefill
                if (localProductArray.size > 0) {
                    for (observerArray in it) {
                        for (item in localProductArray) {
                            if (item.id == observerArray.id) {
                                observerArray.total = item.total
                                observerArray.qty = item.qty

                            }
                        }
                    }
                }
                mSearchAdapter.updateData(it)
                TxtNoDataSearch.visibility = View.GONE
                //isLoadingMoreItems = false
                // parentLayoutAll.visibility = View.VISIBLE
            } else {
                TxtNoDataSearch.visibility = View.VISIBLE
                // parentLayoutAll.visibility = View.GONE
            }

        })
        //***********error
        mvmodel.getErrorSearch().observe(this, Observer {
            TxtNoDataSearch.visibility = View.VISIBLE
            progressPagination.visibility = View.INVISIBLE
            Constant.showToast(it, this)

        })

        //get the Load More Api
        mvmodel.mSucessfulLoadMore().observe(this, Observer {
            //set the prefill qty
            if (localProductArray.size > 0) {
                for (observerArray in it.products) {
                    for (item in localProductArray) {
                        if (item.id == observerArray.id) {
                            observerArray.total = item.total
                            observerArray.qty = item.qty

                        }
                    }
                }
            }

            progressPagination.visibility = View.INVISIBLE
            // storeApiData.products[loadMoreAdapterPosition].page+=1
            //  Log.e("arraySize", it.size.toString())
            storeApiData.products[loadMoreAdapterPosition].usedProds = it.usedProds
            storeApiData.products[loadMoreAdapterPosition].products.addAll(it.products)
            mAdapterStore.updateData(storeApiData.products)
        })

        mvmodel.mFailureLoadMore().observe(this, Observer {
            progressPagination.visibility = View.INVISIBLE
            Constant.showToast(it, this)

        })
        //set the observer Saved Cart
        mvmodel.mSucessfulSavedCart().observe(this, Observer {
            showLoader.visibility = View.GONE
            alertDialog1(it)
        })

        mvmodel.mFailureSavedCart().observe(this, Observer {
            showLoader.visibility = View.GONE
            Constant.showToast(it, this)
        })

        //*************cart prefillData*************//
        mvmodel.mSucessfulCartData().observe(this, Observer {
            if (it.size > 0) {
                Constant.getPrefs(application).edit().putString(Constant.is_has_cart, "1").apply()
            }
            upDateAdapter(it)
            setViewStoreFullScreenData(it)
        })


    }

    fun setbtnHideShowCheck(it: GetProfileModel) {
        if (it.is_purchase_button_show == "1") {
            purchase.visibility = View.VISIBLE
            if (it.is_purchase_disable == "0") {
                purchase.text = getString(R.string.Purchase)
                purchase.setBackgroundDrawable(getDrawable(R.drawable.rectangle_curve_loginbutton))
                purchase.isClickable = true
            } else {
                purchase.setBackgroundDrawable(getDrawable(R.drawable.rectangle_curve_loginbuttonprocessing))
                purchase.text = getString(R.string.Done)
                purchase.isClickable = false
            }

        } else {
            purchase.visibility = View.GONE
        }

    }


    private fun setDefaultStoreAdapter() {
        recyclerSearchAdapter.visibility = View.GONE
        //txtLoadMore.visibility = View.GONE
        recyclerStore.visibility = View.VISIBLE
        val manager = LinearLayoutManager(this)
        recyclerStore.layoutManager = manager
        mAdapterStore = StoreParentAdapter(this)
        recyclerStore.adapter = mAdapterStore
        mAdapterStore.setAdapterViews(setAdapterView)
    }

    fun cartAdapter() {
        val manager2 = LinearLayoutManager(this)
        reyclerTotalproduct.layoutManager = manager2
        mAdapterCart = CartAdapter(this)
        reyclerTotalproduct.adapter = mAdapterCart
    }


    private fun searchAdapterLayout() {
        recyclerSearchAdapter.visibility = View.VISIBLE
        recyclerStore.visibility = View.GONE
        if (setAdapterView == "grid") {

            recyclerSearchAdapter.layoutManager = GridLayoutManager(this, 2)
            mSearchAdapter =
                StoreChildAdapter(this)
            recyclerSearchAdapter.adapter = mSearchAdapter

        } else {
            // val manager2 = LinearLayoutManager(this)
            recyclerSearchAdapter.layoutManager = LinearLayoutManager(this)
            mSearchAdapter =
                StoreChildAdapter(this)
            recyclerSearchAdapter.adapter = mSearchAdapter
        }
    }


    private fun setPriceArrowUpDown() {
        val arrowUp = getResources().getDrawable(R.drawable.price_arrow_up_24)
        val arrowDown = getResources().getDrawable(R.drawable.price_arrow_down)

        if (setPriceFilter == "1") {
            Glide.with(this)
                .load(arrowUp)
                .into(imgPriceArrow)
        } else {
            Glide.with(this)
                .load(arrowDown)
                .into(imgPriceArrow)
        }

    }

    private fun setClick() {
//set the click on the drag and drop
        cardDrgDropLayout.setOnClickListener {
            if (isdrgDop) {
                isdrgDop = false
                Glide.with(this)
                    .load(R.drawable.ic_down_arrow_cart)
                    .into(imgDrgDropView)

                reyclerTotalproduct.visibility = View.VISIBLE
                txtClearCart.visibility = View.VISIBLE

            } else {
                Glide.with(this)
                    .load(R.drawable.ic_arrow_view_up)
                    .into(imgDrgDropView)
                reyclerTotalproduct.visibility = View.GONE
                txtClearCart.visibility = View.GONE
                isdrgDop = true
            }

        }
        //set the click on the filter Layout set the price high to or low to high
        priceFilterLayout.setOnClickListener {
            setFilterDialog()
        }
        gridClick()
        searchClick()
        followUnfollowClick()
        setInfoIconClick()
        setClickClearCart()
        setClickPurchase()
        //setPagination()
        searchCrossClick()
       // searchLoadMoreClick()

    }

    //    private fun searchLoadMoreClick() {
//
//        txtLoadMore.setOnClickListener {
//           page= page+1
//            mvmodel.getSearchData(
//                edtSearch.text.toString(), localMOdelData,
//                page,
//                setPriceFilter, searchUsedProds
//            )
//            txtLoadMore.text=getString(R.string.pleasewait)
//            txtLoadMore.isClickable=false
//            progressPagination.visibility = View.VISIBLE
//        }
//    }
    override fun loadMoreSearchClick() {
           page= page+1
            mvmodel.getSearchData(
                edtSearch.text.toString(), localMOdelData,
                page,
                setPriceFilter, searchUsedProds
            )
            progressPagination.visibility = View.VISIBLE

    }

    private fun searchCrossClick() {
        imgSearchCross.setOnClickListener {
            if (edtSearch.text.isNotEmpty()) {
                edtSearch.setText("")
                searchUsedProds = ""
                page = 1
                setDefaultStoreAdapter()
                initData()
            }
        }
    }

    private fun setClickPurchase() {
        //***********************purchase click for single list***************//
        purchase.setOnClickListener {
            if (totalPriced <= 0) {
                Constant.showToast(getString(R.string.Please_Select_Quantity), this)
                return@setOnClickListener
            } else if (totalPriced < storeApiData.listInfo.minimum_purchase_amount.toDouble()) {
                Constant.showToast(
                    getString(R.string.Total_should_be_high_than_Minimum_order_amount),
                    this
                )
                return@setOnClickListener
            } else {
                val dialog = Dialog(this)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.switch_dialog)
                //default textShow
                dialog.txtHeader.text = getString(R.string.alert)
                dialog.txtMessage.text = getString(R.string.purchase_shopping_alert)
                dialog.yes.text = getString(R.string.buy)
                dialog.no.text = getString(R.string.cancel)
                dialog.show()
                dialog.yes.setOnClickListener {
                    val intent = Intent(this, FinalizePurchaseOrderActivity::class.java)
                    intent.putExtra("KeyProfileInfoData", storeApiData)
                    intent.putExtra("keySellerId", localMOdelData)
                    intent.putExtra("KeyProductJsonFormat", getJsonProductArray())
                    intent.putExtra("keyPartialComissionCredits", partialComission)
                    intent.putExtra("KeyCashOnDelivery", cashOnDelivery)
                    startActivityForResult(intent, 1001)

                    dialog.dismiss()
                }
                //no click
                dialog.no.setOnClickListener {
                    dialog.dismiss()
                }
            }

        }


    }

    private fun setClickClearCart() {
        txtClearCart.setOnClickListener {
            localProductArray = ArrayList<PostShoppingModel>()
            //  val model = ShoppingListModel()
            localProductArray.clear()
            partialComission = ""
            cashOnDelivery = ""
            totalPriced = 0.0
            SetAllProductData()
            mAdapterCart.update(localProductArray)

            if (edtSearch.text.isNotEmpty()) {
                mSearchAdapter.clearCart()
            } else {
                mAdapterStore.clearCart()
            }
        }
    }


    private fun setClickReview(getProfileModel: GetProfileModel) {
        txtReview.setOnClickListener {
            val intent = Intent(this, OtherProfileReviewsAllActivity::class.java)
            intent.putExtra("keyViewAllData", getProfileModel)
            startActivity(intent)

        }

    }

    private fun setInfoIconClick() {
        txtHeaderCashDelivery.setOnClickListener {
            Constant.cashOnDeliveryDialog(this)
        }
        txtHeaderPartialPayment.setOnClickListener {
            val mDialog = AlertDialog.Builder(this)
            mDialog.setTitle(getString(R.string.partial_payment_dialog_header))
            mDialog.setMessage(
                getString(R.string.partial_payment_dialog_body) + " " + Constant.roundValue(
                    Constant.getProfileData(this).credits.toDouble()
                ) + " " + getString(R.string.credits_in_your_account) + "."
            )
            mDialog.setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                dialog.cancel()

            }
            mDialog.show()
        }
    }

    private fun followUnfollowClick() {
        btnFollow.setOnClickListener {
            follow_status = 1
            btnFollow.visibility = View.VISIBLE
            btnunfollow.visibility = View.GONE
            loader.visibility = View.VISIBLE
            mvmodel.setFollowUnfollow(localMOdelData, follow_status)

        }
        //set the click btnFollow
        btnunfollow.setOnClickListener {
            // hasFollow = false
            follow_status = 2
            btnFollow.visibility = View.VISIBLE
            btnunfollow.visibility = View.GONE
            loader.visibility = View.VISIBLE
            mvmodel.setFollowUnfollow(localMOdelData, follow_status)

        }
    }

    private fun gridClick() {
        ///set the click for changing the view
        imgGridView.setOnClickListener {
            if (setAdapterView == "list") {
                setAdapterView = "grid"
                Glide.with(this)
                    .load(R.drawable.grid_selected)
                    .into(imgGridView)

                Glide.with(this)
                    .load(R.drawable.listview_unselected)
                    .into(imgListView)
                if (edtSearch.text.toString().isNotEmpty()) {
                    searchAdapterLayout()
                    mSearchAdapter.updateData(localSearchArray)
                } else {
                    //default adapter grid
                    mAdapterStore.setAdapterViews("grid")
                }

            } else {
                Constant.showToast("You are already selected Grid view.", this)
                return@setOnClickListener
            }

        }

        //set the click on imgListView
        imgListView.setOnClickListener {
            if (setAdapterView == "grid") {
                setAdapterView = "list"
                Glide.with(this)
                    .load(R.drawable.grid_unselected)
                    .into(imgGridView)

                Glide.with(this)
                    .load(R.drawable.listview_selected)
                    .into(imgListView)
                if (edtSearch.text.toString().isNotEmpty()) {
                    searchAdapterLayout()
                    mSearchAdapter.updateData(localSearchArray)
                } else {
                    //default adapter list
                    mAdapterStore.setAdapterViews("list")
                }

            } else {
                Constant.showToast("You are already selected List view.", this)
                return@setOnClickListener
            }

        }
    }

    private fun searchClick() {
        //***************search listner**********************//
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
//                if (s.isEmpty()) {
//
//
//                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.isEmpty()) {
                    setDefaultStoreAdapter()
                    initData()
                    searchUsedProds = ""
                } else {
                    searchAdapterLayout()
                }

            }
        })

        //************done button listner
        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (edtSearch.text.toString().isNotEmpty()) {
                    //set the api of search
                    page = 1
                    mvmodel.getSearchData(
                        edtSearch.text.toString(), localMOdelData,
                        page,
                        setPriceFilter, searchUsedProds
                    )
                    progressPagination.visibility = View.VISIBLE
                } else {
                    //set api without search

                }

            }
            Constant.hideSoftKeyboard(v, this)
            false
        }
    }

//    private fun setPagination() {
//        //***********************pagination**********************//
//        recyclerSearchAdapter.addOnScrollListener(object :
//            CloseListPagination(SearchLayoutLinearmanager) {
//            override fun loadMoreItems() {
//                isLoadingMoreItems = true
//                page++
//                mvmodel.getSearchData(
//                    edtSearch.text.toString(), localMOdelData,
//                    page,
//                    setPriceFilter,
//                    searchUsedProds
//                )
//                progressPagination.visibility = View.VISIBLE
//            }
//
//            override fun isLastPage(): Boolean {
//                return islastpageData
//            }
//
//            override fun isLoading(): Boolean {
//                return isLoadingMoreItems
//
//            }
//
//        })
//    }

    private fun blockCaiguruUser() {
        blockUser.setOnClickListener {
            val mDialog = AlertDialog.Builder(this)
            mDialog.setTitle(getString(R.string.alert))
            mDialog.setCancelable(true)
            mDialog.setMessage(getString(R.string.blocked_user_text))
            mDialog.setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                //   mvmodel.blockedUser(data, usertype)
                progressPagination.visibility = View.VISIBLE
                dialog.cancel()
            }
            mDialog.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
            mDialog.show()
        }
    }

    private fun setFilterDialog() {
        val dialog = Dialog(this)
        var selectedValue = ""
        // dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.filter_dialog_layout)
        dialog.show()

//set the case of prefilling
        if (setPriceFilter == "1") {
            Glide.with(this)
                .load(R.drawable.ic_selected_checkbox_purple)
                .into(dialog.imgLowToHigh)

            Glide.with(this)
                .load(R.drawable.ic_unselected_checkbox_purple)
                .into(dialog.imgHIghToLow)
            selectedValue = "1"
        } else {
            Glide.with(this)
                .load(R.drawable.ic_unselected_checkbox_purple)
                .into(dialog.imgLowToHigh)

            Glide.with(this)
                .load(R.drawable.ic_selected_checkbox_purple)
                .into(dialog.imgHIghToLow)
            selectedValue = "2"
        }

        //set the click on imgLowToHigh
        dialog.imgLowToHigh.setOnClickListener {
            selectedValue = "1"
            Glide.with(this)
                .load(R.drawable.ic_selected_checkbox_purple)
                .into(dialog.imgLowToHigh)

            Glide.with(this)
                .load(R.drawable.ic_unselected_checkbox_purple)
                .into(dialog.imgHIghToLow)
        }

        //set the click on imgHIghToLow
        dialog.imgHIghToLow.setOnClickListener {
            selectedValue = "2"
            Glide.with(this)
                .load(R.drawable.ic_unselected_checkbox_purple)
                .into(dialog.imgLowToHigh)

            Glide.with(this)
                .load(R.drawable.ic_selected_checkbox_purple)
                .into(dialog.imgHIghToLow)
        }

        dialog.btnOk.setOnClickListener {
            setPriceFilter = selectedValue
            setPriceArrowUpDown()
            if (edtSearch.text.toString().isEmpty()) {
                setDefaultStoreAdapter()
                initData()

            } else {

                searchAdapterLayout()
                page = 1
                mvmodel.getSearchData(
                    edtSearch.text.toString(), localMOdelData,
                    page, setPriceFilter, searchUsedProds
                )
                progressPagination.visibility = View.VISIBLE
            }

            dialog.dismiss()
        }

        dialog.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun followOrUnfolowCheck(it: GetProfileModel) {
        if (it.isFollowed == "0") {
            hasFollow = false
            btnFollow.visibility = View.VISIBLE
            btnunfollow.visibility = View.GONE

        } else {
            hasFollow = true
            btnFollow.visibility = View.GONE
            btnunfollow.visibility = View.VISIBLE
        }
    }

    private fun checkMyProfileOrNot() {
        val CheckMyProfileOrNot = Constant.getProfileData(this).user_id
        if (localMOdelData.seller_id == CheckMyProfileOrNot) {
            btnFollowUnfollow.visibility = View.INVISIBLE
            btnFollow.visibility = View.INVISIBLE
            blockUser.visibility = View.INVISIBLE
        }
    }

    private fun userLevelImageBadgeSet(it: GetProfileModel) {
        //if (it.type == "2") {
        if (it.image.isEmpty()) {
            Glide.with(this)
                .load(R.drawable.user_placeholder)
                .into(imgBatchs)
        } else {
            Glide.with(this)
                .load(setSellerLeveLBatch(it.level))
                .into(imgBatchs)
        }
        // }
//        else {
//            if (it.image.isEmpty()) {
//                Glide.with(this)
//                    .load(R.drawable.user_placeholder)
//                    .into(imgBatchs)
//            } else {
//                Glide.with(this)
//                    .load(setBuyerLevel(it.level))
//                    .into(imgBatchs)
//            }
//        }
    }

    private fun setSellerLeveLBatch(level: String): Int {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category.levelImage
            }
        }
        return 0
    }


    private fun userProfileSet(it: GetProfileModel) {
        if (it.image.isEmpty()) {
            Glide.with(this)
                .load(R.drawable.user_placeholder)
                .into(userPic)
        } else {
            Glide.with(this)
                .load(it.image)
                .into(userPic)
        }

    }

    private fun completedOrderTextSet(it: GetProfileModel) {

        val ReputationText = "${getString(R.string.completed_order)}"
        val fulltext = "$ReputationText ${it.completed_orders}"
        val spannable = SpannableString(fulltext)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            ReputationText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            ReputationText.length,
            fulltext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            boldSpan, ReputationText.length,
            fulltext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        completedOrder.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun LevelTextSet(it: GetProfileModel) {
        //   if (it.type == "2") {
        ReputationText = "${getString(R.string.level)}"
        fulltext =
            "$ReputationText ${SellerLevelGet(it.level)} ${"("}${it.points} ${getString(R.string.points)}${")"}"
        //}
//        else {
//            ReputationText = "${getString(R.string.level)}"
//            fulltext =
//                "$ReputationText ${BuyerLevelGet(it.level)} ${"("}${it.points} ${getString(R.string.points)}${")"}"
//        }

        val spannable = SpannableString(fulltext)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            ReputationText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            ReputationText.length,
            fulltext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan, ReputationText.length,
            fulltext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        level.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    private fun SellerLevelGet(level: String): String {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category.levelname
            }
        }
        return " "

    }

//    private fun BuyerLevelGet(level: String): String {
//        val buyerlevel = Constant.BuyerLevel(this)
//        for (category in buyerlevel) {
//
//            if (category.levelType == level.trim()) {
//
//                return category.levelname
//            }
//        }
//        return " "
//    }


//    private fun setBuyerLevel(level: String): Int {
//
//        val BuyerLevel = Constant.BuyerLevel(this)
//        for (category in BuyerLevel) {
//            if (category.levelType == level.trim()) {
//                return category.levelImage
//            }
//        }
//        return 0
//    }

    private fun reputationTextSet(it: GetProfileModel) {
        val ReputationText = "${getString(R.string.reputation)}"
        val fulltext = "$ReputationText ${Constant.getReputationString(this, it.reputation)}"
        val spannable = SpannableString(fulltext)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            ReputationText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            ReputationText.length,
            fulltext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan, ReputationText.length,
            fulltext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        reputation.setText(spannable, TextView.BufferType.SPANNABLE)


    }

    //**************Load more click
    override fun LoadMoreClick(data: ShoppingListModel, position: Int) {
        data.page += 1
        loadMoreAdapterPosition = position
        Log.e("arraySizePage", data.page.toString())
        mvmodel.LoadMoreData(data, localMOdelData, setPriceFilter)
        progressPagination.visibility = View.VISIBLE
    }


    //***************product add click
    override fun productAddClick(
        adapterArray: ArrayList<PostShoppingModel>,
        list: PostShoppingModel,
        position: Int
    ) {
        setAllCalaculations(adapterArray, list, position)
    }


    private fun setAllCalaculations(
        adapterArray: ArrayList<PostShoppingModel>,
        list: PostShoppingModel,
        position: Int
    ) {
        var isNewProduct = true
        var isProductRemove = false
        if (localProductArray.size <= 0) {
            isNewProduct = false
            localProductArray.add(list)

        } else {
            for (item in localProductArray) {
                if (item.id == list.id) {
                    isNewProduct = false
                    if (list.qty == "0") {
                        isProductRemove = true
                    } else {
                        // if (list.qty.toInt() > item.qty.toInt()) {
                        item.qty = list.qty
                        item.price = list.price
                        item.LocalTotal = list.LocalTotal
                        //}
                    }

                }


            }
        }
        //add new product
        if (isNewProduct) {
            localProductArray.add(list)
        }
        //delete the old product
        if (isProductRemove) {
            removeProductPermanentFromCart(list)
        }

        for (item in localProductArray) {
            item.LocalTotal = AllTotal(localProductArray).toString()//set the total
            item.partialComission =
                setTotalPartialComission(localProductArray).toString()//partial payments

        }

        if (localProductArray.size > 0) {
            for (item in localProductArray) {
                //paartial comission
                partialComission =
                    Constant.roundValue(item.partialComission.toDouble())//partial payments

                //cash on delivery
                val cashOnDeliverys =
                    item.LocalTotal.toDouble() - item.partialComission.toDouble()
                cashOnDelivery = Constant.roundValue(cashOnDeliverys)

//total price
                totalPriced = item.LocalTotal.toDouble()
                break
            }

            //set the total and partial comission
//            partialComission =
//                Constant.roundValue(adapterArray[position].partialComission.toDouble())//partial payments

            //cash on delivery
//            val cashOnDeliverys =
//                adapterArray[position].LocalTotal.toDouble() - adapterArray[position].partialComission.toDouble()
//            cashOnDelivery = Constant.roundValue(cashOnDeliverys)

//total price
            // totalPriced = adapterArray[position].LocalTotal.toDouble()

        } else {
            partialComission = ""
            cashOnDelivery = ""
            totalPriced = 0.0
        }
        SetAllProductData()
        mAdapterCart.update(localProductArray)
    }

    private fun removeProductPermanentFromCart(list: PostShoppingModel) {
        val localArray = ArrayList<PostShoppingModel>()
        if (localProductArray.size > 0) {
            for (item in localProductArray) {
                if (item.id != list.id) {
                    localArray.add(item)
                }
            }
        }

        localProductArray.clear()
        localProductArray.addAll(localArray)
    }


    //***********set the data in the below cart
    private fun SetAllProductData() {
        txtTotalProduct.text =
            this.localProductArray.size.toString() + " " + getString(R.string.products_in_your_cart_store)
        txtTotalHeading.text =
            getString(R.string.minum_purchase_cart) + Constant.roundValue(storeApiData.listInfo.minimum_purchase_amount.toDouble()) + ")"

        //partila payments
        if (partialComission.isEmpty()) {
            txtPartialPayment.text =
                "0.0" + getString(R.string.cr)
        } else {
            txtPartialPayment.text =
                partialComission + getString(R.string.cr)
        }
        //cash on delivery
        if (cashOnDelivery.isEmpty()) {
            txtCashOnDelivery.text = "$" + "0.0"
        } else {
            txtCashOnDelivery.text =
                "$" + cashOnDelivery
        }

//set total
        if (totalPriced < 0) {
            txtTotalPrice.text = "$0.00"
        } else {
            txtTotalPrice.text = "$" + totalPriced.toString()
        }


    }

    override fun viewFullScreenProductClick(child: ArrayList<PostShoppingModel>, position: Int) {

        val intent = Intent(this, ViewStoreFullScreenActivity::class.java)
        intent.putParcelableArrayListExtra("KeyStoreProduct", child)
        intent.putExtra("KeyStoreProductPosition", position)
        intent.putExtra("keyStoreApiData", localMOdelData)
        intent.putExtra("keyStoreFilter", setPriceFilter)
        startActivityForResult(intent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            val myArray =
                data!!.getParcelableArrayListExtra<PostShoppingModel>("KeyGetStoreData")!!
            upDateAdapter(myArray)
            searchAdapterUpdate(myArray)
            setViewStoreFullScreenData(myArray)
        }
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            bLetsFinish = true
            finish()
        }
    }

    ///update the adapter qty
    //default adapter update
    private fun upDateAdapter(prefillArray: ArrayList<PostShoppingModel>) {
        for (parentArray in storeApiData.products) {
            for (childArray in parentArray.products) {

                for (adapterArray in prefillArray) {

                    if (childArray.id == adapterArray.id) {
                        // if (adapterArray.qty.toInt() > 0) {
                        childArray.qty = adapterArray.qty
                        childArray.total = adapterArray.total
                        // }
                    }
                }


            }
        }
        mAdapterStore.updateData(storeApiData.products)

    }

    private fun searchAdapterUpdate(prefillArray: ArrayList<PostShoppingModel>) {
        if (localSearchArray.size > 0) {
            for (childArray in localSearchArray) {

                for (adapterArray in prefillArray) {
                    if (childArray.id == adapterArray.id) {
                        childArray.qty = adapterArray.qty
                        childArray.total = adapterArray.total

                    }
                }
            }
            mSearchAdapter.updateData(localSearchArray)
        }

    }

    override fun deleteProduct(
        child: ArrayList<PostShoppingModel>,
        productId: String
    ) {
        if (edtSearch.text.isNotEmpty()) {
            mSearchAdapter.clearCartSingleProduct(productId)
        } else {
            mAdapterStore.clearCartSingleProduct(productId)
        }

        if (localProductArray.size > 0) {
            //set the total and partial comission
            for (item in localProductArray) {
                partialComission =
                    Constant.roundValue(item.partialComission.toDouble())//partial payments

                //cash on delivery
                val cashOnDeliverys =
                    item.LocalTotal.toDouble() - item.partialComission.toDouble()
                cashOnDelivery = Constant.roundValue(cashOnDeliverys)

//total price
                totalPriced = item.LocalTotal.toDouble()
                break
            }
        } else {
            partialComission = ""
            cashOnDelivery = ""
            totalPriced = 0.0
        }
        SetAllProductData()
        mAdapterCart.update(localProductArray)
    }

    override fun CartproductAddClick(
        child: ArrayList<PostShoppingModel>,
        list: PostShoppingModel,
        position: Int
    ) {
        if (edtSearch.text.isNotEmpty()) {
            mSearchAdapter.setQty(list)
        } else {
            mAdapterStore.setQty(list)
        }
        mAdapterStore.setQty(list)
        setAllCalaculations(child, list, position)

    }

    //get the view all screen sort product
    private fun setViewStoreFullScreenData(adapterArray: ArrayList<PostShoppingModel>) {
        val localArray = ArrayList<PostShoppingModel>()
        //check the product that are already in array
        if (localProductArray.size > 0) {
            for (item in localProductArray) {
                for (mAdapter in adapterArray) {
                    if (item.id == mAdapter.id) {
                        item.qty = mAdapter.qty
                        item.price = mAdapter.price
                        mAdapter.duplicateProduct = true
                    }
                }
            }

        }
// add the new product in array
        for (mAdapter in adapterArray) {
            if (mAdapter.qty.toInt() > 0 && mAdapter.duplicateProduct == false) {
                localArray.add(mAdapter)
            }
        }
        localProductArray.addAll(localArray)

        for (item in localProductArray) {
            item.LocalTotal = AllTotal(localProductArray).toString()//set the total
            item.partialComission =
                setTotalPartialComission(localProductArray).toString()//partial payments

        }
        if (adapterArray.size > 0) {
            for (item in localProductArray) {
                //paartial comission
                partialComission =
                    Constant.roundValue(item.partialComission.toDouble())//partial payments

                //cash on delivery
                val cashOnDeliverys =
                    item.LocalTotal.toDouble() - item.partialComission.toDouble()
                cashOnDelivery = Constant.roundValue(cashOnDeliverys)

//total price
                totalPriced = item.LocalTotal.toDouble()
                break
            }

        } else {
            partialComission = ""
            cashOnDelivery = ""
            totalPriced = 0.0
        }
        SetAllProductData()
        mAdapterCart.update(localProductArray)
    }


    private fun AllTotal(list: java.util.ArrayList<PostShoppingModel>): Double {
        var total = 0.0
        for (item in list) {

            if (total == 0.0) {
                total = item.priceWithComission.toDouble() * item.qty.toDouble()
            } else {
                total += item.priceWithComission.toDouble() * item.qty.toDouble()
            }
        }
        return Constant.roundValue(total).toDouble()

    }

    private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
        //set the  total comission of the products
        var totalPatialComission = 0.0
        for (item in data) {
            if (item.price != item.priceWithComission) {
                if (totalPatialComission == 0.0) {
                    val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                    totalPatialComission = allTotal * item.qty.toDouble()
                } else {
                    val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
                    totalPatialComission += allTotal * item.qty.toDouble()
                }
            } else {
                if (totalPatialComission == 0.0) {
                    val allTotal = 1
                    totalPatialComission = allTotal * item.qty.toDouble()
                } else {
                    val allTotal = 1
                    totalPatialComission += allTotal * item.qty.toDouble()
                }
            }

        }
        return totalPatialComission
    }


    private fun getJsonProductArray(
    ): String {

        val parentArray = JSONArray()
        val productArray = JSONArray()
        val jsonObj = JSONObject()
        for (item in localProductArray) {
            if (item.qty.toInt() > 0) {
                val jsonObj = JSONObject()
                jsonObj.put("id", item.id)
                jsonObj.put("qty", item.qty)
                jsonObj.put("price", item.price)
                jsonObj.put("priceWithComission", item.priceWithComission)
                jsonObj.put("name", item.name)
                jsonObj.put("image", item.image)
                jsonObj.put("unit", item.unit)
                jsonObj.put("list_id", item.list_id)
                productArray.put(jsonObj)
            }

        }
        //  jsonObj.put("products", productArray)
        // jsonObj.put("list_id", mData[position].id)
        // jsonObj.put("delivery_type", storeApiData.listInfo.delivery_type)
        //  jsonObj.put("amount", totalPriced)
//        jsonObj.put(
//            "credits",
//            ((mData[position].total.toDouble() * mData[position].comission_per.toDouble()) / 100).toString()
//        )
        // jsonObj.put("credits", partialComission)
        //return parentArray.put(jsonObj).toString()
        return productArray.toString()
    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        toolbarProfileText.text = localMOdelData.seller_name
        //***********set the click on back
        imgBack.setOnClickListener {

            if (storeApiData.is_purchase_button_show == "1") {
                if (localProductArray.size <= 0) {
                    bLetsFinish = true
                }

                if (bLetsFinish) {
                    super.finish() //This will finish the activity and take you back
                } else {
                    //Do whatever it is that you want to do
                    setAlertDialog()
                }

            } else {
                bLetsFinish = true
                super.finish()
            }


        }
    }

    //    override fun onBackPressed() {
//        setAlertDialog()
//        super.onBackPressed()
//
//    }
    override fun finish() {
        if (storeApiData.is_purchase_button_show == "1") {
            if (localProductArray.size <= 0) {
                bLetsFinish = true
            }

            if (bLetsFinish) {
                super.finish() //This will finish the activity and take you back
            } else {
                //Do whatever it is that you want to do
                return setAlertDialog()
            }

        } else {
            bLetsFinish = true
            super.finish()
        }
    }

    fun setAlertDialog() {
        val dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.requested_list_custom_dialog)
        dialog.show()
        dialog.alertHeader.text = getString(R.string.alert)
        dialog.alertBody.text = getString(R.string.alert_cart)
        //yes click
        //*****************set the api of requsted buying list*******************//
        dialog.yes.setOnClickListener {
            mvmodel.save_seller_cart(localMOdelData.seller_id, getJsonProductArray())
            showLoader.visibility = View.VISIBLE
            dialog.dismiss()
        }
        dialog.no.setOnClickListener {
            dialog.dismiss()
            bLetsFinish = true
            finish()

        }
    }

    private fun alertDialog1(it: String) {
        val mDialog = android.app.AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setCancelable(false)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
            bLetsFinish = true
            finish()
        }
        mDialog.show()

    }

    private fun Total(list: PostShoppingModel): Double {
        var total = 0.0

        total += list.priceWithComission.toDouble() * list.qty.toDouble()
        return Constant.roundValue(total).toDouble()

    }
}

abstract class AppBarStateChangeListener : OnOffsetChangedListener {
    enum class State {
        EXPANDED, COLLAPSED, IDLE
    }

    private var mCurrentState =
        State.IDLE

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        mCurrentState = if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED)
            }
            State.EXPANDED
        } else if (Math.abs(i) >= appBarLayout.totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED)
            }
            State.COLLAPSED
        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE)
            }
            State.IDLE
        }
    }

    abstract fun onStateChanged(
        appBarLayout: AppBarLayout?,
        state: State?
    )
}
//abstract class AppBarStateChangeListener : OnOffsetChangedListener {
//    enum class State {
//        EXPANDED, COLLAPSED, IDLE
//    }
//
//    private var mCurrentState =
//        State.IDLE
//
//    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
//        mCurrentState = if (i == 0) {
//            if (mCurrentState != State.EXPANDED) {
//                onStateChanged(appBarLayout, State.EXPANDED)
//            }
//            State.EXPANDED
//        } else if (Math.abs(i) >= appBarLayout.totalScrollRange) {
//            if (mCurrentState != State.COLLAPSED) {
//                onStateChanged(appBarLayout, State.COLLAPSED)
//            }
//            State.COLLAPSED
//        } else {
//            if (mCurrentState != State.IDLE) {
//                onStateChanged(appBarLayout, State.IDLE)
//            }
//            State.IDLE
//        }
//    }
//
//    abstract fun onStateChanged(
//        appBarLayout: AppBarLayout?,
//        state: State?
//    )
//}
