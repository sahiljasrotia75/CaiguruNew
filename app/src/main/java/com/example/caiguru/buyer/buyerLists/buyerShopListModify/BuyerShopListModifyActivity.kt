package com.example.caiguru.buyer.buyerLists.buyerShopListModify

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.postList.buyerPostScreenPrefilled.BuyerPostShopPreFillActivity
import com.example.caiguru.buyer.buyerLists.buyerDeliveryDetail.BuyerDeliveryDetailActivity
import com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment.BuyerShopModel
import com.example.caiguru.databinding.ActivityShoplistModifyBinding
import com.example.caiguru.commonScreens.registerCategory.CategoryModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_shoplist_modify.*

class BuyerShopListModifyActivity : AppCompatActivity() {
    private var buyerShopModel = BuyerShopModel()
    //    var freeProducts = 0
//    private var buyerLevel: String = ""
//    private var creditPerProduct: String = ""
//    private var buyerCredits: String = ""
   // private lateinit var dialog: Dialog
    private var allData = CustomerChildModel()
    lateinit var mbinding: ActivityShoplistModifyBinding
    lateinit var mvmodel: BuyerShopListModifyViewModel
    private var listName: String = ""
    private lateinit var buyerShopListParentAdapter: BuyerShopListParentAdapter
    lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_shoplist_modify)
       // mvmodel = ViewModelProviders.of(this)[BuyerShopListModifyViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerShopListModifyViewModel::class.java)


        buyerShopModel = intent.getParcelableExtra<BuyerShopModel>("keyname")!!
        listName = buyerShopModel.listingname

        SettingUpToolbar()
        setAdapters()
        //set the text
        val udata = getString(R.string.Delivery_Details)
        val text = SpannableString(udata)
        text.setSpan(UnderlineSpan(), 0, udata.length, 0)

        mbinding.deliveryDetailed.text = text
//*************set the api of shopping list***************//
        mvmodel.setBuyerShoplist(buyerShopModel.id)
        mbinding.progress.visibility = View.VISIBLE

        //***************observer of sucessful data&*************//
        mvmodel.mSucessfulGetbuyerList().observe(this, Observer {
            allData = it
            mbinding.progress.visibility = View.GONE
            if (it != null) {
                mbinding.parentlayout.visibility = View.VISIBLE
                mbinding.btnModify.visibility = View.VISIBLE
                mbinding.btnLayout.visibility = View.VISIBLE
                mbinding.listName.setText(it.listingname)
                mbinding.txtListname.setText(it.cat_id)
                mbinding.txtListname.text = categoryData((it.cat_id))

                val gson = Gson()
                val model1: ArrayList<PostShoppingModel> =
                    gson.fromJson(
                        it.product_details,
                        object : TypeToken<ArrayList<PostShoppingModel>>() {}.type
                    )
                buyerShopListParentAdapter.update(model1)

            } else {
                mbinding.txtNoData.visibility = View.VISIBLE
            }
        })
        //failure case
        mvmodel.mFailureGetbuyerList().observe(this, Observer {
            mbinding.txtNoData.visibility = View.VISIBLE
            mbinding.progress.visibility = View.GONE
            mbinding.btnLayout.visibility = View.GONE
           // Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast(it.message,this)

        })
//************click modify
        mbinding.btnModify.setOnClickListener {
            val intent = Intent(this, BuyerPostShopPreFillActivity::class.java)
            intent.putExtra("model", allData)
            startActivity(intent)
        }
//**************delivery details
        mbinding.deliveryDetailed.setOnClickListener {
            val intent = Intent(this, BuyerDeliveryDetailActivity::class.java)
            intent.putExtra("keyModify", allData)
            intent.putExtra("keyModifyUserDetails", buyerShopModel)
            startActivity(intent)
        }
        //delete the list
        mbinding.btnDelete.setOnClickListener {
            showDialogBtnDelete()


        }

        //sucessful of deleted list
        mvmodel.mSucessfulDeleteList().observe(this, Observer {
        //    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            progress.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
            mbinding.btnModify.visibility = View.VISIBLE
            finish()
        })

        //sucessful of deleted list
        mvmodel.mFailureDeleteList().observe(this, Observer {
          //  Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it,this)
            progress.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
            mbinding.btnModify.visibility = View.VISIBLE
        })
    }

    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.commontoolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = listName
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        //inflate menu
    }

    fun showDialogBtnDelete() {


        val mDialog = AlertDialog.Builder(this)
        mDialog.setTitle(getString(R.string.alert))
        mDialog.setMessage(getString(R.string.Are_you_sure_you_want_to_delete_list_permanetly))
        mDialog.setPositiveButton(
            getString(R.string.yes)
        ) { dialog, which ->
            progress.visibility = View.VISIBLE
            btnDelete.visibility = View.GONE
            mbinding.btnModify.visibility = View.GONE
            mvmodel.deleteShoppingList(buyerShopModel.id)
            dialog.cancel()
        }
        mDialog.setNegativeButton(
            getString(R.string.no)
        ) { dialog, which ->
            dialog.cancel()

        }
        mDialog.show()
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

    fun setAdapters() {
        val manager = LinearLayoutManager(this)
        mbinding.rvModifyShopList.layoutManager = manager
        buyerShopListParentAdapter = BuyerShopListParentAdapter(this)
        mbinding.rvModifyShopList.adapter = buyerShopListParentAdapter
    }

    //*************** Matching id's *******************//
    fun categoryData(id: String): String {
        val categoriesList = Constant.categoryData(this)
        for (category in categoriesList) {

            if (category.category_id == id.trim()) {

                return category.name
            }
        }
        return getString(R.string.mix_category_product)
    }
}