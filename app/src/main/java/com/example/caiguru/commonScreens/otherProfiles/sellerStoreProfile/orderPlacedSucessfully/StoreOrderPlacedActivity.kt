package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.orderPlacedSucessfully

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.caiguru.R
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardViewModel
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_store_order_placed.*
import kotlinx.android.synthetic.main.seller_order_cancelled_adapter.view.*
import java.lang.Exception


class StoreOrderPlacedActivity : AppCompatActivity() {
    private lateinit var viewModelDashBoard: DashBoardViewModel
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_order_placed)
        viewModelDashBoard = ViewModelProvider(this).get(DashBoardViewModel::class.java)

        setClick()
        settingUpToolbar()
        initDataa()
        Constant.apiHitOrNot = 0
        setObserver()
    }

    private fun setObserver() {
        viewModelDashBoard.getdataProfile().observe(this, Observer {
            setCreditText()
            progressPlacedOrder.visibility= View.GONE
            AllLayout.visibility= View.VISIBLE
        })

    }

    private fun initDataa() {
        val shredpref = getSharedPreferences("yogeshData", MODE_PRIVATE)
        val tokenFirebase = shredpref.getString("deviceId", "")!!
        viewModelDashBoard.getProfile(tokenFirebase)//get profile Api
        progressPlacedOrder.visibility= View.VISIBLE
        AllLayout.visibility= View.GONE
    }

    private fun setCreditText() {
        try {

            txtPlaceOrderHeader.text =
                getString(R.string.you_have) + " " + Constant.getProfileData(this).credits.toDouble().toInt()
                    .toString() + getString(R.string.cr) + " " + getString(
                    R.string.facebook_suggest_text
                )
            val name = intent.getStringExtra("keyUserName")

            val startName = getString(R.string.inform_text2)
            val middleName = name + "."

            val allString = startName + " " + middleName
            val spannableText = SpannableString(allString)
            val boldSpan = StyleSpan(Typeface.BOLD)

            spannableText.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.hard_grey)),
                0,
                startName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableText.setSpan(
                ForegroundColorSpan(resources.getColor(R.color.purple)),
                startName.length,
                middleName.length + startName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannableText.setSpan(
                boldSpan,
                startName.length,
                middleName.length + startName.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            txtOrderDescript.setText(spannableText, TextView.BufferType.SPANNABLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setClick() {
        btnShareOnFb.setOnClickListener {

            val shareHashTag =
                ShareHashtag.Builder().setHashtag(getString(R.string.facebook_hashtag)).build()
            val shareLinkContent = ShareLinkContent.Builder()
                .setShareHashtag(shareHashTag)
                .setQuote(
                    getString(R.string.fb_share_txt1) + " " + Constant.getProfileData(this).reff_code + " " + getString(
                        R.string.fb_txt_share2
                    )
                )
                //.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.app.caiguru"))
                .setContentUrl(Uri.parse("https://caiguru.com/descargar.html"))
                .build()
            ShareDialog.show(this, shareLinkContent)

        }

    }

    //.........setuptool bar..............//
    private fun settingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.order_successfully_placed)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_cross_facebook)
    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)//set the result
        finish()
        super.onBackPressed()

    }
}