package com.example.caiguru.buyer.buyerLists.buyerShopListModify

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuyerShopListModifyRepo(var application: Application) {

    var mSucessfulGetbuyerList = MutableLiveData<CustomerChildModel>()
    var mFailureGetbuyerList = MutableLiveData<ErrorModel>()

    fun setBuyerShoplist(id: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_buyer_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()!!.string()
                val json = JSONObject(res)
                val status = json.optString("status")
                if (status == "true") {
                    val model = CustomerChildModel()
                    try {


                        val list = json.optJSONObject("list")
                        model.id = list.optString("id")
                        model.buyer_id = list.optString("buyer_id")
                        model.cat_id = list.optString("cat_id")
                        model.listingname = list.optString("listingname")
                        model.name = list.optString("name")
                        model.level = list.optString("level")
                        model.image = list.optString("image")
                        model.quote_requested = list.optString("quote_requested")
                        model.comission_per = list.optString("comission_per")
                        model.amount = list.optString("amount")
                        model.credits = list.optString("credits")

                        //getting the array fro object
                        model.delivery_location =
                            list.optJSONArray("delivery_location").toString()
                        model.delivery_daytime = list.optJSONArray("delivery_daytime").toString()

                        //gettig string from array
                        model.created_at = list.optString("created_at")

                        //getting the array
                        model.product_details = list.optJSONArray("product_details").toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    mSucessfulGetbuyerList.value = model

                } else {

                    val model = ErrorModel()
                    model.message = application.getString(R.string.network_error)
                    mFailureGetbuyerList.value = model
                }            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mFailureGetbuyerList.value = model
            }

        })

    }
    fun SucessfulGetbuyerList(): MutableLiveData<CustomerChildModel> {

        return mSucessfulGetbuyerList
    }

    fun FailureGetbuyerList(): MutableLiveData<ErrorModel> {

        return mFailureGetbuyerList
    }

    //********************delete shopping  list
    var mSucessfulDeleteList=MutableLiveData<String>()
    var mFailureDeleteList=MutableLiveData<String>()
    fun deleteShoppingList(listId: String) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(Constant.BASE_URL)
//            .build()
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.buyer_delete_shopping_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), listId
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()!!.string()
                val json = JSONObject(res)
                val status = json.optString("status")
                if (status == "true") {
                    try {
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        mSucessfulDeleteList.value = message


                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                } else {
                    val message = json.optString("message_${Constant.getLocal(application)}")


                    mFailureDeleteList.value = message
                }

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)
                mFailureGetbuyerList.value = model
            }

        })

    }

    fun mSucessfulDeleteList(): MutableLiveData<String> {

        return mSucessfulDeleteList
    }
    fun mFailureDeleteList(): MutableLiveData<String> {

        return mFailureDeleteList
    }
}
