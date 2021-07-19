package com.example.caiguru.seller.sellerSelectedDaysAndTime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.caiguru.databinding.ActivitySellerSelectDaysAndTimeBinding
import kotlinx.android.synthetic.main.toolbar.view.*
import java.util.ArrayList

class SellerSelectDaysAndTimeActivity : AppCompatActivity() {


    private var preFilledData = ArrayList<DaysParentModel>()
    private var adapterData = ArrayList<DaysParentModel>()
//    private var globalParentIndex: Int = 0
//    private var globalChilIndex: Int = 0
    private lateinit var mAdapter: TimeZoneAdapterParent
    private lateinit var text: TextView
    private lateinit var mvmodel: TimeZoneViewModel
    private lateinit var mbnidnig: ActivitySellerSelectDaysAndTimeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbnidnig =
            DataBindingUtil.setContentView(this, R.layout.activity_seller_select_days_and_time)
        mvmodel = ViewModelProvider(this).get(TimeZoneViewModel::class.java)
        //setting up tool bar
        SettingUpToolbar()
        setAdapter()


        if (intent.hasExtra("keySellerpost")) {
            mbnidnig.txtheading.text = getString(R.string.time_intervals_heading_text)
            preFilledData =
                intent.getParcelableArrayListExtra<DaysParentModel>("keySellerpost")!!
            if (preFilledData.size == 0) {
                mAdapter.update(adapterData)

            } else {
                mAdapter.update(preFilledData)
            }

        } else {
            //buyer side data
            preFilledData = intent.getParcelableArrayListExtra<DaysParentModel>("keyBuyerpost")!!
            if (preFilledData.size == 0) {
                mAdapter.update(adapterData)

            } else {
                //  mAdapter.update(preFilledData)
                mAdapter.update(getSelectedPosition(preFilledData, adapterData))
            }
            mbnidnig.txtheading.text = getString(R.string.time_intervals_heading_text_buyer_side)
        }
        //set the observer  parent adapter
        mvmodel.sendDayWeek().observe(this, Observer {
            adapterData = it
            if (preFilledData.isEmpty()) {
                mAdapter.update(it)

            } else {
                //  mAdapter.update(preFilledData)
                mAdapter.update(getSelectedPosition(preFilledData, adapterData))
            }

        })

        //set the click on btn click
        mbnidnig.btnfinish.setOnClickListener {
            var isNoEmptyField=true
            val model = mAdapter.getUpdatedList()
//**************new code set
            for (parentModel in model) {
                for (childModel in parentModel.slotList) {
                    if (childModel.from.contains(":") && childModel.to.contains(getString(R.string.To))) {
                        isNoEmptyField=false
                    }
                    else if (childModel.from.contains(getString(R.string.From)) && childModel.to.contains(":")) {
                        isNoEmptyField=false
                    }

                }
            }
            if (isNoEmptyField){
                val intent = Intent()
                intent.putExtra("daysmodel", model)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }else{
                Toast.makeText(this, getString(R.string.Data_is_incompleted), Toast.LENGTH_SHORT)
                .show()
                return@setOnClickListener
            }
        }

    }

    private fun getSelectedPosition(
        preFilledData: ArrayList<DaysParentModel>,
        myLocalData: ArrayList<DaysParentModel>
    ): ArrayList<DaysParentModel> {
        for (item in myLocalData) {
            for (prefilData in preFilledData) {
                if (item.weeks == prefilData.weeks) {
                    prefilData.position = item.position
                }
            }
        }
        return preFilledData
    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbnidnig.recyclerr.layoutManager = manager
        mAdapter = TimeZoneAdapterParent(this)
        mbnidnig.recyclerr.adapter = mAdapter
    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        toolbar.clear.visibility = View.VISIBLE
        text.text = getText(R.string.Select_Days_and_Time)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)


        toolbar.clear.setOnClickListener {
            mAdapter.clearTimeSlot()
            for (i in 0 until adapterData.size) {
                for (childIndex in 0 until adapterData[i].slotList.size) {
                    adapterData[i].slotList[childIndex].from = getString(R.string.From)
                    adapterData[i].slotList[childIndex].to = getString(R.string.To)
                }
            }
            preFilledData.clear()
            mAdapter.update(preFilledData)
            mAdapter.update(adapterData)

        }
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

//    override fun getData(childIndex: Int, parentIndex: Int) {
//        globalChilIndex = childIndex
//        globalParentIndex = parentIndex
//    }
}


