package com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuyerPostRepository(var application: Application) {

    var mShoppinglistdata = MutableLiveData<ArrayList<PostShoppingModel>>()

    //................data is come from the dialog all data is static.......//
    var arraymodel = ArrayList<PostShoppingModel>()

    fun ShoppingListData(data1: PostShoppingModel) {
        arraymodel.add(data1)
        mShoppinglistdata.value = arraymodel
    }

    //update the edit list at the same position
    fun editListUpdate(adapterShoppingModel: PostShoppingModel, adapterShoppingPosition: Int) {
        val model = PostShoppingModel()

        if (adapterShoppingPosition > 0) {

            model.name = adapterShoppingModel.name
            model.unit = adapterShoppingModel.unit
            model.price = adapterShoppingModel.price
            model.image = adapterShoppingModel.image
            arraymodel.set(adapterShoppingPosition, model)
            mShoppinglistdata.value = arraymodel

        } else {
            arraymodel.removeAt(adapterShoppingPosition)
            model.name = adapterShoppingModel.name
            model.unit = adapterShoppingModel.unit
            model.price = adapterShoppingModel.price
            model.image = adapterShoppingModel.image
            arraymodel.add(adapterShoppingPosition, model)
            mShoppinglistdata.value = arraymodel

        }
    }
//    //send back the data...................//
//    val msucessfulShoppingList = MutableLiveData<ArrayList<WebServicesShoppingModel>>()
//    val mFailureShoppingList = MutableLiveData<String>()

    fun sendData(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return mShoppinglistdata

    }

    //    fun createShoppingList(
//        listingname: String,
//        categoryModelList: String,
//        deliveryZoneJsonList: String,
//        daysTimeZoneJsonArray: String,
//        shoppingProductListJSonArray: String,
//        listId: String,
//        creditsToDeduct: Double
//    ) {
//        val retrofit = Constant.getRestClient()
//        val services = retrofit.create(WebServices::class.java)
//        val call: Call<ResponseBody> = services.post_buyer_shopping_list(
//            "Bearer " + Constant.getPrefs(application).getString(
//                Constant.token,
//                ""
//            ),
//            listId,
//            listingname,
//            categoryModelList,
//            deliveryZoneJsonList,
//            daysTimeZoneJsonArray,
//            shoppingProductListJSonArray,
//            Constant.getLocal(application),
//            creditsToDeduct.toString()
//
//        )
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                val arraymodel = ArrayList<WebServicesShoppingModel>()
//                try {
//                    if (response.isSuccessful) {
//                        val resp = response.body()!!.string()
//                        val json = JSONObject(resp)
//
//                        val status = json.optString("status")
//                        val msg = json.optString("message_${Constant.getLocal(application)}")
//                        if (status == "true") {
//
//                            val listingname = json.optString("listingname")
//                            val listArray = json.getJSONArray("product_details")
//
//                            for (i in 0 until listArray.length()) {
//                                val webservicesmodel = WebServicesShoppingModel()
//
//                                val innerobject = listArray.optJSONObject(i)
//                                webservicesmodel.name = innerobject.optString("name")
//                                //  webservicesmodel.image = innerobject.optString("image")
//                                webservicesmodel.unit = innerobject.optString("unit")
//                                webservicesmodel.qty = innerobject.optString("qty")
//                                //    webservicesmodel.price = innerobject.optString("price")
//                                webservicesmodel.msg = msg
//                                webservicesmodel.listingname = listingname//add list name
//
//                                arraymodel.add(webservicesmodel)
//
//                            }
//                            msucessfulShoppingList.value = arraymodel
//
//                            updateCreditsInProfile(creditsToDeduct)
//
//                        } else {
//                            val msg = msg
//                            mFailureShoppingList.value = msg
//                        }
//
//                    } else {
//                        Toast.makeText(application, "Network Error", Toast.LENGTH_SHORT).show()
//
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                val msg = application.getString(R.string.network_error)
//                mFailureShoppingList.value = msg
//            }
//
//        })
//
//    }
//    //send the shoppinglist
//    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
//        return msucessfulShoppingList
//    }
//
//    fun mFailureShoppingError(): MutableLiveData<String> {
//        return mFailureShoppingList
//    }
    //************************************ Switch setting api ************************************//
    var mError = MutableLiveData<ErrorModel>()
    var mData = MutableLiveData<String>()

    fun switch_setting(switch: String, type: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.switch_setting(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), switch, type
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            //save the login Data in shared prefrence
                            val editor = Constant.getPrefs(application).edit()
                            editor.putString(Constant.type, type)
                            editor.putString(Constant.switch_active, switch)
                            editor.apply()

                            mData.value = status

                        } else {
                            val model = ErrorModel()
                            model.message =
                                json.optString("message_${Constant.getLocal(application)}")
                            mError.value = model

                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mError.value = model
            }

        })

    }

    //status observer
    fun getdata(): MutableLiveData<String> {
        return mData
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {
        return mError
    }

    //***********************************seller_status*****************************************//

    var msellerStatus = MutableLiveData<ErrorModel>()
    var mErrorStatus = MutableLiveData<ErrorModel>()

    fun seller_active_status(seller_active_status: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.seller_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), seller_active_status
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    // var msg = json.optString("message_${Constant.getLocal(application)}")
                    val model = ErrorModel()
                    if (status == "true") {
                        model.message = json.optString("message_${Constant.getLocal(application)}")

                        Constant.getPrefs(application).edit()
                            .putString(Constant.seller_active_status, seller_active_status).apply()
                        msellerStatus.value = model

                    } else {

                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
                        mError.value = model

                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mErrorStatus.value = model
            }

        })
    }

    //status observer
    fun getsellerStatus(): MutableLiveData<ErrorModel> {
        return msellerStatus
    }

    //error observer
    fun errorgetStatus(): MutableLiveData<ErrorModel> {
        return mErrorStatus
    }

    // update credits in profile
//    private fun updateCreditsInProfile(creditsToDeduct: Double) {
//        val prefs = Constant.getPrefs(application)
//        val json = JSONObject(prefs.getString(Constant.PROFILE, ""))
//        val sellerCredits = json.optString("credits").trim().toDouble()
//
//        val updatedCredits = sellerCredits - creditsToDeduct
//        json.put("credits", updatedCredits.toString())
//
//        val edit = prefs.edit()
//        edit.putString(Constant.PROFILE, json.toString())
//        edit.apply()
//    }
    fun clearData() {
        mShoppinglistdata = MutableLiveData<ArrayList<PostShoppingModel>>()
        //................data is come from the dialog all data is static.......//
        arraymodel = ArrayList<PostShoppingModel>()

    }


}
