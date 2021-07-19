package com.example.caiguru.buyer.buyerOrder.buyerOrderComplete

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
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

class BuyerCompleteRepository(var application: Application) {
    var mSucessfulFinalizeOrder = MutableLiveData<ArrayList<FinalizeModel>>()
    var mFailure = MutableLiveData<String>()
    val mFinalizeOrderArray= ArrayList<FinalizeModel>()
    fun getDeliveredOrder(page: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_buyer_requests(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), "5", page, "2"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        if (page == "1") {
                            mFinalizeOrderArray.clear()
                        }

                        val requests = json.optJSONArray("requests").toString()
                        val gson = Gson()
                        val modelData: ArrayList<FinalizeModel> =
                            gson.fromJson(
                                requests,
                                object : TypeToken<ArrayList<FinalizeModel>>() {}.type
                            )
                        mFinalizeOrderArray.addAll(modelData)
                        mSucessfulFinalizeOrder.value = mFinalizeOrderArray

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

    fun mSucessfulFinalizeOrder(): MutableLiveData<ArrayList<FinalizeModel>> {

        return mSucessfulFinalizeOrder
    }

    fun mFailure(): MutableLiveData<String> {

        return mFailure
    }







}
