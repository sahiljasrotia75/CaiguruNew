package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedDeliveryDetails

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.databinding.ActivityCustomerListDeliveryDetailBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_customer_list_delivery_detail.*

class CustomerListDeliveryDetailActivity : AppCompatActivity() {

    private lateinit var customerDetailsAdapter: CustomerListDeliveryDetailsAdapter
    private lateinit var text: TextView
    private lateinit var mbinding: ActivityCustomerListDeliveryDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_customer_list_delivery_detail)
        //get the parcebale data
        setAdapter()
        val customerModel = intent.getParcelableExtra<CustomerChildModel>("model")
        SettingUpToolbar()
        //getting the data of location
        //convert the gson model
        try {
            //set the level  image
            Glide.with(this)
                .load(levelget(customerModel!!.level).levelImage)
                .into(mbinding.imgBatch)

            mbinding.txtname.text =
                "${customerModel.name}${getString(R.string.s)} ${getString(R.string.shop_list)}"
            mbinding.txtname.text =
                "${customerModel.name}"
            //set the image
            if (customerModel.image == null) {
                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.img)
            } else {

                Glide.with(this)
                    .load(customerModel.image)
                    .into(mbinding.img)
            }
            //get time zone
            val gson1 = Gson()
            val daysParentModel: ArrayList<DaysParentModel> =
                gson1.fromJson(
                    customerModel.delivery_daytime,
                    object : TypeToken<ArrayList<DaysParentModel>>() {}.type
                )
            var datetimeAraay = ArrayList<String>()
            for (i in 0 until daysParentModel.size) {
                val days = updatedDayTime(daysParentModel)
                datetimeAraay = days
            }
            customerDetailsAdapter.Update(datetimeAraay)

            //***********time set
            val gson = Gson()
            val locationModel: ArrayList<DeliveryZoneModel> =
                gson.fromJson(
                    customerModel.delivery_location,
                    object : TypeToken<ArrayList<DeliveryZoneModel>>() {}.type
                )
            //set address
            for (item in locationModel) {
                mbinding.txtaddress.text = item.address
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        //set the click to open the profile
        txtname.setOnClickListener {
            val intent = Intent(this, OtherProfileViewActivity::class.java)
            intent.putExtra("keyListUploadedByCustomerProfile", customerModel)
            startActivity(intent)

        }

        relativeImage.setOnClickListener {
            val intent = Intent(this, OtherProfileViewActivity::class.java)
            intent.putExtra("keyListUploadedByCustomerProfile", customerModel)
            startActivity(intent)

        }
    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Delivery_Details)
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


    fun setAdapter() {

        val manager = LinearLayoutManager(this)
        mbinding.recycleer.layoutManager = manager
        customerDetailsAdapter = CustomerListDeliveryDetailsAdapter(this)
        mbinding.recycleer.adapter = customerDetailsAdapter
    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: List<DaysParentModel>): ArrayList<String> {
        val daysArray = ArrayList<String>()

        for (item in daysArraymodel) {
            var result: String = ""
            for (child in item.value) {

                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                       Constant.getDayString(this,item.day ) + " (" + Constant.ConvertAmPmFormat(this,child.from) + "-" + Constant.ConvertAmPmFormat(this,child.to)+ ")"

                    } else {

                        result + ", " + "(" + Constant.ConvertAmPmFormat(this,child.from)+ "-" + Constant.ConvertAmPmFormat(this,child.to) + ")"
                    }
                }
            }
            daysArray.add(result)

        }

        return daysArray
    }


    private fun levelget(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }


}
