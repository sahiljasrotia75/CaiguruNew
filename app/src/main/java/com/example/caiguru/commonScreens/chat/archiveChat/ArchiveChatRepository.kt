package com.example.caiguru.commonScreens.chat.archiveChat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.chat.ChatParent
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
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
import kotlin.collections.ArrayList

class ArchiveChatRepository(var application: Application) {

    private val mChats = MutableLiveData<List<ChatParent>>()
    private val mError = MutableLiveData<ErrorModel>()

    init {
        getChatChannels()
    }

    fun observeChats(): LiveData<List<ChatParent>> {
        return mChats
    }


    private fun getChatChannels() {
        // code to call web service for latest connections && Chat channels

        val prefs = Constant.getPrefs(application)
        val userType = prefs.getString(Constant.type, "1")
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getArchiveChatChannels(
            "Bearer " + prefs.getString(
                Constant.token,
                ""
            ), userType!!
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                val res = response.body()!!.string()
                val json = JSONObject(res)
                val status = json.optString("status")
                if (status == "true") {
                    val gson = Gson()

                    val channelList = ArrayList<ChatParent>()
                    // Channels list here grouped category wise
                    val jsonChannels = json.optJSONArray("channels")
                    for (i in 0 until jsonChannels.length()) {
                        val obj = jsonChannels.optJSONObject(i)
                        val parent = ChatParent()
                        parent.catId = obj.optString("cat_id")
                        parent.name = obj.optString("name")

                        val jsonChildChannels = obj.optJSONArray("channels")
                        parent.channels =
                            gson.fromJson(
                                jsonChildChannels.toString(),
                                object : TypeToken<ArrayList<ModelChat>>() {}.type
                            )
                        channelList.add(parent)
                    }
                    mChats.value = channelList

                } else {
                    val model = ErrorModel()
                    model.message = "Network Error"
                    mError.value = model
                }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mError.value = model
            }


        })
    }

    fun observeError(): LiveData<ErrorModel> {
        return mError
    }

    fun getChannels() {
        getChatChannels()
    }

}
