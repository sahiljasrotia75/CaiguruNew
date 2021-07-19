package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
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

class ResolutionRepository(var application: Application) {

    var mSucessfulChosseSeller = MutableLiveData<RefuteModel>()
    var mFailureChosseSeller = MutableLiveData<String>()
    fun getOrderDetails(reqId: String, listType: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_dispute_detail(
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
                            val model1: RefuteModel =
                                gson.fromJson(
                                    request,
                                    object : TypeToken<RefuteModel>() {}.type
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

    fun mSucessfulshopListData(): MutableLiveData<RefuteModel> {

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

    var mSucessfulAcceptDisputes = MutableLiveData<String>()
    var mFailureAcceptDisputes = MutableLiveData<String>()

    //*****************accept disputes api******************//
    fun acceptDisputes(reqId: String, listType: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.seller_action_on_dispute(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), reqId, listType, "2", ""
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


                            mSucessfulAcceptDisputes.value = message

                        } else {
                            val message =
                                json.optString("message_${Constant.getLocal(application)}")
                            mFailureAcceptDisputes.value = message

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = R.string.Network_Failure
                    mFailureAcceptDisputes.value = msg.toString()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureAcceptDisputes.value = msg
            }

        })


    }

    fun mSucessfulAcceptDisputes(): MutableLiveData<String> {

        return mSucessfulAcceptDisputes
    }

    fun mFailureAcceptDisputes(): MutableLiveData<String> {

        return mFailureAcceptDisputes
    }

    var mSucessfulRefuteReason = MutableLiveData<String>()
    var mFailureRefuteReason = MutableLiveData<String>()
    //************************refute reason api*******************//

    fun refuteReason(reasonRefute: String, data: NotificationModel) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.seller_action_on_dispute(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.source_id, data.list_type, "6", reasonRefute
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


                            mSucessfulRefuteReason.value = message

                        } else {
                            val message =
                                json.optString("message_${Constant.getLocal(application)}")
                            mFailureRefuteReason.value = message

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = R.string.Network_Failure
                    mFailureRefuteReason.value = msg.toString()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureRefuteReason.value = msg
            }

        })


    }
    fun mSucessfulRefuteReason(): MutableLiveData<String> {

        return mSucessfulRefuteReason
    }

    fun mFailureRefuteReason(): MutableLiveData<String> {

        return mFailureRefuteReason
    }
}
