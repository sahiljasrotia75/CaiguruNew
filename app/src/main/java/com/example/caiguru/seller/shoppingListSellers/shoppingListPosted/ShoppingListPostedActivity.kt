package com.example.caiguru.seller.shoppingListSellers.shoppingListPosted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.databinding.ActivityShoppingListPostedBinding
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.WebServicesShoppingModel

class ShoppingListPostedActivity : AppCompatActivity() {


    private lateinit var listingname: String
    private lateinit var text: TextView
    private lateinit var mbinding: ActivityShoppingListPostedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_list_posted)
        SettingUpToolbar()
        val shoppingmodel = intent.getParcelableArrayListExtra<WebServicesShoppingModel>("keyshopping")!!

        val arraymodel = ArrayList<ShoppingListModel>()

        for (item in shoppingmodel) {
            val model = ShoppingListModel()
            model.listingname = item.listingname
            listingname = model.listingname
            model.image = item.image
            model.listingname = item.listingname
            model.name = item.name
            model.price = item.price
            model.unit = item.unit
            model.priceWithComission = item.priceWithComission
            arraymodel.add(model)
        }
        mbinding.listname.text = listingname
        //set the adapter
        val layoutManager = LinearLayoutManager(this)
        mbinding.recyclershopping.layoutManager = layoutManager
        mbinding.recyclershopping.adapter = ShoppingListPostedAdapter(this, arraymodel)

        //set the click on the button
        mbinding.btnnext.setOnClickListener {
            finish()
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

}
