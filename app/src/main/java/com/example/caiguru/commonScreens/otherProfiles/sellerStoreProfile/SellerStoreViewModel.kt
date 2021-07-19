package com.example.caiguru.commonScreens.otherProfiles.sellerStoreProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerOrder.finalizeOrder.FinalizeModel
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel
import com.example.caiguru.seller.shoppingListSellers.sellerPostShoppingList.PostShoppingModel
import com.example.caiguru.seller.shoppingListSellers.shoppingListPosted.ShoppingListModel

class SellerStoreViewModel(application: Application) : AndroidViewModel(application) {
    val mrepo = SellerStoreRepository(application)


    //send the data
    fun getsellerstore(localMOdelData: FinalizeModel, setPriceFilter: String) {
        mrepo.getsellerstore(localMOdelData, setPriceFilter)

    }

    //get the data
    fun getStoreDataSucessful(): MutableLiveData<GetProfileModel> {
        return mrepo.getdata()

    }

    //error observer
    fun mFailureStoredata(): MutableLiveData<String> {

        return mrepo.errorget()
    }

    //follow or unfollow api

    fun setFollowUnfollow(data: FinalizeModel, followStatus: Int) {

        mrepo.setFollowUnfollow(data, followStatus)
    }


    //get the data
    //status true observer
    fun mSucessfullFollowUnfollow(): MutableLiveData<GetProfileModel> {
        return mrepo.mSucessfullFollowUnfollow()
    }

    //error observer
    fun mFailureeFollowUnfollow(): MutableLiveData<String> {

        return mrepo.mFailureeFollowUnfollow()
    }

    fun blockedUser(data: SellerApprovalModel, type: String) {
        mrepo.blockedUser(data, type)

    }

    //get data
    fun getSucessfulBlockedUser(): MutableLiveData<String> {
        return mrepo.getSucessfulBlockedUser()
    }

    //error observer
    fun errorgetBlockedUser(): MutableLiveData<String> {

        return mrepo.errorgetBlockedUser()
    }

    //**************search api
    fun getSearchData(
        search: String,
        model: FinalizeModel,
        page: Int,
        setPriceFilter: String,
        searchUsedProds: String
    ) {
        mrepo.getSearchData(search, model, page.toString(), setPriceFilter,searchUsedProds)
    }

    //status true observer
    fun mScucessfulSearchProdcut(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return mrepo.mScucessfulSearchProdcut()
    }

    //error observer
    fun getErrorSearch(): MutableLiveData<String> {

        return mrepo.getErrorSearch()
    }

    //*************Load more api****************//
    fun LoadMoreData(
        data: ShoppingListModel,
        localMOdelData: FinalizeModel,
        setPriceFilter: String
    ) {

        mrepo.LoadMoreData(data, localMOdelData, setPriceFilter)
    }

    //status true observer
    fun mSucessfulLoadMore(): MutableLiveData<ShoppingListModel> {
        return mrepo.mSucessfulLoadMore()
    }

    //error observer
    fun mFailureLoadMore(): MutableLiveData<String> {

        return mrepo.mFailureLoadMore()
    }


    //***************saved seller cart product
    fun save_seller_cart(sellerId: String, jsonProductArray: String) {
        mrepo.save_seller_cart(sellerId, jsonProductArray)

    }


    fun mSucessfulSavedCart(): MutableLiveData<String> {
        return mrepo.mSucessfulSavedCart()
    }

    fun mFailureSavedCart(): MutableLiveData<String> {

        return mrepo.mFailureSavedCart()
    }

    //get the seller cart
    fun get_seller_cart(sellerId: String) {
        mrepo.get_seller_cart(sellerId)

    }

    fun mSucessfulCartData(): MutableLiveData<ArrayList<PostShoppingModel>> {
        return mrepo.mSucessfulCartData()
    }

    fun mFailureCart(): MutableLiveData<String> {

        return mrepo.mFailureCart()
    }
}
