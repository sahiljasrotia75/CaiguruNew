package com.example.caiguru.commonScreens.dashBoardParentActivity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.text.InputFilter
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.BuyerAddressMapBoxActivity
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerShopListApproveRejectActivity
import com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment.BuyerShoppingFragment
import com.example.caiguru.buyer.buyerOrder.BuyerOrderFragment
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.BuyerOrderDetailsActivity
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.underReviewOrderDetails.UnderReviewOrderDetailsActivity
import com.example.caiguru.buyer.buyerProfile.buyerSettingFragment.BuyerSettingFragment
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeBuyerFragment
import com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime.BuyerPostShoppingListFragment
import com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime.BuyerPostViewModel
import com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime.PostBuyerShopListModel
import com.example.caiguru.buyer.postList.shoppingListPosted.BuyerShoppingListPostedActivity
import com.example.caiguru.commonScreens.chat.ChatActivity
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.chatMessage.ChatMessagesActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.CommonNotificationActivity
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation.CommonNotificationConfirmationActivity
import com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification.ResolutionActivity
import com.example.caiguru.commonScreens.commonNotifications.rateBuyer.RateBuyerActivity
import com.example.caiguru.commonScreens.commonNotifications.rateSeller.RateSellerActivity
import com.example.caiguru.commonScreens.dashBoardParentActivity.dashboardStartingNotification.sellerUnSeenNotification.CustomPagerAdapter
import com.example.caiguru.commonScreens.dashBoardParentActivity.dashboardStartingNotification.sellerUnSeenNotification.ZoomOutPageTransformer
import com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller.SearchSellerProuctsActivity
import com.example.caiguru.commonScreens.earnCreditsConvert.CreditConvertActivity
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.commonScreens.loginScreen.LoginActivity
import com.example.caiguru.commonScreens.networkError.NetworkErrorActivity
import com.example.caiguru.commonScreens.referralCode.ReferralCodeActivity
import com.example.caiguru.commonScreens.selectCities.CitiesModel
import com.example.caiguru.commonScreens.selectCities.SelectCitiesActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.homeSeller.HomeSellerFragment
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.SellerApprovalOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList.SellerCancelledOrderListActivity
import com.example.caiguru.seller.sellerOrder.sellerOrdersFragment.SellerOrderFragment
import com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList.ToBeDeliveredOrderListActivity
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListSeller.ShopListSellerFragment
import com.example.caiguru.seller.shoppingfragment.SellerSettingFragment
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activate_location_layout.view.*
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.buyer_add_address_dialog.*
import kotlinx.android.synthetic.main.dialog_active.*
import kotlinx.android.synthetic.main.notification_badge.*
import kotlinx.android.synthetic.main.notification_badge.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import me.relex.circleindicator.Config
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class DashBoardBuyerActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    HomeBuyerFragment.SwitchListener, HomeSellerFragment.SwitchListenerBuyer,
    BuyerPostShoppingListFragment.HideButton, CustomPagerAdapter.setBuyerInterface {

    private var resultCode1: String = "0"
    val PERMISSION_ID = 42
    private var creditPerProduct: String = ""
    var freeProducts = 0
    private var buyerLevel: String = ""
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var optionalDeliveryZoneModel = DeliveryZoneModel()
    private lateinit var addressDialog: Dialog
    private lateinit var tokenUser: String
    private var getNotificationModel = ArrayList<NotificationModel>()
    private lateinit var myCustomPagerAdapter: CustomPagerAdapter
    private var tokenFirebase: String = ""
    private lateinit var viewModelDashBoard: DashBoardViewModel

    //  private var menuMain: Menu? = null
    private var position = 0
    private var fragmentCurrent = Fragment()
    lateinit var mvmodel: BuyerPostViewModel
    private lateinit var dialog: Dialog
    private lateinit var mBottomNavigationBuyer: BottomNavigationView
    private lateinit var mBottomNavigationSeller: BottomNavigationView
    var sellerSelectedTap = 0
    private var buyerCredits: String = ""
    lateinit var textToolbarTitle: TextView
    lateinit var done: TextView
    lateinit var PleasWait: TextView
    var showDialog = 0
    // var hideUnhideIcon=0

    @TargetApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        addressDialog = Dialog(this)

        // mvmodel = ViewModelProviders.of(this)[BuyerPostViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerPostViewModel::class.java)
        viewModelDashBoard = ViewModelProvider(this).get(DashBoardViewModel::class.java)
        val shredpref = getSharedPreferences("yogeshData", Context.MODE_PRIVATE)
        tokenFirebase = shredpref.getString("deviceId", "")!!
        // Add code to print out the key hash
        // Constant.printHashKey(this)

        init()
        LOgoutBannedUser()
        toolBarSet()
        switchSellerBuyerCheckFirstTime()
        setAllObserver()//al observer
        pushNotificationCheck() //posh notication api data check
        allButtonClick()//all clicks


    }

    private fun allButtonClick() {
        setwhatsAppIConClick()//whatsapp click
        //********************************skip button of view pager
        ViewPagerSkipButton.setOnClickListener {
            viewPagerLayout.visibility = View.GONE
            viewPager.visibility = View.GONE
            indicator_login.visibility = View.GONE
            ViewPagerSkipButton.visibility = View.GONE
        }
        // ****************************************Post buyer list************************************//
        done = findViewById(R.id.done)
        PleasWait = findViewById(R.id.PleasWait)
        done.setOnClickListener {
            if (fragmentCurrent is BuyerPostShoppingListFragment) {   // is BuyerPostShoppingFragment is a instance of fragment ???..
                val model: PostBuyerShopListModel = (fragmentCurrent as BuyerPostShoppingListFragment).buyer()  // casting  of BuyerPostShoppingFragment

                if (model.listingname.isEmpty()) {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.Please_Enter_Name_Of_The_List),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Constant.showToast(getString(R.string.Please_Enter_Name_Of_The_List), this)
                } else if (model.category_id.isEmpty()) {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.Please_Select_Category),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Constant.showToast(getString(R.string.Please_Select_Category), this)
                } else if (model.daysTimeZoneJsonArray == "[]") {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.Please_Select_Days_You_Recive_Order),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Constant.showToast(
                        getString(R.string.Please_Select_Days_You_Recive_Order),
                        this
                    )

                } else if (model.shoppingProductListJSonArray == "[]") {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.Please_Add_Your_Product),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Constant.showToast(getString(R.string.Please_Add_Your_Product), this)

                } else if (model.PostShoppingModel.size < 2) {
                    Constant.firstTimeCaiguruDialog(this)
                    return@setOnClickListener

                } else if (model.deliveryZoneJsonList == "[]") {
//                    Toast.makeText(
//                        this,
//                        getString(R.string.Please_Select_Your_Location),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Constant.showToast(getString(R.string.Please_Select_Your_Location), this)
                } else {
//                    if (model.totalCredits > 0) {
                    if (model.PostShoppingModel.size > creditDeductionLogic()) {
                        //after five we are set the alert
                        AlertUserAddProductAfterFive(model)
                    } else {
                        viewModelDashBoard.createShoppingList(
                            model.listingname,
                            model.category_id,
                            model.deliveryZoneJsonList,
                            model.daysTimeZoneJsonArray,
                            model.shoppingProductListJSonArray,
                            model.listId,
                            model.credDitToDeduct
                        )
                        PleasWait.visibility = View.VISIBLE
                        done.visibility = View.GONE
                    }

//                    } else {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.You_have_no_credits_in_your_wallet),
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
                }
            }
        }


        homeToolbar.chat.setOnClickListener {
            val intent = Intent(this@DashBoardBuyerActivity, ChatActivity::class.java)
            startActivityForResult(intent, 1010)
        }
        //homeToolbar.refferal.visibility = View.VISIBLE
        homeToolbar.refferal.setOnClickListener {
            val intent = Intent(this@DashBoardBuyerActivity, ReferralCodeActivity::class.java)
            startActivity(intent)
        }
        // show the icon
        homeToolbar.setSearch.setOnClickListener {
            // Constant.customDialogShowTotalCredits(this)
            val intent = Intent(this, SearchSellerProuctsActivity::class.java)
            intent.putExtra("KeyAddressModel", optionalDeliveryZoneModel)
            startActivity(intent)
        }
        //set the click on the dollar
        homeToolbar.checkTotalCredits.setOnClickListener {
            Constant.customDialogShowTotalCredits(this)

        }
    }


    private fun pushNotificationCheck() {
        //notificationApi
        if (intent.hasExtra("KeySourceDashboard11")) {
            val dataModel = intent.getParcelableExtra<NotificationModel>("KeySourceDashboard11")
            viewModelDashBoard.notificationRead(dataModel!!.notification_id)
        }

        //*****************push notification Data***************//
        if (intent.hasExtra("source_id")) {
            val sourceId = intent.getStringExtra("source_id")
            if (sourceId!!.isNotEmpty() && sourceId != "0") {
                PushNotificationIntegration()
            }

        }
    }

    private fun setAllObserver() {
        //******************************Switch active response ****************************//
        mvmodel.getdata().observe(this, Observer {
            //   if (it != null) {
            DashBoardProgress.visibility = View.GONE
            val type = Constant.getPrefs(this).getString(Constant.type, "")
            val switch = Constant.getPrefs(this).getString(Constant.switch_active, "")
            if (switch == "2" && type == "2") {
//                val firstTime = Constant.getPrefs(this)
//                    .getString(Constant.is_seller_first_time, "")
//                if (firstTime == "1") {
//                    firstTimeCaiguruDialog()//set the popup first time
//                }
                if (Constant.startFirstTime == true) {
                    Constant.startFirstTime = false
                    Constant.stopBacgroundTimer = 0
                    showWhatsappIconBlinking()

                }
                showChatCount()//show  count
                AddressLayout.visibility = View.GONE
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.whatsAppText.visibility = View.VISIBLE
                homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                homeToolbar.refferal.visibility = View.GONE
                homeToolbar.checkTotalCredits.visibility = View.GONE
                homeToolbar.setSearch.visibility = View.GONE
                sellernav.visibility = View.VISIBLE
                buyernav.visibility = View.GONE
                //  mbinding.sellernav.setOnNavigationItemSelectedListener(this)
                loadFragment(HomeSellerFragment())
                if (sellerSelectedTap == 0) {
                    showSelected(getString(R.string.home), 0, 0)
                    showSelected(getString(R.string.sell_now), 1, 1)
                    showSelected(getString(R.string.orders), 3, 1)
                    showSelected(getString(R.string.Settings), 4, 1)
                } else if (sellerSelectedTap == 1) {
                    showSelected(getString(R.string.home), 0, 1)
                    showSelected(getString(R.string.sell_now), 1, 0)
                    showSelected(getString(R.string.orders), 3, 1)
                    showSelected(getString(R.string.Settings), 4, 1)
                } else if (sellerSelectedTap == 3) {
                    showSelected(getString(R.string.home), 0, 1)
                    showSelected(getString(R.string.sell_now), 1, 1)
                    showSelected(getString(R.string.orders), 3, 0)
                    showSelected(getString(R.string.Settings), 4, 1)
                } else if (sellerSelectedTap == 4) {
                    showSelected(getString(R.string.home), 0, 1)
                    showSelected(getString(R.string.sell_now), 1, 1)
                    showSelected(getString(R.string.orders), 3, 1)
                    showSelected(getString(R.string.Settings), 4, 0)
                }
                //seller notification api
                if (!intent.hasExtra("source_id")) {
                    viewModelDashBoard.getHomeSellerNotification(tokenUser)
                }
            }
            else if (switch == "1" && type == "2") {

                if (Constant.startFirstTime == true) {
                    Constant.startFirstTime = false
                    Constant.stopBacgroundTimer = 0
                    showWhatsappIconBlinking()

                }
                showChatCount()//show chat count
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.whatsAppText.visibility = View.VISIBLE
                homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                homeToolbar.refferal.visibility = View.GONE
                homeToolbar.checkTotalCredits.visibility = View.GONE
                homeToolbar.setSearch.visibility = View.GONE
                sellernav.visibility = View.VISIBLE
                buyernav.visibility = View.GONE
                AddressLayout.visibility = View.GONE

//                val firstTime = Constant.getPrefs(this)
//                    .getString(Constant.is_seller_first_time, "")
//                if (firstTime == "1") {
//                    firstTimeCaiguruDialog()//set the popup first time
//                }
                //   mbinding.sellernav.setOnNavigationItemSelectedListener(this)
                loadFragment(HomeSellerFragment())
                if (sellerSelectedTap == 0) {
                    showSelected(getString(R.string.home), 0, 0)
                    showSelected(getString(R.string.sell_now), 1, 1)
                    showSelected(getString(R.string.orders), 3, 1)
                    showSelected(getString(R.string.Settings), 4, 1)
                } else if (sellerSelectedTap == 1) {
                    showSelected(getString(R.string.home), 0, 1)
                    showSelected(getString(R.string.sell_now), 1, 0)
                    showSelected(getString(R.string.orders), 3, 1)
                    showSelected(getString(R.string.Settings), 4, 1)
                } else if (sellerSelectedTap == 3) {
                    showSelected(getString(R.string.home), 0, 1)
                    showSelected(getString(R.string.sell_now), 1, 1)
                    showSelected(getString(R.string.orders), 3, 0)
                    showSelected(getString(R.string.Settings), 4, 1)
                } else if (sellerSelectedTap == 4) {
                    showSelected(getString(R.string.home), 0, 1)
                    showSelected(getString(R.string.sell_now), 1, 1)
                    showSelected(getString(R.string.orders), 3, 1)
                    showSelected(getString(R.string.Settings), 4, 0)
                }
                //seller notification api
                if (!intent.hasExtra("source_id")) {
                    viewModelDashBoard.getHomeSellerNotification(tokenUser)
                }

            }
            else {
                showChatCount()//show chat count
                showAdressOnHomePage()//show home Adress
                Constant.startFirstTime = true
                Constant.stopBacgroundTimer = 1
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.refferal.visibility = View.VISIBLE
                homeToolbar.checkTotalCredits.visibility = View.GONE
                homeToolbar.whatsAppText.visibility = View.GONE
                homeToolbar.whatsAppTextICons.visibility = View.GONE
                homeToolbar.setSearch.visibility = View.VISIBLE
                buyernav.visibility = View.VISIBLE
                sellernav.visibility = View.GONE
                AddressLayout.visibility = View.VISIBLE
                loadFragment(HomeBuyerFragment())
                textToolbarTitle.text = getString(R.string.home)
                mBottomNavigationBuyer.menu.getItem(0).isChecked = true
                //set the api
                //buyer notification api
                if (!intent.hasExtra("source_id")) {

                    viewModelDashBoard.getHomePageBuyerNotification(tokenUser)
                }
            }

        })
        //************failure observer************//
        mvmodel.errorget().observe(this, Observer {
            DashBoardProgress.visibility = View.GONE
            //hideLoader()
            //    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, this)
        })

        //******************observer of get profile*****************//
        viewModelDashBoard.getdataProfile().observe(this, Observer {
            if (Constant.getProfileData(this).finalizeCount.isNotEmpty()) {
                if (Constant.getProfileData(this).finalizeCount.toInt() > 0) {
                    notificationsBadge.text = Constant.getProfileData(this).finalizeCount
                    notificationsBadge.visibility = View.VISIBLE
                } else {
                    notificationsBadge.visibility = View.GONE
                }
            } else {
                notificationsBadge.visibility = View.GONE
            }
            //start the background webServices

//            if (firstTimeBackGroundApiHit == 0) {
//                Log.e("bgApiHIt1",firstTimeBackGroundApiHit.toString())
//               // startService(Intent(this, MyBackgroundServices::class.java))
//                viewModelDashBoard.hitBackGroundService()
//                firstTimeBackGroundApiHit = 1
//            }
//            else {
//                Log.e("bgApiHIt1",firstTimeBackGroundApiHit.toString())
//                stopService(Intent(this, MyBackgroundServices::class.java))
//            }

            if (resultCode1 == "1010") {
                resultCode1 = "0"
                if (it.unreadcount == "0" || it.unreadcount.isEmpty()) {
                    homeToolbar.chat.visibility = View.VISIBLE
                    homeToolbar.toolbarMesageButton.visibility = View.GONE
                } else {
                    homeToolbar.toolbarMesageButton.visibility = View.VISIBLE
                    homeToolbar.toolbarMesageButton.text = it.unreadcount
                }
            }

            val seller_active_status =
                Constant.getPrefs(this).getString(Constant.seller_active_status, "")
            if (seller_active_status == "1") {
                mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                changeTitleColor(1)
            } else {
                mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                changeTitleColor(2)
            }
//****************show dialog first time***************//
            if (showDialog == 0) {
                val gson = Gson()
                val json = Constant.getPrefs(this).getString("profile", "1")
                val typeCheck = Constant.getPrefs(this).getString(Constant.type, "")
                if (json == "1" && typeCheck == "1") {
                    getLastLocation()//finding the location
                } else {
                    if (json != "1") {
                        val profileModel = gson.fromJson(json, GetProfileModel::class.java)
                        if (profileModel.lat.isEmpty() && profileModel.long.isEmpty()) {
                            getLastLocation()//finding the location
                        }
                    }
                }
            }
            //***********scheck the location enable or not?
            // getLastLocation()//finding the location
            activateLocationClick()
            if (Constant.UpdateAddressInBackGround == "0") {
                showAdressOnHomePage()
            } else {
                Constant.UpdateAddressInBackGround = "1"
            }
            viewModelDashBoard.hitBackGroundService()
        })
        //*********************failure of get profile
        viewModelDashBoard.errorgetProfile().observe(this, Observer {
            //   Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, this)
            val intent = Intent(this, NetworkErrorActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        })

//*******************************************Seller Status active inactive ******************//
        mvmodel.getsellerStatus().observe(this, Observer {
            progress.visibility = View.GONE
            if (sellerSelectedTap == 0) {
                viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
                loadFragment(HomeSellerFragment())
                if (Constant.startFirstTime == true) {
                    showWhatsappIconBlinking()
                    Constant.stopBacgroundTimer = 0
                }
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.whatsAppText.visibility = View.VISIBLE
                homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                homeToolbar.refferal.visibility = View.GONE
                showChatCount()//show chat count
                homeToolbar.checkTotalCredits.visibility = View.GONE
                homeToolbar.setSearch.visibility = View.GONE
                mBottomNavigationSeller.menu.getItem(0).isChecked = true


                mBottomNavigationSeller.menu.getItem(0).setIcon(R.drawable.ic_home_active_seller)

                mBottomNavigationSeller.menu.getItem(1).setIcon(R.drawable.ic_shoppinglist_inactive_seller)
                val seller_active_status = Constant.getPrefs(this).getString(Constant.seller_active_status, "")

                if (seller_active_status == "1") {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                    changeTitleColor(1)
                } else {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                    changeTitleColor(2)
                }

                mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_inactive)
                mBottomNavigationSeller.menu.getItem(4).setIcon(R.drawable.ic_setting_inactive_seller)

            } else if (sellerSelectedTap == 1) {
                mBottomNavigationSeller.menu.getItem(1).isChecked = true
                mBottomNavigationSeller.menu.getItem(1).isChecked = true
                mBottomNavigationSeller.menu.getItem(0)
                    .setIcon(R.drawable.ic_home_inactive_seller)
                mBottomNavigationSeller.menu.getItem(1)
                    .setIcon(R.drawable.ic_shoppinglist_active_seller)
                mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_inactive)
                mBottomNavigationSeller.menu.getItem(4)
                    .setIcon(R.drawable.ic_setting_inactive_seller)
                val seller_active_status =
                    Constant.getPrefs(this).getString(Constant.seller_active_status, "")
                if (seller_active_status == "1") {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                    changeTitleColor(1)
                } else {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                    changeTitleColor(2)
                }

                loadFragment(ShopListSellerFragment())
            } else if (sellerSelectedTap == 3) {
                mBottomNavigationSeller.menu.getItem(3).isChecked = true
                loadFragment(SellerOrderFragment())
                textToolbarTitle.text = getString(R.string.orders)
                mBottomNavigationSeller.menu.getItem(0)
                    .setIcon(R.drawable.ic_home_inactive_seller)
                mBottomNavigationSeller.menu.getItem(1)
                    .setIcon(R.drawable.ic_shoppinglist_inactive_seller)
                val seller_active_status =
                    Constant.getPrefs(this).getString(Constant.seller_active_status, "")
                if (seller_active_status == "1") {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                    changeTitleColor(1)
                } else {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                    changeTitleColor(2)

                }
                mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_active)
                mBottomNavigationSeller.menu.getItem(4)
                    .setIcon(R.drawable.ic_setting_inactive_seller)

            } else if (sellerSelectedTap == 4) {
                mBottomNavigationSeller.menu.getItem(4).isChecked = true
                loadFragment(SellerSettingFragment())
                textToolbarTitle.text = getString(R.string.Settings)
                mBottomNavigationSeller.menu.getItem(4).isChecked = true
                mBottomNavigationSeller.menu.getItem(0)
                    .setIcon(R.drawable.ic_home_inactive_seller)
                mBottomNavigationSeller.menu.getItem(1)
                    .setIcon(R.drawable.ic_shoppinglist_inactive_seller)
                val seller_active_status =
                    Constant.getPrefs(this).getString(Constant.seller_active_status, "")
                if (seller_active_status == "1") {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                    //  mBottomNavigationSeller.menu.getItem(2).setTitle
                    changeTitleColor(1)
                } else {
                    mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)

                    changeTitleColor(2)

                }
                mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_inactive)
                mBottomNavigationSeller.menu.getItem(4).setIcon(R.drawable.ic_setting_active_seller)

            }
            mBottomNavigationSeller.setOnNavigationItemSelectedListener(this)
            //   Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, this)

        })
        //************failure observer************//
        mvmodel.errorgetStatus().observe(this, Observer {
            progress.visibility = View.GONE
            // Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, this)
        })
