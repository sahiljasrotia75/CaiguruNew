package com.example.caiguru.buyer.postList.shoppingListPosted

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.commonScreens.dashBoardParentActivity.DashBoardBuyerActivity
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityBuyerShopPostBinding
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.WebServicesShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class BuyerShoppingListPostedActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityBuyerShopPostBinding
    private var listingname: String = ""
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_buyer_shop_post)
        SettingUpToolbar()

        val shoppingmodel =
            intent.getParcelableArrayListExtra<WebServicesShoppingModel>("keyshopping")!!

        val arraymodel = ArrayList<ShoppingListModel>()

        for (item in shoppingmodel) {
            val model = ShoppingListModel()
            model.listingname = item.listingname
            listingname = model.listingname
            //  model.image = item.image
            model.listingname = item.listingname
            model.name = item.name
            //  model.price = item.price
            model.qty = item.qty
            model.unit = item.unit
            arraymodel.add(model)


        }
        mBinding.listname.text = listingname
        //set the adapter
        val layoutManager = LinearLayoutManager(this)
        mBinding.recyclershopping.layoutManager = layoutManager
        mBinding.recyclershopping.adapter =
            BuyerShopListPostAdapter(
                this,
                arraymodel
            )


        //set the click on the button
        mBinding.btnnext.setOnClickListener {
            val intent = Intent(this, DashBoardBuyerActivity::class.java)
            startActivity(intent)
            finish()
//            var intent=Intent()
//            setResult(Activity.RESULT_OK,intent)
//            finish()
        }

    }


    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        //getting toolbar id
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Shopping_list_posted)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, DashBoardBuyerActivity::class.java)
        setResult(1000, intent)
        finish()
    }
}