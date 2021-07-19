package com.example.caiguru.commonScreens.dashBoardParentActivity

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyBackgroundServices : Service() {
    override fun onCreate() {


    }

    override fun onStart(intent: Intent?, startId: Int) {
        get_block_users()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun get_block_users() {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.updatepushtopics(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            )
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.e("bgApiHIt1Error","message")
                if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                    Log.e("bgApiHIt1","message")
                        if (status == "true") {
                        //  Constant.showToast("yogesh Singh PAthania",this@MYServiceClass2)
                            Log.e("bgApiHIt","hjfghasdgfsjd")
                           // Constant.BACKGROUND_API_HIT=false ab mai logout karke login karunga tab ni chalehi

                        }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }

        })
    }
}




