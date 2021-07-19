package com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForAllList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerShoppingList.ChooseSellerShoppingModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel

class PayCreditsMultipleListViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = PayCreditsMultipleListRepository(application)

    fun setSingleListCredits(
        jsonconvertModel: String,
        arrayModel: ArrayList<ChooseSellerShoppingModel>,
        homeDeliveryaddress: DeliveryZoneModel,
        partialComissions: Double
    ) {

        mrepo.setSingleListCredits(jsonconvertModel, arrayModel,homeDeliveryaddress,partialComissions)
    }
    // send back tha data
    fun mSucessfulSingleListData(): MutableLiveData<PayCreditsResultModel> {

        return mrepo.mSucessfulSingleListData()
    }

    fun mFailureSingleListData(): MutableLiveData<String> {

        return mrepo.mFailureSingleListData()
    }


}
