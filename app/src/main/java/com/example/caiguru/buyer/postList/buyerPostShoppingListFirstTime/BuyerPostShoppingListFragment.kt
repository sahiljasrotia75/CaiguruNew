package com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.BuyerAddressMapBoxActivity
import com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis.OrderDetailsViewModel
import com.example.caiguru.commonScreens.hideEmoji.HideEmoji
import com.example.caiguru.databinding.BuyerPostshoplistFragmentBinding
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.sellerChooseCategory.SellerChooseCategoryActivity
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.DaysParentModel
import com.example.caiguru.seller.sellerSelectedDaysAndTime.SellerSelectDaysAndTimeActivity
import com.google.gson.Gson
import constant_Webservices.Constant
import kotlinx.android.synthetic.main.buyer_add_more_product_dialog.*
import kotlinx.android.synthetic.main.buyer_postshoplist_fragment.*
import kotlinx.android.synthetic.main.buyer_postshoplist_fragment.txtcategory
import kotlinx.android.synthetic.main.buyer_postshoplist_fragment.txtname
import kotlinx.android.synthetic.main.seller_add_more_product_dialog.btnAdd
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class BuyerPostShoppingListFragment : Fragment(),
    PostShoppingBuyerAdapter.editDataInterface {


    var freeProducts = 0
    private var buyerLevel: String = ""
    private var creditPerProduct: String = ""
    private var buyerCredits: String = ""
    //global model
    private var deliveryTimeList = ArrayList<DaysParentModel>()//days model
    private var addAddressModel = DeliveryZoneModel()
    var arrayAddAddressModel = ArrayList<DeliveryZoneModel>()//location model
    private var adapterShoppingPosition: Int = -1
    private lateinit var dialog: Dialog
    lateinit var mvmodel: BuyerPostViewModel
    var adapter: PostShoppingBuyerAdapter? = null
    private var categoryModelList = SellerCategoryModel()
    lateinit var mBinding: BuyerPostshoplistFragmentBinding
    private var adapterShoppingModel: PostShoppingModel? = null
    private var productShoppingList = ArrayList<PostShoppingModel>()
    var check: String = ""
    var listingname = ""
    val postBuyer =
        PostBuyerShopListModel()
    lateinit var listener: HideButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.buyer_postshoplist_fragment,
            container, false
        )

       // mvmodel = ViewModelProviders.of(activity!!)[BuyerPostViewModel::class.java]
        mvmodel = ViewModelProvider(this).get(BuyerPostViewModel::class.java)
        mvmodel.clearData()
        setObserver()
        setClick()
        setAdapterShoppingList()
        return mBinding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as HideButton
    }

    //    override fun onAttach(activity: Activity?) {
