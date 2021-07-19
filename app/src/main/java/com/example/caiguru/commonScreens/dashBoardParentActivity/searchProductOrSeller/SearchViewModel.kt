package com.example.caiguru.commonScreens.dashBoardParentActivity.searchProductOrSeller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel
import com.example.caiguru.seller.shoppingListSellers.deliveryZoneSeller.DeliveryZoneModel

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = SearchRepository(application)

//set the data
    fun getSearchData(
    search: String,
    address: DeliveryZoneModel,
    searchType: String,
    page: Int
) {
        mrepo.getSearchData(search,address,searchType,page)

    }

    //*********get the data of search
    //status true observer
    fun mFailureSearch(): MutableLiveData<String> {
        return  mrepo.mFailureSearch()
    }

    //error observer
    fun mSucessFulSearch(): MutableLiveData<ArrayList<HomeModel>> {

        return  mrepo.mSucessFulSearch()
    }
    //*****************reportLIst
    fun reportList(reason: String, data: HomeModel) {

        mrepo.reportList(reason,data)
    }

    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mrepo.mFailureReposrtlist()
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mrepo.mSucessfulREportList()
    }

}
