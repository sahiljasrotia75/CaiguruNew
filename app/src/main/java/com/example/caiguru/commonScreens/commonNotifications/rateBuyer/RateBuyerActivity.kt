package com.example.caiguru.commonScreens.commonNotifications.rateBuyer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import com.example.caiguru.commonScreens.commonNotifications.rateSeller.SellerRateModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_rate_buyer.*
import kotlinx.android.synthetic.main.activity_rate_seller.btnSubmit
import kotlinx.android.synthetic.main.activity_rate_seller.edtReviewText
import kotlinx.android.synthetic.main.activity_rate_seller.headingText
import kotlinx.android.synthetic.main.activity_rate_seller.imgBatched
import kotlinx.android.synthetic.main.activity_rate_seller.reputations
import kotlinx.android.synthetic.main.activity_rate_seller.textname
import kotlinx.android.synthetic.main.activity_rate_seller.userPiced
import kotlinx.android.synthetic.main.activity_rate_seller.wait

class RateBuyerActivity : AppCompatActivity(), BuyerRatingAdapter.setRatingDataInterface {

    private var notificationModel = NotificationModel()
    private  var rating: String=""
    private lateinit var mAdapter: BuyerRatingAdapter
    private lateinit var viewModel: RateBuyerViewModel
    private lateinit var text: TextView
    val profileModel = CustomerChildModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate_buyer)
        viewModel = ViewModelProviders.of(this)[RateBuyerViewModel::class.java]
        SettingUpToolbar()
        setAdapterRating()
        mAdapter.update(Constant.sellerRatingData(this))//statics rating set by constant

        edtReviewText.filters = arrayOf<InputFilter>(HideEmoji(this))

        if (intent.hasExtra("keySource7")) {
            notificationModel = intent.getParcelableExtra<NotificationModel>("keySource7")!!
            setAllData(notificationModel)//set  all the data of activity
            profileModel.seller_id = notificationModel.sender_id
            profileModel.name = notificationModel.name
            //notification read api
            viewModel.notificationRead(notificationModel.notification_id)
        } else {
            notificationModel.source_id = intent.getStringExtra("source_id")!!
            notificationModel.list_type = intent.getStringExtra("list_type")!!
          //  val source = intent.getStringExtra("source")
            notificationModel.name = intent.getStringExtra("name")!!
            notificationModel.image = intent.getStringExtra("image")!!
            notificationModel.level = intent.getStringExtra("level")!!
            notificationModel.listingname = intent.getStringExtra("listingname")!!
            notificationModel.reputation = intent.getStringExtra("reputation")!!
            notificationModel.created_at = intent.getStringExtra("created_at")!!
            val notification_id = intent.getStringExtra("notification_id")!!
            val sender_id = intent.getStringExtra("sender_id")
            //for open the profile
            profileModel.seller_id= sender_id!!
            profileModel.name=notificationModel.name
            setAllData(notificationModel)//set  all the data of activity
            //notification read api
            viewModel.notificationRead(notification_id)
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
                viewModel.setRatingData(
                    notificationModel,
                    edtReviewText.text.toString().trim(),
                    rating
                )
                wait.visibility = View.VISIBLE
                btnSubmit.visibility = View.INVISIBLE
            }
        }

        viewModel.mSucessfulshopListData().observe(this, Observer {
            Toast.makeText(
                this, getString(R.string.Rated_Sucessfully),
                Toast.LENGTH_SHORT
            ).show()
            wait.visibility = View.INVISIBLE
            btnSubmit.visibility = View.VISIBLE
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)//set the result
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
        //open the profile
            userPiced.setOnClickListener {
                val intent = Intent(this, OtherProfileViewActivity::class.java)
                intent.putExtra("keyChosseSellerProfileKey1", profileModel)
                startActivity(intent)
            }
            textname.setOnClickListener {
                val intent = Intent(this, OtherProfileViewActivity::class.java)
                intent.putExtra("keyChosseSellerProfileKey1", profileModel)
                startActivity(intent)
            }


    }

    @SuppressLint("SetTextI18n")
    private fun setAllData(allModelData: NotificationModel) {
        headingText.text =
            "${getString(R.string.Please_rate_your_experience)} ${allModelData.name}"
        textname.text = allModelData.name

        //set profile image
        if (allModelData.image.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(userPiced)
        } else {
            Glide.with(this).load(allModelData.image).into(userPiced)
        }
        //set seller level
        if (allModelData.level.isEmpty()) {
            Glide.with(this).load(R.drawable.ic_profile)
                .into(imgBatched)
        } else {
            Glide.with(this).load(levelget(allModelData.level).levelImage).into(imgBatched)
        }

        reputations.text = "${getString(R.string.reputation)} ${Constant.getReputationString(
            this,
            allModelData.reputation
        )}"
    }

    private fun levelget(level: String): BuyerLevelModel {
        val BuyerLevel = Constant.BuyerLevel(this)
        for (category in BuyerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Rate_buyer)
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
        recyclerRatingBuyer.layoutManager = manager
        mAdapter = BuyerRatingAdapter(this)
        recyclerRatingBuyer.adapter = mAdapter
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
