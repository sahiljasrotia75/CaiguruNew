package com.example.caiguru.seller.sellerSetting.sellerProfile.settingfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class SellerSettingViewModel(application: Application) : AndroidViewModel(application) {

    var settingRepository: SellerSettingRepository = SellerSettingRepository(application)
    fun logout(token: String) {
        settingRepository.logout(token)
    }


//    fun getLogout(): MutableLiveData<ErrorModel> {
//        return settingRepository.getsellerStatus()
//    }


}
