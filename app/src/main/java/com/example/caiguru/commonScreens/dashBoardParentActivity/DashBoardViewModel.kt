package com.example.caiguru.commonScreens.dashBoardParentActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.commonNotifications.commonNotification.NotificationModel
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.WebServicesShoppingModel

class DashBoardViewModel(application: Application) : AndroidViewModel(application) {
    var repo = DashBoardRepository(application)


    fun getProfile(token: String) {
        repo.getProfileData(token)
    }

    fun createShoppingList(
        listingname: String,
        categoryModelList: String,
        deliveryZoneJsonList: String,
        daysTimeZoneJsonArray: String,
        shoppingProductListJSonArray: String,
        listId: String,
        creditsToDeduct: Double
    ) {
        repo.createShoppingList(
            listingname,
            categoryModelList,
            deliveryZoneJsonList,
            daysTimeZoneJsonArray,
            shoppingProductListJSonArray,
            listId,
            creditsToDeduct
        )
    }


    //get the data
    fun getdataProfile(): MutableLiveData<GetProfileModel> {
        return repo.getdata()
    }

    //error observer
    fun errorgetProfile(): MutableLiveData<ErrorModel> {
        return repo.errorgetProfile()
    }

    //get  buyer notification data
    fun getHomePageBuyerNotification(token: String) {

        repo.getHomePageNotification(token)
    }

    //seller notification
    fun getHomeSellerNotification(token: String) {

        repo.getHomeSellerNotification(token)
    }

    //set the data of notification
    fun mSucessfulNotification(): MutableLiveData<ArrayList<NotificationModel>> {
        return repo.mSucessfulNotification()
    }

    fun mFailure(): MutableLiveData<String> {
        return repo.mFailure()
    }

    //*********************notificationRead******************//
    fun notificationRead(notificationId: String) {

        repo.notificationRead(notificationId)
    }

    //send the shoppinglist
    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
        return repo.mSucessfulCreateShoppingList()
    }

    fun mFailureShoppingError(): MutableLiveData<String> {
        return repo.mFailureShoppingError()
    }

    //get the data
    fun updatedAddress(model: DeliveryZoneModel) {


        return repo.updatedAddress(model)
    }

    //get address
    fun mUpdateAddress(): MutableLiveData<DeliveryZoneModel> {


        return repo.mUpdateAddress()
    }

    fun failueAddress(): MutableLiveData<String> {
        return repo.failueAddress()
    }

    fun logoutBannedUser(): MutableLiveData<ErrorModel> {
        return repo.logoutBannedUser()
    }

    fun hitBackGroundService() {

        repo.hitBackGroundService()
    }
}
