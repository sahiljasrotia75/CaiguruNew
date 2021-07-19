package com.example.caiguru.commonScreens.registerCategory

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardBuyerActivity
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityRegisterCategoryBinding
import com.example.caiguru.commonScreens.registerActivity.RegisterModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.google.gson.Gson
import constant_Webservices.Constant

class RegisterCategoryActivity : AppCompatActivity() {


    private var catModel: GetProfileModel? = null
    private lateinit var mAdapter: CategoryAdapter
    private lateinit var mvmodel: CategoryViewModel
    private lateinit var mbinding: ActivityRegisterCategoryBinding
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_register_category)
        mvmodel = ViewModelProviders.of(this)[CategoryViewModel::class.java]


        SettingUpToolbar()
        setAdapter()

        //set the observer
        mvmodel.getdata().observe(this, Observer {
            val launchIntent = Intent(this, DashBoardBuyerActivity::class.java)
            launchIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(launchIntent)
            finish()

            mbinding.btnwait.visibility = View.INVISIBLE
            mbinding.btnfinish.visibility = View.VISIBLE
        })


        //case of failure
        mvmodel.errorget().observe(this, Observer {

            Toast.makeText(this, it.errormsg, Toast.LENGTH_SHORT).show()
            mbinding.btnwait.visibility = View.INVISIBLE
            mbinding.btnfinish.visibility = View.VISIBLE

        })


        mvmodel.getdataupdate().observe(this, Observer {
            val intent = Intent()
            intent.putExtra("keyUpdatedData", it)
            setResult(Activity.RESULT_OK, intent)
            var model = GetProfileModel()
            val gson = Gson()
            val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
            model = gson.fromJson(json, GetProfileModel::class.java)
            model.categories = it.category_id

            val json1 = gson.toJson(model)
            Constant.getPrefs(application).edit().putString(Constant.PROFILE, json1).apply()
            finish()
            Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()

            mbinding.btnwait.visibility = View.INVISIBLE
            mbinding.btnfinish.visibility = View.VISIBLE


        })

        mvmodel.errorgetupdate().observe(this, Observer {
            Toast.makeText(this, it.errormsg, Toast.LENGTH_SHORT).show()
            mbinding.btnwait.visibility = View.INVISIBLE
            mbinding.btnfinish.visibility = View.VISIBLE
        })


        //....................Api Start on click................//
        mbinding.btnfinish.setOnClickListener {

            val lessThanThree = mAdapter.checkIfLessThanThree()
            if (lessThanThree) {
                Toast.makeText(this, R.string.Please_select_At_Least_3, Toast.LENGTH_SHORT).show()
            } else {
                //get the data
                if (intent.hasExtra("edit")) {
                    mvmodel.update_categories(mAdapter.update_categories())
                    mbinding.btnwait.visibility = View.VISIBLE
                    mbinding.btnfinish.visibility = View.INVISIBLE
                } else {
                    val data = intent.getParcelableExtra<RegisterModel>("keydata")!!
                    data.categories = mAdapter.getSelectedCategories()
                    //send the data
                    mvmodel.setData(data)
                    mbinding.btnwait.visibility = View.VISIBLE
                    mbinding.btnfinish.visibility = View.INVISIBLE

                }
            }
        }


        //***************when click on edit then item is selected****************************//
        val arrayData = ArrayList<CategoryModel>()
        arrayData.addAll(Constant.categoryData(this))
        if (intent.hasExtra("edit")) {
            var selectedcategories = categoryData()
            for (i in 0 until arrayData.size) {
                for (j in 0 until selectedcategories.size) {
                    if (arrayData[i].category_id == selectedcategories[j].category_id) {
                        arrayData[i].hasselected = true
                    }
                }

            }
            catModel = intent.getParcelableExtra<GetProfileModel>("edit")
            val manager = LinearLayoutManager(this)
            mbinding.recycler.layoutManager = manager
            mAdapter = CategoryAdapter(this, arrayData)
            mbinding.recycler.adapter = mAdapter
        }
        //*************************************************************************************//
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

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        mbinding.recycler.layoutManager = manager
        mAdapter = CategoryAdapter(this, firstTimeArray())
        mbinding.recycler.adapter = mAdapter
    }

    private fun firstTimeArray(): java.util.ArrayList<CategoryModel> {
        val data = Constant.categoryData(this)
        for (item in data) {
            item.hasselected = true
        }
        return data
    }

    fun categoryData(): ArrayList<SellerCategoryModel> {
        val categoriesList = Constant.categoryData1(this)
        // from profile details
        val gson = Gson()
        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
        val model: GetProfileModel = gson.fromJson(json, GetProfileModel::class.java)

        val selectedCategories = model.categories.split(",")

        //global
        val arrayData = ArrayList<SellerCategoryModel>()
        for (category in categoriesList) {
            for (i in 0 until selectedCategories.size) {
                if (category.category_id == selectedCategories[i].trim()) {
                    var model = SellerCategoryModel()
                    model.name = category.name
                    model.imagewhite = category.imagewhite
                    model.imageyellow = category.imageyellow
                    model.category_id = category.category_id
                    arrayData.add(model)
                }
            }
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
