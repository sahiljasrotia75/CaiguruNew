package com.example.caiguru.commonScreens.earnCreditsConvert

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class CreditConvertViewModel(application: Application) : AndroidViewModel(application) {

    var repo: CreditConvertRepository = CreditConvertRepository(application)

    fun cashRequest(totalCredits: String) {

        repo.cashRequest(totalCredits)
    }

    //status observer
    fun mSucessfulCashConvert(): MutableLiveData<String> {
        return repo.mSucessfulCashConvert()
    }

    fun mFailure(): MutableLiveData<String> {
        return repo.mFailure()
    }
    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        repo.notificationRead(notificationId)
    }

}