//***********observer of update address by the first time dialog
        viewModelDashBoard.mUpdateAddress().observe(this, Observer {
            try {
                //clear the address model
                ClearAddressData()
                addressDialog.updateButtonWait.visibility = View.GONE
                addressDialog.updateButton.visibility = View.VISIBLE
                activateLocationLayout.visibility = View.GONE//hide location activation screen
                Constant.showToast(getString(R.string.Address_updated_sucessfully), this)
                addressDialog.dismiss()
                //*****************address set  because they will not update the address first time
                toolbarAdressHome.visibility = View.VISIBLE
                toolbarAdressHome.setText(it.address)
                // viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
                showChatCount()//show chat count
                showAdressOnHomePage()
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.refferal.visibility = View.VISIBLE
                homeToolbar.checkTotalCredits.visibility = View.GONE
                Constant.stopBacgroundTimer = 1
                Constant.startFirstTime = true
                homeToolbar.whatsAppText.visibility = View.GONE
                homeToolbar.whatsAppTextICons.visibility = View.GONE
                homeToolbar.setSearch.visibility = View.VISIBLE
                AddressLayout.visibility = View.VISIBLE
                buyernav.visibility = View.VISIBLE
                sellernav.visibility = View.GONE
                loadFragment(HomeBuyerFragment())
                textToolbarTitle.text = getString(R.string.home)
                mBottomNavigationBuyer.menu.getItem(0).isChecked = true
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e("firstTimeAddressCrash", e.toString())
            }
        })
