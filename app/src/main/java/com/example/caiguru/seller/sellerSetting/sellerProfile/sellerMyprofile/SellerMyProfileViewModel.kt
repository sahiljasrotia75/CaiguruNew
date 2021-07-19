package com.example.caiguru.seller.sellerSetting.sellerProfile.sellerMyprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel

class SellerMyProfileViewModel(application: Application) : AndroidViewModel(application) {
    var repo = SellerMyProfileRepository(application)

    fun getProfile(token: String) {
        repo.getProfileData(token)
    }

    //get the data
    fun SucessfulData(): MutableLiveData<GetProfileModel> {
        return repo.getdata()
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {
        return repo.errorget()
    }


}
