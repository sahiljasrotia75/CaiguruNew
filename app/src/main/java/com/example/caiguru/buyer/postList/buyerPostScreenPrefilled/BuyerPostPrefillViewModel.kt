package com.example.caiguru.buyer.postList.buyerPostScreenPrefilled

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.WebServicesShoppingModel

class BuyerPostPrefillViewModel(application: Application) : AndroidViewModel(application) {

    var postRepository: BuyerPostPrefillRepo =
        BuyerPostPrefillRepo(
            application
        )

    fun editListUpdate(
        adapterShoppingModel: PostShoppingModel,
        adapterShoppingPosition: Int,
        isPrefilling: Boolean,
        nameEdited: String
    ) {
        postRepository.editListUpdate(
            adapterShoppingModel,
            adapterShoppingPosition,
            isPrefilling,
            nameEdited
        )

    }

    fun ShoppingListData(
        model: List<PostShoppingModel>,
        isPrefilling: Boolean
    ) {
        postRepository.ShoppingListData(model, isPrefilling)

    }

    fun sendData(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return postRepository.sendData()
    }

    fun createShoppingList(
        listingname: String,
        categoryModelList: String,
        deliveryZoneJsonList: String,
        daysTimeZoneJsonArray: String,
        shoppingProductListJSonArray: String,
        listId: String
    ) {
        postRepository.createShoppingList(
            listingname,
            categoryModelList,
            deliveryZoneJsonList,
            daysTimeZoneJsonArray,
            shoppingProductListJSonArray,
            listId
        )
    }


    //send the shoppinglist
    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
        return postRepository.mSucessfulCreateShoppingList()
    }

    fun mFailureShoppingError(): MutableLiveData<String> {
        return postRepository.mFailureShoppingError()
    }

    //*********************** Switch Active ***************************** //

    fun switch_setting(switch: String, type: String) {

        return postRepository.switch_setting(switch, type)
    }

    fun getdata(): MutableLiveData<String> {
        return postRepository.getdata()
    }


    fun errorget(): MutableLiveData<ErrorModel> {
        return postRepository.errorget()
    }

    fun seller_active_status(seller_active_status: String) {
        return postRepository.seller_active_status(seller_active_status)
    }

    fun getsellerStatus(): MutableLiveData<ErrorModel> {
        return postRepository.getsellerStatus()
    }

    fun errorgetStatus(): MutableLiveData<ErrorModel> {
        return postRepository.errorgetStatus()
    }

    fun updateCredits(size: Int) {

        postRepository.updateCredits(size)
    }

    fun getCreditDeduction(): String {
        return postRepository.getCreditDeduction()
    }
}
