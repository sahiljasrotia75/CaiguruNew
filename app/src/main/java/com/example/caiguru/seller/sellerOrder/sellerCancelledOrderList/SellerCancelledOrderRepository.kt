package com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
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

class SellerCancelledOrderRepository(var application: Application) {
    var mSucessfulOrderList = MutableLiveData<ArrayList<OrderListModel>>()
    var mFailure = MutableLiveData<String>()

    fun getrequestDetails(id: String, listType: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_request_detail(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id, listType
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    val arrayListData = ArrayList<OrderListModel>()
                    if (status == "true") {

                        val request = json.getJSONObject("request").toString()
                        val gson = Gson()
                        val modelData: OrderListModel =
                            gson.fromJson(
                                request,
                                object : TypeToken<OrderListModel>() {}.type
                            )
                        arrayListData.add(modelData)
                        mSucessfulOrderList.value = arrayListData

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailure.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailure.value = message

            }


        })


    }

    fun mSucessfulOrderList(): MutableLiveData<ArrayList<OrderListModel>> {

        return mSucessfulOrderList
    }

    fun mFailure(): MutableLiveData<String> {

        return mFailure
    }



    //**********************notification Read*********************//

    fun notificationRead(notificationId: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
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

}
