package com.example.caiguru.commonScreens.selectCities

import android.app.Application
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.R
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import constant_Webservices.Constant
import constant_Webservices.WebServices
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SelectCitiesRepository(var application: Application) {

    private var selectedCities = java.util.ArrayList<CitiesModel>()
    var mSucessfulCitiesData = MutableLiveData<ArrayList<CitiesModel>>()
    var mFailureCitiesData = MutableLiveData<ErrorModel>()
    var arrayCitiesModel = ArrayList<CitiesModel>()


    fun getCities(page: String) {

        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getcities(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token, ""
            ), page, ""
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    try {
                        if (page == "0") {
                            arrayCitiesModel.clear()
                        }
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        //create the array

                        if (status == "true") {

                            val arryobj = json.optJSONArray("cities")
                            for (i in 0 until arryobj.length()) {
                                val model = CitiesModel()
                                val citiesobject = arryobj.getJSONObject(i)
                                model.id = citiesobject.optString("id")
                                model.name = citiesobject.optString("name")

                                arrayCitiesModel.add(model)
                               /* val json = Constant.getPrefs(application)
                                    .getString(Constant.sel_cities, "")
                                val dd = json.split(",")
                                for (kk in 0 until dd.size) {
                                    if (dd[kk] == model.id) {
                                        selectedCityArray.add(model)
                                    }
                                }*/

                            }
                            mSucessfulCitiesData.value = arrayCitiesModel
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val model = ErrorModel()
                    model.message = response.errorBody().toString()
                    mFailureCitiesData.value = model

                }
            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)

                mFailureCitiesData.value = model
            }
        })


    }


