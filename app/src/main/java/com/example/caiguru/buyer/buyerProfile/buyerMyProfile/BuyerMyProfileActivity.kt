package com.example.caiguru.buyer.buyerProfile.buyerMyProfile

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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.MyOrderViewModel
import com.example.caiguru.buyer.buyerProfile.follower.FollowerBuyerActivity
import com.example.caiguru.buyer.buyerProfile.followings.FollowingBuyerActivity
import com.example.caiguru.databinding.ActivityMyprofileBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import constant_Webservices.BuyerLevelModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_myprofile.*


class BuyerMyProfileActivity : AppCompatActivity() {
    private lateinit var mvmodel: BuyerMyProfileViewModel
    lateinit var mbinding: ActivityMyprofileBinding
    private lateinit var text: TextView
    private var tokens: String = ""
    private var data = GetProfileModel()
    private lateinit var getCategoryAdapter: GetttingCategoryBuyerAdapter


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_myprofile)
       // mvmodel = ViewModelProviders.of(this)[BuyerMyProfileViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerMyProfileViewModel::class.java)
        SettingUpToolbar()

        tokens = FirebaseInstanceId.getInstance().token!!




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
                val gson = Gson()
                val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
                model = gson.fromJson(json, GetProfileModel::class.java)
                if (model.image.isEmpty()) {
                    Glide.with(this).load(R.drawable.product_placeholder)
                        .into(mbinding.img)
                } else {
                    Glide.with(this).load(model.image).into(mbinding.img)
                }


                Glide.with(this).load(levelget(model.seller_level).levelImage)
                    .into(mbinding.imgBatch)
                mbinding.followingtxt.setText(model.followings + " " + getString(R.string.folowing) + " ")
                mbinding.followerstxt.setText(" " + model.followers + " " + getString(R.string.folow))
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

//set the click on the following
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

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.myprofile)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)

     //   toolbar.editToolbar.visibility = View.VISIBLE
//        toolbar.editToolbar.setOnClickListener {
//            val intent = Intent(this, BuyerEditProfileActivity::class.java)
//            startActivity(intent)
//
//        }
        //     supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //      supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
    }


    //****************************************** set Menu in toolbar ****************************//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        getMenuInflater().inflate(R.menu.profile_menu, menu)
//        menuMain = menu
//        return true
//    }


    //..........back button click...........//
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getCategoryAdapter() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mbinding.rvcategory.layoutManager = manager
        getCategoryAdapter = GetttingCategoryBuyerAdapter(this)
        mbinding.rvcategory.adapter = getCategoryAdapter
        getCategoryAdapter.update(categoryData())
    }

    private fun levelget(level: String): BuyerLevelModel {
        val bellerLevel = Constant.BuyerLevel(this)
        for (category in bellerLevel) {

            if (category.levelType == level.trim()) {

                return category
            }
        }
        return BuyerLevelModel()
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

    override fun onResume() {
        super.onResume()

        mvmodel.getProfile(tokens)
        noDataProfile.visibility = View.GONE
        progressProfile.visibility = View.VISIBLE
        allLayout.visibility = View.GONE
//        var model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString("profile", "")
//        model = gson.fromJson(json, GetProfileModel::class.java)
//        if (model.image.isEmpty()) {
//            Glide.with(this).load(R.drawable.product_placeholder)
//                .into(mbinding.img)
//        } else {
//            Glide.with(this).load(model.image).into(mbinding.img)
//        }
//        Glide.with(this).load(levelget(model.buyer_level).levelImage).into(mbinding.imgBatch)
//
//        mbinding.followingtxt.setText(model.followings + " " + getString(R.string.Followings) + " ")
//        mbinding.followerstxt.setText(" " + model.followers + " " +getString(R.string.Followers))
//        mbinding.editname.setText(model.name)
//        mbinding.editemail.setText(model.email)
//        mbinding.editaddress.setText(model.full_address)
//        mbinding.point.setText(levelget(model.buyer_level).levelname + " " + "(" + model.buyer_points + " " +getString(R.string.points)+ ")")
//        Glide.with(this).load(levelget(model.seller_level).levelImage).into(mbinding.levelimg)
//        mbinding.good.setText(model.buyer_reputation)
//        getCategoryAdapter()

    }

}