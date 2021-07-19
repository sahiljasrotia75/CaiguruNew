package com.example.caiguru.seller.sellerOrder.sellerPendingApprovals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SellerPendingApprovalsViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = SellerPendingApprovalRepository(application)


    fun getSellerRequest(listType: String, page: Int) {

        mrepo.getSellerRequest(listType,page.toString())
    }

    // get the result
    fun mSucessfulPendingApproval(): MutableLiveData<ArrayList<SellerApprovalModel>> {

        return mrepo.mSucessfulPendingApproval()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }




}
