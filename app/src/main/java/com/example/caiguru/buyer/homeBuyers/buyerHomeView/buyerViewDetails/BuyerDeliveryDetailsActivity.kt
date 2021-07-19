package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerViewDetails

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
import com.example.caiguru.buyer.buyerAddAddress.AddAddressModel
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews.HomeViewViewModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews.ViewDetailModel
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_address_map_box.*
import kotlinx.android.synthetic.main.activity_buyer_delivery_details.*

class BuyerDeliveryDetailsActivity : AppCompatActivity() {

    private lateinit var data: ViewDetailModel
    private lateinit var viewModel: DetailsViewModel
    private lateinit var adapterView: HomeDeliveryDetailsAdapter
    private lateinit var text: TextView
    private var delivery_type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_delivery_details)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        setUserViewAdapterAdapter()
        if (intent.hasExtra("keyDeliveryDetail")) {
            layoutDetails.visibility = View.VISIBLE
            data = intent.getParcelableExtra<ViewDetailModel>("keyDeliveryDetail")!!
            setAllData(data)
            SettingUpToolbar()
            setClick()
        } else {
            layoutDetails.visibility = View.GONE
            val data = intent.getParcelableExtra<HomeModel>("keyGetFeeds")!!
            val addressData = intent.getParcelableExtra<AddAddressModel>("KeyAddress")!!
            //*******************buyer_feed_view api****************//
            viewModel.getFeedview(data, addressData)
            progressbarDetails.visibility = View.VISIBLE
        }
        setObserver()

    }

    private fun setObserver() {
        viewModel.mSuccessfulViewDetail().observe(this, Observer {
            data = it
            setAllData(data)
            SettingUpToolbar()
            setClick()
            layoutDetails.visibility = View.VISIBLE
            progressbarDetails.visibility = View.GONE
        })

        viewModel.mError().observe(this, Observer {
            Constant.showToast(it, this)
            layoutDetails.visibility = View.GONE
            progressbarDetails.visibility = View.GONE
        })
    }

    private fun setClick() {
        img.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=data.seller_id
            localModel.cat_id=data.cat_id
            localModel.seller_name=data.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)


//            val profileModel = CustomerChildModel()
//            profileModel.seller_id = data.seller_id
//            profileModel.name = data.name
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", profileModel)
//            startActivity(intent)
        }
        txtname.setOnClickListener {
            val localModel = FinalizeModel()
            localModel.seller_id=data.seller_id
            localModel.cat_id=data.cat_id
            localModel.seller_name=data.name
            val intent = Intent(this, SellerStoreActivity::class.java)
            intent.putExtra("keyOpenSellerStore",localModel)
            startActivity(intent)

//            val profileModel = CustomerChildModel()
//            profileModel.seller_id = data.seller_id
//            profileModel.name = data.name
//            val intent = Intent(this, OtherProfileViewActivity::class.java)
//            intent.putExtra("keyChosseSellerProfileKey", profileModel)
//            startActivity(intent)
        }
    }

    private fun setAllData(data: ViewDetailModel) {
        //   txtname.text = "${data.name}${getString(R.string.s)} ${getString(R.string.shop_list)}"
        //set the payment methods
        delivery_type = data.delivery_type
        if (data.payment_methods.isEmpty()) {
            headerPurchasedMethod.visibility = View.GONE
            views1.visibility = View.GONE
            txtPurchasedText.visibility = View.GONE
        } else {
            txtPurchasedText.text =
                Constant.ModifyCaseOpenListSetPaymentMethods(this, data.payment_methods)
        }


        txtname.text = "${data.name}"
        //image
        if (data.image.isEmpty()) {
            Glide.with(this)
                .load(getDrawable(R.drawable.user_placeholder))
                .into(img)
        } else {
            Glide.with(this)
                .load(data.image)
                .into(img)
        }
        //level
        if (data.level.isEmpty()) {
            Glide.with(this)
                .load(getDrawable(R.drawable.user_placeholder))
                .into(imgBatch)
        } else {
            Glide.with(this)
                .load(levelSeller(data.level))
                .into(imgBatch)
        }


        //set delivry type
        if (data.delivery_type == "1") {
            // txtaddress.text = data.pickup_details.address
            headdeliveyDays.text = getString(R.string.Pickup_days)
            txtdeliverytype.text = getString(R.string.Self_Pickup)
            headdeliveytype1.visibility = View.VISIBLE
            cards1.visibility = View.VISIBLE
            txtdeliverytype1.text = "(" + data.distance + " " + "${getString(R.string.aways)})"
            //set days
            //set the adapter
            var datetimeAraay = ArrayList<String>()
            for (i in 0 until data.pickup_details.days.size) {
                val days = updatedDayTime(data.pickup_details.days)
                datetimeAraay = days
            }
            adapterView.Update(datetimeAraay)

        } else {
            headdeliveyDays.text = getString(R.string.Delivery_Days)
            txtdeliverytype.text = getString(R.string.Home_Delivery)
            headdeliveytype1.visibility = View.GONE
            cards1.visibility = View.GONE
            //   txtaddress.text = data.profile_address
            //    addressLayout.visibility = View.VISIBLE
            //set days
            //set the adapter
            var datetimeAraay = ArrayList<String>()
            for (i in 0 until data.delivery_daytime.size) {
                val days = updatedDayTime(data.delivery_daytime)
                datetimeAraay = days
            }
            adapterView.Update(datetimeAraay)
        }


        txtminimumPrice.text = "$" + Constant.roundValue(data.minimum_purchase_amount.toDouble())
        // mbinding.txtname.text ="${dataModel.name}${getString(R.string.s)} ${getString(R.string.shop_list)}"
        txtname.text = "${data.name}"
        txtcomisssion.text = data.comission_per + "%"

    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: List<DaysParentModel>): ArrayList<String> {
        val daysArray = ArrayList<String>()

        for (item in daysArraymodel) {
            var result: String = ""
            for (child in item.value) {

                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                        Constant.getDayString(this, item.day) + " (" + Constant.ConvertAmPmFormat(
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

    private fun levelSeller(level: String): Int {
        val sellerlevel = Constant.SellerLevel(this)
        for (item in sellerlevel) {
            if (item.levelType == level) {
                return item.levelImage
            }
        }
        return 0
    }

    private fun setUserViewAdapterAdapter() {
        val manager = LinearLayoutManager(this)
        adapterView = HomeDeliveryDetailsAdapter(this)
        recyclerViewDetail.layoutManager = manager
        recyclerViewDetail.adapter = adapterView
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        // text.text = getText(R.string.Delivery_Details)

        if (delivery_type == "1") {
            text.text = getText(R.string.Pickup_Details)
        } else {
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
}
