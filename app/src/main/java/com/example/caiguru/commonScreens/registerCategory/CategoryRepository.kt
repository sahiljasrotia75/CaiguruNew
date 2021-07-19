package com.example.caiguru.commonScreens.registerCategory

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.registerActivity.RegisterModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class CategoryRepository(var application: Application) {
    var mSucessful = MutableLiveData<CategoryModel>()
    var mFailure = MutableLiveData<ErrorModel>()


    fun setData(data: RegisterModel) {
        setDatas(data)
    }

    private fun setDatas(data: RegisterModel) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.register(
            data.name,
            data.email,
            data.password,
            data.social_type,
            data.social_id,
            data.referral_code,
            data.categories,
            data.image

        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        val modeldata = CategoryModel()
                        if (status == "true") {
                            modeldata.msg = msg
                            val token = json.optString("token")
                            val type = json.optString("type")
                            val switch_active = json.optString("switch_active")
                            modeldata.status = status


                            //save the authcode in shared prefrence
                            val editors = Constant.getPrefs(application).edit()
                            editors.putString(Constant.token, token)
                            editors.putString(Constant.type, type)
                            editors.putString(Constant.switch_active, switch_active)
                            editors.apply()
                            //save the data
                            mSucessful.value = modeldata

                        } else {
                            val modelStore = ErrorModel()
                            modelStore.errormsg = msg//creating a new observer
                            modelStore.status = status
                            mFailure.value = modelStore

                        }

                    } catch (e: Exception) {
                        e.printStackTrace()

                    }
                } else {

                    val msg = application.getString(R.string.network_error)
                    val model = ErrorModel()
                    model.errormsg = msg
                    mFailure.value = model

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.errormsg = application.getString(R.string.network_error)
                mFailure.value = model

            }

        })


    }


    //status true observer
    fun getdata(): MutableLiveData<CategoryModel> {
        return mSucessful
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {

        return mFailure
    }

    //***********************************update_categories******************************//

    var mSucessfulUpdate = MutableLiveData<CategoryModel>()
    var mFailureError = MutableLiveData<ErrorModel>()

    fun update_categories(updateCategories: String) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.update_categories(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), updateCategories
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    // var msg = json.optString("message_${Constant.getLocal(application)}")
                    val model = CategoryModel()
                    if (status == "true") {
                        model.category_id = json.optString("categories")
                        model.msg = json.optString("message_${Constant.getLocal(application)}")
                        mSucessfulUpdate.value = model

                    } else {

                        val modelStore = ErrorModel()
                        modelStore.errormsg = model.msg//creating a new observer
                        modelStore.status = status
                        mFailureError.value = modelStore

                    }
                } catch (e:Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.errormsg = application.getString(R.string.network_error)
                mFailureError.value = model
            }

        })

    }

    //status true observer
    fun getdataupdate(): MutableLiveData<CategoryModel> {
        return mSucessfulUpdate
    }

    //error observer
    fun errorgett(): MutableLiveData<ErrorModel> {

        return mFailureError
    }

}




