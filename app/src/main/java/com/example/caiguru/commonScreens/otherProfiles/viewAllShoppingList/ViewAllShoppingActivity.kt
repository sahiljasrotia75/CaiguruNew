package com.example.caiguru.commonScreens.otherProfiles.viewAllShoppingList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.homeSeller.GetProfileModel
import kotlinx.android.synthetic.main.activity_view_all_shopping.*

class ViewAllShoppingActivity : AppCompatActivity() {

    private lateinit var adapter: ViewAllShoppingListParentAdapter
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_shopping)
        SettingUpToolbar()
        setAdapter()
        val dataModel = intent.getParcelableExtra<GetProfileModel>("keyViewAllData")!!
        if (dataModel != null) {
            adapter.update(dataModel.lists)
        }

    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Shopping_lists)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
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

    private fun setAdapter() {
        val manager = LinearLayoutManager(this)
        adapter = ViewAllShoppingListParentAdapter(this)
        ViewAllRecycler.layoutManager = manager
        ViewAllRecycler.adapter = adapter
    }
}
