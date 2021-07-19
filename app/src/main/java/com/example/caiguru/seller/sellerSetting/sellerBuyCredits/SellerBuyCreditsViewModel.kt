package com.example.caiguru.seller.sellerSetting.sellerBuyCredits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class SellerBuyCreditsViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = SellerBuyCreditsReposiory(application)
    fun getCredits() {

        mrepo.getCredits()
    }

    fun setData(): MutableLiveData<ArrayList<BuyCreditsModel>> {
        return mrepo.setData()


    }

    //***********req the data
    fun getBuyCreditsPack(
        globalCreditsModel: BuyCreditsModel,
        allTransectionData: String
    ) {
        mrepo.getBuyCreditsPack(globalCreditsModel,allTransectionData)
    }
//***********get the data
    //sucessful buyer pack

    fun mSucessfullBuyCreditsPacks(): MutableLiveData<BuyCreditsModel> {
        return mrepo.mSucessfullBuyCreditsPacks()
    }

    //failure
    fun mFailure(): MutableLiveData<String> {
        return mrepo.mFailure()
    }

    fun mSuccessMercado(): MutableLiveData<String> {
        return mrepo.mSuccessMercado()
    }

    fun mercadopago(creditCost: Double) {
        mrepo.mercadopago(creditCost)

    }


}
