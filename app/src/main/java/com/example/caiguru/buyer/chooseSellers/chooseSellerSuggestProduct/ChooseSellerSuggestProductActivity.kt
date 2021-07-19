package com.example.caiguru.buyer.chooseSellers.chooseSellerSuggestProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.BuyerMyOrderActivity
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.commonScreens.otherProfiles.otherProfile.OtherProfileViewActivity
import com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.SellerStoreActivity
import com.example.caiguru.databinding.ActivityChooseSellerSuggestProductBinding
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.SellerLevelModel
import kotlinx.android.synthetic.main.activity_choose_seller_suggest_product.*
import kotlinx.android.synthetic.main.suggest_product_for_next_time_adapter.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import org.json.JSONArray


class ChooseSellerSuggestProductActivity : AppCompatActivity() {

    private lateinit var req_ids: String
    //private lateinit var SugestedProductadapter: SuggestProductForNextTimeAdapter
    private lateinit var mbinding: ActivityChooseSellerSuggestProductBinding
    private lateinit var mvmodel: SellerSuggestedProductViewModel
    private lateinit var text: TextView
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding =
            DataBindingUtil.setContentView(this, R.layout.activity_choose_seller_suggest_product)


        if (intent.hasExtra("SellerDetailSingleList")) {
            val SellerDetailSingleList =
                intent.getParcelableExtra<PayCreditsResultModel>("SellerDetailSingleList")!!
            req_ids = SellerDetailSingleList.req_ids
            mbinding.txtheading.text =
                "${getString(R.string.suggest_max_product_to)} ${SellerDetailSingleList.seller_name} ${getString(
                    R.string.for_next_time
                )}"

            if (SellerDetailSingleList.seller_level.isEmpty()) {
                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.imgBatch)
            } else {
                Glide.with(this)
                    .load(levelget(SellerDetailSingleList.seller_level).levelImage)
                    .into(mbinding.imgBatch)
            }

            if (SellerDetailSingleList.seller_image == null || SellerDetailSingleList.seller_image.isEmpty()) {

                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.img)
            } else {


                Glide.with(this)
                    .load(SellerDetailSingleList.seller_image)
                    .into(mbinding.img)
            }

            mbinding.username.text = SellerDetailSingleList.seller_name

            mbinding.txtreputation.text =
                "${getString(R.string.reputation)} ${Constant.getReputationString(
                    this,
                    SellerDetailSingleList.seller_reputation
                )}"
            //open the profile
            relativeImage.setOnClickListener {

                val localModel = FinalizeModel()
                localModel.seller_id=SellerDetailSingleList.seller_id
                localModel.cat_id=""
                localModel.seller_name=SellerDetailSingleList.seller_name
                val intent = Intent(this, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                startActivity(intent)
//                val model = CustomerChildModel()
//                model.seller_id = SellerDetailSingleList.seller_id
//                model.name = SellerDetailSingleList.seller_name
//                val intent = Intent(this, OtherProfileViewActivity::class.java)
//                intent.putExtra("keyChosseSellerProfileKey", model)
//                startActivity(intent)
            }

            username.setOnClickListener {
                val localModel = FinalizeModel()
                localModel.seller_id=SellerDetailSingleList.seller_id
                localModel.cat_id=""
                localModel.seller_name=SellerDetailSingleList.seller_name
                val intent = Intent(this, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                startActivity(intent)


//                val model = CustomerChildModel()
//                model.seller_id = SellerDetailSingleList.seller_id
//                model.name = SellerDetailSingleList.seller_name
//                val intent = Intent(this, OtherProfileViewActivity::class.java)
//                intent.putExtra("keyChosseSellerProfileKey", model)
//                startActivity(intent)

            }


        } else {
            val SellerDetailsMultipleList =
                intent.getParcelableExtra<PayCreditsResultModel>("SellerDetailsMultipleList")

            req_ids = SellerDetailsMultipleList!!.req_ids

            mbinding.txtheading.text =
                "${getString(R.string.suggest_max_product_to)} ${SellerDetailsMultipleList.seller_name} ${getString(
                    R.string.for_next_time
                )}"

            if (SellerDetailsMultipleList.seller_level.isEmpty()) {

                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.imgBatch)
            } else {
                Glide.with(this)
                    .load(levelget(SellerDetailsMultipleList.seller_level).levelImage)
                    .into(mbinding.imgBatch)
            }

            if (SellerDetailsMultipleList.seller_image == null || SellerDetailsMultipleList.seller_image.isEmpty()) {

                Glide.with(this)
                    .load(R.drawable.user_placeholder)
                    .into(mbinding.img)
            } else {


                Glide.with(this)
                    .load(SellerDetailsMultipleList.seller_image)
                    .into(mbinding.img)
            }

            mbinding.username.text = SellerDetailsMultipleList.seller_name
            mbinding.txtreputation.text =
                "${getString(R.string.reputation)} ${Constant.getReputationString(
                    this,
                    SellerDetailsMultipleList.seller_reputation
                )}"
            //open the profile
            relativeImage.setOnClickListener {
                val localModel = FinalizeModel()
                localModel.seller_id=SellerDetailsMultipleList.req_ids
                localModel.cat_id=""
                localModel.seller_name=SellerDetailsMultipleList.seller_name
                val intent = Intent(this, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                startActivity(intent)


//                val model = CustomerChildModel()
//                model.seller_id = SellerDetailsMultipleList.req_ids
//                model.name = SellerDetailsMultipleList.seller_name
//                val intent = Intent(this, OtherProfileViewActivity::class.java)
//                intent.putExtra("keyChosseSellerProfileKey", model)
//                startActivity(intent)

            }

            username.setOnClickListener {
                val localModel = FinalizeModel()
                localModel.seller_id=SellerDetailsMultipleList.req_ids
                localModel.cat_id=""
                localModel.seller_name=SellerDetailsMultipleList.seller_name
                val intent = Intent(this, SellerStoreActivity::class.java)
                intent.putExtra("keyOpenSellerStore",localModel)
                startActivity(intent)


//                val model = CustomerChildModel()
//                model.seller_id = SellerDetailsMultipleList.req_ids
//                model.name = SellerDetailsMultipleList.seller_name
//                val intent = Intent(this, OtherProfileViewActivity::class.java)
//                intent.putExtra("keyChosseSellerProfileKey", model)
//                startActivity(intent)

            }

        }

       // mvmodel = ViewModelProviders.of(this)[SellerSuggestedProductViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(SellerSuggestedProductViewModel::class.java)
        //setAdapterSuggestedProduct()
        SettingUpToolbar()
        //set the click on athe add button
        mbinding.addlayout.setOnClickListener {
            val data = SuggestedProductModel()
            val View = layoutInflater.inflate(R.layout.suggest_product_for_next_time_adapter, null)

            if (addProductLayout.childCount > 4) {
//                Toast.makeText(this, R.string.Add_product_more_than_5, Toast.LENGTH_SHORT)
//                    .show()
                Constant.showToast( getString(R.string.Add_product_more_than_5),this)
            } else {

                addProductLayout.addView(View)
                data.position = getString(R.string.Products)
                View.productname.text = data.position + addProductLayout.childCount
                //  data.suggestedProductName = View.edtaddProduct.text.toString().trim()
                //textArray.add(data)
                //delete the View
                View.imgdeleteData.setOnClickListener {
                    addProductdata(addProductLayout.childCount)
                    addProductLayout.removeView(View)
                }
            }
        }
        mbinding.btnSend.setOnClickListener {
           val suggest_products = JSONArray()
            for (i in 0 until addProductLayout.childCount) {
                val data = SuggestedProductModel()
                val item: ViewGroup = addProductLayout.getChildAt(i) as ViewGroup
                val edtaddProduct = item.findViewById(R.id.edtaddProduct) as EditText
                val productname = item.findViewById(R.id.productname) as TextView

                if (edtaddProduct.text.isEmpty()) {
//                    return@setOnClickListener Toast.makeText(
//                        this,
//                        getString(R.string.product_text) + " " + productname.text.toString(),
//                        Toast.LENGTH_SHORT
//                    )
//                        .show()

                  return@setOnClickListener  Constant.showToast( getString(R.string.product_text),this)
                } else {
                    data.position = productname.text.toString().trim()
                    data.suggestedProductName = edtaddProduct.text.toString().trim()
                    suggest_products.put(data.suggestedProductName)

                }
            }
            if (suggest_products.length()<=0) {
//                Toast.makeText(this, getString(R.string.Please_add_product), Toast.LENGTH_SHORT)
//                    .show()
                Constant.showToast( getString(R.string.Please_add_product),this)
                return@setOnClickListener
            } else {
                mvmodel.suggest_products(req_ids, suggest_products.toString())
                showLoader()
            }
//                for (i in 0 until addProductLayout.childCount) {
//                    val data = SuggestedProductModel()
//                    val item: ViewGroup = addProductLayout.getChildAt(i) as ViewGroup
//                    val edtaddProduct = item.findViewById(R.id.edtaddProduct) as EditText
//                    val productname = item.findViewById(R.id.productname) as TextView
//
//                    if (edtaddProduct.text.isEmpty()) {
//                        return@setOnClickListener Toast.makeText(
//                            this,
//                            getString(R.string.product_text) + " " + productname.text.toString(),
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    } else {
//                        data.position = productname.text.toString().trim()
//                        data.suggestedProductName = edtaddProduct.text.toString().trim()
//                        suggest_products.put(data.suggestedProductName)
//
//                    }
//                }
//                mvmodel.suggest_products(req_ids, suggest_products.toString())
//                showLoader()
//
//            }


        }


        // ***********************************suggest_products reponse******************************8*******//

        mvmodel.getsellerStatus().observe(this, Observer {
            if (it != null) {
                hideLoader()
                val intent = Intent(this, BuyerMyOrderActivity::class.java)
                startActivity(intent)
                finish()
            }

        })

        mvmodel.errorgetStatus().observe(this, Observer {
            //hideLoader()
       //     Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            Constant.showToast( it.message,this)
            finish()
        })

        //*********************************************************************************************88//

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

