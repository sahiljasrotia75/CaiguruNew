package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.viewStoreProductFullScren

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreViewModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_seller_store.*
import kotlinx.android.synthetic.main.activity_view_store_full_screen.*
import kotlinx.android.synthetic.main.add_price_custom_dialog.view.*
import kotlinx.android.synthetic.main.store_child_adapter.view.*
import java.util.ArrayList

class ViewStoreFullScreenActivity : AppCompatActivity(),
    ViewProductFullScreenAdapter.clickInterface {
    private var adapterPosition: Int = -1
    private lateinit var adpterArray: ArrayList<PostShoppingModel>
    private lateinit var mvmodel: SellerStoreViewModel

    //   private var myArray = ArrayList<PostShoppingModel>()
    private lateinit var mAdapterStore: ViewProductFullScreenAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvmodel = ViewModelProvider(this).get(SellerStoreViewModel::class.java)

        setContentView(R.layout.activity_view_store_full_screen)
        setAdapter()
        initData()
        setClick()
        setObserver()
    }

    private fun setClick() {
        imgCross.setOnClickListener {
            finish()
        }

        btnContinue.setOnClickListener {
            val array = ArrayList<PostShoppingModel>()
            for (item in adpterArray) {
                if (item.qty.toInt() > 0) {
                    array.add(item)
                }
            }
            val intent = Intent()
            intent.putParcelableArrayListExtra("KeyGetStoreData", array)
            setResult(Activity.RESULT_OK, intent)//set the result
            finish()
        }
        setProductClick()
    }

    private fun initData() {
       // progressFullScreen.visibility = View.VISIBLE
       // parentLayout.visibility = View.GONE
        val storeApiData = intent.getParcelableExtra<FinalizeModel>("keyStoreApiData")!!
        val priceFilter = intent.getStringExtra("keyStoreFilter")!!
       // mvmodel.getsellerstore(storeApiData, priceFilter)

        adpterArray = intent.getParcelableArrayListExtra<PostShoppingModel>("KeyStoreProduct")!!
        val adapterPosition = intent.getIntExtra("KeyStoreProductPosition", 0)
         mAdapterStore.update(adpterArray)

        setProductData(adpterArray, adapterPosition)
    }

    fun setObserver() {
        //get the Load More Api
        mvmodel.mSucessfulLoadMore().observe(this, Observer {
            progressFullScreen.visibility = View.INVISIBLE
            // storeApiData.products[loadMoreAdapterPosition].page+=1
           // Log.e("arraySize", it.size.toString())
            adpterArray[0].usedProds=it.usedProds
            adpterArray.addAll(it.products)
            mAdapterStore.update(adpterArray)
        })

        mvmodel.mFailureLoadMore().observe(this, Observer {
            progressFullScreen.visibility = View.INVISIBLE
            Constant.showToast(it, this)

        })

    }


    @SuppressLint("SetTextI18n")
    private fun setProductData(myArray: ArrayList<PostShoppingModel>, Position: Int) {
        //adpterArray = myArray
        adapterPosition = Position
        Glide.with(this)
            .load(myArray[Position].image).placeholder(R.drawable.product_placeholder)
            .into(imgProduct)
//set the qty
        qty.text = myArray[Position].qty
//set the product price
        if (myArray[Position].price.isEmpty() || myArray[Position].price <= "0") {
            price.text = "$0"
        } else {
            price.text =
                "$" + (Constant.roundValue(myArray[Position].priceWithComission.toDouble()))
        }
        //add the price after calculation
        if (myArray[Position].total.isEmpty()) {
            txtPriceAddingQty.text = "$0"
        } else {
            txtPriceAddingQty.text =
                "$" + Constant.roundValue(myArray[Position].total.toDouble())
        }

        //set the product name
        txtProductname.text =
            myArray[Position].name
        txtUnit.text = getString(R.string.price_by) + " " + Constant.convertUnits(
            this,
            myArray[Position].unit
        ) + ":"
    }

    //set the subtract product click
    fun setProductClick() {
        subtractbtn.setOnClickListener {
            if (adpterArray[adapterPosition].qty.toInt() > 0) {
                adpterArray[adapterPosition].qty =
                    (adpterArray[adapterPosition].qty.toInt() - 1).toString()
                adpterArray[adapterPosition].total =
                    Total(adpterArray[adapterPosition]).toString()//set the total
                //  adpterArray[adapterPosition].LocalTotal = AllTotal(adpterArray).toString()//set the total
                // adpterArray[adapterPosition].partialComission = setTotalPartialComission(adpterArray).toString()//partial payments

                setProductData(adpterArray, adapterPosition)
            }

        }

        //set the add product click
        addBtn.setOnClickListener {
            adpterArray[adapterPosition].qty =
                (adpterArray[adapterPosition].qty.toInt() + 1).toString()
            adpterArray[adapterPosition].total =
                Total(adpterArray[adapterPosition]).toString()//set the total
            //  adpterArray[adapterPosition].LocalTotal = AllTotal(adpterArray).toString()//set the total
            //  adpterArray[adapterPosition].partialComission = setTotalPartialComission(adpterArray).toString()//
            setProductData(adpterArray, adapterPosition)
        }

        //set the click of custom qty
        qty.setOnClickListener {

            addPriceCustomDialog()
        }
    }

    // add price custom dialog
    private fun addPriceCustomDialog(

    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.add_price_custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(view)
            .create()
        //prefilling
        view.productheading.text = this.getString(R.string.add_qty)
        view.edtPrice.hint = this.getString(R.string.enter_your_qty)
        view.edtPrice.setInputType((InputType.TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_FLAG_DECIMAL))

        if (adpterArray[adapterPosition].qty.toInt() <= 0) {
            view.edtPrice.setText("")
        } else {
            view.edtPrice.setText(adpterArray[adapterPosition].qty)
        }

        mBuilder.show()
        //set the click on the button
        view.btnAddPrice.setOnClickListener {
            if (view.edtPrice.text.isEmpty()) {
                Toast.makeText(
                    this,
                    this.getString(R.string.Please_add_product_price),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (view.edtPrice.text.toString() == "0") {
                Toast.makeText(
                    this,
                    this.getString(R.string.qty_nust_be_greater_than_zero),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                adpterArray[adapterPosition].qty = view.edtPrice.text.toString()
                adpterArray[adapterPosition].total =
                    Total(adpterArray[adapterPosition]).toString()//set the total
                setProductData(adpterArray, adapterPosition)
                mBuilder.dismiss()
            }
        }

    }

    private fun setAdapter() {
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerProduct.layoutManager = manager
        mAdapterStore = ViewProductFullScreenAdapter(this)
        recyclerProduct.adapter = mAdapterStore
    }

    override fun openImagesInterfaces(list: ArrayList<PostShoppingModel>, position: Int) {

        setProductData(list, position)
    }

    override fun LoadMoreClick(data: ShoppingListModel, position: Int, usedProds: String) {
        val storeApiData = intent.getParcelableExtra<FinalizeModel>("keyStoreApiData")!!
        val priceFilter = intent.getStringExtra("keyStoreFilter")!!
        data.page += 1
        data.usedProds=usedProds
        mvmodel.LoadMoreData(data, storeApiData, priceFilter)
        progressFullScreen.visibility = View.VISIBLE
    }

    private fun Total(list: PostShoppingModel): Double {
        var total = 0.0

        total += list.priceWithComission.toDouble() * list.qty.toDouble()
        return Constant.roundValue(total).toDouble()

    }

//    private fun AllTotal(list: ArrayList<PostShoppingModel>): Double {
//        var total = 0.0
//        for (item in list) {
//
//            if (total == 0.0) {
//                total = item.priceWithComission.toDouble() * item.qty.toDouble()
//            } else {
//                total += item.priceWithComission.toDouble() * item.qty.toDouble()
//            }
//        }
//        return Constant.roundValue(total).toDouble()
//
//    }
//
//    private fun setTotalPartialComission(data: ArrayList<PostShoppingModel>): Double {
//        //set the  total comission of the products
//        var totalPatialComission = 0.0
//        for (item in data) {
//            if (totalPatialComission == 0.0) {
//                val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
//                totalPatialComission = allTotal * item.qty.toDouble()
//            } else {
//                val allTotal = item.priceWithComission.toDouble() - item.price.toDouble()
//                totalPatialComission += allTotal * item.qty.toDouble()
//            }
//        }
//        return totalPatialComission
//    }

}