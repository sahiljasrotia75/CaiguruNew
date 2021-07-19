package com.example.caiguru.buyer.buyerOrder.buyerOrderDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerLists.buyerShopApproveReject.BuyerApproveRejectModel
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.sellerOrder.sellerApprovalOrderList.OrderListModel
import kotlinx.android.synthetic.main.activity_buyer_finalize_order_details.*

class FinalizeDetailViewModel(application: Application): AndroidViewModel(application) {

    var repo : FinalizeDetailsRepository = FinalizeDetailsRepository(application)

    fun get_buyer_request_detail(id: String, listType: String) {

        repo.get_buyer_request_detail(id,listType)
    }
    //getting data
    fun mSucessfulGetbuyerList(): MutableLiveData<OrderListModel> {

        return repo.SucessfulGetbuyerList()
    }

    fun mFailureGetbuyerList(): MutableLiveData<String> {

        return repo.FailureGetbuyerList()
    }

    fun change_buyer_request_status(id: String) {
        repo.change_buyer_request_status(id)
    }

    //get back the  data
    fun mSucessfulRequestApprovals(): MutableLiveData<String> {

        return repo.mSucessfulRequestApprovals()
    }

    fun mFailureRequestApprovals(): MutableLiveData<String> {

        return repo.mFailureRequestApprovals()
    }

    fun change_buyer_request_reject_status(id: String) {
        repo.change_buyer_request_reject_status(id)    }


    //get back data
    fun mSucessfulRejectedRequest(): MutableLiveData<String> {

        return repo.mSucessfulRejectedRequest()
    }

    fun mFailureRejectedRequest(): MutableLiveData<String> {

        return repo.mFailureRejectedRequest()
    }

    fun CancelList(id: String, listType: String) {

        repo.btnCancel(id,listType)
    }

    fun mSucessfulCancelList(): MutableLiveData<String> {

        return repo.mSucessfulCancelList()
    }

    fun mFailureCancelLIst(): MutableLiveData<String> {

        return repo.mFailureCancelLIst()
    }

    //*********************notificationRead******************//
//    fun notificationRead(notificationId: String) {
//
//        repo.notificationRead(notificationId)
//    }

}