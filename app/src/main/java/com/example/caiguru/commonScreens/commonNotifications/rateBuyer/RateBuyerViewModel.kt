package com.example.caiguru.commonScreens.commonNotifications.rateBuyer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel

class RateBuyerViewModel(application: Application) : AndroidViewModel(application) {


    var mrepo = RateBuyerRepository(application)

    fun setRatingData(
        notificationModel: NotificationModel,
        comment: String,
        rating: String
    ) {
        mrepo.setRatingData(notificationModel, comment,rating)

    }


    fun mSucessfulshopListData(): MutableLiveData<String> {

        return  mrepo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return  mrepo.mFailureShopList()
    }
    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }
}
