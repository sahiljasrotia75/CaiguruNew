package com.example.caiguru.buyer.buyerOrder.buyerOrderComplete

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class BuyerCompleteViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = BuyerCompleteRepository(application)


    fun getDeliveredOrder(page: Int) {

        mrepo.getDeliveredOrder(page.toString())

    }

    // get the result
    fun mSucessfulFinalizeOrder(): MutableLiveData<ArrayList<FinalizeModel>> {

        return mrepo.mSucessfulFinalizeOrder()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }


}
