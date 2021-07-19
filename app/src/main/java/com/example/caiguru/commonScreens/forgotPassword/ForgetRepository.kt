package com.example.caiguru.commonScreens.forgotPassword

import android.app.Application
import android.widget.Toast
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

class ForgetRepository(var application: Application) {

    val mFailure = MutableLiveData<ErrorModel>()
    val mSuccessful = MutableLiveData<ForgotModel>()

    fun setEmailData(email: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.forgotpassword(email)
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        val model = ForgotModel()
                        if (status == "true") {
                            model.status = status
                            model.msg = msg
                            mSuccessful.value = model

                        } else {
                            val model = ErrorModel()
                            model.message = msg
                            mFailure.value = model
                        }
                    } catch (e: Exception) {

                    }
                } else {
                    Toast.makeText(application, "Network Error", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mFailure.value = model
            }

        })
    }

    fun sucessget(): MutableLiveData<ForgotModel> {
        return mSuccessful
    }

    fun errorget(): MutableLiveData<ErrorModel> {
        return mFailure
    }


}
