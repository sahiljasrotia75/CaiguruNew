package com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
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

class NotificationConfirmationRepository(var application: Application) {

    var mSucessfulChosseSeller = MutableLiveData<OrderModel>()
    var mFailureChosseSeller = MutableLiveData<String>()
    fun getOrderDetails(reqId: String, listType: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.myorderdetail(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), reqId, listType
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            val request = json.optJSONObject("request").toString()

                            val gson = Gson()
                            val model1: OrderModel =
                                gson.fromJson(
                                    request,
                                    object : TypeToken<OrderModel>() {}.type
                                )
                            mSucessfulChosseSeller.value = model1

                        } else {
                            val msg = R.string.network_error
                            mFailureChosseSeller.value = msg.toString()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = R.string.Network_Failure
                    mFailureChosseSeller.value = msg.toString()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureChosseSeller.value = msg
            }

        })
    }

    fun mSucessfulshopListData(): MutableLiveData<OrderModel> {

        return mSucessfulChosseSeller
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mFailureChosseSeller
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


    //set the cancelled accept the order
    var mSucessfulCancellOrder = MutableLiveData<String>()
    var mFailureCancelOrder = MutableLiveData<String>()
    fun SetAgreeCancelledOrder(data: NotificationModel) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_buyer_confirmation_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.source_id, "8", data.list_type, "", "" +
                    "", "", ""
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            val message =
                                json.optString("message_${Constant.getLocal(application)}")
                            mSucessfulCancellOrder.value = message

                        } else {
                            val message =
                                json.optString("message_${Constant.getLocal(application)}")
                            mFailureCancelOrder.value = message

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = application.getString(R.string.network_error)
                    mFailureCancelOrder.value = msg
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureCancelOrder.value = msg
            }

        })
    }

    fun mSucessfulCancellOrder(): MutableLiveData<String> {

        return mSucessfulCancellOrder
    }

    fun mFailureCancelOrder(): MutableLiveData<String> {

        return mFailureCancelOrder
    }


}
