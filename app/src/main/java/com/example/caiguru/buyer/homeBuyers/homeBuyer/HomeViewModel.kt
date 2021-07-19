package com.example.caiguru.buyer.homeBuyers.homeBuyer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var mRepo: HomeRepository = HomeRepository(application)


    fun getProfile(token: String) {

        mRepo.getProfile(token)
    }

    //status observer
    fun getdata(): MutableLiveData<GetProfileModel> {
        return mRepo.getdata()
    }

    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {
        return mRepo.errorget()
    }

    //*********************************buyer home page webservices*************//
    fun setBuyerHome(lat: String, long: String, page: Int) {
        mRepo.setBuyerHome(lat, long, page.toString())
    }

    //get the data
    fun mSuccessfulBuyerHome(): MutableLiveData<ArrayList<HomeModel>> {
        return mRepo.mSuccessfulBuyerHome()
    }

    fun mErrorbuyerHome(): MutableLiveData<String> {
        return mRepo.mErrorbuyerHome()
    }
    //**********************like api
    fun setLikeDisLike(like: String, postId: String, isLike: String) {
        mRepo.setLikeDisLike(like, postId, isLike)
    }


    //*****************reportLIst
    fun reportList(reason: String, data: HomeModel) {

        mRepo.reportList(reason,data)
    }

    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mRepo.mFailureReposrtlist()
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mRepo.mSucessfulREportList()
    }
}
