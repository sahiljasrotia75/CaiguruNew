package com.example.caiguru.commonScreens.commonNotifications.commonNotification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class CommonNotificationViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = CommonNotificationRepository(application)

    fun mSucessfulNotification(): MutableLiveData<ArrayList<NotificationModel>> {
        return mrepo.mSucessfulNotification()
    }

    fun mFailure(): MutableLiveData<String> {
        return mrepo.mFailure()
    }

    fun getNotification(page: Int) {

        mrepo.getNotification(page.toString())
    }


}
