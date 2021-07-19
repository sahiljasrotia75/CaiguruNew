package com.example.caiguru.buyer.buyerLists.buyerDeliveryDetail

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerApproveRejectModel
import com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList.BuyerShopOpenModel
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.homeBuyers.tagUser.TagModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.databinding.ActivityDeliveryDetailBinding
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_delivery_detail.*

class BuyerDeliveryDetailActivity : AppCompatActivity() {
    lateinit var mbinding: ActivityDeliveryDetailBinding
    private lateinit var text: TextView
    private lateinit var deliveryAdapter: DeliveryDetailsAdapter
    val profileModel = TagModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_delivery_detail)



        SettingUpToolbar()

        setAdapter()
        if (intent.hasExtra("keyModify")) {
            val customerModel = intent.getParcelableExtra<CustomerChildModel>("keyModify")
            //  var buyerShopModel = intent.getParcelableExtra<BuyerShopModel>("keyModifyUserDetails")
            try {
//set the level  image
                profileModel.user_id = customerModel!!.buyer_id
                profileModel.name = customerModel.name
                profileModel.type = "1"
                Glide.with(this)
                    .load(levelgetBUyer(customerModel.level).levelImage)
                    .into(mbinding.imgBatch)
                //${getString(R.string.s)} ${getString(R.string.shop_list)}"
                mbinding.txtname.text = "${customerModel.name}"
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

                deliveryAdapter.Update(datetimeAraay)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            val approvedModel =
                intent.getParcelableExtra<BuyerApproveRejectModel>("keyApproveRejected")
            val userProfile =
                intent.getParcelableExtra<BuyerShopOpenModel>("keyApproveRejectedUserProfile")

            if (approvedModel!!.payment_methods.isNotEmpty()) {
                headingPaymentMethods.text = getString(R.string.payment_method) + ":"
                txtPaymentMethods.text = Constant.ModifyCaseOpenListSetPaymentMethods(
                    this,
                    approvedModel.payment_methods
                )
                view2.visibility = View.VISIBLE
                paymentMethodLayout.visibility = View.VISIBLE
            }

            try {
                profileModel.user_id = approvedModel.seller_id
                profileModel.name = approvedModel.seller_name
                profileModel.type = "2"
//set the level  image
                if (approvedModel.type == "1") {
                    Glide.with(this)
                        .load(levelgetBUyer(userProfile!!.seller_level).levelImage)
                        .into(mbinding.imgBatch)
                } else {
                    Glide.with(this)
                        .load(levelgetSeller(userProfile!!.seller_level).levelImage)
                        .into(mbinding.imgBatch)
                }



                mbinding.txtname.text =
                    "${userProfile.seller_name}"
                //set the image
                if (userProfile.seller_image == null) {
                    Glide.with(this)
                        .load(R.drawable.user_placeholder)
                        .into(mbinding.img)
                } else {

                    Glide.with(this)
                        .load(userProfile.seller_image)
                        .into(mbinding.img)
                }
                val gson = Gson()
                val locationModel: ArrayList<DeliveryZoneModel> =
                    gson.fromJson(
                        approvedModel.delivery_location,
                        object : TypeToken<ArrayList<DeliveryZoneModel>>() {}.type
                    )
//set address
                for (item in locationModel) {
                    mbinding.txtaddress.text = item.address
                }
                //get time zone
                val gson1 = Gson()
                val daysParentModel: ArrayList<DaysParentModel> =
                    gson1.fromJson(
                        approvedModel.delivery_daytime,
                        object : TypeToken<ArrayList<DaysParentModel>>() {}.type
                    )
                var datetimeAraay = ArrayList<String>()
                for (i in 0 until daysParentModel.size) {
                    val days = updatedDayTime(daysParentModel)
                    datetimeAraay = days
                }
                deliveryAdapter.Update(datetimeAraay)

                if (approvedModel.delivery_type == "1") {
                    mbinding.txtdeliverytype.text = getString(R.string.Self_Pickup)
                } else {
                    mbinding.txtdeliverytype.text = getString(R.string.Home_Delivery)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        //click on the image
        img.setOnClickListener {
            if (profileModel.type == "1") {
                val intent = Intent(this, OtherProfileViewActivity::class.java)
                intent.putExtra("keyNotQuotedBuyerProfile", profileModel)
                startActivity(intent)
            } else {
                val localModel = FinalizeModel()
                localModel.seller_id=profileModel.user_id
                localModel.cat_id=""
                localModel.seller_name=profileModel.name
                val intent = Intent(this, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                startActivity(intent)
            }

        }
        txtname.setOnClickListener {
            if (profileModel.type == "1") {
                val intent = Intent(this, OtherProfileViewActivity::class.java)
                intent.putExtra("keyNotQuotedBuyerProfile", profileModel)
                startActivity(intent)
            } else {
                val localModel = FinalizeModel()
                localModel.seller_id=profileModel.user_id
                localModel.cat_id=""
                localModel.seller_name=profileModel.name
                val intent = Intent(this, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                startActivity(intent)
            }
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
        //inflate menu
        // toolbar.inflateMenu(R.menu.toolbar_main_menu);
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
        deliveryAdapter = DeliveryDetailsAdapter(this)
        mbinding.recycleer.adapter = deliveryAdapter
    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: List<DaysParentModel>): ArrayList<String> {
        val daysArray = ArrayList<String>()

        for (item in daysArraymodel) {
            var result: String = ""
            for (child in item.value) {
                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                        Constant.getDayString(
                            this,
                            item.day
                        ) + " (" + Constant.ConvertAmPmFormat(
                            this,
                            child.from
                        ) + "-" + Constant.ConvertAmPmFormat(this, child.to) + ")"

                    } else {

                        result + ", " + "(" + Constant.ConvertAmPmFormat(
                            this,
                            child.from
                        ) + "-" + Constant.ConvertAmPmFormat(this, child.to) + ")"
                    }
                }
            }
            daysArray.add(result)

        }

        return daysArray
    }


    private fun levelgetBUyer(level: String): BuyerLevelModel {
        val sellerLevel = Constant.BuyerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
    }

    private fun levelgetSeller(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

}