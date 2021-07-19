package com.example.caiguru.buyer.homeBuyers.homeBuyer

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeRepository(var application: Application) {

    //****************get profile api****************//
    val mSuccessful = MutableLiveData<GetProfileModel>()
    var mError = MutableLiveData<ErrorModel>()

    fun getProfile(token: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getprofile(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), "application/json",token, "1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    var model = GetProfileModel()
                    if (status == "true") {
                        val seller = json.optString("seller_active_status")

                        Constant.getPrefs(application).edit()
                            .putString(Constant.seller_active_status, seller).apply()

                        Constant.getPrefs(application).edit()
                            .putString(Constant.PROFILE, json.toString()).apply()

                        val gson = Gson()
                        model =
                            gson.fromJson(
                                json.toString(),
                                object : TypeToken<GetProfileModel>() {}.type
                            )
                        mSuccessful.value = model


                    } else {
                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
                        mError.value = model
                    }
                    try {
                    } catch (e: Exception) {

                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mError.value = model
            }

        })
    }

    //status observer
    fun getdata(): MutableLiveData<GetProfileModel> {
        return mSuccessful
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {
        return mError
    }

    //***********************buyer home page webservices***************//
    val mSuccessfulBuyerHome = MutableLiveData<ArrayList<HomeModel>>()
    var mErrorbuyerHome = MutableLiveData<String>()
    val arrayModelData = ArrayList<HomeModel>()

    fun setBuyerHome(lat: String, long: String, page: String) {


        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        Log.e("Pagination", page)
        val call: Call<ResponseBody> = services.buyer_hompage(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), page, lat, long
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        if (page == "1") {
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
                            mSuccessfulBuyerHome.value = arrayModelData

                        } else {
                            val msg = application.getString(R.string.Enable_to_get_response_server)
                            mErrorbuyerHome.value = msg
                        }
                    } else {
                        val msg = application.getString(R.string.Enable_to_get_response_server)
                        mErrorbuyerHome.value = msg
                    }

                } catch (e: java.lang.Exception) {
                    val msg = application.getString(R.string.Enable_to_get_response_server)
                    mErrorbuyerHome.value = msg
                    e.printStackTrace()

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mErrorbuyerHome.value = message
            }
        })
    }

    fun mSuccessfulBuyerHome(): MutableLiveData<ArrayList<HomeModel>> {


        return mSuccessfulBuyerHome
    }

    fun mErrorbuyerHome(): MutableLiveData<String> {

        return mErrorbuyerHome
    }

    //****************like api***********//
    fun setLikeDisLike(like: String, postId: String, isLike: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.buyer_feed_like_dislike(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), postId, like
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val resp = response.body()!!.string()
                val obj = JSONObject(resp)
               // val satatus = obj.optString("status")

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })

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
