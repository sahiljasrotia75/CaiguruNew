package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile.finalizePurchaseOrder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class FinalizePurchaseViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = FinalizePurchaseRepository(application)

    fun buyList(
        sellerId: String,
        productJson: String,
        partialComissionCredits: String,
        address: String,
        paymentMethods: String
    ) {

        mrepo.buyList(sellerId,productJson,partialComissionCredits,address,paymentMethods)
    }


    fun mSucessfulSingleListData(): MutableLiveData<String> {

        return mrepo.mSucessfulSingleListData()
    }

    fun mFailureSingleListData(): MutableLiveData<String> {

        return mrepo.mFailureSingleListData()
    }


}

