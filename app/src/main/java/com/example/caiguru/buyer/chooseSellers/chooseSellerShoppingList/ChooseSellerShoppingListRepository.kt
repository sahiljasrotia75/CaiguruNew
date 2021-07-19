package com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList

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
import java.lang.Exception

class ChooseSellerShoppingListRepository(var application: Application) {

    var mSucessfulSellerShoppingList = MutableLiveData<ArrayList<ChooseSellerShoppingModel>>()
    var mFailureChosseSeller = MutableLiveData<String>()

    fun setShoppingList(
        seller_id: String,
        lat: Double,
        long: Double,
        searchtext: String,
        catId: String,
        selfPickup: Int,
        homeDelivery: Int
    ) {


        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.seller_all_listing(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            seller_id,
            lat.toString(),
            long.toString(),
            searchtext,
            catId,
            selfPickup.toString(),
            homeDelivery.toString()

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {

                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val result = json.optJSONArray("results").toString()
                            val gson = Gson()
                            val model1: ArrayList<ChooseSellerShoppingModel> =
                                gson.fromJson(
                                    result,
                                    object :
                                        TypeToken<ArrayList<ChooseSellerShoppingModel>>() {}.type
                                )
                            model1[0].isExpanded = true
                            mSucessfulSellerShoppingList.value = model1

                        } else {
                            val msg = msg
                            mFailureChosseSeller.value = msg.toString()

                        }
                    }

                } catch (e: Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureChosseSeller.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureChosseSeller.value = msg
            }

        })
    }

    fun mSucessfulSellerShoppingList(): MutableLiveData<ArrayList<ChooseSellerShoppingModel>> {

        return mSucessfulSellerShoppingList
    }

    fun mFailureChosseSeller(): MutableLiveData<String> {

        return mFailureChosseSeller
    }
//**********************report List Api
var mFailureReposrtlist = MutableLiveData<String>()
var mSucessfulREportList = MutableLiveData<String>()

    fun reportList(reason: String, data: ChooseSellerShoppingModel) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.report_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),"application/json",data.id,"1",reason

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {

                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            mSucessfulREportList.value = msg

                        } else {
                            val msg = msg
                            mFailureReposrtlist.value = msg.toString()

                        }
                    }

                } catch (e: Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureReposrtlist.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureReposrtlist.value = msg
            }

        })

    }


    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mFailureReposrtlist
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mSucessfulREportList
    }

}
