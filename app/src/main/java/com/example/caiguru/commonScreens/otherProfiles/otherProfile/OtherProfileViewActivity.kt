package com.example.caiguru.commonScreens.otherProfiles.otherProfile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.buyer.homeBuyers.tagUser.TagModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.otherProfiles.viewAllReviewslist.OtherProfileReviewsAllActivity
import com.example.caiguru.commonScreens.otherProfiles.viewAllShoppingList.ViewAllShoppingActivity
import com.example.caiguru.databinding.ActivityOtherProfileViewBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.homeSeller.HomeSellerModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_other_profile_view.*
import kotlinx.android.synthetic.main.followers_adapter.reputation

class OtherProfileViewActivity : AppCompatActivity() {

    private var CheckMyProfileOrNot: String = ""
    private lateinit var ReputationText: String
    private lateinit var fulltext: String
    private lateinit var usertype: String
    private var dataBuyerHome = HomeModel()
    private var Notificationdata = NotificationModel()
    private lateinit var adapterReviews: OtherProfileLatestReviewsAdapter
    private var globalOtherPrileData = GetProfileModel()
    private lateinit var adapter: OtherProfileShoppingListParentAdapter
    private var data = SellerApprovalModel()
    private var follow_status: Int = 0
    private var hasFollow: Boolean = false
    private lateinit var mbinding: ActivityOtherProfileViewBinding
    private lateinit var mvmodel: OtherProfileViewModel
    private var buyer_name: String = ""
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_other_profile_view)
        mvmodel = ViewModelProviders.of(this)[OtherProfileViewModel::class.java]
        //*****************user profile data that they dont open there profile
        var myProfileDataOWner = GetProfileModel()
        val gson = Gson()
        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        myProfileDataOWner = gson.fromJson(json, GetProfileModel::class.java)
        CheckMyProfileOrNot = myProfileDataOWner.user_id
        setAdapter()
        setAdapterReviews()
        //api 1. means buyer  and 2. seller in  all app list type 1=seller listtype 2=buyer
        if (intent.hasExtra("keyCompleted")) {//
            data = intent.getParcelableExtra<SellerApprovalModel>("keyCompleted")!!
            buyer_name = data.buyer_name
            btnFollowUnfollow.visibility = View.VISIBLE
//            if (data.list_type == "1") {
//                usertype = "2"
//            } else {
//                usertype = "1"
//            }
            usertype = "1"
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }
        }
        else if (intent.hasExtra("keyResolutionOpenProfileKey")) { //list id
            val listUploadedCustomer =
                intent.getParcelableExtra<CustomerChildModel>("keyResolutionOpenProfileKey")!!
            buyer_name = listUploadedCustomer.name
            data.buyer_id = listUploadedCustomer.buyer_id
            btnFollowUnfollow.visibility = View.VISIBLE
            usertype = "1"
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyListUploadedByCustomerProfile")) { //list id
            val listUploadedCustomer = intent.getParcelableExtra<CustomerChildModel>("keyListUploadedByCustomerProfile")!!
            buyer_name = listUploadedCustomer.name
            data.buyer_id = listUploadedCustomer.buyer_id
            usertype = "1"
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyChosseSellerProfileKey")) {
            val listUploadedCustomer = intent.getParcelableExtra<CustomerChildModel>("keyChosseSellerProfileKey")!!
            buyer_name = listUploadedCustomer.name
            data.buyer_id = listUploadedCustomer.seller_id
            usertype = "2"
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyChosseSellerProfileKey1")) {
            val listUploadedCustomer =
                intent.getParcelableExtra<CustomerChildModel>("keyChosseSellerProfileKey1")!!
            buyer_name = listUploadedCustomer.name
            data.buyer_id = listUploadedCustomer.seller_id
            usertype = "1"
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }
        }
        //open their own profile
        else if (intent.hasExtra("keyOpenMyProfileBuyerSide")) {
            val listUploadedCustomer =
                intent.getParcelableExtra<CustomerChildModel>("keyOpenMyProfileBuyerSide")!!
            buyer_name = listUploadedCustomer.name
            data.buyer_id = listUploadedCustomer.buyer_id
            btnFollowUnfollow.visibility = View.INVISIBLE
            usertype = "1"
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }
        }
        else if (intent.hasExtra("keyOpenMyProfileSellerSide")) {
            val listUploadedCustomer =
                intent.getParcelableExtra<CustomerChildModel>("keyOpenMyProfileSellerSide")!!
            buyer_name = listUploadedCustomer.name
            data.buyer_id = listUploadedCustomer.buyer_id
            usertype = "2"
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyBuyerHomeCommentProfile")) {
            val tagModel = intent.getParcelableExtra<TagModel>("keyBuyerHomeCommentProfile")!!
            buyer_name = tagModel.name
            data.buyer_id = tagModel.user_id
            usertype = "1"
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyNotQuotedBuyerProfile")) {
            val tagModel = intent.getParcelableExtra<TagModel>("keyNotQuotedBuyerProfile")!!
            buyer_name = tagModel.name
            data.buyer_id = tagModel.user_id
            btnFollowUnfollow.visibility = View.VISIBLE
            if (tagModel.type == "1") {
                usertype = "1"
            } else {
                usertype = "2"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyBuyerHomeCenterSpan")) {
            dataBuyerHome = intent.getParcelableExtra<HomeModel>("keyBuyerHomeCenterSpan")!!
            buyer_name = dataBuyerHome.seller_name
            data.buyer_id = dataBuyerHome.seller_id
            usertype = "1"
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyHomeBuyerSharedList")) {
            dataBuyerHome = intent.getParcelableExtra<HomeModel>("keyHomeBuyerSharedList")!!
            buyer_name = dataBuyerHome.shared_by_name
            data.buyer_id = dataBuyerHome.shared_by
            usertype = "1"
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyHomeBuyerPurposalList")) {
            dataBuyerHome = intent.getParcelableExtra<HomeModel>("keyHomeBuyerPurposalList")!!
            buyer_name = dataBuyerHome.seller_name
            data.buyer_id = dataBuyerHome.seller_id
            btnFollowUnfollow.visibility = View.VISIBLE
            //  usertype = "2"
            if (dataBuyerHome.list_type == "1") {
                usertype = "2"
            } else {
                usertype = "1"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyCancled")) {
            data = intent.getParcelableExtra<SellerApprovalModel>("keyCancled")!!
            buyer_name = data.buyer_name
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.list_type == "1") {
                usertype = "2"
            } else {
                usertype = "1"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

            //api 1. means buyer  and 2. seller in  all app list type 1=seller listtype 2=buyer

        }
        else if (intent.hasExtra("keyHomeCommentProfile")) {
            val Tagdata = intent.getParcelableExtra<ReviewsModel>("keyHomeCommentProfile")!!
            buyer_name = Tagdata.name
            data.buyer_id = Tagdata.user_id
            btnFollowUnfollow.visibility = View.VISIBLE
            if (Tagdata.type == "1") {
                usertype = "1"
            } else {
                usertype = "2"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyTageUserProfile")) {
            val Tagdata = intent.getParcelableExtra<TagModel>("keyTageUserProfile")!!
            buyer_name = Tagdata.name
            data.buyer_id = Tagdata.user_id
            btnFollowUnfollow.visibility = View.VISIBLE
            if (Tagdata.type == "1") {
                usertype = "1"
            } else {
                usertype = "2"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }
        }
        //api 1. means buyer  and 2. seller in  all app list type 1=seller listtype 2=buyer
        else if (intent.hasExtra("keyHomeSellerProfile")) {
            val homeProfileModel = intent.getParcelableExtra<HomeSellerModel>("keyHomeSellerProfile")!!
            buyer_name = homeProfileModel.name
            data.buyer_id = homeProfileModel.buyer_id
            usertype = "1"
            btnFollowUnfollow.visibility = View.VISIBLE
//            if (homeProfileModel.list_type == "1") {
//                usertype = "2"
//            } else {
//                usertype = "1"
//            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else if (intent.hasExtra("keyNotificationProfile")) {
            Notificationdata =
                intent.getParcelableExtra<NotificationModel>("keyNotificationProfile")!!
            buyer_name = Notificationdata.name
            data.buyer_id = Notificationdata.sender_id
            btnFollowUnfollow.visibility = View.VISIBLE
            if (Notificationdata.list_type == "1") {
                usertype = "2"
            } else {
                usertype = "1"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        else {
            //In Response: list_type 1:Seller, 2:Buyer
            //api 1. means buyer  and 2. seller in  all app list type 1=seller listtype 2=buyer
            data = intent.getParcelableExtra<SellerApprovalModel>("keydata")!!
            buyer_name = data.buyer_name
            btnFollowUnfollow.visibility = View.VISIBLE
            if (data.list_type == "1") {
                usertype = "2"
            } else {
                usertype = "1"
            }
            if (data.buyer_id == CheckMyProfileOrNot) {
                btnFollowUnfollow.visibility = View.INVISIBLE
                btnFollow.visibility = View.INVISIBLE
                blockUser.visibility = View.INVISIBLE
            }

        }
        blockCaiguruUser()

        SettingUpToolbar()
        setViewAllText()

        //**************web service of get other profile*************//
        mvmodel.getOtherProfileData(data, usertype)
        //set the click btnFollow
        mbinding.btnFollow.setOnClickListener {
            follow_status = 1
            mbinding.btnFollow.visibility = View.VISIBLE
            mbinding.btnunfollow.visibility = View.GONE
            mbinding.loader.visibility = View.VISIBLE
            mvmodel.setFollowUnfollow(data, follow_status)

        }
        //set the click btnFollow
        mbinding.btnunfollow.setOnClickListener {
            // hasFollow = false
            follow_status = 2
            mbinding.btnFollow.visibility = View.VISIBLE
            mbinding.btnunfollow.visibility = View.GONE
            mbinding.loader.visibility = View.VISIBLE
            mvmodel.setFollowUnfollow(data, follow_status)

        }

        mbinding.progressPagination.visibility = View.VISIBLE
        //follow or Unfollow
        if (hasFollow) {
            hasFollow = false
            follow_status = 1
            mbinding.btnFollow.visibility = View.VISIBLE
            mbinding.btnunfollow.visibility = View.GONE

        } else {
            hasFollow = true
            follow_status = 2
            mbinding.btnFollow.visibility = View.GONE
            mbinding.btnunfollow.visibility = View.VISIBLE
        }


        //***********set the observer of get profile api****************//
        mvmodel.getdataSucessful().observe(this, Observer {
            parentLayoutAll.visibility = View.VISIBLE

            mbinding.parentLayoutAll.visibility = View.VISIBLE
            mbinding.progressPagination.visibility = View.GONE
            mbinding.txtNoData.visibility = View.GONE
            globalOtherPrileData = it
            //set the adapter
            //visibility check
            if (it.lists.size > 0) {
                textNoData.visibility = View.GONE
                adapter.update(it.lists)
            } else {
                textNoData.visibility = View.VISIBLE
            }
            //textNoData1
//same  for reviews case
            if (it.reviews.size > 0) {
                textNoData1.visibility = View.GONE
                adapterReviews.upDate(it.reviews)
            } else {
                textNoData1.visibility = View.VISIBLE
            }

            if (it.lists.size > 1) {
                mbinding.viewAll.visibility = View.VISIBLE
            } else {
                mbinding.viewAll.visibility = View.GONE
            }
            //visibility check of reviews
            if (it.reviews.size > 3) {
                mbinding.reviewsViewAll.visibility = View.VISIBLE
            } else {
                mbinding.reviewsViewAll.visibility = View.GONE
            }
            //set text for raputation
            reputationTextSet(it)
            LevelTextSet(it)// set level text
            completedOrderTextSet(it)
            userProfileSet(it)
            userLevelImageBadgeSet(it)
            followOrUnfolowCheck(it)


        })
        //failure
        mvmodel.errorData().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            mbinding.progressPagination.visibility = View.GONE
            mbinding.txtNoData.visibility = View.VISIBLE
            mbinding.parentLayoutAll.visibility = View.GONE

        })

        //*********************observer of follow or unfollow**************//
        mvmodel.mSucessfullFollowUnfollow().observe(this, Observer {

            if (it.follow_status == "1") {
                mbinding.btnFollow.visibility = View.GONE
                mbinding.btnunfollow.visibility = View.VISIBLE
                mbinding.loader.visibility = View.GONE
            } else {
                mbinding.btnFollow.visibility = View.VISIBLE
                mbinding.loader.visibility = View.GONE
                mbinding.btnunfollow.visibility = View.GONE
            }
        })
//failure
        mvmodel.mFailureeFollowUnfollow().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

        })
        //set the click on the view all
        mbinding.viewAll.setOnClickListener {
            val intent = Intent(this, ViewAllShoppingActivity::class.java)
            intent.putExtra("keyViewAllData", globalOtherPrileData)
            startActivity(intent)

        }

        mbinding.reviewsViewAll.setOnClickListener {
            val intent = Intent(this, OtherProfileReviewsAllActivity::class.java)
            intent.putExtra("keyViewAllData", globalOtherPrileData)
            startActivity(intent)

        }
        //********blocked user sucessful
        mvmodel.getSucessfulBlockedUser().observe(this, Observer {
            progressPagination.visibility = View.INVISIBLE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant. apiHitOrNot = 0
            finish()
        })
        //********blocked user failure
        mvmodel.errorgetBlockedUser().observe(this, Observer {
            progressPagination.visibility = View.INVISIBLE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

    }


    private fun blockCaiguruUser() {
        blockUser.setOnClickListener {
            val mDialog = AlertDialog.Builder(this)
            mDialog.setTitle(getString(R.string.alert))
            mDialog.setCancelable(true)
            mDialog.setMessage(getString(R.string.blocked_user_text))
            mDialog.setPositiveButton(
                getString(R.string.ok),
                DialogInterface.OnClickListener { dialog, which ->
                    mvmodel.blockedUser(data, usertype)
                    progressPagination.visibility = View.VISIBLE
                    dialog.cancel()
                })
            mDialog.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                })
            mDialog.show()
        }
    }

    private fun setViewAllText() {
        val udata = getString(R.string.View_all)
        val text = SpannableString(udata)
        text.setSpan(UnderlineSpan(), 0, udata.length, 0)
        viewAll.text = text
        reviewsViewAll.text = text
    }

    private fun followOrUnfolowCheck(it: GetProfileModel) {
        if (it.isFollowed == "0") {
            hasFollow = false
            mbinding.btnFollow.visibility = View.VISIBLE
            mbinding.btnunfollow.visibility = View.GONE

        } else {
            hasFollow = true
            mbinding.btnFollow.visibility = View.GONE
            mbinding.btnunfollow.visibility = View.VISIBLE
        }

    }

    private fun userLevelImageBadgeSet(it: GetProfileModel) {
        if (it.type == "2") {
            if (it.image.isEmpty()) {
                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.imgBatchs)
            } else {
                Glide.with(this)
                    .load(setSellerLeveLBatch(it.level))
                    .into(mbinding.imgBatchs)
            }
        } else {
            if (it.image.isEmpty()) {
                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.imgBatchs)
            } else {
                Glide.with(this)
                    .load(setBuyerLevel(it.level))
                    .into(mbinding.imgBatchs)
            }
        }
    }

    private fun setSellerLeveLBatch(level: String): Int {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category.levelImage
            }
        }
        return 0
    }


    private fun userProfileSet(it: GetProfileModel) {
        if (it.image.isEmpty()) {
            Glide.with(this)
                .load(R.drawable.user_placeholder)
                .into(mbinding.userPic)
        } else {
            Glide.with(this)
                .load(it.image)
                .into(mbinding.userPic)
        }

    }

    private fun completedOrderTextSet(it: GetProfileModel) {

        val ReputationText = "${getString(R.string.completed_order)}"
        val fulltext = "$ReputationText ${it.completed_orders}"
        val spannable = SpannableString(fulltext)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            ReputationText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            ReputationText.length,
            fulltext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            boldSpan, ReputationText.length,
            fulltext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        completedOrder.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun LevelTextSet(it: GetProfileModel) {
        if (it.type == "2") {
            ReputationText = "${getString(R.string.level)}"
            fulltext =
                "$ReputationText ${SellerLevelGet(it.level)} ${"("}${it.points} ${getString(R.string.points)}${")"}"
        } else {
            ReputationText = "${getString(R.string.level)}"
            fulltext =
                "$ReputationText ${BuyerLevelGet(it.level)} ${"("}${it.points} ${getString(R.string.points)}${")"}"
        }

        val spannable = SpannableString(fulltext)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            ReputationText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            ReputationText.length,
            fulltext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan, ReputationText.length,
            fulltext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        level.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    private fun SellerLevelGet(level: String): String {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category.levelname
            }
        }
        return " "

    }

    private fun BuyerLevelGet(level: String): String {
        val buyerlevel = Constant.BuyerLevel(this)
        for (category in buyerlevel) {

            if (category.levelType == level.trim()) {

                return category.levelname
            }
        }
        return " "
    }


    private fun setBuyerLevel(level: String): Int {

        val BuyerLevel = Constant.BuyerLevel(this)
        for (category in BuyerLevel) {
            if (category.levelType == level.trim()) {
                return category.levelImage
            }
        }
        return 0
    }

    private fun reputationTextSet(it: GetProfileModel) {
        val ReputationText = "${getString(R.string.reputation)}"
        val fulltext = "$ReputationText ${Constant.getReputationString(this, it.reputation)}"
        val spannable = SpannableString(fulltext)
        val boldSpan = StyleSpan(Typeface.BOLD)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            ReputationText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            ReputationText.length,
            fulltext.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan, ReputationText.length,
            fulltext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        reputation.setText(spannable, TextView.BufferType.SPANNABLE)


    }

    //.........setuptool bar..............//
    @SuppressLint("SetTextI18n")
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbarProfile)
        blockUser.visibility = View.VISIBLE
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbarProfileText)
        //  text.text = buyer_name + "'s" + " " + getString(R.string.profile)
        text.text = buyer_name
        text.setSelected(true)
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
        adapter = OtherProfileShoppingListParentAdapter(this)
        recyclerList.layoutManager = manager
        recyclerList.adapter = adapter
    }

    private fun setAdapterReviews() {
        val manager = LinearLayoutManager(this)
        adapterReviews = OtherProfileLatestReviewsAdapter(this)
        recyclerLatestReview.layoutManager = manager
        recyclerLatestReview.adapter = adapterReviews
    }
}
