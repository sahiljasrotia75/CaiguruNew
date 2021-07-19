package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
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
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.seller.homeSeller.HomeSellerModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_resolution.*
import kotlinx.android.synthetic.main.refute_reason_dialog.*
import java.lang.Exception

class ResolutionActivity : AppCompatActivity() {
    private var notificationData = NotificationModel()
    private lateinit var mAdapter: ResolutionAdapter
    private lateinit var viewModel: ResolutionViewModel
    private lateinit var dialog: Dialog
    private lateinit var text: TextView
    val model = CustomerChildModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resolution)
        viewModel = ViewModelProviders.of(this)[ResolutionViewModel::class.java]
        SettingUpToolbar()
        setAdapter()
        dialog = Dialog(this)

        //to be delivered activity
        if (intent.hasExtra("keysellerDisputePending")) {
            val datas = intent.getParcelableExtra<SellerApprovalModel>("keysellerDisputePending")
//************set the api order details
            viewModel.getOrderDetails(datas!!.id, datas.list_type)
            progressPagination.visibility = View.VISIBLE
//for open profile done
            model.buyer_id = datas.buyer_id
            model.name = notificationData.name
            model.list_id = notificationData.list_type
        }
        //home side
        else if (intent.hasExtra("keyHomeFragmentData")) {
            val datas = intent.getParcelableExtra<HomeSellerModel>("keyHomeFragmentData")!!
//************set the api order details
            viewModel.getOrderDetails(datas.req_id, datas.list_type)
            progressPagination.visibility = View.VISIBLE
            notificationData.source_id = datas.req_id
            notificationData.list_type = datas.list_type
//for open profile done
            model.buyer_id = datas.buyer_id
            model.name = datas.name
            model.list_id = datas.list_type


        } else if (intent.hasExtra("keySourceDispute8")) {
            //notification side dispute
            notificationData = intent.getParcelableExtra<NotificationModel>("keySourceDispute8")!!
            actionTakenOnButton(notificationData)
            //for open profile done
//            model.buyer_id = notificationData.sender_id
//            model.name = notificationData.name
//            model.list_id = notificationData.list_type
//************set the api order details
            viewModel.getOrderDetails(notificationData.source_id, notificationData.list_type)
            progressPagination.visibility = View.VISIBLE
            //notification read api
            viewModel.notificationRead(notificationData.notification_id)
        } else {
            notificationData.source_id = intent.getStringExtra("source_id")!!
            notificationData.list_type = intent.getStringExtra("list_type")!!
//            val source = intent.getStringExtra("source")
//            val name = intent.getStringExtra("name")
//            val image = intent.getStringExtra("image")
//            val level = intent.getStringExtra("level")
//            val listingname = intent.getStringExtra("listingname")
//            val reputation = intent.getStringExtra("reputation")
//            val created_at = intent.getStringExtra("created_at")
//for open profile
//            model.buyer_id = buyer_id
//            model.name = name
//            model.list_id = notificationData.list_type
            //************set the api order details
            viewModel.getOrderDetails(notificationData.source_id, notificationData.list_type)
            progressPagination.visibility = View.VISIBLE
        }
        //*****************observer
        viewModel.mSucessfulshopListData().observe(this, Observer {
            if (intent.hasExtra("keysellerDisputePending")) {
                txtRefute.visibility = View.INVISIBLE
                btnConfirmCompletions.visibility = View.INVISIBLE
            }
            progressPagination.visibility = View.GONE
            if (it != null) {
                parentLayout.visibility = View.VISIBLE
                setAllData(it)
                mAdapter.update(it.products,it.comission_per)
                //parameter for open profile
                model.buyer_id = it.buyer_id
                model.name = it.buyer_name
                model.list_id= it.list_type
            } else {
                parentLayout.visibility = View.GONE
                NoDataResolution.visibility = View.VISIBLE
            }
        })
        //failure
        viewModel.mFailureShopList().observe(this, Observer {
            parentLayout.visibility = View.GONE
            progressPagination.visibility = View.GONE
            NoDataResolution.visibility = View.VISIBLE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
        //set the click on the accept disputes
        btnConfirmCompletions.setOnClickListener {

            viewModel.acceptDisputes(notificationData.source_id, notificationData.list_type)
            btnWaitCompletions.visibility = View.VISIBLE
            btnConfirmCompletions.visibility = View.INVISIBLE
        }
        //********observer of accept disputes
        viewModel.mSucessfulAcceptDisputes().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            btnWaitCompletions.visibility = View.INVISIBLE
            btnConfirmCompletions.visibility = View.VISIBLE
            finish()

        })
        viewModel.mFailureAcceptDisputes().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            btnWaitCompletions.visibility = View.INVISIBLE
            btnConfirmCompletions.visibility = View.VISIBLE

        })

        //**********set the click on the refute
        txtRefute.setOnClickListener {
            refuteReasonDialog()

        }
        //**************observer of refute
        viewModel.mSucessfulRefuteReason().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            finish()

        })
        viewModel.mFailureRefuteReason().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            dialog.waitBtn.visibility = View.VISIBLE
            dialog.SubmitBtn.visibility = View.GONE
            dialog.dismiss()
        })

        //set the click image to open the profile
        relativeImaged.setOnClickListener {
            val intent = Intent(this, OtherProfileViewActivity::class.java)
            intent.putExtra("keyResolutionOpenProfileKey", model)
            startActivity(intent)
        }

        textBuyerName.setOnClickListener {
            val intent = Intent(this, OtherProfileViewActivity::class.java)
            intent.putExtra("keyResolutionOpenProfileKey", model)
            startActivity(intent)
        }
    }

