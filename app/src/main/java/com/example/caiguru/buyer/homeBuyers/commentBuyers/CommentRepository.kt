package com.example.caiguru.buyer.homeBuyers.commentBuyers

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
import java.net.URLEncoder

class CommentRepository(var application: Application) {

    //***********************get_feed_comments***************//
    val mSuccessfulgetFeedComment = MutableLiveData<ArrayList<CommentModel>>()
    var mError = MutableLiveData<String>()
    val feedArrayGlobal = ArrayList<CommentModel>()
    fun getCommentFeed(postId: String, page: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_feed_comments(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), postId, page
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    if (page == "1") {
                        feedArrayGlobal.clear()
                    }
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {

                        val comments = json.optJSONArray("comments").toString()
                        val gson = Gson()
                        val arrayData: ArrayList<CommentModel> =
                            gson.fromJson(
                                comments,
                                object : TypeToken<ArrayList<CommentModel>>() {}.type
                            )

                        feedArrayGlobal.addAll(arrayData)
                        mSuccessfulgetFeedComment.value = feedArrayGlobal

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mError.value = msg
                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.Network_Failure)
                mError.value = message
            }
        })
    }

    fun mSuccessfulgetFeedComment(): MutableLiveData<ArrayList<CommentModel>> {


        return mSuccessfulgetFeedComment
    }

    fun mError(): MutableLiveData<String> {

        return mError
    }

    //*******************set feed comment***************//
   // var mSucesssful = MutableLiveData<String>()

    fun setFeedComment(postId: String, comment: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.set_feed_comments(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), postId, URLEncoder.encode(comment, "UTF-8")
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        getCommentFeed(postId, "1")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })

    }
//    fun mSucesssful(): MutableLiveData<String> {
//
//        return mSucesssful
//    }


}
