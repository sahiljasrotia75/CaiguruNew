package com.example.caiguru.commonScreens.otherProfiles.otherProfile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.homeSeller.GetProfileModel
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
import retrofit2.Retrofit


class OtherProfileRepository(var application: Application) {
    var mSucessfull = MutableLiveData<GetProfileModel>()
    var mFailuree = MutableLiveData<String>()

    fun getOtherProfileData(
        data: SellerApprovalModel,
        type: String
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getotherprofile(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.buyer_id, type
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        if (status == "true") {
                            val gson = Gson()
                            val modelData: GetProfileModel =
                                gson.fromJson(
                                    json.toString(),
                                    object : TypeToken<GetProfileModel>() {}.type
                                )
                            if (modelData.lists.size > 0) {
                                modelData.lists[0].isExpanded = true
                            }
                            mSucessfull.value = modelData

                        } else {
                            val msg = application.getString(R.string.network_error)
                            mFailuree.value = msg
                        }
                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailuree.value = msg
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailuree.value = errormsg
            }
        })

    }

    //status true observer
    fun getdata(): MutableLiveData<GetProfileModel> {
        return mSucessfull
    }

    //error observer
    fun errorget(): MutableLiveData<String> {

        return mFailuree
    }


    //**************************follow unfollow api********************//
    var mSucessfullFollowUnfollow = MutableLiveData<GetProfileModel>()
    var mFailureeFollowUnfollow = MutableLiveData<String>()

    //********************follow or unfollow api************//
    fun setFollowUnfollow(data: SellerApprovalModel, followStatus: Int) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.follow_unfollow(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.buyer_id, followStatus.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        val gson = Gson()
                        val modelData: GetProfileModel =
                            gson.fromJson(
                                json.toString(),
                                object : TypeToken<GetProfileModel>() {}.type
                            )
                        mSucessfullFollowUnfollow.value = modelData
                        // {"status":"true","follow_status":"1"}
                    } else {
                        var msg = application.getString(R.string.network_error)
                        mFailureeFollowUnfollow.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureeFollowUnfollow.value = errormsg
            }
        })
    }


    //status true observer
    fun mSucessfullFollowUnfollow(): MutableLiveData<GetProfileModel> {
        return mSucessfullFollowUnfollow
    }

    //error observer
    fun mFailureeFollowUnfollow(): MutableLiveData<String> {

        return mFailureeFollowUnfollow
    }

    //*********************blocked user*****************//
    val mSucessfulBlockedUser=MutableLiveData<String>()
    val mFailureBlockedUser=MutableLiveData<String>()

    fun blockedUser(data: SellerApprovalModel, type: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.block_unblock_user(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.buyer_id, type,"1"
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            mSucessfulBlockedUser.value = message

                        } else {
                            val msg = message
                            mFailureBlockedUser.value = msg
                        }
                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailureBlockedUser.value = msg
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureBlockedUser.value = errormsg
            }
        })

    }

    //status true observer
    fun getSucessfulBlockedUser(): MutableLiveData<String> {
        return mSucessfulBlockedUser
    }

    //error observer
    fun errorgetBlockedUser(): MutableLiveData<String> {

        return mFailureBlockedUser
    }


}
