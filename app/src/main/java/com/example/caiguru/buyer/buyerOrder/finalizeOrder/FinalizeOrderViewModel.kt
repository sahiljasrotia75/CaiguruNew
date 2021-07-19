package com.example.caiguru.buyer.buyerOrder.finalizeOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class FinalizeOrderViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = FinalizeOrderRepository(application)


    fun getFinalizeOrder(page: Int) {

        mrepo.getFinalizeOrder(page.toString())

    }

    // get the result
    fun mSucessfulFinalizeOrder(): MutableLiveData<ArrayList<FinalizeModel>> {
        return mrepo.mSucessfulFinalizeOrder()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }


}
