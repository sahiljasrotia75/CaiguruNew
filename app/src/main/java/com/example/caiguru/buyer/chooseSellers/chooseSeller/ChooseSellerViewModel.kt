package com.example.caiguru.buyer.chooseSellers.chooseSeller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerParentModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel

class ChooseSellerViewModel(application: Application) : AndroidViewModel(application) {
    fun setChooseSeller(lat: String, long: String, checkedHome: String, checkedSelf: String) {

        mrepo.setChooseSeller(lat, long, checkedHome, checkedSelf)
    }

    var mrepo = ChooseSellerRepository(application)

    fun mSucessfulChooseSellerData(): LiveData<ArrayList<CustomerParentModel>> {

        return mrepo.mSucessfulChooseSellerData()
    }

    fun mFailureChosseSeller(): MutableLiveData<String> {

        return mrepo.mFailureChosseSeller()
    }


    //*********************search product api******************//
    fun searchProduct(
        search: String,
        lat: String,
        long: String,
        checkedHome: String,
        checkedSelf: String
    ) {


        mrepo.searchProduct(search, lat, long, checkedHome, checkedSelf)
    }

    //search api get data
    fun mSearchSucessfulData(): MutableLiveData<ArrayList<CustomerChildModel>> {

        return mrepo.mSearchSucessfulDatas()
    }

    fun mSearchFailure(): MutableLiveData<String> {

        return mrepo.mSearchFailure()
    }

    fun updatedAddress(
        addressModel: DeliveryZoneModel

    ) {
        return mrepo.updatedAddress(addressModel)

    }

    fun mUpdateAddress(): MutableLiveData<DeliveryZoneModel> {

        return mrepo. mUpdateAddress()
    }


    fun mFailure(): MutableLiveData<ErrorModel> {

        return mrepo.mFailure()
    }

    //**************************************Load More *************************************//

    fun loadmoreSeller(
        catId: String,
        lat: String,
        long: String,
        checkedHome: String,
        checkedSelf: String
    ) {
        mrepo.loadmoreSeller(catId, lat, long, checkedHome, checkedSelf)

    }


    fun mSucessfulload_moreData(): MutableLiveData<ArrayList<CustomerChildModel>> {

        return mrepo.mSucessfulload_more()
    }

    fun mFailureload_moreData(): MutableLiveData<String> {

        return mrepo.mFailureload_more()
    }

    fun logoutBannedUser(): MutableLiveData<ErrorModel> {
        return mrepo.logoutBannedUser()
    }
}
