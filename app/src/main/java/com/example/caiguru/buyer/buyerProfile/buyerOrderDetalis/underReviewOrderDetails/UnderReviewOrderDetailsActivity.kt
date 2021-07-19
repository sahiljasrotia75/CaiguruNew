package com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsAdapter
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.chatMessage.ChatMessagesActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.commonNotifications.rateSeller.RateSellerActivity
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_buyer_order_details.imgBatchs
import kotlinx.android.synthetic.main.activity_buyer_order_details.price
import kotlinx.android.synthetic.main.activity_buyer_order_details.progressBar
import kotlinx.android.synthetic.main.activity_buyer_order_details.recyclerDetails
import kotlinx.android.synthetic.main.activity_buyer_order_details.relativeParent
import kotlinx.android.synthetic.main.activity_buyer_order_details.reputation
import kotlinx.android.synthetic.main.activity_buyer_order_details.textname
import kotlinx.android.synthetic.main.activity_buyer_order_details.txtStatus
import kotlinx.android.synthetic.main.activity_buyer_order_details.txtlist
import kotlinx.android.synthetic.main.activity_buyer_order_details.userPic
import kotlinx.android.synthetic.main.activity_under_review_order_details.*
import kotlinx.android.synthetic.main.activity_under_review_order_details.acceptedStatusLayout
import kotlinx.android.synthetic.main.activity_under_review_order_details.txtCashOnDeliverys
import kotlinx.android.synthetic.main.activity_under_review_order_details.txtPartialPayment
import kotlinx.android.synthetic.main.toolbar.view.*
import java.lang.Exception

class UnderReviewOrderDetailsActivity : AppCompatActivity() {
    private val profileData = CustomerChildModel()
    private var dataModel = NotificationModel()
    private lateinit var req_id: String
    private var modelChat = ModelChat()
    private lateinit var list_type: String
    private var allUnderReviewData = OrderModel()
    private lateinit var mvmodel: OrderDetailsViewModel
    private lateinit var adapter: OrderDetailsAdapter
    private lateinit var adapterComment: RefuteDisputesCommentAdapter
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_under_review_order_details)
       // mvmodel = ViewModelProviders.of(this)[OrderDetailsViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        setAdapterComment()
        // Status: 1:Pending Approval, 2:Accepted, 3:Rejected, 4:SellerCompleted, 5:Buyer_Confirmed, 6: Under_Review

        if (intent.hasExtra("keySourceRefuteBySeller10")) {
            dataModel =
                intent.getParcelableExtra<NotificationModel>("keySourceRefuteBySeller10")!!
            req_id = dataModel.source_id
            list_type = dataModel.list_type
            //notification read api
            mvmodel.notificationRead(dataModel.notification_id)
            if (dataModel.action_taken == "1") {
                btnMarkComplete.visibility = View.VISIBLE
                textAdmin.visibility = View.VISIBLE
            } else {
                btnMarkComplete.visibility = View.GONE
            }

        } else if (intent.hasExtra("keydetails")) {
            val dataModel = intent.getParcelableExtra<OrderModel>("keydetails")
            req_id = dataModel!!.req_id
            list_type = dataModel!!.list_type
        } else {
            req_id = intent.getStringExtra("source_id")!!
            list_type = intent.getStringExtra("list_type")!!
//            val source = intent.getStringExtra("source")
//            val name = intent.getStringExtra("name")
//            val image = intent.getStringExtra("image")
//            val level = intent.getStringExtra("level")
//            val listingname = intent.getStringExtra("listingname")
//            val reputation = intent.getStringExtra("reputation")
//            val created_at = intent.getStringExtra("created_at")
            btnMarkComplete.visibility = View.VISIBLE
            textAdmin.visibility = View.VISIBLE
        }


        //****************order details Api************//
        mvmodel.getOrderDetails(req_id, list_type)
        progressBar.visibility = View.VISIBLE

        //get order detail observer


        mvmodel.mSucessfulshopListData().observe(this, Observer {
            allUnderReviewData = it
            profileData.seller_id = allUnderReviewData.seller_id
            profileData.name = allUnderReviewData.seller_name
            progressBar.visibility = View.GONE
            if (it != null) {
                RefuteDisputeCommentsRecycler.visibility = View.VISIBLE
                relativeParent.visibility = View.VISIBLE
                setAllData(it)
                adapter.update(it.products, it.comission_per)
                adapterComment.update(it.comments)
                modelChat.listingname = it.listingname
                modelChat.channel_id = it.channel_id
                modelChat.user_id = it.seller_id
                modelChat.level = it.seller_level
                modelChat.image = it.seller_image
                modelChat.type = it.type
                modelChat.name = it.seller_name
            } else {
                relativeParent.visibility = View.GONE
                RefuteDisputeCommentsRecycler.visibility = View.GONE
                btnMarkComplete.visibility = View.GONE
                NoDataUnderreview.visibility = View.VISIBLE
            }

        })
        //failure
        mvmodel.mFailureShopList().observe(this, Observer {
            progressBar.visibility = View.GONE
            NoDataUnderreview.visibility = View.VISIBLE
            relativeParent.visibility = View.GONE
            btnMarkComplete.visibility = View.GONE
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)

        })

        //set the click on the mark as complete
        btnMarkComplete.setOnClickListener {
            val intent = Intent(this, RateSellerActivity::class.java)
            intent.putExtra("keyUnderReview", allUnderReviewData)
            startActivity(intent)
            finish()
        }
        //*************chat icon click
        underReviewToolbar.chatOrderDetail.visibility = View.VISIBLE
        underReviewToolbar.chatOrderDetail.setOnClickListener {
            //chat icon click
            val intent = Intent(this, ChatMessagesActivity::class.java)
            intent.putExtra("model", modelChat)
            startActivity(intent)

        }

