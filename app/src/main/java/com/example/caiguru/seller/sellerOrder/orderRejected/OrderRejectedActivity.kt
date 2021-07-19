package com.example.caiguru.seller.sellerOrder.orderRejected

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.ActivityOrderRejectedBinding
import constant_Webservices.Constant


class OrderRejectedActivity : AppCompatActivity(), OrderRejectedAdapter.selectedData {
    private  var Rejectstatus: String=""
    lateinit var adapter: OrderRejectedAdapter
    lateinit var mbinding: ActivityOrderRejectedBinding
    lateinit var mvmodel: OrderRejectedViewModel
    private lateinit var text: TextView
    var nameOfReason = ""
    var reasonStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_order_rejected)
        //  mvmodel = ViewModelProviders.of(this)[OrderRejectedViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(OrderRejectedViewModel::class.java)

        initData()
        clickListeners()
        setAdapter()
        setObserver()
    }

    private fun initData() {
        if (intent.hasExtra("statusTobeDelivered") && intent.hasExtra("hideBackButton")) {
            Rejectstatus="7"
            SettingUpToolbar()
        }else{
            Rejectstatus="3"
            SettingUpToolbar()
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        }



        val seller_active_status =
            Constant.getPrefs(this).getString(Constant.seller_active_status, "")
        mbinding.edtRejected.filters = arrayOf(HideEmoji(this), LengthFilter(100))
        // mbinding.edtRejected.setFilters(arrayOf<InputFilter>(LengthFilter(100)))

        if (seller_active_status == "1") {
            //    mBinding.switchView.visibility = View.VISIBLE
            mbinding.activeOrder.background = resources.getDrawable(R.color.green)
            mbinding.activeOrder.text = resources.getText(R.string.active)

        } else {
            // mBinding.switchView.visibility = View.VISIBLE
            mbinding.activeOrder.background = resources.getDrawable(R.color.red)
            mbinding.activeOrder.text = resources.getText(R.string.inactive)
        }

    }

    private fun setObserver() {

        mvmodel.mSucessfulRejectedRequest().observe(this, Observer {
            if (it != null) {
                hideLoader()
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }

        })
        mvmodel.mFailureRejectedRequest().observe(this, Observer {
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
            hideLoader()
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()

        })
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun clickListeners() {

        mbinding.btnDone.setOnClickListener {
            val reqId = intent.getStringExtra("model")!!
            if (reasonStatus) {
                if (nameOfReason == "") {
                    if (mbinding.edtRejected.text.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.Please_enter_the_reason_in_the_above_field),
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        showLoader()
                        nameOfReason = mbinding.edtRejected.text.toString()
                        mvmodel.changeRequestStatus(reqId, nameOfReason,Rejectstatus)

                    }

                } else {
                    showLoader()
                    mvmodel.changeRequestStatus(reqId, nameOfReason, Rejectstatus)

                }
            } else {
                Toast.makeText(this, getString(R.string.Please_select_a_reason), Toast.LENGTH_SHORT)
                    .show()

            }


        }

        mbinding.edtRejected.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {

                mbinding.digitText.text = "${s!!.length}/100"

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getString(R.string.order_rejected)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = OrderRejectedAdapter(this, getOrderRejectList())
        mbinding.rvrejectOrder.layoutManager = manager
        mbinding.rvrejectOrder.adapter = adapter
    }

    private fun getOrderRejectList(): ArrayList<OrderRejectedModel> {
        val arrayList: ArrayList<OrderRejectedModel> = ArrayList()

        var orderReject = OrderRejectedModel()
        orderReject.rejectText = getString(R.string.total_amount)
        arrayList.add(orderReject)


        orderReject = OrderRejectedModel()
        orderReject.rejectText = getString(R.string.more_stock)
        arrayList.add(orderReject)

        orderReject = OrderRejectedModel()
        orderReject.rejectText = getString(R.string.cannot_deliver)
        arrayList.add(orderReject)

        orderReject = OrderRejectedModel()
        orderReject.rejectText = getString(R.string.another_reason)
        arrayList.add(orderReject)

        return arrayList
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

    override fun getSelectedPosition(position: Int, name: String, checked: Boolean) {
        reasonStatus = checked
        if (position == 3 && checked) {
            nameOfReason = ""
            mbinding.edtRejected.visibility = View.VISIBLE
            mbinding.digitText.visibility = View.VISIBLE


        } else {
            if (position==0){
                nameOfReason = "The total amount of order is higher than my budget."
            }else if (position==1){
                nameOfReason = "There is no more stock."
            }else if (position==2){
                nameOfReason = "I can't deliver to the address indicated by the user."
            }else {
                nameOfReason = name
            }

            mbinding.edtRejected.visibility = View.GONE
            mbinding.digitText.visibility = View.GONE
        }

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