package com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.databinding.ActivitySellerApprovalOrderListBinding
import com.example.caiguru.seller.homeSeller.HomeSellerModel
import com.example.caiguru.seller.sellerOrder.orderRejected.OrderRejectedActivity
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_approval_order_list.*
import kotlinx.android.synthetic.main.activity_seller_approval_order_list.addressLayout
import kotlinx.android.synthetic.main.activity_seller_approval_order_list.txtCashOnDelivery
import kotlinx.android.synthetic.main.activity_seller_approval_order_list.txtHeader2
import kotlinx.android.synthetic.main.activity_seller_store.*
import kotlinx.android.synthetic.main.finalize_approve_reject_details.*

class SellerApprovalOrderListActivity : AppCompatActivity() {

    private var latitude: String = ""
    private var longitude: String = ""
    private var reqID: String = ""
    private var listType: String = ""
    private lateinit var adapterSuggestedProduct: OrderSuggestedProductAdapter
    private lateinit var adapter: ApprovalOrderListParentAdapter
    private var buyerName: String = ""
    private lateinit var text: TextView
    val localModel = SellerApprovalModel()
    private lateinit var mvmodel: ApprovalOrderListViewModel
    private lateinit var mbinding: ActivitySellerApprovalOrderListBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_approval_order_list)
        mvmodel = ViewModelProviders.of(this)[ApprovalOrderListViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (intent.hasExtra("keyHomeFragmentData")) {
            val homeSellerModel = intent.getParcelableExtra<HomeSellerModel>("keyHomeFragmentData")!!
            listType = homeSellerModel.list_type
            reqID = homeSellerModel.req_id
            buyerName = homeSellerModel.name
            localModel.buyer_id = homeSellerModel.buyer_id
            localModel.buyer_name = homeSellerModel.name
            localModel.list_type = homeSellerModel.list_type
            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${Constant.timesAgoLogic(
                    homeSellerModel.created_at,
                    this
                )}."
            val list_type = homeSellerModel.list_type
            btnHideApproveReject(list_type)
        }
        else if (intent.hasExtra("keyBuyerData")) {
            val sellerDataModel = intent.getParcelableExtra<SellerApprovalModel>("keyBuyerData")!!
            buyerName = sellerDataModel.buyer_name
            listType = sellerDataModel.list_type
            reqID = sellerDataModel.id
            localModel.buyer_id = sellerDataModel.buyer_id
            localModel.buyer_name = sellerDataModel.buyer_name
            localModel.list_type = sellerDataModel.list_type
            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${Constant.timesAgoLogic(
                    sellerDataModel.created_at,
                    this
                )}."
            //hide the button in case of buyer list
            val list_type = sellerDataModel.list_type
            btnHideApproveReject(list_type)
        }
        else if (intent.hasExtra("KeySourceBuyerSubmitQuote12")) {
            val dataModel =
                intent.getParcelableExtra<NotificationModel>("KeySourceBuyerSubmitQuote12")!!
            reqID = dataModel.source_id
            listType = dataModel.list_type
            buyerName = dataModel.name
            localModel.buyer_id = dataModel.sender_id
            localModel.buyer_name = dataModel.name
            localModel.list_type = dataModel.list_type
            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${Constant.timesAgoLogic(
                    dataModel.created_at,
                    this
                )}."
//hide the button
            btnHideApproveRejectActionTaken(dataModel.action_taken)
            //Read notification
            mvmodel.notificationRead(dataModel.notification_id)
        }
        else {
            reqID = intent.getStringExtra("source_id")!!
            listType = intent.getStringExtra("list_type")!!
            val source = intent.getStringExtra("source")!!
            buyerName = intent.getStringExtra("name")!!
            val image = intent.getStringExtra("image")!!
            val level = intent.getStringExtra("level")!!
            val listingname = intent.getStringExtra("listingname")!!
            val reputation = intent.getStringExtra("reputation")!!
            val created_at = intent.getStringExtra("created_at")!!
            val sender_id = intent.getStringExtra("sender_id")!!
            localModel.buyer_id = sender_id
            localModel.buyer_name = buyerName
            localModel.list_type = listType

            btnHideApproveReject(listType)
            minutesAgoList.text =
                "${getString(R.string.Purchased_Order)} ${Constant.timesAgoLogic(
                    created_at,
                    this
                )}."
        }
        spannableTextHeader(localModel.buyer_name)
        sellerActiveStatus()
        SettingUpToolbar()
        setAdapter()
        suggestedProductAdapter()
        //******************Api get_request_detail*******************//
        mvmodel.getrequestDetails(reqID, listType)

        //********************observer of get_request_detail***********//
        //sucessful
        mvmodel.mSucessfulOrderList().observe(this, Observer {
            try {


                mbinding.nestedScrollLayout.visibility = View.VISIBLE
                mbinding.progressedBar.visibility = View.GONE
                adapter.updateData(it)//parent adapter
                for (item in it) {
                    mbinding.txtAddressbuyer.text = item.address
                    mbinding.txtComissions.text =
                        "${Constant.roundValue(item.credits.toDouble())} ${getString(R.string.credits)}"
//set the cash on delivery
                    txtCashOnDelivery.text =
                        "$"+Constant.roundValue(item.amount.toDouble() - item.credits.toDouble())

                    if (item.suggest_products.isEmpty()) {
                        mbinding.suggestProducttxt.visibility = View.INVISIBLE
                    } else {
                        mbinding.suggestProducttxt.visibility = View.VISIBLE
                        //suggest product Adpter
                        adapterSuggestedProduct.Update(item.suggest_products)
                    }
                    if (item.delivery_type == "1") {
                        mbinding.headingAddress.text =
                            getString(R.string.The_order_will_be_picked_up_at_your_address)
                    } else {
                        mbinding.headingAddress.text = getString(R.string.Delivery_Address)
                    }
                    latitude = item.lat
                    longitude = item.long

                    spannableTxtCash( Constant.roundValue(item.amount.toDouble() - item.credits.toDouble()))
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        //failure
        mvmodel.mFailure().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            mbinding.noData.visibility = View.VISIBLE
            mbinding.progressedBar.visibility = View.VISIBLE
            mbinding.nestedScrollLayout.visibility = View.INVISIBLE
        })

        //*****************api of change_request_status***************//
        //set the click on the button request approval
        mbinding.btnApproved.setOnClickListener {
            mvmodel.getRequestApprovals(reqID, listType)
            mbinding.progressedBar.visibility = View.VISIBLE
            mbinding.btnlayout.visibility = View.GONE

        }
        //****************set the observer of approval
        mvmodel.mSucessfulRequestApprovals().observe(this, Observer {
            alertDialog(it)
            mbinding.progressedBar.visibility = View.GONE
            mbinding.btnlayout.visibility = View.VISIBLE
        })
        //failure
        mvmodel.mFailureRequestApprovals().observe(this, Observer {
            alertDialog(it)
            mbinding.btnlayout.visibility = View.VISIBLE
            mbinding.progressedBar.visibility = View.GONE
        })


        //*****************api of change_request_status***************//
        //set the click on the rejected list
        mbinding.btnRejected.setOnClickListener {
            val intent = Intent(this, OrderRejectedActivity::class.java)
            intent.putExtra("model", reqID)
            startActivityForResult(intent, 100)
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }
//        //*********set the obaserver of rejected list
//        mvmodel.mSucessfulRejectedRequest().observe(this, Observer {
//
//            alertDialogNo(it)
//            mbinding.progressedBar.visibility = View.GONE
//            mbinding.btnlayout.visibility = View.VISIBLE
//
//        })
//        mvmodel.mFailureRejectedRequest().observe(this, Observer {
//            alertDialog(it)
//            mbinding.progressedBar.visibility = View.GONE
//            mbinding.btnlayout.visibility = View.VISIBLE
//
//        })
        // set Marker click
        setClicks()
    }

    private fun spannableTxtCash(roundValue: String) {

        val startTxt=getString(R.string.you_must_charge)
        val middleTxt="$"+roundValue
        val endTxt=getString(R.string.to_your_client)
        val allText = startTxt + " " + middleTxt + " " + endTxt
        val spannable = SpannableString(allText)
        val boldSpan = StyleSpan(Typeface.BOLD)


        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
           0,
            startTxt.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            middleTxt.length,
            endTxt.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            startTxt.length ,
            allText.length - endTxt.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan,
            startTxt.length ,
            allText.length - endTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        txtYouMustChargeTxt.setText(spannable, TextView.BufferType.SPANNABLE)

        titleCashOnDelivery.setOnClickListener {
            val mDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            mDialog.setTitle(getString(R.string.Cash_on_Deliverys))
            mDialog.setMessage(getString(R.string.cash_on_delivery_txt_seller_side))
            mDialog.setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                dialog.cancel()

            }
            mDialog.show()
        }
    }

    private fun spannableTextHeader(name: String) {
        val textStart = getString(R.string.textHeader1)
        val textMiddle = name+"."
        val textEnd = getString(R.string.text_header2)

        val allText = textStart + "  " + textMiddle + " " + textEnd
        val spannable = SpannableString(allText)
        val boldSpan = StyleSpan(Typeface.BOLD)

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            textMiddle.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            textMiddle.length,
            textEnd.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            textStart.length ,
            allText.length - textEnd.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan,
            textStart.length ,
            allText.length - textEnd.length ,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        txtHeader2.setText(spannable, TextView.BufferType.SPANNABLE)

    }
    private fun setClicks() {
        addressLayout.setOnClickListener {
            if (mbinding.txtAddressbuyer.text.isNotEmpty()&& latitude.isNotEmpty()) {
//                val latLng = Constant.getLocationFromAddress(this, txtAddressbuyer.text as String?)
                val gmmIntentUri: Uri =
                    Uri.parse("google.navigation:q=" + latitude + "," + longitude)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
                return@setOnClickListener
            } else {
                Toast.makeText(this, getString(R.string.no_address_found), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }
    }


    private fun btnHideApproveRejectActionTaken(actionTaken: String) {
        if (actionTaken == "1") {
            btnlayout.visibility = View.VISIBLE
        } else {
            btnlayout.visibility = View.INVISIBLE
        }

    }

    private fun btnHideApproveReject(list_type: String) {

        if (list_type == "2") {
            btnlayout.visibility = View.INVISIBLE
        } else {
            btnlayout.visibility = View.VISIBLE
        }
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

    //Alert dialog
    private fun alertDialog(it: String) {
     //   val nextStepTxt=getString(R.string.next_step_txt)
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setCancelable(false)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->

            dialog.dismiss()
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }
        mDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)//set the result
        finish()
    }

    //Alert dialog
    private fun alertDialogNo(it: String) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setPositiveButton(
            getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                var intent = Intent(this, OrderRejectedActivity::class.java)
                startActivity(intent)
                //    setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            })
        mDialog.show()

    }

    //.........setuptool bar..............//
    @SuppressLint("SetTextI18n")
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        // text.text = buyerName + "'s" + " " + getString(R.string.Order_List)
        text.text = buyerName
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)


//        text.setOnClickListener {
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keydata", localModel)
//            startActivity(intent)
//        }
//        data = intent.getParcelableExtra<SellerApprovalModel>("keydata")
//        buyer_name = data.buyer_name
//        btnFollowUnfollow.visibility=View.VISIBLE
//        if (data.list_type == "1") {
//            usertype = "2"
//        } else {
//            usertype = "1"
//        }
    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                val intent = Intent()
                setResult(Activity.RESULT_CANCELED, intent)//set the result
                finish()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 100 && resultCode == RESULT_OK) {
            finish()
        }
    }

    //listAdapter
    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = ApprovalOrderListParentAdapter(this)
        mbinding.recyclerOrderList.layoutManager = manager
        mbinding.recyclerOrderList.adapter = adapter
    }

    //suggestedProductAdapter
    private fun suggestedProductAdapter() {

        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterSuggestedProduct = OrderSuggestedProductAdapter(this)
        mbinding.suggestedRecycler.layoutManager = manager
        mbinding.suggestedRecycler.adapter = adapterSuggestedProduct

    }


}
