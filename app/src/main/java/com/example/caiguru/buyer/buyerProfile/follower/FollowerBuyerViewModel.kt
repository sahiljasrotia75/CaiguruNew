package com.example.caiguru.buyer.buyerProfile.follower

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class FollowerBuyerViewModel(application: Application) : AndroidViewModel(application) {
    var mrepo = FollowerBuyerRepository(application)

    fun setFollower(page: String) {

        mrepo.setFollower(page)
    }

    fun mSucessfulFollower(): MutableLiveData<ArrayList<FollowUnfollowModel>> {

        return mrepo.mSucessfulFollower()
    }

    fun mFailureFollower(): MutableLiveData<String> {

        return mrepo.mFailureFollower()
    }
    //************************followunfollow webservice**********************//

    fun setFollowUnfollow(
        list: FollowUnfollowModel,
        position: Int,
        followed: String
    ) {

        mrepo.setFollowUnfollow(list, position,followed)
    }

}
