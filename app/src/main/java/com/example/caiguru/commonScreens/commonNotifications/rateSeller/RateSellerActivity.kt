package com.example.caiguru.commonScreens.commonNotifications.rateSeller

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_rate_seller.*


class RateSellerActivity : AppCompatActivity(), SellerRatingAdapter.setRatingDataInterface {

    private  var list_type: String=""
    private  var req_id: String=""
    private var rating: String = ""
    private lateinit var viewModel: RateSellerViewModel
    private lateinit var mAdapter: SellerRatingAdapter
    private lateinit var text: TextView
    val model = CustomerChildModel()
    var ApiDataModel = NotificationModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_seller)
        viewModel = ViewModelProviders.of(this)[RateSellerViewModel::class.java]
        edtReviewText.filters = arrayOf<InputFilter>(HideEmoji(this))
        SettingUpToolbar()
        setAdapterRating()
        mAdapter.update(Constant.sellerRatingData(this))//statics rating set by constant
        if (intent.hasExtra("keyUnderReview")) {
            val allModelData = intent.getParcelableExtra<OrderModel>("keyUnderReview")
            ApiDataModel.list_type = allModelData!!.list_type
            ApiDataModel.source_id = allModelData.id
            model.seller_id = allModelData.seller_id
            model.name = allModelData.seller_name
            setAllData(allModelData)
        }
        //notification  from dash board
        else if (intent.hasExtra("NotificationRateToSeller")) {
            val allModelData = intent.getParcelableExtra<OrderModel>("NotificationRateToSeller")!!
            ApiDataModel.list_type = allModelData.list_type
            ApiDataModel.source_id = allModelData.id
            model.seller_id = allModelData.seller_id
            model.name = allModelData.seller_name
            req_id= allModelData.req_id
            list_type= allModelData.list_type
            setAllData(allModelData)
        }

        else if (intent.hasExtra("KeySourceNOtification")) {
            val allModelData = intent.getParcelableExtra<NotificationModel>("KeySourceNOtification")!!
            val localMOdel=OrderModel()
            ApiDataModel.list_type = allModelData.list_type
            ApiDataModel.source_id = allModelData.source_id
            model.seller_id = allModelData.sender_id
            model.name = allModelData.name

            localMOdel.list_type=allModelData.list_type
            localMOdel.seller_name=allModelData.name
            localMOdel.seller_image=allModelData.image
            localMOdel.seller_level=allModelData.level
            localMOdel.reputation=allModelData.reputation
            localMOdel.listingname=allModelData.listingname
            req_id= allModelData.source_id
            list_type= allModelData.list_type
            //notification api
            viewModel.notificationRead(allModelData.notification_id)
            setAllData(localMOdel)
        }

        else {
            val allModelData = intent.getParcelableExtra<OrderModel>("keyRatingData")!!

            setAllData(allModelData)//set  all the data of activity
            val notificationModel = intent.getParcelableExtra<NotificationModel>("keySource4")!!
            ApiDataModel.list_type = notificationModel.list_type
            ApiDataModel.source_id = notificationModel.source_id
            model.seller_id = allModelData.seller_id
            model.name = allModelData.seller_name

        }
//set the click on the button
        btnSubmit.setOnClickListener {
            if (rating.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_the_rating),
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                if (intent.hasExtra("NotificationRateToSeller")){
                    viewModel.AdminCompleteListRateSeller(req_id,list_type, edtReviewText.text.toString().trim(),rating)
                }else  if (intent.hasExtra("KeySourceNOtification")){
                    viewModel.AdminCompleteListRateSeller( req_id,list_type, edtReviewText.text.toString().trim(),rating)
                }else{
                    viewModel.setRatingData(
                        ApiDataModel,
                        edtReviewText.text.toString().trim(),
                        rating
                    )
                }

                wait.visibility = View.VISIBLE
                btnSubmit.visibility = View.INVISIBLE
            }
        }

        viewModel.mSucessfulshopListData().observe(this, Observer {
            Toast.makeText(
                this, it,
                Toast.LENGTH_SHORT
            ).show()
            wait.visibility = View.INVISIBLE
            btnSubmit.visibility = View.VISIBLE
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
        //failure
        viewModel.mFailureShopList().observe(this, Observer {
            Toast.makeText(
                this, it,
                Toast.LENGTH_SHORT
            ).show()
            wait.visibility = View.INVISIBLE
            btnSubmit.visibility = View.VISIBLE
        })
        //       //open the profile
        userPiced.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=model.seller_id
            localModel.cat_id=model.cat_id
            localModel.seller_name=model.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)

//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", model)
//            startActivity(intent)
        }
        textname.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=model.seller_id
            localModel.cat_id=model.cat_id
            localModel.seller_name=model.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)

//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", model)
//            startActivity(intent)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setAllData(allModelData: OrderModel) {
        headingText.text =
            "${getString(R.string.Please_rate_your_experience)} ${allModelData.seller_name}"
        textname.text = allModelData.seller_name

        //set profile image
        if (allModelData.seller_image.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(userPiced)
        } else {
            Glide.with(this).load(allModelData.seller_image).into(userPiced)
        }
        //set seller level
        if (allModelData.seller_level.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(imgBatched)
        } else {
            Glide.with(this).load(levelget(allModelData.seller_level).levelImage).into(imgBatched)
        }

        reputations.text = "${getString(R.string.reputation)} ${Constant.getReputationString(
            this,
            allModelData.reputation
        )}"
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
        text.text = getText(R.string.Rate_Seller)
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

    private fun setAdapterRating() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerRating.layoutManager = manager
        mAdapter = SellerRatingAdapter(this)
        recyclerRating.adapter = mAdapter
        // recyclerRating.setItemAnimator(DefaultItemAnimator())


    }

    //get the seller rating
    override fun setRating(child: ArrayList<SellerRateModel>, position: Int) {
        if (child[position].hasSelected) {
            rating = child[position].rating
        } else {
            rating = ""
        }
    }
}
