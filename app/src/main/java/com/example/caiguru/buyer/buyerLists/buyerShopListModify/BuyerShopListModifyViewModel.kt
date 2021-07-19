package com.example.caiguru.buyer.buyerLists.buyerShopListModify

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.listUploadedByCustomer.listUploadedByCustomers.CustomerChildModel

class BuyerShopListModifyViewModel(application: Application) : AndroidViewModel(application) {
    var repo: BuyerShopListModifyRepo = BuyerShopListModifyRepo(application)

    fun setBuyerShoplist(model: String) {
        repo.setBuyerShoplist(model)

    }

    //getting data
    fun mSucessfulGetbuyerList(): MutableLiveData<CustomerChildModel> {

        return repo.SucessfulGetbuyerList()
    }

    fun mFailureGetbuyerList(): MutableLiveData<ErrorModel> {

        return repo.FailureGetbuyerList()
    }


    fun deleteShoppingList(listId: String) {
        repo.deleteShoppingList(listId)

    }
    fun mSucessfulDeleteList(): MutableLiveData<String> {

        return  repo.mSucessfulDeleteList()
    }

    fun mFailureDeleteList(): MutableLiveData<String> {

        return repo.mFailureDeleteList()
    }
}