//****************address failure observer
        viewModelDashBoard.failueAddress().observe(this, Observer {
            Constant.showToast(it, this)
            addressDialog.dismiss()
            addressDialog.updateButtonWait.visibility = View.GONE
            addressDialog.updateButton.visibility = View.VISIBLE
        })

        //***********notification observer***********//
        viewModelDashBoard.mSucessfulNotification().observe(this, Observer {
            getNotificationModel = it
            if (it.isEmpty()) {
                viewPagerLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                indicator_login.visibility = View.GONE
                ViewPagerSkipButton.visibility = View.GONE
            } else {
                viewPagerLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                indicator_login.visibility = View.VISIBLE
                ViewPagerSkipButton.visibility = View.VISIBLE
                myCustomPagerAdapter =
                    CustomPagerAdapter(
                        this,
                        it
                    )
                viewPager.setPageTransformer(
                    true,
                    ZoomOutPageTransformer()
                )
                viewPager!!.adapter = myCustomPagerAdapter
                viewPagerIndicator()
            }
        })
        //************failure observer************//
        viewModelDashBoard.mFailure().observe(this, Observer {
            //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            viewPagerLayout.visibility = View.GONE
            viewPager.visibility = View.GONE
            indicator_login.visibility = View.GONE
            ViewPagerSkipButton.visibility = View.GONE
        })

        //************buyer shopping list posted observer***********************//
        viewModelDashBoard.mSucessfulCreateShoppingList().observe(this, Observer {
            PleasWait.visibility = View.GONE
            done.visibility = View.VISIBLE
            val intent = Intent(this, BuyerShoppingListPostedActivity::class.java)
            intent.putExtra("keyshopping", it)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivityForResult(intent, 999)
        })
        //***********************failure case
        viewModelDashBoard.mFailureShoppingError().observe(this, Observer {
            //visibilty
            PleasWait.visibility = View.GONE
            done.visibility = View.VISIBLE
            val msg = it
            showErrorDialog(msg)
        })

    }

    private fun switchSellerBuyerCheckFirstTime() {
        DashBoardProgress.visibility = View.VISIBLE
        //***********************************switching or type api********************//
        val type1 = Constant.getPrefs(this).getString(Constant.type, "")
        val switch1 = Constant.getPrefs(this).getString(Constant.switch_active, "")
        if (switch1 == "2" && type1 == "2") {
            //seller notification api
//            if (!intent.hasExtra("source_id")) {
//                viewModelDashBoard.getHomeSellerNotification(tokenUser)
//            }
            mvmodel.switch_setting("2", "2")//both permanent ths user is seller
        } else if (switch1 == "1" && type1 == "2") {//not permanent but seller
            //seller notification api
//            if (!intent.hasExtra("source_id")) {
//                viewModelDashBoard.getHomeSellerNotification(tokenUser)
//            }
            mvmodel.switch_setting("1", "2")
        } else {
            //buyer notification api
//            if (!intent.hasExtra("source_id")) {
//
//                viewModelDashBoard.getHomePageBuyerNotification(tokenUser)
//            }
            mvmodel.switch_setting("1", "1")//not permanent but buyer
        }
        // permanently switch to seller switch 2
        // not permanently switch to seller switch 1
        //type 1 buyer and 2 for seller
    }

    private fun toolBarSet() {
        val toolbar = findViewById<Toolbar>(R.id.homeToolbar)
        title = ""
        textToolbarTitle = findViewById(R.id.toolbartittle)
        textToolbarTitle.text = getString(R.string.home_screen_title)
        setSupportActionBar(toolbar)
        setBuyerHomeAddressTop()
        toolbarAdressHome.visibility = View.INVISIBLE
        tokenUser = "Bearer " + Constant.getPrefs(this).getString(
            Constant.token,
            ""
        )
    }

    private fun LOgoutBannedUser() {
        viewModelDashBoard.logoutBannedUser().observe(this, Observer {
            //    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, this)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    //***************************buyer homePage Top address code*****************//
    private fun setBuyerHomeAddressTop() {
        AddressLayout.setOnClickListener {
//set the dialog on click
            addAddressHomeCustomDialog()
        }
    }

    //*********************filling address first time *****************//
    private fun addAddressCustomDialog(addressModel: DeliveryZoneModel) {
        // *********************************** Add Address by custom dialog ******************************************//
//        addressDialog = Dialog(this)
        addressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addressDialog.setContentView(R.layout.buyer_add_address_dialog)
        addressDialog.setCancelable(false)
        addressDialog.show()
        showDialog = 1
        addressDialog.addresspleaseSelect.text = getString(R.string.is_this_your_address)
        addressDialog.textfeed.visibility = View.VISIBLE
        addressDialog.addressHome.visibility = View.VISIBLE//edttext field visible
        addressDialog.address.visibility = View.INVISIBLE//textfield
        addressDialog.EdtMapLocation.visibility = View.VISIBLE
        addressDialog.addressHome.filters = arrayOf<InputFilter>(HideEmoji(this))
        if (addressModel.address.isNotEmpty()) {
            addressDialog.addressHome.setText(addressModel.address)
            optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                .trim()//set the address if they change anything
        }
        //set the click button of custom dialog
        addressDialog.viewmap.setOnClickListener {
            optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                .trim()//set the address if they change anything

            val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("keyChooseSellerLat", this.optionalDeliveryZoneModel.lat)
            intent.putExtra("keyChooseSellerLong", this.optionalDeliveryZoneModel.long)
            intent.putExtra("SelectAddressHome", "Select address")
            intent.putExtra("firstTimelocationKey", " address")
            intent.putExtra("MapType", this.optionalDeliveryZoneModel.address)
            startActivityForResult(intent, 129)
        }
        //************set the click on the edit
        addressDialog.EdtMapLocation.setOnClickListener {
            optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                .trim()//set the address if they change anything

            val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("keyChooseSellerLat", this.optionalDeliveryZoneModel.lat)
            intent.putExtra("keyChooseSellerLong", this.optionalDeliveryZoneModel.long)
            intent.putExtra("SelectAddressHome", "Select address")
            intent.putExtra("MapType", this.optionalDeliveryZoneModel.address)
            startActivityForResult(intent, 129)
        }


        addressDialog.updateButton.setOnClickListener {
            if (addressDialog.addressHome.text.isEmpty()) {
                Constant.showToast(getString(R.string.Please_Enter_your_Address), this)
            } else if (this.optionalDeliveryZoneModel.lat.isEmpty()) {

                Constant.showToast(getString(R.string.Reselect_address), this)
            } else {
                //setFirstTimeUserData()//first time video popup add the status  to visble the video dialog
//*********api hit
                optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                viewModelDashBoard.updatedAddress(this.optionalDeliveryZoneModel)
                addressDialog.updateButtonWait.visibility = View.VISIBLE
                addressDialog.updateButton.visibility = View.GONE
            }

        }
        addressDialog.cancel.setOnClickListener {
            addressDialog.dismiss()
        }
    }

    private fun showAlertLocationEnabled() {
        val mDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.alert))
            .setMessage(getString(R.string.User_location_are_required))
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            startActivityForResult(
                Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                0
            )
        }

        mDialog.show()
    }

    private fun AlertUserAddProductAfterFive(model: PostBuyerShopListModel) {

        val mDialog = android.app.AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        val credits = Constant.getProfileData(this).credits.toDouble().toInt().toString()

        mDialog.setMessage(
            "${getString(R.string.You_need_to_pay)} ${creditPerProduct} ${getString(R.string.credit_for_additional)} $freeProducts ${
                getString(
                    R.string.products
                )
            }. ${getString(R.string.credits_in_your_account)}: ${credits} ${getString(R.string.cr)}"
        )
        mDialog.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            if (buyerCredits.toDouble() < model.credDitToDeduct) {
                //                    Toast.makeText(this, getString(R.string.You_have_no_credits), Toast.LENGTH_LONG)
                //                        .show()
                Constant.showToast(getString(R.string.You_have_no_credits), this)
                dialog.cancel()
            } else {
                viewModelDashBoard.createShoppingList(
                    model.listingname,
                    model.category_id,
                    model.deliveryZoneJsonList,
                    model.daysTimeZoneJsonArray,
                    model.shoppingProductListJSonArray,
                    model.listId,
                    model.credDitToDeduct
                )
            }


        }
        mDialog.setNegativeButton(
            getString(R.string.no)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
    }

    private fun showErrorDialog(msg: String?) {
        //error dialog case of failure
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(msg)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
    }

    //viewPagerIndicator
    fun viewPagerIndicator() {
        try {


            val indicatorWidth = (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10f,
                resources.displayMetrics
            ) + 0.5f).toInt()
            val indicatorHeight = (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4f,
                resources.displayMetrics
            ) + 0.5f).toInt()
            val indicatorMargin = (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 6f,
                resources.displayMetrics
            ) + 0.5f).toInt()

            val config = Config.Builder().width(indicatorWidth)
                .height(indicatorHeight)
                .margin(indicatorMargin)
                .drawable(R.drawable.counter_bg)
                .drawableUnselected(R.drawable.rectctangle_full_white)
                .build()

            indicator_login.initialize(config)
            viewPager!!.adapter =
                CustomPagerAdapter(
                    this,
                    getNotificationModel
                )//set the adapter
            indicator_login.setViewPager(viewPager)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    //***************set the observer to hide the button buyer post shopping list fragment
    override fun buttonHide() {
        done.visibility = View.VISIBLE
        PleasWait.visibility = View.GONE
    }
    //************************************initialize the bottom navigation that user are which side in liast time***********************************************************//

    private fun init() {
        mBottomNavigationBuyer = findViewById(R.id.buyernav)
        mBottomNavigationSeller = findViewById(R.id.sellernav)
        mBottomNavigationBuyer.setOnNavigationItemSelectedListener(this)
        mBottomNavigationSeller.setOnNavigationItemSelectedListener(this)
        mBottomNavigationSeller.setItemIconTintList(null)
        val type = Constant.getPrefs(this).getString(Constant.type, "")

        if (type == "1") {
            mBottomNavigationBuyer.visibility = View.VISIBLE
            mBottomNavigationSeller.visibility = View.GONE
        } else {
            mBottomNavigationBuyer.visibility = View.GONE
            mBottomNavigationSeller.visibility = View.VISIBLE
        }

        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")
        if (seller_active_status == "1") {
            mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
            changeTitleColor(1)
        } else {
            mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
            changeTitleColor(2)
        }
        showChatCount()//show chat count
        homeToolbar.chat.visibility = View.VISIBLE
//****************************set the badge count on my orders
        val bottomNavigationMenuView = buyernav.getChildAt(0) as BottomNavigationMenuView

        val v: View = bottomNavigationMenuView.getChildAt(1)
        val itemView: BottomNavigationItemView = v as BottomNavigationItemView

        val badge: View = LayoutInflater.from(this)
            .inflate(R.layout.notification_badge, itemView, true)
        if (Constant.getProfileData(this).finalizeCount.isNotEmpty()) {
            if (Constant.getProfileData(this).finalizeCount.toInt() > 0) {
                notificationsBadge.text = Constant.getProfileData(this).finalizeCount
                notificationsBadge.visibility = View.VISIBLE
            } else {
                notificationsBadge.visibility = View.GONE
            }
        } else {
            notificationsBadge.visibility = View.GONE
        }
    }

    fun changeTitleColor(i: Int) {
        if (i == 1) {
            val s = SpannableString(getString(R.string.Active))
            s.setSpan(ForegroundColorSpan(resources.getColor(R.color.green)), 0, s.length, 0)
            mBottomNavigationSeller.menu.getItem(2).title = s
        } else {
            val s = SpannableString(getString(R.string.Inactive))
            s.setSpan(ForegroundColorSpan(resources.getColor(R.color.red)), 0, s.length, 0)
            mBottomNavigationSeller.menu.getItem(2).title = s
        }
    }

    override fun onResume() {
        super.onResume()
        viewModelDashBoard.getProfile(tokenFirebase)
        val isFinish = Constant.getPrefs(this).getString("finish", "no")
        if (isFinish == "yes") {
            Constant.getPrefs(this).edit().putString("finish", "").apply()
        }
        if (fragmentCurrent is BuyerPostShoppingListFragment) {
            loadFragment(fragmentCurrent)
        }


    }
