package com.example.caiguru.seller.shoppingListSellers.closeList.closelistDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class ClosedViewModel(application: Application):AndroidViewModel(application){

    var repo : ClosedRepository=ClosedRepository(application)

    fun getSellerCloseList(page: String) {

        repo.getSellerCloseList(page)
    }

    //get response
    fun mSucessful(): MutableLiveData<ArrayList<ListModel>> {

        return repo.mSucessful()
    }

    fun mFailure(): MutableLiveData<ErrorModel> {

        return repo.mFailure()
    }
}
