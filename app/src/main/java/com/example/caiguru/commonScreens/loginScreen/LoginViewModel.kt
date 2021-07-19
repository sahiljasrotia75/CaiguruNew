package com.example.caiguru.commonScreens.loginScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModel(application: Application):AndroidViewModel(application) {
    var loginRepository : LoginRepository = LoginRepository(application)

    fun setLogData(email: String, password: String) {
        loginRepository.setLoginData(email,password)
    }

    fun getdata(): MutableLiveData<String> {
        return loginRepository.getdata()
    }


    fun errorget():MutableLiveData<ErrorModel>{
        return loginRepository.errorget()
    }

    fun loginUsingFacebook(
        socialId: String?,
        url: String,
        userName: String,
        userEmail: String
    ) {
        loginRepository.loginUsingFacebook(socialId, url, userName, userEmail)
    }

    fun facebookObserver(): LiveData<ModelFacebook> {
      return  loginRepository.observeFacebook()
    }

}
