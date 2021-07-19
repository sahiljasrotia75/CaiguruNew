package com.example.caiguru.buyer.buyerOrder.buyerToBeDelivered

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
import com.example.caiguru.buyer.buyerOrder.buyerOrderDetails.FinalizeOrderDetailsActivity
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails.UnderReviewOrderDetailsActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation.CommonNotificationConfirmationActivity
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_to_be_delivered.*
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.toolbar.*

class BuyerToBeDeliveredActivity : AppCompatActivity(), BuyerDeliveredAdapter.setAdapterInterface {
    private lateinit var mvmodel: BuyerDeliveredViewModel
    private lateinit var adapter: BuyerDeliveredAdapter
    private lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_to_be_delivered)
        mvmodel = ViewModelProvider(this).get(BuyerDeliveredViewModel::class.java)

        setAdapter()
        SettingUpToolbar()
        Pagination()
        setObserver()
        setClick()
    }

    private fun setClick() {
        whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }
    }

    private fun setObserver() {

        mvmodel.mSucessfulFinalizeOrder().observe(this, Observer {
            progresapproval.visibility = View.GONE
            progresapprovalPagination.visibility = View.GONE
            if (it.size > 0) {
                recyclerldelivered.visibility = View.VISIBLE
                txtNoData.visibility = View.GONE
            } else {
                recyclerldelivered.visibility = View.GONE
                txtNoData.visibility = View.VISIBLE
            }

            adapter.upDate(it)

        })

        mvmodel.mFailure().observe(this, Observer {
            Constant.showToast(it, this)
            progresapproval.visibility = View.GONE
            progresapprovalPagination.visibility = View.GONE
            recyclerldelivered.visibility = View.GONE
            txtNoData.visibility = View.VISIBLE
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
        text.text = getText(R.string.To_be_Delivered)
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

    private fun setAdapter() {
        adapter = BuyerDeliveredAdapter(this)
        recyclerldelivered.layoutManager = layoutpag
        recyclerldelivered.adapter = adapter
    }

    private fun Pagination() {
        //***********************pagination**********************//
        recyclerldelivered.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                page++
                //************get Finalize order
                mvmodel.getDeliveredOrder(page)
                progresapprovalPagination.visibility = View.VISIBLE
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }

    override fun onResume() {
        super.onResume()
        page = 1
        mvmodel.getDeliveredOrder(page)
        progresapproval.visibility = View.VISIBLE


    }

    override fun setAdapterClick(list: FinalizeModel) {
        if (list.req_status == "7") {
            val model = NotificationModel()
            model.source_id = list.id
            model.list_type = list.list_type
            val intent =
                Intent(this, CommonNotificationConfirmationActivity::class.java)
            intent.putExtra("keyFinalizeOrder", list)
            intent.putExtra("KeySourceCancellOrderConfirm", model)
            startActivity(intent)
        } else if (list.req_status == "6") {
            val model = OrderModel()
            model.req_id = list.id
            model.list_type = list.list_type
            val intent = Intent(this, UnderReviewOrderDetailsActivity::class.java)
            intent.putExtra("keydetails", model)
            startActivity(intent)
        } else {
            val intent = Intent(this, FinalizeOrderDetailsActivity::class.java)
            intent.putExtra("KeyBuyerDetails", list)
            intent.putExtra("KeyShowCaNcelBtnFromDeliveryTab", "")
            startActivity(intent)
        }

    }

}

