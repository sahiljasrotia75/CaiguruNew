package com.example.caiguru.seller.shoppingListSellers.loadmorecloseList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoadMoreCloseListRepository(var application: Application) {
    var mSucessful = MutableLiveData<ArrayList<ListParentModel>>()
    var mFaillure = MutableLiveData<ErrorModel>()
    var arrayParentModel = ArrayList<ListParentModel>()

    //   var arrayParentModel = ArrayList<ListParentModel>()


    fun getallcloselist(catId: String, page: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_all_close_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), catId, page
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {

                    if (page == "0") {
                        arrayParentModel.clear()
                    }

                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")


                    if (status == "true") {

                        val lists = json.optJSONArray("lists")
                        for (i in 0 until lists.length()) {
                            val jsonObject = lists.optJSONObject(i)
                            val listParentModel = ListParentModel()
                            listParentModel.id = jsonObject.optString("id")
                            listParentModel.cat_id = jsonObject.optString("cat_id")
                            listParentModel.listingname = jsonObject.optString("listingname")
                            listParentModel.comission_per =
                                jsonObject.optString("comission_per")
                            listParentModel.created_at = jsonObject.optString("created_at")
                            listParentModel.delivery_days =
                                jsonObject.optJSONArray("delivery_days").toString()
                            listParentModel.pickup_details =
                                jsonObject.optString("pickup_details")
                            listParentModel.minimum_purchase_amount =
                                jsonObject.optString("minimum_purchase_amount")
                            //location
                            listParentModel.delivery_zone =
                                jsonObject.optJSONArray("delivery_zone").toString()
                            //product deteils
                            listParentModel.product_details =
                                jsonObject.optJSONArray("product_details").toString()
                            listParentModel.amount = jsonObject.optString("amount")
                            listParentModel.allow_modify = jsonObject.optString("allow_modify")
                            listParentModel.credits = jsonObject.optString("credits")

                            //add data
                            arrayParentModel.add(listParentModel)
                        }
                        mSucessful.value = arrayParentModel
                    } else {
                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
                        mFaillure.value = model
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mFaillure.value = model
            }
        })
    }

    fun mSucessful(): MutableLiveData<ArrayList<ListParentModel>> {
        return mSucessful
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return mFaillure
    }

}
