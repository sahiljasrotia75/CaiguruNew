package com.example.caiguru.commonScreens.selectCities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList.CloseListPagination
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_select_cities.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*

class SelectCitiesActivity : AppCompatActivity(), SelectCitiesAdapter.selectedCityInteface,
    GetttingSelectedCityAdapter.removeSelectedCityInterface {
    private var selectedCity = ArrayList<CitiesModel>()
    private var allCityGlobal = ArrayList<CitiesModel>()
    private var gettingSlectedCity = ArrayList<CitiesModel>()
    private lateinit var toolbarr: Toolbar
    private lateinit var selectCitiesAdapter: SelectCitiesAdapter
    private lateinit var getSelectedCityAdapter: GetttingSelectedCityAdapter//click the city than show on the adapter
    private lateinit var mvmodel: SelectCitiesViewModel
    lateinit var text: TextView
    var islastpageData: Boolean = false
    var isLoadingMoreItems: Boolean = false
    var page = 0
    var layoutpag = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_cities)
     //   mvmodel = ViewModelProviders.of(this)[SelectCitiesViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(SelectCitiesViewModel::class.java)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        getCityAdapter()//getting the cities
        selectedCityAdapter()
        //var from = intent.getStringExtra("keyHideButton")
        if (intent.hasExtra("keyHideButton")) {
            SettingUpToolbar()
        } else {
            SettingUpToolbar()
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        }
        //set the text
        if (intent.hasExtra("textSellerSide")){
            textheading.text=getString(R.string.city_heading_seller_side)
        }
        mvmodel.listSavedCities(intent)
        mvmodel.getCities(page.toString())

//**********************set cities api****************//
        //sucessful observer
        mvmodel.mSucessfulCitiesData().observe(this, Observer {
            allCityGlobal = it
            progressPagination.visibility = View.GONE
            isLoadingMoreItems = false
            selectCitiesAdapter.Update(categoryDatas(it))

        })

        //failure observer
        mvmodel.mFailureCitiesData().observe(this, Observer {
            progressPagination.visibility = View.GONE
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        })


        //***********************pagination**********************//
        recyclercities.addOnScrollListener(object : CloseListPagination(layoutpag) {
            override fun loadMoreItems() {
                isLoadingMoreItems = true
                progressPagination.visibility = View.VISIBLE
                page++
                mvmodel.getCities(page.toString())
            }

            override fun isLastPage(): Boolean {
                return islastpageData
            }

            override fun isLoading(): Boolean {
                return isLoadingMoreItems

            }

        })

        //**************************getting cities set to 2nd adapter*****************//
        mvmodel.mSelectedCityGet().observe(this, Observer {

            if (it.size > 0) {
                gettingSlectedCity = it
                txtcities.text = getString(R.string.Selected_Cities) + " " + it.size.toString()
                noCitySelected.visibility = View.GONE
            } else {
                noCitySelected.visibility = View.VISIBLE
                txtcities.text = getString(R.string.Selected_Cities) + " " + it.size.toString()
            }
            getSelectedCityAdapter.upDateData(it)


        })

        //**********************set the click on the search button*********************//
        searchview.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                page = 0

                if (s!!.isNotEmpty()) {
                    mvmodel.searchCity(s.toString(), page.toString())
                } else {
                    mvmodel.getCities(page.toString())
                }

            }

        })
        //*******************************done button api save the selected city and send to server*************//
        toolbarr.done.setOnClickListener {
            Constant.hideSoftKeyboard(it,this)//hide keybaord
            if (gettingSlectedCity.size == 0 || gettingSlectedCity.isEmpty()) {
                Toast.makeText(this, getString(R.string.select_city), Toast.LENGTH_SHORT).show()

            } else if (getSelectedCityAdapter.getSlectedCity().size == 0 || getSelectedCityAdapter.getSlectedCity().isEmpty()) {
                Toast.makeText(this, getString(R.string.select_city), Toast.LENGTH_SHORT).show()

            } else {
                val cities = mvmodel.getSelectedCities()
                if (intent.hasExtra("cities")) {
                    val intent = Intent()
                    intent.putParcelableArrayListExtra("cities", cities)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    mvmodel.setWebServicesCity(gettingSlectedCity)
                }
            }
        }

        //*******************observer of selecetd city ******************//
        //sucessful
        mvmodel.mSucessfulsendCity().observe(this, Observer {
            selectedCity = it
            var msg: String = ""
            var resultcode = 0
            for (item in it) {
                msg = item.msg
                val type = intent.getStringExtra("type")
                val switch = intent.getStringExtra("switch")
                val from = intent.getStringExtra("from")

                if (from == "1") {
                    resultcode = 1233
                } else {
                    resultcode = 1234
                }
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                val intent = Intent()
                intent.putExtra("type", type)
                intent.putExtra("switch", switch)
                intent.putExtra("from", from)
                setResult(resultcode, intent)
                finish()
            }

        })
        //failure
        mvmodel.mFailureSendCity().observe(this, Observer {
            noCitySelected.visibility = View.VISIBLE
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()


        })


    }


    //***********************************city already selected*****************//
//    private fun cityData(id: String): CitiesModel {
//        for (item in allCityGlobal) {
//
//            if (item.id == id) {
//                return item
//            }
//        }
//        return CitiesModel()
//    }

    //1nd adapter
    //add cities in second Adapter
    override fun selectedCities(mData: CitiesModel) {

        mvmodel.setSelectedCity(mData)
    }

    //remove cities
    override fun removeCities(mData: CitiesModel) {
        mvmodel.removeCity(mData)


    }

    //remove city from 2nd adapter button
    override fun removeSelectedCity(mData: CitiesModel) {
        noCitySelected.visibility = View.VISIBLE
        mvmodel.removeCity(mData)
    }


    private fun SettingUpToolbar() {
        //getting toolbar id
        toolbarr = findViewById<Toolbar>(R.id.toolbar)
        toolbar.done.visibility = View.VISIBLE
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Select_Cities)


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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 199 && resultCode == RESULT_OK) {

        }
    }
    private fun selectedCityAdapter() {
        recyclercities.layoutManager = layoutpag//set layout pag
        selectCitiesAdapter = SelectCitiesAdapter(this)
        recyclercities.adapter = selectCitiesAdapter
    }

    //get selcted city adapter
    private fun getCityAdapter() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerbottom.layoutManager = manager
        getSelectedCityAdapter = GetttingSelectedCityAdapter(this)
        recyclerbottom.adapter = getSelectedCityAdapter
    }


    fun categoryDatas(it: ArrayList<CitiesModel>): ArrayList<CitiesModel> {
        val arrayData = ArrayList<CitiesModel>()
        for (category in it) {
            category.hasselected = false
            for (element in mvmodel.getSelectedCities()) {
                if (category.id == element.id.trim()) {
                    category.hasselected = true
                }
            }

            arrayData.add(category)
        }
        return arrayData
    }
}