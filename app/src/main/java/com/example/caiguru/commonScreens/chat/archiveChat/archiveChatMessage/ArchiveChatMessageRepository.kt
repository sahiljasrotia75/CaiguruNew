package com.example.caiguru.commonScreens.chat.archiveChat.archiveChatMessage

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.chat.chatMessage.MessageItem
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
import java.lang.Exception
import kotlin.collections.ArrayList

class ArchiveChatMessageRepository(var application: Application) {
    private var myProfile: GetProfileModel
    private lateinit var intentData: ModelChat
    private val messagesList = MutableLiveData<List<MessageItem>>()
    private val mError = MutableLiveData<ErrorModel>()
    var list = ArrayList<MessageItem>()
    var lastId = "0"

    init {

        val gson = Gson()
        val json = Constant.getPrefs(application).getString(Constant.PROFILE, "{}")
        myProfile = gson.fromJson(json, GetProfileModel::class.java)
        // getDummyMessages()
    }

    fun getMessages(): LiveData<List<MessageItem>> {
        return messagesList
    }


    fun postModel(model: ModelChat) {
        intentData = model
        getMessagesList()
    }

    private fun getMessagesList() {
        val prefs = Constant.getPrefs(application)
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getChatMessages(
            "Bearer " + prefs.getString(
                Constant.token,
                ""
            ), intentData.channel_id, lastId
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
              try {

                val res = response.body()!!.string()
                val json = JSONObject(res)
                val status = json.optString("status")
                if (status == "true") {

                    val jsonMsg = json.optJSONArray("messages")
                    lastId = json.optString("last_id")

                    val gson = Gson()
                    val messages: ArrayList<MessageItem> =
                        gson.fromJson(
                            jsonMsg.toString(),
                            object : TypeToken<ArrayList<MessageItem>>() {}.type
                        )

                    val itr = list.iterator()
                    while (itr.hasNext()) {
                        val x = itr.next()
                        if (x.id == "")
                            itr.remove()
                    }

                    list.addAll(messages)

                    messagesList.value = list

                } else {
                    val model = ErrorModel()
                    model.message = "Network Error"
                    mError.value = model
                }
            }  catch (e:Exception){

            }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mError.value = model
            }

        })
    }


}