//*****************CHOOSE SELLER CODE*********************//
    //****************************************** set Menu in toolbar ****************************//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        getMenuInflater().inflate(R.menu.choose_seller_menu, menu)
//        menuMain = menu
//        return true
//    }
//*****************CHOOSE SELLER CODE*********************//
    //***************************************Click on menu*******************************//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//
//        if (item!!.itemId == R.id.location) {
//            if (fragmentCurrent is ChooseSellerFragment) {
//                (fragmentCurrent as ChooseSellerFragment).callMenuDialog()
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    //*****************************navigation click**********************************************************//
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        val type = Constant.getPrefs(this).getString(Constant.type, "")
        if (type == "1") {
            when (p0.itemId) {
                R.id.buyer_home -> {
                    position = 0
                    homeToolbar.done.visibility = View.GONE
                    sellernav.visibility = View.GONE
                    buyernav.visibility = View.VISIBLE
                    ClearAddressData()//clear the address
                    //viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
                    homeToolbar.chat.visibility = View.VISIBLE
                    homeToolbar.refferal.visibility = View.VISIBLE
                    homeToolbar.setSearch.visibility = View.VISIBLE
                    homeToolbar.checkTotalCredits.visibility = View.GONE
                    Constant.stopBacgroundTimer = 1
                    Constant.startFirstTime = true
                    homeToolbar.whatsAppText.visibility = View.GONE
                    homeToolbar.whatsAppTextICons.visibility = View.GONE
                    AddressLayout.visibility = View.VISIBLE
                    loadFragment(HomeBuyerFragment())
                    textToolbarTitle.text = getString(R.string.home)
                    mBottomNavigationBuyer.menu.getItem(0).isChecked = true
                    showChatCount()//show chat count
                    showAdressOnHomePage()
                }
                R.id.buyer_seller -> {
                    position = 1
                    viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
                    homeToolbar.done.visibility = View.GONE
                    ClearAddressData()//clear the address
                    //*****************CHOOSE SELLER CODE*********************//
                    //loadFragment(ChooseSellerFragment())

                    if (Constant.getProfileData(this).finalizeCount.isNotEmpty()) {
                        if (Constant.getProfileData(this).finalizeCount.toInt() > 0) {
                            notificationsBadge.text = Constant.getProfileData(this).finalizeCount
                            notificationsBadge.visibility = View.VISIBLE
                        } else {
                            notificationsBadge.visibility = View.GONE
                        }
                    } else {
                        notificationsBadge.visibility = View.GONE
                    }

                    loadFragment(BuyerOrderFragment())
                    textToolbarTitle.text = getString(R.string.orders)
                    homeToolbar.whatsAppText.visibility = View.VISIBLE
                    homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                    homeToolbar.chat.visibility = View.GONE
                    AddressLayout.visibility = View.GONE
                    homeToolbar.refferal.visibility = View.GONE
                    if (Constant.startFirstTime == true) {
                        Constant.startFirstTime = false
                        Constant.stopBacgroundTimer = 0
                        showWhatsappIconBlinking()

                    }
//                    Constant.stopBacgroundTimer = 1
//                    Constant.startFirstTime = true
                    homeToolbar.checkTotalCredits.visibility = View.GONE
                    homeToolbar.setSearch.visibility = View.GONE
                    mBottomNavigationBuyer.menu.getItem(1).isChecked = true
                }

                R.id.buyer_post -> {
                    position = 2
                    val list = getSelectedCitiesList()
                    if (list.size == 0) {
                        val launchIntent = Intent(this, SelectCitiesActivity::class.java)
                        launchIntent.putExtra("switch", "1")
                        launchIntent.putExtra("type", "1")
                        launchIntent.putExtra("from", "2")
                        launchIntent.putExtra("keyHideButton", "hide")
                        // finish()
                        startActivityForResult(launchIntent, 1234)

                    } else {
                        loadFragment(BuyerPostShoppingListFragment())
                        textToolbarTitle.text = getString(R.string.post_shopping_list)
                        homeToolbar.done.visibility = View.VISIBLE
                        homeToolbar.chat.visibility = View.GONE
                        homeToolbar.whatsAppText.visibility = View.GONE
                        Constant.stopBacgroundTimer = 1
                        Constant.startFirstTime = true
                        homeToolbar.whatsAppTextICons.visibility = View.GONE
                        ClearAddressData()//clear the address
                        AddressLayout.visibility = View.GONE
                        homeToolbar.refferal.visibility = View.GONE
                        homeToolbar.checkTotalCredits.visibility = View.VISIBLE
                        homeToolbar.setSearch.visibility = View.GONE
                        mBottomNavigationBuyer.menu.getItem(2).isChecked = true
                    }

                }
                R.id.buyer_shopping_list -> {
                    position = 3
                    loadFragment(BuyerShoppingFragment())
                    homeToolbar.done.visibility = View.GONE
                    ClearAddressData()//clear the address
                    textToolbarTitle.text = getString(R.string.shopping_list)
                    homeToolbar.chat.visibility = View.GONE
                    AddressLayout.visibility = View.GONE
                    homeToolbar.refferal.visibility = View.GONE
                    homeToolbar.whatsAppText.visibility = View.GONE
                    Constant.stopBacgroundTimer = 1
                    Constant.startFirstTime = true
                    homeToolbar.whatsAppTextICons.visibility = View.GONE
                    homeToolbar.checkTotalCredits.visibility = View.GONE
                    homeToolbar.setSearch.visibility = View.GONE
                    //toolbar.inflateMenu(R.menu.choose_seller_menu)
                    mBottomNavigationBuyer.menu.getItem(3).isChecked = true
                }
                R.id.buyer_setting -> {
                    homeToolbar.chat.visibility = View.GONE
                    val json = Constant.getPrefs(this).getString(Constant.PROFILE, "1")
                    if (json != "1") {
                        position = 4
                        viewModelDashBoard.getProfile(tokenFirebase)
                        loadFragment(BuyerSettingFragment())
                        ClearAddressData()//clear the address
                        textToolbarTitle.text = getString(R.string.Settings)
                        homeToolbar.chat.visibility = View.GONE
                        AddressLayout.visibility = View.GONE
                        homeToolbar.refferal.visibility = View.GONE
                        homeToolbar.whatsAppText.visibility = View.GONE
                        Constant.stopBacgroundTimer = 1
                        Constant.startFirstTime = true
                        homeToolbar.whatsAppTextICons.visibility = View.GONE
                        homeToolbar.checkTotalCredits.visibility = View.GONE
                        homeToolbar.setSearch.visibility = View.GONE
                        homeToolbar.done.visibility = View.GONE
                        mBottomNavigationBuyer.menu.getItem(4).isChecked = true

                    } else {
                        viewModelDashBoard.getProfile(tokenFirebase)
                    }
                }
            }
//*****************CHOOSE SELLER CODE*********************//
//            if (position == 1) {
//                menuMain!!.findItem(R.id.location).setVisible(true)
//            } else {
//                menuMain!!.findItem(R.id.location).setVisible(false)
//            }

            mBottomNavigationBuyer.visibility = View.VISIBLE
            mBottomNavigationSeller.visibility = View.GONE

        } else {
            homeToolbar.chat.visibility = View.VISIBLE
            viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
            homeToolbar.refferal.visibility = View.VISIBLE
            homeToolbar.checkTotalCredits.visibility = View.GONE
//            Constant.stopBacgroundTimer = 1
//            Constant.startFirstTime =true
            homeToolbar.whatsAppText.visibility = View.GONE
            homeToolbar.whatsAppTextICons.visibility = View.GONE
            homeToolbar.setSearch.visibility = View.GONE
            when (p0.itemId) {
                R.id.homeSeller -> {
                    sellerSelectedTap = 0
                    sellernav.visibility = View.VISIBLE
                    buyernav.visibility = View.GONE
                    loadFragment(HomeSellerFragment())
                    textToolbarTitle.text = getString(R.string.home)
                    showChatCount()//show chat count
                    showAdressOnHomePage()
                    if (Constant.startFirstTime == true) {
                        Constant.startFirstTime = false
                        Constant.stopBacgroundTimer = 0
                        showWhatsappIconBlinking()

                    }

                    homeToolbar.chat.visibility = View.VISIBLE
                    homeToolbar.whatsAppText.visibility = View.VISIBLE
                    homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                    homeToolbar.refferal.visibility = View.GONE
                    homeToolbar.checkTotalCredits.visibility = View.GONE
                    homeToolbar.setSearch.visibility = View.GONE
                    //  mBottomNavigationBuyer.menu.getItem(0).isChecked = false
                    mBottomNavigationSeller.menu.getItem(0).isChecked = true
                    mBottomNavigationSeller.menu.getItem(0)
                        .setIcon(R.drawable.ic_home_active_seller)
                    mBottomNavigationSeller.menu.getItem(1)
                        .setIcon(R.drawable.ic_shoppinglist_inactive_seller)
                    val seller_active_status =
                        Constant.getPrefs(this).getString(Constant.seller_active_status, "")

                    if (seller_active_status == "1") {
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                        changeTitleColor(1)
                    } else {
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                        changeTitleColor(2)
                    }
                    mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_inactive)
                    mBottomNavigationSeller.menu.getItem(4)
                        .setIcon(R.drawable.ic_setting_inactive_seller)
                }

                R.id.shoppingListSeller -> {
                    sellerSelectedTap = 1
                    // viewModelDashBoard.getProfile(tokenFirebase)
                    loadFragment(ShopListSellerFragment())
                    textToolbarTitle.text = getString(R.string.sell_now)
                    homeToolbar.chat.visibility = View.GONE
                    homeToolbar.whatsAppText.visibility = View.GONE
                    Constant.stopBacgroundTimer = 1
                    Constant.startFirstTime = true
                    homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                    AddressLayout.visibility = View.GONE
                    homeToolbar.refferal.visibility = View.GONE
                    homeToolbar.checkTotalCredits.visibility = View.GONE
                    homeToolbar.setSearch.visibility = View.GONE
                    //  mBottomNavigationBuyer.menu.getItem(1).isChecked = false
                    mBottomNavigationSeller.menu.getItem(1).isChecked = true
                    mBottomNavigationSeller.menu.getItem(0)
                        .setIcon(R.drawable.ic_home_inactive_seller)
                    mBottomNavigationSeller.menu.getItem(1)
                        .setIcon(R.drawable.ic_shoppinglist_active_seller)

                    val seller_active_status =
                        Constant.getPrefs(this).getString(Constant.seller_active_status, "")

                    if (seller_active_status == "1") {
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                        changeTitleColor(1)
                    } else {
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                        changeTitleColor(2)
                    }
                    mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_inactive)
                    mBottomNavigationSeller.menu.getItem(4)
                        .setIcon(R.drawable.ic_setting_inactive_seller)

                }

                R.id.sellerActive -> {
                    if (sellerSelectedTap == 0) {
                        viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
                        showChatCount()//show chat count
                        homeToolbar.chat.visibility = View.VISIBLE
                        homeToolbar.refferal.visibility = View.VISIBLE
                        homeToolbar.checkTotalCredits.visibility = View.GONE
                        homeToolbar.setSearch.visibility = View.GONE
                    } else {
                        homeToolbar.chat.visibility = View.GONE
                        AddressLayout.visibility = View.GONE
                        homeToolbar.refferal.visibility = View.GONE
                        homeToolbar.checkTotalCredits.visibility = View.GONE
                        homeToolbar.setSearch.visibility = View.GONE
                    }
                    homeToolbar.whatsAppText.visibility = View.GONE
                    homeToolbar.whatsAppTextICons.visibility = View.GONE
                    Constant.stopBacgroundTimer = 1
                    Constant.startFirstTime = true
                    val seller_active_status =
                        Constant.getPrefs(this).getString(Constant.seller_active_status, "")
                    if (seller_active_status == "1") {
                        dialog = Dialog(this)
                        //   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setContentView(R.layout.dialog_active)
                        dialog.dialogInactive.visibility = View.VISIBLE
                        // dialog.setTitle("Inactive")
                        dialog.show()
//************************inactive click
                        dialog.inactive.setOnClickListener {
                            progress.visibility = View.VISIBLE
                            mvmodel.seller_active_status("2")
                            dialog.dismiss()
                        }

                    } else {
                        dialog = Dialog(this)
                        //  dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        mBottomNavigationSeller.setItemIconTintList(null)
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.setContentView(R.layout.dialog_active)
                        dialog.dialogActive.visibility = View.VISIBLE
                        // dialog.setTitle("Active")
                        dialog.show()
//*************************active click
                        dialog.active.setOnClickListener {
                            progress.visibility = View.VISIBLE
                            mvmodel.seller_active_status("1")
                            dialog.dismiss()
                        }
                    }

                    mBottomNavigationSeller.menu.getItem(2).isChecked = true
                }
                R.id.cartSeller -> {
                    Constant.stopBacgroundTimer = 1
                    Constant.startFirstTime = true
                    homeToolbar.chat.visibility = View.GONE
                    AddressLayout.visibility = View.GONE
                    homeToolbar.whatsAppText.visibility = View.GONE
                    homeToolbar.whatsAppTextICons.visibility = View.VISIBLE
                    homeToolbar.refferal.visibility = View.GONE
                    homeToolbar.checkTotalCredits.visibility = View.GONE
                    homeToolbar.setSearch.visibility = View.GONE
                    sellerSelectedTap = 3
                    //  mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                    loadFragment(SellerOrderFragment())
                    textToolbarTitle.text = getString(R.string.orders)
                    //  mBottomNavigationBuyer.menu.getItem(1).isChecked = false
                    mBottomNavigationSeller.menu.getItem(3).isChecked = true
                    mBottomNavigationSeller.menu.getItem(0)
                        .setIcon(R.drawable.ic_home_inactive_seller)
                    mBottomNavigationSeller.menu.getItem(1)
                        .setIcon(R.drawable.ic_shoppinglist_inactive_seller)
                    val seller_active_status =
                        Constant.getPrefs(this).getString(Constant.seller_active_status, "")

                    if (seller_active_status == "1") {
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                        changeTitleColor(1)
                    } else {
                        mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                        changeTitleColor(2)
                    }
                    mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_active)
                    mBottomNavigationSeller.menu.getItem(4)
                        .setIcon(R.drawable.ic_setting_inactive_seller)

                }


                R.id.settingSeller -> {
                    //********api hit  for updating the profile  code set in 11-02-2020
                    val json = Constant.getPrefs(this).getString(Constant.PROFILE, "1")
                    if (json != "1") {
                        Constant.stopBacgroundTimer = 1
                        Constant.startFirstTime = true
                        homeToolbar.chat.visibility = View.GONE
                        AddressLayout.visibility = View.GONE
                        homeToolbar.refferal.visibility = View.GONE
                        homeToolbar.checkTotalCredits.visibility = View.GONE
                        homeToolbar.whatsAppText.visibility = View.GONE
                        homeToolbar.whatsAppTextICons.visibility = View.GONE
                        homeToolbar.setSearch.visibility = View.GONE
                        sellerSelectedTap = 4
                        viewModelDashBoard.getProfile(tokenFirebase)
                        loadFragment(SellerSettingFragment())
                        textToolbarTitle.text = getString(R.string.Settings)
                        mBottomNavigationSeller.menu.getItem(4).isChecked = true
                        mBottomNavigationSeller.menu.getItem(0)
                            .setIcon(R.drawable.ic_home_inactive_seller)
                        mBottomNavigationSeller.menu.getItem(1)
                            .setIcon(R.drawable.ic_shoppinglist_inactive_seller)
                        val seller_active_status =
                            Constant.getPrefs(this).getString(Constant.seller_active_status, "")

                        if (seller_active_status == "1") {
                            mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
                            changeTitleColor(1)
                        } else {
                            mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
                            changeTitleColor(2)
                        }
                        mBottomNavigationSeller.menu.getItem(3).setIcon(R.drawable.ic_cart_inactive)
                        mBottomNavigationSeller.menu.getItem(4)
                            .setIcon(R.drawable.ic_setting_active_seller)
                    } else {
                        viewModelDashBoard.getProfile(tokenFirebase)
                    }
                }
            }
            if (sellerSelectedTap == 0) {
                showSelected(getString(R.string.home), 0, 0)
                showSelected(getString(R.string.sell_now), 1, 1)
                showSelected(getString(R.string.orders), 3, 1)
                showSelected(getString(R.string.Settings), 4, 1)
            } else if (sellerSelectedTap == 1) {
                showSelected(getString(R.string.home), 0, 1)
                showSelected(getString(R.string.sell_now), 1, 0)
                showSelected(getString(R.string.orders), 3, 1)
                showSelected(getString(R.string.Settings), 4, 1)
            } else if (sellerSelectedTap == 3) {
                showSelected(getString(R.string.home), 0, 1)
                showSelected(getString(R.string.sell_now), 1, 1)
                showSelected(getString(R.string.orders), 3, 0)
                showSelected(getString(R.string.Settings), 4, 1)
            } else if (sellerSelectedTap == 4) {
                showSelected(getString(R.string.home), 0, 1)
                showSelected(getString(R.string.sell_now), 1, 1)
                showSelected(getString(R.string.orders), 3, 1)
                showSelected(getString(R.string.Settings), 4, 0)
            }
            mBottomNavigationBuyer.visibility = View.GONE
            mBottomNavigationSeller.visibility = View.VISIBLE
        }
        return false
    }

