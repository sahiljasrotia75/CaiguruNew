package com.example.caiguru.commonScreens.selectCities

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class SelectCitiesViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = SelectCitiesRepository(application)

    fun getCities(page: String) {
        mrepo.getCities(page)
    }


    fun mSucessfulCitiesData(): MutableLiveData<ArrayList<CitiesModel>> {

        return mrepo.mSucessfulCitiesData()
    }

    fun mFailureCitiesData(): MutableLiveData<ErrorModel> {

        return mrepo.mFailureCitiesData()
    }
    // set the 2nd adapter data

    fun setSelectedCity(mData: CitiesModel) {

        mrepo.setSelectedCity(mData)

    }

    //getting the data
    fun mSelectedCityGet(): MutableLiveData<ArrayList<CitiesModel>> {

        return mrepo.mSelectedCityGet()
    }

    //remove city
    fun removeCity(mData: CitiesModel) {
        mrepo.removeCity(mData)

    }

    //********************search city********************//

    fun searchCity(searchText: String, page: String) {
        mrepo.searchCity(searchText, page)

    }


    // set the updated city  for api
    fun setWebServicesCity(gettingSlectedCity: java.util.ArrayList<CitiesModel>) {

        mrepo.setWebServicesCity(gettingSlectedCity)
    }

    fun mSucessfulsendCity(): MutableLiveData<ArrayList<CitiesModel>> {

        return mrepo.mSucessfulsendCity()
    }

    fun mFailureSendCity(): MutableLiveData<ErrorModel> {

        return mrepo.mFailureSendCity()
    }

    fun getSelectedCities(): ArrayList<CitiesModel> {
        return mrepo.getSelectedCities()
    }

    fun listSavedCities(intent: Intent) {
        mrepo.listSavedCities(intent)
    }


}
