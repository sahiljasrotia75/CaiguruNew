package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.raiseADsiputeBuyerSideNotification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel

class RaiseDisputesViewModel(application: Application) : AndroidViewModel(application) {


    var mrepo = RaiseDisputesRepository(application)


    fun mSucessfulshopListData(): MutableLiveData<String> {

        return mrepo.mSucessfulshopListData()
    }


    fun mFailureShopList(): MutableLiveData<String> {

        return mrepo.mFailureShopList()

    }

    fun setDisputeData(
        model: NotificationModel,
        name: String,
        number: String,
        reason: String
    ) {
        mrepo.setDisputeData(model, name, number, reason)

    }

}
