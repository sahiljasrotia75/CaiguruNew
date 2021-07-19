package com.example.caiguru.buyer.chooseSellers.chooseSeller

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ChooseSellerRepository(var application: Application) {

    var mSucessfulChosseSeller = MutableLiveData<ArrayList<CustomerParentModel>>()
    var mFailureChosseSeller = MutableLiveData<String>()

    fun setChooseSeller(lat: String, long: String, home_delivery: String, self_pickup: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.choose_seller(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), self_pickup, home_delivery, lat, long

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
                        }
                        if (status == "true") {
                            val resul = json.optJSONArray("results").toString()

                            val gson = Gson()
                            val model1: ArrayList<CustomerParentModel> =
                                gson.fromJson(
                                    resul,
                                    object : TypeToken<ArrayList<CustomerParentModel>>() {}.type
                                )
                            mSucessfulChosseSeller.value = model1

                        } else {
                            val msg = R.string.network_error
                            mFailureChosseSeller.value = msg.toString()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureChosseSeller.value = msg
            }
        })
    }

    fun mSucessfulChooseSellerData(): LiveData<ArrayList<CustomerParentModel>> {
        return mSucessfulChosseSeller
    }

    fun mFailureChosseSeller(): MutableLiveData<String> {

        return mFailureChosseSeller
    }


    //*************************serach product Api********************//
    var mSearchSucessfulDatas = MutableLiveData<ArrayList<CustomerChildModel>>()
    var mSearchFailure = MutableLiveData<String>()
    fun searchProduct(
        search: String,
        lat: String,
        long: String,
        home_delivery: String,
        self_pickup: String
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.search_by_product(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), search, self_pickup, home_delivery, lat, long
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                val res = response.body()!!.string()
                val json = JSONObject(res)
                val status = json.optString("status")
                val msg = json.optString("message_${Constant.getLocal(application)}")
                if (status == "true") {
                    val resul = json.optJSONArray("results")
                    val gson = Gson()
                    if (resul.length()>0){
                        val modelData: ArrayList<CustomerChildModel> =
                            gson.fromJson(
                                resul.toString(),
                                object : TypeToken<ArrayList<CustomerChildModel>>() {}.type
                            )
                        mSearchSucessfulDatas.value = modelData
                    }else{
                        val modelData= ArrayList<CustomerChildModel>()
                        mSearchSucessfulDatas.value = modelData
                    }

                } else {
                    mSearchFailure.value = msg.toString()

                }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mSearchFailure.value = msg
            }

        })

    }

    fun mSearchSucessfulDatas(): MutableLiveData<ArrayList<CustomerChildModel>> {
        return mSearchSucessfulDatas
    }

    fun mSearchFailure(): MutableLiveData<String> {
        return mSearchFailure
    }

    //****************************************update Address Api ****************************************//

    var mUpdateAddress = MutableLiveData<DeliveryZoneModel>()
    var mFaillure = MutableLiveData<ErrorModel>()

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

                        mUpdateAddress.value = model
                    } else {
                        val model = ErrorModel()
                        model.message = msg
                        mFaillure.value = model
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mSearchFailure.value = msg
            }

        })

    }

    fun mUpdateAddress(): MutableLiveData<DeliveryZoneModel> {

        return mUpdateAddress
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return mFaillure
    }


    //*******************************************load_more_sellers**************************************//
    var mSucessfulload_more = MutableLiveData<ArrayList<CustomerChildModel>>()
    var mFailureload_more = MutableLiveData<String>()


    fun loadmoreSeller(
        catId: String,
        lat: String,
        long: String,
        home_delivery: String,
        self_pickup: String
    ) {


        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.load_more_sellers(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), catId, self_pickup, home_delivery, lat, long

        )

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {

                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")

                        if (status == "true") {
                         //   var model = CustomerChildModel()
                         //   model.cat_id = json.optString("cat_id")

                            val resul = json.optJSONArray("results").toString()

                            val gson = Gson()
                            val model1: ArrayList<CustomerChildModel> =
                                gson.fromJson(
                                    resul,
                                    object : TypeToken<ArrayList<CustomerChildModel>>() {}.type
                                )
                            mSucessfulload_more.value = model1

                        } else {
                            val msg = R.string.network_error
                            mFailureload_more.value = msg.toString()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = "Network Failure"
                    mFailureChosseSeller.value = msg
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureChosseSeller.value = msg
            }

        })


    }


    fun mSucessfulload_more(): MutableLiveData<ArrayList<CustomerChildModel>> {

        return mSucessfulload_more
    }

    fun mFailureload_more(): MutableLiveData<String> {

        return mFailureload_more
    }

    //********************logout when the user is banned
    var mData = MutableLiveData<ErrorModel>()
    fun logout() {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
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

}
