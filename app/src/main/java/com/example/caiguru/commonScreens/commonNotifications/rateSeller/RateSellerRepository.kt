package com.example.caiguru.commonScreens.commonNotifications.rateSeller

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

class RateSellerRepository(var application: Application) {

    var mSucessfulChosseSeller = MutableLiveData<String>()
    var mFailureChosseSeller = MutableLiveData<String>()

    fun setRatingData(
        data: NotificationModel,
        comment: String,
        rating: String
    ) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.change_buyer_confirmation_status(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.source_id, "5", data.list_type, rating, comment, "", ""
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
                            mSucessfulChosseSeller.value = message

                        } else {
                            val message =
                                json.optString("message_${Constant.getLocal(application)}")
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

    //*********************admin complete the list then buyer give the rating to seller
    fun AdminCompleteListRateSeller(
        reqId: String,
        listType: String,
        comment: String,
        rating: String
    ) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.rate_to_seller(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), reqId, listType, rating, comment
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            val message =application.getString(R.string.Rated_Sucessfully)
                            mSucessfulChosseSeller.value = message

                        } else {
                           val message =application.getString(R.string.network_error)

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
