package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForAllList

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.caiguru.buyer.chooseSellers.chooseSellerPlaceOrder.ChooseSellerPlaceOrderActivity
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.databinding.ActivityPayCreditsMultipleListBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSetting.sellerBuyCredits.SellerBuyCreditsActivity
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.show_credits_dialog.*
import java.lang.Exception
import java.util.ArrayList

class PayCreditsMultipleListActivity : AppCompatActivity() {
    private var PartialComissions: Double = 0.0
    private var homeDeliveryaddress = DeliveryZoneModel()
    private var arrayModel = ArrayList<ChooseSellerShoppingModel>()
    private lateinit var dialog: Dialog
    private lateinit var creditsPayAdapter: PayCreditsForMultipleListAdapter
    private lateinit var mbinding: ActivityPayCreditsMultipleListBinding
    private lateinit var mvmodel: PayCreditsMultipleListViewModel

    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_credits_multiple_list)
       // mvmodel = ViewModelProviders.of(this)[PayCreditsMultipleListViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(PayCreditsMultipleListViewModel::class.java)

        SettingUpToolbar()
        setAdapter()

        arrayModel =
            intent.getParcelableArrayListExtra<ChooseSellerShoppingModel>("keyshoppingData")!!
        val allDataJsonFormat = intent.getStringExtra("totalCredits")!!
        homeDeliveryaddress = intent.getParcelableExtra<DeliveryZoneModel>("homeDeliveryaddress")!!

//        val gson = Gson()
//        val jsonconvertModel: ArrayList<ChooseSellerShoppingModel> =
//            gson.fromJson(
//                allDataJsonFormat,
//                object : TypeToken<ArrayList<ChooseSellerShoppingModel>>() {}.type
//            )

        try {
//            for (i in 0 until arrayModel.size) {
//
//                var total = arrayModel[i].total
//                if (total.isEmpty()) {
//                    Toast.makeText(this, getString(R.string.Total_Empty), Toast.LENGTH_SHORT).show()
//                } else {
//                    var comission_per = arrayModel[i].comission_per.toDouble()
//                    result += total.toDouble() * comission_per / 100
//                }
//            }

            PartialComissions = 0.0
            for (i in 0 until arrayModel.size) {

                val total = arrayModel[i].PartialComission
                if (total.isEmpty()) {
                   // Toast.makeText(this, getString(R.string.Total_Empty), Toast.LENGTH_SHORT).show()
                    Constant.showToast(getString(R.string.Total_Empty),this)
                } else {
                    PartialComissions += arrayModel[i].PartialComission.toDouble()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        mbinding.totalcreditstxt.text =
            "${getString(R.string.You_need_to_pay)} ${Constant.roundValue(PartialComissions)} ${getString(R.string.credits_to_finish_this_order)}"

        mbinding.btnfinish.text =
            "${getString(R.string.Pay)}  ${Constant.roundValue(PartialComissions)}  ${getString(R.string.credits)}"

//set the click
        //*******************set the observer*******************//
        mbinding.btnfinish.setOnClickListener {
            var model = GetProfileModel()
            val gson = Gson()
            val json = Constant.getPrefs(this).getString("profile", "")
            model = gson.fromJson(json, GetProfileModel::class.java)
            val credits = model.credits.toDouble()

//            if (credits < 0) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Your_do_not_have_enough_credits),
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else

                if (credits < PartialComissions) {
                creditDialog()
            } else {
                mvmodel.setSingleListCredits(allDataJsonFormat, arrayModel, homeDeliveryaddress,PartialComissions)
                mbinding.btnfinish.visibility=View.INVISIBLE
                mbinding.btnWAIT.visibility=View.VISIBLE
            }

        }

        //*******************set the observer*******************//
        //sucessful
        mvmodel.mSucessfulSingleListData().observe(this, Observer {
            mbinding.btnfinish.visibility=View.VISIBLE
            mbinding.btnWAIT.visibility=View.INVISIBLE
            val intent = Intent(this, ChooseSellerPlaceOrderActivity::class.java)
            intent.putExtra("keySellerDetailMultipleSide", it)
            intent.putExtra("keyshoppingData", arrayModel)
            intent.putExtra("multiple", "yes")
            startActivity(intent)
            finish()

        })

        //failure
        mvmodel.mFailureSingleListData().observe(this, Observer {
            mbinding.btnfinish.visibility=View.VISIBLE
            mbinding.btnWAIT.visibility=View.INVISIBLE
         //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
        })


        try {
            for (i in 0 until arrayModel.size) {

                creditsPayAdapter.Update(arrayModel, homeDeliveryaddress)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onResume() {
        super.onResume()
        val isFinish = Constant.getPrefs(this).getString("finish", "no")
        if (isFinish == "yes") {
            finish()
        }

    }


    private fun creditDialog() {

//        var model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//        model = gson.fromJson(json, GetProfileModel::class.java)
//        val credits = model.credits.toDouble()

//        result = 0.0
//        try {
//
//            for (i in 0 until arrayModel.size) {
//
//                var total = arrayModel[i].PartialComission
//                if (total.isEmpty()) {
//                    Toast.makeText(this, getString(R.string.Total_Empty), Toast.LENGTH_SHORT).show()
//                } else {
//                   // var comission_per = arrayModel[i].PartialComission.toDouble()
//                    result += arrayModel[i].PartialComission.toDouble()
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        val newCredits = PartialComissions - Constant.getProfileData(this).credits.toDouble()

        dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.show_credits_dialog)
        dialog.show()

        dialog.totalCredit.text =
            "${getString(R.string.you_dont_have_credits)}  ${Constant.roundValue((newCredits))}  ${getString(R.string.additional_credits)}"
        //set the click on  button
        dialog.buyCredit.setOnClickListener {

            val intent = Intent(this, SellerBuyCreditsActivity::class.java)
            intent.putExtra("profileKey", Constant.getProfileData(this))
            startActivity(intent)
            dialog.dismiss()
        }


    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Pay_Credits)
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

    //close list parent adapter
    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbinding.recycler.layoutManager = manager
        creditsPayAdapter = PayCreditsForMultipleListAdapter(this)
        mbinding.recycler.adapter = creditsPayAdapter
    }
}


