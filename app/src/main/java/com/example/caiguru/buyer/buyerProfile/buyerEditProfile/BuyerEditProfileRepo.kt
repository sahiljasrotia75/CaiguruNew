package com.example.caiguru.buyer.buyerProfile.buyerEditProfile

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
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



class BuyerEditProfileRepo(var application: Application) {

    var mSucessfull = MutableLiveData<DialogModel>()
    var mFailuree = MutableLiveData<String>()
    fun setProfileImage(uri: File) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        //pass list like this
        val file = uri
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
        val call: Call<ResponseBody> = services.uploadimage(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val model = DialogModel()
                if (response.isSuccessful) {
                    try {

                        val res = response.body()!!.string()
                        //get the data from the object
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            val image = json.optString("image")

                            model.image = image
                            mSucessfull.value = model

                        } else {

                            val errormsg = message
                            mFailuree.value = errormsg
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailuree.value = errormsg
            }

        })
    }
    //status true observer
    fun getdatasucessful(): MutableLiveData<DialogModel> {
        return mSucessfull
    }

    //error observer
    fun errorget(): MutableLiveData<String> {

        return mFailuree
    }
    //**********************************update profile api************************************************//
    var mSucessfulupdateData = MutableLiveData<GetProfileModel>()
    var mFailureError = MutableLiveData<ErrorModel>()
    fun updatedProfile(
        edtname: String,
        updatedimage: String,
        editemail: String,
        categories: String,
        address: String,
        lat: String,
        long: String
    ) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.update_profile(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), edtname, updatedimage, editemail, categories, address, lat, long
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val resp = response.body()!!.string()
                        val json1 = JSONObject(resp)
                        val status = json1.optString("status")
                        val msg = json1.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val gson = Gson()
                            val model1: GetProfileModel = gson.fromJson(
                                json1.toString(),
                                object : TypeToken<GetProfileModel>() {}.type
                            )
                            model1.msg = msg
                            model1.image=updatedimage
                            Constant.getPrefs(application).edit().putString("profile", json1.toString()).apply()

                            mSucessfulupdateData.value = model1
                        }else{
                            val msg = json1.optString("message_${Constant.getLocal(application)}")
                            val model = ErrorModel()
                            model.message=msg
                            mFailureError.value = model
                        }

                    } catch (e: Exception) {

                    }
                } else {
                  //  Toast.makeText(application, application.getString(R.string.Network_Failure), Toast.LENGTH_SHORT).show()

                    Constant.showToast(application.getString(R.string.Network_Failure),application)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mFailureError.value = model

            }
        })
    }

    fun mSucessfulupdateData(): MutableLiveData<GetProfileModel> {

        return mSucessfulupdateData
    }

    fun mFailureError(): MutableLiveData<ErrorModel> {

        return mFailureError
    }
//*********************get profile Api
val mSuccessfulGetProfile = MutableLiveData<GetProfileModel>()
    var mError = MutableLiveData<ErrorModel>()

    fun getProfileData(token: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getprofile(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),"application/json", token, "1"
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    var model = GetProfileModel()
                    if (status == "true") {
                        val seller = json.optString("seller_active_status")

                        Constant.getPrefs(application).edit()
                            .putString(Constant.seller_active_status, seller).apply()

                        Constant.getPrefs(application).edit()
                            .putString(Constant.PROFILE, json.toString()).apply()

                        val gson = Gson()
                        model =
                            gson.fromJson(
                                json.toString(),
                                object : TypeToken<GetProfileModel>() {}.type)   // JSON to Object
                        mSuccessfulGetProfile.value = model


                    } else {
                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
                        mError.value = model
                    }
                    try {
                    } catch (e: Exception) {

                    }
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
    fun mSucessfulGetProfile(): MutableLiveData<GetProfileModel> {
        return mSuccessfulGetProfile
    }

    //error observer
    fun errorGetProfile(): MutableLiveData<ErrorModel> {
        return mError
    }
}
