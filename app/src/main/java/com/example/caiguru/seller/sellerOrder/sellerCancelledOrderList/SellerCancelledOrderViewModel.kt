package com.example.caiguru.seller.sellerOrder.sellerCancelledOrderList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel

class SellerCancelledOrderViewModel(application: Application) : AndroidViewModel(application) {


    fun getrequestDetails(id: String, listType: String) {

        mrepo.getrequestDetails(id,listType)
    }

    var mrepo = SellerCancelledOrderRepository(application)

    // get the result
    fun mSucessfulOrderList(): MutableLiveData<ArrayList<OrderListModel>> {

        return mrepo.mSucessfulOrderList()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }


    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }
}
