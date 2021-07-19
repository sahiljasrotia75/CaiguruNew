package com.example.caiguru.commonScreens.commonNotifications.commonNotificationConfirmation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerProfile.buyerMyOrder.OrderModel
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel

class NotificationConfirmationViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = NotificationConfirmationRepository(application)

    fun getOrderDetails(reqId: String, listType: String) {

        mrepo.getOrderDetails(reqId, listType)
    }

    fun mSucessfulshopListData(): MutableLiveData<OrderModel> {

        return mrepo.mSucessfulshopListData()
    }

    fun mFailureShopList(): MutableLiveData<String> {

        return mrepo.mFailureShopList()
    }


    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        mrepo.notificationRead(notificationId)
    }


    //set confirm cancellation order
    fun SetAgreeCancelledOrder(data: NotificationModel) {

        mrepo.SetAgreeCancelledOrder(data)
    }

    fun mSucessfulCancellOrder(): MutableLiveData<String> {

        return mrepo.mSucessfulCancellOrder()
    }

    fun mFailureCancelOrder(): MutableLiveData<String> {

        return mrepo.mFailureCancelOrder()
    }

}
