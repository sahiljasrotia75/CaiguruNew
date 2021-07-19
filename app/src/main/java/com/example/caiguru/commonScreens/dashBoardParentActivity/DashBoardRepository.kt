package com.example.caiguru.commonScreens.dashBoardParentActivity

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.WebServicesShoppingModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardRepository(var application: Application) {

    val mSuccessful = MutableLiveData<GetProfileModel>()
    var mError = MutableLiveData<ErrorModel>()

    fun getProfileData(token: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getprofile(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), "application/json", token, "1"
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val logout = json.optString("logout")

                        if (logout == "true") {
                            logout()
                        } else {
                            val is_has_cart = json.optString("is_has_cart")
                            val model = GetProfileModel()
                            if (status == "true") {
                                val seller = json.optString("seller_active_status")
                                Constant.getPrefs(application).edit().putString(Constant.seller_active_status, seller).apply()

                                Constant.getPrefs(application).edit().putString(Constant.PROFILE, json.toString()).apply()

                                //save the  is_has_cart
                                Constant.getPrefs(application).edit().putString(Constant.is_has_cart,is_has_cart).apply()
                                mSuccessful.value = model
                            } else {

                                val model = ErrorModel()
                                model.message =
                                    json.optString("message_${Constant.getLocal(application)}")
                                mError.value = model


                            }
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
    fun getdata(): MutableLiveData<GetProfileModel> {
        return mSuccessful
    }

    //error observer
    fun errorgetProfile(): MutableLiveData<ErrorModel> {
        return mError
    }


    //send back the data...................//
    val msucessfulShoppingList = MutableLiveData<ArrayList<WebServicesShoppingModel>>()
    val mFailureShoppingList = MutableLiveData<String>()

//    fun sendData(): MutableLiveData<ArrayList<PostShoppingModel>> {
//        return mShoppinglistdata
//    }

    fun createShoppingList(
        listingname: String,
        categoryModelList: String,
        deliveryZoneJsonList: String,
        daysTimeZoneJsonArray: String,
        shoppingProductListJSonArray: String,
        listId: String,
        creditsToDeduct: Double
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.post_buyer_shopping_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            listId,
            listingname,
            categoryModelList,
            deliveryZoneJsonList,
            daysTimeZoneJsonArray,
            shoppingProductListJSonArray,
            Constant.getLocal(application),
            creditsToDeduct.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val arraymodel = ArrayList<WebServicesShoppingModel>()
                try {
                    if (response.isSuccessful) {
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
                                //  webservicesmodel.image = innerobject.optString("image")
                                webservicesmodel.unit = innerobject.optString("unit")
                                webservicesmodel.qty = innerobject.optString("qty")
                                //    webservicesmodel.price = innerobject.optString("price")
                                webservicesmodel.msg = msg
                                webservicesmodel.listingname = listingname//add list name

                                arraymodel.add(webservicesmodel)

                            }
                            msucessfulShoppingList.value = arraymodel

                            updateCreditsInProfile(creditsToDeduct)

                        } else {
                            val msg = msg
                            mFailureShoppingList.value = msg
                        }

                    } else {
                        Toast.makeText(
                            application,
                            application.getString(R.string.network_error),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureShoppingList.value = msg
            }

        })

    }

    //send the shoppinglist
    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
        return msucessfulShoppingList
    }

    fun mFailureShoppingError(): MutableLiveData<String> {
        return mFailureShoppingList
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

    //********************get buyer notification api****************//
    var mSucessfulNotificationData = MutableLiveData<ArrayList<NotificationModel>>()
    var mFailure = MutableLiveData<String>()

    fun getHomePageNotification(token: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_homepage_notifications(
            token, "1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        val notifications = json.optJSONArray("notifications").toString()
                        val gson = Gson()
                        val modelData: ArrayList<NotificationModel> =
                            gson.fromJson(
                                notifications,
                                object : TypeToken<ArrayList<NotificationModel>>() {}.type
                            )
                        mSucessfulNotificationData.value = modelData

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailure.value = msg
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mFailure.value = message
            }
        })

    }

    //status observer
    fun mSucessfulNotification(): MutableLiveData<ArrayList<NotificationModel>> {
        return mSucessfulNotificationData
    }

    fun mFailure(): MutableLiveData<String> {
        return mFailure
    }


    //**********************notification Read*********************//

    fun notificationRead(notificationId: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.notification_read(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), notificationId
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })


    }

    //********************seller notification**************//
    fun getHomeSellerNotification(token: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_seller_homepage_notifications(
            token, "1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val notifications = json.optJSONArray("notifications").toString()
                        val gson = Gson()
                        val modelData: ArrayList<NotificationModel> =
                            gson.fromJson(
                                notifications,
                                object : TypeToken<ArrayList<NotificationModel>>() {}.type
                            )
                        mSucessfulNotificationData.value = modelData

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailure.value = msg
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mFailure.value = message
            }
        })
    }


    //****************************************update Address Api ****************************************//

    var mUpdateAddress = MutableLiveData<DeliveryZoneModel>()
    var failureAddress = MutableLiveData<String>()
    fun updatedAddress(addressModel: DeliveryZoneModel) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.update_address(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), addressModel.address, addressModel.lat, addressModel.long
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)

                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val model = DeliveryZoneModel()
                            val address = json.getJSONObject("address")
                            model.address = address.optString("full_address")
                            model.lat = address.optString("lat")
                            model.long = address.optString("long")
                            model.message = msg

                            val json = Constant.getPrefs(application).getString(Constant.PROFILE, "")
                            val gson = Gson()
                            var profileModel = GetProfileModel()
                            profileModel = gson.fromJson(json, GetProfileModel::class.java)
                            profileModel.lat = model.lat
                            profileModel.long = model.long
                            profileModel.full_address = model.address

                            val json2 = gson.toJson(profileModel)
                            Constant.getPrefs(application).edit()
                                .putString(Constant.PROFILE, json2).apply()

                            mUpdateAddress.value = model
                        } else {

                            failureAddress.value = msg
                        }
                    } else {
                        val msg = application.getString(R.string.network_error)
                        failureAddress.value = msg

                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                failureAddress.value = msg
            }

        })

    }

    fun mUpdateAddress(): MutableLiveData<DeliveryZoneModel> {

        return mUpdateAddress
    }

    fun failueAddress(): MutableLiveData<String> {
        return failureAddress
    }

    //********************logout when the user is banned
    var mData = MutableLiveData<ErrorModel>()
    fun logout() {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.logout(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            )
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val model = ErrorModel()
                model.message = application.getString(R.string.Logout_Sucessfully)
                Constant.getPrefs(application).edit().clear().apply()
                mData.value = model
                Constant.disconnectFromFacebook()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.Logout_Sucessfully)
                Constant.getPrefs(application).edit().clear().apply()
                mData.value = model
                Constant.disconnectFromFacebook()
            }

        })


    }


    //status observer
    fun logoutBannedUser(): MutableLiveData<ErrorModel> {
        return mData
    }

    //**************************back ground services
    fun hitBackGroundService() {
        Log.e("bgApiHIt", "Bg api start")
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.updatepushtopics(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            )
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {


                        val res = response.body()!!.string()
                        val json = JSONObject(res)
//                    val status = json.optString("status")
//                    val message = json.optString("message_${Constant.getLocal(application)}")
//                    if (status == "true") {
//                        Log.e("bgApiHIt","br api sucessful")
//
//                    }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })
    }

}