//        super.onAttach(activity)
//        listener = activity as HideButton
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtname.filters = arrayOf<InputFilter>(HideEmoji(activity!!), InputFilter.LengthFilter(40))
        //get the category
        getProfile()
        creditDeductionLogic()
        mBinding.txtcategory.setOnClickListener {
            if (txtcategory.text.isEmpty()) {
                val intent = Intent(context, SellerChooseCategoryActivity::class.java)
                startActivityForResult(intent, 111)
            } else {
                val intent = Intent(context, SellerChooseCategoryActivity::class.java)
                intent.putExtra("keyPrefillingBuyerCategory", txtcategory.text.toString().trim())
                startActivityForResult(intent, 111)
            }
        }

        productShoppingList.clear()
        adapter!!.update(productShoppingList)
        //get the chooseDeliveryZone
        mBinding.chooseDeliveryZone.setOnClickListener {

            try {
                if (ActivityCompat.checkSelfPermission(
                        activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity!!, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    val mDialog = android.app.AlertDialog.Builder(context)
                    mDialog.setTitle(context?.getString(R.string.alert))
                    mDialog.setCancelable(false)

                    mDialog.setMessage(getString(R.string.LOcationPermissionCustomPopText))
                    mDialog.setPositiveButton(
                        getString(R.string.ok)
                    ) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            activity!!, arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ), 112
                        )
                        Handler().postDelayed({}, 1000)
                        dialog.cancel()
                    }
                    mDialog.show()
                } else {
                   // val intent = Intent(context, BuyerAddAddressActivity::class.java)
                    val intent = Intent(context, BuyerAddressMapBoxActivity::class.java)
                    intent.putExtra("keyBuyerAddress", arrayAddAddressModel)
                    startActivityForResult(intent, 112)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        //get the txtdayAndTimePickOptional
        mBinding.txtdayAndTimePickOptional.setOnClickListener {
            val intent = Intent(context, SellerSelectDaysAndTimeActivity::class.java)
            intent.putExtra("keyBuyerpost", deliveryTimeList)
            startActivityForResult(intent, 113)
        }
    }

    //*************************** Web Service of Post Buyer Shopping List **************************//
    fun buyer(): PostBuyerShopListModel {
        var creditToDeduct = 0
        if ((productShoppingList.size - freeProducts) > 0) {
            creditToDeduct = (productShoppingList.size - freeProducts) * creditPerProduct.toInt()
        }
        postBuyer.category_id = categoryModelList.category_id
        postBuyer.listId = "0"
        postBuyer.listingname = txtname.text.toString().trim()
        postBuyer.shoppingProductListJSonArray = shoppingProductListJSonArray()
        postBuyer.deliveryZoneJsonList = deliveryZoneJsonList()
        postBuyer.daysTimeZoneJsonArray = daysTimeZoneJsonArray()
        postBuyer.credDitToDeduct = creditToDeduct.toDouble()
        postBuyer.totalCredits = buyerCredits.toDouble().toInt()
        postBuyer.PostShoppingModel = productShoppingList
        return postBuyer
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == AppCompatActivity.RESULT_OK) {
            val datasmodel = data!!.getParcelableExtra<SellerCategoryModel>("keyname")!!
            categoryModelList = datasmodel
            mBinding.txtcategory.text = datasmodel.name
            //************************getting the user location*********************//
        } else if (requestCode == 112 && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val geocoder = Geocoder(context)
                    val address: Address?
                    try {
                        addAddressModel = data.getParcelableExtra("deliveryZone")!!
                        val lat = data.getStringExtra("lat")
                        val long = data.getStringExtra("long")

                        val addresses = geocoder.getFromLocation(lat!!.toDouble(), long!!.toDouble(), 1)
                        address = addresses[0]
                        Log.e("Map Fragment ", address.toString())
                        val builder = StringBuilder()
                        //  builder.append(address?.featureName).append(",\t")
                        builder.append(address?.getAddressLine(0)).append(",\t")
                        val resultDestination = builder.toString()
                        addAddressModel.address = resultDestination
                        arrayAddAddressModel.add(addAddressModel)
                        mBinding.chooseDeliveryZone.text = addAddressModel.address

                        Log.e("Map Fragment2 ", resultDestination)
                    } catch (e: IOException) {
                        Log.e("MapsActivity", e.localizedMessage)
                    }

                } catch (e: Exception) {
                    Log.e("MainActivity", e.message.toString())
                }
            }
        }

        //**********************time zone req code******************************//
        if (requestCode == 113 && resultCode == AppCompatActivity.RESULT_OK) {
            val setdata = data!!.getParcelableArrayListExtra<DaysParentModel>("daysmodel")!!
            deliveryTimeList = setdata
            mBinding.txtdayAndTimePickOptional.text = updatedDayTime(setdata)
            //optional time zone request
        }
    }

    //..............delivery zone array json format...........//
    fun deliveryZoneJsonList(): String {
        val jsonarray = JSONArray()
        for (i in 0 until arrayAddAddressModel.size) {
            val jsonobj = JSONObject()
            jsonobj.put("lat", arrayAddAddressModel[i].lat)
            jsonobj.put("long", arrayAddAddressModel[i].long)
            jsonobj.put("radius", arrayAddAddressModel[i].radius)
            jsonobj.put("address", arrayAddAddressModel[i].address)
            jsonarray.put(jsonobj)
        }

        Log.d("deliveryZoneJsonList= ", jsonarray.toString())
        postBuyer.deliveryZoneJsonList = jsonarray.toString()
        return jsonarray.toString()
    }
    //............time zone json array.........//

    fun daysTimeZoneJsonArray(): String {
        val parent = JSONArray()
        for (i in 0 until deliveryTimeList.size) {
            val mArray = JSONArray()
            val jsoninnerobject = JSONObject()
            if (i == 0) {
                jsoninnerobject.put("day", "Monday")
            }

            if (i == 1) {
                jsoninnerobject.put("day", "Tuesday")
            }

            if (i == 2) {
                jsoninnerobject.put("day", "Wednesday")
            }

            if (i == 3) {
                jsoninnerobject.put("day", "Thursday")
            }

            if (i == 4) {
                jsoninnerobject.put("day", "Friday")
            }

            if (i == 5) {
                jsoninnerobject.put("day", "Saturday")
            }

            if (i == 6) {
                jsoninnerobject.put("day", "Sunday")
            }

            for (slot in deliveryTimeList[i].slotList) {
                if (slot.from.contains(":")) {
                    val jsoninnerobject1 = JSONObject()
                    jsoninnerobject1.put("from", slot.from)
                    jsoninnerobject1.put("to", slot.to)
                    mArray.put(jsoninnerobject1)
                }
            }
            if (mArray.length() > 0) {
                jsoninnerobject.put("value", mArray)
                parent.put(jsoninnerobject)
            }
        }
        Log.d("timeZoneJsonArray1= ", parent.toString())
        postBuyer.daysTimeZoneJsonArray = parent.toString()
        return parent.toString()
    }

    // ...........json format shopping product list..........//

    fun shoppingProductListJSonArray(): String {
        val jsonarray = JSONArray()
        for (i in 0 until productShoppingList.size) {
            val jsonobject = JSONObject()
            jsonobject.put("name", productShoppingList[i].name)
            jsonobject.put("unit", productShoppingList[i].unit)
            jsonobject.put("qty", productShoppingList[i].price)
            jsonarray.put(jsonobject)
        }
        Log.d("shoppingListJSonArray= ", jsonarray.toString())
        postBuyer.shoppingProductListJSonArray = jsonarray.toString()
        return jsonarray.toString()
    }

    // ......update day and time format..............//
    private fun updatedDayTime(daysArraymodel: ArrayList<DaysParentModel>): String {
        var result: String = ""

        for (item in daysArraymodel) {

            for (child in item.slotList) {

                if (child.from.contains(":")) {
                    result = if (result.isEmpty()) {
                        Constant.getDayString(
                            context!!,
                            item.weeks
                        ) + "(" + Constant.ConvertAmPmFormat(
                            context!!,
                            child.from
                        ) + "-" + Constant.ConvertAmPmFormat(context!!, child.to) + ")"

                    } else {
                        if (result.contains(Constant.getDayString(context!!, item.weeks))) {
                            result + ", " + "(" + Constant.ConvertAmPmFormat(
                                context!!,
                                child.from
                            ) + "-" + Constant.ConvertAmPmFormat(context!!, child.to) + ")"
                        } else {
                            result + ", " + Constant.getDayString(
                                context!!,
                                item.weeks
                            ) + "(" + Constant.ConvertAmPmFormat(
                                context!!,
                                child.from
                            ) + "-" + Constant.ConvertAmPmFormat(context!!, child.to) + ")"
                        }
                    }
                }
            }

        }
        if (result.isEmpty()) {
            result = getString(R.string.Delivery_Time)
        }

        return result
    }

    private fun setAdapterShoppingList() {
        val manager = LinearLayoutManager(activity!!)
        adapter =
            PostShoppingBuyerAdapter(
                activity!!,
                this
            )
        mBinding.recycler.layoutManager = manager
        mBinding.recycler.adapter = adapter
    }

    fun setClick() {
        //..........set the click on the add Button in list...................//
        mBinding.btnAddproduct.setOnClickListener {
            //set the dialog
            txtname.setFocusable(false)
            addProductCustomDialog()
        }
    }


    private fun setObserver() {
        mvmodel.sendData().observe(viewLifecycleOwner, Observer {
            setAdapterShoppingList()
            productShoppingList = it
            adapter!!.update(it)
            if (it.size > 0) {
                mBinding.txtproductOfYourShopping.visibility = View.VISIBLE
            } else {
                mBinding.txtproductOfYourShopping.visibility = View.GONE
            }
        })

//        mvmodel.mSucessfulCreateShoppingList().observe(this, Observer {
//            //visibilty
//            listener.buttonHide()
//            val intent = Intent(activity, BuyerShoppingListPostedActivity::class.java)
//            intent.putExtra("keyshopping", it)
//            startActivity(intent)
//
//        })
//        //failure case
//        mvmodel.mFailureShoppingError().observe(this, Observer {
//            //visibilty
//            listener.buttonHide()
//            val msg = it
//            showErrorDialog(msg)
//        })

    }

    override fun deleteShoppingList(list: ArrayList<PostShoppingModel>, position: Int) {
        if (list.size > 0) {
            list.removeAt(position)
        }
        shoppingListData(list)
    }

    private fun shoppingListData(list: java.util.ArrayList<PostShoppingModel>) {
        adapter!!.update(list)
        if (list.size > 0) {
            mBinding.txtproductOfYourShopping.visibility = View.VISIBLE
        } else {
            mBinding.txtproductOfYourShopping.visibility = View.INVISIBLE
        }

    }

