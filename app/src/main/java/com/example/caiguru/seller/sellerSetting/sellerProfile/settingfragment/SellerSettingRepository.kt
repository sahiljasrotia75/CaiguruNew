package com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment

import android.app.Application
import android.util.Log
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerSettingRepository(var application: Application) {
   // var mData = MutableLiveData<ErrorModel>()

    fun logout(token: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.logout(
            token
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("logout", "seller side")
//                val model = ErrorModel()
//                model.message = application.getString(R.string.Logout_Sucessfully)
//              Constant.getPrefs(application).edit().clear().apply()
//                mData.value = model
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                val model = ErrorModel()
//                model.message = application.getString(R.string.Logout_Sucessfully)
//                Constant.getPrefs(application).edit().clear().apply()
//                mData.value = model
            }
        })

    }

    //status observer
//    fun getsellerStatus(): MutableLiveData<ErrorModel> {
//        return mData
//    }
}
