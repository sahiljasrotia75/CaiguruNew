package com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class BuyerOpenShopListRepo(var application: Application) {
    var mSucessfulChosseSeller = MutableLiveData<ArrayList<BuyerShopOpenModel>>()
    var mFailureChosseSeller = MutableLiveData<String>()
    var AllDataModel = ArrayList<BuyerShopOpenModel>()
    fun list_all_request(id: String, page: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.list_all_request(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), page, id
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            if (page == "1") {
                                AllDataModel.clear()
                            }
                            val lists = json.optJSONArray("requests").toString()

                            val gson = Gson()
                            val model1: ArrayList<BuyerShopOpenModel> =
                                gson.fromJson(
                                    lists,
                                    object : TypeToken<ArrayList<BuyerShopOpenModel>>() {}.type
                                )
                            AllDataModel.addAll(model1)
                            mSucessfulChosseSeller.value = AllDataModel

                        } else {
                            val msg = R.string.network_error
                            mFailureChosseSeller.value = msg.toString()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {

                    val msg = application.getString(R.string.network_error)
                    mFailureChosseSeller.value = msg
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureChosseSeller.value = msg
            }

        })
    }

    fun mSucessfulshopListData(): MutableLiveData<ArrayList<BuyerShopOpenModel>> {

        return mSucessfulChosseSeller
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mFailureChosseSeller
    }

}




