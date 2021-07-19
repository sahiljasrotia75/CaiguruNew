package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PostShoppingRepository(var application: Application) {

    var mShoppinglistdata = MutableLiveData<ArrayList<PostShoppingModel>>()

    var freeProducts = 0
    private var sellerLevel: String = ""
    private var creditPerProduct: String = ""
    private var creditToDeduct: String = "0.0"
    private var sellerCredits: String = ""

    //api
    var mSucessfull = MutableLiveData<DialogModel>()
    var mFailuree = MutableLiveData<String>()
    var mdataZone = MutableLiveData<ArrayList<DeliveryZoneModel>>()
    var arraymodel1 = ArrayList<DeliveryZoneModel>()


    init {

        mdataZone.value = DummyData1()
        getProfile()
        creditDeductionLogic()
    }

    //empty delivery zone show
    private fun DummyData1(): ArrayList<DeliveryZoneModel> {
        val model = DeliveryZoneModel()
        model.address = application.getString(R.string.Delivery_Zone)
        arraymodel1.add(model)
        return arraymodel1
    }

    // **********************Add Address plus uadate new tab add***********************

    fun addNewAddress(deliveryZoneModel: DeliveryZoneModel) {
        val model = DeliveryZoneModel()
        model.address = deliveryZoneModel.address
        model.lat = deliveryZoneModel.lat
        model.long = deliveryZoneModel.long
        model.radius = deliveryZoneModel.radius
        arraymodel1.add(model)
        mdataZone.value = arraymodel1
    }

    //update location same position
    fun UpdateData(deliveryZoneModel: DeliveryZoneModel, position: Int) {
        val model = DeliveryZoneModel()
        if (position > 0) {
            model.address = deliveryZoneModel.address
            model.lat = deliveryZoneModel.lat
            model.long = deliveryZoneModel.long
            model.radius = deliveryZoneModel.radius
            arraymodel1.set(position, model)
            mdataZone.value = arraymodel1

        } else {
            arraymodel1.removeAt(position)
            model.address = deliveryZoneModel.address
            model.lat = deliveryZoneModel.lat
            model.long = deliveryZoneModel.long
            model.radius = deliveryZoneModel.radius
            arraymodel1.add(position, model)
            mdataZone.value = arraymodel1
        }

    }

    //upadte zone
    fun sendDataDeliveryZone(): MutableLiveData<ArrayList<DeliveryZoneModel>> {
        return mdataZone
    }

    fun deleteProduct(position: Int, list: java.util.ArrayList<PostShoppingModel>) {
        if (list.size > freeProducts) {
            creditToDeduct =
                (creditToDeduct.toDouble() - creditPerProduct.toDouble()).toString()

        }

    }

    //................data is come from the dialog all data is static.......//
    var arraymodel = ArrayList<PostShoppingModel>()

    fun ShoppingListData(
        data1: List<PostShoppingModel>,
        prefilling: Boolean,
        deleteArray: String
    ) {
        if (deleteArray.isNotEmpty()) {
            arraymodel.clear()

        }
        arraymodel.addAll(data1)

        if (arraymodel.size > freeProducts && !prefilling) {
            creditToDeduct =
                (creditToDeduct.toDouble() + creditPerProduct.toDouble()).toString()

        }
        mShoppinglistdata.value = arraymodel
    }

    //update the edit list at the same position

    fun editListUpdate(
        adapterShoppingModel: PostShoppingModel,
        adapterShoppingPosition: Int,
        prefilling: Boolean,
        nameEdited: String
    ) {

        val model = PostShoppingModel()
        if (arraymodel.size > freeProducts && prefilling) {
            //            for( i in 0 until arraymodel.size){
//                if (arraymodel[i].name==adapterShoppingModel.name){
//                    exit=1
//                    break
//                }
//            }

            if (nameEdited.isNotEmpty()) {
                creditToDeduct =
                    (creditToDeduct.toDouble() + creditPerProduct.toDouble()).toString()
            }


        }
        if (adapterShoppingPosition >= 0) {

            model.name = adapterShoppingModel.name
            model.unit = adapterShoppingModel.unit
            model.price = adapterShoppingModel.price
            model.image = adapterShoppingModel.image
            model.priceWithComission = adapterShoppingModel.priceWithComission
            model.saved_product_id = adapterShoppingModel.saved_product_id
            arraymodel.set(adapterShoppingPosition, model)
            mShoppinglistdata.value = arraymodel

        } else {

            arraymodel.removeAt(adapterShoppingPosition)
            model.name = adapterShoppingModel.name
            model.unit = adapterShoppingModel.unit
            model.price = adapterShoppingModel.price
            model.image = adapterShoppingModel.image
            model.priceWithComission = adapterShoppingModel.priceWithComission
            arraymodel.add(adapterShoppingPosition, model)
            mShoppinglistdata.value = arraymodel

        }
    }


    //send back the data...................//
    fun sendData(): MutableLiveData<ArrayList<PostShoppingModel>> {

        return mShoppinglistdata

    }

    fun showcreditToDeduct(): String {

        return creditToDeduct
    }

    //............set the image api..............//

    fun setProfileImage(uri: File) {

        setImage(uri)
    }

    fun setImage(uri: File) {
        val retrofit = Constant.getRestClient()


        val services = retrofit.create(WebServices::class.java)
        //pass list like this
        val file = uri
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


        val call: Call<ResponseBody> = services.uploadimage(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val model = DialogModel()
                if (response.isSuccessful) {
                    try {

                        val res = response.body()!!.string()
                        //get the data from the object
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            val image = json.optString("image")

                            model.image = image
                            model.message = message
                            mSucessfull.value = model

                        } else {

                            val errormsg = message
                            mFailuree.value = errormsg
                        }


                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                }

            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailuree.value = errormsg


            }


        })

    }

    //status true observer
    fun getdatasucessful(): MutableLiveData<DialogModel> {
        return mSucessfull
    }


    //error observer
    fun errorget(): MutableLiveData<String> {

        return mFailuree
    }
    //*****************************web service of get shopping list *******************************//

    val msucessfulShoppingList = MutableLiveData<ArrayList<WebServicesShoppingModel>>()
    val mFailureShoppingList = MutableLiveData<String>()

    //............................web servies start of create shopping list.........................//

    fun createShoppingList(
        listingname: String,
        categoryModelList: SellerCategoryModel,
        deliveryZone: String,
        deliveryDays: String,
        pickupDetails: String,
        minimumPurchaseAmount: String,
        commissionPer: Int,
        productDetails: String,
        prefillData: ListParentModel,
        productShoppingList: java.util.ArrayList<PostShoppingModel>,
        paymentMetods: String,
        whichTypeData: String
    ) {

        var listId2 = "0"
        if (prefillData.cat_id.isNotEmpty() && whichTypeData == "prefil") {
            listId2 = prefillData.id
            val gson = Gson()
            val json = prefillData.product_details
            val model1: List<PostShoppingModel> =
                gson.fromJson(json, object : TypeToken<List<PostShoppingModel>>() {}.type)
            var creditsToDeduct1 = 0

            if (productShoppingList.size > freeProducts) {

                for (i in 0 until productShoppingList.size) {
                    var exist = 0
                    for (j in 0 until model1.size) {
                        if (productShoppingList[i].name == model1[j].name) {
                            exist = 1
                            break
                        }
                    }
                    if (exist == 0) {
                        creditsToDeduct1++
                    }
                }
            }
            // creditToDeduct
        }

        val city = Constant.getPrefs(application).getString(Constant.sel_cities, "")
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.post_seller_shopping_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            listId2,
            listingname,
            categoryModelList.category_id,
            deliveryZone,
            deliveryDays,
            pickupDetails,
            minimumPurchaseAmount,
            commissionPer.toString(),
            productDetails,
            Constant.getLocal(application),
            city!!,
            creditToDeduct,
            paymentMetods

        )
        call.enqueue(object : Callback<ResponseBody> {


            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val arraymodel = ArrayList<WebServicesShoppingModel>()

                if (response.isSuccessful) {
                    try {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)

                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            val listingname = json.optString("listingname")
                            val listArray = json.getJSONArray("product_details")

                            for (i in 0 until listArray.length()) {
                                val webservicesmodel = WebServicesShoppingModel()
                                val innerobject = listArray.optJSONObject(i)
                                webservicesmodel.name = innerobject.optString("name")
                                webservicesmodel.image = innerobject.optString("image")
                                webservicesmodel.unit = innerobject.optString("unit")
                                webservicesmodel.price = innerobject.optString("price")
                                webservicesmodel.priceWithComission = innerobject.optString("priceWithComission")
                                webservicesmodel.msg = msg
                                webservicesmodel.listingname = listingname//add list name
                                arraymodel.add(webservicesmodel)
                            }
                            msucessfulShoppingList.value = arraymodel

                            if (creditToDeduct.toDouble() > 0) {
                                updateCreditsInProfile(creditToDeduct.toDouble())
                            }

                        } else {
                            val msg = msg
                            mFailureShoppingList.value = msg
                        }
                    } catch (e: Exception) {
                    }
                } else {
                    Toast.makeText(application, application.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureShoppingList.value = msg
            }
        })

    }


    // update credits in profile
    private fun updateCreditsInProfile(creditsToDeduct: Double) {
        val prefs = Constant.getPrefs(application)
        val json = JSONObject(prefs.getString(Constant.PROFILE, ""))
        val sellerCredits = json.optString("credits").trim().toDouble()

        val updatedCredits = sellerCredits - creditsToDeduct
        json.put("credits", updatedCredits.toString())

        val edit = prefs.edit()
        edit.putString(Constant.PROFILE, json.toString())
        edit.apply()
    }


    //send the shoppinglist
    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
        return msucessfulShoppingList
    }

    fun mFailureShoppingError(): MutableLiveData<String> {
        return mFailureShoppingList
    }

    private fun creditDeductionLogic() {
        when (sellerLevel) {
            "1" -> freeProducts = 5
            "2" -> freeProducts = 6
            "3" -> freeProducts = 8
            "4" -> freeProducts = 10
        }

    }

    private fun getProfile() {
        val json = JSONObject(Constant.getPrefs(application).getString(Constant.PROFILE, ""))
        sellerCredits = json.optString("credits").trim()
        sellerLevel = json.optString("seller_level").trim()
        creditPerProduct = json.optString("per_product_credits").trim()
    }


}
