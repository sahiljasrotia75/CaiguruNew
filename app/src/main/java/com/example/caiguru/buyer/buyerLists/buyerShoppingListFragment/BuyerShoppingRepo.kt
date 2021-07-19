package com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment

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
import java.lang.Exception

class BuyerShoppingRepo(var application: Application) {
    var mSucessfulChosseSeller = MutableLiveData<ArrayList<BuyerShopModel>>()
    var mFailureChosseSeller = MutableLiveData<String>()
    val modelArray = ArrayList<BuyerShopModel>()


    fun your_shopping_lists(page: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.your_shopping_lists(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), page
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
                            if (page == "1") {
                                modelArray.clear()
                            }

                            val lists = json.optJSONArray("lists").toString()

                            val gson = Gson()
                            val model1: ArrayList<BuyerShopModel> =
                                gson.fromJson(
                                    lists,
                                    object : TypeToken<ArrayList<BuyerShopModel>>() {}.type
                                )
                            modelArray.addAll(model1)
                            mSucessfulChosseSeller.value = modelArray

                        } else {
                            val msg = R.string.network_error
                            mFailureChosseSeller.value = msg.toString()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = R.string.network_error
                    mFailureChosseSeller.value = msg.toString()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureChosseSeller.value = msg
            }

        })
    }

    fun mSucessfulshopListData(): MutableLiveData<ArrayList<BuyerShopModel>> {

        return mSucessfulChosseSeller
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mFailureChosseSeller
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

}
