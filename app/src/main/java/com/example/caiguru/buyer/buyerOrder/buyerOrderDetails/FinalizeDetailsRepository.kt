package com.example.caiguru.buyer.buyerOrder.buyerOrderDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerApproveRejectModel
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
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

class FinalizeDetailsRepository(var application: Application) {

    var mSucessfulGetbuyerList = MutableLiveData<OrderListModel>()
    var mFailureGetbuyerList = MutableLiveData<String>()
    fun get_buyer_request_detail(id: String, listType: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_request_detail(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id,listType
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                  //  val arrayListData = ArrayList<OrderListModel>()
                    if (status == "true") {
                        val request = json.getJSONObject("request").toString()
                        val gson = Gson()
                        val modelData: OrderListModel =
                            gson.fromJson(
                                request,
                                object : TypeToken<OrderListModel>() {}.type
                            )
                       // arrayListData.add(modelData)
                        mSucessfulGetbuyerList.value = modelData

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailureGetbuyerList.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureGetbuyerList.value = message

            }


        })
    }

    fun SucessfulGetbuyerList(): MutableLiveData<OrderListModel> {

        return mSucessfulGetbuyerList
    }

    fun FailureGetbuyerList(): MutableLiveData<String> {

        return mFailureGetbuyerList
    }

    //****************************************api change buyer status************************************//
    var mSucessfulRequestApprovals = MutableLiveData<String>()
    var mFailureRequestApprovals = MutableLiveData<String>()

    fun change_buyer_request_status(id: String) {

//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_buyer_request_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id, "2"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val message = json.optString("message_${Constant.getLocal(application)}")

                        mSucessfulRequestApprovals.value = message

                    } else {
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mFailureRequestApprovals.value = message
                    }
                } catch (e: java.lang.Exception) {
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

        return mSucessfulRequestApprovals
    }

    fun mFailureRequestApprovals(): MutableLiveData<String> {

        return mFailureRequestApprovals
    }


    //*****************************api change buyer status***********************************//

    var mSucessfulRejectedRequest = MutableLiveData<String>()
    var mFailureRejectedRequest = MutableLiveData<String>()

    fun change_buyer_request_reject_status(id: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_buyer_request_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id, "3"
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val message = json.optString("message_${Constant.getLocal(application)}")

                        mSucessfulRejectedRequest.value = message

                    } else {
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mFailureRejectedRequest.value = message
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureRequestApprovals.value = message
            }
        })
    }

    fun mSucessfulRejectedRequest(): MutableLiveData<String> {

        return mSucessfulRejectedRequest
    }

    fun mFailureRejectedRequest(): MutableLiveData<String> {

        return mFailureRejectedRequest
    }


    //*****************cancel list
    var mSucessfulCancelList = MutableLiveData<String>()
    var mFailureCancelLIst = MutableLiveData<String>()
    fun btnCancel(id: String, listType: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.cancel_status(
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
                    if (status == "true") {

                        val message = json.optString("message_${Constant.getLocal(application)}")

                        mSucessfulCancelList.value = message

                    } else {
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mFailureCancelLIst.value = message
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureCancelLIst.value = message
            }
        })
    }
    fun mSucessfulCancelList(): MutableLiveData<String> {

        return mSucessfulCancelList
    }

    fun mFailureCancelLIst(): MutableLiveData<String> {

        return mFailureCancelLIst
    }

    //**********************notification Read*********************//

//    fun notificationRead(notificationId: String) {
////        val retrofit = Retrofit.Builder()
////            .baseUrl(Constant.BASE_URL)
////            .build()
//        val retrofit = Constant.getRestClient()
//        val services = retrofit.create(WebServices::class.java)
//        val call: Call<ResponseBody> = services.notification_read(
//            "Bearer " + Constant.getPrefs(application).getString(
//                Constant.token,
//                ""
//            ), notificationId
//        )
//        call.enqueue(object : Callback<ResponseBody> {
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            }
//
//        })
//
//
//    }
}
