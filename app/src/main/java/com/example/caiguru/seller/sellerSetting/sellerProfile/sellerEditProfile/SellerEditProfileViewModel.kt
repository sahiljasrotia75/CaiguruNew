package com.example.caiguru.seller.sellerSetting.sellerProfile.sellerEditProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.DialogModel
import java.io.File

class SellerEditProfileViewModel(application: Application) : AndroidViewModel(application) {
    fun profileImage(uri: File) {
        repo.setProfileImage(uri)
    }

    var repo: SellerEditProfileRepo = SellerEditProfileRepo(application)

    //status true observer
    fun getdatasucessful(): MutableLiveData<DialogModel> {
        return repo.getdatasucessful()
    }

    //error observer
    fun errorget(): MutableLiveData<String> {

        return repo.errorget()
    }

    fun updateprofile(
        edtname: String,
        updatedimage: String,
        editemail: String,
        categories: String,
        addressModel: String,
        lat: String,
        long: String
    ) {
        return repo.updatedProfile(
            edtname,
            updatedimage,
            editemail,
            categories,
            addressModel,
            lat,
            long
        )
    }


    fun mSucessfulupdateData(): MutableLiveData<GetProfileModel> {

        return repo.mSucessfulupdateData()
    }

    fun mFailureError(): MutableLiveData<ErrorModel> {

        return repo.mFailureError()
    }

    fun getProfile(tokens: String) {

        repo.getProfileData(tokens)
    }

    //status observer
    fun mSucessfulGetProfile(): MutableLiveData<GetProfileModel> {
        return repo.mSucessfulGetProfile()
    }

    //error observer
    fun errorGetProfile(): MutableLiveData<ErrorModel> {
        return repo.errorGetProfile()
    }

    //************Clear Cart
    fun clearCart() {
        repo.clearCart()

    }

    fun mSucessFulClearCart(): MutableLiveData<String> {
        return repo.mSucessFulClearCart()
    }

    fun mErrorClearCart(): MutableLiveData<String> {
        return repo.mErrorClearCart()
    }
}
