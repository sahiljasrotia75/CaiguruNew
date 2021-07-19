package com.example.caiguru.buyer.postList.buyerPostScreenPrefilled

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.BuyerAddressMapBoxActivity
import com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime.PostBuyerShopListModel
import com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime.SpinnerAdapterss
import com.example.caiguru.buyer.postList.shoppingListPosted.BuyerShoppingListPostedActivity
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.ActivityShoplistPrefillBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.sellerChooseCategory.SellerChooseCategoryActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.SellerSelectDaysAndTimeActivity
import com.example.caiguru.seller.sellerSelectedDaysAndTime.SlotModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_home_screen.*
import kotlinx.android.synthetic.main.buyer_add_more_product_dialog.*
import kotlinx.android.synthetic.main.buyer_add_more_product_dialog.btnAdd
import kotlinx.android.synthetic.main.buyer_postshoplist_fragment.*
import kotlinx.android.synthetic.main.show_total_credits_dialog.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.toolbar.view.done
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class BuyerPostShopPreFillActivity : AppCompatActivity(),
    PostShoppingPrefillBuyerAdapter.editDataInterface {

    private lateinit var dialogs: Dialog
    private val setTimeServerData = ArrayList<DaysParentModel>()
    lateinit var mbinding: ActivityShoplistPrefillBinding
    lateinit var text: TextView
    lateinit var toolbarText: TextView
    var freeProducts = 0
    private var buyerLevel: String = ""
    private var creditPerProduct: String = ""
    private var buyerCredits: String = ""

    //global model
    private var deliveryTimeList = ArrayList<DaysParentModel>()//days model

    private var addAddressModel = DeliveryZoneModel()//statics
    var arrayAddAddressModel = ArrayList<DeliveryZoneModel>()//location model statics
    private var adapterShoppingPosition: Int = -1
    private lateinit var dialog: Dialog
    lateinit var mvmodel: BuyerPostPrefillViewModel
    var adapter: PostShoppingPrefillBuyerAdapter? = null
    private var categoryModelList = SellerCategoryModel()
    private var adapterShoppingModel: PostShoppingModel? = null
    private var productShoppingList = ArrayList<PostShoppingModel>()
    var check: String = ""
    var listingname = ""
    val postBuyer =
        PostBuyerShopListModel()
    var listId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_shoplist_prefill)
        //mvmodel = ViewModelProviders.of(this)[BuyerPostPrefillViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerPostPrefillViewModel::class.java)
        SettingUpToolbar()
        setObserver()
        txtname.filters = arrayOf<InputFilter>(HideEmoji(this), InputFilter.LengthFilter(40))
        setClick()
        setAdapterShoppingList()
        //get the category
        getProfile()
        creditDeductionLogic()
        mbinding.txtcategory.setOnClickListener {
            val intent = Intent(this, SellerChooseCategoryActivity::class.java)
            startActivityForResult(intent, 111)

        }
        //get the chooseDeliveryZone
        mbinding.chooseDeliveryZone.setOnClickListener {
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
                } else {
                    // val intent = Intent(this, BuyerAddAddressActivity::class.java)
                    val intent = Intent(this, BuyerAddressMapBoxActivity::class.java)
                    intent.putExtra("keyPrefillAddres", arrayAddAddressModel)
                    startActivityForResult(intent, 112)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //get the txtdayAndTimePickOptional
        mbinding.txtdayAndTimePickOptional.setOnClickListener {
            val intent = Intent(this, SellerSelectDaysAndTimeActivity::class.java)
            intent.putExtra("keyBuyerpost", deliveryTimeList)
            startActivityForResult(intent, 113)
        }


        val customerChildModel = intent.getParcelableExtra<CustomerChildModel>("model")

        mbinding.txtname.setText(customerChildModel!!.listingname)
        listId = customerChildModel.id


        val categoriesList = Constant.categoryData(this)
        for (category in categoriesList) {
            if (category.category_id == customerChildModel.cat_id) {
                mbinding.txtcategory.setText(category.name)
            }else{
                mbinding.txtcategory.setText( getString(R.string.mix_category_product))

            }
        }
        categoryModelList.category_id = customerChildModel.cat_id


        // ****************************************Post buyer list ************************************//

        val done = findViewById<TextView>(R.id.done)
        done.setOnClickListener {
            val model = buyer1()

            if (model.listingname.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_Name_Of_The_List),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (model.category_id.isEmpty()) {

                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_Category),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (model.daysTimeZoneJsonArray == "[]") {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_Days_You_Recive_Order),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (model.shoppingProductListJSonArray == "[]") {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Add_Your_Product),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (model.PostShoppingModel.size < 2) {
                Constant.firstTimeCaiguruDialog(this)
                return@setOnClickListener
            } else if (model.deliveryZoneJsonList == "[]") {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Select_Your_Location),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

//                if (model.totalCredits > 0) {
                val gson = Gson()
                val json = customerChildModel.product_details
                val getarrayModelOld: List<PostShoppingModel> =
                    gson.fromJson(json, object : TypeToken<List<PostShoppingModel>>() {}.type)
                if (model.PostShoppingModel.size > freeProducts && getarrayModelOld.size < model.PostShoppingModel.size) {
                    AlertUserAddProductAfterFive(model, getarrayModelOld)
                } else {
                    mvmodel.createShoppingList(
                        model.listingname,
                        model.category_id,
                        model.deliveryZoneJsonList,
                        model.daysTimeZoneJsonArray,
                        model.shoppingProductListJSonArray,
                        listId
                    )
                    PleasWait.visibility = View.VISIBLE
                    done.visibility = View.GONE
                }
//                } else {
//                    Toast.makeText(this, getString(R.string.You_have_no_credits), Toast.LENGTH_LONG)
//                        .show()
//                }

            }
        }
        try {

            val gson1 = Gson()
            val json1 = customerChildModel.delivery_location
            arrayAddAddressModel =
                gson1.fromJson(json1, object : TypeToken<List<DeliveryZoneModel>>() {}.type)

            addAddressModel = arrayAddAddressModel[0]
            mbinding.chooseDeliveryZone.text = addAddressModel.address
            arrayAddAddressModel.add(addAddressModel)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val json2 = JSONArray(customerChildModel.delivery_daytime)
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
        mbinding.txtdayAndTimePickOptional.text = updatedDayTime(deliveryTimeList)

//send the prefilling data
        val gson = Gson()
        val json = customerChildModel.product_details
        val model1: List<PostShoppingModel> =
            gson.fromJson(json, object : TypeToken<List<PostShoppingModel>>() {}.type)
        mvmodel.ShoppingListData(model1, true)


        // show the icon of dollar
        homeToolbar.checkTotalCredits.visibility = View.VISIBLE
        homeToolbar.checkTotalCredits.setOnClickListener {
            Constant.customDialogShowTotalCredits(this)
        }
    }

    private fun AlertUserAddProductAfterFive(
        model: PostBuyerShopListModel,
        oldModel: List<PostShoppingModel>
    ) {

        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        val credits = Constant.getProfileData(this).credits.toDouble().toInt().toString()

        mDialog.setMessage(
            "${getString(R.string.you_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_each_additional_product)} $freeProducts ${getString(
                R.string.products
            )}. ${getString(R.string.credits_in_your_account)}: ${credits} ${getString(R.string.cr)}"
        )
        mDialog.setPositiveButton(
            getString(R.string.yes),
            DialogInterface.OnClickListener { dialog, which ->
                if (buyerCredits.toDouble() < mvmodel.getCreditDeduction().toDouble()) {
                    Toast.makeText(this, getString(R.string.You_have_no_credits), Toast.LENGTH_LONG)
                        .show()
                    dialog.cancel()
                } else {
                    mvmodel.createShoppingList(
                        model.listingname,
                        model.category_id,
                        model.deliveryZoneJsonList,
                        model.daysTimeZoneJsonArray,
                        model.shoppingProductListJSonArray,
                        listId

                    )
                    PleasWait.visibility = View.VISIBLE
                    done.visibility = View.GONE
                }
            })
        mDialog.setNegativeButton(
            getString(R.string.no)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
    }

