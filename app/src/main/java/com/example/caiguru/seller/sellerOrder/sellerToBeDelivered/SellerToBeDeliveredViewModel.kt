package com.example.caiguru.seller.sellerOrder.sellerToBeDelivered

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class SellerToBeDeliveredViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = SellerToBeDeliveredRepository(application)


    fun getSellerRequest( Page: String) {

        mrepo.getSellerRequest(Page)
    }

    // get the result
    fun mSucessfulPendingApproval(): MutableLiveData<ArrayList<SellerApprovalModel>> {

        return mrepo.mSucessfulPendingApproval()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }



}
