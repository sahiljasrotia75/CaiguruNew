package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.selectCities.CitiesModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerListRepository(var application: Application) {
    var mSucessfuldata = MutableLiveData<ArrayList<CustomerParentModel>>()
    var mFailureData = MutableLiveData<String>()
    private var selectedCities = ArrayList<CitiesModel>()

    init {
        selectedCities = getSelectedCities()
    }

    private fun getSelectedCities(): java.util.ArrayList<CitiesModel> {
        val listCities = ArrayList<CitiesModel>()
        val profile = Constant.getPrefs(application).getString(Constant.PROFILE, "")
        if (profile!!.isNotEmpty()){
            val obj = JSONObject(profile)
            val citiesArr = obj.optJSONArray("sel_cities")

            for (i in 0 until  citiesArr.length()){
                val child = citiesArr.optJSONObject(i)
                val model = CitiesModel()
                model.id = child.optString("id")
                model.name = child.optString("name")
                listCities.add(model)
            }

        }
        return listCities
    }

    private fun getCommaSeperatedCityIds(): String{
        var cityIds = ""
        for (city in selectedCities){
            cityIds = if (cityIds.isEmpty()){
                city.id
            }else{
                "$cityIds,${city.id}"
            }
        }
        return cityIds
    }


    fun listuploadedbycustomer() {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.list_uploaded_by_customer(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token,
                ""
            ), getCommaSeperatedCityIds()
        )
        call.enqueue(object : Callback<ResponseBody> {


            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    val res = response.body()!!.string()
                    val json = JSONObject(res)
                    val status = json.optString("status")
                    if (status == "true") {
                        val resul = json.optJSONArray("results").toString()
                        val gson = Gson()
                        val model1: ArrayList<CustomerParentModel> =
                            gson.fromJson(
                                resul,
                                object : TypeToken<ArrayList<CustomerParentModel>>() {}.type
                            )
                        mSucessfuldata.value = model1
                    } else {
                        val msg = R.string.network_error
                        mFailureData.value = msg.toString()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val msg = application.getString(R.string.network_error)
                mFailureData.value = msg
            }

        })


    }

    fun getData(): MutableLiveData<ArrayList<CustomerParentModel>> {

        return mSucessfuldata
    }

    fun getListFailure(): MutableLiveData<String> {

        return mFailureData
    }

    fun getCities(): ArrayList<CitiesModel> {
        return selectedCities
    }

    fun setCities(cities: ArrayList<CitiesModel>) {
        selectedCities = cities
    }
}
