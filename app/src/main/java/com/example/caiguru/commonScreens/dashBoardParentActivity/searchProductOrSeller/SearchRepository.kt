package com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
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

class SearchRepository(var application: Application) {
    //********************get all the data of blocked users
    var mSucessFulSearch = MutableLiveData<ArrayList<HomeModel>>()
    var mFailureSearch = MutableLiveData<String>()
    val arrayModelData = ArrayList<HomeModel>()

    //*********************************search api******************//
    fun getSearchData(
        search: String,
        address: DeliveryZoneModel,
        searchType: String,
        page: Int
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.buyer_feed_filter(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            page.toString(), address.lat, address.long, searchType, search
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        if (page == 1) {
                            arrayModelData.clear()
                        }
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        if (status == "true") {

                            val posts = json.optJSONArray("posts").toString()
                            val gson = Gson()
                            val arrayData: ArrayList<HomeModel> =
                                gson.fromJson(
                                    posts,
                                    object : TypeToken<ArrayList<HomeModel>>() {}.type
                                )
                            arrayModelData.addAll(arrayData)
                            mSucessFulSearch.value = arrayModelData

                        } else {
                            val msg = application.getString(R.string.Enable_to_get_response_server)
                            mFailureSearch.value = msg
                        }
                    }  else {
                        val msg = application.getString(R.string.network_error)
                        mFailureSearch.value = msg
                    }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureSearch.value = errormsg
            }
        })

    }

    //status true observer
    fun mFailureSearch(): MutableLiveData<String> {
        return mFailureSearch
    }

    //error observer
    fun mSucessFulSearch(): MutableLiveData<ArrayList<HomeModel>> {

        return mSucessFulSearch
    }
    //**********************report List Api
    var mFailureReposrtlist = MutableLiveData<String>()
    var mSucessfulREportList = MutableLiveData<String>()

    fun reportList(reason: String, data: HomeModel) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.report_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),"application/json",data.list_id,data.list_type,reason

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

                } catch (e: java.lang.Exception) {
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
