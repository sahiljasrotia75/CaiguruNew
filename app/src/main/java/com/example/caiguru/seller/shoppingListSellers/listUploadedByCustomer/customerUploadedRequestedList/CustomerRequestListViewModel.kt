package com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.customerUploadedRequestedList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel
import java.io.File

class CustomerRequestListViewModel(application: Application) : AndroidViewModel(application) {
    fun setBuyerlist(list_id: String) {

        mrepo.getBuyerList(list_id)
    }


    //getting data
    fun mSucessfulGetbuyerList(): MutableLiveData<CustomerChildModel> {

        return mrepo.SucessfulGetbuyerList()
    }

    fun mFailureGetbuyerList(): MutableLiveData<ErrorModel> {

        return mrepo.FailureGetbuyerList()
    }

    var mrepo = CustomerRequestListRepository(application)
//    fun mSucessfulCategoryData(): MutableLiveData<ArrayList<CustomerRequestedListParentModel>> {
//
//
//        return mrepo.mSucessfulCategoryData()
//    }

    //***********************request buyer list api****************//
    fun setRequestBuyerList(
        parentModel: CustomerChildModel,
        childModel: String
    ) {

        mrepo.setRequestBuyerList(parentModel, childModel)
    }

    //request buyer list
    fun mSucessfulRequestBuyerList(): MutableLiveData<String> {

        return mrepo.mSucessfulRequestBuyerList()
    }

    fun mFailureRequestBuyerList(): MutableLiveData<String> {

        return mrepo.mFailureRequestBuyerList()
    }

    //******************upload pprofile image***********************//

    fun uploadProfileImage(uri: File?) {
        mrepo.uploadProfileImage(uri)

    }

    //status true observer
    fun mSucessfullUploadImage(): MutableLiveData<String> {
        return mrepo.mSucessfullUploadImage()
    }


    //error observer
    fun mFailureeUploadImage(): MutableLiveData<String> {

        return mrepo.mFailureeUploadImage()
    }
    //*****************reportLIst
    fun reportList(reason: String, data: CustomerChildModel) {

        mrepo.reportList(reason,data)
    }

    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mrepo.mFailureReposrtlist()
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mrepo.mSucessfulREportList()
    }
}
