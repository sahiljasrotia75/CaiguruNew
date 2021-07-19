package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.loadmore

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadMoreRepository(var application: Application) {

    var mSucessfuldata = MutableLiveData<ArrayList<CustomerChildModel>>()
    var mFailureData = MutableLiveData<String>()
    fun loadmoreBuyerlist(mData: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.load_more_buyer_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), mData
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    if (status == "true") {
                        val result = json.optJSONObject("results")
                        val list = result.optJSONArray("lists").toString()
                        val gson = Gson()
                        val model1: ArrayList<CustomerChildModel> =
                            gson.fromJson(
                                list,
                                object : TypeToken<ArrayList<CustomerChildModel>>() {}.type
                            )
                        mSucessfuldata.value = model1
                    } else {
                        val msg = R.string.network_error
                        mFailureData.value = msg.toString()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureData.value = msg
            }
        })

    }


    fun getData(): MutableLiveData<ArrayList<CustomerChildModel>> {

        return mSucessfuldata
    }

    fun getListFailure(): MutableLiveData<String> {

        return mFailureData
    }

}



