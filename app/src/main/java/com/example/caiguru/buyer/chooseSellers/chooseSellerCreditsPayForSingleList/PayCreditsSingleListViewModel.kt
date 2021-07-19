package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel

class PayCreditsSingleListViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = PayCreditsSingleListRepository(application)

    //set the credits
    fun setSingleListCredits(
        jsonStringData: String,
        arrayModel: ChooseSellerShoppingModel,
        totalCredits: String,
        homeDeliveryaddres: DeliveryZoneModel
    ) {

        mrepo.setSingleListCredits(jsonStringData, arrayModel, totalCredits, homeDeliveryaddres)
    }


    fun mSucessfulSingleListData(): MutableLiveData<PayCreditsResultModel> {

        return mrepo.mSucessfulSingleListData()
    }

    fun mFailureSingleListData(): MutableLiveData<String> {

        return mrepo.mFailureSingleListData()
    }

}

