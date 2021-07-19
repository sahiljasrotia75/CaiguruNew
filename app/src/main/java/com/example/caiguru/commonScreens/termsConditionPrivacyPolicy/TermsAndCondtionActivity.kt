package com.example.caiguru.commonScreens.termsConditionPrivacyPolicy

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.webkit.WebSettings.PluginState
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.caiguru.R
import kotlinx.android.synthetic.main.activity_terms_and_condtion.*


class TermsAndCondtionActivity : AppCompatActivity() {

    private lateinit var text: TextView
  //  private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_and_condtion)
        SettingUpToolbar()
       // webView = findViewById<View>(R.id.webview) as WebView
        progressBarTerms.visibility = View.VISIBLE
        webview.getSettings().setJavaScriptEnabled(true)
        webview.getSettings().setLoadWithOverviewMode(true)
        webview.getSettings().setUseWideViewPort(true)
        webview.getSettings().setBuiltInZoomControls(true)
        webview.getSettings().setPluginState(PluginState.ON)
        openTermsWebViewData()
    }

    private fun openTermsWebViewData() {
        webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                progressBarTerms.visibility = View.GONE
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBarTerms.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                Toast.makeText(this@TermsAndCondtionActivity, getString(R.string.Network_error), Toast.LENGTH_SHORT).show()
                super.onReceivedError(view, request, error)
            }
        }
        Handler().postDelayed({
            if (intent.hasExtra("TermsEnglish")) {
                webview?.loadUrl("https://caiguru.com/terms-en.html")
            } else if (intent.hasExtra("TermsSpanish")) {
                webview?.loadUrl("https://caiguru.com/terms-es.html")
            }else if (intent.hasExtra("PrivacyEnglish")) {
                webview?.loadUrl("https://caiguru.com/privacy-en.html")
            }else if (intent.hasExtra("PrivacySpanish")) {
                webview?.loadUrl("https://caiguru.com/privacy-es.html")
            }
        }, 500)
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        if (intent.hasExtra("TermsEnglish")) {
            text.text = getText(R.string.Terms_Conditions)

        } else if (intent.hasExtra("TermsSpanish")) {
            text.text = getText(R.string.Terms_Conditions)
        }
        else if (intent.hasExtra("PrivacyEnglish")) {
            text.text = getText(R.string.Privacy_Policy)

        }else if (intent.hasExtra("PrivacySpanish")) {
            text.text = getText(R.string.Privacy_Policy)
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
