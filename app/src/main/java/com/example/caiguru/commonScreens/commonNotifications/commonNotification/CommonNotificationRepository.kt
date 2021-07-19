package com.example.caiguru.commonScreens.commonNotifications.commonNotification

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
import java.lang.Exception

class CommonNotificationRepository(var application: Application) {
    var mSucessfulNotificationData = MutableLiveData<ArrayList<NotificationModel>>()
    var mFailure = MutableLiveData<String>()
    var modelData = ArrayList<NotificationModel>()

    fun getNotification(page: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_notifications(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), page
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    if (page == "1") {
                        modelData.clear()
                    }
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val notifications = json.optJSONArray("notifications").toString()
                        val gson = Gson()
                        val arrayData: ArrayList<NotificationModel> =
                            gson.fromJson(
                                notifications,
                                object : TypeToken<ArrayList<NotificationModel>>() {}.type
                            )
                        modelData.addAll(arrayData)
                        mSucessfulNotificationData.value = modelData

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailure.value = msg
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
    fun mSucessfulNotification(): MutableLiveData<ArrayList<NotificationModel>> {
        return mSucessfulNotificationData
    }

    fun mFailure(): MutableLiveData<String> {
        return mFailure
    }
}
