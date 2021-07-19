package com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class ApprovalOrderListViewModel(application: Application) : AndroidViewModel(application) {
    fun getrequestDetails(req_id: String, listType: String) {

        mrepo.getrequestDetails(req_id,listType)
    }

    var mrepo = ApprovalOrderListRepository(application)

    // get the result
    fun mSucessfulOrderList(): MutableLiveData<ArrayList<OrderListModel>> {

        return mrepo.mSucessfulOrderList()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }


    //*****************request approval api
    fun getRequestApprovals(reqId: String, listType: String) {

        mrepo.getRequestApprovals(reqId,listType)
    }

    //get back the  data
    fun mSucessfulRequestApprovals(): MutableLiveData<String> {

        return mrepo.mSucessfulRequestApprovals()
    }

    fun mFailureRequestApprovals(): MutableLiveData<String> {

        return mrepo.mFailureRequestApprovals()
    }


    //*********************api of rejected request***************"//
    fun getRejectedRequest(sellerDataModel: SellerApprovalModel) {

        mrepo.getRejectedRequest(sellerDataModel)
    }
//    //get back data
//    fun mSucessfulRejectedRequest(): MutableLiveData<String> {
//
//        return mrepo.mSucessfulRejectedRequest()
//    }
//
//    fun mFailureRejectedRequest(): MutableLiveData<String> {
//
//        return mrepo.mFailureRejectedRequest()
//    }

    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }
}
