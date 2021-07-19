package com.example.caiguru.seller.sellerOrder.sellerOrderCompleted

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
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

class SellerOrderCompletedRepository(var application: Application) {
    var mSucessfulPendingApproval = MutableLiveData<ArrayList<SellerApprovalModel>>()
    var mFailure = MutableLiveData<String>()
    val ArrayMOdel = ArrayList<SellerApprovalModel>()
    fun getSellerRequest(page: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_seller_requests(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), "5", page, "1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        if (page == "1") {
                            ArrayMOdel.clear()
                        }
                        val requests = json.optJSONArray("requests").toString()
                        val gson = Gson()
                        val modelData: ArrayList<SellerApprovalModel> =
                            gson.fromJson(
                                requests,
                                object : TypeToken<ArrayList<SellerApprovalModel>>() {}.type
                            )
                        ArrayMOdel.addAll(modelData)
                        mSucessfulPendingApproval.value = ArrayMOdel
                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailure.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailure.value = message

            }


        })


    }

    fun mSucessfulPendingApproval(): MutableLiveData<ArrayList<SellerApprovalModel>> {

        return mSucessfulPendingApproval
    }

    fun mFailure(): MutableLiveData<String> {

        return mFailure
    }


}
