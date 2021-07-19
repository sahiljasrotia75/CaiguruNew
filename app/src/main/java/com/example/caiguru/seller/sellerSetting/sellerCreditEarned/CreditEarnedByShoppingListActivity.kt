package com.example.caiguru.seller.sellerSetting.sellerCreditEarned

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityCreditEarnedByShoppingListBinding
import com.example.caiguru.commonScreens.earnCreditsConvert.CreditConvertActivity
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import com.google.firebase.iid.FirebaseInstanceId
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_credit_earned_by_shopping_list.*
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.lang.Exception

class CreditEarnedByShoppingListActivity : AppCompatActivity() {

     var tokens: String=""
    private lateinit var mvmodel: CreditEarnedViewModel
    private lateinit var mAdapterReferalCode: EarnedByReferalCodeAdapter
    private lateinit var mAdapterEarnedShoppingList: EarnedShoppingListParentAdapter
    private var hasSelected: Boolean = true
    private var hasSelected1: Boolean = true
    private lateinit var text: TextView
    //private lateinit var dialog: Dialog
    private lateinit var mbinding: ActivityCreditEarnedByShoppingListBinding
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 1
    var layoutpag = LinearLayoutManager(this)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_credit_earned_by_shopping_list)
      //  mvmodel = ViewModelProviders.of(this)[CreditEarnedViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(CreditEarnedViewModel::class.java)
        //homeToolbar.inCash.visibility=View.VISIBLE

        SettingUpToolbar()
        Pagination()
        hasSelected = true
        mbinding.btnEarnedByShoppinList.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple_fillcolor))
        mbinding.btnEarnedByShoppinList.setTextColor(this.resources.getColor(R.color.white))
        mbinding.recyclerEarnedShoppinList.visibility = View.VISIBLE
        mbinding.recyclerEarnedByReferalCode.visibility = View.GONE

      //  mvmodel.earned_by_listing(page.toString())
        progress.visibility = View.VISIBLE
        setAdapterEarnedShoppinList()

        hasSelected1 = true
        mbinding.btnEarnedByReferalCode.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referral_credit_purple))
        mbinding.btnEarnedByReferalCode.setTextColor(this.resources.getColor(R.color.purple))

        setAdapterEarnedShoppinList()
        setAdapterEarnedBYReferalCode()
