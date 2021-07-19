package com.example.caiguru.seller.sellerSetting.sellerBuyCredits

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerBuyCreditsReposiory(var application: Application) {
    var mSucessfullData = MutableLiveData<ArrayList<BuyCreditsModel>>()
    var mFailure = MutableLiveData<String>()
    fun getCredits() {
        val dataArray = ArrayList<BuyCreditsModel>()
        var data = BuyCreditsModel()
        data.credits = "100"
        data.total = "3.99"
        data.hasselected = true
        dataArray.add(data)

        data = BuyCreditsModel()
        data.credits = "200"
        data.total = "6.99"
        data.hasselected = false
        dataArray.add(data)


        data = BuyCreditsModel()
        data.credits = "500"
        data.total = "16.99"
        data.hasselected = false
        dataArray.add(data)

        data = BuyCreditsModel()
        data.credits = "1000"
        data.total = "31.99"
        data.hasselected = false
        dataArray.add(data)


        mSucessfullData.value = dataArray
    }

    fun setData(): MutableLiveData<ArrayList<BuyCreditsModel>> {
        return mSucessfullData


    }

    //************************api of get purchase credits*****************//
    var mSucessfullBuyCreditsPack = MutableLiveData<BuyCreditsModel>()
    var mFailurePack = MutableLiveData<String>()

    fun getBuyCreditsPack(
        globalCreditsModel: BuyCreditsModel,
        allTransectionData: String
    ) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.update_wallet(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), globalCreditsModel.totalWithoutText, globalCreditsModel.credits,allTransectionData
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful){
                    try {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        if (status == "true") {
                            val model = BuyCreditsModel()
                            model.credits = json.optString("credits")
                            model.mseeage = json.optString("message_${Constant.getLocal(application)}")

                            mSucessfullBuyCreditsPack.value = model
                        } else {
                            val message = json.optString("message_${Constant.getLocal(application)}")
                            mFailurePack.value = message
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    val msg = application.getString(R.string.network_error)
                    mFailurePack.value = msg
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailurePack.value = message
            }
        })

    }

    //sucessful buyer pack
    fun mSucessfullBuyCreditsPacks(): MutableLiveData<BuyCreditsModel> {
        return mSucessfullBuyCreditsPack

    }

    //failure
    fun mFailure(): MutableLiveData<String> {
        return mFailurePack

    }

    //********************************************MercadoPago***************************************//

    var mSuccessMercado = MutableLiveData<String>()

    fun mercadopago(creditCost: Double) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.mercadopago(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            creditCost.toString()
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                    val status = json.optString("status")
                    if (status == "true") {
                        //   {"status":"true","preference_id":"128631035-5f95542a-e676-49cd-9ecc-df37fa169708"}
                        val preference_id = json.optString("preference_id")
                        mSuccessMercado.value = preference_id
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    val message = application.getString(R.string.network_error)
                    mFailurePack.value = message
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailurePack.value = message
            }
        })

    }
    //failure
    fun mSuccessMercado(): MutableLiveData<String> {
        return mSuccessMercado

    }

}
