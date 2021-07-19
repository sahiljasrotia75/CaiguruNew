package com.example.caiguru.buyer.buyerLists.buyerShopApproveReject

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuyerShopListApproveRejectRepo(var application: Application) {

    var mSucessfulGetbuyerList = MutableLiveData<BuyerApproveRejectModel>()
    var mFailureGetbuyerList = MutableLiveData<ErrorModel>()
    fun get_buyer_request_detail(id: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_buyer_request_detail(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    if (status == "true") {
                        val model = BuyerApproveRejectModel()

                        val request = json.optJSONObject("request")
                        model.id = request.optString("id")
                        model.amount = request.optString("amount")
                        model.cat_id = request.optString("cat_id")
                        model.credits = request.optString("credits")
                        model.listingname = request.optString("listingname")
                        model.delivery_type = request.optString("delivery_type")
                        model.seller_id = request.optString("seller_id")
                        model.type = request.optString("type")
                        model.seller_name = request.optString("seller_name")
                        model.payment_methods = request.optString("payment_methods")
                        //     model.level = model.level
                        //getting the array fro object
                        //getting the array
                        model.products = request.optJSONArray("products").toString()
                        model.delivery_daytime = request.optJSONArray("delivery_daytime").toString()
                        model.delivery_location =request.optJSONArray("delivery_location").toString()
                        mSucessfulGetbuyerList.value = model

                    } else {

                        val model = ErrorModel()
                        model.message = "Network Error"
                        mFailureGetbuyerList.value = model
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mFailureGetbuyerList.value = model
            }


        })
    }

    fun SucessfulGetbuyerList(): MutableLiveData<BuyerApproveRejectModel> {

        return mSucessfulGetbuyerList
    }

    fun FailureGetbuyerList(): MutableLiveData<ErrorModel> {

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