//**************************************Earned by Reffer********************************//
        //sucessful case
        mvmodel.getList().observe(this, Observer {
            progress.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
                recyclerEarnedByReferalCode.visibility = View.INVISIBLE
                recyclerEarnedShoppinList.visibility = View.INVISIBLE
                mAdapterReferalCode.update(it)
            } else {
                txtNoData.visibility = View.INVISIBLE
                recyclerEarnedByReferalCode.visibility = View.VISIBLE
                recyclerEarnedShoppinList.visibility = View.INVISIBLE
                mAdapterReferalCode.update(it)
            }
        })

        //failure
        mvmodel.getListFailure().observe(this, Observer {
            Constant.showToast(it,this)
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            txtNoData.visibility = View.VISIBLE
            recyclerEarnedShoppinList.visibility = View.INVISIBLE
            recyclerEarnedByReferalCode.visibility = View.INVISIBLE
            progress.visibility = View.GONE


        })
        //***********************************getProfileData***********************************************//
        mvmodel.SucessfulDataGetProfile().observe(this, Observer {
            progress.visibility = View.GONE
            totalcredit.visibility = View.VISIBLE
            mbinding.totalcredit.setText(
                Constant.roundValue(it.earned_credits.toDouble()) + getString(
                    R.string.Cr
                )
            )
            if (it.earned_credits.isNotEmpty()) {
                try {
                    val earned_credits = Constant.roundValue(it.earned_credits.toDouble()).toDouble()
                    val redeemCredit =
                        Constant.roundValue(it.redeem_credit_limit.toDouble()).toDouble()
                    if (earned_credits >= redeemCredit) {
                        toolbar.inCash.visibility = View.VISIBLE
                        toolbar.inCash.setOnClickListener {
                            val intent = Intent(this, CreditConvertActivity::class.java)
                            intent.putExtra("KeySourceopenScreenByActivity", 1)
                            startActivity(intent)
                        }
                    } else {
                        toolbar.inCash.visibility = View.GONE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        })
        //failure
        mvmodel.errorgetInGetProfile().observe(this, Observer {
       //     Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message,this)
            txtNoData.visibility = View.VISIBLE
            recyclerEarnedShoppinList.visibility = View.INVISIBLE
            recyclerEarnedByReferalCode.visibility = View.INVISIBLE
            progress.visibility = View.GONE
        })


        //**************************************Earned by Commission********************************//
        //sucessful case
        mvmodel.getList1().observe(this, Observer {
            progress.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
                recyclerEarnedShoppinList.visibility = View.INVISIBLE
                recyclerEarnedByReferalCode.visibility = View.INVISIBLE

                mAdapterEarnedShoppingList.updateData(it)
            } else {
                txtNoData.visibility = View.INVISIBLE
                recyclerEarnedShoppinList.visibility = View.VISIBLE
                recyclerEarnedByReferalCode.visibility = View.INVISIBLE

                mAdapterEarnedShoppingList.updateData(it)
            }
            isLoadingMoreItems = false
            progressPagin.visibility = View.INVISIBLE
        })
        //failure
        mvmodel.getFailure().observe(this, Observer {
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            txtNoData.visibility = View.VISIBLE
            recyclerEarnedShoppinList.visibility = View.INVISIBLE
            recyclerEarnedByReferalCode.visibility = View.INVISIBLE
            progress.visibility = View.GONE
            progressPagin.visibility = View.INVISIBLE


        })
        //*************************************************************************//

        //*****************click of Earned by shopping list*******************//
        mbinding.btnEarnedByShoppinList.setOnClickListener {
            if (hasSelected) {
                hasSelected = false
                mbinding.btnEarnedByShoppinList.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple_fillcolor))
                mbinding.btnEarnedByShoppinList.setTextColor(this.resources.getColor(R.color.white))
                mbinding.recyclerEarnedShoppinList.visibility = View.VISIBLE
                mbinding.recyclerEarnedByReferalCode.visibility = View.GONE

                mvmodel.earned_by_listing(page.toString())
                progress.visibility = View.VISIBLE
                setAdapterEarnedShoppinList()

                hasSelected1 = true
                mbinding.btnEarnedByReferalCode.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referral_credit_purple))
                mbinding.btnEarnedByReferalCode.setTextColor(this.resources.getColor(R.color.purple))

            } else {
                hasSelected = true
            }
        }

        //*********************click earned by referal code*****************//
        mbinding.btnEarnedByReferalCode.setOnClickListener {
            if (hasSelected1) {

                hasSelected1 = false
                mbinding.btnEarnedByReferalCode.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_referal_purple_fillcolor))
                mbinding.btnEarnedByReferalCode.setTextColor(this.resources.getColor(R.color.white))
                mbinding.recyclerEarnedShoppinList.visibility = View.GONE
                mbinding.recyclerEarnedByReferalCode.visibility = View.VISIBLE

                mvmodel.earnedByreffer()
                progress.visibility = View.VISIBLE
                setAdapterEarnedBYReferalCode()

                hasSelected = true
                mbinding.btnEarnedByShoppinList.setBackgroundDrawable(this.getDrawable(R.drawable.curve_rectangle_credit_purple))
                mbinding.btnEarnedByShoppinList.setTextColor(this.resources.getColor(R.color.purple))

            } else {
                hasSelected1 = true

            }
        }
    }

//    private fun showDialog() {
//        dialog = Dialog(this)
//        // dialog.setCancelable(false);
//        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setContentView(R.layout.activity_twelve_hundred_credit)
//        dialog.show()
//    }


    private fun setAdapterEarnedShoppinList() {
        //val manager = LinearLayoutManager(this)
        mbinding.recyclerEarnedShoppinList.layoutManager = layoutpag
        mAdapterEarnedShoppingList = EarnedShoppingListParentAdapter(this)
        mbinding.recyclerEarnedShoppinList.adapter = mAdapterEarnedShoppingList
    }

    private fun setAdapterEarnedBYReferalCode() {
        val manager = LinearLayoutManager(this)
        mbinding.recyclerEarnedByReferalCode.layoutManager = manager
        mAdapterReferalCode = EarnedByReferalCodeAdapter(this)
        mbinding.recyclerEarnedByReferalCode.adapter = mAdapterReferalCode
    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Credits_Earned)
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
//set the get Profile Api
        tokens = FirebaseInstanceId.getInstance().token!!
        mvmodel.getProfile(tokens)
        progress.visibility = View.VISIBLE
        page=1
        mvmodel.earned_by_listing(page.toString())

    }
    private fun Pagination() {
        //***********************pagination**********************//
        recyclerEarnedShoppinList.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPagin.visibility = View.VISIBLE
                page++
                //************get notification api
                mvmodel.earned_by_listing(page.toString())
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems
            }
        })
    }
}

