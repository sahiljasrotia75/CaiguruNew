package com.example.caiguru.seller.sellerSetting.sellerCreditEarned

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel

class CreditEarnedViewModel(application: Application):AndroidViewModel(application) {
    fun earnedByreffer() {
        repo.earnedByreffer()
    }

    var repo :CreditEarnedRepository=CreditEarnedRepository(application)

    fun getList(): MutableLiveData<ArrayList<EarnedReferedFriendModel>> {

        return repo.getData()
    }

    fun getListFailure(): MutableLiveData<String> {

        return repo.getListFailure()
    }

    fun earned_by_listing(page: String) {

        repo.earned_by_listing(page)
    }

    fun getList1(): MutableLiveData<ArrayList<EarnedReferedFriendModel>> {

        return repo.getData1()
    }

    fun getFailure(): MutableLiveData<String> {

        return repo.getFailure()
    }

    //get the data
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
