package com.example.caiguru.buyer.buyerProfile.followings

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.buyerProfile.follower.FollowUnfollowModel
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

class FollowingBuyerRepository(var application: Application) {

    var mSucessfulFollowing = MutableLiveData<ArrayList<FollowUnfollowModel>>()
    var mFailure = MutableLiveData<String>()
    var modelData = ArrayList<FollowUnfollowModel>()
    fun setFollowing(page: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_follow_data(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), "1", page

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    val model = GetProfileModel()
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            if (page == "1") {
                                modelData.clear()
                            }
                            model.count = json.optString("count").toInt()
                            val resul = json.optJSONArray("followers").toString()

                            val gson = Gson()
                            val allData: ArrayList<FollowUnfollowModel> =
                                gson.fromJson(
                                    resul,
                                    object : TypeToken<ArrayList<FollowUnfollowModel>>() {}.type
                                )
                            modelData.addAll(allData)
                            mSucessfulFollowing.value = modelData

                        } else {
                            val msg = R.string.network_error
                            mFailure.value = msg.toString()

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailure.value = msg
            }

        })

    }

    fun mSucessfulFollowing(): LiveData<ArrayList<FollowUnfollowModel>> {
        return mSucessfulFollowing
    }

    fun mFailure(): MutableLiveData<String> {

        return mFailure
    }

    //**********************follow unfollow service*****************//
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
//                        val gson = Gson()
//                        val modelData: GetProfileModel =
//                            gson.fromJson(
//                                json.toString(),
//                                object : TypeToken<GetProfileModel>() {}.type
//                            )
                        // modelData[position]=list
                        modelData.set(position, list)
                        mSucessfulFollowing.value = modelData
                        // {"status":"true","follow_status":"1"}
                    } else {
                        val msg = application.getString(R.string.network_error)
                        mFailure.value = msg
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailure.value = errormsg
            }
        })
    }


    //status true observer
//    fun mSucessfullFollowUnfollow(): MutableLiveData<GetProfileModel> {
//        return mSucessfullFollowUnfollow
//    }
//
//    //error observer
//    fun mFailureeFollowUnfollow(): MutableLiveData<String> {
//
//        return mFailureeFollowUnfollow
//    }


}
