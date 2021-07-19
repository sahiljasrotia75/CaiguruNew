package com.example.caiguru.buyer.buyerOrder.buyerPendingApproval

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class BuyerPendingViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = BuyerPendingRepository(application)




    // get the result
    fun mSucessfulFinalizeOrder(): MutableLiveData<ArrayList<FinalizeModel>> {

        return mrepo.mSucessfulFinalizeOrder()
    }

    fun mFailure(): MutableLiveData<String> {

        return mrepo.mFailure()
    }

    fun getBuyerRequest(listType: String, page: Int) {

        mrepo.getBuyerRequest(listType,page)
    }


}
