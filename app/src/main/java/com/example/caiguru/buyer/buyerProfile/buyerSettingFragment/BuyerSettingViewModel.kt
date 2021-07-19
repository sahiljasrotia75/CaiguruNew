package com.example.caiguru.buyer.buyerProfile.buyerSettingFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class BuyerSettingViewModel(application: Application):AndroidViewModel(application) {
    fun logout(token: String) {

        repo.logout(token)
    }

    var repo:BuyerSettingRepo=BuyerSettingRepo(application)

//    fun getLogout(): MutableLiveData<ErrorModel> {
//        return repo.getsellerStatus()
//    }



}
