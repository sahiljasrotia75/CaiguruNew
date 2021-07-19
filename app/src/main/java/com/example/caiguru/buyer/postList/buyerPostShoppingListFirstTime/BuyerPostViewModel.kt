package com.example.caiguru.buyer.postList.buyerPostShoppingListFirstTime

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.commonScreens.loginScreen.ErrorModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel

class BuyerPostViewModel( application: Application): AndroidViewModel(application) {

    var postRepository : BuyerPostRepository =
        BuyerPostRepository(
            application
        )
    fun editListUpdate(adapterShoppingModel: PostShoppingModel, adapterShoppingPosition: Int) {
        postRepository.editListUpdate(adapterShoppingModel, adapterShoppingPosition)
    }

    fun ShoppingListData(model: PostShoppingModel) {
        postRepository.ShoppingListData(model)
    }

    fun sendData(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return postRepository.sendData()
    }

//    fun createShoppingList(
//        listingname: String,
//        categoryModelList: String,
//        deliveryZoneJsonList: String,
//        daysTimeZoneJsonArray: String,
//        shoppingProductListJSonArray: String,
//        listId: String,
//        creditsToDeduct:Double
//    ) {
//        postRepository.createShoppingList(listingname,categoryModelList,deliveryZoneJsonList,daysTimeZoneJsonArray,shoppingProductListJSonArray,listId, creditsToDeduct)
//    }
//
//    //send the shoppinglist
//    fun mSucessfulCreateShoppingList(): MutableLiveData<ArrayList<WebServicesShoppingModel>> {
//        return postRepository.mSucessfulCreateShoppingList()
//    }
//    fun mFailureShoppingError(): MutableLiveData<String> {
//        return postRepository.mFailureShoppingError()
//    }

    //*********************** Switch Active ***************************** //

    fun switch_setting(switch: String, type: String) {

        return postRepository.switch_setting(switch,type)
    }

    fun getdata(): MutableLiveData<String> {
        return postRepository.getdata()
    }


    fun errorget():MutableLiveData<ErrorModel>{
        return postRepository.errorget()
    }

    fun seller_active_status(seller_active_status: String) {
        return postRepository.seller_active_status(seller_active_status)
    }

    fun getsellerStatus(): MutableLiveData<ErrorModel> {
        return postRepository.getsellerStatus()
    }

    fun errorgetStatus():MutableLiveData<ErrorModel>{
        return postRepository.errorgetStatus()
    }

    fun clearData() {

        postRepository.clearData()
    }




}
