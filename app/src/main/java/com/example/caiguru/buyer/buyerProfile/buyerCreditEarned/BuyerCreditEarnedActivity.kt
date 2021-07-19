package com.example.caiguru.buyer.buyerProfile.buyerCreditEarned

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.example.caiguru.databinding.ActivityBuyerCreditEarnedBinding
import com.example.caiguru.commonScreens.earnCreditsConvert.CreditConvertActivity
import com.google.firebase.iid.FirebaseInstanceId
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_credit_earned.*
import kotlinx.android.synthetic.main.activity_buyer_credit_earned.progress
import kotlinx.android.synthetic.main.nodata.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.lang.Exception

class BuyerCreditEarnedActivity : AppCompatActivity() {

     var tokens: String=""
    private lateinit var mbinding: ActivityBuyerCreditEarnedBinding
    private lateinit var mvmodel: BuyerCreditEarnedViewModel
    private lateinit var mAdapter: TopRefferedUserAdapter
    private lateinit var text: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_buyer_credit_earned)
      //  mvmodel = ViewModelProviders.of(this)[BuyerCreditEarnedViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerCreditEarnedViewModel::class.java)
        SettingUpToolbar()
        NODatadescriptionText.text = getString(R.string.No_user_at_the_moment)
        mvmodel.earnedByreffer()
        progress.visibility = View.VISIBLE
        setAdapter()

        //***********************************getProfileData***********************************************//
        mvmodel.SucessfulDataGetProfile().observe(this, Observer {
            progress.visibility = View.GONE
            mbinding.totalcredit.setText(
                Constant.roundValue(it.earned_credits.toDouble()) + getString(
                    R.string.credits
                )
            )
            if (it.earned_credits.isNotEmpty()) {
                try {
                    val earned_credits = Constant.roundValue(it.earned_credits.toDouble()).toDouble()
                    val redeemCredit = Constant.roundValue(it.redeem_credit_limit.toDouble()).toDouble()

                    if (earned_credits >= redeemCredit) {
                        toolbar.inCash.visibility = View.VISIBLE
                        toolbar.inCash.setOnClickListener {
                            val intent = Intent(this, CreditConvertActivity::class.java)
                            intent.putExtra("KeySourceopenScreenByActivity",1)
                            startActivity(intent)
                        }

                    } else {
                        toolbar.inCash.visibility = View.GONE
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }

            }

        })
        //failure
        mvmodel.errorgetInGetProfile().observe(this, Observer {
            //Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message,this)
            txtNoData.visibility = View.VISIBLE
            progress.visibility = View.GONE
            rvbuyerTopReffered.visibility = View.INVISIBLE
        })

        //sucessful case
        mvmodel.getList().observe(this, Observer {
            progress.visibility = View.GONE
            if (it.isEmpty()) {
                txtNoData.visibility = View.VISIBLE
                rvbuyerTopReffered.visibility = View.INVISIBLE
                mAdapter.update(it)
            } else {
                txtNoData.visibility = View.INVISIBLE
                rvbuyerTopReffered.visibility = View.VISIBLE
                mAdapter.update(it)
            }
        })

        //failure
        mvmodel.getListFailure().observe(this, Observer {

          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            txtNoData.visibility = View.VISIBLE
            progress.visibility = View.GONE
            rvbuyerTopReffered.visibility = View.INVISIBLE

        })

    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbinding.rvbuyerTopReffered.layoutManager = manager
        mAdapter = TopRefferedUserAdapter(this)
        mbinding.rvbuyerTopReffered.adapter = mAdapter
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Credits_Earned)
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

    override fun onResume() {
        super.onResume()
//set the get Profile Api
        tokens = FirebaseInstanceId.getInstance().token!!
        mvmodel.getProfile(tokens)
        progress.visibility = View.VISIBLE

    }
}