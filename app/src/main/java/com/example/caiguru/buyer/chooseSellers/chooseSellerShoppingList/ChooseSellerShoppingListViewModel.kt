package com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ChooseSellerShoppingListViewModel(application: Application) : AndroidViewModel(application) {


    var mrepo = ChooseSellerShoppingListRepository(application)


    //*********************seller list listing******************//

    fun setShoppingList(
        data: String,
        lat: Double,
        long: Double,
        searchtext: String,
        catId: String,
        selfPickup: Int,
        homeDelivery: Int
    ) {
        mrepo.setShoppingList(data, lat, long,searchtext,catId,selfPickup,homeDelivery)

    }


    fun mSucessfulSellerShoppingList(): MutableLiveData<ArrayList<ChooseSellerShoppingModel>> {

        return mrepo.mSucessfulSellerShoppingList()
    }

    fun mFailureChosseSeller(): MutableLiveData<String> {

        return mrepo.mFailureChosseSeller()
    }
//*****************reportLIst
    fun reportList(reason: String, data: ChooseSellerShoppingModel) {

        mrepo.reportList(reason,data)
    }

    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mrepo.mFailureReposrtlist()
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mrepo.mSucessfulREportList()
    }

}

