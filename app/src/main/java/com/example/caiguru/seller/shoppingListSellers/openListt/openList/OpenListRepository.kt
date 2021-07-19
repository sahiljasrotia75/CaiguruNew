package com.example.caiguru.seller.shoppingListSellers.openListt.openList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenListRepository(var application: Application) {

    var mdata = MutableLiveData<ArrayList<ListParentModel>>()
    var mError = MutableLiveData<ErrorModel>()
    var arrayParentModel = ArrayList<ListParentModel>()


    fun getSeller(page: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_seller_shopping_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
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
                        val lists = json.optJSONArray("lists")
                        for (i in 0 until lists.length()) {
                            val jsonObject = lists.optJSONObject(i)
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

                        mdata.value = arrayParentModel
                    } else {
                        val model = ErrorModel()
                        model.message = json.optString("message_${Constant.getLocal(application)}")
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

    fun sendListData(): MutableLiveData<ArrayList<ListParentModel>> {
        return mdata

    }

    fun sendListDataFailure(): MutableLiveData<ErrorModel> {
        return mError

    }


    //**************************close list web service************************//
    val mcloseListFailure = MutableLiveData<String>()
    val mcloseListSucessful = MutableLiveData<String>()
    var arraymodel = ArrayList<ListParentModel>()

    fun closeList(closeList: java.util.ArrayList<ListParentModel>, position: Int) {
        arraymodel = closeList

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.closelist(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), closeList[position].id
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {

                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    val message = json.optString("message_${Constant.getLocal(application)}")
                    if (status == "true") {

                        mcloseListSucessful.value = message

                    } else {
                        val msg = message
                        mcloseListFailure.value = msg

                    }

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)

                mcloseListFailure.value = message
            }


        })
    }


    fun mSucessfullcloseList(): MutableLiveData<String> {

        return mcloseListSucessful
    }

    fun mFailureCloseList(): MutableLiveData<String> {

        return mcloseListFailure
    }

    fun showOpenList(position: Int) {
        arrayParentModel[position].isExpanded = true
        mdata.value = arrayParentModel
    }


    //********pal pause api
    val mPlayPuaseSucessful = MutableLiveData<String>()
    fun SetPlayPauseApi(list: DialogModel) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.pauseplayproduct(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), list.list_id, list.id, list.status
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {

                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    val message = json.optString("message_${Constant.getLocal(application)}")
                    if (status == "true") {

                        mPlayPuaseSucessful.value = message

                    } else {
                        val msg = message
                        mPlayPuaseSucessful.value = msg

                    }

                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)

                mPlayPuaseSucessful.value = message
            }


        })
    }

    fun mSucessfulPlayPause(): MutableLiveData<String> {
        return mPlayPuaseSucessful
    }
}