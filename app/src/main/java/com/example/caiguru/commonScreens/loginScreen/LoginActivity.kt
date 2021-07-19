package com.example.caiguru.commonScreens.loginScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardBuyerActivity
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.databinding.ActivityLoginBinding
import com.example.caiguru.commonScreens.forgotPassword.ForgotPasswordActivity
import com.example.caiguru.commonScreens.registerActivity.RegisterActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import constant_Webservices.Constant
import java.util.*

class LoginActivity : AppCompatActivity(), LoginListener {


    private lateinit var callbackManager: CallbackManager
    private lateinit var mbinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //full screen code
        // requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        //  mViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mbinding.login = this

        callbackManager = CallbackManager.Factory.create()
        setObserver()
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id))
        FacebookSdk.fullyInitialize()

        //..........................user automatically login after one time login.................//
        val data = Constant.getPrefs(this).getString(Constant.token, "1")
        //................................check the user is already login or not....................................//
        if (data != "1") {
            val intent = Intent(this, DashBoardBuyerActivity::class.java)//if login
            startActivity(intent)
            finish()
        }

        mbinding.viewSignUpClick.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        mbinding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            startActivity(intent)
        }
    }

    override fun loginListener(email: String, password: String) {
        if (mbinding.editemail.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
        } else if (!Constant.isValidEmailId(mbinding.editemail.text.toString().trim())) {
            Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show()
        } else if (mbinding.editpassword.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
        } else {
            mViewModel.setLogData(email, password)
            showLoader()
        }
    }

    //*********************facebook click******************//
    override fun facebookListener() {
        Log.e("LOCALE: ", "" + Constant.getLocal(application))
        loginWithFacebook()
    }

    private fun setObserver() {
        mViewModel.getdata().observe(this, Observer {
            hideLoader()
            if (it != null) {
                val launchIntent = Intent(this, DashBoardBuyerActivity::class.java)
                launchIntent.flags =
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(launchIntent)
                finish()
            }

        })

        mViewModel.errorget().observe(this, Observer {
            hideLoader()
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })
//***********when the user first time login from facebook
        mViewModel.facebookObserver().observe(this, Observer {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("facebook", it)
            startActivity(intent)
        })
    }

    private fun showLoader() {
        mbinding.btnlogin.visibility = View.GONE
        mbinding.btnPleaseWait.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        mbinding.btnlogin.visibility = View.VISIBLE
        mbinding.btnPleaseWait.visibility = View.GONE
    }


    private fun loginWithFacebook() {
        try {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager, object :
                FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result!!.accessToken
                    GraphRequest.newMeRequest(accessToken)
                    { user, graphResponse ->

                        Log.d("1", user.optString("email"))
                        Log.d("2", user.optString("name"))
                        Log.d("3", user.optString("id"))
                        val url =
                            "https://graph.facebook.com/${user.optString("id")}/picture?type=large&redirect=true&width=600&height=600"
                        val userName = user.optString("name")
                        val userEmail = user.optString("email")
                        val socialId = user.optString("id")
                        mViewModel.loginUsingFacebook(socialId, url, userName, userEmail)

//                    dialog.setMessage("Loading...")
//                    dialog.show()
//                    dialog.setCancelable(false)
                    }.executeAsync()
                }


                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                    //dialog.dismiss()
                    Log.i("FACEBOOK Error: ", error!!.message.toString())
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
