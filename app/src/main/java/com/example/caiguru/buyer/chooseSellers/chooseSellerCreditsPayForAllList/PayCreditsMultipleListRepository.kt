package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForAllList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class PayCreditsMultipleListRepository(var application: Application) {
    var mSucessfulSingleListData = MutableLiveData<PayCreditsResultModel>()
    var mFailureSingleListData = MutableLiveData<String>()

    fun setSingleListCredits(
        jsonconvertModel: String,
        arrayModel: ArrayList<ChooseSellerShoppingModel>,
        models: DeliveryZoneModel,
        partialComissions: Double
    ) {
        var id = ""
        var total = ""
        for (item in arrayModel) {
            id = item.seller_id
            //credits = item.PartialComission
            total = item.total

        }
        val jsonobj = JSONObject()
        jsonobj.put("lat", models.lat)
        jsonobj.put("long", models.long)
        jsonobj.put("full_address", models.address)

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.buy_listing(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), id, jsonconvertModel, partialComissions.toString(), jsonobj.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val model = PayCreditsResultModel()
                            model.req_ids = json.optString("req_ids")
                           // val listing = json.getJSONArray("listings").toString()

                            val gson = Gson()
                            val modelArray: PayCreditsResultModel=
                                gson.fromJson(
                                    json.toString(),
                                    object : TypeToken<PayCreditsResultModel>() {}.type
                                )
                            mSucessfulSingleListData.value = modelArray

                        } else {

                            val msg = msg
                            mFailureSingleListData.value = msg
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }else{

                    val msg = application.getString(R.string.Network_error)
                    mFailureSingleListData.value = msg
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureSingleListData.value = msg
            }


        })

    }

    fun mSucessfulSingleListData(): MutableLiveData<PayCreditsResultModel> {

        return mSucessfulSingleListData
    }

    fun mFailureSingleListData(): MutableLiveData<String> {

        return mFailureSingleListData
    }


}

