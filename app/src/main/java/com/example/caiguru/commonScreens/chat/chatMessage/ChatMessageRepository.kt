package com.example.caiguru.commonScreens.chat.chatMessage

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.chat.ModelChat
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ChatMessageRepository(var application: Application) {
    private var time2: Long = 0
    private var time: Long = 0
    private var myProfile: GetProfileModel
    private lateinit var intentData: ModelChat
    private val messagesList = MutableLiveData<List<MessageItem>>()
    private val mError = MutableLiveData<ErrorModel>()
    var list = ArrayList<MessageItem>()
    var lastId = "0"
    //  var android_last_id = "0"
    var stopGetMessages = true

    init {

        val gson = Gson()
        val json = Constant.getPrefs(application).getString(Constant.PROFILE, "{}")
        myProfile = gson.fromJson(json, GetProfileModel::class.java)
    }

    fun getMessages(): LiveData<List<MessageItem>> {
        return messagesList
    }

    //**************************send message api****************//
    fun sendMessage(messageText: String, time: Long) {
        val message = MessageItem()
        message.created_at = application.getString(R.string.Just_Now)
        message.message = messageText
        message.user_id = ""
        // this.time = System.currentTimeMillis()
//        if (time > 0) {
//            time2 = 0
//            time2 = time
//        }
        //this.time = System.currentTimeMillis()

        sendMessageService(message)

    }
//************************send messages*********************//

    private fun sendMessageService(message: MessageItem) {
        val prefs = Constant.getPrefs(application)
        val retrofit = Constant.getRestClient()

         // stopGetMessages = true

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.sendMessage(
            "Bearer " + prefs.getString(
                Constant.token,
                ""
            ), intentData.channel_id, message.message, message.image, lastId
        )
        Log.e("SendMessageLastId1", lastId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    if (status == "true") {
                        val jsonMsg = json.optJSONArray("lastMessage")
                        //lastId = json.optString("last_id")
                        lastId = json.optString("android_last_id")
                        Log.e("SendMessageLastId2", lastId)
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
                        Log.e("SendMessageeee", message.message)
                        list.addAll(messages)
                        if (list.isNotEmpty()) {
                            messagesList.value = list
                        }


                    } else {
                        val model = ErrorModel()
                        model.message = application.getString(R.string.Network_error)
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


    //******************get message of user everytime********************//
    fun getMessagesConinous(model: ModelChat) {
        intentData = model
        val t = Timer()
        //Set the schedule function and rate
        t.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    if (time2 >= 3000) {
                        time2 = 0
                        Log.e("SendMessvryTimeGetTimer", time2.toString())
                        stopGetMessages = false
                    }

                    if (!stopGetMessages) {
                        getMessageEveryTime()
                    }
                }
            },
            //Set how long before to start calling the TimerTask (in milliseconds)
            0,
            //Set the amount of time between each execution (in milliseconds)
            3000
        )

    }

    fun getMessageEveryTime() {
        val prefs = Constant.getPrefs(application)
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_bg_messages(
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
                        Log.e("SendMessageevryTimeGet", messages.toString())

                        if (messages.isNotEmpty()) {
                            list.addAll(messages)
                            messagesList.value = list
                        }


                    } else {
                        val model = ErrorModel()
                        model.message =application.getString(R.string.network_error)
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

//********************get the message first time******************//
    fun getMessageFirstTime(model: ModelChat) {
        val time = System.currentTimeMillis()
        if (time > 0) {
            time2 = 0
            time2 = time
        } else {
            time2 = time
        }
        intentData = model

        getMessagesFirst()

    }
//********************get the message first time
    private fun getMessagesFirst() {
       // stopGetMessages = true
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
                        Log.e("SendMessageLastId0", lastId)
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
                        Log.e("SendMessaggetFirstTime", messages.toString())

                        if (messages.isNotEmpty()) {
                            list.addAll(messages)
                            messagesList.value = list
                        }


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

    fun uploadImage(picturePath: File) {

        val message = MessageItem()
        message.created_at = application.getString(R.string.Just_Now)
        message.msgType = "2"
        message.image = picturePath.path
        list.add(message)
        messagesList.value = list
        val services = Constant.getRestClient().create(WebServices::class.java)
        //pass list like this

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), picturePath)
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("image", picturePath.name, requestFile)

        val call: Call<ResponseBody> = services.uploadimage(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val res = response.body()!!.string()
                    //get the data from the object
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    val message = json.optString("message_${Constant.getLocal(application)}")
                    if (status == "true") {

                        val image = json.optString("image")


                        val message = MessageItem()
                        message.created_at = application.getString(R.string.Just_Now)
                        message.image = image
                        message.user_id = ""

                        sendMessageService(message)

                    } else {

                        val errormsg = message
                        //mFailuree.value = errormsg
                    }

                }

            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
              //  val errormsg = application.getString(R.string.network_error)
                // mFailure.value = errormsg


            }


        })

    }


    //*********************stop backGrondApi
    fun stopBackGroundService() {
        stopGetMessages = true
    }
}
