package com.example.caiguru.commonScreens.forgotPassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityForgotBinding
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.caiguru.commonScreens.loginScreen.LoginActivity

import constant_Webservices.Constant.Companion.isValidEmailId


class ForgotPasswordActivity : AppCompatActivity() ,ForgetClick{


    lateinit var mBinding: ActivityForgotBinding
    lateinit var mViewModel: ForgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forgot)
        mViewModel = ViewModelProviders.of(this).get(ForgetViewModel::class.java)
        SettingUpToolbar()
        setObserver()
        mBinding.click = this
    }

    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //.............................. forgotPassword..............................................//

    override fun setclick(email: String) {

        if (mBinding.editemail.text!!.isEmpty()) {
            Toast.makeText(this, getString(R.string.Enter_Email), Toast.LENGTH_SHORT).show()
        } else if (!isValidEmailId(mBinding.editemail.text.toString().trim())) {
            Toast.makeText(this, getString(R.string.Enter_the_Valid_Email), Toast.LENGTH_SHORT).show()
        } else {
            mViewModel.setEmail(email)
            showLoader()

        }

    }
    fun setObserver(){
        mViewModel.SucessfulData().observe(this, Observer {
            if (it != null) {
                hideLoader()
                Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
                //when the password is changed user o to the login screen
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        mViewModel.errorget().observe(this, Observer {
            hideLoader()
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
    }

    fun showLoader() {
        mBinding.sumbit.visibility = View.GONE
        mBinding.pleaseWait.visibility = View.VISIBLE
    }

    fun hideLoader() {
        mBinding.sumbit.visibility = View.VISIBLE
        mBinding.pleaseWait.visibility = View.GONE
    }


}