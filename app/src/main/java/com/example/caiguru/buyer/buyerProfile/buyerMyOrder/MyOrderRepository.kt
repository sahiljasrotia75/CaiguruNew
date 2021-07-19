package com.example.caiguru.buyer.buyerProfile.buyerMyOrder

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

class MyOrderRepository(
    var application: Application) {

    var mSucessfulChosseSeller = MutableLiveData<ArrayList<OrderModel>>()
    var allMOdelData=ArrayList<OrderModel>()
    var mFailureChosseSeller = MutableLiveData<String>()
    fun getMyOrder(page: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.myorders(
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
                        if (status == "true") {
                            val orders = json.optJSONArray("orders").toString()
                            if (page == "1") {
                                allMOdelData.clear()
                            }
                            val gson = Gson()
                            val model1: ArrayList<OrderModel> =
                                gson.fromJson(
                                    orders,
                                    object : TypeToken<ArrayList<OrderModel>>() {}.type
                                )
                            allMOdelData.addAll(model1)
                            mSucessfulChosseSeller.value = allMOdelData

                        } else {
                            val msg = R.string.network_error
                            mFailureChosseSeller.value = msg.toString()

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

    fun mSucessfulshopListData(): MutableLiveData<ArrayList<OrderModel>> {

        return mSucessfulChosseSeller
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mFailureChosseSeller
    }

}






