package com.example.caiguru.seller.sellerSetting.sellerBuyCredits

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.*
import com.example.caiguru.R
import com.example.caiguru.commonScreens.referralCode.ReferralCodeActivity
import com.example.caiguru.databinding.ActivitySellerBuyCreditsBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.mercadopago.android.px.core.MercadoPagoCheckout
import com.mercadopago.android.px.model.Payment
import com.mercadopago.android.px.model.exceptions.MercadoPagoError
import constant_Webservices.Constant
import org.json.JSONObject
import java.lang.Exception


class SellerBuyCreditsActivity : AppCompatActivity(), BuyCreditsAdapter.GetCreditsPackInterface,
    PurchasesUpdatedListener {
    private lateinit var referalText: ImageView
    private var creditCost: Double = 0.0
    private var creditPosition: Int = 0
    private var paymentType: Int = 0
    private var profileData = GetProfileModel()
    private var globalCreditsModel = BuyCreditsModel()
    private lateinit var text: TextView
    private var hasSelected: Boolean = true
    private var hasSelected1: Boolean = true
    private lateinit var mvmodel: SellerBuyCreditsViewModel
    private lateinit var mbinding: ActivitySellerBuyCreditsBinding
    lateinit var mcreditAdapter: BuyCreditsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra("profileKey")) {
            profileData = intent.getParcelableExtra<GetProfileModel>("profileKey")!!
        }
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_buy_credits)
        //    mvmodel = ViewModelProviders.of(this)[SellerBuyCreditsViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(SellerBuyCreditsViewModel::class.java)
        SettingUpToolbar()
        setAdapters()
        //    mvmodel.getCredits()
        paymentType = 1
        hasSelected = true
        hasSelected1 = false
        mbinding.mercadoPaymentImg.setImageResource(R.drawable.ic_mercado_payment_selected)
        mbinding.mercadoPaymentImg.setBackgroundDrawable(this.getDrawable(R.drawable.circle_background_purple))
        mcreditAdapter.update(getCreditsMerCando())
        //*********set the credits
        if (profileData.credits.isNotEmpty()) {
            mbinding.totalCredits.text =
                ((Constant.roundValue(profileData.credits.toDouble())) + getString(R.string.cr))
        }

