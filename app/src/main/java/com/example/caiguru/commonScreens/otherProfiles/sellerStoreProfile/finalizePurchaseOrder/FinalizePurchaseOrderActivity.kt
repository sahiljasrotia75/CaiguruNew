package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.finalizePurchaseOrder

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.orderPlacedSucessfully.StoreOrderPlacedActivity
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.sellerSetting.sellerBuyCredits.SellerBuyCreditsActivity
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_finalize_purchase_order.*
import kotlinx.android.synthetic.main.activity_finalize_purchase_order.txtCashOnDelivery
import kotlinx.android.synthetic.main.show_credits_dialog.*
import org.json.JSONObject

class FinalizePurchaseOrderActivity : AppCompatActivity() {
    lateinit var text: TextView
    private var alladdress: String = ""
    private lateinit var mvmodel: FinalizePurchaseViewModel
    private var deliveryType: String = ""
    private lateinit var payCreditsAdapter: FinalizeTimeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalize_purchase_order)
        mvmodel = ViewModelProvider(this).get(FinalizePurchaseViewModel::class.java)
        SettingUpToolbar()
        setAdapter()
        setClick()
        initData()
        setObserver()
    }

    private fun setObserver() {

        mvmodel.mSucessfulSingleListData().observe(this, Observer {
            val sellerData = intent.getParcelableExtra<FinalizeModel>("keySellerId")!!

            btnWait.visibility = View.INVISIBLE
            btnPayCredits.visibility = View.VISIBLE
            //   Constant.showToast(it, this)
            val intent = Intent(this, StoreOrderPlacedActivity::class.java)
            intent.putExtra("keyUserName", sellerData.seller_name)
            startActivityForResult(intent, 1001)
        })
        mvmodel.mFailureSingleListData().observe(this, Observer {
            Constant.showToast(it, this)
            btnWait.visibility = View.INVISIBLE
            btnPayCredits.visibility = View.VISIBLE
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }
    }

    //.........setuptool bar..............//
    @SuppressLint("SetTextI18n")
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getString(R.string.finalize_purchase_order)
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

    private fun setClick() {
        val productArrayJson = intent.getStringExtra("KeyProductJsonFormat")!!
        val PartialComissionCredits = intent.getStringExtra("keyPartialComissionCredits")!!
        //  val CashOnDelivery = intent.getStringExtra("KeyCashOnDelivery")
        val sellerData = intent.getParcelableExtra<FinalizeModel>("keySellerId")!!
        val profileModel = intent.getParcelableExtra<GetProfileModel>("KeyProfileInfoData")!!

        btnPayCredits.setOnClickListener {
            if (Constant.getProfileData(this).credits.toDouble() < PartialComissionCredits.toDouble()) {

                creditDialog()

            } else {
                mvmodel.buyList(
                    sellerData.seller_id,
                    productArrayJson,
                    PartialComissionCredits,
                    alladdress, profileModel.listInfo.payment_methods
                )

                btnWait.visibility = View.VISIBLE
                btnPayCredits.visibility = View.INVISIBLE
            }

        }
    }

    private fun creditDialog() {
        val PartialComissionCredits = intent.getStringExtra("keyPartialComissionCredits")!!

        val newCredits =
            PartialComissionCredits.toDouble() - Constant.getProfileData(this).credits.toDouble()
        val dialog = Dialog(this)
        //    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.show_credits_dialog)
        dialog.show()

        dialog.totalCredit.text =
            "${getString(R.string.you_dont_have_credits)}  ${Constant.roundValue((newCredits.toDouble()))}  ${
                getString(
                    R.string.additional_credits
                )
            }"
        //set the click on  button
        dialog.buyCredit.setOnClickListener {
            val intent = Intent(this, SellerBuyCreditsActivity::class.java)
            intent.putExtra("profileKey", Constant.getProfileData(this))
            startActivity(intent)
            dialog.dismiss()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initData() {
        val profileModel = intent.getParcelableExtra<GetProfileModel>("KeyProfileInfoData")!!
        // val sellerId = intent.getParcelableExtra<FinalizeModel>("keySellerId")
        //val productArray = intent.getStringExtra("KeyProductJsonFormat")
        val PartialComissionCredits = intent.getStringExtra("keyPartialComissionCredits")
        val CashOnDelivery = intent.getStringExtra("KeyCashOnDelivery")

        txtPaymentMethod.text =
            Constant.ModifyCaseOpenListSetPaymentMethods(
                this,
                profileModel.listInfo.payment_methods
            )

        btnPayCredits.text =
            getString(R.string.Pay) + " " + PartialComissionCredits + " " + getString(
                R.string.credits
            )

        belowHeadingText.text =
            getString(R.string.You_need_to_pay) + " " + PartialComissionCredits + " " + getString(
                R.string.credits_to_finish_this_order
            )
        // mBinding.txtPrice.text = "$" + Constant.roundValue(arrayModel.total.toDouble())
        txtPrice.text = "$" + CashOnDelivery

        // try {
        val datetimeAraay = ArrayList<String>()
        if (profileModel.listInfo.delivery_type == "1") {
            headdeliveyDays.text = getString(R.string.Pickup_days)
            headdeliveyAddress.text = getString(R.string.Pickup_Distance)
            txtCashOnDelivery.text = getString(R.string.Payment_amount_on_pickup)
            // deliveryType = "Self Pickup"
            deliveryType = getString(R.string.Self_Pickup)
            txtdeliverytype.text = getString(R.string.Pickup_at_seller_address)
            //show only self pickup time
            if (profileModel.listInfo.pickup_details.address.isNotEmpty()) {
                datetimeAraay.addAll(updatedDayTime(profileModel.listInfo.pickup_details.days))
                payCreditsAdapter.Update(datetimeAraay)
                //set the address
            }

            txtdeliveryAddress.text =
                "(" + profileModel.listInfo.distance + "${getString(R.string.aways)})"

            val jsonobj = JSONObject()
            jsonobj.put("lat", profileModel.listInfo.pickup_details.lat)
            jsonobj.put("long", profileModel.listInfo.pickup_details.long)
            jsonobj.put("full_address", profileModel.listInfo.pickup_details.address)

            alladdress = jsonobj.toString()
        } else {
            headdeliveyDays.text = getString(R.string.Delivery_Days)
            headdeliveyAddress.text = getString(R.string.Delivery_Address)
            txtCashOnDelivery.text = getString(R.string.Cash_on_Delivery)
            deliveryType = getString(R.string.Home_Delivery)
            txtdeliverytype.text = deliveryType
            //show only delivery details time
            datetimeAraay.addAll(updatedDayTime(profileModel.listInfo.delivery_daytime))
            payCreditsAdapter.Update(datetimeAraay)
            //set the address
            val modelData = intent.getParcelableExtra<FinalizeModel>("keySellerId")!!
            if (modelData.lat.isNotEmpty()&& modelData.address.isNotEmpty()) {
                val jsonobj = JSONObject()
                jsonobj.put("lat", modelData.lat)
                jsonobj.put("long", modelData.long)
                jsonobj.put("full_address",modelData.address)
                alladdress = jsonobj.toString()
                txtdeliveryAddress.text = modelData.address
            } else {
                val jsonobj = JSONObject()
                jsonobj.put("lat", Constant.getProfileData(this).lat)
                jsonobj.put("long", Constant.getProfileData(this).long)
                jsonobj.put("full_address", Constant.getProfileData(this).full_address)
                alladdress = jsonobj.toString()
                txtdeliveryAddress.text = Constant.getProfileData(this).full_address
            }


        }
        // listingName.text = infoModel.listingname
        sellerName.text = profileModel.name
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: List<DaysParentModel>): ArrayList<String> {

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
        recycleerTime.layoutManager = manager
        payCreditsAdapter = FinalizeTimeAdapter(this)
        recycleerTime.adapter = payCreditsAdapter
    }


}