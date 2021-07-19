package com.example.caiguru.seller.sellerOrder.orderRejected

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class OrderRejectedViewModel(application: Application) : AndroidViewModel(application) {
    fun changeRequestStatus(
        reqId: String,
        nameOfReason: String,
        Rejectstatus: String
    ) {

        repo.changeRequestStatus(reqId,nameOfReason,Rejectstatus)
    }

    var repo: OrderRejectedRepo = OrderRejectedRepo(application)

    //get back data
    fun mSucessfulRejectedRequest(): MutableLiveData<String> {

        return repo.mSucessfulRejectedRequest()
    }

    fun mFailureRejectedRequest(): MutableLiveData<String> {

        return repo.mFailureRejectedRequest()
    }

}
