package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.AddAddressModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerViewDetails.BuyerDeliveryDetailsActivity
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.seller.sellerSetting.sellerBuyCredits.SellerBuyCreditsActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.activity_buyer_home_view_detail.*
import kotlinx.android.synthetic.main.choose_sellershopping_list_parent_adapter.view.*
import kotlinx.android.synthetic.main.refute_reason_dialog.*
import kotlinx.android.synthetic.main.show_credits_dialog.*
import kotlinx.android.synthetic.main.switch_dialog.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


class BuyerHomeViewActivity : AppCompatActivity(), HomeViewProductsAdapter.setTotalInterface {

    private var addressData = AddAddressModel()
    private var data = HomeModel()
    private var partialComission: String = ""
    private var cashOnDelivery: String = ""
    private var totalPriced: Double = 0.0
    private var allGlobalData = ViewDetailModel()
    private lateinit var adapterView: HomeViewProductsAdapter
    private lateinit var text: TextView
    private lateinit var viewModel: HomeViewViewModel
    private lateinit var dialog: Dialog
    private lateinit var headingTextCashOnDelivery: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer_home_view_detail)
        // viewModel = ViewModelProviders.of(this)[HomeViewViewModel::class.java]
        viewModel = ViewModelProvider(this).get(HomeViewViewModel::class.java)
        SettingUpToolbar()
        setUserViewAdapterAdapter()
        headingTextCashOnDelivery = findViewById(R.id.cashOnDelivery)
        data = intent.getParcelableExtra<HomeModel>("keyViewDetail")!!
        addressData = intent.getParcelableExtra<AddAddressModel>("keyViewDetailAddress")!!
        //*******************buyer_feed_view api****************//
        viewModel.getFeedview(data, addressData)
        progress.visibility = View.VISIBLE
        feedViewObserver()
        deliveryDetailClick()
        reportListClick()
        purchaseListClick()
        purchaseListObserver()
        initData()
        setClicks()
        //***********set the click on the share
        share.setOnClickListener {
//            var profileData = GetProfileModel()
//            val gson = Gson()
//            val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//            if (json.isNotEmpty()) {
//                profileData = gson.fromJson(json, GetProfileModel::class.java)
//            }
            var allText = ""
            for (item in allGlobalData.product_details) {
                if (allText.isEmpty()) {
                    allText =
                        "${getString(R.string.namesShare)}: ${item.name}, ${getString(R.string.Unit)}: ${item.unit}, ${getString(
                            R.string.Price
                        )}: $${item.priceWithComission}"
                } else {
                    allText =
                        "${allText}, \"${getString(R.string.namesShare)}: ${item.name}, ${getString(
                            R.string.Unit
                        )}: ${item.unit} ${getString(
                            R.string.Price
                        )}: $${item.priceWithComission}\""
                }
                val message =
                    "${getString(R.string.share_List_text)}\n$allText\n${getString(R.string.ShareLinkText)} ${Constant.getProfileData(
                        this
                    ).sharelink}"
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_TEXT, message)
                startActivityForResult(Intent.createChooser(share, getString(R.string.Share)), 1)
            }
        }

        if (data.is_processing == "1") {
            PurchaseButton.visibility = View.GONE
        } else {
            PurchaseButton.visibility = View.VISIBLE
        }
        ViewShoppingRecycler.setFocusableInTouchMode(false)
        ViewShoppingRecycler.setFocusable(false)

        //*************************report list reponse
        //sucessfull
        viewModel.mSucessfulREportList().observe(this, Observer {
            //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            dialog.waitBtn.visibility = View.GONE
            dialog.SubmitBtn.visibility = View.VISIBLE
            dialog.dismiss()
        })
        //failure
        viewModel.mFailureReposrtlist().observe(this, Observer {
            //   Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            dialog.waitBtn.visibility = View.GONE
            dialog.SubmitBtn.visibility = View.VISIBLE
            dialog.dismiss()
        })
        //set the total credits
        try {
            val credits = Constant.getProfileData(this).credits.toDouble().toInt().toString()
            txtTotalWalletCredit.text =
                "(${getString(R.string.credit_in_account)} ${credits} ${this.getString(
                    R.string.cr
                )})"

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setClicks() {
        ///set the click on info  credits
//        txtTotalWalletCredit.txtTotalWalletCredit.setOnClickListener {
//            Constant.customDialogShowTotalCredits(this)
//
//        }
        //****************set the click on cash on delivery
        headingTextCashOnDelivery.cashOnDelivery.setOnClickListener {
            Constant.cashOnDeliveryDialog(this)
        }
    }

    private fun initData() {
        PartialComission.text =
            "${getString(R.string.partial_payment)} ${getString(R.string.partial_money)}"
        val img = getResources().getDrawable(R.drawable.ic_baseline_info_24)
//        txtTotalWalletCredit.setCompoundDrawablesWithIntrinsicBounds(
//            null,
//            null,
//            img,
//            null
//        )
        //************cashn on delivery info****************//
        headingTextCashOnDelivery.cashOnDelivery.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            img,
            null
        )
    }

    //***********purchase observer
    private fun purchaseListObserver() {
        val data = intent.getParcelableExtra<HomeModel>("keyViewDetail")
        PleaseWaitButton.visibility = View.INVISIBLE


        //sucessful
        viewModel.mSucessfulBuyList().observe(this, Observer {
            Constant.apiHitOrNot = 0
            PleaseWaitButton.visibility = View.INVISIBLE
            if (data!!.is_processing == "1") {
                PurchaseButton.visibility = View.GONE
            } else {
                PurchaseButton.visibility = View.VISIBLE
            }
//            Toast.makeText(this, getString(R.string.purchased_sucessfully), Toast.LENGTH_SHORT)
//                .show()

            Constant.showToast(getString(R.string.purchased_sucessfully), this)
            finish()

        })
        //failure
        viewModel.mErrorBuyList().observe(this, Observer {
            if (data!!.is_processing == "1") {
                PurchaseButton.visibility = View.GONE
            } else {
                PurchaseButton.visibility = View.VISIBLE

            }
            PleaseWaitButton.visibility = View.INVISIBLE
            //      Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)

        })
    }

    private fun purchaseListClick() {
        PurchaseButton.setOnClickListener {

            val credits = Constant.getProfileData(this).credits.toDouble()
            if (totalPriced <= 0) {

                Constant.showToast(getString(R.string.Please_Select_Quantity), this)
            } else if (totalPriced < allGlobalData.minimum_purchase_amount.toDouble()) {

                Constant.showToast(
                    getString(R.string.Total_should_be_high_than_Minimum_order_amount),
                    this
                )
            } else if (credits < partialComission.toDouble()) {

                creditDialog(partialComission)
            } else {
                dialog = Dialog(this)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.switch_dialog)
                //default textShow
                dialog.txtHeader.text = getString(R.string.alert)
                dialog.txtMessage.text = getString(R.string.purchase_shopping_alert)
                dialog.yes.text = getString(R.string.buy)
                dialog.no.text = getString(R.string.cancel)
                dialog.show()
                //hit the api on click
                dialog.yes.setOnClickListener {
                    viewModel.purchaseList(
                        getJsonProdcutStringArray(),
                        getAddressJSonFormat(),
                        allGlobalData,

                        partialComission.toDouble()
                    )
                    PleaseWaitButton.visibility = View.VISIBLE
                    PurchaseButton.visibility = View.INVISIBLE
                    dialog.dismiss()

                }
                //no click
                dialog.no.setOnClickListener {
                    dialog.dismiss()
                }

            }
        }


    }

    private fun getAddressJSonFormat(): String {
        val jsonobject = JSONObject()
        if (allGlobalData.delivery_type == "1") {
            jsonobject.put("lat", allGlobalData.pickup_details.lat)
            jsonobject.put("long", allGlobalData.pickup_details.long)
            jsonobject.put("full_address", allGlobalData.pickup_details.address)

        } else {
            jsonobject.put("lat", allGlobalData.profile_lat)
            jsonobject.put("long", allGlobalData.profile_long)
            jsonobject.put("full_address", allGlobalData.profile_address)
        }
        return jsonobject.toString()
    }

    //single purchase
    private fun getJsonProdcutStringArray(
    ): String {

        val parentArray = JSONArray()
        val productArray = JSONArray()
        val jsonObj = JSONObject()
        val arraydata = adapterView.getAllData()

        for (i in 0 until arraydata.size) {
            val jsonObj = JSONObject()
            jsonObj.put("id", arraydata[i].id)
            jsonObj.put("qty", arraydata[i].qty)
            jsonObj.put("price",  arraydata[i].price)
            jsonObj.put("priceWithComission", arraydata[i].priceWithComission)
            jsonObj.put("name",  arraydata[i].name)
            jsonObj.put("image", arraydata[i].image)
            jsonObj.put("unit",  arraydata[i].unit)
            productArray.put(jsonObj)
        }

        jsonObj.put("products", productArray)
        jsonObj.put("list_id", allGlobalData.id)
        jsonObj.put("delivery_type", allGlobalData.delivery_type)
        jsonObj.put("amount", totalPriced)
        // jsonObj.put("credits", setComission(arraydata))
        jsonObj.put("credits", partialComission)
        return parentArray.put(jsonObj).toString()
    }

    private fun deliveryDetailClick() {

        deliveryDetailed.setOnClickListener {
            val intent = Intent(this, BuyerDeliveryDetailsActivity::class.java)
            allGlobalData.distance = data.distance
            intent.putExtra("keyDeliveryDetail", this.allGlobalData)
            startActivity(intent)
        }
    }

    private fun reportListClick() {
        txtReportList.setOnClickListener {
            val mDialog = AlertDialog.Builder(this)
            mDialog.setTitle(getString(R.string.alert))
            mDialog.setCancelable(true)
            mDialog.setMessage(getString(R.string.report_text3))
            mDialog.setPositiveButton(
                getString(R.string.ok)
            ) { dialog, which ->
                openReasonPopup()
                dialog.cancel()
            }
            mDialog.setNegativeButton(
                getString(R.string.cancel)
            ) { dialog, which ->
                dialog.cancel()
            }
            mDialog.show()
        }
    }

    private fun openReasonPopup() {
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.refute_reason_dialog)
        dialog.edtReason.hint = getString(R.string.enter_reason)
        dialog.show()
        dialog.edtReason.filters = arrayOf<InputFilter>(
            HideEmoji(this)
        )

        dialog.SubmitBtn.setOnClickListener {
            if (dialog.edtReason.text.isEmpty()) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.enter_reason),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
                Constant.showToast(getString(R.string.enter_reason), this)

            } else {
                viewModel.reportList(dialog.edtReason.text.toString().trim(), data)
                dialog.waitBtn.visibility = View.VISIBLE
                dialog.SubmitBtn.visibility = View.GONE
            }

        }

    }

    private fun feedViewObserver() {
        viewModel.mSuccessfulViewDetail().observe(this, Observer {
            progress.visibility = View.GONE
            if (it == null) {
                nodataView.visibility = View.VISIBLE
                parentLayouts.visibility = View.GONE
            } else {
                this.allGlobalData = it
                setData(it)//set the data in activity

                setButtonPurchaseVisibility(it)
                adapterView.update(
                    it.product_details,
                    it.is_buy_btn_show,
                    it.comission_per
                )//set the adapter
                parentLayouts.visibility = View.VISIBLE
                nodataView.visibility = View.GONE
            }
        })

        viewModel.mError().observe(this, Observer {
            //Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Constant.showToast(it, this)
            nodataView.visibility = View.VISIBLE
            parentLayouts.visibility = View.GONE
            progress.visibility = View.GONE
        })
    }

    private fun setButtonPurchaseVisibility(it: ViewDetailModel) {
//1 for visible button other for invisible
        if (it.is_buy_btn_show == "1") {
            totalLayout.visibility = View.VISIBLE
            deliveryDetailsLayout.visibility = View.VISIBLE
            //belowLayout.visibility = View.VISIBLE
            //    PurchaseButton.visibility = View.VISIBLE
            minimuorderText.visibility = View.VISIBLE
            if (data.is_processing == "1") {
                PurchaseButton.visibility = View.GONE
            } else {
                PurchaseButton.visibility = View.VISIBLE
            }
        } else {
            totalLayout.visibility = View.GONE
            deliveryDetailsLayout.visibility = View.GONE
            // belowLayout.visibility = View.GONE
            PurchaseButton.visibility = View.GONE
            minimuorderText.visibility = View.GONE
        }

        //  val data = intent.getParcelableExtra<HomeModel>("keyViewDetail")


    }

    @SuppressLint("SetTextI18n")
    private fun setData(it: ViewDetailModel) {
        listName.text = it.listingname
        txtListname.text = CategoryId(it.cat_id)

        if (cashOnDelivery.isEmpty()) {
            txtCashOnDelivery.text = "$" + "0.0"
        } else {
            txtCashOnDelivery.text =
                "$" + cashOnDelivery
        }
        //partial payments
        if (partialComission.isEmpty()) {
            txtPartialpayments.text =
                "0.0" + getString(R.string.cr)
        } else {
            txtPartialpayments.text =
                partialComission + getString(R.string.cr)
        }
        priced.text = "$0.00"
        val mystring = getString(R.string.Delivery_Details)
        val content = SpannableString(mystring)
        content.setSpan(UnderlineSpan(), 0, mystring.length, 0)
        deliveryDetailed.setText(content)

        //set the span text report text
        val txtReport = getString(R.string.report_list)
        val reportSpannable = SpannableString(txtReport)
        reportSpannable.setSpan(UnderlineSpan(), 0, txtReport.length, 0)
        txtReportList.setText(reportSpannable)

        minimuorderText.text =
            "${getString(R.string.minimum_order_msg)} $${Constant.roundValue(it.minimum_purchase_amount.toDouble())}"
    }

    private fun CategoryId(catId: String): String {
        val category = Constant.categoryData(this)
        for (item in category) {
            if (item.category_id == catId) {
                return item.name
            }
        }
        return getString(R.string.mix_category_product)
    }

    private fun setUserViewAdapterAdapter() {
        val manager = LinearLayoutManager(this)
        adapterView = HomeViewProductsAdapter(this)
        ViewShoppingRecycler.layoutManager = manager
        ViewShoppingRecycler.adapter = adapterView
    }

    //.........setuptool bar..............//
    private fun SettingUpToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        title = ""
        setSupportActionBar(toolbar)
        //set the text
        text = findViewById(R.id.toolbartittle)
        text.text = getText(R.string.View_Shopping_List)
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

    @SuppressLint("SetTextI18n")
    override fun setTotal(
        list: ArrayList<PostShoppingModel>,
        position: Int
    ) {
        partialComission =
            Constant.roundValue(list[position].partialComission.toDouble())//partial payments
        //partial payments
        if (partialComission.isEmpty()) {
            txtPartialpayments.text =
                "0.0" + getString(R.string.cr)
        } else {
            txtPartialpayments.text =
                partialComission + getString(R.string.cr)
        }
        allGlobalData.product_details = list
        //cash on delivery
        val cashOnDeliverys =
            list[position].total.toDouble() - list[position].partialComission.toDouble()
        cashOnDelivery = Constant.roundValue(cashOnDeliverys)
        if (cashOnDelivery.isEmpty()) {
            txtCashOnDelivery.text = "$" + "0.0"
        } else {
            txtCashOnDelivery.text =
                "$" + cashOnDelivery
        }
        if (list[position].total.isEmpty()) {
            priced.text = "$0.00"
        } else {
            totalPriced = list[position].total.toDouble()
            priced.text = "$" + list[position].total
        }

    }

    private fun creditDialog(total_credits: String) {
//        var model = GetProfileModel()
//        val gson = Gson()
//        val json = Constant.getPrefs(this).getString(Constant.PROFILE, "")
//        model = gson.fromJson(json, GetProfileModel::class.java)

        // val credits = Constant.getProfileData(this).credits.toDouble()

        val newCredits = total_credits.toDouble() - Constant.getProfileData(this).credits.toDouble()
        dialog = Dialog(this)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.show_credits_dialog)
        dialog.show()


        dialog.totalCredit.text =
            "${getString(R.string.you_dont_have_credits)}  ${Constant.roundValue((newCredits))}  ${getString(
                R.string.additional_credits
            )}"

        //set the click on  button
        dialog.buyCredit.setOnClickListener {
            val intent = Intent(this, SellerBuyCreditsActivity::class.java)
            intent.putExtra("profileKey", Constant.getProfileData(this))
            startActivity(intent)
            dialog.dismiss()
        }

    }


}
