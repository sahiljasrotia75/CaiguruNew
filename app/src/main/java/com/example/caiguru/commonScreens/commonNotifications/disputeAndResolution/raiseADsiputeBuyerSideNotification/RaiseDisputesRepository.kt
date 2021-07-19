package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.raiseADsiputeBuyerSideNotification

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class RaiseDisputesRepository(var application: Application) {

    var mSucessfulChosseSeller = MutableLiveData<String>()
    var mFailureChosseSeller = MutableLiveData<String>()

    fun setDisputeData(data: NotificationModel, name: String, number: String, comment: String) {


        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_buyer_confirmation_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),data.source_id,"6",data.list_type,"",comment,name,number
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            val message = json.optString("message_${Constant.getLocal(application)}")
                            mSucessfulChosseSeller.value = message

                        } else {
                            val message = json.optString("message_${Constant.getLocal(application)}")
                            mFailureChosseSeller.value = message

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

    fun mSucessfulshopListData(): MutableLiveData<String> {

        return mSucessfulChosseSeller
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mFailureChosseSeller
    }






}
