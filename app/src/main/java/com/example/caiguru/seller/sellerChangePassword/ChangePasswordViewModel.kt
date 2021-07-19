package com.example.caiguru.seller.sellerChangePassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {
    fun updatePassword(password: String, oldPassword: String) {
        repo.updatePassword(password, oldPassword)
    }


    var repo: ChangePasswordRepo = ChangePasswordRepo(application)


    fun getUpdatePassword(): MutableLiveData<ErrorModel> {
        return repo.getsellerStatus()
    }

    fun getError(): MutableLiveData<ErrorModel> {
        return repo.errorgetStatus()
    }

}
