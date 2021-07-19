package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.BuyerAddressMapBoxActivity
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.ActivitySellerPostShoppingListBinding
import com.example.caiguru.databinding.OpenFromCameraPermissionBinding
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.sellerChooseCategory.SellerChooseCategoryActivity
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.SellerSelectDaysAndTimeActivity
import com.example.caiguru.seller.sellerSelectedDaysAndTime.SlotModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliverZoneAdapter
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.deliveryZoneMaBox.DeliveryZoneMapBoxActivity
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod.PaymentMethodAdapter
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.paymentMethod.PaymentMethodModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller.SavedProductActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.dragDropProduct.DragDropProductActivity
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListPostedActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_post_shopping_list.*
import kotlinx.android.synthetic.main.add_price_custom_dialog.view.*
import kotlinx.android.synthetic.main.payment_option_dialog.*
import kotlinx.android.synthetic.main.seller_add_more_product_dialog.*
import kotlinx.android.synthetic.main.seller_add_more_product_dialog.img
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class SellerPostShoppingListActivity : AppCompatActivity(), DeliverZoneAdapter.sendDataInterface,
    PostShoppingAdapter.editDataInterface {

    private var whichTypeData: String = "copy"
    private lateinit var paymentmethodAdapter: PaymentMethodAdapter
    private var prefillingProductModel = ArrayList<PostShoppingModel>()// prefilling model
    private var seller_maximum_commission: String = ""
    private var seller_minimum_commission: Int = 0
    var freeProducts = 0
    private var sellerLevel: String = ""
    private var creditPerProduct: String = ""

    //  private var creditToDeduct: String = ""
    private var sellerCredits: String = ""
    private var list: ListParentModel = ListParentModel()
    private var photoFile: File? = null
    var check: Int = -1
    private lateinit var dialogs: Dialog

    ///global model
    private var deliveryTimeList = ArrayList<DaysParentModel>()
    private val setTimeServerData = ArrayList<DaysParentModel>()

    private var categoryModelList = SellerCategoryModel()
    private var productShoppingList = java.util.ArrayList<PostShoppingModel>()///updated data
    private var deliveryZoneList = ArrayList<DeliveryZoneModel>()
    var pickupOptionalTimeZone = ArrayList<DaysParentModel>()
    var setpickupOptionalTimeZone = ArrayList<DaysParentModel>()
    var optionalDeliveryZoneModel = DeliveryZoneModel()

    //............................//
    private val optionTimePickerCode: Int = 121
    private var adapterShoppingPosition: Int = -1
    private var adapterEditShoppingModel: PostShoppingModel = PostShoppingModel()
    private var adapterPosition: Int = -1
    private lateinit var dialog: Dialog
    lateinit var adapter: PostShoppingAdapter
    lateinit var mAdapter: DeliverZoneAdapter
    private val chooseCategoryRequestCode: Int = 11
    private lateinit var mvmodel: PostShoppingViewModel
    private lateinit var text: TextView
    var model = PostShoppingModel()
    var modelDialog = DialogModel()
    var deliveryZoneModel = DeliveryZoneModel()
    private lateinit var mbinding: ActivitySellerPostShoppingListBinding

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_post_shopping_list)
        mvmodel = ViewModelProvider(this).get(PostShoppingViewModel::class.java)
        settingUpToolbar()
        paymentMethodSelect()
        activeInActiveCheck()
        setAllClicks()//all clicks
        firstimeInitvariables() //init data first time
        setAdapterShoppingList()
        DeliveryZoneTimeAdapter()
        seekBarClick()
        setAllObserver()
        CheckCopyAndModifyList()
        //set the touch drag and drop code
        //itemTouchHelper.attachToRecyclerView(recyclers)


    }

    private fun CheckCopyAndModifyList() {
        if (intent.hasExtra("prefillMOdify")) {
            whichTypeData = "prefil"
        } else {
            whichTypeData = "copy"
        }

    }

    //****************init variable first time
    private fun firstimeInitvariables() {

        //************************* check of post and get api ******************************//
        //prefilling case
        if (intent.hasExtra("model")) {
            gettingList()
            shoppingProgress.visibility = View.GONE
            activeHome.visibility = View.VISIBLE
            //*************hide the sekbar check or comision visible or invisible check
            //1 for visible else hide
            if (Constant.getProfileData(this).enable_seller_commission == "1") {
                txtcomission.visibility = View.VISIBLE
                seekbar.visibility = View.VISIBLE
                mbinding.seekbar.setProgress(seller_minimum_commission)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mbinding.seekbar.min = seller_minimum_commission
                }
            } else {
                txtcomission.visibility = View.GONE
                seekbar.visibility = View.GONE
            }
            //***************************set the seek bar by default***************//

//*****************hide the emoji*********************//
            txtname.filters = arrayOf<InputFilter>(HideEmoji(this), LengthFilter(50))
            //by default set the seekbar
            txtcomission.text =
                "${getString(R.string.Your_Comission)} ${seller_minimum_commission}${"%"}"

//************hide keyboard****************//
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)


        } else {
            //*************2nd layout screen
            layoutPart2.visibility = View.VISIBLE
            layoutPart1.visibility = View.GONE
            activeHome.visibility = View.GONE
            //*********firstLayout screen
            txtproductOfYourShopping.visibility = View.GONE
            addlayout.visibility = View.GONE
            textMoreProducts.visibility = View.INVISIBLE
            shoppingProgress.visibility = View.VISIBLE
        }
        //show toolbar variables    // show the icon of dollar
        toolbar.checkTotalCredits.visibility = View.VISIBLE
        seller_maximum_commission = Constant.getProfileData(this).seller_maximum_commission
        seller_minimum_commission = Constant.getProfileData(this).seller_minimum_commission.toInt()
        sellerCredits = Constant.getProfileData(this).credits
        sellerLevel = Constant.getProfileData(this).seller_level
        creditPerProduct = Constant.getProfileData(this).per_product_credits
        creditDeductionLogic(sellerLevel)

    }

    //********observer
    private fun setAllObserver() {
        //...........set the observer of delivery zone category................//
        mvmodel.sendDataDeliveryZone().observe(this, Observer {
            if (it.size > 0) {
                mAdapter.update(it)
                deliveryZoneList = it
            }

        })

        //...........set  the observer of add shopping list..........//
        mvmodel.sendData().observe(this, Observer {
            productShoppingList = it
            shoppingListData(it)
        })
        //*********************set the observer of get shopping list*******************//
        //sucessful case
        mvmodel.mSucessfulCreateShoppingList().observe(this, Observer {
            //visibilty
            passIntent()
            mbinding.btnfinish.visibility = View.VISIBLE
            mbinding.btnWait.visibility = View.GONE
            val intent = Intent(this, ShoppingListPostedActivity::class.java)
            intent.putExtra("keyshopping", it)
            startActivity(intent)
            finish()


        })
        //failure case
        mvmodel.mFailureShoppingError().observe(this, Observer {
            //visibilty
            mbinding.btnfinish.visibility = View.VISIBLE
            mbinding.btnWait.visibility = View.GONE
            val msg = it
            showErrorDialog(msg)
        })

        //.............observer of update image in the dialog box..................//
        //data sucessful
        mvmodel.getdataDialogSucessful().observe(this, Observer {
            adapterEditShoppingModel.image = it.image
            modelDialog.image = it.image//set image globally
            setProfilePic(it.image)
            dialog.progressdialog.visibility = View.INVISIBLE
            dialog.img.visibility = View.VISIBLE
        })
        //data failure
        mvmodel.geterrorDialog().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            dialog.progressdialog.visibility = View.INVISIBLE
            dialog.img.visibility = View.VISIBLE
        })


    }

    private fun passIntent() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)//set the result
        finish()
    }

    private fun setAllClicks() {

        //set the click on the saved products
        txtSavedProducts.setOnClickListener {
//            if (categoryModelList.name.isEmpty()) {
//                Constant.showToast(getString(R.string.Please_Select_Category), this)
//                return@setOnClickListener
//            } else {
                val intent = Intent(this, SavedProductActivity::class.java)
                intent.putExtra("KeyCategory", categoryModelList)
                intent.putParcelableArrayListExtra("KeyProductArray", adapter.getAllShoppingArrayData())
                startActivityForResult(intent, 500)
            //}
        }

        //toolBar Click
        toolbar.checkTotalCredits.setOnClickListener {

            Constant.customDialogShowTotalCredits(this)
        }
        //..........................set Click on chooseDeliveryZone .............................//
        mbinding.chooseDeliveryZone.setOnClickListener {
            // val intent = Intent(this, BuyerAddAddressActivity::class.java)
            val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
            intent.putExtra("choose", "Choose a Pickup Address")
            intent.putExtra("keyOptionalDeliveryZone", optionalDeliveryZoneModel)
            startActivityForResult(intent, 129)

            try {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 129
                    )

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        //set the click  on add location
        mbinding.addLocation.setOnClickListener {
            if (deliveryZoneList.size >= 2) {
                Toast.makeText(
                    this,
                    getString(R.string.You_Can_not_add_the_location_more_than_two),
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val intent = Intent(this, DeliveryZoneMapBoxActivity::class.java)
                startActivityForResult(intent, 3)

                try {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 101
                        )
                        // ActivityCompat.requestPermissions(getApplication(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        //..........set the click on the add product in list...................//
        mbinding.txtNewProducts.setOnClickListener {
//set the popup when the user add the first product
            if (adapter.getAllDataArraySize() <= 0) {
                showPopUpProductAdd()

            } else {
                if (Constant.getProfileData(this).enable_seller_commission == "1") {
                    if (mbinding.seekbar.progress < seller_minimum_commission) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    } else {
                        txtname.setFocusable(false)
                        edtPurchase.setFocusable(false)
                        addProductCustomDialog("add")//first time dialog open
                    }
                } else {
                    txtname.setFocusable(false)
                    edtPurchase.setFocusable(false)
                    addProductCustomDialog("add")//first time dialog open
                }
            }

        }

        //................set the click on the category name...........................//
        mbinding.txtcategory.setOnClickListener {
            if (txtcategory.text.isEmpty()) {
                val intent = Intent(this, SellerChooseCategoryActivity::class.java)
                startActivityForResult(intent, chooseCategoryRequestCode)
            } else {
                val intent = Intent(this, SellerChooseCategoryActivity::class.java)
                intent.putExtra("keyPrefillCategory", txtcategory.text.toString().trim())
                startActivityForResult(intent, chooseCategoryRequestCode)
            }
        }
        //...............set click on the delivery time zone...................//
        mbinding.txtdeliverytime.setOnClickListener {
            val intent = Intent(this, SellerSelectDaysAndTimeActivity::class.java)
            intent.putExtra("keySellerpost", deliveryTimeList)
            startActivityForResult(intent, 100)
        }

        //.................set the click on the optional delivery time.........//
        mbinding.txtdayAndTimePickOptional.setOnClickListener {
            val intent = Intent(this, SellerSelectDaysAndTimeActivity::class.java)
            intent.putExtra("keySellerpost", pickupOptionalTimeZone)
            startActivityForResult(intent, optionTimePickerCode)
        }
        //******************set the click after 1st screen complete and user go to the 2nd part of the screen
        mbinding.btnfinish.setOnClickListener {
            //*****************prefilling click
            if (intent.hasExtra("model")) {
                val chooseDe = mbinding.chooseDeliveryZone.text.toString().trim().length
                var optionalText = mbinding.txtdayAndTimePickOptional.text.toString().trim().length
                if (mbinding.txtdayAndTimePickOptional.text.toString()
                        .trim() == getString(R.string.Delivery_Time)
                ) {
                    optionalText = 0
                }
                //
                if (mbinding.txtname.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_Name_Of_The_List),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (mbinding.txtcategory.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Category),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (deliveryZoneList.get(0).address == getString(R.string.Delivery_Zone)
                ) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Your_Location),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (mbinding.txtdeliverytime.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Time_Zone),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (mbinding.edtPurchase.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_Minimum_Purchase),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (mbinding.edtSelectPaymentMethod.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.please_select_payment_method),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (productShoppingList.size == 0) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Add_Your_Product),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else if (productShoppingList.size <= 1) {
                    Constant.firstTimeCaiguruDialog(this)
                    return@setOnClickListener
                } else if (chooseDe > 0 && optionalText == 0) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_pickup_Time_Zone),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener


                } else if (chooseDe == 0 && optionalText > 0) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Your_pickup_Location),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener

                } else if (Constant.getProfileData(this).enable_seller_commission == "1" && mbinding.seekbar.progress < seller_minimum_commission) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
