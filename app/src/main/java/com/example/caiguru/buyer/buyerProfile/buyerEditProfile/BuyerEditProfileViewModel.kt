package com.example.caiguru.buyer.buyerProfile.buyerEditProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import java.io.File

class BuyerEditProfileViewModel(application: Application) : AndroidViewModel(application) {
    fun profileImage(uri: File) {
        repo.setProfileImage(uri)
    }
    //status true observer
    fun getdatasucessful(): MutableLiveData<DialogModel> {
        return repo.getdatasucessful()
    }
    //error observer
    fun errorget(): MutableLiveData<String> {

        return repo.errorget()
    }
    fun mSucessfulupdateData(): MutableLiveData<GetProfileModel> {

        return repo.mSucessfulupdateData()
    }

    fun mFailureError(): MutableLiveData<ErrorModel> {

        return repo.mFailureError()
    }

    fun updateprofile(
        edtname: String,
        updatedimage: String,
        editemail: String,
        categories: String,
        address: String,
        lat: String,
        long: String
    ) {

        return repo.updatedProfile(edtname, updatedimage, editemail, categories, address, lat, long)

    }

    fun getProfile(tokens: String) {

        repo.getProfileData(tokens)
    }
    //get data
    //status observer
    fun mSucessfulGetProfile(): MutableLiveData<GetProfileModel> {
        return   repo.mSucessfulGetProfile()
    }

    //error observer
    fun errorGetProfile(): MutableLiveData<ErrorModel> {
        return repo.errorGetProfile()
    }

    var repo: BuyerEditProfileRepo = BuyerEditProfileRepo(application)

}
