package com.example.caiguru.buyer.homeBuyers.tagUser

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

class TagRepository(var application: Application) {

    //*********************** get_tag_users webservices***************//
    val mSuccessfulgetTagUser = MutableLiveData<ArrayList<TagModel>>()
    var mError = MutableLiveData<String>()
    val getTagArray = ArrayList<TagModel>()
    fun getTagUser(postId: String, search: String, page: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_tag_users(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), page, search, postId
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    if (page == "1") {
                        getTagArray.clear()
                    }
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        val followers = json.optJSONArray("followers").toString()
                        val gson = Gson()
                        val arrayData: ArrayList<TagModel> =
                            gson.fromJson(
                                followers,
                                object : TypeToken<ArrayList<TagModel>>() {}.type
                            )
                        getTagArray.addAll(arrayData)
                        mSuccessfulgetTagUser.value = getTagArray

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

    fun mSuccessfulgetTagUser(): MutableLiveData<ArrayList<TagModel>> {


        return mSuccessfulgetTagUser
    }

    fun mError(): MutableLiveData<String> {

        return mError
    }

    //***************************post_tag_user*************//
    var mSucessfulPostTag = MutableLiveData<String>()
    var mErrorPostTag = MutableLiveData<String>()
    fun postTaggedUser(tagUserId: String, postId: String, status: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.post_tag_user(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), postId, tagUserId,status
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    val message = json.optString("message_${Constant.getLocal(application)}")

                    if (status == "true") {

                        mSucessfulPostTag.value = message

                    } else {
                        mErrorPostTag.value = message
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mErrorPostTag.value = message
            }
        })

    }

    fun mSucessfulPostTag(): MutableLiveData<String> {
        return mSucessfulPostTag
    }

    fun mErrorPostTag(): MutableLiveData<String> {
        return mErrorPostTag
    }
}