//    private fun checkClearCart() {
//        if (Constant.ClearCartLat != Constant.getProfileData(this).lat && Constant.ClearCartLong != Constant.getProfileData(
//                this
//            ).long
//        ){
//            mvmodel.ClearCart()
//        }
//    }

    private fun showSelected(text: String, sellerSelectedTap: Int, selected: Int) {
        if (selected == 0) {
            val s = SpannableString(text)
            s.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.colorPrimary)),
                0,
                s.length,
                0
            )
            mBottomNavigationSeller.menu.getItem(sellerSelectedTap).title = s
        } else {
            val s = SpannableString(text)
            s.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.light_grey)),
                0,
                s.length,
                0
            )
            mBottomNavigationSeller.menu.getItem(sellerSelectedTap).title = s

        }

    }

    //buyer side
    private fun loadFragment(fragment1: Fragment) {
        fragmentCurrent = fragment1
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.buyer_frag_Container, fragment1)
        transaction.commit()
    }

    //***************************************Seller Switch*****************************************//
//not permanentlty switch to seller
    override fun onSwitchChangeNo() {
        // hideLoader()
        /*    val list = getSelectedCitiesList()
        if (list.size == 0) {
            val launchIntent = Intent(this, SelectCitiesActivity::class.java)
            launchIntent.putExtra("switch", "1")
            launchIntent.putExtra("type", "2")
            launchIntent.putExtra("from", "1")
            startActivityForResult(launchIntent, 1233)

        } else {
            // seller notification api
            if (!intent.hasExtra("source_id")){
                viewModelDashBoard.getHomeSellerNotification(tokenUser)
            }
            Constant.getPrefs(this).edit().putString(Constant.type, "2").apply()
            mvmodel.switch_setting("1", "2")
        }*/

    }

    //permanentlty switch to selller
    override fun onSwitchChangeYes() {
        // setFirstTimeUserData()//set userFirstTime Data
        ClearAddressData()// clear the address
        val list = getSelectedCitiesList()
        if (list.size == 0) {
            val launchIntent = Intent(this, SelectCitiesActivity::class.java)
            //launchIntent.putExtra("switch", "2")
            launchIntent.putExtra("switch", "2")
            launchIntent.putExtra("type", "2")
            launchIntent.putExtra("from", "1")
            launchIntent.putExtra("textSellerSide", "1")
            startActivityForResult(launchIntent, 1233)
        } else {
            DashBoardProgress.visibility = View.VISIBLE
            // seller notification api
//            if (!intent.hasExtra("source_id")) {
//                viewModelDashBoard.getHomeSellerNotification(tokenUser)
//            }
            mvmodel.switch_setting("2", "2")  // permanently switch to seller switch 2
            // mvmodel.switch_setting("1", "2")
        }
    }
