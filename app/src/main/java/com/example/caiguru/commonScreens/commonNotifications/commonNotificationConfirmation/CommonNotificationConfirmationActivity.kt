package com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.raiseADsiputeBuyerSideNotification.RaiseADisputesActivity
import com.example.caiguru.commonScreens.commonNotifications.rateSeller.RateSellerActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_buyer_notification_confirmation.*
import kotlinx.android.synthetic.main.activity_buyer_order_details.*
import java.lang.Exception

class CommonNotificationConfirmationActivity : AppCompatActivity() {

    private var dummyText: String = ""
    private var data = NotificationModel()
    private lateinit var textData: TextView
    private var allDataGlobal = OrderModel()
    private lateinit var viewModel: NotificationConfirmationViewModel
    private lateinit var mAdapter: CommonNotificationConfirmationsAdapter
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_notification_confirmation)
        viewModel = ViewModelProviders.of(this)[NotificationConfirmationViewModel::class.java]

        SettingUpToolbar()
        setAdapterNotifications()
        initData()
        setObserver()
        setClick()


    }

    private fun setClick() {
        //set the click on the btnConfirmCompletion
        btnConfirmCompletion.setOnClickListener {
            if (intent.hasExtra("KeySourceCancellOrderConfirm")) {
                viewModel.SetAgreeCancelledOrder(data)
                btnConfirmCompletion.text = getString(R.string.pleasewait)
                btnConfirmCompletion.isClickable = false
            } else {
                val intent = Intent(this, RateSellerActivity::class.java)
                intent.putExtra("keyRatingData", allDataGlobal)
                intent.putExtra("keySource4", data)
                startActivityForResult(intent, 100)
            }


        }
        //set the click on the textdisputes
        textdisputes.setOnClickListener {
            val intent = Intent(this, RaiseADisputesActivity::class.java)
            intent.putExtra("keyRatingData", allDataGlobal)
            intent.putExtra("keySource4", data)
            startActivityForResult(intent, 200)
        }
        //open the profile
        relativeImages.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=allDataGlobal.seller_id
            localModel.cat_id=allDataGlobal.cat_id
            localModel.seller_name=allDataGlobal.seller_name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)
//            val model = CustomerChildModel()
//            model.seller_id = allDataGlobal.seller_id
//            model.name = allDataGlobal.seller_name
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", model)
//            startActivity(intent)

        }
        textnames.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=allDataGlobal.seller_id
            localModel.cat_id=allDataGlobal.cat_id
            localModel.seller_name=allDataGlobal.seller_name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)


