package com.example.caiguru.seller.sellerSetting.sellerProfile.sellerMyprofile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.follower.FollowerBuyerActivity
import com.example.caiguru.buyer.buyerProfile.followings.FollowingBuyerActivity
import com.example.caiguru.databinding.ActivityMyprofileBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.sellerSetting.sellerProfile.sellerEditProfile.SellerEditProfile
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_myprofile.*
import kotlinx.android.synthetic.main.toolbar.view.*

class MyProfileActivity : AppCompatActivity() {

    private var data = GetProfileModel()
    private var tokens: String = ""
    private lateinit var mvmodel: SellerMyProfileViewModel
    lateinit var mbinding: ActivityMyprofileBinding
    private lateinit var text: TextView
    private var menuMain: Menu? = null
    private lateinit var getCategoryAdapter: GetttingCategoryAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_myprofile)
        mvmodel = ViewModelProviders.of(this)[SellerMyProfileViewModel::class.java]
        SettingUpToolbar()
        //  getCategoryAdapter()
        tokens = FirebaseInstanceId.getInstance().token!!
        //  Log.e("getToken", Constant.token)
        // mvmodel.getProfile(tokens)

        mvmodel.SucessfulData().observe(this, Observer {
            progressProfile.visibility = View.GONE
            if (it == null) {
                noDataProfile.visibility = View.VISIBLE
                allLayout.visibility = View.GONE
            } else {

                data = it
                progressProfile.visibility = View.GONE
                allLayout.visibility = View.VISIBLE

                //  mbinding.followingtxt.setText(it.followings + " " + getString(R.string.Followings)+ " ")
                //  mbinding.followerstxt.setText(" " + it.followers + " " + getString(R.string.Followers))

                var model = GetProfileModel()
//                val gson = Gson()
//                val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
                model = Constant.getProfileData(this)
                if (model.image.isEmpty()) {
                    Glide.with(this).load(R.drawable.product_placeholder)
                        .into(mbinding.img)
                } else {
                    Glide.with(this).load(model.image).into(mbinding.img)
                }


                Glide.with(this).load(levelget(model.seller_level).levelImage)
                    .into(mbinding.imgBatch)

                mbinding.followingtxt.setText(model.followings + " " + getString(R.string.following) + " ")
                mbinding.followerstxt.setText(" " + model.followers + " " + getString(R.string.followers))
                mbinding.editname.setText(model.name)
                mbinding.editemail.setText(model.email)
                mbinding.editaddress.setText(model.full_address)
                mbinding.point.setText(
                    levelget(model.seller_level).levelname + " " + "(" + model.seller_points + " " + getString(
                        R.string.points
                    ) + ")"
                )
                mbinding.good.setText(model.seller_reputation)
                //set the adapter
                Glide.with(this).load(levelget(model.seller_level).levelImage)
                    .into(mbinding.levelimg)
                getCategoryAdapter()

            }

        })


        mvmodel.errorget().observe(this, Observer {
            noDataProfile.visibility = View.VISIBLE
            progressProfile.visibility = View.GONE
            allLayout.visibility = View.GONE
        })



        mbinding.followingtxt.setOnClickListener {
            val intent = Intent(this, FollowingBuyerActivity::class.java)
            startActivity(intent)
        }
        //set the click on the follower
        mbinding.followerstxt.setOnClickListener {
            val intent = Intent(this, FollowerBuyerActivity::class.java)
            startActivity(intent)
        }


    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        mvmodel.getProfile(tokens)
        noDataProfile.visibility = View.GONE
        progressProfile.visibility = View.VISIBLE
        allLayout.visibility = View.GONE

//        var model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//        model = gson.fromJson(json, GetProfileModel::class.java)
//        if (model.image.isEmpty()) {
//            Glide.with(this).load(R.drawable.product_placeholder)
//                .into(mbinding.img)
//        } else {
//            Glide.with(this).load(model.image).into(mbinding.img)
//        }
//
//
//        Glide.with(this).load(levelget(model.seller_level).levelImage).into(mbinding.imgBatch)
//
//        mbinding.followingtxt.setText(model.followings + " " + "Following" + " ")
//        mbinding.followerstxt.setText(" " + model.followers + " " + "Followers")
//        mbinding.editname.setText(model.name)
//        mbinding.editemail.setText(model.email)
//        mbinding.editaddress.setText(model.full_address)
//        mbinding.point.setText(levelget(model.seller_level).levelname + " " + "(" + model.seller_points + " " + "points" + ")")
//        mbinding.good.setText(model.seller_reputation)
//        //set the adapter
//        Glide.with(this).load(levelget(model.seller_level).levelImage).into(mbinding.levelimg)
        // getCategoryAdapter()
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.myprofile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

        toolbar.editToolbar.visibility = View.VISIBLE
        toolbar.editToolbar.setOnClickListener {
            val intent = Intent(this, SellerEditProfile::class.java)
            startActivity(intent)

        }
        //   supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //   supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

    }

    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
//            } else if (item!!.itemId == R.id.edit) {
//                val intent = Intent(this, SellerEditProfile::class.java)
//                startActivity(intent)
//            }
        }
        return super.onOptionsItemSelected(item)
    }


    //****************************************** set Menu in toolbar ****************************//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        getMenuInflater().inflate(R.menu.profile_menu, menu)
//        menuMain = menu
//        return true
//    }

    //get selcted city adapter
    private fun getCategoryAdapter() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mbinding.rvcategory.layoutManager = manager
        getCategoryAdapter = GetttingCategoryAdapter(this)
        mbinding.rvcategory.adapter = getCategoryAdapter
        getCategoryAdapter.update(categoryData())
    }


    private fun levelget(level: String): SellerLevelModel {
        val sellerLevel = Constant.SellerLevel(this)
        for (category in sellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return SellerLevelModel()
    }

    //
    fun categoryData(): ArrayList<SellerCategoryModel> {
        val categoriesList = Constant.categoryData1(this)
        // from profile details
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//        val model: GetProfileModel = gson.fromJson(json, GetProfileModel::class.java)

        val selectedCategories = Constant.getProfileData(this).categories.split(",")

        //global
        val arrayData = ArrayList<SellerCategoryModel>()
        for (category in categoriesList) {
            for (i in 0 until selectedCategories.size) {
                if (category.category_id == selectedCategories[i].trim()) {
                    val model = SellerCategoryModel()
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
}