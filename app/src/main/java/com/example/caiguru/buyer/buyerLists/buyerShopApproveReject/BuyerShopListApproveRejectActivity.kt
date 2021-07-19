package com.example.caiguru.buyer.buyerLists.buyerShopApproveReject

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerLists.buyerDeliveryDetail.BuyerDeliveryDetailActivity
import com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList.BuyerShopOpenModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.databinding.ActivityShoplistApproveRejectBinding
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import java.lang.Exception

class BuyerShopListApproveRejectActivity : AppCompatActivity() {

    private var dataModel = NotificationModel()
    private var buyerShopModel = BuyerShopOpenModel()
    private lateinit var id: String
    private var allData = BuyerApproveRejectModel()
    lateinit var mbinding: ActivityShoplistApproveRejectBinding
    lateinit var mvmodel: BuyerShopListApproveRejectViewModel
    lateinit var text: TextView
    private lateinit var buyerShopListApproveRejectAdapter: BuyerShopListApproveRejectAdapter
    private var personName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_shoplist_approve_reject)
        //   mvmodel = ViewModelProviders.of(this)[BuyerShopListApproveRejectViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerShopListApproveRejectViewModel::class.java)
        mbinding.listName.visibility = View.GONE
        if (intent.hasExtra("keySourceSubmitQuoteBySeller1")) {
            dataModel =
                intent.getParcelableExtra<NotificationModel>("keySourceSubmitQuoteBySeller1")!!
            personName = dataModel.name
            id = dataModel.source_id
            buyerShopModel.seller_level = dataModel.level
            buyerShopModel.seller_name = dataModel.name
            buyerShopModel.seller_image = dataModel.image
            //notification read api
            mvmodel.notificationRead(dataModel.notification_id)

//            if (dataModel.action_taken == "1") {
//                mbinding.btnLayout.visibility = View.VISIBLE
//            } else {
//                mbinding.btnLayout.visibility = View.GONE
//            }


        } else if (intent.hasExtra("keymodel")) {
            buyerShopModel = intent.getParcelableExtra<BuyerShopOpenModel>("keymodel")!!
            personName = buyerShopModel.seller_name
            id = buyerShopModel.id
            // mbinding.btnLayout.visibility = View.VISIBLE
        } else {
//            val name = intent.getStringExtra("name")
//            val image = intent.getStringExtra("image")
//            val level = intent.getStringExtra("level")
//            val listingname = intent.getStringExtra("listingname")
//            val reputation = intent.getStringExtra("reputation")
//            val created_at = intent.getStringExtra("created_at")
            id = intent.getStringExtra("source_id")!!
            //  val list_type = intent.getStringExtra("list_type")
            // val source = intent.getStringExtra("source")
            personName = intent.getStringExtra("name")!!
            buyerShopModel.seller_name = personName
            buyerShopModel.seller_image = intent.getStringExtra("image")!!
            buyerShopModel.seller_level = intent.getStringExtra("level")!!
//            val listingname = intent.getStringExtra("listingname")
//            val reputation = intent.getStringExtra("reputation")
//            val created_at = intent.getStringExtra("created_at")

            //  mbinding.btnLayout.visibility = View.VISIBLE
        }
        SettingUpToolbar()
        setAdapters()
        spannableTextHeader(buyerShopModel.seller_name)
        //***************************get buyer request Api
        mvmodel.get_buyer_request_detail(id)
        mbinding.progress.visibility = View.VISIBLE

        //set the text
        val udata = getString(R.string.Delivery_Details)
        val text = SpannableString(udata)
        text.setSpan(UnderlineSpan(), 0, udata.length, 0)
        mbinding.deliveryDetailed.text = text
        // holder.itemView.deliveryDetails.text = text


        mvmodel.mSucessfulGetbuyerList().observe(this, Observer {
            mbinding.progress.visibility = View.GONE
            allData = it
            if (it != null) {
                mbinding.parentLayouts.visibility = View.VISIBLE
                try {
                    mbinding.cashonDelivery.setText("$" + Constant.roundValue(it.amount.toDouble() - it.credits.toDouble()))
                    mbinding.commission.setText(Constant.roundValue(it.credits.toDouble()) + getString(
                            R.string.credits
                        ))

                    mbinding.price.setText("$" + Constant.roundValue(it.amount.toDouble()))//set the total price
                    mbinding.listName.setText(it.listingname)

                    spannableTxtCash(Constant.roundValue(it.amount.toDouble() - it.credits.toDouble()))

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //  mbinding.txtListname.setText(it.cat_id)
                mbinding.txtListname.text = categoryData((it.cat_id))

                //   mbinding.txtNoData.visibility = View.VISIBLE
                val productdetail = it.products

                val gson = Gson()
                val model1: ArrayList<PostShoppingModel> =
                    gson.fromJson(
                        productdetail,
                        object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
                    )
                buyerShopListApproveRejectAdapter.update(model1)

            } else {
                mbinding.txtNoData.visibility = View.VISIBLE

            }

            btnHideShow()//hide the btn
        })
        mbinding.deliveryDetailed.setOnClickListener {
            val intent = Intent(this, BuyerDeliveryDetailActivity::class.java)
            intent.putExtra("keyApproveRejected", allData)
            intent.putExtra("keyApproveRejectedUserProfile", buyerShopModel)
            startActivity(intent)
        }

        //failure case
        mvmodel.mFailureGetbuyerList().observe(this, Observer {
            mbinding.txtNoData.visibility = View.VISIBLE
            mbinding.progress.visibility = View.GONE
            //  Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message, this)
        })


        //*****************api of change_request_status***************//
        //set the click on the button request approval
        mbinding.btnapprove.setOnClickListener {
            mvmodel.change_buyer_request_status(id)
        }

        //****************set the observer of approval
        mvmodel.mSucessfulRequestApprovals().observe(this, Observer {
            alertDialog(it)
        })
        //failure
        mvmodel.mFailureRequestApprovals().observe(this, Observer {
            alertDialog1(it)
        })
        //*****************api of change_request_status***************//
        //set the click on the rejected list
        mbinding.btnreject.setOnClickListener {
            mvmodel.change_buyer_request_reject_status(id)
        }

        //*********set the obaserver of rejected list
        mvmodel.mSucessfulRejectedRequest().observe(this, Observer {
            alertDialog(it)


        })
        mvmodel.mFailureRejectedRequest().observe(this, Observer {
            alertDialog1(it)


        })

    }


    private fun spannableTxtCash(roundValue: String) {

        val startTxt = getString(R.string.you_must_charge)
        val middleTxt = "$" + roundValue
        val endTxt = getString(R.string.to_your_client)
        val allText = startTxt + " " + middleTxt + " " + endTxt
        val spannable = SpannableString(allText)
        val boldSpan = StyleSpan(Typeface.BOLD)


        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            0,
            startTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            middleTxt.length,
            endTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            startTxt.length,
            allText.length - endTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            boldSpan,
            startTxt.length,
            allText.length - endTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mbinding.txtYouMustChargeTxt.setText(spannable, TextView.BufferType.SPANNABLE)

        mbinding.cashonDelivertext.setOnClickListener {
            Constant.cashOnDeliveryDialog(this)
        }
    }

    private fun spannableTextHeader(name: String) {
        val textStart = name + " "
        val textEnd = getString(R.string.buyer_approve_header_txt)

        val allText = textStart + " " + textEnd
        val spannable = SpannableString(allText)
        val boldSpan = StyleSpan(Typeface.BOLD)

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.purple)),
            0,
            textStart.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
            textStart.length,
            textEnd.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )



        spannable.setSpan(
            boldSpan,
            0,
            textStart.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mbinding.txtHeader2.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    private fun btnHideShow() {
        if (intent.hasExtra("keySourceSubmitQuoteBySeller1")) {
            if (dataModel.action_taken == "1") {
                mbinding.btnLayout.visibility = View.VISIBLE
            } else {
                mbinding.btnLayout.visibility = View.GONE
            }

        } else if (intent.hasExtra("keymodel")) {
            mbinding.btnLayout.visibility = View.VISIBLE

        } else {
            mbinding.btnLayout.visibility = View.VISIBLE
        }
    }


    //Alert dialog
    private fun alertDialog(it: String) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setCancelable(false)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            setResult(75, intent)
            finish()
        }
        mDialog.show()

    }  //Alert dialog

    private fun alertDialog1(it: String) {
        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(it)
        mDialog.setCancelable(false)
        mDialog.setPositiveButton(
            getString(R.string.ok)
        ) { dialog, which ->
            dialog.dismiss()

        }
        mDialog.show()

    }


    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        //     text.text = listName + "'s" + " " + getString(R.string.Quote)
        text.text = personName
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        //inflate menu
    }

    fun setAdapters() {
        val manager = LinearLayoutManager(this)
        mbinding.rvAppRejShopList.layoutManager = manager
        buyerShopListApproveRejectAdapter = BuyerShopListApproveRejectAdapter(this)
        mbinding.rvAppRejShopList.adapter = buyerShopListApproveRejectAdapter
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

    //*************** Matching id's *******************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(this)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return getString(R.string.orders)
    }


}