//    private fun customDialogShowTotalCredits() {
//        var model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//        model = gson.fromJson(json, GetProfileModel::class.java)
//
//        dialogs = Dialog(this)
//        dialogs.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialogs.setContentView(R.layout.show_total_credits_dialog)
//        dialogs.show()
//        val credits = Constant.roundValue(model.credits.toDouble())
//        dialogs.totalCredits.text = credits + getString(R.string.credits)
//        //set the click on  button
//        dialogs.Ok.setOnClickListener {
//            dialogs.dismiss()
//        }
//    }

    fun reUpdateData() {
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
            if (exist >= 0) {
                if (setTimeServerData[exist].slotList.size > 1) {
                    deliveryTimeListNew.add(setTimeServerData[exist])
                } else {
                    var model = DaysParentModel()
                    val slotMOdelArray = ArrayList<SlotModel>()
                    val slotMOdel = SlotModel()
                    slotMOdel.from = getString(R.string.From)
                    slotMOdel.to = getString(R.string.To)
                    slotMOdelArray.add(setTimeServerData[exist].slotList[0])
                    slotMOdelArray.add(slotMOdel)
                    model = setTimeServerData[exist]
                    model.slotList = slotMOdelArray
                    deliveryTimeListNew.add(model)
                }

            } else {
                val model = DaysParentModel()
                val slotMOdel = SlotModel()
                model.weeks = dayOf[i]
                val slotMOdelArray = ArrayList<SlotModel>()
                for (t in 0 until 2) {
                    slotMOdel.from = getString(R.string.From)
                    slotMOdel.to = getString(R.string.To)
                    slotMOdelArray.add(slotMOdel)
                }
                model.slotList = slotMOdelArray
                deliveryTimeListNew.add(model)
            }
        }
        deliveryTimeList.clear()
        deliveryTimeList.addAll(deliveryTimeListNew)

    }


    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.homeToolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        toolbarText = findViewById(R.id.PleasWait)
        homeToolbar.done.visibility = View.VISIBLE
        text.text = getText(R.string.post_shopping_list)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        //inflate menu
        toolbarText.setText(getString(R.string.Wait))
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


    //*************************** Web Service of Post Buyer Shopping List **************************//

    fun buyer1(): PostBuyerShopListModel {
        postBuyer.category_id = categoryModelList.category_id
        postBuyer.listId = ""
        postBuyer.listingname = txtname.text.toString().trim()
        postBuyer.shoppingProductListJSonArray = shoppingProductListJSonArray()
        postBuyer.deliveryZoneJsonList = deliveryZoneJsonList()
        postBuyer.daysTimeZoneJsonArray = daysTimeZoneJsonArray()
        //    postBuyer.credDitToDeduct = creditToDeduct.toDouble()
        postBuyer.totalCredits = buyerCredits.toDouble().toInt()
        postBuyer.PostShoppingModel = productShoppingList
        return postBuyer
    }

    //**************************************************************************************************//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//*************************getting the category***********************//
        if (requestCode == 111 && resultCode == AppCompatActivity.RESULT_OK) {

            val datasmodel = data!!.getParcelableExtra<SellerCategoryModel>("keyname")!!
            categoryModelList = datasmodel
            mbinding.txtcategory.text = datasmodel.name

            //************************getting the user location*********************//

        } else if (requestCode == 112 && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    val geocoder = Geocoder(this)
                    val address: Address?
                    try {

                        addAddressModel = data.getParcelableExtra("deliveryZone")!!
                        val lat = data.getStringExtra("lat")
                        val long = data.getStringExtra("long")

                        val addresses = geocoder.getFromLocation(lat!!.toDouble(), long!!.toDouble(), 1)
                        address = addresses[0]
                        Log.e("Map Fragment ", address.toString())
                        val builder = StringBuilder()
                        //   builder.append(address?.featureName).append(",\t")
                        builder.append(address?.getAddressLine(0)).append(",\t")
                        val resultDestination = builder.toString()
                        addAddressModel.address = resultDestination
                        if (addAddressModel.address.isEmpty()) {

                            Toast.makeText(
                                this,
                                getString(R.string.network_error),
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            arrayAddAddressModel.add(addAddressModel)
                            mbinding.chooseDeliveryZone.text = addAddressModel.address
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

        //**********************time zone req code******************************//

        if (requestCode == 113 && resultCode == AppCompatActivity.RESULT_OK) {

            val setdata = data!!.getParcelableArrayListExtra<DaysParentModel>("daysmodel")!!
            deliveryTimeList = setdata
            mbinding.txtdayAndTimePickOptional.text = updatedDayTime(setdata)
            //optional time zone request
        }
    }

    //..............delivery zone array json format...........//
    fun deliveryZoneJsonList(): String {
        val jsonarray = JSONArray()
        for (i in 0 until arrayAddAddressModel.size) {
            if (arrayAddAddressModel.size - 1 == i) {
                val jsonobj = JSONObject()
                jsonobj.put("lat", arrayAddAddressModel[i].lat)
                jsonobj.put("long", arrayAddAddressModel[i].long)
                jsonobj.put("radius", arrayAddAddressModel[i].radius)
                jsonobj.put("address", arrayAddAddressModel[i].address)
                jsonarray.put(jsonobj)
            }
        }
        Log.d("deliveryZoneJsonList= ", jsonarray.toString())
        postBuyer.deliveryZoneJsonList = jsonarray.toString()
        return jsonarray.toString()
    }
    //............time zone json array.........//

    fun daysTimeZoneJsonArray(): String {
        var parent = JSONArray()
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
        postBuyer.daysTimeZoneJsonArray = parent.toString()
        return parent.toString()
    }


    // ...........json format shopping product list..........//

    fun shoppingProductListJSonArray(): String {

        val jsonarray = JSONArray()
        for (i in 0 until productShoppingList.size) {
            val jsonobject = JSONObject()
            jsonobject.put("name", productShoppingList[i].name)
            // jsonobject.put("image", productShoppingList[i].image)
            jsonobject.put("unit", productShoppingList[i].unit)
            jsonobject.put("qty", productShoppingList[i].qty)
            jsonarray.put(jsonobject)
        }
        Log.d("shoppingListJSonArray= ", jsonarray.toString())
        postBuyer.shoppingProductListJSonArray = jsonarray.toString()
        return jsonarray.toString()
    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: ArrayList<DaysParentModel>): String {
        var result: String = ""

        for (item in daysArraymodel) {

            for (child in item.slotList) {

                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                        Constant.getDayString(this, item.weeks) + "(" + Constant.ConvertAmPmFormat(
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
        if (result.isEmpty()) {
            result = getString(R.string.Delivery_Time)
        }

        return result
    }

    private fun setAdapterShoppingList() {
        val manager = LinearLayoutManager(this)
        adapter =
            PostShoppingPrefillBuyerAdapter(
                this
            )
        mbinding.recycler.layoutManager = manager
        mbinding.recycler.adapter = adapter
    }

    fun setClick() {
        //..........set the click on the add Button in list...................//
        mbinding.btnAddproduct.setOnClickListener {

            txtname.setFocusable(false)
            //set the dialog
            addProductCustomDialog()

        }
    }

    private fun setObserver() {
        mvmodel.sendData().observe(this, Observer {
            setAdapterShoppingList()
            productShoppingList = it
            adapter!!.update(it)

            if (it.size > 0) {
                mbinding.txtproductOfYourShopping.visibility = View.VISIBLE

                //mBinding.txtaddmore.text = getString(R.string.Add_more_product)
            } else {
                mbinding.txtproductOfYourShopping.visibility = View.GONE
                // mbinding.txtaddmore.text = getString(R.string.Add_product)
            }

        })

        mvmodel.mSucessfulCreateShoppingList().observe(this, Observer {
            PleasWait.visibility = View.GONE
            done.visibility = View.VISIBLE
            val intent = Intent(this, BuyerShoppingListPostedActivity::class.java)
            intent.putExtra("keyshopping", it)
            startActivity(intent)
            finish()


        })
        //failure case
        mvmodel.mFailureShoppingError().observe(this, Observer {
            PleasWait.visibility = View.GONE
            done.visibility = View.VISIBLE

            val msg = it
            showErrorDialog(msg)
        })

    }


    override fun deleteShoppingList(list: ArrayList<PostShoppingModel>, position: Int) {
        if (list.size > 0) {
            list.removeAt(position)
            mvmodel.updateCredits(list.size)
        }
        shoppingListData(list)
    }

    private fun shoppingListData(list: java.util.ArrayList<PostShoppingModel>) {
        adapter!!.update(list)
        if (list.size > 0) {
            mbinding.txtproductOfYourShopping.visibility = View.VISIBLE
        } else {
            mbinding.txtproductOfYourShopping.visibility = View.INVISIBLE
        }

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


    private fun addProductCustomDialog() {

        dialog = Dialog(this)
        dialog.setContentView(R.layout.buyer_add_more_product_dialog)

        val Units = arrayOf("Select Unit", "unit", "oz", "lbs")

        val spinnerAdapters =
            SpinnerAdapterss(
                this,
                Units
            )
        dialog.edtunit11!!.adapter = spinnerAdapters
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.edtname1.filters = arrayOf<InputFilter>(
            HideEmoji(this), InputFilter.LengthFilter(50)
        )
        if (adapterShoppingModel != null) {
            val model = DialogModel()
            model.name = adapterShoppingModel!!.name
            model.unit = adapterShoppingModel!!.unit
            model.qty = adapterShoppingModel!!.qty
            //  model.image = adapterShoppingModel!!.image

            dialog.edtname1.setText(model.name)
            if (model.unit == "unit") {
                dialog.edtunit11.setSelection(1)
            } else if (model.unit == "oz") {
                dialog.edtunit11.setSelection(2)
            } else if (model.unit == "lbs") {
                dialog.edtunit11.setSelection(3)
            }
            dialog.edtqty.setText(model.qty)

        } else {

            dialog.edtname1.setText("")
            dialog.edtunit11.setSelection(0)
            dialog.edtqty.setText("")
        }

        dialog.btnAdd.setOnClickListener {
            txtname.setFocusable(true)
            txtname.setFocusableInTouchMode(true)

            val id = dialog.edtunit11.selectedItemId.toInt()
            if (adapterShoppingModel != null) {
                val old = adapterShoppingModel!!.name
                val new = dialog.edtname1.text.toString()
                if (old == new) {
                    adapterShoppingModel!!.name = old
                    adapterShoppingModel!!.unit = setUnit(dialog.edtunit11.selectedItem.toString())
                    adapterShoppingModel!!.qty = dialog.edtqty.text.toString()

                    if (adapterShoppingModel!!.name.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Enter_The_Name),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (adapterShoppingModel!!.name.length > 50) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_add_less_than_thirty_char),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (adapterShoppingModel!!.unit == getString(R.string.Select_Unit)) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (adapterShoppingModel!!.qty.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Enter_The_quantity),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        // adapterShoppingModel!!.image = modelDialog.image
                        mvmodel.editListUpdate(
                            adapterShoppingModel!!,
                            adapterShoppingPosition,
                            false,
                            ""
                        )
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()
                    }
                } else {

                    adapterShoppingModel!!.name = new
                    adapterShoppingModel!!.unit = setUnit(dialog.edtunit11.selectedItem.toString())
                    adapterShoppingModel!!.qty = dialog.edtqty.text.toString()
                    // adapterShoppingModel!!.image = modelDialog.image

                    if (adapterShoppingModel!!.name.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Enter_The_Name),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (adapterShoppingModel!!.name.length > 50) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_add_less_than_thirty_char),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (adapterShoppingModel!!.unit == getString(R.string.Select_Unit)) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (adapterShoppingModel!!.qty.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_Enter_The_quantity),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {
                        mvmodel.editListUpdate(
                            adapterShoppingModel!!,
                            adapterShoppingPosition,
                            true,
                            "nameEdited"
                        )
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()
                    }
                }

            } else {
                val model = PostShoppingModel()
                if (dialog.edtname1.text.isEmpty()) {
                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_Name),
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
                } else if (dialog.edtqty.text.isEmpty()) {

                    Toast.makeText(
                        this,
                        getString(R.string.Please_Enter_The_quantity),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    model.name = dialog.edtname1.text.toString()
                    model.unit = setUnit(dialog.edtunit11.selectedItem.toString())
                    model.qty = dialog.edtqty.text.toString()
                    //   model.image = modelDialog.image//g lobal model we want to set the image
                    val array = ArrayList<PostShoppingModel>()

                    if (array.size > 5) {
                        Toast.makeText(
                            this,
                            getString(R.string.You_cannot_add_product),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        array.add(model)
                        mvmodel.ShoppingListData(array, false)
                        Constant.hideSoftKeyboard(it, this)
                        dialog.cancel()
                    }

                    dialog.cancel()
                }

            }

        }

        dialog.setOnDismissListener {
            adapterShoppingModel = null
            adapterShoppingPosition = -1
        }
        dialog.setOnCancelListener {
            txtname.setFocusableInTouchMode(true)
            txtname.setFocusable(true)
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

    override fun edtData(data: PostShoppingModel, position: Int) {
        adapterShoppingModel = data
        adapterShoppingPosition = position

        txtname.setFocusable(false)

        // mBinding.txtproductOfYourShopping.visibility = View.GONE
        addProductCustomDialog()
    }

    private fun creditDeductionLogic() {
        when (buyerLevel) {
            "1" -> freeProducts = 5
            "2" -> freeProducts = 6
            "3" -> freeProducts = 8
            "4" -> freeProducts = 10
            "5" -> freeProducts = 15
        }
        textaddFive.text =
            "${getString(R.string.you_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_each_additional_product)} $freeProducts ${getString(
                R.string.products
            )}"
    }

    private fun getProfile() {
        val json = JSONObject(Constant.getPrefs(this).getString(Constant.PROFILE, ""))
        buyerCredits = json.optString("credits").trim()
        buyerLevel = json.optString("buyer_level").trim()
        creditPerProduct = json.optString("per_product_credits").trim()
    }

}


