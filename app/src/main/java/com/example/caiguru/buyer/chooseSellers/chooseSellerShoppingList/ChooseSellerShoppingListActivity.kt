package com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.text.TextUtils
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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForAllList.PayCreditsMultipleListActivity
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsSingleListActivity
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.ActivityChooseSellerShoppingListBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.refute_reason_dialog.*
import kotlinx.android.synthetic.main.switch_dialog.*
import kotlin.collections.ArrayList
import org.json.JSONArray
import org.json.JSONObject

class ChooseSellerShoppingListActivity : AppCompatActivity(),
    ChooseSellerShoppingListParentAdapter.reportListInterface {

    private lateinit var dialog: Dialog
    private lateinit var dialogReport: Dialog
    private var homeDeliveryaddress = DeliveryZoneModel()
    private lateinit var searchtext: String
    private var self_pickup: Int = 0
    private var home_delivery: Int = 0
    private var GlobalArrayModel = ArrayList<ChooseSellerShoppingModel>()
    private lateinit var mvmodel: ChooseSellerShoppingListViewModel
    private lateinit var chooseSellerParentAdapter: ChooseSellerShoppingListParentAdapter
    private var username: String = ""
    private lateinit var text: TextView
    private lateinit var mbinding: ActivityChooseSellerShoppingListBinding
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var fusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_choose_seller_shopping_list)
        mvmodel = ViewModelProvider(this).get(ChooseSellerShoppingListViewModel::class.java)

        //*******************seller_all_listing***********************//
        if (intent.hasExtra("keymodel")) {
            searchtext = intent.getStringExtra("searchtext")!!
            homeDeliveryaddress =
                intent.getParcelableExtra<DeliveryZoneModel>("homeDeliveryaddress")!!
            lat = homeDeliveryaddress.lat.toDouble()
            long = homeDeliveryaddress.long.toDouble()
            val data = intent.getParcelableExtra<CustomerChildModel>("keymodel")!!
            username = data.name
            if (data.delivery_type == "1") {
                self_pickup = 1
                home_delivery = 0
                mvmodel.setShoppingList(
                    data.seller_id,
                    lat,
                    long,
                    searchtext,
                    data.cat_id,
                    self_pickup,
                    home_delivery
                )
            } else {
                self_pickup = 0
                home_delivery = 2
                mvmodel.setShoppingList(
                    data.seller_id,
                    lat,
                    long,
                    searchtext,
                    data.cat_id,
                    self_pickup,
                    home_delivery
                )
            }
        } else {
            val data = intent.getParcelableExtra<CustomerChildModel>("keymodelData")!!
            searchtext = intent.getStringExtra("searchKeymodelData")!!
            homeDeliveryaddress = intent.getParcelableExtra<DeliveryZoneModel>("addressHomeAddres")!!
            lat = homeDeliveryaddress.lat.toDouble()
            long = homeDeliveryaddress.long.toDouble()
            username = data.name
            if (data.delivery_type == "1") {
                self_pickup = 1
                home_delivery = 0
                mvmodel.setShoppingList(
                    data.seller_id,
                    lat,
                    long,
                    searchtext,
                    data.cat_id,
                    self_pickup,
                    home_delivery
                )
            } else {
                self_pickup = 0
                home_delivery = 2
                mvmodel.setShoppingList(
                    data.seller_id,
                    lat,
                    long,
                    searchtext,
                    data.cat_id,
                    self_pickup,
                    home_delivery
                )
            }
        }
        SettingUpToolbar()
        setAdapters()
        openLocation()

        //****************************set the observer of seller_all_listing********************//
        //sucessful
        mvmodel.mSucessfulSellerShoppingList().observe(this, Observer {
            mbinding.progressPagination.visibility = View.GONE
            GlobalArrayModel = it
            if (it.size > 1) {
                mbinding.PurchaseAll.visibility = View.VISIBLE
            } else {
                mbinding.PurchaseAll.visibility = View.GONE
            }
            if (it.isEmpty()) {
                mbinding.PurchaseAll.visibility = View.GONE
                mbinding.txtNoData.visibility = View.VISIBLE
            } else {
                chooseSellerParentAdapter.update(it, homeDeliveryaddress)
            }
        })
        //failure
        mvmodel.mFailureChosseSeller().observe(this, Observer {
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            mbinding.progressPagination.visibility = View.GONE
            mbinding.txtNoData.visibility = View.VISIBLE
            mbinding.PurchaseAll.visibility = View.GONE


        })

        //set the click on button for purchase all
        mbinding.PurchaseAll.setOnClickListener {
            val selectedLists = getAllSelectedListData()
            if (selectedLists.size > 0) {
                dialog = Dialog(this)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.switch_dialog)
                //default textShow
                dialog.txtHeader.text=getString(R.string.alert)
                dialog.txtMessage.text=getString(R.string.purchase_shopping_alert)
                dialog.yes.text=getString(R.string.buy)
                dialog.no.text=getString(R.string.cancel)
                dialog.show()
                dialog.yes.setOnClickListener {
                    val allDataJsonFormat = getDataTotalCredits(selectedLists)//purchase data
                    val intent = Intent(this, PayCreditsMultipleListActivity::class.java)
                    // intent.putExtra("keyshoppingData", chooseSellerParentAdapter.getUpdatesArray())
                    intent.putExtra("keyshoppingData", selectedLists)
                    intent.putExtra("totalCredits", allDataJsonFormat)
                    intent.putExtra("homeDeliveryaddress", homeDeliveryaddress)
                    startActivity(intent)
                    dialog.dismiss()
                    finish()
                }
                //no click
                dialog.no.setOnClickListener {
                    dialog.dismiss()
                }

            }
        }
        //*************************report list reponse
        //sucessfull
        mvmodel.mSucessfulREportList().observe(this, Observer {
           // Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            dialogReport.waitBtn.visibility = View.GONE
            dialogReport.SubmitBtn.visibility = View.VISIBLE
            dialogReport.dismiss()
        })
        //failure
        mvmodel.mFailureReposrtlist().observe(this, Observer {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            dialogReport.waitBtn.visibility = View.GONE
            dialogReport.SubmitBtn.visibility = View.VISIBLE
            dialogReport.dismiss()
        })
    }

    private fun getAllSelectedListData(): ArrayList<ChooseSellerShoppingModel> {
        var hasQuantity = false
        var hasMinimumPurchase = false
        val data = ArrayList<ChooseSellerShoppingModel>()
        parent@ for (model in chooseSellerParentAdapter.getUpdatesArray()) {
            for (child in model.product_details) {
                if (child.qty.toInt() > 0) {
                    hasQuantity = true
                    if (model.total.toDouble() >= model.minimum_purchase_amount.toDouble()) {
                        hasMinimumPurchase=true
                        data.add(model)
                        break
                    }else{
                        data.add(model)
                    }
//                    else {
//                        Constant.showToast( getString(R.string.minimum_mismatch),this)
//                        break
//                    }
                }
            }
        }

        if (!hasQuantity) {
            Constant.showToast( getString(R.string.no_list_selected),this)
        }
        if (hasQuantity){
            if (!hasMinimumPurchase) {
                data.clear()
                Constant.showToast( getString(R.string.Total_should_be_high_than_Minimum_order_amount),this)
            }
        }


        return data
    }

    //multiple list purchase
    private fun getDataTotalCredits(mData: ArrayList<ChooseSellerShoppingModel>): String {
        val parentArray = JSONArray()


        for (i in 0 until mData.size) {
            val jsonObj = JSONObject()
            val productArray = JSONArray()
            for (j in 0 until mData[i].product_details.size) {
                val jsonObj = JSONObject()
                jsonObj.put("id", mData[i].product_details[j].id)
                jsonObj.put("qty", mData[i].product_details[j].qty)
                jsonObj.put("price", mData[i].product_details[j].price)
                jsonObj.put("priceWithComission", mData[i].product_details[j].priceWithComission)
                jsonObj.put("name", mData[i].product_details[j].name)
                jsonObj.put("image", mData[i].product_details[j].image)
                jsonObj.put("unit", mData[i].product_details[j].unit)
                productArray.put(jsonObj)
            }
            jsonObj.put("products", productArray)
            jsonObj.put("list_id", mData[i].id)
            jsonObj.put("delivery_type", mData[i].delivery_type)
            jsonObj.put("amount", mData[i].total)
//            jsonObj.put(
//                "credits",
//                ((mData[i].total.toDouble() * mData[i].comission_per.toDouble()) / 100).toString()
//            )
            jsonObj.put("credits", mData[i].PartialComission)

            parentArray.put(jsonObj)
        }

        return parentArray.toString()
    }

    private fun openLocation() {
        try {
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
                    ActivityCompat.requestPermissions(
                        this, arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), 101
                    )
                    Handler().postDelayed({}, 1000)
                    dialog.cancel()
                }
                mDialog.show()
            } else {
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
                    if (location != null) {
                        lat = location.latitude
                        long = location.longitude
                    }
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setAdapters() {
        val manager = LinearLayoutManager(this)
        mbinding.recyclerList.layoutManager = manager
        chooseSellerParentAdapter = ChooseSellerShoppingListParentAdapter(this)
        mbinding.recyclerList.adapter = chooseSellerParentAdapter
    }

    override fun onResume() {
        super.onResume()
        val isFinish = Constant.getPrefs(this).getString("finish", "no")
        if (isFinish == "yes") {
            finish()
            Constant.getPrefs(this).edit().putString("finish", "no").apply()
        }
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        // text.text = username + " " + getText(R.string.Shopping_list)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        text.text = username
        text.setSelected(true)
        text.setEllipsize(TextUtils.TruncateAt.MARQUEE)
        text.setSingleLine(true)
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

    //*************set the click on report list
    override fun reportList(data: ChooseSellerShoppingModel) {
        dialogReport = Dialog(this)
        dialogReport.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogReport.setContentView(R.layout.refute_reason_dialog)
        dialogReport.edtReason.hint = getString(R.string.enter_reason)
        dialogReport.show()
        dialogReport.edtReason.filters = arrayOf<InputFilter>(
            HideEmoji(this)
        )

        dialogReport.SubmitBtn.setOnClickListener {
            if (dialogReport.edtReason.text.isEmpty()) {
                Constant.showToast( getString(R.string.enter_reason),this)

            } else {
                mvmodel.reportList(dialogReport.edtReason.text.toString().trim(), data)
                dialogReport.waitBtn.visibility = View.VISIBLE
                dialogReport.SubmitBtn.visibility = View.GONE
            }

        }
    }
}
