package com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis

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
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.chatMessage.ChatMessagesActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_buyer_order_details.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.lang.Exception

class BuyerOrderDetailsActivity : AppCompatActivity() {
    private var messagesUnreadCount: String = ""
    private var req_id: String = ""
    private var list_type: String = ""
    private lateinit var mvmodel: OrderDetailsViewModel
    private lateinit var adapter: OrderDetailsAdapter
    private lateinit var text: TextView
    private var modelChat = ModelChat()
    val profileData = CustomerChildModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_order_details)
        //  mvmodel = ViewModelProviders.of(this)[OrderDetailsViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(OrderDetailsViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        //   val gson = Gson()
        //    val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        // if (Constant.getProfileData(this).email != "") {
        // var model = GetProfileModel()
        // model = gson.fromJson(json, GetProfileModel::class.java)
        messagesUnreadCount = Constant.getProfileData(this).unreadcount
        //  }

        if (intent.hasExtra("keySourceAcceptedOrder2")) {
            val dataModel = intent.getParcelableExtra<NotificationModel>("keySourceAcceptedOrder2")
            req_id = dataModel!!.source_id
            list_type = dataModel.list_type
            //notification read api
            mvmodel.notificationRead(dataModel.notification_id)

        } else if (intent.hasExtra("keySourceRejectOrder2")) {
            val dataModel = intent.getParcelableExtra<NotificationModel>("keySourceRejectOrder2")
            req_id = dataModel!!.source_id
            list_type = dataModel.list_type
            //notification read api
            mvmodel.notificationRead(dataModel.notification_id)

        } else if (intent.hasExtra("keySourceAcceptDisputeBySeller9")) {
            val dataModel =
                intent.getParcelableExtra<NotificationModel>("keySourceAcceptDisputeBySeller9")
            req_id = dataModel!!.source_id
            list_type = dataModel.list_type
            //notification read api
            mvmodel.notificationRead(dataModel.notification_id)

        } else if (intent.hasExtra("keydetails")) {
            val dataModel = intent.getParcelableExtra<OrderModel>("keydetails")
            req_id = dataModel!!.req_id
            list_type = dataModel.list_type
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
        }

        //****************order details Api************//
        mvmodel.getOrderDetails(req_id, list_type)
        progressBar.visibility = View.VISIBLE

        //get order detail observer
        mvmodel.mSucessfulshopListData().observe(this, Observer {
            progressBar.visibility = View.GONE
            profileData.seller_id = it.seller_id
            profileData.name = it.seller_name
            modelChat.listingname = it.listingname
            modelChat.channel_id = it.channel_id
            modelChat.user_id = it.seller_id
            modelChat.level = it.seller_level
            modelChat.image = it.seller_image
            modelChat.type = it.type
            modelChat.name = it.seller_name
            if (it != null) {
                relativeParent.visibility = View.VISIBLE
                setAllData(it)
                adapter.update(it.products, it.comission_per)
            } else {
                relativeParent.visibility = View.INVISIBLE
                moDataOrderdetails.visibility = View.VISIBLE
            }
        })
        //failure
        mvmodel.mFailureShopList().observe(this, Observer {
            progressBar.visibility = View.GONE
            moDataOrderdetails.visibility = View.VISIBLE
            relativeParent.visibility = View.INVISIBLE
            //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)

        })
        //*************chat icon click
        orderDetailsToolbar.chatOrderDetail.setOnClickListener {
            //chat icon click
            val intent = Intent(this, ChatMessagesActivity::class.java)
            intent.putExtra("model", modelChat)
            startActivity(intent)

        }
//       //open the profile
        relativeImagge.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=profileData.seller_id
            localModel.cat_id=""
            localModel.seller_name=profileData.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)


