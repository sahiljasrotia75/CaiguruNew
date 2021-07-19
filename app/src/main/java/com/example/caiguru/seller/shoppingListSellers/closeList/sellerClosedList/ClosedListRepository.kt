package com.example.caiguru.seller.shoppingListSellers.closeList.sellerClosedList

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

class ClosedListRepository(var application: Application) {


    var mSucessful = MutableLiveData<ArrayList<ListParentModel>>()
    var mFaillure = MutableLiveData<ErrorModel>()
    var arrayParentModel = ArrayList<ListParentModel>()


    fun getSellerCloseList(page: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_seller_shopping_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token, ""
            ), "2", page
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
                        for (i in 0 until lists1.length()) {
                            val jsonObject = lists1.optJSONObject(i)
                            val listParentModel = ListParentModel()
                            listParentModel.cat_id = jsonObject.optString("cat_id")
                            //list name
                            val lists = jsonObject.optJSONArray("lists")
                            for (item in 0 until lists.length()) {
                                val jsonObject = lists.optJSONObject(item)
                                val listParentModel = ListParentModel()
                                listParentModel.id = jsonObject.optString("id")
                                listParentModel.cat_id = jsonObject.optString("cat_id")
                                listParentModel.listingname = jsonObject.optString("listingname")
                                listParentModel.comission_per = jsonObject.optString("comission_per")
                                listParentModel.created_at = jsonObject.optString("created_at")
                                listParentModel.delivery_days =
                                    jsonObject.optJSONArray("delivery_days").toString()
                                listParentModel.pickup_details = jsonObject.optString("pickup_details")
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

    //*******************************open Again WebService*******************************//

    var mFaillureCloseList = MutableLiveData<String>()
    var mSucessfulCloseList1 = MutableLiveData<String>()

    fun openAgainList(
        mData: ListParentModel,
        position: Int
    ) {


        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.open_again(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), mData.id
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    try {


                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
//                            val model = ListParentModel()
//                            var message = message
//                            openAgainArrayModel.add(model)
//                          arrayParentModel.removeAt(position)
//                            mSucessful.value = arrayParentModel
                            mSucessfulCloseList1.value = message

                        } else {
                            val msg = message
                            mFaillureCloseList.value = msg

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)

                mFaillureCloseList.value = message
            }


        })
    }

//    fun mSucessfulOpenAgainList(): MutableLiveData<ArrayList<ListParentModel>> {
//
//        return mSucessfulCloseList
//    }

    fun mFailureOpenAgainList(): MutableLiveData<String> {

        return mFaillureCloseList
    }

    fun mSucessfulCloseList1(): MutableLiveData<String> {

        return mSucessfulCloseList1
    }


}