//****************first time user popup show code is commented  as per client requirement
//    private fun setFirstTimeUserData() {
//        Constant.getPrefs(this).edit()
//            .putString(
//                Constant.is_seller_first_time,
//                "1"
//            )
//            .apply()
//
//    }

    //****************************************Buyer Switch***********************************//
    override fun onSwitchChangeBuyerNo() {

    }

    override fun onSwitchChangeBuyerYes() {
        DashBoardProgress.visibility = View.VISIBLE
        mvmodel.switch_setting("1", "1")
//        if (!intent.hasExtra("source_id")) {
//            viewModelDashBoard.getHomePageBuyerNotification(tokenUser)
//        }
    }

    override fun onStatus(seller_active_status: String) {
        if (seller_active_status == "1") {
            mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_active)
            changeTitleColor(1)
        } else {
            mBottomNavigationSeller.menu.getItem(2).setIcon(R.drawable.ic_inactive)
            changeTitleColor(2)
        }
    }

    private fun getSelectedCitiesList(): java.util.ArrayList<CitiesModel> {
        val listCities = ArrayList<CitiesModel>()
        val profile = Constant.getPrefs(application).getString(Constant.PROFILE, "")
        if (profile!!.isNotEmpty()) {
            val obj = JSONObject(profile)
            val citiesArr = obj.optJSONArray("sel_cities")

            for (i in 0 until citiesArr.length()) {
                val child = citiesArr.optJSONObject(i)
                val model = CitiesModel()
                model.id = child.optString("id")
                model.name = child.optString("name")
                listCities.add(model)
            }
        }
        return listCities
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {

            if (resultCode == 1233) {
                val tyoe = data!!.getStringExtra("type")!!
                val switch = data.getStringExtra("switch")!!
                Constant.getPrefs(this).edit().putString(Constant.type, tyoe)
                    .apply()
                mvmodel.switch_setting(switch, tyoe)
            }

            if (requestCode == 1234) {
                loadFragment(fragmentCurrent)
                val from = data!!.getStringExtra("from")
                if (from != "2") {

                    if (fragmentCurrent is BuyerPostShoppingListFragment) {
                        val model: PostBuyerShopListModel =
                            (fragmentCurrent as BuyerPostShoppingListFragment).buyer()
                        if (model.totalCredits > 0) {
                            viewModelDashBoard.createShoppingList(
                                model.listingname,
                                model.category_id,
                                model.deliveryZoneJsonList,
                                model.daysTimeZoneJsonArray,
                                model.shoppingProductListJSonArray,
                                model.listId,
                                model.credDitToDeduct
                            )
                        } else {
//                            Toast.makeText(
//                                this,
//                                getString(R.string.You_have_no_credits),
//                                Toast.LENGTH_SHORT
//                            ).show()
                            Constant.showToast(getString(R.string.You_have_no_credits), this)
                        }
                    }
                } else {
                    Constant.stopBacgroundTimer = 1
                    Constant.startFirstTime = true
                    buyerPurchaseListAlertDialog()
                    loadFragment(BuyerPostShoppingListFragment())
                    textToolbarTitle.text = getString(R.string.post_shopping_list)
                    homeToolbar.done.visibility = View.VISIBLE
                    homeToolbar.chat.visibility = View.GONE
                    AddressLayout.visibility = View.GONE
                    homeToolbar.refferal.visibility = View.GONE
                    homeToolbar.checkTotalCredits.visibility = View.VISIBLE
                    homeToolbar.whatsAppText.visibility = View.GONE
                    homeToolbar.whatsAppTextICons.visibility = View.GONE
                    homeToolbar.setSearch.visibility = View.GONE
                    mBottomNavigationBuyer.menu.getItem(2).isChecked = true
                }
            }

        } catch (e: Exception) {

        }
        if (requestCode == 999) {
            if (fragmentCurrent is BuyerPostShoppingListFragment) {
                loadFragment(BuyerPostShoppingListFragment())
                textToolbarTitle.text = getString(R.string.post_shopping_list)
                homeToolbar.done.visibility = View.VISIBLE
                Constant.stopBacgroundTimer = 1
                Constant.startFirstTime = true
                homeToolbar.chat.visibility = View.GONE
                AddressLayout.visibility = View.GONE
                homeToolbar.refferal.visibility = View.GONE
                homeToolbar.whatsAppText.visibility = View.GONE
                homeToolbar.whatsAppTextICons.visibility = View.GONE
                homeToolbar.checkTotalCredits.visibility = View.VISIBLE
                homeToolbar.setSearch.visibility = View.GONE
                mBottomNavigationBuyer.menu.getItem(2).isChecked = true
            }
        }
        //1001 used for run  again home notification api on back
        if (requestCode == 1001) {
            val token = "Bearer " + Constant.getPrefs(this).getString(
                Constant.token,
                ""
            )
            //this api again hit when they coming back to the notification and refresh it
            if (!intent.hasExtra("source_id")) {
                viewModelDashBoard.getHomePageBuyerNotification(tokenUser)
            }
        }
        //1001 used for run  again home notification api on back
        if (requestCode == 1002) {
            val token = "Bearer " + Constant.getPrefs(this).getString(
                Constant.token,
                ""
            )
            //this api again hit when they coming back to the notification and refresh it
            if (!intent.hasExtra("source_id")) {
                viewModelDashBoard.getHomeSellerNotification(tokenUser)
            }

        }

        if (requestCode == 129 && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val geocoder = Geocoder(this)
                    val address: Address?
                    try {
                        val latitudeResult = data.getStringExtra("lat")
                        val longitudeResult = data.getStringExtra("long")
                        // 2
                        val addresses =
                            geocoder.getFromLocation(
                                latitudeResult!!.toDouble(),
                                longitudeResult!!.toDouble(),
                                1
                            )

                        address = addresses[0]
                        val builder = StringBuilder()
                        builder.append(address?.getAddressLine(0)).append(",\t")
                        val resultDestination = builder.toString()
                        optionalDeliveryZoneModel = data.getParcelableExtra("deliveryZone")!!
                        //   optionalDeliveryZoneModel.address = resultDestination
//                        lat = latitudeResult
//                        long = longitudeResult
                        optionalDeliveryZoneModel.lat = latitudeResult!!
                        optionalDeliveryZoneModel.long = longitudeResult!!
                        //Addressdatas = resultDestination
                        if (optionalDeliveryZoneModel.address.isEmpty() && optionalDeliveryZoneModel.lat.isEmpty()) {
//                            Toast.makeText(
//                                this,
//                                getString(R.string.Reselect_address),
//                                Toast.LENGTH_SHORT
//                            ).show()
                            Constant.showToast(getString(R.string.Reselect_address), this)
                        } else {
                            addressDialog.addressHome.setText(optionalDeliveryZoneModel.address)
                        }
                        Log.e("Map Fragment2 ", resultDestination)
                    } catch (e: IOException) {
                        Log.e("MapsActivity", e.localizedMessage)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message.toString())
                }
            }
        }
        //toolbar address set
        else if (requestCode == 112 && data != null) {

            if (resultCode == Activity.RESULT_OK) {
                try {
                    val geocoder = Geocoder(this)
                    val address: Address?
                    try {
                        val lat = data.getStringExtra("lat")!!
                        val long = data.getStringExtra("long")!!
                        // 2
                        val addresses =
                            geocoder.getFromLocation(lat.toDouble(), long.toDouble(), 1)
                        address = addresses[0]
                        Log.e("Map Fragment ", address.toString())
                        val builder = StringBuilder()
                        builder.append(address?.getAddressLine(0)).append(",\t")
                        val resultDestination = builder.toString()
                        optionalDeliveryZoneModel = data.getParcelableExtra("deliveryZone")!!
                        // optionalDeliveryZoneModel.address = resultDestination
                        if (optionalDeliveryZoneModel.address.isEmpty()) {

                            Constant.showToast(getString(R.string.network_error), this)
                        } else {
                            Constant.stopBacgroundTimer = 1
                            Constant.startFirstTime = true
                            addressDialog.addressHome.setText(optionalDeliveryZoneModel.address)
                            homeToolbar.chat.visibility = View.VISIBLE
                            homeToolbar.refferal.visibility = View.VISIBLE
                            homeToolbar.checkTotalCredits.visibility = View.GONE
                            homeToolbar.whatsAppText.visibility = View.GONE
                            homeToolbar.whatsAppTextICons.visibility = View.GONE
                            homeToolbar.setSearch.visibility = View.VISIBLE
                            buyernav.visibility = View.VISIBLE
                            sellernav.visibility = View.GONE
                            AddressLayout.visibility = View.VISIBLE
                            //loadFragment(fragobj)
                            mBottomNavigationBuyer.menu.getItem(0).isChecked = true
                        }
                        Log.e("Map Fragment2 ", resultDestination)
                    } catch (e: IOException) {
                        Log.e("MapsActivity", e.localizedMessage)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message.toString())
                }
            }

        }

        if (requestCode == 1010 && resultCode == Activity.RESULT_OK) {
            resultCode1 = "1010"
            viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
        }

        //
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Constant.showToast("sucess", this)
        }
    }

    override fun inflatePostFragment() {
        loadFragment(BuyerPostShoppingListFragment())
        textToolbarTitle.text = getString(R.string.post_shopping_list)
        homeToolbar.done.visibility = View.VISIBLE
        homeToolbar.checkTotalCredits.visibility = View.VISIBLE
        homeToolbar.setSearch.visibility = View.GONE
        homeToolbar.whatsAppText.visibility = View.GONE
        Constant.stopBacgroundTimer = 1
        Constant.startFirstTime = true
        homeToolbar.whatsAppTextICons.visibility = View.GONE
        mBottomNavigationBuyer.menu.getItem(2).isChecked = true
    }


    //****************buyer notification or seller
    override fun buyerNotificationClick(notificationModel: NotificationModel) {
        val type = Constant.getPrefs(this).getString(Constant.type, "")
//type 1 buyer type 2=seller
        if (type == "1") {
            val intent = Intent(this, CommonNotificationConfirmationActivity::class.java)
            intent.putExtra("keySource4", notificationModel)
            startActivityForResult(intent, 1001)
        } else {
            val intent = Intent(this, RateBuyerActivity::class.java)
            intent.putExtra("keySource7", notificationModel)
            startActivityForResult(intent, 1002)
        }


    }

    //****************************notification data set***************//
    private fun PushNotificationIntegration() {
        val source_id = intent.getStringExtra("source_id")!!
        val list_type = intent.getStringExtra("list_type")!!
        val source = intent.getStringExtra("source")!!
        val name = intent.getStringExtra("name")!!
        val image = intent.getStringExtra("image")!!
        val level = intent.getStringExtra("level")!!
        val listingname = intent.getStringExtra("listingname")!!
        val reputation = intent.getStringExtra("reputation")!!
        val created_at = intent.getStringExtra("created_at")!!
        val notification_id = intent.getStringExtra("notification_id")!!
        val sender_id = intent.getStringExtra("sender_id")!!

        //source 1 when the seller quote to the buyer list notification send to buyer "seller submit the quote"
        if (source.equals("1")) {
            val intent = Intent(this, BuyerShopListApproveRejectActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source-2 when seller approve the buyer quote notification send to buyer "accept your order request"
        else if (source.equals("2")) {
            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 3 when the seller reject the order of buyer quote send to buyer "reject your order request"
        else if (source.equals("3")) {
            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 4 when the seller complete the list notification to Buyer "seller has complete your order"
        else if (source.equals("4")) {
            val intent = Intent(this, CommonNotificationConfirmationActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            intent.putExtra("notification_id", notification_id)
            startActivity(intent)

        }
        //source 5 when the buyer accept the request of seller that are quote on buyer list notification send to seller "buyer has accepted your order"
        else if (source.equals("5")) {
            val intent =
                Intent(this, ToBeDeliveredOrderListActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 6 when the buyer reject the request of seller that are quoted on buyer list notification send to  seller "buyer Reject your order"
        else if (source.equals("6")) {
            val intent = Intent(this, SellerCancelledOrderListActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 7 when the buyer complete the list of seller notification send to seller "buyer confirmed your list "
        else if (source.equals("7")) {
            val intent = Intent(this, RateBuyerActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            intent.putExtra("notification_id", notification_id)
            intent.putExtra("sender_id", sender_id)
            startActivity(intent)
        }
        // source 8 when the buyer create the dispute  the notification send to the seller " buyer added a dispute on your list"
        else if (source.equals("8")) {
            val intent = Intent(this, ResolutionActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 9 when the seller accept the dispute  the notification send to the buyer "seller has accepte the dispute"
        else if (source.equals("9")) {
            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 10 when the seller refute to the dispute of byer  notification send to buyer"seller has refute on your dispute"
        else if (source.equals("10")) {
            val intent = Intent(this, UnderReviewOrderDetailsActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        //source 11 when the buyer is shared the shopping to the another buyer than another buyer get the notification"Share feed "
        //no case for this
        // else if (source.equals("11")) {
//            val intent = Intent(this, DashBoardBuyerActivity::class.java)
//            startActivity(intent)
//            finish()
//            intent.putExtra("source_id", source_id);
//            intent.putExtra("list_type", list_type);
//            intent.putExtra("source", source);
        // }
        //source 12 when the buyer submit the quote  the seller list  notification send to the seller "buyer submit the quote"
        else if (source.equals("12")) {
            val intent = Intent(this, SellerApprovalOrderListActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            intent.putExtra("sender_id", sender_id)
            startActivity(intent)
        }
        // source 13  is used for when the  seller or buyer are complete their 1200 creedits
        else if (source.equals("13")) {
            val intent = Intent(this, CreditConvertActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)
            startActivity(intent)
        }
        // source 14  is used for message of chat activity
        else if (source.equals("14")) {
            val modelChat = ModelChat()
            modelChat.listingname = listingname
            modelChat.channel_id = source_id
            modelChat.user_id = sender_id
            modelChat.level = level
            modelChat.image = image.toString()
            modelChat.type = list_type.toString()
            modelChat.name = name.toString()
            val intent = Intent(this, ChatMessagesActivity::class.java)
            intent.putExtra("model", modelChat)
            startActivity(intent)
        }
        // source 14  is used for message of chat activity
        else if (source.equals("15")) {
            val modelChat = OrderModel()
            modelChat.listingname = listingname
            modelChat.req_id = source_id
            modelChat.seller_id = sender_id
            modelChat.seller_level = level
            modelChat.seller_image = image
            modelChat.list_type = list_type
            modelChat.seller_name = name
            modelChat.reputation = reputation
            val intent = Intent(this, RateSellerActivity::class.java)
            intent.putExtra("NotificationRateToSeller", modelChat)
            startActivity(intent)
        }
        // source 16 when the seller canceller the buyer order notification send to buyer
        else if (source.equals("16")) {
            val model = NotificationModel()
            model.source_id = source_id
            model.list_type = list_type
            model.source = source
            model.name = name
            model.image = image
            model.level = level
            model.listingname = listingname
            model.reputation = reputation
            model.created_at = created_at
            model.action_taken = "1"
//            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)//complete
//            intent.putExtra("source_id", source_id)
//            intent.putExtra("list_type", list_type)
//            intent.putExtra("source", source)
//            intent.putExtra("name", name)
//            intent.putExtra("image", image)
//            intent.putExtra("level", level)
//            intent.putExtra("listingname", listingname)
//            intent.putExtra("reputation", reputation)
//            intent.putExtra("created_at", created_at)
//            startActivity(intent)

            val intent = Intent(this, CommonNotificationConfirmationActivity::class.java)
            intent.putExtra("keySource4", model)
            intent.putExtra("KeySourceCancellOrderConfirm", model)
            this.startActivity(intent)

        }
        //when the buyer cancell order notification send to seller
        else if (source.equals("17")) {
            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)

            startActivity(intent)

        } else if (source.equals("18")) {
            val intent = Intent(this, BuyerOrderDetailsActivity::class.java)//complete
            intent.putExtra("source_id", source_id)
            intent.putExtra("list_type", list_type)
            intent.putExtra("source", source)
            intent.putExtra("name", name)
            intent.putExtra("image", image)
            intent.putExtra("level", level)
            intent.putExtra("listingname", listingname)
            intent.putExtra("reputation", reputation)
            intent.putExtra("created_at", created_at)

            startActivity(intent)

        } else {
            //for all
            val intent = Intent(this, CommonNotificationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun creditDeductionLogic(): Int {
        val json = JSONObject(Constant.getPrefs(this).getString(Constant.PROFILE, ""))
        buyerLevel = json.optString("buyer_level").trim()
        creditPerProduct = json.optString("per_product_credits").trim()
        buyerCredits = json.optString("credits").trim()
        when (buyerLevel) {
            "1" -> freeProducts = 5
            "2" -> freeProducts = 6
            "3" -> freeProducts = 8
            "4" -> freeProducts = 10
            "5" -> freeProducts = 15
        }

        return freeProducts

    }

    //*************************chatting count
    private fun showChatCount() {
        val type = Constant.getPrefs(this).getString(Constant.type, "")
        if (type == "2") {
            if (Constant.getProfileData(this).unreadcount_seller == "0" || Constant.getProfileData(
                    this
                ).unreadcount_seller.isEmpty()
            ) {
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.toolbarMesageButton.visibility = View.GONE
            } else {
                homeToolbar.toolbarMesageButton.visibility = View.VISIBLE
                homeToolbar.toolbarMesageButton.text =
                    Constant.getProfileData(this).unreadcount_seller
            }
        } else {
            if (Constant.getProfileData(this).unreadcount == "0" || Constant.getProfileData(this).unreadcount.isEmpty()) {
                homeToolbar.chat.visibility = View.VISIBLE
                homeToolbar.toolbarMesageButton.visibility = View.GONE
            } else {
                homeToolbar.toolbarMesageButton.visibility = View.VISIBLE
                homeToolbar.toolbarMesageButton.text = Constant.getProfileData(this).unreadcount
            }
        }


    }

    //show adress only first time when user
    fun showAdressOnHomePage() {
        if (Constant.getProfileData(this).full_address != "") {
//**********************set the address in the toolbar text
            if (optionalDeliveryZoneModel.address.isNotEmpty()) {
                toolbarAdressHome.visibility = View.VISIBLE
                toolbarAdressHome.setText(optionalDeliveryZoneModel.address)

            } else if (Constant.getProfileData(this).full_address.isNotEmpty()) {
                toolbarAdressHome.text = Constant.getProfileData(this).full_address
                toolbarAdressHome.visibility = View.VISIBLE
            }
        }
    }

    //****************clear the address
    fun ClearAddressData() {
        optionalDeliveryZoneModel.address = ""
        optionalDeliveryZoneModel.lat = ""
        optionalDeliveryZoneModel.long = ""
    }

    //*****************************address update first time
    private fun getLastLocation() {
        //check the permission is aloow or not
        //if allow then go to the if condition
        if (checkPermissions()) {
            //check location enabled in settings
            if (isLocationEnabled()) {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        optionalDeliveryZoneModel.lat = location.latitude.toString()
                        optionalDeliveryZoneModel.long = location.longitude.toString()
                        optionalDeliveryZoneModel.address =
                            getAddressConvertLatLong(
                                location.latitude,
                                location.longitude
                            )//convert the address

                        addAddressCustomDialog(optionalDeliveryZoneModel)//show the dialog
                    }
                }
            } else {
//show to alert location enabled redirect the user to setting screen
                showAlertLocationEnabled()
            }
        } else {
            activateLocationLayout.visibility =
                View.VISIBLE//if location is not allow then visible screen
        }
    }

    private fun getAddressConvertLatLong(latitude: Double, longitude: Double): String {
        try {
            val geocoder = Geocoder(this)
            val address: Address?
            val addresses =
                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1
                )
            address = addresses[0]
            val builder = StringBuilder()
            builder.append(address?.getAddressLine(0)).append(",\t")
            val resultDestination = builder.toString()
            return resultDestination
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return ""
    }

    //****************these all are permission
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details \

            return
        }
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // val mLastLocation: Location = locationResult.lastLocation
//            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }

    }

    fun checkLocationEnabledOrNot() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val mDialog = android.app.AlertDialog.Builder(this)
            mDialog.setTitle(getString(R.string.alert))
            mDialog.setCancelable(false)

            mDialog.setMessage(getString(R.string.LOcationPermissionCustomPopText))
            mDialog.setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                requestPermissions()
                Handler().postDelayed({}, 1000)
                dialog.cancel()
            }
            mDialog.show()

        }
    }

    //**************first time dialog video tour of app***************//
//    private fun firstTimeCaiguruDialog() {
//        dialog = Dialog(this)
//        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog.setContentView(R.layout.first_time_dialog)
//        dialog.show()
//
//        //set the click on  button
//        dialog.btnWatchVideo.setOnClickListener {
//            val browserIntent =
//                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtubeLInk)))
//            startActivity(browserIntent)
//            //set the data in shared pref
//            Constant.getPrefs(this).edit()
//                .putString(
//                    Constant.is_seller_first_time,
//                    Constant.getProfileData(this).is_seller_first_time
//                )
//                .apply()
//            dialog.dismiss()
//        }
//        dialog.setOnDismissListener {
//            //set the data in shared pref
//            Constant.getPrefs(this).edit()
//                .putString(
//                    Constant.is_seller_first_time,
//                    Constant.getProfileData(this).is_seller_first_time
//                )
//                .apply()
//            dialog.dismiss()
//        }
//
//    }

    fun buyerPurchaseListAlertDialog() {
        val mDialog = android.app.AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setCancelable(false)
        mDialog.setMessage(getString(R.string.buyer_post_shopping_alert))
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.cancel()
        }

        mDialog.show()
    }

    //set the click on the actvate lovation
    fun activateLocationClick() {
        activateLocationLayout.btnActivateLocation.setOnClickListener {
            checkLocationEnabledOrNot()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ID -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    //  Toast.makeText(this, "locattion Accepted", Toast.LENGTH_SHORT).show()
                    if (showDialog == 0) {
                        fusedLocationClient =
                            LocationServices.getFusedLocationProviderClient(this)

                        val gson = Gson()
                        val json = Constant.getPrefs(this).getString("profile", "1")
                        val typeCheck = Constant.getPrefs(this).getString(Constant.type, "")
                        if (json == "1" && typeCheck == "1") {

                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {

                                return
                            }


                            fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                                val location: Location? = task.result
                                if (location == null) {
                                    requestNewLocationData()
                                } else {
                                    optionalDeliveryZoneModel.lat = location.latitude.toString()
                                    optionalDeliveryZoneModel.long = location.longitude.toString()
                                    optionalDeliveryZoneModel.address =
                                        getAddressConvertLatLong(
                                            location.latitude,
                                            location.longitude
                                        )

                                    addAddressCustomDialog(optionalDeliveryZoneModel)//show the dialog
                                }
                            }
                            addAddressCustomDialog(optionalDeliveryZoneModel)

                        } else {
                            if (json != "1") {
                                val profileModel = gson.fromJson(json, GetProfileModel::class.java)
                                if (profileModel.lat.isEmpty() && profileModel.long.isEmpty()) {
                                    if (ActivityCompat.checkSelfPermission(
                                            this,
                                            Manifest.permission.ACCESS_FINE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                            this,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {

                                        return
                                    }
                                    fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                                        val location: Location? = task.result
                                        if (location == null) {
                                            requestNewLocationData()
                                        } else {
                                            optionalDeliveryZoneModel.lat =
                                                location.latitude.toString()
                                            optionalDeliveryZoneModel.long =
                                                location.longitude.toString()
                                            optionalDeliveryZoneModel.address =
                                                getAddressConvertLatLong(
                                                    location.latitude,
                                                    location.longitude
                                                )

                                            addAddressCustomDialog(optionalDeliveryZoneModel)//show the dialog
                                        }
                                    }
                                    addAddressCustomDialog(optionalDeliveryZoneModel)
                                }
                            }
                        }
                    }
                    activateLocationLayout.visibility = View.GONE
                } else {
                    showAlertLocationEnabled()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    //*********************filling or edit the address from the dashboard *****************//
    private fun addAddressHomeCustomDialog() {
        // *********************************** Add Address by custom dialog ******************************************//
        addressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addressDialog.setContentView(R.layout.buyer_add_address_dialog)
        addressDialog.setCancelable(true)
        addressDialog.show()
        addressDialog.textfeed.visibility = View.VISIBLE
        addressDialog.addressHome.visibility = View.VISIBLE//edttext field visible
        addressDialog.address.visibility = View.INVISIBLE//textfield
        addressDialog.EdtMapLocation.visibility = View.VISIBLE
        addressDialog.addressHome.filters = arrayOf<InputFilter>(HideEmoji(this))
        //set address prefill
        if (this.optionalDeliveryZoneModel.lat.isEmpty()) {
            if (Constant.getProfileData(this).lat != "") {
                this.optionalDeliveryZoneModel.lat = Constant.getProfileData(this).lat
                this.optionalDeliveryZoneModel.long = Constant.getProfileData(this).long
                this.optionalDeliveryZoneModel.address =
                    Constant.getProfileData(this).full_address
                addressDialog.addressHome.setText(Constant.getProfileData(this).full_address)
            }
        } else {
            addressDialog.addressHome.setText(optionalDeliveryZoneModel.address)
        }
        //set the click button of custom dialog
        addressDialog.viewmap.setOnClickListener {
            Constant.UpdateAddressInBackGround = "1"
            optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                .trim()//set the address if they change anything
            val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("keyOptionalDeliveryZone1", this.optionalDeliveryZoneModel)
            intent.putExtra("Select", "Select address")
            startActivityForResult(intent, 112)
        }
        //************set the click on the edit
        addressDialog.EdtMapLocation.setOnClickListener {
            Constant.UpdateAddressInBackGround = "1"
            optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                .trim()//set the address if they change anything
            val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("keyOptionalDeliveryZone1", this.optionalDeliveryZoneModel)
            intent.putExtra("Select", "Select address")
            startActivityForResult(intent, 112)
        }


        addressDialog.updateButton.setOnClickListener {
            if (addressDialog.addressHome.text.isEmpty()) {
                Constant.showToast(getString(R.string.Please_Enter_your_Address), this)
            } else if (this.optionalDeliveryZoneModel.lat.isEmpty()) {
                Constant.showToast(getString(R.string.Reselect_address), this)
            } else {
                optionalDeliveryZoneModel.address = addressDialog.addressHome.text.toString()
                toolbarAdressHome.visibility = View.VISIBLE
                toolbarAdressHome.setText(optionalDeliveryZoneModel.address)
                val bundle = Bundle()
                bundle.putString("edttext", "From Activity")
                bundle.putParcelable("edttext", this.optionalDeliveryZoneModel)
                val fragobj = HomeBuyerFragment()
                fragobj.setArguments(bundle)
                loadFragment(fragobj)
                addressDialog.dismiss()
            }

        }
        addressDialog.cancel.setOnClickListener {
            addressDialog.dismiss()
        }
    }


    fun showWhatsappIconBlinking() {
        // blinkTextView()

        getMessagesConinous()
    }


    //set chat icon click
    fun setwhatsAppIConClick() {
        homeToolbar.whatsAppText.setOnClickListener {
            Constant.openWhatsApp(this)
        }
        homeToolbar.whatsAppTextICons.setOnClickListener {
            Constant.openWhatsApp(this)
        }
    }

    //*************check package install or not
//    private fun whatsappInstalledOrNot(uri: String): Boolean {
//        val pm = packageManager
//        var app_installed = false
//        app_installed = try {
//            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
//            true
//        } catch (e: PackageManager.NameNotFoundException) {
//            false
//        }
//        return app_installed
//    }

    //****************open whatsapp with message************//
//    fun openWhatsappWIthMessage() {
//        ///  var smsNumber = "+91 89688 81388" // contains spaces.
//        var smsNumber = "+44 7831 805229" // contains spaces.
//        smsNumber = smsNumber.replace("+", "").replace(" ", "")
//        val packageManager: PackageManager = this.getPackageManager()
//        val i = Intent(Intent.ACTION_VIEW)
//
//        try {
//            val url =
//                "https://api.whatsapp.com/send?phone=" + smsNumber + "&text=" + URLEncoder.encode(
//                    "",
//                    "UTF-8"
//                )
//            i.setPackage("com.whatsapp")
//            i.data = Uri.parse(url)
//            if (i.resolveActivity(packageManager) != null) {
//                this.startActivity(i)
//            }
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//    }

    //*************both code mixture
//    private fun openWhatsApp() {
//        val isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp")
//        if (isWhatsappInstalled) {
//            openWhatsappWIthMessage()
//        } else {
//            val uri: Uri = Uri.parse("market://details?id=com.whatsapp")
//            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
//            Toast.makeText(
//                this, getString(R.string.app_not_installed),
//                Toast.LENGTH_SHORT
//            ).show()
//            startActivity(goToMarket)
//        }
//
//
//    }

    fun getMessagesConinous() {
        // try {
        runOnUiThread {
            // Stuff that updates the UI
            val t = Timer()
            //Set the schedule function and rate
            t.scheduleAtFixedRate(
                object : TimerTask() {
                    override fun run() {
                        if (Constant.stopBacgroundTimer == 0) {
                            val asyncTask = AsyncTaskRunner(this@DashBoardBuyerActivity)
                            asyncTask.execute()
                        } else {
                            t.cancel()
                        }
                    }
                },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                1000
            )
        }
    }

    private class AsyncTaskRunner(var context: DashBoardBuyerActivity) :
        AsyncTask<String?, String?, String?>() {
        private var resp: String? = "1"

        override fun doInBackground(vararg p0: String?): String? {
            if (resp == "1") {
                resp = "0"
            } else {
                resp = "1"
            }
            return resp
        }


        override fun onPostExecute(result: String?) {
            if (context.homeToolbar.whatsAppTextICons.getVisibility() == View.VISIBLE) {
                context.homeToolbar.whatsAppTextICons.setVisibility(View.INVISIBLE)
            } else {
                context.homeToolbar.whatsAppTextICons.setVisibility(View.VISIBLE)
            }
        }


        override fun onPreExecute() {

        }

        override fun onProgressUpdate(vararg text: String?) {

        }


    }

}