//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", profileData)

            startActivity(intent)
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
           // txtlist.text = setCategory(it.cat_id)
            txtlist.text = getString(R.string.mix_category_product)
            try {
                price.text = "$" + (Constant.roundValue(it.amount.toDouble()))
            } catch (e: Exception) {
                e.printStackTrace()
            }



            if (it.delivery_type == "1") {
                txtPickupAddress.text = it.picking_detail.address
            } else {
                pickupAddress.visibility = View.GONE
                txtPickupAddress.visibility = View.GONE
                view7.visibility = View.GONE
            }

            if (it.status == "3") {
                orderComplted.visibility = View.GONE
                txtorderComplted.visibility = View.GONE
                view5.visibility = View.GONE
                txtsellerReason.visibility = View.VISIBLE
                sellerReason.visibility = View.VISIBLE
                sellerReason.text = Constant.setReason(this,it.reason)

            } else if (it.status == "1") {
                orderComplted.visibility = View.GONE
                txtorderComplted.visibility = View.GONE
                view5.visibility = View.GONE
                txtsellerReason.visibility = View.GONE
                sellerReason.visibility = View.GONE

            } else if (it.status == "4") {
                orderComplted.setText(getString(R.string.seller_completed_on))
                txtorderComplted.text = Constant.ATconvert(
                    this,
                    Constant.ConvertAmPmFormat(
                        this,
                        Constant.orderdateTimeFormat(it.completed_date)
                    )
                )
                orderComplted.visibility = View.VISIBLE
                txtorderComplted.visibility = View.VISIBLE
                view4.visibility = View.VISIBLE

            } else if (it.status == "2") {
                orderComplted.visibility = View.GONE
                txtorderComplted.visibility = View.GONE
                view5.visibility = View.GONE
                txtsellerReason.visibility = View.GONE
                sellerReason.visibility = View.GONE
            } else if (it.status == "7") {
                orderComplted.visibility = View.GONE
                txtorderComplted.visibility = View.GONE
                view5.visibility = View.GONE
                txtsellerReason.visibility = View.GONE
                sellerReason.visibility = View.GONE
            }  else {
                txtorderComplted.text = Constant.ATconvert(
                    this,
                    Constant.ConvertAmPmFormat(
                        this,
                        Constant.orderdateTimeFormat(it.completed_date)
                    )
                )
                txtorderComplted.visibility = View.VISIBLE
                view5.visibility = View.VISIBLE
                orderComplted.visibility = View.VISIBLE
                view4.visibility = View.VISIBLE
            }

            if (it.completed_date.isEmpty()) {
                txtorderComplted.text = ""
                txtorderComplted.visibility = View.GONE
                view5.visibility = View.GONE
                orderComplted.visibility = View.GONE

            }
            if (it.reason.isNotEmpty()) {
                sellerReason.text = Constant.setReason(this,it.reason)
                txtsellerReason.visibility = View.VISIBLE
                sellerReason.visibility = View.VISIBLE
            }
            txtOrderOn.text = Constant.ATconvert(
                this,
                Constant.ConvertAmPmFormat(this, Constant.orderdateTimeFormat(it.created_at))
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Status: 1:Pending Approval, 2:Accepted, 3:Rejected, 4:SellerCompleted, 5:Buyer_Confirmed, 6: Under_Review
    private fun setStatus(status: String): String {
        if (status == "1") {
            orderDetailsToolbar.chatOrderDetail.visibility = View.GONE
            acceptedStatusLayout.visibility = View.GONE
            return getString(R.string.Pending_Approvals)

        } else if (status == "2") {
            showChatCount()
            orderDetailsToolbar.chatOrderDetail.visibility = View.VISIBLE
            acceptedStatusLayout.visibility = View.VISIBLE
            return getString(R.string.Accepted)
        } else if (status == "3") {
            orderDetailsToolbar.chatOrderDetail.visibility = View.GONE
            acceptedStatusLayout.visibility = View.GONE
            return getString(R.string.Rejected)

        } else if (status == "4") {
            showChatCount()
            orderDetailsToolbar.chatOrderDetail.visibility = View.VISIBLE
            acceptedStatusLayout.visibility = View.VISIBLE
            return getString(R.string.SellerCompleted)

        } else if (status == "5") {
            showChatCount()
            orderDetailsToolbar.chatOrderDetail.visibility = View.VISIBLE
            acceptedStatusLayout.visibility = View.VISIBLE
            return getString(R.string.Buyer_Confirmed)

        } else if (status == "7") {
            showChatCount()
            orderDetailsToolbar.chatOrderDetail.visibility = View.GONE
            acceptedStatusLayout.visibility = View.GONE
            return getString(R.string.seller_cancelled)

        } else if (status == "8") {
            showChatCount()
            orderDetailsToolbar.chatOrderDetail.visibility = View.GONE
            acceptedStatusLayout.visibility = View.GONE
            return getString(R.string.buyer_confirmed_order_cancelled)

        } else {
            showChatCount()
            orderDetailsToolbar.chatOrderDetail.visibility = View.VISIBLE
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
        val toolbar = findViewById<Toolbar>(R.id.orderDetailsToolbar)
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

    //*************************chatting count
    private fun showChatCount() {
//        if (messagesUnreadCount=="0") {
//            orderDetailsToolbar.toolbarMesageButton2.visibility = View.GONE
//        } else {
//            orderDetailsToolbar.toolbarMesageButton2.visibility=View.VISIBLE
//            orderDetailsToolbar.toolbarMesageButton2.text = messagesUnreadCount
//        }
    }
}
