package com.example.caiguru.seller.sellerChooseCategory

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivitySellerChooseCategoryBinding
import constant_Webservices.Constant

class SellerChooseCategoryActivity : AppCompatActivity() {

    private lateinit var text: TextView
    private lateinit var mAdapter: SellerCategoryAdapter
    private lateinit var mbinding: ActivitySellerChooseCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_seller_choose_category)

        setAdapter()
        SettingUpToolbar()

        if (intent.hasExtra("keyPrefillCategory")) {

            val data = intent.getStringExtra("keyPrefillCategory")!!

            mAdapter.prefillingData(data)
        }
        else if (intent.hasExtra("keyPrefillingBuyerCategory")){
            val data = intent.getStringExtra("keyPrefillingBuyerCategory")!!
            mAdapter.prefillingData(data)
        }
        //....................Api Start on click................//
        mbinding.btnfinish.setOnClickListener {

            val lessThanThree = mAdapter.checkIfLessThanThree()

            if (lessThanThree) {
                Toast.makeText(this, R.string.Seller_category_heading, Toast.LENGTH_SHORT).show()
            } else {
                //get the data
                val categories = mAdapter.getSelectedCategories()
                val intent = Intent()
                intent.putExtra("keyname", categories)
                setResult(Activity.RESULT_OK, intent)//set the result
                finish()
            }
        }
    }


    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbinding.recycler.layoutManager = manager
        mAdapter = SellerCategoryAdapter(this, categoryData())
        mbinding.recycler.adapter = mAdapter
    }

    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.choose_category)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

    }

    //category
    fun categoryData(): ArrayList<SellerCategoryModel> {
        val categoriesList = Constant.categoryData1(this)
        val arrayData = ArrayList<SellerCategoryModel>()
        for (category in categoriesList) {
            val model = SellerCategoryModel()
            model.name = category.name
            model.imagewhite = category.imagewhite
            model.imageyellow = category.imageyellow
            model.category_id = category.category_id
            arrayData.add(model)
        }
        return arrayData

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}