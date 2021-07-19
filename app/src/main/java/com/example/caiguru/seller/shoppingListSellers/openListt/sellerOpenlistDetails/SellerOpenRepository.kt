package com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails.ListModel
import com.example.caiguru.seller.shoppingListSellers.openListt.openList.ListParentModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SellerOpenRepository(var application: Application) {
    var mSucessful = MutableLiveData<ArrayList<ListModel>>()
    var mFaillure = MutableLiveData<ErrorModel>()
    val list = ArrayList<ListModel>()
    var arrayParentModel = ArrayList<ListParentModel>()

    fun getSeller(page: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_seller_shopping_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token, ""
            ), "1", page
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {


                    if (page == "1") {
                        arrayParentModel.clear()
                    }

                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")

                    if (status == "true") {
                        //list name
                        val lists1 = json.optJSONArray("lists")
                        val list2 = ArrayList<ListModel>()
                        for (i in 0 until lists1.length()) {
                            val jsonObject = lists1.optJSONObject(i)
                            val listModel = ListModel()
                            listModel.cat_id = jsonObject.optString("cat_id")
                            listModel.count = jsonObject.optInt("count")

                            val arrayParentModel1 = ArrayList<ListParentModel>()
                            val lists = jsonObject.optJSONArray("lists")
                            for (item in 0 until lists.length()) {
                                val jsonObject = lists.optJSONObject(item)
                                val listParentModel = ListParentModel()
                                listParentModel.id = jsonObject.optString("id")
                                listParentModel.cat_id = jsonObject.optString("cat_id")
                                listParentModel.payment_methods = jsonObject.optString("payment_methods")
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
                                arrayParentModel1.add(listParentModel)
                            }

                            listModel.lists = arrayParentModel1

                            list2.add(listModel)

                        }

                        mSucessful.value = list2
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

    fun mSucessful(): MutableLiveData<ArrayList<ListModel>> {

        return mSucessful
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return mFaillure
    }


}
