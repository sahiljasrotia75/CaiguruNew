package com.example.caiguru.buyer.buyerProfile.buyerOrderDetalis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel

class OrderDetailsViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = OrderDetailsRepository(application)

    fun getOrderDetails(reqId: String, listType: String) {

        mrepo.getOrderDetails(reqId,listType)
    }

    fun mSucessfulshopListData(): MutableLiveData<OrderModel> {

        return mrepo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mrepo.mFailureShopList()
    }

    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }



}
