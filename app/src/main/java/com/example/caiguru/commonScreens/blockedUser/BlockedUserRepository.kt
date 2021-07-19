package com.example.caiguru.commonScreens.blockedUser

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
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
import java.lang.Exception

class BlockedUserRepository(var application: Application) {
    //********************get all the data of blocked users
    var mSucessfulgetAllDataBlockedUser= MutableLiveData<ArrayList<BlockedUserModel>>()
    var mFailureAllDataBlockedUser = MutableLiveData<String>()
    var getAllBlockedUser = ArrayList<BlockedUserModel>()

    fun get_block_users(page: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_block_users(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),"application/json",page
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            if (page == "1") {
                                getAllBlockedUser.clear()
                            }
                            val block_users = json.optJSONArray("block_users").toString()

                            // convert JSON into java object

                            val gson = Gson()
                            val blockedData: ArrayList<BlockedUserModel> =
                                gson.fromJson(
                                    block_users,
                                    object : TypeToken<ArrayList<BlockedUserModel>>() {}.type
                                )
                            getAllBlockedUser.addAll(blockedData)
                            mSucessfulgetAllDataBlockedUser.value = getAllBlockedUser

                        } else {
                            mFailureAllDataBlockedUser.value = message

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val msg = R.string.network_error
                    mFailureAllDataBlockedUser.value = msg.toString()

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureAllDataBlockedUser.value = msg
            }

        })

    }
    fun mSucessfulgetAllDataBlockedUser(): MutableLiveData<ArrayList<BlockedUserModel>> {

        return mSucessfulgetAllDataBlockedUser
    }

    fun mFailureAllDataBlockedUser(): MutableLiveData<String> {

        return mFailureAllDataBlockedUser
    }

    //*********************blocked user*****************//
    val mSucessfulBlockedUser=MutableLiveData<String>()
    val mFailureBlockedUser=MutableLiveData<String>()

    fun blockedUser(data: BlockedUserModel) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.block_unblock_user(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), data.block_user_id.toString(), data.type.toString(),"2"
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
