package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.selectCities.CitiesModel

class CustomerListViewModel(application: Application) : AndroidViewModel(application) {


    var repo: CustomerListRepository = CustomerListRepository(application)

    fun listuploadedbycustomer() {
        repo.listuploadedbycustomer()


    }

    fun getList(): MutableLiveData<ArrayList<CustomerParentModel>> {

        return repo.getData()
    }

    fun getListFailure(): MutableLiveData<String> {

        return repo.getListFailure()
    }

    fun getSelectedCities(): ArrayList<CitiesModel> {
        return repo.getCities()
    }

    fun setCities(cities: ArrayList<CitiesModel>) {
        repo.setCities(cities)
    }


}
