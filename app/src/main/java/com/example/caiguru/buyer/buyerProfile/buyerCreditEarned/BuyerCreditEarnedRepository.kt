package com.example.caiguru.buyer.buyerProfile.buyerCreditEarned

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSetting.sellerCreditEarned.EarnedReferedFriendModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuyerCreditEarnedRepository(var application: Application) {
    var mSucessful = MutableLiveData<ArrayList<EarnedReferedFriendModel>>()
    var mFailureData = MutableLiveData<String>()

    fun earnedByreffer() {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.earned_by_reffer(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), ""
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    if (status == "true") {
                        val resul = json.optJSONArray("rows").toString()
                        val gson = Gson()
                        val model1: ArrayList<EarnedReferedFriendModel> =
                            gson.fromJson(
                                resul,
                                object : TypeToken<ArrayList<EarnedReferedFriendModel>>() {}.type
                            )
                        mSucessful.value = model1
                    } else {
                        val msg = R.string.network_error
                        mFailureData.value = msg.toString()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureData.value = msg
            }

        })
    }

    fun getData(): MutableLiveData<ArrayList<EarnedReferedFriendModel>> {
        return mSucessful
    }


    fun getListFailure(): MutableLiveData<String> {
        return mFailureData
    }
    //**********************get Profile Api****************//
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
                if (response.isSuccessful) {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    var model = GetProfileModel()
                    if (status == "true") {
                        val seller = json.optString("seller_active_status")

                        Constant.getPrefs(application).edit()
                            .putString(Constant.seller_active_status, seller).apply()

                        Constant.getPrefs(application).edit()
                            .putString(Constant.PROFILE, json.toString()).apply()

                        val gson = Gson()
                        model =
                            gson.fromJson(
                                json.toString(),
                                object : TypeToken<GetProfileModel>() {}.type)
                        mSuccessful.value = model


                    } else {
                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
                        mError.value = model
                    }
                    try {
                    } catch (e: Exception) {

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
    fun errorget(): MutableLiveData<ErrorModel> {
        return mError
    }

}
