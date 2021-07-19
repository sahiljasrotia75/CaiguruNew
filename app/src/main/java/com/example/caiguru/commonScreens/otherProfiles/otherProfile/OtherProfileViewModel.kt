package com.example.caiguru.commonScreens.otherProfiles.otherProfile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.caiguru.seller.homeSeller.GetProfileModel
import com.example.caiguru.seller.sellerOrder.sellerPendingApprovals.SellerApprovalModel

class OtherProfileViewModel(application: Application) : AndroidViewModel(application) {
    val mrepo = OtherProfileRepository(application)


    //send the data
    fun getOtherProfileData(
        data: SellerApprovalModel,
        type: String
    ) {

        mrepo.getOtherProfileData(data,type)
    }

    //get the data
    fun getdataSucessful(): MutableLiveData<GetProfileModel> {
        return mrepo.getdata()

    }

    //error observer
    fun errorData(): MutableLiveData<String> {

        return mrepo.errorget()
    }

    //follow or unfollow api

    fun setFollowUnfollow(data: SellerApprovalModel, followStatus: Int) {

        mrepo.setFollowUnfollow(data,followStatus)
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
        mrepo.blockedUser(data,type)

    }
    //get data
    fun getSucessfulBlockedUser(): MutableLiveData<String> {
        return  mrepo.getSucessfulBlockedUser()
    }

    //error observer
    fun errorgetBlockedUser(): MutableLiveData<String> {

        return  mrepo.errorgetBlockedUser()
    }
}
