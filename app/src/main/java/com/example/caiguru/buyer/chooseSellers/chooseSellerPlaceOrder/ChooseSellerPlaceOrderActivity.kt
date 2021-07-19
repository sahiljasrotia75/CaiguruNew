package com.example.caiguru.buyer.chooseSellers.chooseSellerPlaceOrder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.BuyerMyOrderActivity
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerSuggestProduct.ChooseSellerSuggestProductActivity
import com.example.caiguru.databinding.ActivityChooseSellerPlaceOrderBinding
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.toolbar.view.*
import java.util.ArrayList

class ChooseSellerPlaceOrderActivity : AppCompatActivity() {

    private var SellerDetailSingleList = PayCreditsResultModel()
    private var SellerDetailsMultipleList = PayCreditsResultModel()
    private lateinit var parentAdapter: ChooseSellerPlaceOrderParentAdapter
    private var payCreditArrayModelJson = ArrayList<ChooseSellerShoppingModel>()

    private var payCreditArrayModel = ChooseSellerShoppingModel()
    private var payCreditArrayModel1 = ArrayList<ChooseSellerShoppingModel>()
    private lateinit var text: TextView
    private lateinit var mbinding: ActivityChooseSellerPlaceOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_choose_seller_place_order)
        setAdapters()
        SettingUpToolbar()
//        intent.putExtra("keySellerDetailMultipleSide", it)
//        intent.putExtra("keyshoppingData", arrayModel)
//        intent.putExtra("multiple", "yes")
        if (intent.hasExtra("keycreditData")) {

            payCreditArrayModel =
                intent.getParcelableExtra<ChooseSellerShoppingModel>("keycreditData")!!
            SellerDetailSingleList =
                intent.getParcelableExtra<PayCreditsResultModel>("keySellerDetails")!!
            payCreditArrayModel1.add(payCreditArrayModel)
            //set the data in the adapter
            parentAdapter.upDateData(payCreditArrayModel1)

        } else {

            payCreditArrayModelJson =
                intent.getParcelableArrayListExtra<ChooseSellerShoppingModel>("keyshoppingData")!!
             SellerDetailsMultipleList =
                 intent.getParcelableExtra<PayCreditsResultModel>("keySellerDetailMultipleSide")!!
            parentAdapter.upDateData(payCreditArrayModelJson)
        }


//***************suggested prodyuct for next time****************//
        mbinding.btnsuggest.setOnClickListener {

            if (intent.hasExtra("keycreditData")) {
                val intent = Intent(this, ChooseSellerSuggestProductActivity::class.java)
                intent.putExtra("SellerDetailSingleList", SellerDetailSingleList)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, ChooseSellerSuggestProductActivity::class.java)
                intent.putExtra("SellerDetailsMultipleList", SellerDetailsMultipleList)
                startActivity(intent)
                finish()
            }
        }

    }

//    override fun onPause() {
//        super.onPause()
//        finish()
//    }

    override fun onResume() {
        super.onResume()
        val isFinish = Constant.getPrefs(this).getString("finish", "no")
        if (isFinish == "yes") {
            finish()
        }

    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        mbinding.include2.done.visibility = View.VISIBLE
        //set the text
        text = findViewById(R.id.toolbartittle)
        if (intent.hasExtra("keySellerDetails")){
            text.text = getText(R.string.order_placed)

        }else{
            text.text = getText(R.string.Shopping_list_posted)
        }



        mbinding.include2.done.setOnClickListener {
            val intent = Intent(this,BuyerMyOrderActivity::class.java)
            startActivity(intent)
            Constant.getPrefs(this).edit().putString("finish", "yes").apply()
            finish()
        }


    }

    fun setAdapters() {
        val manager = LinearLayoutManager(this)
        mbinding.recyclershopping.layoutManager = manager
        parentAdapter = ChooseSellerPlaceOrderParentAdapter(this)
        mbinding.recyclershopping.adapter = parentAdapter
    }

//    //..........back button click...........//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        if (item != null) {
//            if (item.itemId == android.R.id.home) {
//                finish()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onBackPressed() {
        Constant.getPrefs(this).edit().putString("finish", "yes").apply()
        finish()

        super.onBackPressed()
    }



}

