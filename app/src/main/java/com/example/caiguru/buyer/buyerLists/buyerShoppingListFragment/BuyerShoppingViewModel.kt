package com.example.caiguru.buyer.buyerLists.buyerShoppingListFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class BuyerShoppingViewModel(application: Application) : AndroidViewModel(application) {

    var repo: BuyerShoppingRepo = BuyerShoppingRepo(application)

    fun your_shopping_lists(page: String) {
        repo.your_shopping_lists(page)

    }

    fun mSucessfulshopListData(): MutableLiveData<ArrayList<BuyerShopModel>> {

        return repo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return repo.mFailureShopList()
    }



    fun logoutBannedUser(): MutableLiveData<ErrorModel> {
        return repo.logoutBannedUser()
    }
}
