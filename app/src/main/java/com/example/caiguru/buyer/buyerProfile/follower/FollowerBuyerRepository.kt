package com.example.caiguru.buyer.buyerProfile.follower

import android.app.Application
import android.util.Log
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
import retrofit2.Retrofit
import java.lang.Exception

class FollowerBuyerRepository(var application: Application) {

    var mSucessfulFollower = MutableLiveData<ArrayList<FollowUnfollowModel>>()
    var mFailureFollower = MutableLiveData<String>()
    var modelData= ArrayList<FollowUnfollowModel>()
    fun setFollower(page: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_follow_data(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), "2", page

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    val model=GetProfileModel()
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            if (page=="1"){
                                modelData.clear()
                            }
                            model. count = json.optString("count").toInt()
                            val resul = json.optJSONArray("followers").toString()

                            val gson = Gson()
                            val modelData1:ArrayList<FollowUnfollowModel> =
                                gson.fromJson(
                                    resul,
                                    object : TypeToken<ArrayList<FollowUnfollowModel>>() {}.type
                                )
                            modelData.addAll(modelData1)
                            mSucessfulFollower.value = modelData

                        } else {
                            val msg = R.string.network_error
                            mFailureFollower.value = msg.toString()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureFollower.value = msg
            }

        })
    }
    fun mSucessfulFollower(): MutableLiveData<ArrayList<FollowUnfollowModel>> {

        return mSucessfulFollower
    }

    fun mFailureFollower(): MutableLiveData<String> {

        return mFailureFollower
    }

    //************************followunfollow webservice**********************//


    fun setFollowUnfollow(
        list: FollowUnfollowModel,
        position: Int,
        followed: String
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.follow_unfollow(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), list.user_id, followed
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        list.isFollowed = json.optString("follow_status")

                       // modelData[position]=list
                        modelData.set(position,list)
                        mSucessfulFollower.value = modelData
                        // {"status":"true","follow_status":"1"}

                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailureFollower.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureFollower.value = errormsg
            }
        })
    }
    

}
