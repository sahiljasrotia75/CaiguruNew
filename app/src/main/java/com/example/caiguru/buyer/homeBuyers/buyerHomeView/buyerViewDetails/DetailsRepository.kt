package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerViewDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerAddAddress.AddAddressModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews.ViewDetailModel
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
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

class DetailsRepository(var application: Application) {

    //***********************get_feed_comments***************//
    val mSuccessfulViewDetail = MutableLiveData<ViewDetailModel>()
    var mError = MutableLiveData<String>()
    fun getFeedview(
        data: HomeModel,
        addressData: AddAddressModel
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.buyer_feed_view(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),addressData.lat,addressData.long,addressData.address, data.post_id, data.list_id
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {

                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        val list = json.optJSONObject("list").toString()
                        val gson = Gson()
                        val arrayData: ViewDetailModel =
                            gson.fromJson(
                                list,
                                object : TypeToken<ViewDetailModel>() {}.type
                            )
                        mSuccessfulViewDetail.value = arrayData
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

    fun mSuccessfulViewDetail(): MutableLiveData<ViewDetailModel> {


        return mSuccessfulViewDetail
    }

    fun mError(): MutableLiveData<String> {

        return mError
    }

}