//                if (sellerCredits.toDouble().toInt() > 0) {
                    val listingname = txtname.text.toString().trim()
                    val minimum_purchase_amount = mbinding.edtPurchase.text.toString().trim()
                    val paymentMetods =
                        ConvertPuchasedOrder(mbinding.edtSelectPaymentMethod.text.toString().trim())
                    var commission_per = 0
                    if (Constant.getProfileData(this).enable_seller_commission == "1") {
                        commission_per = mbinding.seekbar.progress
                    }
                    if (prefillingProductModel.isEmpty()) {
                        if (productShoppingList.size > freeProducts) {
                            AlertUserAddProductAfterFive(
                                listingname,
                                minimum_purchase_amount,
                                commission_per, paymentMetods
                            )
                        } else {
                            mvmodel.createShoppingList(
                                listingname,
                                categoryModelList,
                                deliveryZoneJsonList(),
                                daysTimeZoneJsonArray(),
                                optionalTimeAndLocationJsonArray(),
                                minimum_purchase_amount,
                                commission_per,
                                shoppingProductListJSonArray(),
                                list,
                                adapter.getAllShoppingArrayData(), paymentMetods, whichTypeData
                            )
                            //visibilty
                            mbinding.btnfinish.visibility = View.GONE
                            mbinding.btnWait.visibility = View.VISIBLE
                        }

                    } else {
                        if (productShoppingList.size > freeProducts && prefillingProductModel.size < productShoppingList.size) {
                            AlertUserAddProductAfterFive(
                                listingname,
                                minimum_purchase_amount,
                                commission_per,
                                paymentMetods
                            )
                        } else {
                            mvmodel.createShoppingList(
                                listingname,
                                categoryModelList,
                                deliveryZoneJsonList(),
                                daysTimeZoneJsonArray(),
                                optionalTimeAndLocationJsonArray(),
                                minimum_purchase_amount,
                                commission_per,
                                shoppingProductListJSonArray(),
                                list,
                                adapter.getAllShoppingArrayData(),
                                paymentMetods,
                                whichTypeData
                            )
                            //visibilty
                            mbinding.btnfinish.visibility = View.GONE
                            mbinding.btnWait.visibility = View.VISIBLE
                        }
                    }

                }
            }
            //*************simple create list click
            else {
                //*********************set the click on the final step
                val chooseDe = mbinding.chooseDeliveryZone.text.toString().trim().length
                var optionalText = mbinding.txtdayAndTimePickOptional.text.toString().trim().length
                if (mbinding.txtdayAndTimePickOptional.text.toString()
                        .trim() == getString(R.string.Delivery_Time)
                ) {
                    optionalText = 0
                }
                if (deliveryZoneList.get(0).address == getString(R.string.Delivery_Zone)
                ) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Your_Location),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (mbinding.txtdeliverytime.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Time_Zone),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (mbinding.edtPurchase.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_Minimum_Purchase),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (mbinding.edtSelectPaymentMethod.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.please_select_payment_method),
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (chooseDe > 0 && optionalText == 0) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_pickup_Time_Zone),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener


                } else if (chooseDe == 0 && optionalText > 0) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Your_pickup_Location),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener

                } else {
                    val listingname = intent.getStringExtra("keyListName")!!
                    categoryModelList.category_id = intent.getStringExtra("keyCategoryId")!!
                    val productArray = intent.getStringExtra("keyProductJsonArray")!!
                    val minimum_purchase_amount = mbinding.edtPurchase.text.toString().trim()
                    val paymentMetods = ConvertPuchasedOrder(mbinding.edtSelectPaymentMethod.text.toString().trim())
                    var commission_per = 0
                    if (intent.getStringExtra("keyComissionPer") != "") {
                        commission_per = intent.getStringExtra("keyComissionPer")!!.toInt()
                    }
                    mvmodel.createShoppingList(
                        listingname,
                        categoryModelList,
                        deliveryZoneJsonList(),
                        daysTimeZoneJsonArray(),
                        optionalTimeAndLocationJsonArray(),
                        minimum_purchase_amount,
                        commission_per,
                        productArray,
                        list,
                        adapter.getAllShoppingArrayData(),
                        paymentMetods,
                        whichTypeData

                    )
                    //visibilty
                    mbinding.btnfinish.visibility = View.GONE
                    mbinding.btnWait.visibility = View.VISIBLE


//                    if (prefillingProductModel.isEmpty()) {
//                        if (productShoppingList.size > freeProducts) {
//                            AlertUserAddProductAfterFive(
//                                listingname,
//                                minimum_purchase_amount,
//                                commission_per, paymentMetods
//                            )
//                        } else {
//                            mvmodel.createShoppingList(
//                                listingname,
//                                categoryModelList,
//                                deliveryZoneJsonList(),
//                                daysTimeZoneJsonArray(),
//                                optionalTimeAndLocationJsonArray(),
//                                minimum_purchase_amount,
//                                commission_per,
//                                productArray,
//                                list,
//                                productShoppingList, paymentMetods
//
//                            )
//                            //visibilty
//                            mbinding.btnfinish.visibility = View.GONE
//                            mbinding.btnWait.visibility = View.VISIBLE
//                        }
//
//                    }
//                    else {
//                        if (productShoppingList.size > freeProducts && prefillingProductModel.size < productShoppingList.size) {
//                            AlertUserAddProductAfterFive(
//                                listingname,
//                                minimum_purchase_amount,
//                                commission_per,
//                                paymentMetods
//                            )
//                        }
//
//                        else {
//                            mvmodel.createShoppingList(
//                                listingname,
//                                categoryModelList,
//                                deliveryZoneJsonList(),
//                                daysTimeZoneJsonArray(),
//                                optionalTimeAndLocationJsonArray(),
//                                minimum_purchase_amount,
//                                commission_per,
//                                shoppingProductListJSonArray(),
//                                list, productShoppingList, paymentMetods
//                            )
//                            //visibilty
//                            mbinding.btnfinish.visibility = View.GONE
//                            mbinding.btnWait.visibility = View.VISIBLE
//                        }
//                    }

                }
            }

        }
    }

    private fun activeInActiveCheck() {
        val seller_active_status = Constant.getPrefs(this).getString(Constant.seller_active_status, "")

        if (seller_active_status == "1") {
            mbinding.activeHome.background = resources.getDrawable(R.color.green)
            mbinding.activeHome.text = resources.getText(R.string.active)
        } else {
            mbinding.activeHome.background = resources.getDrawable(R.color.red)
            mbinding.activeHome.text = resources.getText(R.string.inactive)
        }
    }


    //***************************************dialog AlertUserAddProductAfterFive
    fun AlertUserAddProductAfterFive(
        listingname: String,
        minimumPurchaseAmount: String,
        commissionPer: Int,
        paymentMetods: String
    ) {
        val credits = Constant.getProfileData(this).credits.toDouble().toInt().toString()
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(
            "${getString(R.string.You_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_additional)} $freeProducts ${
                getString(
                    R.string.Product
                )
            }. ${getString(R.string.credits_in_your_account)}: ${credits} ${getString(R.string.cr)}"
        )
        mDialog.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            if (sellerCredits.toDouble().toInt() < mvmodel.showcreditToDeduct().toDouble()) {
                Log.e("credits", mvmodel.showcreditToDeduct())
                Toast.makeText(
                    this,
                    getString(R.string.You_have_no_credits),
                    Toast.LENGTH_LONG
                )
                    .show()
                dialog.dismiss()
            } else {
                mvmodel.createShoppingList(
                    listingname,
                    categoryModelList,
                    deliveryZoneJsonList(),
                    daysTimeZoneJsonArray(),
                    optionalTimeAndLocationJsonArray(),
                    minimumPurchaseAmount,
                    commissionPer,
                    shoppingProductListJSonArray(),
                    list, adapter.getAllShoppingArrayData(), paymentMetods, whichTypeData
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


    //...........................getting list id ............................//

    private fun gettingList() {
        try {
            val id = intent.getParcelableExtra<ListParentModel>("model")!!
          //  val productArray=intent.getParcelableArrayListExtra<DialogModel>("modifyProductArray")
            list = id
            // setTimeServerData = id.
            mbinding.txtname.setText(id.listingname)

            mbinding.edtPurchase.setText(id.minimum_purchase_amount)
            //prefilling the select payment method
            edtSelectPaymentMethod.text =
                Constant.ModifyCaseOpenListSetPaymentMethods(this, id.payment_methods)
            mbinding.seekbar.progress = id.comission_per.toDouble().roundToInt()

            val categoriesList = Constant.categoryData(this)
            for (category in categoriesList) {
                if (category.category_id == id.cat_id) {
                    mbinding.txtcategory.setText(category.name)
                    categoryModelList.name=category.name
                    categoryModelList.category_id=id.cat_id
                }
            }

            val gson = Gson()
            val json = id.product_details
            val model1: List<PostShoppingModel> =
                gson.fromJson(json, object : TypeToken<List<PostShoppingModel>>() {}.type)
            //mvmodel.ShoppingListData(model1[0])
            // jghjghjg
            if (intent.hasExtra("prefillMOdify")) {
                prefillingProductModel = ArrayList<PostShoppingModel>()
                prefillingProductModel.addAll(model1)
            }
//          else{
//              productShoppingList.addAll(model1)// only the case of copy
//          }
            mvmodel.ShoppingListData(model1, false, "")


            val gson1 = Gson()
            val json1 = id.delivery_zone
            val deliveryZone: List<DeliveryZoneModel> =
                gson1.fromJson(json1, object : TypeToken<List<DeliveryZoneModel>>() {}.type)
            //mvmodel.ShoppingListData(model1[0])
            for (i in 0 until deliveryZone.size) {
                mbinding.addLocation.visibility = View.VISIBLE

                if (i == 0) {
                    mvmodel.UpdateData(deliveryZone[0], 0)
                } else {
                    mvmodel.addNewAddress(deliveryZone[i])
                }
            }
            //  [{"day":"Monday","value":[{"from":"06:15 AM","to":"07:15 AM"},{"from":"08:15 AM","to":"09:16 AM"}]}]
            val json2 = JSONArray(id.delivery_days)
            for (i in 0 until json2.length()) {
                val jsonObject = json2.optJSONObject(i)
                val values = jsonObject.optJSONArray("value")
                val model = DaysParentModel()
                //     categoryModelList = id.
                val gson = Gson()
                val json = values.toString()
                val model1: ArrayList<SlotModel> =
                    gson.fromJson(json, object : TypeToken<ArrayList<SlotModel>>() {}.type)
                model.weeks = jsonObject.optString("day")
                model.slotList = model1
                setTimeServerData.add(model)
            }
            reUpdateData()
            mbinding.txtdeliverytime.text = updatedDayTime(deliveryTimeList)


            val saraData = JSONObject(id.pickup_details)
            optionalDeliveryZoneModel.lat = saraData.optString("lat")
            optionalDeliveryZoneModel.long = saraData.optString("long")
            optionalDeliveryZoneModel.address = saraData.optString("address")
            mbinding.chooseDeliveryZone.text = optionalDeliveryZoneModel.address
            val days = saraData.optJSONArray("days")

            for (i in 0 until days.length()) {
                val jsonObject = days.optJSONObject(i)
                val values = jsonObject.optJSONArray("value")
                val model = DaysParentModel()
                //     categoryModelList = id.
                val gson = Gson()
                val json = values.toString()
                val model1: ArrayList<SlotModel> =
                    gson.fromJson(json, object : TypeToken<ArrayList<SlotModel>>() {}.type)
                model.weeks = jsonObject.optString("day")
                model.slotList = model1
                setpickupOptionalTimeZone.add(model)
            }
            reUpdateOptionalTimeData()
            mbinding.txtdayAndTimePickOptional.text = updatedDayTime(pickupOptionalTimeZone)

            Log.e("", id.toString())

        } catch (e: Exception) {

            Log.e("", e.toString())

        }

    }


    private fun reUpdateData() {
        val deliveryTimeListNew = ArrayList<DaysParentModel>()
        //for (item in setTimeServerData) {
        val dayOf = arrayListOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )
        for (i in 0 until dayOf.size) {

            var exist = -1
            for (j in 0 until setTimeServerData.size) {
                if (setTimeServerData[j].weeks == dayOf[i]) {
                    exist = j
                    break
                }
            }

            //Compare and fill the slot of monday_sunday..
            if (exist >= 0) {
                if (setTimeServerData[exist].slotList.size > 1) {
                    deliveryTimeListNew.add(setTimeServerData[exist])
                } else {
                    var model = DaysParentModel()
                    val slotMOdelArray = ArrayList<SlotModel>()
                    val slotMOdel = SlotModel()
                    slotMOdel.from = "From"
                    slotMOdel.to = "To"
                    slotMOdelArray.add(setTimeServerData[exist].slotList[0])
                    slotMOdelArray.add(slotMOdel)
                    model = setTimeServerData[exist]
                    model.slotList = slotMOdelArray
                    deliveryTimeListNew.add(model)
                }


                //Not-fill the slot of monday_sunday..
                //and remain black
            } else {
                val model = DaysParentModel()
                val slotMOdel = SlotModel()
                model.weeks = dayOf[i]
                val slotMOdelArray = ArrayList<SlotModel>()
                for (t in 0 until 2) {
                    slotMOdel.from = "From"
                    slotMOdel.to = "To"
                    slotMOdelArray.add(slotMOdel)
                }
                model.slotList = slotMOdelArray
                deliveryTimeListNew.add(model)
            }
        }
        deliveryTimeList.clear()
        deliveryTimeList.addAll(deliveryTimeListNew)

    }

    //***************************** pickUp Optional Time ***********************************//
    private fun reUpdateOptionalTimeData() {
        val deliveryTimeListNew = ArrayList<DaysParentModel>()
        //for (item in setTimeServerData) {
        val dayOf = arrayListOf(
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
        )
        for (i in 0 until dayOf.size) {

            var exist = -1
            for (j in 0 until setpickupOptionalTimeZone.size) {
                if (setpickupOptionalTimeZone[j].weeks == dayOf[i]) {
                    exist = j
                    break
                }
            }

            if (exist >= 0) {
                if (setpickupOptionalTimeZone[exist].slotList.size > 1) {
                    deliveryTimeListNew.add(setpickupOptionalTimeZone[exist])
                } else {
                    var model = DaysParentModel()
                    val slotMOdelArray = ArrayList<SlotModel>()
                    val slotMOdel = SlotModel()
                    slotMOdel.from = "From"
                    slotMOdel.to = "To"
                    slotMOdelArray.add(setpickupOptionalTimeZone[exist].slotList[0])
                    slotMOdelArray.add(slotMOdel)
                    model = setpickupOptionalTimeZone[exist]
                    model.slotList = slotMOdelArray
                    deliveryTimeListNew.add(model)

                }
                // deliveryTimeListNew.add(setpickupOptionalTimeZone[exist])
            } else {
                val model = DaysParentModel()
                val slotMOdel = SlotModel()
                model.weeks = dayOf[i]
                val slotMOdelArray = ArrayList<SlotModel>()
                for (t in 0 until 2) {
                    slotMOdel.from = "From"
                    slotMOdel.to = "To"
                    slotMOdelArray.add(slotMOdel)
                }
                model.slotList = slotMOdelArray
                deliveryTimeListNew.add(model)
            }
        }
        pickupOptionalTimeZone.clear()
        pickupOptionalTimeZone.addAll(deliveryTimeListNew)

    }

    //...............delivery zone array json format...........//
    private fun deliveryZoneJsonList(): String {
        val jsonarray = JSONArray()
        for (i in 0 until deliveryZoneList.size) {
            val jsonobj = JSONObject()
            jsonobj.put("lat", deliveryZoneList[i].lat)
            jsonobj.put("long", deliveryZoneList[i].long)
            jsonobj.put("radius", deliveryZoneList[i].radius)
            jsonobj.put("address", deliveryZoneList[i].address)
            jsonarray.put(jsonobj)
        }
        Log.d("deliveryZoneJsonList= ", jsonarray.toString())
        return jsonarray.toString()

    }

    //............time zone json array.........//
    private fun daysTimeZoneJsonArray(): String {
        val parent = JSONArray()
        for (i in 0 until deliveryTimeList.size) {
            val mArray = JSONArray()
            val jsoninnerobject = JSONObject()
            if (i == 0) {
                jsoninnerobject.put("day", "Monday")
            }

            if (i == 1) {
                jsoninnerobject.put("day", "Tuesday")
            }

            if (i == 2) {
                jsoninnerobject.put("day", "Wednesday")
            }

            if (i == 3) {
                jsoninnerobject.put("day", "Thursday")
            }

            if (i == 4) {
                jsoninnerobject.put("day", "Friday")
            }

            if (i == 5) {
                jsoninnerobject.put("day", "Saturday")
            }

            if (i == 6) {
                jsoninnerobject.put("day", "Sunday")
            }

            for (slot in deliveryTimeList[i].slotList) {
                if (slot.from.contains(":")) {
                    val jsoninnerobject1 = JSONObject()
                    jsoninnerobject1.put("from", slot.from)
                    jsoninnerobject1.put("to", slot.to)
                    mArray.put(jsoninnerobject1)
                }

            }
            if (mArray.length() > 0) {
                jsoninnerobject.put("value", mArray)
                parent.put(jsoninnerobject)
            }
        }

        Log.d("timeZoneJsonArray1= ", parent.toString())
        return parent.toString()
    }

    //...........json format shopping product list..........//
    private fun shoppingProductListJSonArray(): String {

        val jsonarray = JSONArray()
        for (i in 0 until adapter.getAllShoppingArrayData().size) {
            val jsonobject = JSONObject()
            jsonobject.put("name", adapter.getAllShoppingArrayData()[i].name)
            jsonobject.put("image", adapter.getAllShoppingArrayData()[i].image)
            jsonobject.put("unit", adapter.getAllShoppingArrayData()[i].unit)
            jsonobject.put("price", adapter.getAllShoppingArrayData()[i].price)
            jsonobject.put(
                "priceWithComission",
                adapter.getAllShoppingArrayData()[i].priceWithComission
            )
            jsonobject.put("saved_product_id", adapter.getAllShoppingArrayData()[i].saved_product_id)
            jsonobject.put("status", adapter.getAllShoppingArrayData()[i].status)

            jsonarray.put(jsonobject)
        }
        Log.d("shoppingListJSonArray= ", jsonarray.toString())
        return jsonarray.toString()
    }

    //................optional time zone and location zpne json array.............//
    private fun optionalTimeAndLocationJsonArray(): String {
        val jsonobj = JSONObject()
        jsonobj.put("lat", optionalDeliveryZoneModel.lat)
        jsonobj.put("long", optionalDeliveryZoneModel.long)
        jsonobj.put("address", optionalDeliveryZoneModel.address)

        val parent = JSONArray()
        for (i in 0 until pickupOptionalTimeZone.size) {
            val mArray = JSONArray()
            val jsoninnerobject = JSONObject()
            if (i == 0) {
                jsoninnerobject.put("day", "Monday")
            }

            if (i == 1) {
                jsoninnerobject.put("day", "Tuesday")
            }

            if (i == 2) {
                jsoninnerobject.put("day", "Wednesday")
            }

            if (i == 3) {
                jsoninnerobject.put("day", "Thursday")
            }

            if (i == 4) {
                jsoninnerobject.put("day", "Friday")
            }

            if (i == 5) {
                jsoninnerobject.put("day", "Saturday")
            }

            if (i == 6) {
                jsoninnerobject.put("day", "Sunday")
            }

            for (slot in pickupOptionalTimeZone[i].slotList) {
                if (slot.from.contains(":")) {
                    val jsoninnerobject1 = JSONObject()
                    jsoninnerobject1.put("from", slot.from)
                    jsoninnerobject1.put("to", slot.to)
                    mArray.put(jsoninnerobject1)
                }

            }
            if (mArray.length() > 0) {
                jsoninnerobject.put("value", mArray)
                parent.put(jsoninnerobject)
            }
        }
        jsonobj.put("days", parent)
        Log.d("optional = ", jsonobj.toString())
        return jsonobj.toString()
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

    //..................set seek bar click..................//
    private fun seekBarClick() {
        // Set a SeekBar change listener
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
                txtcomission.text = getString(R.string.Your_Comission) + " " + i + "%"
                seekBar.max = seller_maximum_commission.toInt()
                seekBar.min = seller_minimum_commission
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                adapter.update(productShoppingList, seekBar.progress)
                if (seekBar.progress < seller_minimum_commission) {
                    seekBarAlertToast()
                }
            }
        })
    }

    private fun seekBarAlertToast() {
        Toast.makeText(
            this,
            getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
            Toast.LENGTH_SHORT
        ).show()
    }

    //......................... adapter interface data of shopping list send to dialog dialog..............//
    override fun edtData(data: PostShoppingModel, position: Int) {
        adapterEditShoppingModel = data
        adapterShoppingPosition = position
        adapterEditShoppingModel.image = data.image
        mbinding.txtproductOfYourShopping.visibility = View.INVISIBLE
        //mbinding.txtaddmore.text = getString(R.string.Add_product)

        txtname.setFocusable(false)
        edtPurchase.setFocusable(false)
        addProductCustomDialog("edit")
    }

    //..........adapter interface for deelting the data in the particular position
    override fun deleteShoppingList(list: ArrayList<PostShoppingModel>, position: Int) {
        mvmodel.deleteProduct(position, list)
        if (list.size > 0) {
            list.removeAt(position)
        }
        shoppingListData(list)
    }

    //................shopping adapter  data function
    private fun shoppingListData(it: ArrayList<PostShoppingModel>) {
        adapter.update(it, mbinding.seekbar.progress)
        if (it.size > 0) {
            mbinding.txtproductOfYourShopping.visibility = View.VISIBLE
            // mbinding.txtaddmore.text = getString(R.string.Add_more_product)
        } else {
            mbinding.txtproductOfYourShopping.visibility = View.INVISIBLE
            // mbinding.txtaddmore.text = getString(R.string.Add_product)
        }
    }


    //.....................shopping list adapter...........//
    private fun setAdapterShoppingList() {
        val manager = LinearLayoutManager(this)
        adapter = PostShoppingAdapter(this)
        mbinding.recyclers.layoutManager = manager
        mbinding.recyclers.adapter = adapter
        //set the drag and drop
//        val callback: Callback = ItemMoveCallback(adapter)
//        val touchHelper = ItemTouchHelper(callback)
//        touchHelper.attachToRecyclerView(mbinding.recyclers)
    }

    private fun DeliveryZoneTimeAdapter() {
        val manager = LinearLayoutManager(this)
        mAdapter = DeliverZoneAdapter(this)
        mbinding.recycler1.layoutManager = manager
        mbinding.recycler1.adapter = mAdapter

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 102) {
            if (grantResults.contains(-1)) {

            } else {
                showDialog()

            }
        }

        if (requestCode == 101) {
            if (grantResults.contains(-1)) {

                Toast.makeText(
                    this,
                    getString(R.string.Please_accept_permission),
                    Toast.LENGTH_LONG
                ).show()
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 101
                )


            }
            //  else {

            //  showDialog()

