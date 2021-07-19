package com.example.caiguru.commonScreens.commonNotifications.disputeAndResolution.refuteSellerSideNotification

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel

class ResolutionViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = ResolutionRepository(application)

    fun getOrderDetails(reqId: String, listType: String) {

        mrepo.getOrderDetails(reqId, listType)
    }

    fun mSucessfulshopListData(): MutableLiveData<RefuteModel> {

        return mrepo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mrepo.mFailureShopList()
    }

    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }

    //**************accept disputes api************//
    fun acceptDisputes(sourceId: String, listType: String) {

        mrepo.acceptDisputes(sourceId, listType)
    }

    //failure or sucessful
    fun mSucessfulAcceptDisputes(): MutableLiveData<String> {

        return mrepo.mSucessfulAcceptDisputes()
    }

    fun mFailureAcceptDisputes(): MutableLiveData<String> {

        return mrepo.mFailureAcceptDisputes()
    }

    fun refuteReason(reasonRefute: String, notificationData: NotificationModel) {

        mrepo.refuteReason(reasonRefute,notificationData)
    }
    //

    fun mSucessfulRefuteReason(): MutableLiveData<String> {

        return mrepo.mSucessfulRefuteReason()
    }

    fun mFailureRefuteReason(): MutableLiveData<String> {

        return mrepo.mFailureRefuteReason()
    }

}