//    private fun getCommaSeperatedCityIds(): String {
//        var cityIds = ""
//        for (city in selectedCities) {
//            cityIds = if (cityIds.isEmpty()) {
//                city.id
//            } else {
//                "$cityIds,${city.id}"
//            }
//        }
//        return cityIds
//    }

    fun mSucessfulCitiesData(): MutableLiveData<ArrayList<CitiesModel>> {

        return mSucessfulCitiesData
    }

    fun mFailureCitiesData(): MutableLiveData<ErrorModel> {

        return mFailureCitiesData
    }
    //********************************************set selected city*************************//

    var mSelectedcity = MutableLiveData<ArrayList<CitiesModel>>()
    //var selectedCityArray = ArrayList<CitiesModel>()

    private fun highlightSelectedCity(mData: CitiesModel) {
        for (city in arrayCitiesModel) {
            if (city.id == mData.id) {
                city.hasselected = true
                break
            }
        }

        mSucessfulCitiesData.value = arrayCitiesModel
    }


    fun setSelectedCity(mData: CitiesModel) {

        if (!checkExist(mData.id)) {
            selectedCities.add(mData)
            mSelectedcity.value = selectedCities
        }

        highlightSelectedCity(mData)

        /*val hashSet = HashSet<CitiesModel>()
        hashSet.addAll(arrayCitiesModel)
        arrayCitiesModel.clear()
        arrayCitiesModel.addAll(hashSet)
        val hashSet1 = HashSet<CitiesModel>()
        hashSet1.addAll(selectedCityArray)
        selectedCityArray.clear()
        selectedCityArray.addAll(hashSet1)
        var model = CitiesModel()
        model.name = mData.name
        model.id = mData.id

        var exist = 0
        for (i in 0 until selectedCityArray.size) {
            if (mData.id == selectedCityArray[i].id) {
                exist = 1
                break
            }
        }
        if (exist == 0) {
            model.hasselected = true
            selectedCityArray.add(model)
            mSelectedcity.value = selectedCityArray
        }

        var json = Constant.getPrefs(application).getString(Constant.sel_cities, "")

        for (j in 0 until arrayCitiesModel.size) {
            if (mData.id == arrayCitiesModel[j].id) {
                arrayCitiesModel[j].hasselected = true
                if (json.isNotEmpty()) {
                    json = json + "," + arrayCitiesModel[j].id
                } else {
                    json = arrayCitiesModel[j].id
                }
            }
        }
        Constant.getPrefs(application).edit().putString(Constant.sel_cities, json).apply()

        mSucessfulCitiesData.value = arrayCitiesModel*/
    }


    private fun checkExist(id: String): Boolean {
        var isExist = false
        for (city in selectedCities) {
            if (city.id == id) {
                isExist = true
                break
            }
        }

        return isExist
    }

    //send back the data
    fun mSelectedCityGet(): MutableLiveData<ArrayList<CitiesModel>> {

        return mSelectedcity
    }

    fun removeCity(mData: CitiesModel) {

      //  val data = ArrayList<CitiesModel>()
        for (city in arrayCitiesModel){
            if (city.id == mData.id){
                city.hasselected = false
            }
         //   data.add(city)
        }

        mSucessfulCitiesData.value = arrayCitiesModel

       selectedCities = removeFromList(mData)
        mSelectedcity.value = selectedCities

       /* val hashSet = HashSet<CitiesModel>()
        hashSet.addAll(arrayCitiesModel)
        arrayCitiesModel.clear()
        arrayCitiesModel.addAll(hashSet)
        for (j in 0 until arrayCitiesModel.size) {
            if (mData.id == arrayCitiesModel[j].id) {
                arrayCitiesModel[j].hasselected = false

            }
        }

        // Constant.getPrefs(application).edit().putString(Constant.sel_cities, dd ).apply()


        var json = Constant.getPrefs(application).getString(Constant.sel_cities, "")
        var removeSelectCat = json.split(",")
        var removeSelectCat1 = ArrayList<String>()
        removeSelectCat1.addAll(removeSelectCat)

        for (i in 0 until selectedCityArray.size) {
            if (mData.id == selectedCityArray[i].id) {
                selectedCityArray.removeAt(i)
                for (n in 0 until removeSelectCat.size) {
                    if (removeSelectCat[n] == mData.id) {
                        removeSelectCat1.removeAt(n)
                        break
                    }
                }

                break
            }
        }
        var jj = ""
        for (p in 0 until removeSelectCat1.size) {
            if (jj.isEmpty()) {
                jj = removeSelectCat1[p]
            } else {
                jj = jj + "," + removeSelectCat1[p]
            }
        }
        Constant.getPrefs(application).edit().putString(Constant.sel_cities, jj).apply()
        mSelectedcity.value = selectedCityArray

        mSucessfulCitiesData.value = arrayCitiesModel*/
    }

    private fun removeFromList(mData: CitiesModel) : ArrayList<CitiesModel> {
        val list = ArrayList<CitiesModel>()
        for (city in selectedCities){
            if (mData.id == city.id){
                continue
            }

            list.add(city)
        }

       return list
    }

    //***********************search city Api*************************//
    fun searchCity(searchText: String, page: String) {

        val retrofit = Constant.getRestClient()

        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.getcities(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token, ""
            ), page, searchText
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

               if (response.isSuccessful) {
                    try {
                        val resp = response.body()!!.string()
                        val json = JSONObject(resp)
                        val status = json.optString("status")
                        //create the array
                        arrayCitiesModel.clear()
                        if (status == "true") {
                          //  var arrayCitiesModel1 = ArrayList<CitiesModel>()
                            val arryobj = json.optJSONArray("cities")
                            for (i in 0 until arryobj.length()) {
                                val model = CitiesModel()
                                val citiesobject = arryobj.getJSONObject(i)
                                model.id = citiesobject.optString("id")
                                model.name = citiesobject.optString("name")

                                //  arrayCitiesModel1.add(model)
                                arrayCitiesModel.add(model)
                            }
                            mSucessfulCitiesData.value = arrayCitiesModel
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    val model = ErrorModel()
                    model.message = response.errorBody().toString()
                    mFailureCitiesData.value = model

                }


            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)

                mFailureCitiesData.value = model
            }
        })


    }

    var mSucessfulsendCity = MutableLiveData<ArrayList<CitiesModel>>()
    var mFailureSendCity = MutableLiveData<ErrorModel>()

    //*******************************web services of selected city on button click*******************//


    fun setWebServicesCity(gettingSlectedCity: ArrayList<CitiesModel>) {
        val citiesArray = ArrayList<CitiesModel>()
        var cities = ""
        for (item in gettingSlectedCity) {

            if (cities.isEmpty()) {

                cities = item.id
            } else {
                cities = cities + "," + item.id
            }


        }
        val retrofit = Constant.getRestClient()
        val services = retrofit.create(WebServices::class.java)
        val call: Call<ResponseBody> = services.update_city(
            "Bearer " + Constant.getPrefs(application).getString(
                Constant.token, ""
            ), cities
        )
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    val model = CitiesModel()
                    val resp = response.body()!!.string()
                    val json = JSONObject(resp)
                   // val status = json.optString("status")
                    model.id = json.optString("sel_cities")
                    model.msg = json.optString("message_${Constant.getLocal(application)}")
                    citiesArray.add(model)
                    Constant.getPrefs(application).edit()
                        .putString(Constant.sel_cities, model.id)
                        .apply()
                    // mSucessfulCitiesData.value = citiesArray
                    mSucessfulsendCity.value = citiesArray
                } else {
                    val model = ErrorModel()
                    model.message = application.getString(R.string.network_error)
                    mFailureCitiesData.value = model
                }


            }


            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val model = ErrorModel()
                model.message = application.getString(R.string.network_error)

                mFailureCitiesData.value = model
            }
        })


    }

    fun mSucessfulsendCity(): MutableLiveData<ArrayList<CitiesModel>> {

        return mSucessfulsendCity
    }

    fun mFailureSendCity(): MutableLiveData<ErrorModel> {

        return mFailureSendCity
    }

    fun getSelectedCities(): java.util.ArrayList<CitiesModel> {
        return selectedCities
    }


    private fun getSelectedCitiesList(): java.util.ArrayList<CitiesModel> {
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

    fun listSavedCities(intent: Intent) {
        if (intent.hasExtra("cities")) {
            selectedCities = intent.getParcelableArrayListExtra<CitiesModel>("cities")!!
            mSelectedcity.value = selectedCities
        }else{
            selectedCities = getSelectedCitiesList()
            mSelectedcity.value = selectedCities
        }
    }
}
