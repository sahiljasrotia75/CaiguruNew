package com.example.caiguru.seller.sellerChangePassword

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

class ChangePasswordRepo(var application: Application) {
    var mData = MutableLiveData<ErrorModel>()
    var mErrorData = MutableLiveData<ErrorModel>()

    fun updatePassword(password: String, oldPassword: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.update_password(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), password, oldPassword
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    val model = ErrorModel()
                    if (status == "true") {
                        model.message = json.optString("message_${Constant.getLocal(application)}")


                        mData.value = model


                    } else {

                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
                        mErrorData.value = model

                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mErrorData.value = model            }
        })

    }
    //status observer
    fun getsellerStatus(): MutableLiveData<ErrorModel> {
        return mData
    }

    //error observer
    fun errorgetStatus(): MutableLiveData<ErrorModel> {
        return mErrorData
    }

}
