package com.example.caiguru.commonScreens.registerActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import java.io.File

class RegisterViewModel(application: Application):AndroidViewModel(application) {
    fun profileImage(uri: File) {
        mrepo.setImage(uri)
    }


    val mrepo=RegisterRepository(application)



    //status true observer
    fun getdataSucessful(): MutableLiveData<RegisterModel> {
        return mrepo.getdata()
    }

    //error observer
    fun errorData(): MutableLiveData<String> {

        return mrepo.errorget()
    }
//*************email registeration
    fun check_email_valid(email: String) {
        mrepo. check_email_valid(email)

    }
    fun mSucessfulEmailRegisteration(): MutableLiveData<String> {
        return  mrepo. mSucessfulEmailRegisteration()
    }

    //error observer
    fun mFailureEmailRegisteration(): MutableLiveData<String> {

        return  mrepo. mFailureEmailRegisteration()
    }
}
