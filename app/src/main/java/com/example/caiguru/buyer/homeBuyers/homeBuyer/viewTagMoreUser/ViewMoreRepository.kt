package com.example.caiguru.buyer.homeBuyers.homeBuyer.viewTagMoreUser

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

class ViewMoreRepository(var application: Application) {

    //*********************** get_tag_users webservices***************//
    val mSuccessfulgetTagUser = MutableLiveData<ArrayList<HomeViewMoreModel>>()
    var mError = MutableLiveData<String>()

    fun get_shared_users(postId: String, sharedBy: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_shared_users(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), postId, sharedBy
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        val tags = json.optJSONArray("tags").toString()
                        val gson = Gson()
                        val arrayData: ArrayList<HomeViewMoreModel> =
                            gson.fromJson(
                                tags,
                                object : TypeToken<ArrayList<HomeViewMoreModel>>() {}.type
                            )
                        mSuccessfulgetTagUser.value = arrayData

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mError.value = msg
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mError.value = message
            }
        })
    }

    fun mSuccessfulgetTagUser(): MutableLiveData<ArrayList<HomeViewMoreModel>> {
        return mSuccessfulgetTagUser
    }

    fun mError(): MutableLiveData<String> {
        return mError
    }


}