//                val intent = Intent(this, DeliveryZoneActivity::class.java)
//                startActivityForResult(intent, 2)
            // }
        }
    }


    //**************************Deliver zone  adapter implement interfae**************************//
    override fun sendDataAdapter(deliveryZoneModel: DeliveryZoneModel, position: Int) {
        adapterPosition = position

        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 101
                )
                // ActivityCompat.requestPermissions(getApplication(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);

            } else {

//                Log.e("Check address", deliveryZoneModel.address)
//                val intent = Intent(this, DeliveryZoneActivity::class.java)
//                intent.putExtra("keySellerDeliveryZone", deliveryZoneModel)
//                startActivityForResult(intent, 2)
                val intent = Intent(this, DeliveryZoneMapBoxActivity::class.java)
                intent.putExtra("keySellerDeliveryZone", deliveryZoneModel)
                startActivityForResult(intent, 2)
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }// add location

    override fun dragDropListClick(list: ArrayList<PostShoppingModel>) {
        val intent = Intent(this, DragDropProductActivity::class.java)
        intent.putParcelableArrayListExtra("KeyDragDropData", list)
        startActivityForResult(intent, 10001)

    }

    //*********************************** ON ACTIVITY RESULT **********************************//
    //......getting the result of name category...........//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
// 1 code getting the category data
        try {

            if (requestCode == chooseCategoryRequestCode && resultCode == RESULT_OK) {
                val datasmodel = data!!.getParcelableExtra<SellerCategoryModel>("keyname")!!
                categoryModelList = datasmodel
                mbinding.txtcategory.text = datasmodel.name
            }
            //data of dialog box to reate the list
            else if (requestCode == 2 && data != null) {
                if (resultCode == Activity.RESULT_OK) {
                    try {


                        val geocoder = Geocoder(this)
                        val address: Address?
                        try {

                            deliveryZoneModel = data.getParcelableExtra("deliveryZone")!!
                            val lat = data.getStringExtra("lat")!!
                            val long = data.getStringExtra("long")!!
                            // 2
                            val addresses = geocoder.getFromLocation(lat!!.toDouble(), long.toDouble(), 1)
                            address = addresses[0]
                            Log.e("Map Fragment ", address.toString())
                            val builder = StringBuilder()
                            //  builder.append(address?.featureName).append(",\t")
                            builder.append(address?.getAddressLine(0)).append(",\t")
                            val resultDestination = builder.toString()
                            deliveryZoneModel.address = resultDestination
                            if (deliveryZoneModel.address.isEmpty()) {
                                Toast.makeText(
                                    this,
                                    getString(R.string.network_error),
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                updateData(deliveryZoneModel, adapterPosition)
                                mbinding.addLocation.visibility = View.VISIBLE
                            }
                            Log.e("Map Fragment2 ", resultDestination)
                            // arraymodel1.add(deliveryZoneModel)
                        } catch (e: IOException) {
                            Log.e("MapsActivity", e.localizedMessage)
                        }

                    } catch (e: Exception) {
                        Log.e("MainActivity", e.message.toString())
                    }
                }
            } else if (requestCode == 3 && data != null) {
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        deliveryZoneModel = data.getParcelableExtra("deliveryZone")!!
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
                            //   builder.append(address?.featureName).append(",\t")
                            builder.append(address?.getAddressLine(0)).append(",\t")
                            val resultDestination = builder.toString()
                            deliveryZoneModel.address = resultDestination
                            if (deliveryZoneModel.address.isEmpty()) {

                                Toast.makeText(
                                    this,
                                    getString(R.string.network_error),
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                mvmodel.addNewAddress(deliveryZoneModel)
                            }

                            //arraymodel1.add(deliveryZoneModel)
                            Log.e("Map Fragment2 ", resultDestination)
                        } catch (e: IOException) {
                            Log.e("MapsActivity", e.localizedMessage)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", e.message.toString())
                    }
                }
            } else if (requestCode == 129 && data != null) {

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
                            //    builder.append(address?.featureName).append(",\t")
                            builder.append(address?.getAddressLine(0)).append(",\t")
                            val resultDestination = builder.toString()
                            optionalDeliveryZoneModel = data.getParcelableExtra("deliveryZone")!!
                            optionalDeliveryZoneModel.address = resultDestination
                            if (optionalDeliveryZoneModel.address.isEmpty()) {

                                Toast.makeText(
                                    this,
                                    getString(R.string.network_error),
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                mbinding.chooseDeliveryZone.text =
                                    optionalDeliveryZoneModel.address
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
            //......................getting the image of dialog...........//
            else if (requestCode == 22 && resultCode == Activity.RESULT_OK) {
                //   try {

                val resultBitmap = getRotateImage(photoFile)
                val uri = getImageUri(resultBitmap)

//..................................Api FOr Update Image....................................................................//
                mvmodel.profileImage(Constant.saveBitmapToFile(uri)!!)//send the data for the api
                // mvmodel.profileImage(uri)//send the data for the api
                dialog.progressdialog.visibility = View.VISIBLE
                dialog.img.visibility = View.INVISIBLE

            }
//2nd code
            else if (requestCode == 1 && data != null) {
                try {

                    val selectedPicture: Uri = data.getData()!!

                    val file: File =
                        Constant.uploadImage2(
                            Constant.getBitmapImage(selectedPicture, this),
                            this
                        )
                    dialog.progressdialog.visibility = View.VISIBLE
                    dialog.img.visibility = View.INVISIBLE
                    mvmodel.profileImage(file)//send the data for the api

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, getString(R.string.Failed), Toast.LENGTH_SHORT).show()
                }
            }
            //time zone req code
            else if (requestCode == 100 && resultCode == RESULT_OK) {

                val setdata = data!!.getParcelableArrayListExtra<DaysParentModel>("daysmodel")!!
                deliveryTimeList = setdata
                mbinding.txtdeliverytime.text = updatedDayTime(setdata)
                //optional time zone request
            } else if (requestCode == optionTimePickerCode && resultCode == Activity.RESULT_OK) {

                val setdata = data!!.getParcelableArrayListExtra<DaysParentModel>("daysmodel")!!
                pickupOptionalTimeZone = setdata
                mbinding.txtdayAndTimePickOptional.text = updatedDayTime(pickupOptionalTimeZone)

            } else if (requestCode == 500 && resultCode == Activity.RESULT_OK) {

                val productSavedArray =
                    data!!.getParcelableArrayListExtra<PostShoppingModel>("keySavedProductData")!!
                mvmodel.ShoppingListData(productSavedArray, false, "")
            } else if (requestCode == 10001 && resultCode == Activity.RESULT_OK) {

                val productSavedArray =
                    data!!.getParcelableArrayListExtra<PostShoppingModel>("KeyDragDropData")!!
                mvmodel.ShoppingListData(productSavedArray, false, "clearArray")
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateData(deliveryZoneModel: DeliveryZoneModel, adapterPosition: Int) {

        mvmodel.UpdateData(deliveryZoneModel, adapterPosition)
    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: ArrayList<DaysParentModel>): String {
        var result: String = ""

        for (item in daysArraymodel) {

            for (child in item.slotList) {

                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                        Constant.getDayString(
                            this,
                            item.weeks
                        ) + "(" + Constant.ConvertAmPmFormat(
                            this,
                            child.from
                        ) + "-" + Constant.ConvertAmPmFormat(this, child.to) + ")"

                    } else {
                        if (result.contains(Constant.getDayString(this, item.weeks))) {
                            result + ", " + "(" + Constant.ConvertAmPmFormat(
                                this,
                                child.from
                            ) + "-" + Constant.ConvertAmPmFormat(this, child.to) + ")"
                        } else {
                            result + ", " + Constant.getDayString(
                                this,
                                item.weeks
                            ) + "(" + Constant.ConvertAmPmFormat(
                                this,
                                child.from
                            ) + "-" + Constant.ConvertAmPmFormat(this, child.to) + ")"
                        }
                    }
                }
            }

        }
//        if (result.isEmpty()) {
//            result = ""
//        }

        return result
    }

    private fun getRotateImage(photoFile: File?): Bitmap {
        val ei: ExifInterface = ExifInterface(photoFile!!.path)
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        val bitmap = BitmapFactory.decodeFile(photoFile.path)

        var rotatedBitmap: Bitmap? = bitmap
        when (orientation) {

            ExifInterface.ORIENTATION_ROTATE_90 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 90)


            ExifInterface.ORIENTATION_ROTATE_180 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 180)


            ExifInterface.ORIENTATION_ROTATE_270 ->
                rotatedBitmap = TransformationUtils.rotateImage(bitmap, 270)


            ExifInterface.ORIENTATION_NORMAL ->
                rotatedBitmap = bitmap

        }

        return rotatedBitmap!!
    }

    //.........setuptool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Post_Shopping_List)
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

    //...............set up dialog for getting the product list................//
    private fun addProductCustomDialog(from: String) {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.seller_add_more_product_dialog)
        dialog.show()
        //hide the emoji
        dialog.edtname.filters = arrayOf<InputFilter>(HideEmoji(this), LengthFilter(50))
        val Units = arrayOf("Select Unit", "unit", "oz", "lbs")
        val spinnerAdapters = SpinnerShoppingListAdapter(this, Units)
        dialog.edtunit2!!.adapter = spinnerAdapters
        dialog.setOnCancelListener {
            txtname.setFocusableInTouchMode(true)
            txtname.setFocusable(true)
            edtPurchase.setFocusableInTouchMode(true)
            edtPurchase.setFocusable(true)
        }
