package com.example.caiguru.buyer.chooseSellers.chooseSellerDeliveryDetails

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.databinding.ActivityChooseSellerDeliveryDetailsBinding
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_choose_seller_delivery_details.*
import java.lang.Exception

class ChooseSellerDeliveryDetailsActivity : AppCompatActivity() {
    private var dataModel = ChooseSellerShoppingModel()
    private var delivery_type: Int = 0
    private var deliveryType: String = ""
    private lateinit var chooseSellerAdapter: ChooseSellerDeliveryDetailsAdapter
    private lateinit var mbinding: ActivityChooseSellerDeliveryDetailsBinding
    private lateinit var text: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_choose_seller_delivery_details)
        setAdapter()
        try {
            dataModel = intent.getParcelableExtra<ChooseSellerShoppingModel>("keydetails")!!
            // level image code
            //set the level  image
            Glide.with(this)
                .load(levelget(dataModel.level).levelImage)
                .into(mbinding.imgBatch)
            //set the image
            if (dataModel.image == null || dataModel.image.isEmpty()) {

                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.img)
            } else {
                Glide.with(this)
                    .load(dataModel.image)
                    .into(mbinding.img)
            }

            //set the payment methods
            txtPurchasedText.text=Constant.ModifyCaseOpenListSetPaymentMethods(this,dataModel.payment_methods)
            //getting type
            val datetimeAraay = ArrayList<String>()
            delivery_type = dataModel.delivery_type
            if (delivery_type == 1) {
                deliveryType = getString(R.string.Self_Pickup)
                mbinding.txtdeliverytype.text = getString(R.string.Pickup_at_seller_address)
                mbinding.headdeliveyDays.text = getString(R.string.Pickup_days)
                mbinding.headdeliveytype1.text = getString(R.string.Pickup_Distance)
                if (dataModel.distance == "") {
                    mbinding.txtdeliverytype1.text = "(0.0 " +" "+"${getString(R.string.aways)})"
                } else {
                    mbinding.txtdeliverytype1.text =
                       "("+ dataModel.distance +" "+"${getString(R.string.aways)})"
                }
                //show only self pickup time
                dataModel.pickup_details.days
                datetimeAraay.addAll(updatedDayTime2(dataModel.pickup_details.days))
                chooseSellerAdapter.Update(datetimeAraay)

            } else {

                deliveryType = getString(R.string.Home_Delivery)
                mbinding.txtdeliverytype.text = deliveryType
                mbinding.headdeliveyDays.text = getString(R.string.Delivery_Days)
                //show only delivery details time
                //set the adapter
                mbinding.headdeliveytype1.visibility = View.GONE
                mbinding.cards1.visibility = View.GONE
                dataModel.delivery_daytime
                datetimeAraay.addAll(updatedDayTime2(dataModel.delivery_daytime))
                chooseSellerAdapter.Update(datetimeAraay)

            }


            SettingUpToolbar()
            mbinding.txtminimumPrice.text = "$" + Constant.roundValue(dataModel.minimum_purchase_amount.toDouble())
            // mbinding.txtname.text ="${dataModel.name}${getString(R.string.s)} ${getString(R.string.shop_list)}"
            mbinding.txtname.text = "${dataModel.name}"
            mbinding.txtcomisssion.text = dataModel.comission_per + "%"

        } catch (e: Exception) {
            e.printStackTrace()
        }

        //set the click on the other profile
        relativeImage.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=dataModel.seller_id
            localModel.cat_id=dataModel.cat_id
            localModel.seller_name=dataModel.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)



//            val model = CustomerChildModel()
//            model.seller_id = dataModel.seller_id
//            model.name = dataModel.name
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", model)
//            startActivity(intent)
        }

        txtname.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=dataModel.seller_id
            localModel.cat_id=dataModel.cat_id
            localModel.seller_name=dataModel.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)

//            val model = CustomerChildModel()
//            model.seller_id = dataModel.seller_id
//            model.name = dataModel.name
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", model)
//            startActivity(intent)

        }
    }

    //get the level of seller
    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        if (delivery_type == 1) {
            deliveryType = getString(R.string.Self_Pickup)
            text.text = getText(R.string.Pickup_Details)
        } else {
            deliveryType = getString(R.string.Home_Delivery)
            text.text = getText(R.string.Delivery_Details)
        }

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

    // ......update day and time format..............//
//    private fun updatedDayTime(daysArraymodel: List<DaysParentModel>): String {
//        var result: String = ""
//
//        for (item in daysArraymodel) {
//
//            for (child in item.value) {
//
//                if (child.from.contains(":")) {
//                    result = if (result.isEmpty()) {
//                        item.day + " (" + child.from + "-" + child.to + ")"
//
//                    } else {
//
//                        result + ", " + item.day + "(" + child.from + "-" + child.to + ")"
//                    }
//                }
//            }
//
//        }
//
//        return result
//    }
    // ......update day and time format..............//
    private fun updatedDayTime2(daysArraymodel: List<DaysParentModel>): ArrayList<String> {

        val daysArray = ArrayList<String>()
        for (i in 0 until daysArraymodel.size) {

            val day = Constant.getDayString(this, daysArraymodel[i].day)
            var value = ""
            for (j in 0 until daysArraymodel[i].value.size) {
                if (value.isEmpty()) {
                    value = Constant.ConvertAmPmFormat(
                        this,
                        daysArraymodel[i].value[j].from
                    ) + "-" + Constant.ConvertAmPmFormat(this, daysArraymodel[i].value[j].to)
                } else {
                    value =
                        value + "," + Constant.ConvertAmPmFormat(
                            this,
                            daysArraymodel[i].value[j].from
                        ) + "-" + Constant.ConvertAmPmFormat(this, daysArraymodel[i].value[j].to)
                }
            }
            daysArray.add(day + " (" + value + ")")
        }

        return daysArray
    }


    fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbinding.recycleer.layoutManager = manager
        chooseSellerAdapter = ChooseSellerDeliveryDetailsAdapter(this)
        mbinding.recycleer.adapter = chooseSellerAdapter
    }

}
