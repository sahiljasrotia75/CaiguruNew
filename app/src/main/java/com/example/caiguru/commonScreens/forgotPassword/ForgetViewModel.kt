package com.example.caiguru.commonScreens.forgotPassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel

class ForgetViewModel(application: Application) : AndroidViewModel(application) {


    var forgetRepository: ForgetRepository = ForgetRepository(application)

    fun setEmail(email: String) {
        forgetRepository.setEmailData(email)
    }

    //get the data
    fun SucessfulData(): MutableLiveData<ForgotModel> {
        return  forgetRepository.sucessget()
    }
    //error observer
    fun errorget(): MutableLiveData<ErrorModel> {
        return forgetRepository.errorget()
    }

}