// edit case model
        if (adapterEditShoppingModel.name.length > 0) {
            val model = DialogModel()
            model.name = adapterEditShoppingModel.name
            model.unit = adapterEditShoppingModel.unit
            model.price = adapterEditShoppingModel.price
            model.image = adapterEditShoppingModel.image
            model.priceWithComission = adapterEditShoppingModel.priceWithComission//editext case field
            model.saved_product_id = adapterEditShoppingModel.saved_product_id//editext case field
            dialog.edtname.setText(model.name)

            if (model.unit == "unit") {
                dialog.edtunit2.setSelection(1)
            } else if (model.unit == "oz") {
                dialog.edtunit2.setSelection(2)
            } else if (model.unit == "lbs") {
                dialog.edtunit2.setSelection(3)
            }

            dialog.edtprice.setText(model.price)
            if (model.image.isNotEmpty()) {
                Glide.with(this)
                    .load(model.image)
                    .into(dialog.img)
            } else {
                Glide.with(this)
                    .load(R.drawable.ic_product_img)
                    .into(dialog.img)
            }
            dialog.edtPriceWithComission.setText(Constant.roundValue(model.priceWithComission.toDouble()))
        } else {
               //all field is empty
            dialog.edtname.setText("")
            dialog.edtunit2.setSelection(0)
            dialog.edtprice.setText("")
            dialog.edtPriceWithComission.setText("")//new field
            Glide.with(this)
                .load(R.drawable.ic_product_img)
                .into(dialog.img)
        }