//            val model = CustomerChildModel()
//            model.seller_id = allDataGlobal.seller_id
//            model.name = allDataGlobal.seller_name
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", model)
//            startActivity(intent)

        }
    }

    private fun setObserver() {
        //get order detail observer
        viewModel.mSucessfulshopListData().observe(this, Observer {
            allDataGlobal = it

            progressPaginations.visibility = View.GONE
            if (it != null) {
                allLayout.visibility = View.VISIBLE
                setAllData(it)
                mAdapter.update(it.products, it.comission_per)
                txtCancelReason.text=getString(R.string.reason)+" "+Constant.setReason(this,it.reason)
            } else {
                allLayout.visibility = View.GONE
                NoDataConfirmation.visibility = View.VISIBLE
            }
        })
        //failure
        viewModel.mFailureShopList().observe(this, Observer {
            allLayout.visibility = View.GONE
            progressPaginations.visibility = View.GONE
            NoDataConfirmation.visibility = View.VISIBLE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })


//cancell order observer
        viewModel.mSucessfulCancellOrder().observe(this, Observer {
            Constant.showToast(it, this)

            btnConfirmCompletion.text = getString(R.string.i_agree)
            finish()
        })

        viewModel.mFailureCancelOrder().observe(this, Observer {
            Constant.showToast(it, this)
            btnConfirmCompletion.text = getString(R.string.i_agree)
            btnConfirmCompletion.isClickable = true

        })
    }

    private fun initData() {
        textData = findViewById(R.id.NODatadescriptionText)
        textData.text = getString(R.string.no_notification)
        if (intent.hasExtra("keySource4")) {
            data = intent.getParcelableExtra<NotificationModel>("keySource4")!!
            hideButnActionTaken(data)

//************set the api order details
            viewModel.getOrderDetails(data.source_id, data.list_type)
            //notification read api
            viewModel.notificationRead(data.notification_id)
            progressPaginations.visibility = View.VISIBLE
        }
        else if (intent.hasExtra("keyFinalizeOrder")) {
         val    localModel = intent.getParcelableExtra<FinalizeModel>("keyFinalizeOrder")!!
            data.source_id=localModel.id
            data.list_type=localModel.list_type
            //hideButnActionTaken(data)

//************set the api order details
            viewModel.getOrderDetails(data.source_id, data.list_type)
            //notification read api
           // viewModel.notificationRead(data.notification_id)
            progressPaginations.visibility = View.VISIBLE
        }
        else {
            val source_id = intent.getStringExtra("source_id")!!
            val list_type = intent.getStringExtra("list_type")!!
            //  val source = intent.getStringExtra("source")
            val notification_id = intent.getStringExtra("notification_id")!!
            data.source_id = source_id
            data.list_type = list_type
            //************set the api order details
            viewModel.getOrderDetails(data.source_id, data.list_type)
            //notification read api
            viewModel.notificationRead(notification_id)
        }
        //************set the api order details
       // viewModel.getOrderDetails(data.source_id, data.list_type)
        //notification read api
        //viewModel.notificationRead(data.notification_id)
        progressPaginations.visibility = View.VISIBLE

        if (intent.hasExtra("KeySourceCancellOrderConfirm")) {
            txtcreditsTxt.visibility=View.VISIBLE
            txtCancelReason.visibility=View.VISIBLE
            btnConfirmCompletion.text = getString(R.string.i_agree)
            dummyText = getString(R.string.raised_cancellation_request)
        } else {
            dummyText = getString(R.string.has_marked_the_list)
            btnConfirmCompletion.text = getString(R.string.Confirm_Completion)
            txtcreditsTxt.visibility=View.GONE
            txtCancelReason.visibility=View.GONE
        }

    }

    private fun hideButnActionTaken(data: NotificationModel) {
        if (data.action_taken == "1") {
            btnConfirmCompletion.visibility = View.VISIBLE
            textdisputes.visibility = View.VISIBLE
        } else {
            btnConfirmCompletion.visibility = View.INVISIBLE
            textdisputes.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllData(it: OrderModel) {
        //*****************set the payment method**********//
        txtpaymentMethod.text =
            Constant.ModifyCaseOpenListSetPaymentMethods(this, it.payment_methods)
        //set profile image
        if (it.seller_image.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(userPics)
        } else {
            Glide.with(this).load(it.seller_image).into(userPics)
        }
        //set seller level
        if (it.seller_level.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(imgBatchs)
        } else {
            Glide.with(this).load(levelget(it.seller_level).levelImage).into(imgBatches)
        }

        val name = it.seller_name

        val fullText = "$name $dummyText"
        val spannable = SpannableString(fullText)

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            0,
            name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textnames.setText(spannable, TextView.BufferType.SPANNABLE)

        txtlists.text = setCategory(it.cat_id)
        try {
            prices.text = "$" + Constant.roundValue(it.amount.toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (it.delivery_type == "1") {
            //self pickup
            deliveryAddress.text = getString(R.string.Pickup_Address)
            txtdeliveryAdress.text = it.picking_detail.address

        } else {
            //home delivery
            deliveryAddress.text = getString(R.string.Delivery_Address)
            txtdeliveryAdress.text = it.address
        }

        try {
            txtCreditsSpent.text =
                "${Constant.roundValue(it.credits.toDouble())} ${getString(R.string.credits)}"
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun setCategory(catId: String): String {
        val categoriesList = Constant.categoryData1(this)
        for (item in categoriesList) {
            if (item.category_id == catId) {

                return item.name
            }
        }
        return getString(R.string.mix_category_product)
    }

    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Buyer_Confirmation)
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

    private fun setAdapterNotifications() {
        val manager = LinearLayoutManager(this)
        recyclerNotification.layoutManager = manager
        mAdapter = CommonNotificationConfirmationsAdapter(this)
        recyclerNotification.adapter = mAdapter
    }

    //onactvity Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200 && resultCode == RESULT_OK) {

            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()


        }
        if (requestCode == 100 && resultCode == RESULT_OK) {

            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()


        }


    }
//    fun ModifyCaseOpenListSetPaymentMethods(paymentMethods: String): String {
//        val localArray = Constant.setUpPaymentMethods(this)
//        val allmyData = paymentMethods.split(",")
//        var selectedPayment = ""
//        for (items in localArray) {
//            for (myString in allmyData) {
//                if (items.paymentId == myString) {
//                    if (selectedPayment.isEmpty()){
//                        selectedPayment=items.paymentName
//                    }else{
//                        selectedPayment = "$selectedPayment, ${items.paymentName}"
//                    }
//                }
//            }
//        }
//        return selectedPayment
//    }
}
