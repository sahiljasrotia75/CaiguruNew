package com.example.caiguru.seller.sellerChangePassword

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.ActivityChangePasswordBinding
import com.example.caiguru.seller.sellerSetting.sellerProfile.sellerEditProfile.SellerEditProfile
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.activity_editprofile.editchangepassword

class ChangePasswordActivity:AppCompatActivity() {
    lateinit var mbinding:ActivityChangePasswordBinding
    lateinit var mViewModel:ChangePasswordViewModel
    lateinit var text:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)

       // mViewModel = ViewModelProviders.of(this)[ChangePasswordViewModel::class.java]
        mViewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
        SettingUpToolbar()
        editoldpassword.filters = arrayOf<InputFilter>(HideEmoji(this))
        editchangepassword.filters = arrayOf<InputFilter>(HideEmoji(this))
        editconfirmpassword.filters = arrayOf<InputFilter>(HideEmoji(this))

        mbinding.btnDone.setOnClickListener {
            if (mbinding.editoldpassword.text.isEmpty()){
                Toast.makeText(this, getString(R.string.enter_old_password), Toast.LENGTH_SHORT).show()
            }else if(mbinding.editchangepassword.text.isEmpty()){
                Toast.makeText(this, getString(R.string.enter_change_password), Toast.LENGTH_SHORT).show()
            }else if(mbinding.editconfirmpassword.text.isEmpty()){
                Toast.makeText(this, getString(R.string.please_enter_confirm_password), Toast.LENGTH_SHORT).show()
            } else if(mbinding.editchangepassword.text.toString() != mbinding.editconfirmpassword.text.toString()){
                Toast.makeText(this, getString(R.string.password_match), Toast.LENGTH_SHORT).show()

            }else{
                mViewModel.updatePassword(mbinding.editchangepassword.text.toString(),mbinding.editoldpassword.text.toString())
                showLoader()
            }
        }
        //******************************************************Update Password ******************************************//
        mViewModel.getUpdatePassword().observe(this, Observer {
            if (it != null) {
                hideLoader()
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        mViewModel.getError().observe(this, Observer {
            hideLoader()
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })

        //*****************************************************************************************************************//
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.changepassword)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        //inflate menu
        // toolbar.inflateMenu(R.menu.toolbar_main_menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }else if(item.itemId==R.id.edit){
                val intent= Intent(this, SellerEditProfile::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showLoader() {
        mbinding.btnDone.visibility = View.GONE
        mbinding.btnPleaseWait.visibility = View.VISIBLE
    }

    fun hideLoader() {
        mbinding.btnDone.visibility = View.VISIBLE
        mbinding.btnPleaseWait.visibility = View.GONE
    }

}