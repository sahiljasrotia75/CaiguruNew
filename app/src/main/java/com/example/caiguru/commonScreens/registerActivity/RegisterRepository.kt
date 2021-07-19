package com.example.caiguru.commonScreens.registerActivity

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R

import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterRepository(var application: Application) {
    var mSucessfull = MutableLiveData<RegisterModel>()
    var mFailuree = MutableLiveData<String>()

    fun setImage(uri: File) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        //pass list like this
        val file = uri
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)



        val call: Call<ResponseBody> = services.uploadimage(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val model = RegisterModel()
                if (response.isSuccessful) {
//                    try {
                        val res = response.body()!!.string()
                        //get the data from the object
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            val image = json.optString("image")
                            model.image = image
                            mSucessfull.value = model

                        } else {

                            val errormsg = message
                            mFailuree.value = errormsg
                        }

//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//
//                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailuree.value = errormsg


            }


        })

    }
    //status true observer
    fun getdata(): MutableLiveData<RegisterModel> {
        return mSucessfull
    }

    //error observer
    fun errorget(): MutableLiveData<String> {

        return mFailuree
    }

    //******************check email validation
    var mFailureEmailRegisteration = MutableLiveData<String>()
    var mSucessfulEmailRegisteration = MutableLiveData<String>()
    fun check_email_valid(email: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.check_email_valid(
             email
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mSucessfulEmailRegisteration.value = message

                    } else {

                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mFailureEmailRegisteration.value = message
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureEmailRegisteration.value = message
            }

        })
    }
    fun mSucessfulEmailRegisteration(): MutableLiveData<String> {
        return mSucessfulEmailRegisteration
    }

    //error observer
    fun mFailureEmailRegisteration(): MutableLiveData<String> {

        return mFailureEmailRegisteration
    }
}
