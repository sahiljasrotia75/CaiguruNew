package com.example.caiguru.buyer.chooseSellers.chooseSellerSuggestProduct

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class SellerSuggestedProductViewModel(application: Application):AndroidViewModel(application) {


    var repo : SellerSuggestedProductRepository=SellerSuggestedProductRepository(application)

    fun suggest_products(s: String, suggest_products: String) {

        return repo.suggest_products(s,suggest_products)


    }

    fun getsellerStatus(): MutableLiveData<ErrorModel> {
        return repo.getsellerStatus()
    }

    fun errorgetStatus(): MutableLiveData<ErrorModel> {
        return repo.errorgetStatus()
    }

}
