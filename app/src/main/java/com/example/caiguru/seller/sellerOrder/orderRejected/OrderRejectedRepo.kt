package com.example.caiguru.seller.sellerOrder.orderRejected

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class OrderRejectedRepo(var application: Application) {
    //*******************api change_request_status****************//
    var mSucessfulRejectedRequest = MutableLiveData<String>()
    var mFailureRejectedRequest = MutableLiveData<String>()
    fun changeRequestStatus(
        reqId: String,
        nameOfReason: String,
        Rejectstatus: String
    ) {

//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_request_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), reqId, Rejectstatus, "1", nameOfReason
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
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureRejectedRequest.value = message
            }
        })


    }

    fun mSucessfulRejectedRequest(): MutableLiveData<String> {

        return mSucessfulRejectedRequest
    }

    fun mFailureRejectedRequest(): MutableLiveData<String> {

        return mFailureRejectedRequest
    }

}
