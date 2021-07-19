package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.dragDropProduct

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import kotlinx.android.synthetic.main.activity_drag_drop_product.*

class DragDropProductActivity : AppCompatActivity() {
    private lateinit var text: TextView
    private lateinit var mAdapter: DragDropAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_drop_product)
        SettingUpToolbar()
        setAdapter()
        setClick()
    }

    private fun setClick() {
        btnDragDrop.setOnClickListener {
            val intent = Intent()
            intent.putParcelableArrayListExtra(
                "KeyDragDropData",
                mAdapter.getAllShoppingArrayData()
            )
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()

        }

    }

    //parentSavedPrdctRecycler adapter
    private fun setAdapter() {
        val array = intent.getParcelableArrayListExtra<PostShoppingModel>("KeyDragDropData")!!
        val manager = LinearLayoutManager(this)
        recyclerDragDrop.layoutManager = manager
        mAdapter = DragDropAdapter(this)
        recyclerDragDrop.adapter = mAdapter
        val callback: ItemTouchHelper.Callback = ItemMoveCallback(mAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerDragDrop)
        mAdapter.update(array)
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.chnage_order)
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
}