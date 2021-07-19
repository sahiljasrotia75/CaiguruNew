package com.example.caiguru.commonScreens.loginScreen

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository(var application: Application) {

    var mData = MutableLiveData<String>()
    var facebookData = MutableLiveData<ModelFacebook>()
    var mError = MutableLiveData<ErrorModel>()

    fun setLoginData(email: String, password: String) {
        val retrofit =Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.login(email,password)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            val token = json.optString("token")
                            val type = json.optString("type")
                            val switch_active = json.optString("switch_active")

                            //save the login Data in shared prefrence
                            val editor = Constant.getPrefs(application).edit()
                            editor.putString(Constant.token, token.toString())
                            editor.putString(Constant.type, type.toString())
                            editor.putString(Constant.switch_active, switch_active.toString())
                            editor.apply()
                            mData.value= status

                        }else {
                            val model = ErrorModel()
                            model.message = json.optString("message_${Constant.getLocal(application)}")
                            mError.value = model
                        }
                    } catch (e: Exception) {

                    }
                }else{
                    var error = response.errorBody().toString()
                    Toast.makeText(application, error, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mError.value = model
            }

        })
    }
    //status observer
    fun getdata(): MutableLiveData<String> {
        return mData
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {
        return mError
    }

    fun loginUsingFacebook(
        socialId: String?,
        url: String,
        userName: String,
        userEmail: String
    ) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.socialLogin(socialId,"2")
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        if (status == "true") {
                            if (json.optString("register") == "1") {
                                val model = ModelFacebook()
                                model.id = socialId!!
                                model.email = userEmail
                                model.name = userName
                                model.picture = url
                                facebookData.value = model
                            }else{
                                val token = json.optString("token")
                                val type = json.optString("type")
                                val switch_active = json.optString("switch_active")
                                //save the login Data in shared prefrence
                                val editor = Constant.getPrefs(application).edit()
                                editor.putString(Constant.token, token.toString())
                                editor.putString(Constant.type, type.toString())
                                editor.putString(Constant.switch_active, switch_active.toString())
                                editor.apply()
                                mData.value= status
                            }
                        }else{
                           Log.e("LOCALE: ", ""+Constant.getLocal(application))
                            val model = ErrorModel()
                            model.message = json.optString("message_${Constant.getLocal(application)}")
                            mError.value = model
                        }
                    } catch (e: Exception) {

                    }
                }else{
                    Toast.makeText(application, application.getString(R.string.network_error), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mError.value = model
            }

        })
    }

    fun observeFacebook(): LiveData<ModelFacebook> {
        return facebookData
    }


}

class ModelFacebook() : Parcelable{
    var id = ""
    var name = ""
    var email = ""
    var picture = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        name = parcel.readString().toString()
        email = parcel.readString().toString()
        picture = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(picture)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ModelFacebook> {
        override fun createFromParcel(parcel: Parcel): ModelFacebook {
            return ModelFacebook(parcel)
        }

        override fun newArray(size: Int): Array<ModelFacebook?> {
            return arrayOfNulls(size)
        }
    }
}
