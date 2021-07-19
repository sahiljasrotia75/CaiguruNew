package com.example.caiguru.seller.homeSeller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class HomeSellerViewModel(application: Application) : AndroidViewModel(application) {
    var repo: HomeSellerRepository = HomeSellerRepository(application)

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

    //home webservices
    fun getHomeSellerData(page: String) {
        repo.getHomeSellerData(page)

    }

    fun mSuccessfulHomeData(): MutableLiveData<ArrayList<HomeSellerModel>> {
        return repo.mSuccessfulHomeData()
    }

    fun mErrorHomeData(): MutableLiveData<String> {
        return repo.mErrorHomeData()
    }
    //ban user
    fun logoutBannedUser(): MutableLiveData<ErrorModel> {
        return repo.logoutBannedUser()
    }

}