//    private fun InitilizeDialog() {
//        dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setContentView(R.layout.refute_reason_dialog)
//    }

    //button visibility
  private fun actionTakenOnButton(data: NotificationModel) {

        if (data.action_taken == "1") {
            txtRefute.visibility = View.VISIBLE
            btnConfirmCompletions.visibility = View.VISIBLE
        } else {
            txtRefute.visibility = View.INVISIBLE
            btnConfirmCompletions.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAllData(it: RefuteModel) {
       // txtListName.text = it.listingname
        txtListName.text = getString(R.string.order)
        try {
            txttotal.text = "$" + Constant.roundValue(it.amount.toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        textBuyerName.text = it.contact_name
        txtNumber.text = it.contact_number
        txtReason.text = it.comment
        //set profile image
        if (it.buyer_image.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(userPic)
        } else {
            Glide.with(this).load(it.buyer_image).into(userPic)
        }
        //set seller level
        if (it.buyer_level.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(imgBatchs)
        } else {
            Glide.with(this).load(levelget(it.buyer_level).levelImage).into(imgBatchs)
        }
    }

    private fun levelget(level: String): BuyerLevelModel {

        val buyerlevel = Constant.BuyerLevel(this)
        for (category in buyerlevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }

        return BuyerLevelModel()

    }


//    private fun levelget(level: String): SellerLevelModel {
//        val sellerLevel = Constant.SellerLevel(this)
//        for (category in sellerLevel) {
//            if (category.levelType == level.trim()) {
//                return category
//            }
//        }
//        return SellerLevelModel()
//    }


    private fun refuteReasonDialog() {

        //  dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.refute_reason_dialog)
        dialog.show()
        dialog.edtReason.filters = arrayOf<InputFilter>(
            HideEmoji(this))

        dialog.SubmitBtn.setOnClickListener {
            if (dialog.edtReason.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Enter_Refute_Message),
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                viewModel.refuteReason(dialog.edtReason.text.toString().trim(), notificationData)
                dialog.waitBtn.visibility = View.VISIBLE
                dialog.SubmitBtn.visibility = View.GONE
            }

        }
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Resolution)
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
        val manager = LinearLayoutManager(this)
        ResolutionRecycler.layoutManager = manager
        mAdapter = ResolutionAdapter(this)
        ResolutionRecycler.adapter = mAdapter
    }
}
