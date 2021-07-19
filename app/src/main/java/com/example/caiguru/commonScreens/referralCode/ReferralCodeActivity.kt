package com.example.caiguru.commonScreens.referralCode

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.databinding.ActivityReferralCodeBinding
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_referral_code.*
import kotlinx.android.synthetic.main.tutorils_dialog.*

class ReferralCodeActivity : AppCompatActivity() {

    private lateinit var dialog: Dialog
    private lateinit var mbinding: ActivityReferralCodeBinding
    private lateinit var mvmodel: ReferralViewModel
    lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_referral_code)
       // mvmodel = ViewModelProviders.of(this)[ReferralViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(ReferralViewModel::class.java)
        SettingUpToolbar()
        mbinding.refferalcode.setText(Constant.getProfileData(this).reff_code)
        setClicks()

    }

    private fun setClicks() {
        mbinding.btnShare.setOnClickListener {
            val message =
                "${getString(R.string.share_text1)} ${Constant.getProfileData(this).reff_code} ${getString(
                    R.string.share_text2
                )}\n${getString(R.string.click_on_the_linkBelow)} ${Constant.getProfileData(this).sharelink}"
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, message)
            startActivityForResult(Intent.createChooser(share, getString(R.string.share)), 1)
        }

//set the popup that how it works
        txtHowItWork.setOnClickListener {
            tutorilsDialog()
        }

        //set the click to tool to ear money
        txtToolToEranMoney.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(getString(R.string.earn_money_tool_link))
            startActivity(i)
        }
    }

    //***************tutorils dialog****************//
    private fun tutorilsDialog() {
         dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.tutorils_dialog)
        //default textShow
        dialog.txtTutorilsHeader.setText("${getString(R.string.step)} ${"1"}")
        dialog.txtStepsText.text = getString(R.string.tutorilStep1)
        dialog.btnBack.visibility = View.INVISIBLE//default hide btn
        dialog.show()
        //set the Clicks
        dialog.btnBack.setOnClickListener {
//            if (txtTutorilsHeader.text == "${getString(R.string.step)} ${"1"}") {
//                dialog.dismiss()
//            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"2"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"1"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep1)
                dialog.btnBack.visibility = View.INVISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"3"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"2"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep2)
                dialog.btnBack.visibility = View.VISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"4"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"3"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep3)
                dialog.btnBack.visibility = View.VISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"5"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"4"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep4)
                dialog.btnBack.visibility = View.VISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"6"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"5"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep5)
                dialog.btnBack.visibility = View.VISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"7"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"6"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep6)
                dialog.btnBack.visibility = View.VISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"8"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"7"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep7)
                dialog.btnBack.visibility = View.VISIBLE
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
        }
        //btnNextClick
        dialog.btnNext.setOnClickListener {
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"1"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"2"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep2)
                dialog.btnNext.visibility = View.VISIBLE
                dialog.btnBack.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"2"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"3"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep3)
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"3"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"4"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep4)
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"4"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"5"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep5)
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"5"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"6"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep6)
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"6"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"7"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep7)
                dialog.btnNext.visibility = View.VISIBLE
                dialog.imageTutorils.visibility = View.GONE
                return@setOnClickListener
            }
            if (dialog.txtTutorilsHeader.text == "${getString(R.string.step)} ${"7"}") {
                dialog.txtTutorilsHeader.text = "${getString(R.string.step)} ${"8"}"
                dialog.txtStepsText.text = getString(R.string.tutorilStep8)
                dialog.btnNext.visibility = View.INVISIBLE
                dialog.imageTutorils.visibility = View.VISIBLE
                return@setOnClickListener
            }
//                if (txtTutorilsHeader.text == "${getString(R.string.step)} ${"8"}") {
//                    dialog.dismiss()
//                }
        }
    }

    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.rewards)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    // ..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            finish()
        }
    }
}