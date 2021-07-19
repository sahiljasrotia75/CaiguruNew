package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerAddAddress.AddAddressModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel

class HomeViewViewModel(application: Application) : AndroidViewModel(application) {

    var mRepo: HomeViewRepository = HomeViewRepository(application)

    //send the data
    fun getFeedview(
        data: HomeModel,
        addressData: AddAddressModel
    ) {
        mRepo.getFeedview(data,addressData)

    }

    fun mSuccessfulViewDetail(): MutableLiveData<ViewDetailModel> {


        return mRepo.mSuccessfulViewDetail()
    }

    fun mError(): MutableLiveData<String> {

        return mRepo.mError()
    }

    //******************purchase api
    fun purchaseList(
        jsonProdcutStringArray: String,
        addressJSonFormat: String,
        allGlobalData: ViewDetailModel,
        credits: Double
    ) {
        mRepo.purchaseList(jsonProdcutStringArray, addressJSonFormat, allGlobalData, credits)

    }

    fun mSucessfulBuyList(): MutableLiveData<PayCreditsResultModel> {
        return mRepo.mSucessfulBuyList()
    }

    fun mErrorBuyList(): MutableLiveData<String> {
        return mRepo.mErrorBuyList()
    }
    //*****************reportLIst
    fun reportList(reason: String, data: HomeModel) {

        mRepo.reportList(reason,data)
    }

    fun mFailureReposrtlist(): MutableLiveData<String> {

        return mRepo.mFailureReposrtlist()
    }

    fun mSucessfulREportList(): MutableLiveData<String> {

        return mRepo.mSucessfulREportList()
    }
}
