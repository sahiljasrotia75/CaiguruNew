package com.example.caiguru.buyer.buyerLists.buyerQuotedShoppingList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class BuyerOpenshopListModifyViewModel(application: Application): AndroidViewModel(application) {
    fun list_all_request(id: String, page: String) {

        repo.list_all_request(id,page)
    }

    fun mSucessfulshopListData(): MutableLiveData<ArrayList<BuyerShopOpenModel>> {

        return repo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return repo.mFailureShopList()
    }

    var repo : BuyerOpenShopListRepo = BuyerOpenShopListRepo(application)

}