//***************set the observer******************//
//        mvmodel.setData().observe(this, Observer {
//            mcreditAdapter.update(it)
//            for (items in it) {
//                if (items.hasselected) {
//                    globalCreditsModel = items
//                }
//            }
//        })

        //set the click on payment image
        mbinding.mercadoPaymentImg.setOnClickListener {
            mcreditAdapter.update(getCreditsMerCando())
            if (hasSelected) {
                paymentType = 1
                hasSelected = true
                mbinding.mercadoPaymentImg.setImageResource(R.drawable.ic_mercado_payment_selected)
                mbinding.mercadoPaymentImg.setBackgroundDrawable(this.getDrawable(R.drawable.circle_background_purple))

                hasSelected1 = false
                mbinding.androidPaymetImg.setImageResource(R.drawable.ic_android_unselected)
                mbinding.androidPaymetImg.setBackgroundResource(R.drawable.circle_background_grey)

            } else {
                paymentType = 1
                hasSelected = true
                mbinding.mercadoPaymentImg.setImageResource(R.drawable.ic_mercado_payment_selected)
                mbinding.mercadoPaymentImg.setBackgroundDrawable(this.getDrawable(R.drawable.circle_background_purple))

                hasSelected1 = false
                mbinding.androidPaymetImg.setImageResource(R.drawable.ic_android_unselected)
                mbinding.androidPaymetImg.setBackgroundResource(R.drawable.circle_background_grey)
            }
        }

        //android  payment image
        mbinding.androidPaymetImg.setOnClickListener {
            mcreditAdapter.update(getCreditsAndroid())
            if (hasSelected1) {
                paymentType = 2
                hasSelected1 = true
                mbinding.androidPaymetImg.setImageResource(R.drawable.ic_android_selected)
                mbinding.androidPaymetImg.setBackgroundDrawable(this.getDrawable(R.drawable.circle_background_purple))

                hasSelected = false
                mbinding.mercadoPaymentImg.setImageResource(R.drawable.ic_mercado_payment_unselected)
                mbinding.mercadoPaymentImg.setBackgroundResource(R.drawable.circle_background_grey)
            } else {
                paymentType = 2
                hasSelected1 = true
                mbinding.androidPaymetImg.setImageResource(R.drawable.ic_android_selected)
                mbinding.androidPaymetImg.setBackgroundDrawable(this.getDrawable(R.drawable.circle_background_purple))

                hasSelected = false
                mbinding.mercadoPaymentImg.setImageResource(R.drawable.ic_mercado_payment_unselected)
                mbinding.mercadoPaymentImg.setBackgroundResource(R.drawable.circle_background_grey)
            }
        }


        //********************api of update wallet******************//
        mbinding.btncontinue.setOnClickListener {

            if ((!hasSelected && !hasSelected1) || (hasSelected && hasSelected1)) {
                Toast.makeText(
                    this,
                    getString(R.string.please_select_payment_option),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (creditPosition == -1) {
                Toast.makeText(this, getString(R.string.please_select_credits), Toast.LENGTH_SHORT)
                    .show()
            } else {
                selectPaymentType()
            }
        }

        //*****************response of udate wallet****************//
        //sucesful
        mvmodel.mSucessfullBuyCreditsPacks().observe(this, Observer {
            // Toast.makeText(this, it.mseeage, Toast.LENGTH_SHORT).show()
            showErrorDialog(it.mseeage)
            profileData.credits = it.credits
            if (it.credits.isNotEmpty()) {
                mbinding.totalCredits.text =
                    ((Constant.roundValue(it.credits.toDouble())) + getString(R.string.cr))
            }
            //update the shared preferences
            val prefs = Constant.getPrefs(application)
            val json = JSONObject(prefs.getString(Constant.PROFILE, ""))
            json.put("credits", it.credits)
            val edit = prefs.edit()
            edit.putString(Constant.PROFILE, json.toString())
            edit.apply()
            mbinding.btnwaits.visibility = View.GONE
            mbinding.btncontinue.visibility = View.VISIBLE

        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            mbinding.btnwaits.visibility = View.GONE
            mbinding.btncontinue.visibility = View.VISIBLE
            showErrorDialog(it)
        })
        mvmodel.mSuccessMercado().observe(this, Observer {
            MercadoPagoCheckout.Builder(Constant.PUBLIC_KEY, it)
                .build()
                .startPayment(this, 112)
            mbinding.btnwaits.visibility = View.GONE
            mbinding.btncontinue.visibility = View.VISIBLE
        })
    }

    //error dialog case of failure
    private fun showErrorDialog(it: String) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == 112) {
                if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                    val payment =
                        data!!.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT) as Payment
                    val allTransectionData = GetAllDataOfPaymentInJsonFormat(payment)
                    Log.d("check", payment.paymentStatus)

                    mvmodel.getBuyCreditsPack(globalCreditsModel, allTransectionData)
                    mbinding.btnwaits.visibility = View.VISIBLE
                    mbinding.btncontinue.visibility = View.GONE
                    //  ((TextView) findViewById(R.id.mp_results)).setText("Resultado del pago: " + payment.getStatus());
                    //Done!
                } else if (resultCode == RESULT_CANCELED) {
                    if (data != null && data.getExtras() != null
                        && data.getExtras()!!.containsKey(MercadoPagoCheckout.EXTRA_ERROR)
                    ) {
                        val mercadoPagoError =
                            data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR) as MercadoPagoError

                        mbinding.btnwaits.visibility = View.GONE
                        mbinding.btncontinue.visibility = View.VISIBLE

                        showErrorDialog(mercadoPagoError.message)


                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun GetAllDataOfPaymentInJsonFormat(payment: Payment): String {
        val jsonobj = JSONObject()
        try {
            jsonobj.put("card", payment.card)
            //  jsonobj.put("binaryMode", payment.binaryMode)
            jsonobj.put("callForAuthorizeId", payment.callForAuthorizeId)
            // jsonobj.put("captured", payment.captured)
            jsonobj.put("collectorId", payment.collectorId)
            jsonobj.put("couponAmount", payment.couponAmount)
            jsonobj.put("currencyId", payment.currencyId)
            jsonobj.put("dateApproved", payment.dateApproved)
            jsonobj.put("dateCreated", payment.dateCreated)
            jsonobj.put("dateLastUpdated", payment.dateLastUpdated)
            jsonobj.put("description", payment.description)
            //  jsonobj.put("differentialPricingId", payment.differentialPricingId)
            jsonobj.put("externalReference", payment.externalReference)
            jsonobj.put("feeDetails", payment.feeDetails)
            jsonobj.put("id", payment.id)
            // jsonobj.put("installments", payment.installments)
            jsonobj.put("issuerId", payment.issuerId)
            //  jsonobj.put("liveMode", payment.liveMode)
            jsonobj.put("moneyReleaseDate", payment.moneyReleaseDate)
            jsonobj.put("notificationUrl", payment.notificationUrl)
            jsonobj.put("operationType", payment.operationType)
            jsonobj.put("order", payment.order)
            jsonobj.put("payer", payment.payer)
            jsonobj.put("paymentMethodId", payment.paymentMethodId)
            jsonobj.put("paymentTypeId", payment.paymentTypeId)
            jsonobj.put("refunds", payment.refunds)
            jsonobj.put("statementDescriptor", payment.statementDescriptor)
            jsonobj.put("status", payment.paymentStatus)
            jsonobj.put("paymentStatusDetail", payment.paymentStatusDetail)
            jsonobj.put("transactionAmount", payment.transactionAmount)
            jsonobj.put("transactionAmountRefunded", payment.transactionAmountRefunded)
            jsonobj.put("transactionDetails", payment.transactionDetails)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonobj.toString()
    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        referalText = findViewById(R.id.refferal)
        referalText.visibility = View.VISIBLE
        text.text = getText(R.string.Buy_Credits)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

//*************referal text click
        referalText.setOnClickListener {
            val intent = Intent(this@SellerBuyCreditsActivity, ReferralCodeActivity::class.java)
            startActivity(intent)
        }

    }


    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                val intent = Intent()
                intent.putExtra("keyTotalCredits", profileData)
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //on back pressed
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)//set the result
        intent.putExtra("keyTotalCredits", profileData)
        finish()
    }

    fun setAdapters() {
        val layout = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mbinding.recyclercredits.layoutManager = layout
        mcreditAdapter = BuyCreditsAdapter(this)
        mbinding.recyclercredits.adapter = mcreditAdapter
    }

    //get the credits pack by the adapter click
    override fun getCreditsPack(
        data: BuyCreditsModel,
        position: Int
    ) {
        globalCreditsModel = data

        if (data.hasselected) {
            creditPosition = position
        } else {
            creditPosition = -1
        }
    }

    private fun selectPaymentType() {
        if (paymentType == 1) {
            mercadoPagoPurchase()
        } else if (paymentType == 2) {
            callInAppPurchase()
        }
    }

    private fun mercadoPagoPurchase() {
        if (creditPosition == 0) {
            creditCost = 100.0
        } else if (creditPosition == 1) {
            creditCost = 200.0
        } else if (creditPosition == 2) {
            creditCost = 500.0
        } else {
            creditCost = 1000.0
        }

        startMercadoPagoCheckout()

    }


    private fun startMercadoPagoCheckout() {


//        MercadoPagoCheckout.Builder()
//            .setActivity(this)
//            .setPublicKey(Constant.PUBLIC_KEY)
//            .setCheckoutPreference(checkoutPreference)
//            .startForPaymentData()

        mvmodel.mercadopago(creditCost)
        mbinding.btnwaits.visibility = View.VISIBLE
        mbinding.btncontinue.visibility = View.GONE
    }


    private fun callInAppPurchase() {
        val client = Constant.getBillingClient(this)
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    val skuList = ArrayList<String>()
                    if (creditPosition == 0) {
                        skuList.add("caiguru.buyerseller")
                    } else if (creditPosition == 1) {
                        skuList.add("caiguru_buyerseller_200")
                    } else if (creditPosition == 2) {
                        skuList.add("caiguru_buyerseller_500")
                    } else {
                        skuList.add("caiguru_buyerseller_1000")
                    }
                    val params = SkuDetailsParams.newBuilder()
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
                    client.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
                        // Process the result.
                        Log.i("SKU Details: ", skuDetailsList[0].originalJson)
                        val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()
                        client.launchBillingFlow(this@SellerBuyCreditsActivity, flowParams)
                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }


    override fun onPurchasesUpdated(
        billingResult: BillingResult?,
        purchases: MutableList<Purchase>?
    ) {

        //if (billingResult!!.responseCode == 7){

        if (billingResult!!.responseCode == BillingClient.BillingResponseCode.OK) {
            for (purchase in purchases!!) {
                handlePurchase(purchase, billingResult)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, getString(R.string.Purchased_failed), Toast.LENGTH_SHORT).show()
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show()
            // Handle any other error codes.
        }


        //Toast.makeText(this, billingResult!!.debugMessage, Toast.LENGTH_LONG).show()
        //}
        //Log.i("Purchased: ", billingResult!!.debugMessage)

    }

    private fun handlePurchase(
        purchase: Purchase,
        billingResult: BillingResult
    ) {
        val client = Constant.getBillingClient(this)
        val acknowledgePurchaseParams =
            AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

        val acknowledgePurchaseResponseListener =
            AcknowledgePurchaseResponseListener {
                val allTransectionData = "PlayStore response in app purchase"
                mvmodel.getBuyCreditsPack(globalCreditsModel, allTransectionData)
                mbinding.btnwaits.visibility = View.VISIBLE
                mbinding.btncontinue.visibility = View.GONE
                //   Toast.makeText(this, "Purchase acknowledged", Toast.LENGTH_SHORT).show()
            }

        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .setDeveloperPayload(purchase.developerPayload)
                .build()

        //    val pToken = purchase.purchaseToken

        val consumerListener = ConsumeResponseListener { billingResult, pToken ->
            //Log.d("", "consumeAsync")
        }
        client.consumeAsync(consumeParams, consumerListener)
        client.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener)

    }

    fun getCreditsAndroid(): ArrayList<BuyCreditsModel> {
        val dataArray = ArrayList<BuyCreditsModel>()
        var data = BuyCreditsModel()
        data.credits = "100"
        data.total = "${getString(R.string.peso)}" + " " + "3.99"
        data.totalWithoutText = "3.99"
        data.hasselected = true
        dataArray.add(data)

        data = BuyCreditsModel()
        data.credits = "200"
        data.total = "${getString(R.string.peso)}" + " " + "6.99"
        data.totalWithoutText = "6.99"
        data.hasselected = false
        dataArray.add(data)


        data = BuyCreditsModel()
        data.credits = "500"
        data.total = "${getString(R.string.peso)}" + " " + "16.99"
        data.totalWithoutText = "16.99"
        data.hasselected = false
        dataArray.add(data)

        data = BuyCreditsModel()
        data.credits = "1000"
        data.total = "${getString(R.string.peso)}" + " " + "31.99"
        data.totalWithoutText = "31.99"
        data.hasselected = false
        dataArray.add(data)
        return dataArray
    }

    fun getCreditsMerCando(): ArrayList<BuyCreditsModel> {
        val dataArray = ArrayList<BuyCreditsModel>()
        var data = BuyCreditsModel()
        data.credits = "100"
        data.total = "100" + " " + "${getString(R.string.pesos)}"
        data.totalWithoutText = "100"
        data.hasselected = true
        dataArray.add(data)

        data = BuyCreditsModel()
        data.credits = "200"
        data.total = "200" + " " + "${getString(R.string.pesos)}"
        data.totalWithoutText = "200"
        data.hasselected = false
        dataArray.add(data)


        data = BuyCreditsModel()
        data.credits = "500"
        data.total = "500" + " " + "${getString(R.string.pesos)}"
        data.totalWithoutText = "500"
        data.hasselected = false
        dataArray.add(data)

        data = BuyCreditsModel()
        data.credits = "1000"
        data.total = "1000" + " " + "${getString(R.string.pesos)}"
        data.totalWithoutText = "1000"
        data.hasselected = false
        dataArray.add(data)

        return dataArray
    }

//    override fun onResume() {
//        super.onResume()
//        paymentType = 1
//        hasSelected = false
//        mbinding.mercadoPaymentImg.setImageResource(R.drawable.ic_mercado_payment_selected)
//        mbinding.mercadoPaymentImg.setBackgroundDrawable(this.getDrawable(R.drawable.circle_background_purple))
//        mcreditAdapter.update(getCreditsMerCando())
//
//    }

}