//    private fun showErrorDialog(msg: String?) {
//        //error dialog case of failure
//        val mDialog = AlertDialog.Builder(activity)
//        mDialog.setTitle("Alert")
//        mDialog.setMessage(msg)
//        mDialog.setPositiveButton(
//            android.R.string.ok,
//            DialogInterface.OnClickListener { dialog, which ->
//                dialog.cancel()
//
//            })
//        mDialog.show()
//    }

    private fun addProductCustomDialog() {
        dialog = Dialog(activity!!)
       // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.buyer_add_more_product_dialog)

        val Units = arrayOf("Select Unit", "unit", "oz", "lbs")
        val spinnerAdapters =
            SpinnerAdapterss(
                context!!,
                Units
            )
        dialog.edtunit11!!.adapter = spinnerAdapters
        //hide the emojic
        dialog.edtname1.filters = arrayOf<InputFilter>(HideEmoji(activity!!),
            InputFilter.LengthFilter(50)
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        if (adapterShoppingModel != null) {
            val model = DialogModel()
            model.name = adapterShoppingModel!!.name
            model.unit = adapterShoppingModel!!.unit
            model.price = adapterShoppingModel!!.price
            dialog.edtname1.setText(model.name)

            if (model.unit == "unit") {
                dialog.edtunit11.setSelection(1)
            } else if (model.unit == "oz") {
                dialog.edtunit11.setSelection(2)
            } else if (model.unit == "lbs") {
                dialog.edtunit11.setSelection(3)
            }
            dialog.edtqty.setText(model.price)
        } else {
            dialog.edtname1.setText("")
            dialog.edtunit11.setSelection(0)
            dialog.edtqty.setText("")
        }
        //set the click button of custom dialog
        dialog.btnAdd.setOnClickListener {
            val id = dialog.edtunit11.selectedItemId.toInt()
            if (adapterShoppingModel != null) {
                adapterShoppingModel!!.name = dialog.edtname1.text.toString()
                adapterShoppingModel!!.unit = setUnit(dialog.edtunit11.selectedItem.toString())
                adapterShoppingModel!!.price = dialog.edtqty.text.toString()

                if (adapterShoppingModel!!.name.isEmpty()) {
                    Toast.makeText(
                        activity,
                        getString(R.string.Please_Enter_The_Name),
                        Toast.LENGTH_SHORT
                    )
                        .show()

                } else if (adapterShoppingModel!!.name.length > 50) {
                    Toast.makeText(
                        activity,
                        getString(R.string.Please_add_less_than_thirty_char),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (adapterShoppingModel!!.unit == getString(R.string.Select_Unit)) {
                    Toast.makeText(
                        activity,
                        getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (adapterShoppingModel!!.price.isEmpty()) {
                    Toast.makeText(
                        activity,
                        getString(R.string.Please_Enter_The_quantity),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                   txtname.setFocusable(true)
                    txtname.setFocusableInTouchMode(true)
                    mvmodel.editListUpdate(adapterShoppingModel!!, adapterShoppingPosition)
                    Constant.hideSoftKeyboard(it, activity!!)
                    dialog.cancel()
                }
            } else {
                val model = PostShoppingModel()
                if (dialog.edtname1.text.isEmpty()) {
                    Toast.makeText(
                        activity,
                        getString(R.string.Please_Enter_The_Name),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtname1.text.length > 50) {
                    Toast.makeText(
                        activity,
                        getString(R.string.Please_add_less_than_thirty_char),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (id == 0) {

                    Toast.makeText(
                        activity,
                        getString(R.string.Please_Enter_The_Unit_Of_Measurement),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (dialog.edtqty.text.isEmpty()) {

                    Toast.makeText(
                        activity,
                        getString(R.string.Please_Enter_The_quantity),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else {
                    try {
                        model.name = dialog.edtname1.text.toString()
                        model.unit = setUnit(dialog.edtunit11.selectedItem.toString())
                        model.price = dialog.edtqty.text.toString()
//                        modelDialog.image//global model we want to set the image
                        mvmodel.ShoppingListData(model)
                        Constant.hideSoftKeyboard(it, activity!!)
                        dialog.cancel()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        dialog.setOnDismissListener {
            adapterShoppingModel = null
            adapterShoppingPosition = -1
        }

        dialog.setOnCancelListener {
            txtname.setFocusable(true)
            txtname.setFocusableInTouchMode(true)

        }
    }

    private fun setUnit(unit: String): String {
        var units = getString(R.string.Select_Unit)
        if (unit == "1") {
            units = "unit"
        } else if (unit == "2") {
            units = "oz"
        } else if (unit == "3") {
            units = "lbs"
        }
        return units
    }

    override fun edtData(data: PostShoppingModel, position: Int) {
        adapterShoppingModel = data
        adapterShoppingPosition = position

        txtname.setFocusable(false)

        addProductCustomDialog()
    }

    private fun creditDeductionLogic() {
        when (buyerLevel) {
            "1" -> freeProducts = 5
            "2" -> freeProducts = 6
            "3" -> freeProducts = 8
            "4" -> freeProducts = 10
            "5" -> freeProducts = 15
        }
        textaddFive.text =
            "${getString(R.string.You_need_to_pay)} $creditPerProduct ${getString(R.string.credit_for_additional)} $freeProducts ${getString(
                R.string.products
            )}"
    }

    private fun getProfile() {
      //  val gson = Gson()
       // val json = JSONObject(Constant.getPrefs(activity!!).getString(Constant.PROFILE, ""))
       // val pref = Constant.getPrefs(context!!).getString(Constant.PROFILE, "")
       // val profiledata = gson.fromJson(pref, GetProfileModel::class.java)
        buyerCredits =Constant.getProfileData(context!!).credits
        buyerLevel = Constant.getProfileData(context!!).buyer_level
        creditPerProduct = Constant.getProfileData(context!!).per_product_credits
     }

    interface HideButton {
        fun buttonHide()
        fun inflatePostFragment()
    }


}












