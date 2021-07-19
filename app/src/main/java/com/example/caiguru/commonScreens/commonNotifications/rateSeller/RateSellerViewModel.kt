package com.example.caiguru.commonScreens.commonNotifications.rateSeller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel

class RateSellerViewModel(application: Application) : AndroidViewModel(application) {


    var mrepo = RateSellerRepository(application)

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

    fun AdminCompleteListRateSeller(
        reqId: String,
        listType: String,
        comment: String,
        rating: String
    ) {

        mrepo.AdminCompleteListRateSeller(reqId,listType,comment,rating)
    }

    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }


}