//****************************set the price by the dialog****************//
        dialog.edtprice.setOnClickListener {
            addPriceCustomDialog(
                "Price",
                dialog.edtprice.text.toString().trim(),
                Constant.getProfileData(this).plateform_commission
            )
        }
        //*****************Price With comission*****************//
        dialog.edtPriceWithComission.setOnClickListener {
            addPriceCustomDialog(
                "PriceWithComission",
                dialog.edtPriceWithComission.text.toString().trim(),
                Constant.getProfileData(this).plateform_commission
            )
        }


        //..........set the click on the image............//

        dialog.img.setOnClickListener {
            if (Constant.checkPermissionForCameraGallery(this))
                showDialog()
        }

        //set the click button of custom dialog
        dialog.btnAdd.setOnClickListener {
            txtname.setFocusableInTouchMode(true)
            txtname.setFocusable(true)
            edtPurchase.setFocusableInTouchMode(true)
            edtPurchase.setFocusable(true)
            val id = dialog.edtunit2.selectedItemId.toInt()

            //*****************edit  button model click  check the data is  available or not
            if (adapterEditShoppingModel.name.length > 0) {
//                adapterEditShoppingModel.name = dialog.edtname.text.toString()
//                adapterEditShoppingModel.unit = setUnit(dialog.edtunit2.selectedItem.toString())
//                adapterEditShoppingModel.price = dialog.edtprice.text.toString()
//                adapterEditShoppingModel.priceWithComission =
//                    dialog.edtPriceWithComission.text.toString()
                //adapterShoppingModel!!.image = modelDialog.image

                if (adapterEditShoppingModel.name.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener

                }
                else if (adapterEditShoppingModel.name.length > 50) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_add_less_than_thirty_char),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                else if (adapterEditShoppingModel.unit == "Select Unit") {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                else if (adapterEditShoppingModel.price.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Price),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                else if (adapterEditShoppingModel.image.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_select_The_Image),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                else if (adapterEditShoppingModel.priceWithComission.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_enter_the_price_comission),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return@setOnClickListener
                }
                else {
                    val old = adapterEditShoppingModel.name
                    val new = dialog.edtname.text.toString()
                    if (old == new) {
                        adapterEditShoppingModel.name = old
                        adapterEditShoppingModel.unit =
                            setUnit(dialog.edtunit2.selectedItem.toString())
                        adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                        adapterEditShoppingModel.priceWithComission =
                            dialog.edtPriceWithComission.text.toString()
                        // adapterShoppingModel!!.image = modelDialog.image
                        mvmodel.editListUpdate(
                            adapterEditShoppingModel,
                            adapterShoppingPosition,
                            false,
                            ""
                        )
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()
                    }
                    else {
                        adapterEditShoppingModel.name = new
                        adapterEditShoppingModel.unit =
                            setUnit(dialog.edtunit2.selectedItem.toString())
                        adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                        // adapterShoppingModel!!.image = modelDialog.image
                        mvmodel.editListUpdate(
                            adapterEditShoppingModel,
                            adapterShoppingPosition,
                            true,
                            "nameEdited"
                        )
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()

                    }
                }
//**********************without edit data first time they will come************//
            }
            else {

                val model = PostShoppingModel()
                if (dialog.edtname.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtname.text.length > 50) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_add_less_than_thirty_char),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (id == 0) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtprice.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Price),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtPriceWithComission.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_enter_the_price_comission),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (adapterEditShoppingModel.image.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_select_The_Image),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {

                    model.name = dialog.edtname.text.toString()
                    model.unit = setUnit(dialog.edtunit2.selectedItem.toString())
                    model.price = dialog.edtprice.text.toString()
                    model.priceWithComission = dialog.edtPriceWithComission.text.toString()
                    model.image = adapterEditShoppingModel.image //global model we want to set the image
                    model.saved_product_id = "0"
                    model.status ="1"
                    val array = ArrayList<PostShoppingModel>()

//                    if (array.size > 5) {
//                        Toast.makeText(
//                            this,
//                            getString(R.string.You_cannot_add_product_more_than), Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
                    //********************** that is the case of prefilling************//
                    if (from == "edit") {
                        val old = adapterEditShoppingModel.name
                        val new = dialog.edtname.text.toString()

                        if (old == new) {
                            adapterEditShoppingModel.name = old
                            adapterEditShoppingModel.unit =
                                setUnit(dialog.edtunit2.selectedItem.toString())
                            adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                            adapterEditShoppingModel.priceWithComission =
                                dialog.edtPriceWithComission.text.toString()
                            // adapterShoppingModel!!.image = modelDialog.image
                            mvmodel.editListUpdate(
                                adapterEditShoppingModel,
                                adapterShoppingPosition,
                                false,
                                ""
                            )
                            Constant.hideSoftKeyboard(it, this)
                            dialog.cancel()
                        } else {
                            adapterEditShoppingModel.name = new
                            adapterEditShoppingModel.unit =
                                setUnit(dialog.edtunit2.selectedItem.toString())
                            adapterEditShoppingModel.price = dialog.edtprice.text.toString()
                            adapterEditShoppingModel.priceWithComission =
                                dialog.edtPriceWithComission.text.toString()
                            // adapterShoppingModel!!.image = modelDialog.image
                            mvmodel.editListUpdate(
                                adapterEditShoppingModel,
                                adapterShoppingPosition,
                                true,
                                "nameEdited"
                            )
                            Constant.hideSoftKeyboard(it, this)
                            dialog.cancel()
                        }
                        return@setOnClickListener
                    }


                        array.add(model)
                        mvmodel.ShoppingListData(array, false, "")
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()
                   // }
                    dialog.cancel()
                }
            }
        }
        dialog.setOnDismissListener {
            adapterEditShoppingModel = PostShoppingModel()
            adapterShoppingPosition = -1
        }
    }

    private fun setUnit(unit: String): String {
        var units = getString(R.string.Select_Unit)
        if (unit == "1") {
            units = "unit"
        } else if (unit == "2") {
            units = "oz"
        } else if (unit == "3") {
            units = "lbs"
        }
        return units

    }

    //....................................Update profile image by gallary and camera.................//
    private fun showDialog() {
        val builder: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.Open_By))
        val mBinding: OpenFromCameraPermissionBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.open_from_camera_permission,
                null,
                false
            )

        if (check == 0) {
            mBinding.camera.isChecked = true
            mBinding.gallery.isChecked = false
        }
        if (check == 1) {
            mBinding.gallery.isChecked = false
            mBinding.camera.isChecked = true
        }
        builder.setView(mBinding.root)
        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->

            if (mBinding.camera.isChecked) {
                openCamera()
                check = 0


            } else if (mBinding.gallery.isChecked) {
                openGallery()
                check = 1

            }
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->

        }
        builder.create()
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            photoFile = createImageFile()
        } catch (ex: Exception) {
            // Error occurred while creating the File
            Log.i("", "IOException")
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            val uri = FileProvider.getUriForFile(
                this,
                "com.app.caiguru" + ".provider",
                photoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, 22)
        }
    }

    @SuppressLint("IntentReset")
    private fun openGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intent.setType("image/*")
        startActivityForResult(intent, 1)
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName,  // prefix
            ".jpg",         // suffix
            storageDir      // directory
        )
        return image
    }

    //url link get to used this
    private fun getImageUri(inImage: Bitmap): File {
        val root: String = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/req_images")
        myDir.mkdirs()
        val fname = "Image_profile.jpg"
        val file = File(myDir, fname)
        if (file.exists())
            file.delete()

        try {
            val out = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }


    //set the image in the dialog
    private fun setProfilePic(image: String) {
        adapterEditShoppingModel.image = image
        modelDialog.image = image
        Glide.with(this)
            .load(image)
            .into(dialog.img)
    }

    @SuppressLint("SetTextI18n")
    private fun creditDeductionLogic(sellerLevels: String) {
        when (sellerLevels) {
            "1" -> freeProducts = 5
            "2" -> freeProducts = 6
            "3" -> freeProducts = 8
            "4" -> freeProducts = 10
        }
        textMoreProducts.text =
                //${getString(R.string.credit_for_additional)}
            "${getString(R.string.You_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_additional)} $freeProducts ${
                getString(
                    R.string.Product
                )
            }"

    }

    // add price custom dialog
    private fun addPriceCustomDialog(
        type: String,
        Price: String,
        plateformCommission: String
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.add_price_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(view)
            .create()
        //prefilling
        if (type == "Price") {
            view.productheading.text = getString(R.string.Set_Price)
            if (Price.isEmpty()) {
                view.edtPrice.setText("")
            } else {
                view.edtPrice.setText(Price)
            }
        } else {
            view.productheading.text = getString(R.string.Price_header_text)
            view.edtPrice.hint = getString(R.string.Enter_price_With_comissionTExt2)
            if (Price.isEmpty()) {
                view.edtPrice.setText("")
            } else {
                view.edtPrice.setText(Price)
            }
        }
        mBuilder.show()
        //set the click on the button
        view.btnAddPrice.setOnClickListener {
            if (view.edtPrice.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_add_product_price),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (view.edtPrice.text.toString().toDouble() <= 0) {
                Toast.makeText(
                    this,
                    getString(R.string.price_should_be_greater_than_zero),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                if (type == "Price") {
                    var price = view.edtPrice.text.toString().trim()
                    price = price.replace(",", ".")
                    val commission_per = mbinding.seekbar.progress
                    val allComission = commission_per + plateformCommission.toDouble()
                    val comissiontotal = price.toDouble() / 100 * allComission
                    val alltotal = price.toDouble() + comissiontotal


                    dialog.edtprice.setText(Constant.roundValue(price.toDouble()))
                    dialog.edtPriceWithComission.setText(Constant.roundValue(alltotal))
                    mBuilder.dismiss()
                } else {

                    var price = view.edtPrice.text.toString().trim()
                    price = price.replace(",", ".")
                    val commission_per = mbinding.seekbar.progress
                    val allComission = commission_per + plateformCommission.toDouble()
                    val priceA = price.toDouble() * 100
                    val PriceB = 100 + allComission
                    val comissiontotal = priceA / PriceB
                    dialog.edtPriceWithComission.setText(Constant.roundValue(price.toDouble()))
                    dialog.edtprice.setText(Constant.roundValue(comissiontotal))
                    mBuilder.dismiss()

                }
            }
        }

    }

    fun paymentMethodSelect() {
        edtSelectPaymentMethod.setOnClickListener {
            val alltext = edtSelectPaymentMethod.text.toString().trim()
            openPaymentDialog(alltext)

        }
    }

    private fun openPaymentDialog(alltext: String) {
        val dialog = Dialog(this)
        // dialog.setCancelable(false);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.payment_option_dialog)
        dialog.show()
        val manager = LinearLayoutManager(this)
        dialog.paymentMethodRecycler.layoutManager = manager
        paymentmethodAdapter = PaymentMethodAdapter(this)
        dialog.paymentMethodRecycler.adapter = paymentmethodAdapter
        if (alltext.isEmpty()) {
            paymentmethodAdapter.update(Constant.setUpPaymentMethods(this))
        } else {
            //prefilling case
            paymentmethodAdapter.update(setUpPrefillingText(alltext))
        }
        //**************ok button
        dialog.btnOk.setOnClickListener {
            if (paymentmethodAdapter.getSelectedData().isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_select_at_least_one),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                edtSelectPaymentMethod.text = paymentmethodAdapter.getSelectedData()
                dialog.dismiss()
            }
        }
    }

    //prefilling in the dialog
    private fun setUpPrefillingText(alltext: String): ArrayList<PaymentMethodModel> {
        val localArray = Constant.setUpPaymentMethods(this)
        val removeSpaceString = alltext.replace(", ", ",")
        val allmyData = removeSpaceString.split(",")
        for (items in localArray) {
            for (myString in allmyData) {
                if (items.paymentName == myString) {
                    items.isSelected = true
                }
            }
        }
        return localArray
    }

    //*************send the data to the api******************//
    private fun ConvertPuchasedOrder(purchaseText: String): String {
        val localArray = Constant.setUpPaymentMethods(this)
        val removeSpaceString = purchaseText.replace(", ", ",")
        val allmyData = removeSpaceString.split(",")
        var selectedPayment = ""
        for (items in localArray) {
            for (myString in allmyData) {
                if (items.paymentName == myString) {
                    if (selectedPayment.isEmpty()) {
                        selectedPayment = items.paymentId
                    } else {
                        selectedPayment = "$selectedPayment,${items.paymentId}"

                    }
                }
            }
        }
        Log.e("yspppp", selectedPayment)
        return selectedPayment
    }
    //*******************Add product popup

    private fun showPopUpProductAdd() {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(
            getString(R.string.add_product_alert_text)
        )
        mDialog.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            if (Constant.getProfileData(this).enable_seller_commission == "1") {

                if (mbinding.seekbar.progress < seller_minimum_commission) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Select_Comission_toast2) + " " + seller_minimum_commission + "%",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    txtname.setFocusable(false)
                    edtPurchase.setFocusable(false)
                    addProductCustomDialog("add")//first time dialog open
                }
            } else {
                txtname.setFocusable(false)
                edtPurchase.setFocusable(false)
                addProductCustomDialog("add")//first time dialog open
            }
            dialog.cancel()
        }
        mDialog.setNegativeButton(
            getString(R.string.no)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
    }

//var data=   adapter.getAllShoppingArrayData()
}



