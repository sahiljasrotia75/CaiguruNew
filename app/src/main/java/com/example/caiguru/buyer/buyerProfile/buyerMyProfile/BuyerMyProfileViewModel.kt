package com.example.caiguru.buyer.buyerProfile.buyerMyProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel

class BuyerMyProfileViewModel(application: Application):AndroidViewModel(application) {
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


    var repo :BuyerMyProfileRepo=BuyerMyProfileRepo(application)

}
