package com.example.caiguru.seller.homeSeller

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeSellerRepository(var application: Application) {

    val mSuccessful = MutableLiveData<GetProfileModel>()
    var mError = MutableLiveData<ErrorModel>()

    fun getProfileData(token: String) {

//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getprofile(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),"application/json", token, "1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {

                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val logout = json.optString("logout")
                        if (logout == "true") {
                            logout()
                        } else {
                            val model = GetProfileModel()
                            if (status == "true") {
                                val seller = json.optString("seller_active_status")

                                Constant.getPrefs(application).edit()
                                    .putString(Constant.seller_active_status, seller).apply()

                                Constant.getPrefs(application).edit()
                                    .putString(Constant.PROFILE, json.toString()).apply()

                                mSuccessful.value = model

                            } else {
                                val model = ErrorModel()
                                model.message =
                                    json.optString("message_${Constant.getLocal(application)}")
                                mError.value = model
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
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
    fun errorget(): MutableLiveData<ErrorModel> {
        return mError
    }

    //*******************home seller web services**************//
    val mSuccessfulHomeData = MutableLiveData<ArrayList<HomeSellerModel>>()
    var mErrorHomeData = MutableLiveData<String>()
    var mHomeArray = ArrayList<HomeSellerModel>()
    fun getHomeSellerData(page: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.seller_hompage(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), page
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    if (page == "1") {
                        mHomeArray.clear()
                    }

                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val List = json.optJSONArray("lists").toString()
                        val gson = Gson()
                        val arrayData: ArrayList<HomeSellerModel> =
                            gson.fromJson(
                                List,
                                object : TypeToken<ArrayList<HomeSellerModel>>() {}.type
                            )
                        mHomeArray.addAll(arrayData)
                        mSuccessfulHomeData.value = mHomeArray

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mErrorHomeData.value = msg
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mErrorHomeData.value = message
            }
        })

    }

    fun mSuccessfulHomeData(): MutableLiveData<ArrayList<HomeSellerModel>> {
        return mSuccessfulHomeData
    }

    fun mErrorHomeData(): MutableLiveData<String> {
        return mErrorHomeData
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
