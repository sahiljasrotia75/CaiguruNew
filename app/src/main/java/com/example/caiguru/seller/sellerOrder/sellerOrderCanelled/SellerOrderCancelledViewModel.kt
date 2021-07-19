package com.example.caiguru.seller.sellerOrder.sellerOrderCanelled

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class SellerOrderCancelledViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = SellerOrderCancelledRepository(application)


    fun getSellerRequest(page: String) {

        mrepo.getSellerRequest(page)
    }

    // get the result
    fun mSucessfulPendingApproval(): MutableLiveData<ArrayList<SellerApprovalModel>> {

        return mrepo.mSucessfulPendingApproval()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }



}
