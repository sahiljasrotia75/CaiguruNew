package com.example.caiguru.commonScreens.earnCreditsConvert

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_twelve_hundred_credit.*
import kotlinx.android.synthetic.main.add_custom_credits_dialog.*

class CreditConvertActivity : AppCompatActivity() {

    private lateinit var viewModel: CreditConvertViewModel
    private lateinit var dialog: Dialog
    private var dataModel = NotificationModel()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twelve_hundred_credit)
        viewModel = ViewModelProviders.of(this)[CreditConvertViewModel::class.java]

        if (intent.hasExtra("KeySourceCreditConvert12")) {


            dataModel = intent.getParcelableExtra<NotificationModel>("KeySourceCreditConvert12")!!

            //notification api
            viewModel.notificationRead(dataModel.notification_id)
        } else if (intent.hasExtra("KeySourceopenScreenByActivity")) {

        } else {

            dataModel.source_id = intent.getStringExtra("source_id")!!
            dataModel.list_type = intent.getStringExtra("list_type")!!

//            val source = intent.getStringExtra("source")
//            val name = intent.getStringExtra("name")
//            val image = intent.getStringExtra("image")
//            val level = intent.getStringExtra("level")
//            val listingname = intent.getStringExtra("listingname")
//            val reputation = intent.getStringExtra("reputation")
//            val created_at = intent.getStringExtra("created_at")

        }


        noThanksClick()
        LetsDoItClick()
        observerConvertCash()
        var model = GetProfileModel()
        val gson = Gson()
        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        model = gson.fromJson(json, GetProfileModel::class.java)
        totalcredit.setText(Constant.roundValue(model.earned_credits.toDouble()) + getString(R.string.credits))
        twelvehundredtext.setText(getString(R.string.you_have_earned) + " " + Constant.roundValue(model.earned_credits.toDouble()) + " " +
                    getString(R.string.credits_and_you_can_exchange) + " " + Constant.roundValue(
                model.earned_credits.toDouble()
            ) + " " +
                    getString(R.string.credits_for_real_money)
        )
    }

    private fun observerConvertCash() {
        viewModel.mSucessfulCashConvert().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            dialog.btnPleaseWaits.visibility = View.GONE
            dialog.btnAddPrice.visibility = View.VISIBLE
            dialog.dismiss()
            finish()
        })

        viewModel.mFailure().observe(this, Observer {
            dialog.btnPleaseWaits.visibility = View.GONE
            dialog.btnAddPrice.visibility = View.VISIBLE
            Toast.makeText(
                this,
                it,
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        })
    }

    private fun LetsDoItClick() {

        btnLetsdoit.setOnClickListener {
            getCredits()
        }

    }

    private fun noThanksClick() {
        //no thanks click
        txtNoThanks.setOnClickListener {
            Constant.hideCreditConvert = true
            finish()
        }
    }

    //...............set up dialog for getting the product list................//
    private fun getCredits() {
        dialog = Dialog(this)
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.add_custom_credits_dialog)
        dialog.show()


        dialog.btnAddPrice.setOnClickListener {

//            var model1 = GetProfileModel()
//            val gson = Gson()
//            val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")

            val model = Constant.getProfileData(this)

            try {
                val totalCredits = dialog.edtPrice.text.toString().trim()
                if (totalCredits.isEmpty()) {
                    Toast.makeText(this, getString(R.string.Enter_Your_credits), Toast.LENGTH_SHORT)
                        .show()
                } else if (totalCredits.toDouble() < model.redeem_credit_limit.toDouble()) {
                    Toast.makeText(this, getString(R.string.Enter_at_least), Toast.LENGTH_SHORT)
                        .show()

                } else if (totalCredits.toDouble() > model.earned_credits.toDouble()) {
                    Toast.makeText(
                        this,
                        getString(R.string.left_credits) + " " + Constant.roundValue(model.earned_credits.toDouble()) + getString(
                            R.string.credits
                        ),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (totalCredits.toDouble() <= model.earned_credits.toDouble()) {
                    viewModel.cashRequest(totalCredits)
                    dialog.btnPleaseWaits.visibility = View.VISIBLE
                    dialog.btnAddPrice.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}
