package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.loadmore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel

class LoadMoreViewModel(application: Application) : AndroidViewModel(application) {
    var repo: LoadMoreRepository = LoadMoreRepository(application)

    fun loadmoreBuyerlist(mData: String) {
        repo.loadmoreBuyerlist(mData)
    }

    fun getList(): MutableLiveData<ArrayList<CustomerChildModel>> {

        return repo.getData()
    }

    fun getListFailure(): MutableLiveData<String> {

        return repo.getListFailure()
    }


}
