package com.example.caiguru.buyer.buyerLists.buyerShopApproveReject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class BuyerShopListApproveRejectViewModel(application: Application): AndroidViewModel(application) {

    var repo : BuyerShopListApproveRejectRepo = BuyerShopListApproveRejectRepo(application)

    fun get_buyer_request_detail(id: String) {

        repo.get_buyer_request_detail(id)
    }
    //getting data
    fun mSucessfulGetbuyerList(): MutableLiveData<BuyerApproveRejectModel> {

        return repo.SucessfulGetbuyerList()
    }

    fun mFailureGetbuyerList(): MutableLiveData<ErrorModel> {

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




    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        repo.notificationRead(notificationId)
    }

}