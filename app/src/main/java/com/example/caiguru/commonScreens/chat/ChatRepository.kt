package com.example.caiguru.commonScreens.chat

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
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
import java.util.*
import kotlin.collections.ArrayList

class ChatRepository(var application: Application) {

    private val mConnections = MutableLiveData<List<ModelChat>>()
    private val mChats = MutableLiveData<List<ChatParent>>()
    private val mError = MutableLiveData<ErrorModel>()

    init {
        // getDummyConnections()
        // getDummyChat()


        val t = Timer()
        //Set the schedule function and rate
        t.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    getChatChannels()
                }
            },
            //Set how long before to start calling the TimerTask (in milliseconds)
            0,
            //Set the amount of time between each execution (in milliseconds)
            5000
        )
    }


    fun observeLatestConnections(): LiveData<List<ModelChat>> {
        return mConnections
    }

    fun observeChats(): LiveData<List<ChatParent>> {
        return mChats
    }

    /* private fun getDummyConnections() {
         val mData = ArrayList<ModelChat>()
         var model = ModelChat()
         model.name = "User 1"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.name = "User 2"
         model.image =
             "https://www.imagesgoodnight.com/wp-content/uploads/2019/01/Girls-Whatsapp-Profile-Images-1-300x259.jpg"
         model.userTag = "2"
         mData.add(model)

         model = ModelChat()
         model.name = "User 3"
         model.image = "https://cdnaws.sharechat.com/1534169931478_8306_compressed_40.jpg"
         model.userTag = "1"
         mData.add(model)

         model = ModelConnection()
         model.name = "User 4"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.userTag = "1"
         mData.add(model)

         model = ModelConnection()
         model.name = "User 5"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.userTag = "2"
         mData.add(model)

         mConnections.value = mData
     }*/

    private fun getChatChannels() {
        // code to call web service for latest connections && Chat channels
        val prefs = Constant.getPrefs(application)
        val userType = prefs.getString(Constant.type, "1")
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getChatChannels(
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
                val logout = json.optString("logout")
                if (status == "true") {
                    // Latest connections list
                    val jsonConnections = json.optJSONArray("latest_channels")
                    val gson = Gson()
                    val latestChannels: ArrayList<ModelChat> =
                        gson.fromJson(
                            jsonConnections.toString(),
                            object : TypeToken<ArrayList<ModelChat>>() {}.type
                        )
                    mConnections.value = latestChannels

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
                    if (logout=="false") {
                        val model = ErrorModel()
                        model.message = application.getString(R.string.Network_Failure)
                        mError.value = model
                    }
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

    /* private fun getDummyChat() {
         val mData = ArrayList<ModelChat>()
         var model = ModelChat()
         model.title = "User 1"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.userTag = "1"
         model.message = "This is the dummy message to be shaown in last message"
         mData.add(model)

         model = ModelChat()
         model.title = "User 2"
         model.image =
             "https://www.imagesgoodnight.com/wp-content/uploads/2019/01/Girls-Whatsapp-Profile-Images-1-300x259.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "2"
         mData.add(model)

         model = ModelChat()
         model.title = "User 3"
         model.image = "https://cdnaws.sharechat.com/1534169931478_8306_compressed_40.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.title = "User 4"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.title = "User 5"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "2"
         mData.add(model)

         model = ModelChat()
         model.title = "User 2"
         model.image =
             "https://www.imagesgoodnight.com/wp-content/uploads/2019/01/Girls-Whatsapp-Profile-Images-1-300x259.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "2"
         mData.add(model)

         model = ModelChat()
         model.title = "User 3"
         model.image = "https://cdnaws.sharechat.com/1534169931478_8306_compressed_40.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.title = "User 4"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.title = "User 5"
         model.message = "This is the dummy message to be shaown in last message"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.userTag = "2"
         mData.add(model)
         model = ModelChat()
         model.title = "User 2"
         model.image =
             "https://www.imagesgoodnight.com/wp-content/uploads/2019/01/Girls-Whatsapp-Profile-Images-1-300x259.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "2"
         mData.add(model)

         model = ModelChat()
         model.title = "User 3"
         model.image = "https://cdnaws.sharechat.com/1534169931478_8306_compressed_40.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.title = "User 4"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "1"
         mData.add(model)

         model = ModelChat()
         model.title = "User 5"
         model.image = "https://i.pinimg.com/originals/be/ac/96/beac96b8e13d2198fd4bb1d5ef56cdcf.jpg"
         model.message = "This is the dummy message to be shaown in last message"
         model.userTag = "2"
         mData.add(model)

         mChats.value = mData
     }*/
}
