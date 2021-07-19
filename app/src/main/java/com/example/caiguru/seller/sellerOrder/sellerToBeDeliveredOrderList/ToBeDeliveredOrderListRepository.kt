package com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
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

class ToBeDeliveredOrderListRepository(var application: Application) {
    var mSucessfulOrderList = MutableLiveData<ArrayList<OrderListModel>>()
    var mFailure = MutableLiveData<String>()

    fun getrequestDetails(sellerDataModel: SellerApprovalModel) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_request_detail(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), sellerDataModel.id,sellerDataModel.list_type
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


    //*******************api change_request_status****************//
    var mSucessfulRequestSet = MutableLiveData<String>()
    var mFailureRequestApprovals = MutableLiveData<String>()

    fun changeRequestStatus(sellerDataModel: SellerApprovalModel) {

//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_request_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), sellerDataModel.id, "4",sellerDataModel.list_type,""
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val message = json.optString("message_${Constant.getLocal(application)}")

                        mSucessfulRequestSet.value = message

                    } else {
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mFailure.value = message
                    }
                } catch (e: Exception) {

                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureRequestApprovals.value = message
            }
        })


    }

    fun mSucessfulRequestApprovals(): MutableLiveData<String> {

        return mSucessfulRequestSet
    }

    fun mFailureRequestApprovals(): MutableLiveData<String> {

        return mFailureRequestApprovals
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