//       //open the profile
        userPic.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=profileData.seller_id
            localModel.cat_id=""
            localModel.seller_name=profileData.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)


//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", profileData)
//            startActivity(intent)
        }
        textname.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=profileData.seller_id
            localModel.cat_id=""
            localModel.seller_name=profileData.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)

//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", profileData)
//            startActivity(intent)
        }

    }


    @SuppressLint("SetTextI18n")
    private fun setAllData(it: OrderModel) {
        try {
            txtStatus.text = setStatus(it.status)
            //set  the patial payment
            txtPartialPayment.text =
                Constant.roundValue(it.credits.toDouble()) + " " + getString(R.string.cr)
//set the cash on delivery
            txtCashOnDeliverys.text =
                Constant.roundValue(it.amount.toDouble() - it.credits.toDouble())

            txtStatus.text = setStatus(it.status)
            //profile image
            if (it.seller_image.isEmpty()) {
                Glide.with(this).load(R.drawable.ic_profile)
                    .into(userPic)
            } else {
                Glide.with(this).load(it.seller_image).into(userPic)
            }
//level image
            if (it.seller_level.isEmpty()) {
                Glide.with(this).load(R.drawable.ic_profile).into(imgBatchs)
            } else {
                Glide.with(this).load(levelget(it.seller_level).levelImage).into(imgBatchs)
            }

            textname.text = it.seller_name
            reputation.text =
                "${getString(R.string.rupatation)}: " + Constant.getReputationString(
                    this,
                    it.reputation
                )
          //  txtlist.text = setCategory(it.cat_id)
            txtlist.text = getString(R.string.mix_category_product)

            price.text = "$" + (Constant.roundValue(it.amount.toDouble()))

            //check of status
            if (it.status == "5") {
                OrderCompleted.visibility = View.VISIBLE
//                txtCmission.text =
//                    "$" + Constant.roundValue(it.comission_per.toDouble()) + getString(R.string.cr)
                if (it.delivery_type == "1") {
                    pickupAddresss.visibility = View.VISIBLE
                    txtPickupAddresss.visibility = View.VISIBLE
                    view00.visibility = View.VISIBLE
                    txtPickupAddresss.text = it.picking_detail.address
                } else {
                    pickupAddresss.visibility = View.GONE
                    txtPickupAddresss.visibility = View.GONE
                    view00.visibility = View.GONE
                }
                //order on
                txtOrderOns.text = Constant.ATconvert(
                    this,
                    Constant.ConvertAmPmFormat(this, Constant.orderdateTimeFormat(it.created_at))
                )


                //complted on
                orderComplteds.setText(getString(R.string.seller_completed_on))
                txtorderComplteds.text = Constant.ATconvert(
                    this,
                    Constant.ConvertAmPmFormat(
                        this,
                        Constant.orderdateTimeFormat(it.completed_date)
                    )
                )
            } else {
                OrderCompleted.visibility = View.GONE
//                txtCmission.text =
//                    "$" + Constant.roundValue(it.comission_per.toDouble()) + getString(R.string.cr)


            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Status: 1:Pending Approval, 2:Accepted, 3:Rejected, 4:SellerCompleted, 5:Buyer_Confirmed, 6: Under_Review
    private fun setStatus(status: String): String {
        if (status == "1") {
            return getString(R.string.Pending_Approvals)
        } else if (status == "2") {
            acceptedStatusLayout.visibility = View.VISIBLE
            return getString(R.string.Accepted)
        } else if (status == "3") {
            return getString(R.string.Rejected)
        } else if (status == "4") {
            acceptedStatusLayout.visibility = View.VISIBLE
            return getString(R.string.SellerCompleted)
        } else if (status == "5") {
            acceptedStatusLayout.visibility = View.VISIBLE
            return getString(R.string.Buyer_Confirmed)
        } else {
            acceptedStatusLayout.visibility = View.GONE
            return getString(R.string.Under_Review)
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

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.underReviewToolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Orderdetails)
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

    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = OrderDetailsAdapter(this)
        recyclerDetails.layoutManager = manager
        recyclerDetails.adapter = adapter
    }

    private fun setAdapterComment() {
        val manager = LinearLayoutManager(this)
        adapterComment = RefuteDisputesCommentAdapter(this)
        RefuteDisputeCommentsRecycler.layoutManager = manager
        RefuteDisputeCommentsRecycler.adapter = adapterComment
    }

}
