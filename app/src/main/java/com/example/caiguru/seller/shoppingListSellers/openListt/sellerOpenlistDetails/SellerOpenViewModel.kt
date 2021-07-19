package com.example.caiguru.seller.shoppingListSellers.openListt.sellerOpenlistDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails.ListModel

class SellerOpenViewModel(application: Application):AndroidViewModel(application) {
    fun getSellerShoppingList(page: String) {
        return repo.getSeller(page)
    }

    //get response
    fun mSucessful(): MutableLiveData<ArrayList<ListModel>> {

        return repo.mSucessful()
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return repo.mFailure()
    }

    var repo:SellerOpenRepository=SellerOpenRepository(application)

}
