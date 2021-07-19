package com.example.caiguru.seller.sellerOrder.sellerToBeDeliveredOrderList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class ToBeDeliveredOrderListViewModel(application: Application) : AndroidViewModel(application) {
    fun getrequestDetails(sellerDataModel: SellerApprovalModel) {

        mrepo.getrequestDetails(sellerDataModel)
    }

    var mrepo = ToBeDeliveredOrderListRepository(application)

    // get the result
    fun mSucessfulOrderList(): MutableLiveData<ArrayList<OrderListModel>> {

        return mrepo.mSucessfulOrderList()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }


    //**************to be delivered request get********************//
    fun changeRequestStatus(sellerDataModel: SellerApprovalModel) {

        mrepo.changeRequestStatus(sellerDataModel)
    }


    fun mSucessfulRequestApprovals(): MutableLiveData<String> {

        return  mrepo.mSucessfulRequestApprovals()
    }

    fun mFailureRequestApprovals(): MutableLiveData<String> {

        return  mrepo.mFailureRequestApprovals()
    }

    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }

}
