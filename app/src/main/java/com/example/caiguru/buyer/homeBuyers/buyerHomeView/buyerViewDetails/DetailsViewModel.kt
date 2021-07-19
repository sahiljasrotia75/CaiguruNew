package com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerViewDetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerAddAddress.AddAddressModel
import com.example.caiguru.buyer.chooseSellers.chooseSellerCreditsPayForSingleList.PayCreditsResultModel
import com.example.caiguru.buyer.homeBuyers.buyerHomeView.buyerHomeViews.ViewDetailModel
import com.example.caiguru.buyer.homeBuyers.homeBuyer.HomeModel

class DetailsViewModel(application: Application) : AndroidViewModel(application) {

    var mRepo: DetailsRepository = DetailsRepository(application)

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





}
