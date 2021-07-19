package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.raiseADsiputeBuyerSideNotification


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.mercadopago.android.px.model.Order
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_raise_adisputes.*


class RaiseADisputesActivity : AppCompatActivity() {

    private lateinit var viewModel: RaiseDisputesViewModel
    private lateinit var mAdapterDisputes: RaiseADisputeAdapter
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_raise_adisputes)
        viewModel = ViewModelProviders.of(this)[RaiseDisputesViewModel::class.java]
        SettingUpToolbar()
        setAdapter()
        val allModelData = intent.getParcelableExtra<OrderModel>("keyRatingData")!!
        setAllData(allModelData)//set  all the data of activity
        val notificationModel = intent.getParcelableExtra<NotificationModel>("keySource4")!!
        mAdapterDisputes.update(allModelData.products,allModelData.comission_per)
//set the click on the confirm completion
        edtReason.filters = arrayOf<InputFilter>(HideEmoji(this))
        edtName.filters = arrayOf<InputFilter>(HideEmoji(this))

        btnConfirmCompletions.setOnClickListener {
            if (edtName.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Name),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (edtNumber.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Number),
                    Toast.LENGTH_SHORT
                ).show()

            } else if (edtReason.text.isEmpty()) {
                Toast.makeText(
                    this,
                    getString(R.string.Please_Enter_The_Reason),
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                viewModel.setDisputeData(
                    notificationModel,
                    edtName.text.toString().trim(),
                    edtNumber.text.toString().trim(),
                    edtReason.text.toString().trim()


                )
                Wait.visibility = View.VISIBLE
                btnConfirmCompletions.visibility = View.INVISIBLE
            }

        }

        //set the observer
        viewModel.mSucessfulshopListData().observe(this, Observer {
            Toast.makeText(
                this, it,
                Toast.LENGTH_SHORT
            ).show()
            Wait.visibility = View.INVISIBLE
            btnConfirmCompletions.visibility = View.VISIBLE

            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        })
        //failure
        viewModel.mFailureShopList().observe(this, Observer {
            Toast.makeText(
                this, it,
                Toast.LENGTH_SHORT
            ).show()
            Wait.visibility = View.INVISIBLE
            btnConfirmCompletions.visibility = View.VISIBLE
        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById( R.id.toolbartittle)
        text.text = getText(R.string.Raise_a_dispute1)
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

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        disputeRecycler.layoutManager = manager
        mAdapterDisputes = RaiseADisputeAdapter(this)
        disputeRecycler.adapter = mAdapterDisputes
    }


    @SuppressLint("SetTextI18n")
    private fun setAllData(it: OrderModel) {
        txtListName.text = it.listingname
        try {
            txttotal.text = "$" + Constant.roundValue(it.amount.toDouble() )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
