package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
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
import java.lang.Exception

class CustomerRequestListRepository(var application: Application) {
    var mSucessfulGetbuyerList = MutableLiveData<CustomerChildModel>()
    var mFailureGetbuyerList = MutableLiveData<ErrorModel>()

    fun getBuyerList(list_id: String) {
        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.get_buyer_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), list_id
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
                        model.created_at = list.optString("created_at")
                        model.name = list.optString("name")
                        model.level = list.optString("level")
                        model.image = list.optString("image")
                        model.quote_requested = list.optString("quote_requested")
                        model.comission_per = list.optString("comission_per")
                        model.amount = list.optString("amount")
                        model.credits = list.optString("credits")
                        model.product_details = list.optJSONArray("product_details").toString()
                        //getting the array fro object
                        model.delivery_location =
                            list.optJSONArray("delivery_location").toString()
                        model.delivery_daytime = list.optJSONArray("delivery_daytime").toString()

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e("error ::::::", e.message.toString())
                    }
                    mSucessfulGetbuyerList.value = model

                } else {

                    val model = ErrorModel()
                    model.message = application.getString(R.string.Network_error)
                    mFailureGetbuyerList.value = model
                }
            }

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


    //********************************request buyer list********************//

    var mSucessfulRequestBuyerList = MutableLiveData<String>()
    var mFailureRequestBuyerList = MutableLiveData<String>()
    fun setRequestBuyerList(parentModel: CustomerChildModel, products: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.request_buyer_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),
            parentModel.id,
            parentModel.buyer_id,
            products,
            parentModel.totals,
            parentModel.comission,
            parentModel.payment_methods

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
                            mSucessfulRequestBuyerList.value = message
                        } else {
                            mFailureRequestBuyerList.value = message
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val message = application.getString(R.string.Network_error)
                    mFailureRequestBuyerList.value = message
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = application.getString(R.string.network_error)
                mFailureRequestBuyerList.value = message
            }

        })

    }

    fun mSucessfulRequestBuyerList(): MutableLiveData<String> {

        return mSucessfulRequestBuyerList
    }

    fun mFailureRequestBuyerList(): MutableLiveData<String> {

        return mFailureRequestBuyerList
    }

    //**************************web service of upload image******************//
    var mSucessfullUploadImage = MutableLiveData<String>()
    var mFailureeUploadImage = MutableLiveData<String>()
    fun uploadProfileImage(uri: File?) {
        val retrofit = Constant.getRestClient()


        val services = retrofit.create(WebServices::class.java)
        //pass list like this
        val file = uri
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        // MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("image", file?.name, requestFile)


        val call: Call<ResponseBody> = services.uploadimage(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // val model = DialogModel()
                if (response.isSuccessful) {
                    try {

                        val res = response.body()!!.string()
                        //get the data from the object
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val message = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val image = json.optString("image")
                            //model.message = message
                            mSucessfullUploadImage.value = image
                        } else {
                            val errormsg = message
                            mFailureeUploadImage.value = errormsg
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errormsg = application.getString(R.string.network_error)
                mFailureeUploadImage.value = errormsg
            }
        })
    }

    //status true observer
    fun mSucessfullUploadImage(): MutableLiveData<String> {
        return mSucessfullUploadImage
    }

    //error observer
    fun mFailureeUploadImage(): MutableLiveData<String> {

        return mFailureeUploadImage
    }
    //**********************report List Api
    var mFailureReposrtlist = MutableLiveData<String>()
    var mSucessfulREportList = MutableLiveData<String>()

    fun reportList(reason: String, data: CustomerChildModel) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.report_list(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ),"application/json",data.id,"2",reason

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")
                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {

                            mSucessfulREportList.value = msg

                        } else {
                            val msg = msg
                            mFailureReposrtlist.value = msg.toString()

                        }
                    }

                } catch (e: Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailureReposrtlist.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureReposrtlist.value = msg
            }

        })

    }


    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mFailureReposrtlist
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mSucessfulREportList
    }

}