//    private fun setAdapterSuggestedProduct() {
//        val manager = LinearLayoutManager(this)
//        SugestedProductadapter = SuggestProductForNextTimeAdapter(this)
//        mbinding.recycler.layoutManager = manager
//        mbinding.recycler.adapter = SugestedProductadapter
//    }

//
//    override fun deleteData(
//        mData: ArrayList<SuggestedProductModel>,
//        position: Int
//    ) {
//        if (mData.size > 0) {
//            mData.removeAt(position)
//        }
//        addProductdata(mData)
//    }

    private fun addProductdata(data: Int) {
        if (data > 0) {
            mbinding.txtaddmore.text = getString(R.string.Add_more_product)
        } else {
            mbinding.txtaddmore.text = getString(R.string.Add_product)
        }

    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.Suggest_Product)
        //  supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back_icon)
        toolbar.skip.visibility = View.VISIBLE
        toolbar.skip.setOnClickListener {
            //      Constant.getPrefs(this).edit().putString("finish", "yes").apply()
            val intent = Intent(this, BuyerMyOrderActivity::class.java)
            startActivity(intent)
            finish()
        }
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

    fun showLoader() {
        mbinding.btnSend.visibility = View.GONE
        mbinding.btnPleaseWait.visibility = View.VISIBLE
    }

    fun hideLoader() {
        mbinding.btnSend.visibility = View.VISIBLE
        mbinding.btnPleaseWait.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        Constant.getPrefs(this).edit().putString("finish", "yes").apply()
    }

}
