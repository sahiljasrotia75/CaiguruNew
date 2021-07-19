package com.example.caiguru.buyer.buyerProfile.buyerCreditEarned

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerSetting.sellerCreditEarned.EarnedReferedFriendModel


class BuyerCreditEarnedViewModel(application: Application):AndroidViewModel(application) {

    var repo: BuyerCreditEarnedRepository=BuyerCreditEarnedRepository(application)

    fun earnedByreffer() {
        repo.earnedByreffer()
    }

    fun getList(): MutableLiveData<ArrayList<EarnedReferedFriendModel>> {

        return repo.getData()
    }

    fun getListFailure(): MutableLiveData<String> {

        return repo.getListFailure()
    }
    fun SucessfulDataGetProfile(): MutableLiveData<GetProfileModel> {
        return repo.getdata()
    }

    //error observer
    fun errorgetInGetProfile(): MutableLiveData<ErrorModel> {
        return repo.errorget()
    }
    fun getProfile(token: String) {
        repo.getProfileData(token)
    }

}
