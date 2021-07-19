package com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.savedProductSeller

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.seller.sellerChooseCategory.SellerCategoryModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedProductRepository(var application: Application) {

    //**********************report List Api
    var mFailure = MutableLiveData<String>()
    var mSucessfulSavedList = MutableLiveData<ArrayList<ShoppingListModel>>()
    val localArray = ArrayList<ShoppingListModel>()
    fun getSavedProductData(categoryData: SellerCategoryModel?, page: Int) {
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getsavedproducts(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), categoryData!!.category_id, "","1"

        )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    if (response.isSuccessful) {
                        if (page == 1) {
                            localArray.clear()
                        }
                        val res = response.body()!!.string()
                        val json = JSONObject(res)
                        val status = json.optString("status")

                        val msg = json.optString("message_${Constant.getLocal(application)}")
                        if (status == "true") {
                            val products = json.optJSONArray("products").toString()
                            val gson = Gson()
                            val arrayData: ArrayList<ShoppingListModel> =
                                gson.fromJson(
                                    products,
                                    object : TypeToken<ArrayList<ShoppingListModel>>() {}.type
                                )
                            localArray.addAll(arrayData)
                            mSucessfulSavedList.value = localArray
                        } else {
                            val msg = msg
                            mFailure.value = msg.toString()

                        }
                    }

                } catch (e: java.lang.Exception) {
                    val msg = application.getString(R.string.network_error)
                    mFailure.value = msg
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailure.value = msg
            }

        })

    }


    fun mFailure(): MutableLiveData<String> {

        return mFailure
    }

    fun mSucessfulSavedlist(): MutableLiveData<ArrayList<ShoppingListModel>> {

        return mSucessfulSavedList
    }


}
