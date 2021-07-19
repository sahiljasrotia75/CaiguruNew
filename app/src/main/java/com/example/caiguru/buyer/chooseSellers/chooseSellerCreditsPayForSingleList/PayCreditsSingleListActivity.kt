package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList

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
import com.example.caiguru.databinding.ActivityPayCreditsSingleListBinding
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.sellerSetting.sellerBuyCredits.SellerBuyCreditsActivity
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.show_credits_dialog.*
import java.lang.Exception

class PayCreditsSingleListActivity : AppCompatActivity() {
    private lateinit var payCreditsAdapter: PayCreditSingleListDeliveryDetailsAdapter
    private var homeDeliveryaddres = DeliveryZoneModel()
    private lateinit var total_credits: String
    private var arrayModel = ChooseSellerShoppingModel()
    private lateinit var dialog: Dialog
    private lateinit var mvmodel: PayCreditsSingleListViewModel
    private lateinit var mBinding: ActivityPayCreditsSingleListBinding
    private lateinit var text: TextView
    private var deliveryType: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_credits_single_list)
        mvmodel = ViewModelProvider(this).get(PayCreditsSingleListViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        arrayModel = intent.getParcelableExtra<ChooseSellerShoppingModel>("keydata")!!
        homeDeliveryaddres = intent.getParcelableExtra<DeliveryZoneModel>("homeDeliveryaddres")!!
        val jsonStringData = intent.getStringExtra("jsonStr")!!


//convert  ths json data in model
        //  [{"products":[{"id":"349","qty":"5"}],"list_id":"185","delivery_type":2,"amount":"200.0","credits":"2.0"}]
//        val gson = Gson()
//        val jsonconvertModel: ArrayList<ChooseSellerShoppingModel> =
//            gson.fromJson(
//                jsonStringData,
//                object : TypeToken<ArrayList<ChooseSellerShoppingModel>>() {}.type
//            )

        total_credits =  arrayModel.PartialComission

        mBinding.btnPayCredits.text =
            getString(R.string.Pay) + " " + arrayModel.PartialComission + " " + getString(
                R.string.credits
            )

        mBinding.belowHeadingText.text =
            getString(R.string.You_need_to_pay) + " " + arrayModel.PartialComission + " " + getString(
                R.string.credits_to_finish_this_order
            )
       // mBinding.txtPrice.text = "$" + Constant.roundValue(arrayModel.total.toDouble())
        mBinding.txtPrice.text = "$" + arrayModel.cashOnDelivery

        try {
            val datetimeAraay = ArrayList<String>()
            if (arrayModel.delivery_type == 1) {
                mBinding.headdeliveyDays.text = getString(R.string.Pickup_days)
                mBinding.headdeliveyAddress.text = getString(R.string.Pickup_Distance)
                mBinding.txtCashOnDelivery.text = getString(R.string.Payment_amount_on_pickup)
                // deliveryType = "Self Pickup"
                deliveryType = getString(R.string.Self_Pickup)
                mBinding.txtdeliverytype.text = getString(R.string.Pickup_at_seller_address)
                //show only self pickup time
                if (arrayModel.pickup_details.address.isNotEmpty()) {
                    datetimeAraay.addAll(updatedDayTime(arrayModel.pickup_details.days))
                    payCreditsAdapter.Update(datetimeAraay)
                    //set the address
                }

                mBinding.txtdeliveryAddress.text =
                    "(" + arrayModel.distance + "${getString(R.string.aways)})"

            } else {

                mBinding.headdeliveyDays.text = getString(R.string.Delivery_Days)
                mBinding.headdeliveyAddress.text = getString(R.string.Delivery_Address)
                mBinding.txtCashOnDelivery.text = getString(R.string.Cash_on_Delivery)
                deliveryType = getString(R.string.Home_Delivery)
                mBinding.txtdeliverytype.text = deliveryType
                //show only delivery details time
                datetimeAraay.addAll(updatedDayTime(arrayModel.delivery_daytime))
                payCreditsAdapter.Update(datetimeAraay)
                //set the address
                mBinding.txtdeliveryAddress.text = homeDeliveryaddres.address

            }
            mBinding.listingName.text = arrayModel.listingname
        } catch (e: Exception) {
            e.printStackTrace()
        }




//**********************buy listing Api*********************//
        //set the click of pay credits
        mBinding.btnPayCredits.setOnClickListener {
//            var model = GetProfileModel()
//            val gson = Gson()
//            val json = Constant.getPrefs(this).getString("profile", "")
//            model = gson.fromJson(json, GetProfileModel::class.java)

           // val credits = model.credits.toDouble()

//            val myPurchseCredit =
//                (arrayModel.total.toDouble() * arrayModel.comission_per.toDouble() / 100)

//            if (credits < 0) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.Your_do_not_have_enough_credits),
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            } else

                if (Constant.getProfileData(this).credits.toDouble() < total_credits.toDouble()) {

                creditDialog()
//                Toast.makeText(
//                    this,
//                    getString(R.string.You_did_not_have_credit) + " " + credits,
//                    Toast.LENGTH_SHORT
//                ).show()
            } else {
                mvmodel.setSingleListCredits(
                    jsonStringData,
                    arrayModel,
                    total_credits,
                    homeDeliveryaddres

                )
                mBinding.btnWait.visibility = View.VISIBLE
                mBinding.btnPayCredits.visibility = View.INVISIBLE
            }
            // mvmodel.setSingleListCredits(jsonStringData, arrayModel, total_credits, total_price)

        }
        //*******************set the observer*******************//
        //sucessful
        mvmodel.mSucessfulSingleListData().observe(this, Observer {
            mBinding.btnWait.visibility = View.INVISIBLE
            mBinding.btnPayCredits.visibility = View.VISIBLE
            val intent = Intent(this, ChooseSellerPlaceOrderActivity::class.java)
            intent.putExtra("keycreditData", arrayModel)
            intent.putExtra("keySellerDetails", it)
            intent.putExtra("multiple", "no")
            startActivity(intent)
            finish()

        })

        //failure
        mvmodel.mFailureSingleListData().observe(this, Observer {
            mBinding.btnWait.visibility = View.INVISIBLE
            mBinding.btnPayCredits.visibility = View.VISIBLE
            Constant.showToast(it,this)
        })
    }

    override fun onResume() {
        super.onResume()
        val isFinish = Constant.getPrefs(this).getString("finish", "no")
        if (isFinish == "yes") {
            finish()
        }

    }


    private fun creditDialog() {
        val newCredits = total_credits.toDouble() - Constant.getProfileData(this).credits.toDouble()
        dialog = Dialog(this)
        //    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.show_credits_dialog)
        dialog.show()

        dialog.totalCredit.text =
            "${getString(R.string.you_dont_have_credits)}  ${Constant.roundValue((newCredits.toDouble()))}  ${getString(
                R.string.additional_credits
            )}"
        //set the click on  button
        dialog.buyCredit.setOnClickListener {
            val intent = Intent(this, SellerBuyCreditsActivity::class.java)
            intent.putExtra("profileKey", Constant.getProfileData(this))
            startActivity(intent)
            dialog.dismiss()
        }

    }


    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: List<DaysParentModel>): ArrayList<String> {

        val daysArray = ArrayList<String>()
        for (i in 0 until daysArraymodel.size) {

            val day = Constant.getDayString(this, daysArraymodel[i].day)
            var value = ""
            for (j in 0 until daysArraymodel[i].value.size) {
                if (value.isEmpty()) {
                    value = Constant.ConvertAmPmFormat(
                        this,
                        daysArraymodel[i].value[j].from
                    ) + "-" + Constant.ConvertAmPmFormat(this, daysArraymodel[i].value[j].to)
                } else {
                    value =
                        value + "," + Constant.ConvertAmPmFormat(
                            this,
                            daysArraymodel[i].value[j].from
                        ) + "-" + Constant.ConvertAmPmFormat(this, daysArraymodel[i].value[j].to)
                }
            }
            daysArray.add(day + " (" + value + ")")
        }

        return daysArray
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

    fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mBinding.recycleerTime.layoutManager = manager
        payCreditsAdapter = PayCreditSingleListDeliveryDetailsAdapter(this)
        mBinding.recycleerTime.adapter = payCreditsAdapter
    }
}
