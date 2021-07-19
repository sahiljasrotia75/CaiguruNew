package com.example.caiguru.buyer.buyerProfile.followings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.buyer.buyerProfile.follower.FollowUnfollowModel

class FollowingBuyerViewModel(application: Application) : AndroidViewModel(application) {

    var mrepo = FollowingBuyerRepository(application)

    fun setFollowing(page: Int) {

        mrepo.setFollowing(page.toString())
    }

    fun mSucessfulFollowing(): LiveData<ArrayList<FollowUnfollowModel>> {
        return mrepo.mSucessfulFollowing()
    }

    fun mFailure(): MutableLiveData<String> {

        return  mrepo.mFailure()
    }

    fun setFollowUnfollow(
        list: FollowUnfollowModel,
        position: Int,
        followed: String
    ) {

        mrepo.setFollowUnfollow(list, position,followed)
    }

}
