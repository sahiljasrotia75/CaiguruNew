package com.example.caiguru.buyer.buyerOrder.buyerPendingApproval

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList.BuyerShopOpenModel
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerShopListApproveRejectActivity
import com.example.caiguru.buyer.buyerOrder.buyerOrderDetails.FinalizeApproveRejectDetailsActivity
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.BuyerOrderDetailsActivity
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.SellerApprovalOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_pending_approval.*
import kotlinx.android.synthetic.main.toolbar.*

class BuyerPendingApprovalActivity : AppCompatActivity(),
    PendingApprovalsByBuyerAdapter.setAdapteClick {
    private lateinit var mAdapter: PendingApprovalsByBuyerAdapter
    private lateinit var mvmodel: BuyerPendingViewModel
    private lateinit var text: TextView
    var list_Type = "1"
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_pending_approval)
        mvmodel = ViewModelProviders.of(this)[BuyerPendingViewModel::class.java]
        SettingUpToolbar()
        SetAdapterpendingBySeller()
        progressedBars.visibility = View.VISIBLE
        PendingPagination()
        //setClick()
        setObserver()
      //  initData()
//set the click on the button


    }

//    private fun initData() {
//        hasSelected = true
//        btnPendingByer.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple_fillcolor))
//        btnPendingByer.setTextColor(this.resources.getColor(R.color.white))
//
//        hasSelected1 = true
//        btnPendingSeller.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referral_credit_purple))
//        btnPendingSeller.setTextColor(this.resources.getColor(R.color.purple))
//        //****************api get_seller_requests*******************//
//        mvmodel.getBuyerRequest(list_Type, page)
//    }

    private fun setObserver() {
        //*************************get seller request observer******************//
        //sucessful
        mvmodel.mSucessfulFinalizeOrder().observe(this, Observer {
            progressedBars.visibility = View.GONE
            isLoadingMoreItems == true
            if (it.isEmpty()) {
                NoDataSellerPending.visibility = View.VISIBLE
                pendingBySellerRecycler.visibility = View.GONE
            } else {
                mAdapter.upDate(it)
                NoDataSellerPending.visibility = View.GONE
                pendingBySellerRecycler.visibility = View.VISIBLE
            }
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            progressedBars.visibility = View.GONE
            NoDataSellerPending.visibility = View.VISIBLE
            pendingBySellerRecycler.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

//    private fun setClick() {
//        btnPendingByer.setOnClickListener {
//            if (hasSelected) {
//                hasSelected = false
//                btnPendingByer.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple_fillcolor))
//                btnPendingByer.setTextColor(this.resources.getColor(R.color.white))
//                NoDataSellerPending.visibility = View.GONE
//                pendingBySellerRecycler.visibility = View.GONE
//                //****************api get_seller_requests*******************//
//                page = 1
//                list_Type = "2"
//                mvmodel.getBuyerRequest(list_Type, page)
//                progressedBars.visibility = View.VISIBLE
//                hasSelected1 = true
//                btnPendingSeller.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referral_credit_purple))
//                btnPendingSeller.setTextColor(this.resources.getColor(R.color.purple))
//
//            } else {
//                hasSelected = true
//            }
//        }
//        //set the click on the seller button
//        btnPendingSeller.setOnClickListener {
//            if (hasSelected1) {
//                hasSelected1 = false
//                btnPendingSeller.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referal_purple_fillcolor))
//                btnPendingSeller.setTextColor(this.resources.getColor(R.color.white))
//                NoDataSellerPending.visibility = View.GONE
//                pendingBySellerRecycler.visibility = View.GONE
//                //****************api get_seller_requests*******************//
//                page = 1
//                list_Type = "1"
//                mvmodel.getBuyerRequest(list_Type, page)
//                progressedBars.visibility = View.VISIBLE
//                hasSelected = true
//                btnPendingByer.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple))
//                btnPendingByer.setTextColor(this.resources.getColor(R.color.purple))
//
//            } else {
//
//                hasSelected1 = true
//            }
//
//        }
//    }




    private fun PendingPagination() {
        //***********************pagination**********************//
        pendingBySellerRecycler.addOnScrollListener(object :
            CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressedBars.visibility = View.VISIBLE
                page++
                //****************api get_seller_requests*******************//
                mvmodel.getBuyerRequest(list_Type, page)
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        whatsAppTextICons.visibility = View.VISIBLE
        text.text = getText(R.string.Pending_Approvals)
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

    private fun SetAdapterpendingBySeller() {
        mAdapter = PendingApprovalsByBuyerAdapter(this)
        pendingBySellerRecycler.layoutManager = layoutpag
        pendingBySellerRecycler.adapter = mAdapter
    }

    override fun setClick(list: FinalizeModel) {

        if (list.list_type == "1") {
            val model = OrderModel()
            model.list_type = list.list_type
            model.req_id = list.id
            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)
            intent.putExtra("keydetails", model)
            startActivity(intent)
        } else {
            val model = BuyerShopOpenModel()
            model.seller_name = list.seller_name
            model.id = list.id
            model.seller_image = list.seller_image
            model.seller_level = list.seller_level
            val intent = Intent(this, FinalizeApproveRejectDetailsActivity::class.java)
            intent.putExtra("keyMyOrderApproveReject", list)
            startActivityForResult(intent, 200)


        }
        whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getBuyerRequest(list_Type, page)
        progressedBars.visibility = View.VISIBLE

    }


}
