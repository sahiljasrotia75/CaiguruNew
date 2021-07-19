package com.example.caiguru.buyer.homeBuyers.homeBuyer.viewTagMoreUser

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class ViewMoreViewModel(application: Application) : AndroidViewModel(application) {

    var mRepo: ViewMoreRepository = ViewMoreRepository(application)

    fun get_shared_users(postId: String, sharedBy: String) {
        mRepo.get_shared_users(postId, sharedBy)

    }

    fun mSuccessfulgetTagUser(): MutableLiveData<ArrayList<HomeViewMoreModel>> {


        return mRepo.mSuccessfulgetTagUser()
    }

    fun mError(): MutableLiveData<String> {

        return mRepo.mError()
    }


}
