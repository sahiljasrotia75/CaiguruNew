package com.example.caiguru.seller.sellerOrder.sellerPendingApprovals

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivitySellerPendingApprovalsBinding
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.SellerApprovalOrderListActivity
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_pending_approvals.*

class SellerPendingApprovalsActivity : AppCompatActivity(),
    PendingApprovalsBySellerAdapter.setAdapteClick {

    private lateinit var mvmodel: SellerPendingApprovalsViewModel
    private lateinit var mbinding: ActivitySellerPendingApprovalsBinding
    private lateinit var adapterSeller: PendingApprovalsBySellerAdapter
    private lateinit var text: TextView
    private var hasSelected: Boolean = true
    private var hasSelected1: Boolean = true
    var list_Type = "2"
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 0
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_pending_approvals)
        mvmodel = ViewModelProviders.of(this)[SellerPendingApprovalsViewModel::class.java]
        SettingUpToolbar()
        SetAdapterpendingBySeller()
        mbinding.progressedBars.visibility = View.VISIBLE
        PendingPagination()
        sellerActiveStatus()

        hasSelected = true
        mbinding.btnPendingByer.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple_fillcolor))
        mbinding.btnPendingByer.setTextColor(this.resources.getColor(R.color.white))

        hasSelected1 = true
        mbinding.btnPendingSeller.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referral_credit_purple))
        mbinding.btnPendingSeller.setTextColor(this.resources.getColor(R.color.purple))
        //****************api get_seller_requests*******************//
        mvmodel.getSellerRequest(list_Type, page)
//set the click on the button
        mbinding.btnPendingByer.setOnClickListener {
            if (hasSelected) {
                hasSelected = false
                mbinding.btnPendingByer.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple_fillcolor))
                mbinding.btnPendingByer.setTextColor(this.resources.getColor(R.color.white))
                NoDataSellerPending.visibility = View.GONE
                pendingBySellerRecycler.visibility = View.GONE
                //****************api get_seller_requests*******************//
                page = 0
                list_Type = "2"
                mvmodel.getSellerRequest(list_Type, page)
                mbinding.progressedBars.visibility = View.VISIBLE
                hasSelected1 = true
                mbinding.btnPendingSeller.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referral_credit_purple))
                mbinding.btnPendingSeller.setTextColor(this.resources.getColor(R.color.purple))

            } else {
                hasSelected = true
            }
        }
        //set the click on the seller button
        mbinding.btnPendingSeller.setOnClickListener {
            if (hasSelected1) {
                hasSelected1 = false
                mbinding.btnPendingSeller.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referal_purple_fillcolor))
                mbinding.btnPendingSeller.setTextColor(this.resources.getColor(R.color.white))
                NoDataSellerPending.visibility = View.GONE
                pendingBySellerRecycler.visibility = View.GONE
                //****************api get_seller_requests*******************//
                page = 0
                list_Type = "1"
                mvmodel.getSellerRequest(list_Type, page)
                mbinding.progressedBars.visibility = View.VISIBLE
                hasSelected = true
                mbinding.btnPendingByer.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple))
                mbinding.btnPendingByer.setTextColor(this.resources.getColor(R.color.purple))

            } else {

                hasSelected1 = true
            }

        }
        //*************************get seller request observer******************//
        //sucessful
        mvmodel.mSucessfulPendingApproval().observe(this, Observer {
            mbinding.progressedBars.visibility = View.GONE
            isLoadingMoreItems == true
            if (it.isEmpty()) {
                NoDataSellerPending.visibility = View.VISIBLE
                mbinding.pendingBySellerRecycler.visibility = View.GONE
            } else {
                adapterSeller.upDate(it)
                NoDataSellerPending.visibility = View.GONE
                mbinding.pendingBySellerRecycler.visibility = View.VISIBLE
            }
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            mbinding.progressedBars.visibility = View.GONE
            NoDataSellerPending.visibility = View.VISIBLE
            mbinding.pendingBySellerRecycler.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }



    private fun sellerActiveStatus() {

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            activeOrder.background = resources.getDrawable(R.color.green)
            activeOrder.text = resources.getText(R.string.active)

        } else {
            activeOrder.background = resources.getDrawable(R.color.red)
            activeOrder.text = resources.getText(R.string.inactive)
        }
    }

    private fun PendingPagination() {
        //***********************pagination**********************//
        mbinding.pendingBySellerRecycler.addOnScrollListener(object :
            CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                mbinding.progressedBars.visibility = View.VISIBLE
                page++
                //****************api get_seller_requests*******************//
                mvmodel.getSellerRequest(list_Type, page)
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
        adapterSeller = PendingApprovalsBySellerAdapter(this)
        mbinding.pendingBySellerRecycler.layoutManager = layoutpag
        mbinding.pendingBySellerRecycler.adapter = adapterSeller
    }

    override fun setClick(list: SellerApprovalModel) {
        val intent = Intent(this, SellerApprovalOrderListActivity::class.java)
        intent.putExtra("keyBuyerData", list)
        startActivityForResult(intent, 200)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        page = 0
        mvmodel.getSellerRequest(list_Type, page)
        mbinding.progressedBars.visibility = View.VISIBLE

    }
}
