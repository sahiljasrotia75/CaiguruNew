package com.example.caiguru.commonScreens.help

import android.R.attr.phoneNumber
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.caiguru.R
import kotlinx.android.synthetic.main.activity_help.*


class HelpActivity : AppCompatActivity() {
    private lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        SettingUpToolbar()
        emailTitle.text =
            "${getString(R.string.You_can_mail)} - ${getString(R.string.caiguru_email)}"
        numberTitle.text =
            "${getString(R.string.You_can_call)} - ${getString(R.string.client_number)}"

        //*****************email click***************//
        emailTitle.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + getString(R.string.caiguru_email)))
            intent.putExtra(Intent.EXTRA_SUBJECT, "your_subject")
            intent.putExtra(Intent.EXTRA_TEXT, "your_text")
            startActivity(intent)
        }

        //******************number  click***********//
        numberTitle.setOnClickListener {
//            val intent = Intent(Intent.ACTION_DIAL)
//            intent.data = Uri.parse(getString(R.string.telephoneNO))
//            startActivity(intent)

//            val smsIntent = Intent(Intent.ACTION_VIEW)
//            smsIntent.type = "vnd.android-dir/mms-sms"
//            smsIntent.putExtra("address", getString(R.string.client_number))
//            smsIntent.putExtra("sms_body", getString(R.string.Dear_Caiguru))
//            startActivity(smsIntent)


            try {
                if (getDefaultSmsAppPackageName(this) != null) {
                    val smsUri = Uri.parse("smsto:$phoneNumber")
                    val smsIntent = Intent(Intent.ACTION_VIEW, smsUri)
                    smsIntent.type = "vnd.android-dir/mms-sms"
                    smsIntent.putExtra("address", getString(R.string.client_number))
                    smsIntent.putExtra("sms_body", getString(R.string.Dear_Caiguru))
                    startActivity(smsIntent)
                }
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("smsto:" + Uri.encode(getString(R.string.telephoneNO)))
                startActivity(intent)
                e.printStackTrace()
            }

        }

    }

    fun getDefaultSmsAppPackageName(context: Context): String? {
        val defaultSmsPackageName: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context)
            return defaultSmsPackageName
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_DEFAULT).setType("vnd.android-dir/mms-sms")
            val resolveInfos: List<ResolveInfo> =
                context.getPackageManager().queryIntentActivities(intent, 0)
            if (resolveInfos != null && !resolveInfos.isEmpty()) return resolveInfos[0].activityInfo.packageName
        }
        return null
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Help)
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
