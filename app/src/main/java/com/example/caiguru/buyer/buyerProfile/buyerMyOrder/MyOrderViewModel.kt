package com.example.caiguru.buyer.buyerProfile.buyerMyOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MyOrderViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = MyOrderRepository(application)

    fun getMyOrder(page: String) {
        mrepo.getMyOrder(page)

    }

    fun mSucessfulshopListData(): MutableLiveData<ArrayList<OrderModel>> {

        return mrepo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mrepo.mFailureShopList()
    }


}
