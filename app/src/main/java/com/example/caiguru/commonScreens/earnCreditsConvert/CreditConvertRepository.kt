package com.example.caiguru.commonScreens.earnCreditsConvert

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

class CreditConvertRepository(var application: Application) {
    var mSucessfulCashConvert = MutableLiveData<String>()
    var mFailure = MutableLiveData<String>()

    fun cashRequest(totalCredits: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.cash_request(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), totalCredits
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {

                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    val message = json.optString("message_${Constant.getLocal(application)}")
                    if (status == "true") {

                        mSucessfulCashConvert.value = message
                    } else {
                        mFailure.value = message
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mFailure.value = message
            }
        })

    }

    //status observer
    fun mSucessfulCashConvert(): MutableLiveData<String> {
        return mSucessfulCashConvert
    }

    fun mFailure(): MutableLiveData<String> {
        return mFailure
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
}
